package com.rajmart;

public class Product {

    int id;
    String name;
    double price;
    int quantity;
    String image;

    public Product() {
    }

    public Product(
            int id,
            String name,
            double price,
            int quantity,
            String image
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void displayProduct() {
        System.out.println(
            "Product ID: " + id +
            " | Name: " + name +
            " | Price: Rs." + price +
            " | Stock: " + quantity +
            " | Image: " + image
        );
    }
}
