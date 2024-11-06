package app.libmgmt.model;

public class Admin extends User {
    private String adminId;

    public Admin(String name, String email, String password) {
        super(name, email, password);
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
