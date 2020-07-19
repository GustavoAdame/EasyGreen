package com.example.easygreen.models;


import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Ingredient")
public class Ingredient extends ParseObject implements Parcelable {
    public static final String KEY_ingredient_name = "ingredient_name";
    public static final String KEY_group = "group";

    public String getName(){
        return getString(KEY_ingredient_name);
    }
    public void setName(String name){
        put(KEY_ingredient_name, name);
    }

    public ParseObject getGroup(){
        return getParseObject(KEY_group);
    }

    public void setGroup(Group group){
        put(KEY_group, group);
    }

}
