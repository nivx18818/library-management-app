package app.libmgmt.model;

public class Student extends User {
    private String studentId;
    private String major;

    public Student(String userId, String name, String email, String password, String studentId, String major) {
        super(userId, name, email, password);
        this.studentId = studentId;
        this.major = major;
    }

    public Student(String userId, String name, String email, String password, String studentId, String major, String salt) {
        super(userId, name, email, password, salt);
        this.studentId = studentId;
        this.major = major;
    }

    //data format: [name, major, email, id, password]
    public Student(String[] userData) {
        super(userData);
        this.studentId = userData[3];
        this.major = userData[1];
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
