package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.List;

public class User extends SugarRecord<User> {
    String name;
    String email;

    public User() {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public List<TravelUser> getTravels() {
        List<TravelUser> userTravels = TravelUser.find(TravelUser.class, "user = ?", this.getId().toString());

        return userTravels;
    }
}
