package app.libmgmt.model;

import app.libmgmt.model.Borrower;

public class ExternalBorrower extends Borrower {
    private String socialId;
    private String phoneNumber;

    public ExternalBorrower() {
    }

    public ExternalBorrower(int id, String name, String email, String password, String role, String socialId,
            String phoneNumber) {
        super(id, name, email, password, role);
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
