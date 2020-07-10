package com.example.easygreen.models;

import android.widget.ImageView;

public class Ingredient {
    private String name;
    private Group group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
