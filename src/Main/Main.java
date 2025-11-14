package Main;

import Config.config;
import java.util.*;

public class Main {

    public static void viewUsers() {
        config db = new config();
        String query = "SELECT * FROM tbl_user";
        String[] headers = {"User ID", "User Name", "User Email", "User Role", "User Status"};
        String[] columns = {"User_id", "User_name", "User_email", "User_role", "User_status"};
        db.viewRecords(query, headers, columns);
    }

    public static void viewSuppliers() {
        config db = new config();
        String query = "SELECT * FROM tbl_supplier";
        String[] headers = {"Supplier ID", "Name", "Contact", "Email", "Address"};
        String[] columns = {"Supplier_id", "Supplier_name", "Supplier_contact", "Supplier_email", "Supplier_address"};
        db.viewRecords(query, headers, columns);
    }

    public static void viewStockTransactions() {
        config db = new config();
        String query = "SELECT * FROM tbl_stocktransaction";
        String[] headers = {"Transaction ID", "Supplier ID", "Transaction Date"};
        String[] columns = {"Transaction_id", "Supplier_id", "Transaction_date"};
        db.viewRecords(query, headers, columns);
    }

    public static void viewRequests() {
        config db = new config();
        String query = "SELECT * FROM tbl_request";
        String[] headers = {"Request ID", "Requested Product", "Request Date"};
        String[] columns = {"Request_id", "Request_product", "Request_date"};
        db.viewRecords(query, headers, columns);
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            config database = new config();
            config.connectDB(); 
            char cont;

            do {
                System.out.println("\n===== FASTFOOD INVENTORY SYSTEM =====");
                System.out.println("1. Login User");
                System.out.println("2. Register User");
                System.out.println("3. View Users");
                System.out.println("4. Update User");
                System.out.println("5. Delete User");
                System.out.println("6. Approve User");
                System.out.println("7. Add Supplier");
                System.out.println("8. View Suppliers");
                System.out.println("9. Add Stock Transaction");
                System.out.println("10. View Stock Transactions");
                System.out.println("11. Add Request");
                System.out.println("12. View Requests");
                System.out.println("13. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {

                    case 1:
                        System.out.print("Enter Email: ");
                        String email = sc.nextLine();
                        System.out.print("Enter Password: ");
                        String pass = sc.nextLine();

                        String hashPass = database.hashPass(pass);

                        String qry = "SELECT * FROM tbl_user WHERE User_email = ? AND User_pass = ?";
                        List<Map<String, Object>> result = database.fetchRecords(qry, email, hashPass);

                        if (result.isEmpty()) {
                            System.out.println(" INVALID CREDENTIALS");
                        } else {
                            Map<String, Object> user = result.get(0);
                            String stat = user.get("User_status").toString();
                            String type = user.get("User_role").toString();

                            if (stat.equalsIgnoreCase("Pending")) {
                                System.out.println("️ Account is pending. Contact the Admin!");
                            } else {
                                System.out.println(" LOGIN SUCCESS!");
                                System.out.println("Welcome, " + type + "!");
                            }
                        }
                        break;

                    case 2:
                        System.out.print("Enter User Name: ");
                        String name = sc.nextLine();

                        String regEmail;
                        while (true) {
                            System.out.print("Enter User Email: ");
                            regEmail = sc.nextLine();
                            String checkQry = "SELECT * FROM tbl_user WHERE User_email = ?";
                            List<Map<String, Object>> emailCheck = database.fetchRecords(checkQry, regEmail);

                            if (emailCheck.isEmpty()) break;
                            else System.out.println("Email already exists. Try another.");
                        }

                        String role = "";
                        while (true) {
                            System.out.print("Enter Role (1 - Admin / 2 - Staff): ");
                            String roleInput = sc.nextLine();
                            if (roleInput.equals("1")) { role = "Admin"; break; }
                            else if (roleInput.equals("2")) { role = "Staff"; break; }
                            else System.out.println("Invalid input!");
                        }

                        System.out.print("Enter Password: ");
                        String password = sc.nextLine();
                        String hashedPassword = database.hashPass(password);

                        String insertSQL = "INSERT INTO tbl_user (User_name, User_email, User_pass, User_role, User_status) VALUES (?, ?, ?, ?, ?)";
                        database.addRecord(insertSQL, name, regEmail, hashedPassword, role, "PENDING");

                        System.out.println("Registration successful! Please wait for Admin approval.");
                        break;

                    case 3:
                        viewUsers();
                        break;

                    case 4:
                        System.out.print("Enter User ID to update: ");
                        int Id = sc.nextInt(); sc.nextLine();

                        System.out.print("Enter New Name: ");
                        String Uname = sc.nextLine();
                        System.out.print("Enter New Email: ");
                        String Uemail = sc.nextLine();
                        System.out.print("Enter New Role (Admin/Staff): ");
                        String Urole = sc.nextLine();
                        System.out.print("Enter New Password: ");
                        String Upass = sc.nextLine();

                        String newHashedPass = database.hashPass(Upass);
                        String sqlUpdate = "UPDATE tbl_user SET User_name = ?, User_email = ?, User_role = ?, User_pass = ? WHERE User_id = ?";
                        database.updateRecord(sqlUpdate, Uname, Uemail, Urole, newHashedPass, Id);
                        System.out.println("User updated successfully!");
                        break;

                    case 5:
                        System.out.print("Enter User ID to delete: ");
                        int deleteId = sc.nextInt(); sc.nextLine();
                        String sqlDelete = "DELETE FROM tbl_user WHERE User_id = ?";
                        database.deleteRecord(sqlDelete, deleteId);
                        System.out.println("User deleted successfully!");
                        break;

                    case 6:
                        System.out.println("Pending Accounts:");
                        String pendingQuery = "SELECT * FROM tbl_user WHERE User_status = 'PENDING'";
                        String[] headers = {"User ID", "User Name", "User Email", "User Role", "User Status"};
                        String[] columns = {"User_id", "User_name", "User_email", "User_role", "User_status"};
                        database.viewRecords(pendingQuery, headers, columns);

                        System.out.print("Enter User ID to approve: ");
                        int approveId = sc.nextInt(); sc.nextLine();

                        String approveSQL = "UPDATE tbl_user SET User_status = 'APPROVED' WHERE User_id = ?";
                        database.updateRecord(approveSQL, approveId);
                        System.out.println("User approved successfully!");
                        break;

                    case 7:
                        System.out.print("Enter Supplier Name: "); String sName = sc.nextLine();
                        System.out.print("Enter Supplier Contact: "); String sContact = sc.nextLine();
                        System.out.print("Enter Supplier Email: "); String sEmail = sc.nextLine();
                        System.out.print("Enter Supplier Address: "); String sAddress = sc.nextLine();

                        String insertSupplierSQL = "INSERT INTO tbl_supplier (Supplier_name, Supplier_contact, Supplier_email, Supplier_address) VALUES (?, ?, ?, ?)";
                        database.addRecord(insertSupplierSQL, sName, sContact, sEmail, sAddress);
                        System.out.println("Supplier added successfully!");
                        break;

                    case 8:
                        viewSuppliers();
                        break;

                    case 9:
                        System.out.print("Enter Supplier ID: "); int stSupplierId = sc.nextInt(); sc.nextLine();
                        System.out.print("Enter Transaction Date (YYYY-MM-DD): "); String stDate = sc.nextLine();

                        String insertStockSQL = "INSERT INTO tbl_stocktransaction (Supplier_id, Transaction_date) VALUES (?, ?)";
                        database.addRecord(insertStockSQL, stSupplierId, stDate);
                        System.out.println("Stock transaction added successfully!");
                        break;

                    case 10:
                        viewStockTransactions();
                        break;

                    case 11:
                        System.out.print("Enter Requested Product: "); String reqProduct = sc.nextLine();
                        System.out.print("Enter Request Date (YYYY-MM-DD): "); String reqDate = sc.nextLine();

                        String insertRequestSQL = "INSERT INTO tbl_request (Request_product, Request_date) VALUES (?, ?)";
                        database.addRecord(insertRequestSQL, reqProduct, reqDate);
                        System.out.println("Request added successfully!");
                        break;

              
                    case 12:
                        viewRequests();
                        break;

                 
                    case 13:
                        System.out.println("Exiting...");
                        config.closeConnection();
                        System.out.println("Thank you! Program ended.");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice.");
                        break;
                }

                if (choice != 13) {
                    System.out.print("Do you want to continue? (Y/N): ");
                    cont = sc.next().charAt(0); sc.nextLine();
                } else {
                    cont = 'N';
                }

            } while (cont == 'Y' || cont == 'y');
        }
    }
}
