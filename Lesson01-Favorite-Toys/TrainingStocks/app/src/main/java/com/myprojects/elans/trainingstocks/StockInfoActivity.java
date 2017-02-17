package com.myprojects.elans.trainingstocks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by elans on 2/12/2017.
 */
public class StockInfoActivity extends Activity{

    private static final String TAG = "STOCKQUOTE";

    TextView companyNameTextView;
    TextView lastTradePriceonlyTextView;
    TextView daysLowTextView;
    TextView daysHighTextView;
    TextView yearLowTextView;
    TextView yearHighTextView;
    TextView changeTextView;
    TextView daysRangeTextView;

    static final String KEY_ITEM = "quote";
    static final String KEY_NAME = "name";
    static final String KEY_YEAR_LOW    = "yearLow";
    static final String KEY_YEAR_HIGH   = "yearHigh";
    static final String KEY_DAYS_LOW    = "daysLow";
    static final String KEY_DAYS_HIGH   = "daysHigh";
    static final String KEY_CHANGE      = "change";
    static final String KEY_IDAYS_RANGE = "daysRange";
    static final String KEY_LAST_TRADE_PRICE_ONLY = "lastTradePriceOnly";

    String companyName = "";
    String lastTradePriceonly = "";
    String daysLow = "";
    String daysHigh = "";
    String yearLow = "";
    String yearHigh = "";
    String change = "";
    String daysRange = "";

    String yahooUrlFirst = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
    String yahooUrlSecond = "%22)&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);

        Intent intent = getIntent();
        String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);

        companyNameTextView = (TextView)findViewById(R.id.companyNameTextView);
        lastTradePriceonlyTextView = (TextView)findViewById(R.id.lastTradePriceOnlyTextView);
        daysLowTextView = (TextView)findViewById(R.id.daysLowTextView);
        daysHighTextView = (TextView)findViewById(R.id.daysHighTextView);
        yearLowTextView = (TextView)findViewById(R.id.yearLowTextView);
        yearHighTextView = (TextView)findViewById(R.id.yearHighTextView);
        changeTextView = (TextView)findViewById(R.id.changeTextView);
        daysRangeTextView = (TextView)findViewById(R.id.daysRangeTextView);

        Log.d(TAG, "Before Url Creation" + stockSymbol);

        final String yqlUrl =yahooUrlFirst + stockSymbol + yahooUrlSecond;

        new MyAsyncTask().execute(yqlUrl);
    }

    private class MyAsyncTask extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... args) {
            try{
                URL url = new URL(args[0]);

                URLConnection connection;
                connection = url.openConnection();

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection)connection;

                int responseCode = httpsURLConnection.getResponseCode();

                if(responseCode == HttpsURLConnection.HTTP_OK){
                    InputStream in = httpsURLConnection.getInputStream();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document dom = db.parse(in);
                    Element docElem = dom.getDocumentElement();
                    NodeList nl = docElem.getElementsByTagName("quote");

                    if(nl != null && nl.getLength() > 0){
                        for (int i=0;i<nl.getLength();i++){
                            StackInfo theStock = getStockInfo(docElem);

                            companyName = theStock.getName();
                            lastTradePriceonly = theStock.getLastTradePriceonly();
                            daysLow = theStock.getDaysLow();
                            daysHigh = theStock.getDaysHigh();
                            yearLow = theStock.getYearLow();
                            yearHigh = theStock.getYearHigh();
                            change = theStock.getChange();
                            daysRange = theStock.getDaysRange();
                        }
                    }
                }
            } catch (MalformedURLException e){
                Log.d(TAG, "Malformed URL Exception", e);
            } catch (IOException e){
                Log.d(TAG, "IOException", e);
            } catch (SAXException e){
                Log.d(TAG, "SAX Exception", e);
            } catch (ParserConfigurationException e){
                Log.d(TAG, "ParserConfigurationException", e);
            } finally {

            }


            return null;
        }

        private StackInfo getStockInfo(Element entry){
            String stockName = getTextValue(entry, "Name");
            String stockYearLow = getTextValue(entry, "YearLow");
            String stockYearHigh = getTextValue(entry, "YearHigh");
            String stockDaysLow = getTextValue(entry, "DaysLow");
            String stockDaysHigh = getTextValue(entry, "DaysHigh");
            String stockLastTradePriceOnly = getTextValue(entry, "LastTradePriceOnly");
            String stockChange = getTextValue(entry, "Change");
            String stockDaysRange = getTextValue(entry, "DaysRange");

            StackInfo theStock = new StackInfo(stockDaysLow,stockDaysHigh,stockYearLow,
                    stockYearHigh,stockChange,stockName,stockLastTradePriceOnly,
                    stockDaysRange);

            return theStock;
        }

        private String getTextValue(Element entry, String tagName){
            String tagValueToReturn = null;

            NodeList nl = entry.getElementsByTagName(tagName);

            if(nl != null && nl.getLength() > 0){
                Element element = (Element)nl.item(0);
                tagValueToReturn = element.getFirstChild().getNodeValue();
            }
            return tagValueToReturn;
        }

        protected void onPostExecute(String result){
            companyNameTextView.setText(companyName);
            yearLowTextView.setText("Year Low: "+ yearLow);
            yearHighTextView.setText("Year High: "+ yearHigh);
            daysLowTextView.setText("Days Low: "+ daysLow);
            daysHighTextView.setText("Days High: "+ daysHigh);
            lastTradePriceonlyTextView.setText("Last Price: "+ lastTradePriceonly);
            changeTextView.setText("Change: "+ change);
            daysRangeTextView.setText("Days Range: "+ daysRange);
        }
    }
}
