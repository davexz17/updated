package Main;

import Config.config;
import java.util.*;

public class Main {

    // ✅ View all users
    public static void viewUsers() {
        config db = new config();
        String votersQuery = "SELECT * FROM tbl_user";
        String[] votersHeaders = {"User ID", "User Name", "User Email", "User Role", "User Status"};
        String[] votersColumns = {"User_id", "User_name", "User_email", "User_role", "User_status"};
        db.viewRecords(votersQuery, votersHeaders, votersColumns);
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
                System.out.println("6. Exit");
                System.out.println("7. Approve User");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {

                    // 🔹 LOGIN
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
                                if (type.equalsIgnoreCase("Admin")) {
                                    System.out.println("Welcome, Admin!");
                                } else if (type.equalsIgnoreCase("Staff") || type.equalsIgnoreCase("Customer")) {
                                    System.out.println("Welcome, " + type + "!");
                                } else {
                                    System.out.println("Unknown user role: " + type);
                                }
                            }
                        }
                        break;

                    // 🔹 REGISTER
                    case 2: {
                        config con = new config();

                        System.out.print("Enter User Name: ");
                        String name = sc.nextLine();

                        String regEmail;
                        while (true) {
                            System.out.print("Enter User Email: ");
                            regEmail = sc.nextLine();

                            String checkQry = "SELECT * FROM tbl_user WHERE User_email = ?";
                            List<Map<String, Object>> emailCheck = con.fetchRecords(checkQry, regEmail);

                            if (emailCheck.isEmpty()) {
                                break;
                            } else {
                                System.out.println("️ Email already exists. Please use another.");
                            }
                        }

                        String role = "";
                        while (true) {
                            System.out.print("Enter Role (1 - Admin / 2 - Staff): ");
                            String roleInput = sc.nextLine();
                            if (roleInput.equals("1")) {
                                role = "Admin";
                                break;
                            } else if (roleInput.equals("2")) {
                                role = "Staff";
                                break;
                            } else {
                                System.out.println("Invalid input! Please enter 1 for Admin or 2 for Staff.");
                            }
                        }

                        System.out.print("Enter Password: ");
                        String password = sc.nextLine();

                        String hashedPassword = database.hashPass(password);

                        String insertSQL = "INSERT INTO tbl_user (User_name, User_email, User_pass, User_role, User_status) VALUES (?, ?, ?, ?, ?)";
                        con.addRecord(insertSQL, name, regEmail, hashedPassword, role, "PENDING");

                        System.out.println(" Registration successful! Please wait for Admin approval.");
                        break;
                    }

                    // 🔹 VIEW USERS
                    case 3:
                        viewUsers();
                        break;

                    // 🔹 UPDATE USER
                    case 4:
                        System.out.print("Enter User ID to update: ");
                        int Id = sc.nextInt();
                        sc.nextLine();

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
                        System.out.println(" User updated successfully!");
                        break;

                    // 🔹 DELETE USER
                    case 5:
                        System.out.print("Enter User ID to delete: ");
                        int deleteId = sc.nextInt();
                        String sqlDelete = "DELETE FROM tbl_user WHERE User_id = ?";
                        database.deleteRecord(sqlDelete, deleteId);
                        System.out.println("🗑️ User deleted successfully!");
                        break;
                        
                        // 🔹 APPROVE USER
                    case 7:
                        System.out.println("Pending Accounts:");
                        String pendingQuery = "SELECT * FROM tbl_user WHERE User_status = 'PENDING'";
                        String[] headers = {"User ID", "User Name", "User Email", "User Role", "User Status"};
                        String[] columns = {"User_id", "User_name", "User_email", "User_role", "User_status"};
                        database.viewRecords(pendingQuery, headers, columns);

                        System.out.print("Enter User ID to approve: ");
                        int approveId = sc.nextInt();
                        sc.nextLine();

                        String approveSQL = "UPDATE tbl_user SET User_status = 'APPROVED' WHERE User_id = ?";
                        database.updateRecord(approveSQL, approveId);
                        System.out.println(" User approved successfully!");
                        break;


                    // 🔹 EXIT
                    case 6:
                        System.out.println("Exiting...");
                        config.closeConnection();
                        System.out.println("Thank you! Program ended.");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice.");
                        break;
                }

                if (choice != 6) {
                    System.out.print("Do you want to continue? (Y/N): ");
                    cont = sc.next().charAt(0);
                    sc.nextLine(); // consume newline
                } else {
                    cont = 'N';
                }

            } while (cont == 'Y' || cont == 'y');
        }
        
    }
}
