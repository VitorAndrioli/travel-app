package com.traveltrack.vitor.travelapp;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class moneyFormatter implements ValueFormatter {

    private DecimalFormat mFormat;
    private Currency currency;

    public moneyFormatter(Currency currency) {
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        this.currency = currency;
    }

    @Override
    public String getFormattedValue(float value) {
        return this.currency.getSymbol() + " " + mFormat.format(value); // append a dollar-sign
    }
}
