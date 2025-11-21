package Main;

import Config.config;
import java.util.*;

public class Main {

    // Method to list available products for the user to select from
    public static void listProducts(Scanner sc, config database) {
        config db = new config();
        String query = "SELECT Product_id, Product_name, Product_price FROM tbl_products";  
        String[] headers = {"Product ID", "Product Name", "Product Price"};  
        String[] columns = {"Product_id", "Product_name", "Product_price"};  
        db.viewRecords(query, headers, columns);  
    }

    // Method to request a product (user selects a product)
    public static void requestProduct(Scanner sc, config database) {
        listProducts(sc, database);

        System.out.print("Enter the Product ID of the product you want to request: ");
        int productId = sc.nextInt();
        sc.nextLine();  

        String productQuery = "SELECT Product_name, Product_price FROM tbl_products WHERE Product_id = ?";
        List<Map<String, Object>> productDetails = database.fetchRecords(productQuery, productId);

        System.out.println("Product Details Returned: " + productDetails);

        if (productDetails == null || productDetails.isEmpty()) {
            System.out.println("Invalid Product ID or no product found.");
            return; 
        }

        Map<String, Object> product = productDetails.get(0);

        String productName = (product.get("Product_name") != null) ? product.get("Product_name").toString() : "Unknown Product";
        Double productPrice = (product.get("Product_price") != null) ? (Double) product.get("Product_price") : 0.0;

        System.out.println("You selected: " + productName + " - Price: $" + productPrice);

        long requestDate = System.currentTimeMillis() / 1000;  

        String insertSQL = "INSERT INTO tbl_request (requested_product, user_id, request_date) VALUES (?, ?, ?)";
        database.addRecord(insertSQL, productId, 1, requestDate); 

        System.out.println("Product request has been successfully made!");
    }

    // Method to add a product
    public static void addProduct(Scanner sc, config database) {
        System.out.print("Enter Product Name: ");
        String productName = sc.nextLine();

        System.out.print("Enter Product Price: ");
        double price = sc.nextDouble();
        sc.nextLine();  

        System.out.print("Enter Product Description: ");
        String description = sc.nextLine();

        String insertSQL = "INSERT INTO tbl_products (Product_name, Product_price, Product_description) VALUES (?, ?, ?)";
        database.addRecord(insertSQL, productName, price, description);  

        System.out.println("Product added successfully!");
    }

    // Method to view suppliers
    public static void viewSuppliers() {
        config db = new config();
        String query = "SELECT Supplier_id, Supplier_name, Supplier_contact, Supplier_email, Supplier_address FROM tbl_supplier";
        String[] headers = {"Supplier ID", "Supplier Name", "Contact", "Email", "Address"};  // Display headers
        String[] columns = {"Supplier_id", "Supplier_name", "Supplier_contact", "Supplier_email", "Supplier_address"};  // Columns to fetch from the database

        // Fetch the records from the database
        db.viewRecords(query, headers, columns);
    }

    // Method to view requests (fetching data from tbl_request, tbl_products, and tbl_user)
    // Method to view requests (fetching data from tbl_request, tbl_products, and tbl_user)
// Method to view requests (fetching data from tbl_request, tbl_products, and tbl_user)
public static void viewRequests() {
    config db = new config();
    String query = "SELECT r.request_id, p.Product_name, u.User_name, r.request_date " +
                   "FROM tbl_request r " +
                   "JOIN tbl_products p ON r.requested_product = p.Product_id " +
                   "JOIN tbl_user u ON r.user_id = u.User_id";  // Join tbl_request with tbl_products and tbl_user

    String[] headers = {"Request ID", "Product Name", "User Name", "Request Date"};  // Display headers
    String[] columns = {"request_id", "Product_name", "User_name", "request_date"};  // Columns to fetch from the database

    // Debugging: Print the query to verify it
    System.out.println("Executing SQL Query: " + query);

    // Fetch the records from the database
    List<Map<String, Object>> records = db.fetchRecords(query, columns);

    // Debugging: Check if the records are empty or not
    System.out.println("Records fetched: " + records.size());

    // Check if records are empty before proceeding
    if (records != null && !records.isEmpty()) {
        // Printing the records in a readable format
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-20s | %-20s | %-15s |\n", headers[0], headers[1], headers[2], headers[3]);
        System.out.println("--------------------------------------------------------------------------------");

        for (Map<String, Object> row : records) {
            // Debugging: Print the row data to see what is returned
            System.out.println("Row data: " + row);

            String requestDate = row.get("request_date") != null ? row.get("request_date").toString() : "N/A";  // Handle null values

            // Convert the timestamp to a human-readable date format
            String formattedDate = "Invalid Date";
            if (!"N/A".equals(requestDate)) {
                long timestamp = Long.parseLong(requestDate);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedDate = sdf.format(new java.util.Date(timestamp * 1000)); // Convert to milliseconds
            }

            // Print each row of data
            System.out.printf("| %-12d | %-20s | %-20s | %-15s |\n", 
                row.get("request_id"), 
                row.get("Product_name"), 
                row.get("User_name"), 
                formattedDate);
        }
        System.out.println("--------------------------------------------------------------------------------");
    } else {
        
        System.out.println("No requests available.");
    }
}


