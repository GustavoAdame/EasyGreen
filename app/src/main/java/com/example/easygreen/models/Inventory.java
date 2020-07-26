package com.example.easygreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;


@ParseClassName("Inventory")
public class Inventory  extends ParseObject {
    public static final String KEY_user = "user";
    public static final String KEY_ingredient_list = "ingredient_list";
    public static final String KEY_inventory = "inventory";

    public ParseObject getUser(){
        return getParseUser(KEY_user);
    }

    public void setUser(ParseUser user){
        put(KEY_user, user);
    }

    public JSONArray getInventory(){
        return getJSONArray(KEY_ingredient_list);
    }

    public void setInventory(JSONArray ingredients){
        put(KEY_ingredient_list, ingredients);
    }

}
