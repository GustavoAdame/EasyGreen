package com.example.easygreen.models;

import java.util.HashSet;

public class Inventory {
    private long ID;
    private int count;
    private HashSet<Ingredient> list;

    public Inventory(long ID, int count, HashSet<Ingredient> list) {
        this.ID = ID;
        this.count = count;
        this.list = list;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HashSet<Ingredient> getList() {
        return list;
    }

    public void setList(HashSet<Ingredient> list) {
        this.list = list;
    }
}
