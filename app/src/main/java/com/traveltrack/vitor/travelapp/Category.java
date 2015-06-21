package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.List;

public class Category extends SugarRecord<Category> {
    private String name;
    private String portName;
    private String uri;

    public Category() {

    }

    public Category(String name, String portName, String uri) {
        this.setName(name);
        this.setPortName(portName);
        this.setURI(uri);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortName() {
        return this.portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getName() {
        return this.name;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public String getURI() {
        return this.uri;
    }

    public float getExpenses(User user, Travel travel) {
        float total = 0;

        List<Expense> expenses = Expense.find(Expense.class,
                "user = ? and category = ? and travel = ?",
                user.getId().toString(), this.getId().toString(), travel.getId().toString());

        for (int i=0; i<expenses.size(); i++) {
            total += expenses.get(i).getValue();
        }

        return total;
    }


}
