package Entities;

public class Supplier {
    private int supplier_id;
    private final String supplier_name;
    private final String supplier_contact;
    private final String supplier_email;
    private final String supplier_address;

    public Supplier(String name, String contact, String email, String address) {
        this.supplier_name = name;
        this.supplier_contact = contact;
        this.supplier_email = email;
        this.supplier_address = address;
    }

   
}