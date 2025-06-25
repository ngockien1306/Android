package com.example.baitaplon.model;

public class Product {
    private int id;
    private String name;
    private int image;
    private int categoryId;
    private int price;

    // Constructor có tham số
    public Product(int id, String name, int image, int categoryId, int price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.categoryId = categoryId;
    }

    // Constructor mặc định nếu cần dùng khi tạo rỗng rồi set sau
    public Product() {
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPrice() {
        return price;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
