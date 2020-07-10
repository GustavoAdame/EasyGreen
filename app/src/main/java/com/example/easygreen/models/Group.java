package com.example.easygreen.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.List;

@ParseClassName("Group")
public class Group extends ParseObject {
    public static final String KEY_group_name = "group_name";
    public static final String KEY_group_list = "group_list";

    public String getName(){
        return getString(KEY_group_name);
    }

    public void setName(String groupName){
        put(KEY_group_name, groupName);
    }

    public ParseRelation getList(){
        return getRelation(KEY_group_name);
    }

    public void setList(List<Ingredient> groupList){
        put(KEY_group_list, groupList);
    }

}
