package com.rajmart;

public class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.price * quantity;
    }

    public void displayCartItem() {
        System.out.println(
                product.name +
                        " | Qty: " + quantity +
                        " | Total: Rs. " + getTotal());
    }
}