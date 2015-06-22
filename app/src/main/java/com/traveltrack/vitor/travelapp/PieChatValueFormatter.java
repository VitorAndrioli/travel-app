package com.traveltrack.vitor.travelapp;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class PieChatValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;
    private Currency currency;
    private double total;

    public PieChatValueFormatter(Currency currency, double total) {
        mFormat = new DecimalFormat("###,###,##0.00");
        this.currency = currency;
        this.total = total;
    }

    @Override
    public String getFormattedValue(float value) {
        if (value/total <= 0.04) {
            return "";
        } else {
            return this.currency.getSymbol() + " " + mFormat.format(value);
        }
    }
}
