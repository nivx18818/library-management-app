package app.libmgmt.model;

public class Admin extends User {
    private String adminId;

    public Admin(String name, String email, String password, String role) {
        super(name, email, password, role);
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getUserRole() {
        return "ADMIN";
    }
}
