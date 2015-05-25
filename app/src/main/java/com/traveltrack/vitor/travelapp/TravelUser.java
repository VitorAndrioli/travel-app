package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

public class TravelUser extends SugarRecord<TravelUser> {
    User user;
    Travel travel;
    double budget;

    public TravelUser() {

    }

    public TravelUser(Travel travel, User user, double budget) {
        this.travel = travel;
        this.user = user;
        this.budget = budget;
    }

}
