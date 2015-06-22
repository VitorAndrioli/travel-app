package com.traveltrack.vitor.travelapp;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class BarChartValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;
    private Currency currency;

    public BarChartValueFormatter(Currency currency) {
        mFormat = new DecimalFormat("###,###,##0.00");
        this.currency = currency;
    }

    @Override
    public String getFormattedValue(float value) {
        return this.currency.getSymbol() + " " + mFormat.format(value);

    }
}
