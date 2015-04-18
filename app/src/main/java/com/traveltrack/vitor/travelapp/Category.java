package com.traveltrack.vitor.travelapp;

import com.orm.SugarRecord;

/**
 * Created by vitor on 18/04/15.
 */
public class Category extends SugarRecord<Category> {
    String name;

    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }
}
