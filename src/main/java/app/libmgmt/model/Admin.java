package app.libmgmt.model;

import app.libmgmt.model.User;

public class Admin extends User {
    private String adminId;

    public Admin() {
    }

    public Admin(int id, String name, String email, String password, String role, String adminId) {
        super(id, name, email, password, role);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void addBook() {
        // TODO: Add book to the library
    }

    public void removeBook() {
        // TODO: Remove book from the library
    }

    public void updateBook() {
        // TODO: Update book information
    }

    public manageUser() {
        // TODO: Manage users
    }

    @Override
    public String getUserRole() {
        return "ADMIN";
    }
}
