package com.example.easygreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

@ParseClassName("Shopping_List")
public class Shopping_List extends ParseObject {
    public static final String KEY_objectId = "objectId";
    public static final String KEY_shopping_list = "shopping_list";

    public String getId(){
        return getString(KEY_objectId);
    }

    public void setId(String id){
        put(KEY_objectId, id);
    }

    public JSONArray getShoppingList(){
        return getJSONArray(KEY_shopping_list);
    }

    public void setShoppingList(JSONArray shoppingList){
        put(KEY_shopping_list, shoppingList);
    }
}
