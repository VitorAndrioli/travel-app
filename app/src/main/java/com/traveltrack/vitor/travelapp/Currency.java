package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

public class Currency extends SugarRecord<Currency>{
    private String name;
    private String code;
    private String symbol;

    public Currency() {}

    public Currency(String name, String code, String symbol) {
        this.setName(name);
        this.setCode(code);
        this.setSymbol(symbol);
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() { return this.symbol; }
}
