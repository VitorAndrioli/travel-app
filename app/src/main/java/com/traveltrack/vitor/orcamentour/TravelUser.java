package com.traveltrack.vitor.orcamentour;

import com.orm.SugarRecord;

public class TravelUser extends SugarRecord<TravelUser> {
    private User user;
    private Travel travel;
    private double budget;

    public TravelUser() {

    }

    public TravelUser(Travel travel, User user, double budget) {
        this.travel = travel;
        this.user = user;
        this.budget = budget;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public Travel getTravel() {
        return this.travel;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getBudget() {
        return this.budget;
    }
}
