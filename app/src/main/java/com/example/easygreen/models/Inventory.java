package com.example.easygreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.List;


@ParseClassName("Inventory")
public class Inventory  extends ParseObject {
    public static final String KEY_count = "count";
    public static final String KEY_inventory_list = "inventory_list";

    public int getCount(){
        return getInt(KEY_count);
    }

    public void setCount(int count){
        put(KEY_count, count);
    }

    public ParseRelation getInventory(){
        return getRelation(KEY_inventory_list);
    }

    public void setInventory(List<Ingredient> ingredients){
        put(KEY_inventory_list, ingredients);
    }
}
