package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

import java.util.List;

public class User extends SugarRecord<User> {
    private String name;
    private String email;
    private String password;
    private String imageURI;

    private Currency defaultCurrency;

    public User() {}

    public User(String name, String email, String password) {
        Currency defaultCurrency = Currency.find(Currency.class, "code = 'BRL'").get(0);
        this.setDefaultCurrency( defaultCurrency );

        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
    }

    public void update(String name, String email, String imageURI, Currency currency) {
        this.setName(name);
        this.setEmail(email);
        this.setImageURI(imageURI);
        this.setDefaultCurrency(currency);

        this.save();
    }

    public List<Travel> getTravels() {
        List<Travel> travels = Travel.findWithQuery(
                Travel.class,
                "SELECT * FROM Travel INNER JOIN Travel_user ON Travel.id = Travel_user.travel where Travel_user.user = ? ORDER BY Travel.start DESC",
                this.getId().toString());

        return travels;
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

    public void setDefaultCurrency(Currency currency) {
        this.defaultCurrency = currency;
    }

    public Currency getDefaultCurrency() {
        return this.defaultCurrency;
    }

    public void setImageURI(String uri) {
        this.imageURI = uri;
    }

    public String getImageURI() {
        return this.imageURI;
    }

}
