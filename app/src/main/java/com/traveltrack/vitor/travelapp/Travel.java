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
    private float exchangeRate;
    public String suffix;

    public Travel() {}

    public Travel(String name, String imageURI, Date start, Date end, Currency currency, float exchangeRate, User user) {
        this.setName(name, user);
        this.setImageURI(imageURI);
        this.setStart(start);
        this.setEnd(end);
        this.setCurrency(currency);
        this.setExchangeRate(exchangeRate);
    }

    public void update(String name, String imageURI, Date start, Date end, Currency currency, float exchangeRate, User user) {
        this.setName(name, user);
        this.setImageURI(imageURI);
        this.setStart(start);
        this.setEnd(end);
        this.setCurrency(currency);
        this.setExchangeRate(exchangeRate);

        this.save();
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

    public double getTotalExpensesInDefaultCurrency() {
        return this.getTotalExpenses() * this.exchangeRate;
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

    public double getTotalExpensesByCategoryInDefaultCurrency(Category category) {
        return this.getTotalExpensesByCategory(category) * this.exchangeRate;
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

    public void setName(String name, User user) {
        if (!name.equals( this.name )) {
            int travelsWithSameName = Travel.findWithQuery(Travel.class,
                    "SELECT * FROM Travel INNER JOIN Travel_user ON Travel.id = Travel_user.travel WHERE Travel.name = ? AND Travel_user.user = ?",
                    name,
                    user.getId().toString()
            ).size();

            if (travelsWithSameName > 0) {
                this.suffix = " - " + (travelsWithSameName + 1);
            }

            this.name = name;
        }
    }

    public String getName() {
        String name = this.name;
        if (this.suffix != null) {
            name += this.suffix;
        }

        return name;
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

    public void setExchangeRate(float rate) {
        if (rate <= 0)
            this.exchangeRate = 1;
        else
            this.exchangeRate = rate;
    }

    public float getExchangeRate() {
        return this.exchangeRate;
    }

}
