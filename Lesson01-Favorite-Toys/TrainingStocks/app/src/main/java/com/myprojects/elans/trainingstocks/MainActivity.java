package com.myprojects.elans.trainingstocks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String STOCK_SYMBOL = "com.myprojects.elans.STOCK";

    private SharedPreferences stockSymbolEntered;

    private TableLayout stockTableScrollView;

    private EditText stockSymbolEditText;

    private Button enterStockSymbolButton;
    private Button deleteStockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockSymbolEntered = getSharedPreferences("stockList",MODE_PRIVATE);

        stockTableScrollView = (TableLayout)findViewById(R.id.stockSymbolScrollView);
        stockSymbolEditText = (EditText)findViewById(R.id.enterSymbolEditView);
        enterStockSymbolButton = (Button)findViewById(R.id.enterStockSymbolButton);
        deleteStockButton = (Button)findViewById(R.id.deleteStockButton);

        enterStockSymbolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stockSymbolEditText.getText().length() > 0) {
                    saveStockSymbol(stockSymbolEditText.getText().toString());
                    stockSymbolEditText.setText("");
                    //enforce close the keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(stockSymbolEditText.getWindowToken(),0);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.invalid_stock_symbol);
                    builder.setPositiveButton(R.string.ok,null);
                    builder.setMessage(R.string.missing_stock_symbol);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        deleteStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllStocks();

                SharedPreferences.Editor preferenceEditor = stockSymbolEntered.edit();
                preferenceEditor.clear();
                preferenceEditor.apply();
            }
        });

        updateSavedStockList(null);
    }

    private void deleteAllStocks(){
        stockTableScrollView.removeAllViews();
    }

    private void updateSavedStockList(String newStockSymbol){
        String [] stocks = stockSymbolEntered.getAll().keySet().toArray(new String[0]);

        Arrays.sort(stocks,String.CASE_INSENSITIVE_ORDER);

        if(newStockSymbol != null){
            insertStockInScrollView(newStockSymbol, Arrays.binarySearch(stocks,newStockSymbol));
        } else {
            for(int i=0;i<stocks.length;i++){
                insertStockInScrollView(stocks[i],i);
            }
        }

    }

    private void saveStockSymbol(String newStock){
        String isTheStockNew = stockSymbolEntered.getString(newStock,null);
        SharedPreferences.Editor preferenceEditor = stockSymbolEntered.edit();
        preferenceEditor.putString(newStock,newStock);
        preferenceEditor.apply();

        if(isTheStockNew == null){
            updateSavedStockList(newStock);
        }
    }

    private void insertStockInScrollView(String stock, int arrayIndex){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newStockRow = inflater.inflate(R.layout.stock_quote_row,null);
        TextView newStockTextView = (TextView)newStockRow.findViewById(R.id.stockSymbolTextView);
        newStockTextView.setText(stock);

        Button stockQuoteButton = (Button)newStockRow.findViewById(R.id.stockQuoteButton);
        Button quoteFromWebButton = (Button)newStockRow.findViewById(R.id.quoteFromWebButton);

        stockQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow tableRow = (TableRow)view.getParent();
                TextView stockTextView = (TextView)tableRow.findViewById(R.id.stockSymbolTextView);
                String stockSymbol = stockTextView.getText().toString();
                Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
                intent.putExtra(STOCK_SYMBOL, stockSymbol);
                startActivity(intent);
            }
        });

        quoteFromWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow tableRow = (TableRow)view.getParent();
                TextView stockTextView = (TextView)tableRow.findViewById(R.id.stockSymbolTextView);
                String stockSymbol = stockTextView.getText().toString();

                String stockUrl = getString(R.string.yahoo_stock_url)+stockSymbol;
                Intent getStockWebpage = new Intent(Intent.ACTION_VIEW, Uri.parse(stockUrl));

                startActivity(getStockWebpage);
            }
        });

        stockTableScrollView.addView(newStockRow,arrayIndex);

    }
}
