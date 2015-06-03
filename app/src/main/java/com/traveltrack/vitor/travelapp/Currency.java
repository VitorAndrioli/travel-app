package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

public class Currency extends SugarRecord<Currency>{
    private String name;
    private String symbol;

    public Currency() {}

    public Currency(String name, String symbol) {
        this.setName(name);
        this.setSymbol(symbol);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() { return this.symbol; }
}
