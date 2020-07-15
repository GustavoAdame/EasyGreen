package com.example.easygreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;


@ParseClassName("Inventory")
public class Inventory  extends ParseObject {
    public static final String KEY_objectId = "objectId";
    public static final String KEY_ingredient_list = "ingredient_list";

    public String getId(){
        return getString(KEY_objectId);
    }

    public void setId(String id){
        put(KEY_objectId, id);
    }

    public JSONArray getInventory(){
        return getJSONArray(KEY_ingredient_list);
    }

    public void setInventory(JSONArray ingredients){
        put(KEY_ingredient_list, ingredients);
    }
}
