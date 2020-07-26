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
    public static final String KEY_expiration = "expiration";

    public ParseObject getUser(){
        return getParseUser(KEY_user);
    }

    public void setUser(ParseUser user){
        put(KEY_user, user);
    }

    public JSONArray getInventory(){
        return getJSONArray(KEY_inventory);
    }

    public void setInventory(JSONArray ingredients){
        put(KEY_inventory, ingredients);
    }

    public JSONArray getExpirations(){
        return getJSONArray(KEY_expiration);
    }

    public void setExpirations(JSONArray dates){
        put(KEY_expiration, dates);
    }

}
