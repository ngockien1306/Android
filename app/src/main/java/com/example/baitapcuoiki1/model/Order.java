package com.example.baitapcuoiki1.model;

public class Order {
    private int productId;
    private String name;
    private int image;
    private int price;
    private String orderTime;

    public Order(int productId, String name, int image, int price, String orderTime) {
        this.productId = productId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.orderTime = orderTime;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public int getImage() { return image; }
    public int getPrice() { return price; }
    public String getOrderTime() { return orderTime; }
}
