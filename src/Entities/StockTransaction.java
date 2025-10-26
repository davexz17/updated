package Entities;

public class StockTransaction {
    private int transaction_id;
    private final int supplier_id;
    private final String transaction_date;

    public StockTransaction(int supplier_id, String date) {
        this.supplier_id = supplier_id;
        this.transaction_date = date;
    }

   
}