package app.libmgmt.model;

public class Admin extends User {
    private int adminId;

    public Admin(int userId, String name, String email, String password, int adminId) {
        super(userId, name, email, password);
        this.adminId = adminId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String getUserRole() {
        return "ADMIN";
    }
}
