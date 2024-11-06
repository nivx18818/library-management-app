package app.libmgmt.model;

public class ExternalBorrower extends User {
    private String socialId;
    private String phoneNumber;

    public ExternalBorrower(String name, String email, String password, String socialId,
            String phoneNumber) {
        super(name, email, password);
        this.socialId = socialId;
        this.phoneNumber = phoneNumber;
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
