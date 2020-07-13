package com.example.easygreen.models;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Recipe")
public class Recipe extends ParseObject {
    public static final String KEY_recipe_name = "recipe_name";
    public static final String KEY_recipe_image = "recipe_image";
    public static final String KEY_recipe_tag = "recipe_tag";
    public static final String KEY_recipe_description = "recipe_description";

    public String getName(){
        return getString(KEY_recipe_name);
    }

    public void setName(String name){
        put(KEY_recipe_name, name);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_recipe_image);
    }

    public void setImage(ParseFile image){
        put(KEY_recipe_image, image);
    }

    public String getTag(){
        return getString(KEY_recipe_tag);
    }

    public void setTag(String tag){
        put(KEY_recipe_tag, tag);
    }

    public String getDescription(){
        return getString(KEY_recipe_description);
    }

    public void setDescription(String description){
        put(KEY_recipe_description, description);
    }

}
