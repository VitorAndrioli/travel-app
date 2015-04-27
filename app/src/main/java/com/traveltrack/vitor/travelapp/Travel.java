package com.traveltrack.vitor.travelapp;

import android.net.Uri;

import com.orm.SugarRecord;

import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Created by vitor on 18/04/15.
 */
public class Travel extends SugarRecord<Travel> {
    String name;
    Date beginning;
    Date end;
    String image_uri;

    public Travel() {

    }

    public Travel(String name) {
        this.name = name;
    }

    public Travel(String name, Date beginning, Date end) {
        this.name = name;
        this.beginning = beginning;
        this.end = end;
    }

    public Travel(String name, String image_uri) {
        this.name = name;
        this.image_uri = image_uri;
    }

    public Travel(String name, Date beginning, Date end, String image_uri) {
        this.name = name;
        this.beginning = beginning;
        this.end = end;
        this.image_uri = image_uri;
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = Expense.find(Expense.class, "travel = ?", this.getId().toString());

        return expenses;
    }

    public List<TravelUser> getParticipants() {
        List<TravelUser> users = TravelUser.find(TravelUser.class, "travel = ?", this.getId().toString());

        return users;
    }

    public double getTotalSpent() {
        double total = 0;
        List<Expense> expenses = Expense.find(Expense.class, "travel = ?", this.getId().toString());

        for (int i=0; i<expenses.size(); i++)
            total += expenses.get(i).value;

        return total;
    }

    public double getTotalSpentByCategory(Category category) {
        double category_total = 0;
        List<Expense> expenses = Expense.find(Expense.class, "travel = ? and category = ?", this.getId().toString(), category.getId().toString());

        for (int i=0; i<expenses.size(); i++)
            category_total += expenses.get(i).value;

        return category_total;
    }

}
