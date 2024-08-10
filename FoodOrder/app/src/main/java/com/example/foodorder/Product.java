package com.example.foodorder;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private String description;
    private int imageResource; // Yeni eklenen değişken

    public Product(int id, String name, double price, String description, int imageResource) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }
}
