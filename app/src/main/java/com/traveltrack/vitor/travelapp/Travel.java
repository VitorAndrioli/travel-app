package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by vitor on 18/04/15.
 */
public class Travel extends SugarRecord<Travel> {
    String name;
    Date begining;
    Date end;

    public Travel() {

    }

    public Travel(String name) {
        this.name = name;
    }

    public List<Expense> getExpenses() {

    }

    public List<User> getParticipants() {

    }

    public double getTotalSpent() {

    }

    public double getTotalSpentByCategory(Category category) {

    }

}