    public static boolean loginUser(Scanner sc, config database) {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        String hashPass = database.hashPass(pass);

        String qry = "SELECT * FROM tbl_user WHERE User_email = ? AND User_pass = ?";
        List<Map<String, Object>> result = database.fetchRecords(qry, email, hashPass);

        if (result.isEmpty()) {
            System.out.println("INVALID CREDENTIALS");
            return false;
        } else {
            Map<String, Object> user = result.get(0);
            String stat = user.get("User_status").toString();
            String type = user.get("User_role").toString();

            if (stat.equalsIgnoreCase("Pending")) {
                System.out.println("Account is pending. Contact the Admin!");
                return false;
            } else {
                System.out.println("LOGIN SUCCESS!");
                System.out.println("Welcome, " + type + "!");
                return true;
            }
        }
    }

    
    public static boolean registerUser(Scanner sc, config database) {
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
        return false;
    }

    // Method to approve a user account
    public static void approveUser(Scanner sc, config database) {
        System.out.println("===== Pending Accounts =====");
        String pendingQuery = "SELECT * FROM tbl_user WHERE User_status = 'PENDING'";
        String[] headers = {"User ID", "User Name", "User Email", "User Role", "User Status"};
        String[] columns = {"User_id", "User_name", "User_email", "User_role", "User_status"};
        database.viewRecords(pendingQuery, headers, columns);

        System.out.print("Enter User ID to approve: ");
        int approveId = sc.nextInt();
        sc.nextLine();  // Consume newline

        String approveSQL = "UPDATE tbl_user SET User_status = 'APPROVED' WHERE User_id = ?";
        database.updateRecord(approveSQL, approveId);
        System.out.println("User approved successfully!");
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            config database = new config();
            config.connectDB(); 
            boolean loggedIn = false;
            String loggedInRole = "";

            // Login or Register before accessing main menu
            while (!loggedIn) {
                System.out.println("===== FASTFOOD INVENTORY SYSTEM =====");
                System.out.println("1. Login User");
                System.out.println("2. Register User");
                System.out.print("Choose an option (1 or 2): ");
                int choice = sc.nextInt();
                sc.nextLine();  // Consume newline

                if (choice == 1) {
                    loggedIn = loginUser(sc, database);
                    if (loggedIn) {
                        // Retrieve the user's role after successful login
                        String qry = "SELECT User_role FROM tbl_user WHERE User_email = ?";
                        List<Map<String, Object>> result = database.fetchRecords(qry, sc.nextLine());
                        loggedInRole = result.get(0).get("User_role").toString();
                    }
                } else if (choice == 2) {
                    loggedIn = registerUser(sc, database);
                } else {
                    System.out.println("Invalid option. Please choose 1 or 2.");
                }
            }

            // After login or registration, the main menu is accessible
            char cont;
            do {
                System.out.println("\n===== MAIN MENU =====");

                // Show different menu based on the user's role
                if (loggedInRole.equalsIgnoreCase("Admin")) {
                    System.out.println("1. View Users");
                    System.out.println("2. View Suppliers");
                    System.out.println("3. View Stock Transactions");
                    System.out.println("4. View Requests");
                    System.out.println("5. Approve Pending Users");
                    System.out.println("6. Exit");
                    System.out.println("7. Request a Product");
                    System.out.println("8. Add Product");  // New option to add a product
                } else if (loggedInRole.equalsIgnoreCase("Staff")) {
                    System.out.println("1. View Suppliers");
                    System.out.println("2. View Stock Transactions");
                    System.out.println("3. View Requests");
                    System.out.println("4. Exit");
                    System.out.println("5. Request a Product");  // New option for Staff to request a product
                }

                System.out.print("Enter choice: ");
                
                int choice = sc.nextInt();
                sc.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        if (loggedInRole.equalsIgnoreCase("Admin")) {
                            viewUsers();
                        } else if (loggedInRole.equalsIgnoreCase("Staff")) {
                            viewSuppliers();
                        }
                        break;

                    case 2:
                        if (loggedInRole.equalsIgnoreCase("Admin")) {
                            viewSuppliers();
                        } else if (loggedInRole.equalsIgnoreCase("Staff")) {
                            viewStockTransactions();
                        }
                        break;

                    case 3:
                        if (loggedInRole.equalsIgnoreCase("Admin")) {
                            viewStockTransactions();
                        } else if (loggedInRole.equalsIgnoreCase("Staff")) {
                            viewRequests();
                        }
                        break;

                    case 4:
                        if (loggedInRole.equalsIgnoreCase("Admin")) {
                            viewRequests();
                        } else {
                            // Staff Exit
                            System.out.println("Exiting...");
                            System.exit(0);
                        }
                        break;

                    case 5:
                        // Admin Exit
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;

                    case 6:
                        // Admin Exit
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;

                    case 7:
                        // Request a product
                        requestProduct(sc, database);  // Add product request feature
                        break;

                    case 8:
                        // Add Product (Admin only)
                        addProduct(sc, database);  // Add product feature
                        break;

                    default:
                        System.out.println("Invalid choice.");
                        break;
                }

            } while (true);  // Removed log out option

        }
    }

    private static void viewUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void viewStockTransactions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
