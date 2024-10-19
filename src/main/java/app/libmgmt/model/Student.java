package app.libmgmt.model;

public class Student extends Borrower {
    private String studentId;
    private String major;

    public Student(String name, String email, String password, String role, String studentId, String major) {
        super(name, email, password, role);
        this.studentId = studentId;
        this.major = major;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String getUserRole() {
        return "STUDENT";
    }
}
