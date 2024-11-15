package app.libmgmt.model;

public class Student extends User {
    private String studentId;
    private String major;

    public Student(int userId, String name, String email, String password, String studentId, String major) {
        super(userId, name, email, password);
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
