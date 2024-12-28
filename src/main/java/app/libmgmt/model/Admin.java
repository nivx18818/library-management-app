package app.libmgmt.model;

public class Admin extends User {
    private int adminId;

    public Admin(String userId, String name, String email, String password, int adminId) {
        super(userId, name, email, password);
        this.adminId = adminId;
    }

    public Admin(String userId, String name, String email, String password, int adminId, String salt) {
        super(userId, name, email, password, salt);
        this.adminId = adminId;
    }

    //data format: [name, major, email, id, password]
    public Admin(String[] userData) {
        super(userData);
        this.adminId = Integer.parseInt(userData[3]);
    }

    public int getAdminId() {
        return adminId;
    }

    @Override
    public String getUserRole() {
        return "ADMIN";
    }
}
