package Entities;

public class Product {
    private int product_id;
    private final String product_name;
    private final double product_price;
    private final String product_status;

    public Product(String name, double price, String status) {
        this.product_name = name;
        this.product_price = price;
        this.product_status = status;
    }

    
}