package app.libmgmt.dao;

import app.libmgmt.model.Admin;
import app.libmgmt.model.ExternalBorrower;
import app.libmgmt.model.Student;
import app.libmgmt.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

public class UserDAO {

    public UserDAO() {
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO User(name, email, password, role, admin_id, student_id, major, "
                   + "social_id, phone_number, salt, id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUserRole());
            statement.setString(10, user.getSalt());
    
            switch (user) {
                case Admin admin -> {
                    statement.setInt(5, admin.getAdminId());
                    statement.setNull(6, Types.VARCHAR);
                    statement.setNull(7, Types.VARCHAR);
                    statement.setNull(8, Types.VARCHAR);
                    statement.setNull(9, Types.VARCHAR);
                    statement.setString(11, admin.getUserId());
                }
    
                case Student student -> {
                    statement.setNull(5, Types.VARCHAR);
                    statement.setString(6, student.getStudentId());
                    statement.setString(7, student.getMajor());
                    statement.setNull(8, Types.VARCHAR);
                    statement.setNull(9, Types.VARCHAR);
                    statement.setString(11, student.getUserId());
                }
    
                case ExternalBorrower externalBorrower -> {
                    statement.setNull(5, Types.VARCHAR);
                    statement.setNull(6, Types.VARCHAR);
                    statement.setNull(7, Types.VARCHAR);
                    statement.setString(8, externalBorrower.getSocialId());
                    statement.setString(9, externalBorrower.getPhoneNumber());
                    statement.setString(11, externalBorrower.getUserId());
                }
    
                default -> throw new IllegalStateException("Unexpected value: " + user);
            }
    
            statement.executeUpdate();
        }
    }
    

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE User SET name = ?, email = ?, major = ?, phone_number = ? WHERE id = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
    
            switch (user) {
                case Student student -> {
                    statement.setString(3, student.getMajor());
                    statement.setNull(4, Types.VARCHAR);
                }
                case ExternalBorrower externalBorrower -> {
                    statement.setString(4, externalBorrower.getPhoneNumber());
                    statement.setNull(3, Types.VARCHAR);
                }

                default -> throw new IllegalStateException("Unexpected value: " + user);
            }
            statement.setString(5, user.getUserId());
    
            statement.executeUpdate();
        }
    }
    

    public void deleteUserById(String userId) throws SQLException {
        String sql = "DELETE FROM User WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            statement.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email, password, role, admin_id, student_id, major, social_id, phone_number FROM User";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("role");
                System.out.println("Role: " + role);

                User user = null;

                switch (role) {
                    case "ADMIN":
                        System.out.println("Admin detected");
                        user = new Admin(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getInt("admin_id"));
                        break;

                    case "STUDENT":
                        System.out.println("Student detected");
                        user = new Student(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("student_id"),
                                rs.getString("major"));
                        break;

                    case "EXTERNAL_BORROWER":
                        System.out.println("External Borrower detected");
                        user = new ExternalBorrower(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("social_id"),
                                rs.getString("phone_number"));
                        break;
                }

                if (user != null) {
                    users.add(user);
                } else {
                    System.out.println("No user created for role: " + role);
                }
            }
        }

        return users;
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, name, email, password, student_id, major FROM User WHERE role = 'STUDENT'";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("student_id"),
                        rs.getString("major")));
            }
        }

        return students;
    }

    public List<ExternalBorrower> getAllExternalBorrowers() throws SQLException {
        List<ExternalBorrower> externalBorrowers = new ArrayList<>();
        String sql = "SELECT id, name, email, password, social_id, phone_number FROM User WHERE role = 'EXTERNAL_BORROWER'";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                externalBorrowers.add(new ExternalBorrower(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("social_id"),
                        rs.getString("phone_number")));
            }
        }

        return externalBorrowers;
    }

    public User getUserById(String userId) throws SQLException {
        String sql = "SELECT id, name, email, password, role, admin_id, student_id, major, social_id, phone_number FROM User WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                switch (role) {
                    case "ADMIN":
                        return new Admin(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getInt("admin_id"));

                    case "STUDENT":
                        return new Student(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("student_id"),
                                rs.getString("major"));

                    case "EXTERNAL_BORROWER":
                        return new ExternalBorrower(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("social_id"),
                                rs.getString("phone_number"));
                }
            }
        }

        return null;
    }

    public int countUser() throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM User";
        
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } 
        
        return count;
    }

    public String fetchUserNameFromUserId(String userId) throws SQLException {
        String sql = "SELECT name FROM User WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }
        }

        return null;
    }

    public Map<String, Object> getPasswordHashAndSalt(String userId) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        String sql = "SELECT password, salt FROM User WHERE id = ?";
    
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
    
            if (rs.next()) {
                result.put("password", rs.getString("password"));  
                result.put("salt", Base64.getDecoder().decode(rs.getString("salt"))); 
            }
        }
    
        return result;
    }
}
