package com.traveltrack.vitor.orcamentour;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class DataValueFormater implements ValueFormatter {

    private DecimalFormat mFormat;

    public DataValueFormater() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value) {

        if(value > 0)
            return mFormat.format(value);
        else
            return "";
    }
}