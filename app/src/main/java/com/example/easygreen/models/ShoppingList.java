package com.example.easygreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

@ParseClassName("ShoppingList")
public class ShoppingList extends ParseObject {
    public static final String KEY_user = "user";
    public static final String KEY_shopping_list = "shopping_list";

    public ParseUser getUser(){
        return ParseUser.getCurrentUser();
    }

    public void setUser(ParseUser user){
        put(KEY_user, user);
    }

    public JSONArray getShoppingList(){
        return getJSONArray(KEY_shopping_list);
    }

    public void setShoppingList(JSONArray shoppingList){
        put(KEY_shopping_list, shoppingList);
    }
}
