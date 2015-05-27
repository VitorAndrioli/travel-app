package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

public class Category extends SugarRecord<Category> {
    private String name;
    private String uri;

    public Category() {

    }

    public Category(String name, String uri) {
        this.setName(name);
        this.setURI(uri);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public String getURI() {
        return this.uri;
    }
}
