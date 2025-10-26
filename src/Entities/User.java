package Entities;

public class User {
    private int user_id;
    private String user_name;
    private String user_email;
    private String user_pass;
    private String user_role;

    public User(String name, String email, String pass, String role) {
        this.user_name = name;
        this.user_email = email;
        this.user_pass = pass;
        this.user_role = role;
    }

    // Getters and Setters
    public int getUser_id() { return user_id; }
    public String getUser_name() { return user_name; }
    public String getUser_email() { return user_email; }
    public String getUser_pass() { return user_pass; }
    public String getUser_role() { return user_role; }

    public void setUser_id(int user_id) { this.user_id = user_id; }
    public void setUser_name(String user_name) { this.user_name = user_name; }
    public void setUser_email(String user_email) { this.user_email = user_email; }
    public void setUser_pass(String user_pass) { this.user_pass = user_pass; }
    public void setUser_role(String user_role) { this.user_role = user_role; }
}