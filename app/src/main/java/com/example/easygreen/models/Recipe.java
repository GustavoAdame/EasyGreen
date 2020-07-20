package com.example.easygreen.models;


public class Recipe {
    public String recipe_id = "";
    public String recipe_name = "";
    public String recipe_imageURL = "";
    public String recipe_tag = "";
    public String recipe_description = "";

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getName(){
        return recipe_name;
    }

    public void setName(String name){
        recipe_name = name;
    }

    public String getTag(){
        return recipe_tag;
    }

    public void setTag(String tag){
        recipe_tag = tag;
    }

    public String getURL(){
        return recipe_imageURL;
    }

    public void setURL(String url){
        recipe_imageURL = url;
    }

    public String getDescription(){
        return recipe_description;
    }

    public void setDescription(String description){
        recipe_description = description;
    }

}
