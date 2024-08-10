package com.example.foodorder;
public class CartItem {
    private int id;
    private int quantity;
    private String name;
    private double price;
    private String description;
    private byte[] image;

    public CartItem(int id, int quantity, String name, double price, String description, byte[] image) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    // Getter ve setter metodları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
