    package Config;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class config {
    // 🔹 Persistent connection
    private static Connection con = null;

    // ✅ Connect only once
    public static Connection connectDB() {
        if (con == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:Application.db");
                System.out.println("Connection Successful");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Connection Failed: " + e);
            }
        }
        return con;
    }

    // ✅ Close connection when program ends
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null;
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    // ✅ Add record
    public void addRecord(String sql, Object... values) {
        try (PreparedStatement pstmt = connectDB().prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    // ✅ Update record
    public void updateRecord(String sql, Object... values) {
        try (PreparedStatement pstmt = connectDB().prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    // ✅ View records
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (PreparedStatement pstmt = connectDB().prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            // Print table header
            StringBuilder headerLine = new StringBuilder();
            headerLine.append("--------------------------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-20s | ", header));
            }
            headerLine.append("\n--------------------------------------------------------------------------------");
            System.out.println(headerLine.toString());

            // Print rows
            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-20s | ", value != null ? value : ""));
                }
                System.out.println(row.toString());
            }
            System.out.println("--------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // ✅ Delete record
    public void deleteRecord(String sql, Object... values) {
        try (PreparedStatement pstmt = connectDB().prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    // ✅ Fetch records (returns list of maps)
  public java.util.List<java.util.Map<String, Object>> fetchRecords(String sqlQuery, Object... values) {
    java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();

    try (PreparedStatement pstmt = connectDB().prepareStatement(sqlQuery)) {
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        ResultSet rs = pstmt.executeQuery();
        
        if (!rs.isBeforeFirst()) {  // Check if no records are returned
            System.out.println("No records found for the query: " + sqlQuery);
        }

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            java.util.Map<String, Object> row = new java.util.HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            records.add(row);
        }

    } catch (SQLException e) {
        System.out.println("Error fetching records: " + e.getMessage());
    }

    return records;  // Return the list of records, could be empty if no records were found
}



    // ✅ Hash password (SHA-256)
    public String hashPass(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }
}