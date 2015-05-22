package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.List;

public class User extends SugarRecord<User> {
    String name;
    String email;
    String password;

    public User() {

    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public List<TravelUser> getTravels() {
        List<TravelUser> userTravels = TravelUser.find(TravelUser.class, "user = ?", this.getId().toString());

        return userTravels;
    }

    public float getExpensesByCategory(Category category, Travel travel) {
        float total = 0;

        List<Expense> expenses = Expense.find(Expense.class, "user = ? and category = ? and travel = ?",
                this.getId().toString(), category.getId().toString(), travel.getId().toString());

        for (int i=0; i<expenses.size(); i++)
            total += expenses.get(i).value;

        return total;
    }
}
