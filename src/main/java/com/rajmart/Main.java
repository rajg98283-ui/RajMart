package com.rajmart;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ArrayList<Product> products = new ArrayList<>();
        ArrayList<CartItem> cart = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        products.addAll(ProductDAO.getAllProducts());

        boolean running = true;

        System.out.println("Welcome to Raj Gaming Mart!");

        while (running) {

            System.out.println("\n--- MENU ---");
            System.out.println("1. View Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            if (choice == 1) {

                System.out.println("\nAvailable Gaming Products:");

                for (Product product : products) {
                    product.displayProduct();
                }

            } else if (choice == 2) {

                System.out.print("\nEnter Product ID: ");
                int selectedId = scanner.nextInt();

                System.out.print("Enter Quantity: ");
                int selectedQuantity = scanner.nextInt();

                Product selectedProduct = null;

                for (Product product : products) {

                    if (product.id == selectedId) {
                        selectedProduct = product;
                        break;
                    }
                }

                if (selectedProduct == null) {

                    System.out.println("Invalid Product ID!");

                } else if (selectedQuantity <= 0) {

                    System.out.println("Quantity must be greater than 0.");

                } else if (selectedQuantity > selectedProduct.quantity) {

                    System.out.println(
                            "Only " + selectedProduct.quantity + " item(s) available.");

                } else {

                    cart.add(new CartItem(selectedProduct, selectedQuantity));

                    selectedProduct.quantity -= selectedQuantity;

                    ProductDAO.updateQuantity(
                            selectedProduct.id,
                            selectedProduct.quantity);

                    System.out.println("Added to Cart!");
                }

            } else if (choice == 3) {

                if (cart.isEmpty()) {

                    System.out.println("\nYour cart is empty.");

                } else {

                    showCart(cart);
                }

            } else if (choice == 4) {

                if (cart.isEmpty()) {

                    System.out.println(
                            "\nYour cart is empty. Add products before checkout.");

                } else {

                    scanner.nextLine();

                    System.out.print("\nEnter Customer Name: ");
                    String customerName = scanner.nextLine();

                    Customer customer = new Customer(customerName);

                    System.out.println("\nChoose Payment Method:");
                    System.out.println("1. Cash");
                    System.out.println("2. UPI");
                    System.out.println("3. Card");
                    System.out.print("Choose payment option: ");

                    int paymentChoice = scanner.nextInt();

                    String paymentMethod;

                    if (paymentChoice == 1) {
                        paymentMethod = "Cash";
                    } else if (paymentChoice == 2) {
                        paymentMethod = "UPI";
                    } else if (paymentChoice == 3) {
                        paymentMethod = "Card";
                    } else {
                        paymentMethod = "Unknown";
                    }

                    double subtotal = 0;

                    for (CartItem item : cart) {
                        subtotal += item.getTotal();
                    }

                    double gst = subtotal * 0.18;
                    double grandTotal = subtotal + gst;
                    double deliveryCharge = 50.00;
                    double serviceCharge = 10.00;
                    grandTotal = subtotal + deliveryCharge + serviceCharge;

                    String consoleEmail = "console@rajmart.com";
                    int customerId = OrderDAO.getCustomerIdByEmail(consoleEmail);

                    String paymentStatus;

                    if ("UPI".equalsIgnoreCase(paymentMethod) ||
                        "Card".equalsIgnoreCase(paymentMethod)) {
                        paymentStatus = "PAID";
                    } else {
                        paymentStatus = "PENDING";
                    }

                    int orderId = -1;

                    if (customerId > 0) {
                        orderId = OrderDAO.createOrder(
                                customerId,
                                subtotal,
                                deliveryCharge,
                                serviceCharge,
                                grandTotal,
                                "Console Order",
                                paymentMethod,
                                paymentStatus);
                    } else {
                        System.out.println("Console customer account not found: " + consoleEmail);
                    }
System.out.println("\n--- RAJ GAMING MART BILL ---");
                    System.out.println("Customer: " + customer.name);
                    System.out.println("Payment: " + paymentMethod);

                    showCart(cart);

                    if (orderId != -1) {

                        for (CartItem item : cart) {

                            OrderDAO.addOrderItem(
                                    orderId,
                                    item.product.id,
                                    item.product.name,
                                    item.quantity,
                                    item.product.price,
                                    item.getTotal());
                        }

                        System.out.println("Order saved successfully!");
                        System.out.println("Order ID: " + orderId);

                    } else {

                        System.out.println("Order could not be saved.");
                    }

                    System.out.println("Thank you for shopping!");

                    cart.clear();
                }

            } else if (choice == 5) {

                running = false;

                System.out.println(
                        "\nThank you for shopping at Raj Gaming Mart!");

            } else {

                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    public static void showCart(ArrayList<CartItem> cart) {

        double subtotal = 0;

        System.out.println("\n--- CART DETAILS ---");

        for (CartItem item : cart) {

            item.displayCartItem();

            subtotal += item.getTotal();
        }

        double gst = subtotal * 0.18;
        double grandTotal = subtotal + gst;

        System.out.println("----------------------------");
        System.out.println("Subtotal: Rs. " + subtotal);
        System.out.println("GST (18%): Rs. " + gst);
        System.out.println("Grand Total: Rs. " + grandTotal);
        System.out.println("----------------------------");
    }
}