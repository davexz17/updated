package Entities;

public class StockTransaction {
    private int transaction_id;
    private int supplier_id;
    private String transaction_date;

    public StockTransaction() {}

    public StockTransaction(int supplier_id, String date) {
        this.supplier_id = supplier_id;
        this.transaction_date = date;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }
}
