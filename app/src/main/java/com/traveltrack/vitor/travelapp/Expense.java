package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

public class Expense extends SugarRecord<Expense> {
    double value;
    String description;

    Category category;
    Travel travel;
    User user;

    public Expense() {

    }

    public Expense (double value, String description, Category category, User user, Travel travel) {
        this.value = value;
        this.description = description;
        this.category = category;
        this.user = user;
        this.travel = travel;
    }
}
