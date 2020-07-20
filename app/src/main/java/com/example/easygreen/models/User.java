package com.example.easygreen.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_image = "profileImage";
    public static final String KEY_username = "username";
    public static final String KEY_password = "password";
    public static final String KEY_email = "email";

    public static String getKEY_username() {
        return KEY_username;
    }

    public static String getKEY_password() {
        return KEY_password;
    }

    public static String getKEY_email() {
        return KEY_email;
    }

    public ParseFile getImage(){
        return getParseFile(KEY_image);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_image, parseFile);
    }
}
