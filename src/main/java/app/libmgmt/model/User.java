package app.libmgmt.model;

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String salt;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //data format: [name, major, email, id, password]
    public User(String[] userData) {
        this.name = userData[0];
        this.email = userData[2];
        this.password = userData[4];
        this.userId = userData[3];
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void resetPassword(String newPassword) {
        password = newPassword;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public abstract String getUserRole();
}
