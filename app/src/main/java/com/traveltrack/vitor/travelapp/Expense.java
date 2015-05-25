package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.Date;

public class Expense extends SugarRecord<Expense> {
    double value;
    String description;
    Date date;


    Category category;
    Travel travel;
    User user;

    public Expense() {

    }

    public Expense (double value, String description, Category category, User user, Travel travel, Date date) {
        this.value = value;
        this.description = description;
        this.date = date;
        this.category = category;
        this.user = user;
        this.travel = travel;
    }
}
