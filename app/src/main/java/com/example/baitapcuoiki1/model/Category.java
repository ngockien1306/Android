package com.example.baitapcuoiki1.model;

public class Category {
    private String name;
    private int image;
    private int id;
    public Category(int id, int image, String name){
        this.id=id;
        this.image=image;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public int getImage(){
        return image;
    }

    public String getName(){
        return name;
    }

}
