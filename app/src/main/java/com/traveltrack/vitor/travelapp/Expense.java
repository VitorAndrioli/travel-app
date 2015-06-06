package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.text.DecimalFormat;
import java.util.Date;

public class Expense extends SugarRecord<Expense> {
    private double value;
    private String description;
    private Date date;

    private Category category;
    private Travel travel;
    private User user;

    public Expense() {

    }

    public Expense (double value, String description, Category category, User user, Travel travel, Date date) {
        this.setValue(value);
        this.setDescription(description);
        this.setDate(date);
        this.setCategory(category);
        this.setUser(user);
        this.setTravel(travel);
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return this.getTravel().getCurrency().getSymbol() + " " + df.format( this.getValue() );
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public Travel getTravel() {
        return this.travel;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

}
