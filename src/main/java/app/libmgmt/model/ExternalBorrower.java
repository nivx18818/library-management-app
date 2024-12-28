package app.libmgmt.model;

public class ExternalBorrower extends User {
    private String socialId;
    private String phoneNumber;

    public ExternalBorrower(String userId, String name, String email, String password, String socialId,
            String phoneNumber) {
        super(userId, name, email, password);
        this.socialId = socialId;
        this.phoneNumber = phoneNumber;
    }

    public ExternalBorrower(String userId, String name, String email, String password, String socialId,
            String phoneNumber, String salt) {
        super(userId, name, email, password, salt);
        this.socialId = socialId;
        this.phoneNumber = phoneNumber;
    }

    //data format: [name, major, email, id, password]
    public ExternalBorrower(String[] userData) {
        super(userData);
        this.socialId = userData[3];
        this.phoneNumber = userData[1];
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getUserRole() {
        return "EXTERNAL_BORROWER";
    }
}
