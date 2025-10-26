package Entities;

public class Request {
    private int request_id;
    private final String request_product;
    private final String request_date;

    public Request(String product, String date) {
        this.request_product = product;
        this.request_date = date;
    }

    
}