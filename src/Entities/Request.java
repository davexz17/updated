package Entities;

public class Request {
    private int request_id;
    private String request_product;
    private String request_date;

    public Request() {}

    public Request(String product, String date) {
        this.request_product = product;
        this.request_date = date;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getRequest_product() {
        return request_product;
    }

    public void setRequest_product(String request_product) {
        this.request_product = request_product;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }
}
