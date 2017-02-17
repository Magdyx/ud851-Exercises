package com.myprojects.elans.trainingstocks;

/**
 * Created by elans on 2/12/2017.
 */
public class StackInfo {
    private String daysLow = "";
    private String daysHigh = "";
    private String yearLow = "";
    private String yearHigh = "";
    private String change = "";
    private String name = "";
    private String lastTradePriceonly= "";
    private String daysRange = "";

    public StackInfo(String daysLow, String daysHigh, String yearLow,
                     String yearHigh, String change, String name,
                     String lastTradePriceonly, String daysRange) {
        this.daysLow = daysLow;
        this.daysHigh = daysHigh;
        this.yearLow = yearLow;
        this.yearHigh = yearHigh;
        this.change = change;
        this.name = name;
        this.lastTradePriceonly = lastTradePriceonly;
        this.daysRange = daysRange;
    }

    public String getDaysLow() {
        return daysLow;
    }

    public void setDaysLow(String daysLow) {
        this.daysLow = daysLow;
    }

    public String getDaysHigh() {
        return daysHigh;
    }

    public void setDaysHigh(String daysHigh) {
        this.daysHigh = daysHigh;
    }

    public String getYearLow() {
        return yearLow;
    }

    public void setYearLow(String yearLow) {
        this.yearLow = yearLow;
    }

    public String getYearHigh() {
        return yearHigh;
    }

    public void setYearHigh(String yearHigh) {
        this.yearHigh = yearHigh;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastTradePriceonly() {
        return lastTradePriceonly;
    }

    public void setLastTradePriceonly(String lastTradePriceonly) {
        this.lastTradePriceonly = lastTradePriceonly;
    }

    public String getDaysRange() {
        return daysRange;
    }

    public void setDaysRange(String daysRange) {
        this.daysRange = daysRange;
    }
}
