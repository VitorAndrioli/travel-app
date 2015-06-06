package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

public class Travel extends SugarRecord<Travel> {
    private String name;
    private Date start;
    private Date end;
    private String imageURI;
    private Currency currency;

    public Travel() {}

    public Travel(String name, String imageURI, Date start, Date end, Currency currency) {
        this.setName(name);
        this.setImageURI(imageURI);
        this.setStart(start);
        this.setEnd(end);
        this.setCurrency(currency);
    }

    public Travel(String name, Date start, Date end) {
        this.setName(name);
        this.setStart(start);
        this.setEnd(end);
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = Expense.find(Expense.class, "travel = ? ORDER BY date DESC", this.getId().toString());

        return expenses;
    }

    public float getTotalExpenses() {
        float total = 0;

        List<Expense> expenses = this.getExpenses();

        for(int i=0; i<expenses.size(); i++) {
            total += expenses.get(i).getValue();
        }

        return total;
    }

    public List<TravelUser> getParticipants() {
        List<TravelUser> users = TravelUser.find(TravelUser.class, "travel = ?", this.getId().toString());

        return users;
    }

    public double getTotalExpensesByCategory(Category category) {
        double category_total = 0;
        List<Expense> expenses = Expense.find(Expense.class, "travel = ? and category = ?", this.getId().toString(), category.getId().toString());

        for (int i=0; i<expenses.size(); i++)
            category_total += expenses.get(i).getValue();

        return category_total;
    }

    @Override
    public void delete() {
        List<TravelUser> users = this.getParticipants();

        for (int i=0; i<users.size(); i++) {
            TravelUser travelUser = TravelUser.find(TravelUser.class,
                    "travel = ? and user = ?",
                    this.getId().toString(),
                    users.get(i).getUser().getId().toString()).get(0);
            travelUser.delete();
        }

        List<Expense> expenses = this.getExpenses();
        for (int i=0; i<expenses.size(); i++) {
            expenses.get(i).delete();
        }

        super.delete();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setImageURI(String uri) {
        this.imageURI = uri;
    }

    public String getImageURI() {
        return this.imageURI;
    }

    public void setStart(Date date) {
        this.start = date;
    }

    public Date getStart() {
        return this.start;
    }

    public void setEnd(Date date) {
        this.end = date;
    }

    public Date getEnd() {
        return this.end;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return this.currency;
    }

}
