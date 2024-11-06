package app.libmgmt.dao;

import app.libmgmt.model.Admin;
import app.libmgmt.model.ExternalBorrower;
import app.libmgmt.model.Student;
import app.libmgmt.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addUser(User user) {
        String sql = "INSERT INTO User(id, name, email, password, role, admin_id, student_id, major, social_id, phone_number)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getUserId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getUserRole());

            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                statement.setString(6, admin.getAdminId());
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.VARCHAR);
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.VARCHAR);

            } else if (user instanceof Student) {
                Student student = (Student) user;
                statement.setNull(6, Types.INTEGER);
                statement.setString(7, student.getStudentId());
                statement.setString(8, student.getMajor());
                statement.setNull(9, Types.VARCHAR);
                statement.setNull(10, Types.VARCHAR);

            } else if (user instanceof ExternalBorrower) {
                ExternalBorrower externalBorrower = (ExternalBorrower) user;
                statement.setNull(6, Types.INTEGER);
                statement.setNull(7, Types.VARCHAR);
                statement.setNull(8, Types.VARCHAR);
                statement.setString(9, externalBorrower.getSocialId());
                statement.setString(10, externalBorrower.getPhoneNumber());
            }

            statement.executeUpdate();
            System.out.println("User added");
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE User SET name = ?, email = ?, password = ?, role = ?, admin_id = ?, student_id = ?," +
                "major = ?, social_id = ?, phone_number= ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUserRole());

            if (user instanceof Student) {
                Student student = (Student) user;
                statement.setNull(5, Types.VARCHAR);
                statement.setString(6, student.getStudentId());
                statement.setString(7, student.getMajor());
                statement.setNull(8, Types.VARCHAR);
                statement.setNull(9, Types.VARCHAR);

            } else if (user instanceof ExternalBorrower) {
                ExternalBorrower externalBorrower = (ExternalBorrower) user;
                statement.setNull(5, Types.VARCHAR);
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.VARCHAR);
                statement.setString(8, externalBorrower.getSocialId());
                statement.setString(9, externalBorrower.getPhoneNumber());
            }

            statement.setInt(10, user.getUserId());
            statement.executeUpdate();
            System.out.println("User updated");
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public void deleteUser(User user) {
        String sql = "DELETE FROM User WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getUserId());
            statement.executeUpdate();
            System.out.println("User deleted");

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("role");

                User user = null;
                if ("admin".equals(role)) {
                    user = new Admin(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("admin_id")
                    );

                } else if ("student".equals(role)) {
                    user = new Student(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("student_id"),
                            rs.getString("major")
                    );

                } else if ("external_borrower".equals(role)) {
                    user = new ExternalBorrower(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("social_id"),
                            rs.getString("phone_number")
                    );
                }

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }

        return users;
    }

    public User selectUserById(int userId) {
        String sql = "SELECT * FROM User WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                if ("admin".equals(role)) {
                    return new Admin(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("admin_id")
                    );

                } else if ("student".equals(role)) {
                    return new Student(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("student_id"),
                            rs.getString("major")
                    );

                } else if ("external_borrower".equals(role)) {
                    return new ExternalBorrower(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("social_id"),
                            rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }

        return null;
    }
}