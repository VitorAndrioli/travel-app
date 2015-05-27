package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.List;

public class User extends SugarRecord<User> {
    private String name;
    private String email;
    private String password;

    public User() {}

    public User(String name, String email, String password) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
    }

    public List<TravelUser> getTravels() {
        List<TravelUser> userTravels = TravelUser.find(TravelUser.class, "user = ?", this.getId().toString());
        return userTravels;
    }

    public float getExpensesByCategory(Category category, Travel travel) {
        float total = 0;

        List<Expense> expenses = Expense.find(Expense.class,
            "user = ? and category = ? and travel = ?",
            this.getId().toString(), category.getId().toString(), travel.getId().toString());

        for (int i=0; i<expenses.size(); i++) {
            total += expenses.get(i).getValue();
        }

        return total;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
}
