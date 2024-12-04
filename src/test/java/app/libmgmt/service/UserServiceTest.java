package app.libmgmt.service;

import app.libmgmt.model.User;
import app.libmgmt.model.ExternalBorrower;
import app.libmgmt.model.Student;
import app.libmgmt.dao.DatabaseConnection;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserServiceTest {

    private UserService userService;

    @BeforeAll
    static void setUpClass() throws SQLException {
        DatabaseConnection.isTesting = true;
    }

    @BeforeEach
    void setUp() throws Exception {
        Connection connection = DatabaseConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS User");
            statement.execute("CREATE TABLE User (" +
                    "id TEXT PRIMARY KEY, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "password TEXT, " +
                    "role TEXT, " +
                    "admin_id INTEGER, " +
                    "student_id TEXT, " +
                    "major TEXT, " +
                    "social_id TEXT, " +
                    "phone_number TEXT, " +
                    "salt TEXT)");

            // Prepopulate with test data
            statement.execute("INSERT INTO User VALUES (" +
                    "'1', 'Admin User', 'admin@test.com', 'password', 'ADMIN', 1, NULL, NULL, NULL, NULL, 'salt')");
            statement.execute("INSERT INTO User VALUES (" +
                    "'2', 'Student User', 'student@test.com', 'password', 'STUDENT', NULL, 'S1', 'CS', NULL, NULL, 'salt')");
            statement.execute("INSERT INTO User VALUES (" +
                    "'3', 'External User', 'external@test.com', 'password', 'EXTERNAL_BORROWER', NULL, NULL, NULL, '123456789', '1234567890', 'salt')");
        }

        userService = new UserService();
    }

    @AfterAll
    static void tearDownClass() {
        DatabaseConnection.isTesting = false;
    }

    @Test
    void testAddUser() {
        User user = new Student("4", "New User", "newuser@test.com", "password", "S2", "IT");
        userService.addUser(user);
        Student retrievedUser = (Student) userService.getUserById("4");
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals("New User", retrievedUser.getName());
        Assertions.assertEquals("newuser@test.com", retrievedUser.getEmail());
        Assertions.assertEquals("S2", retrievedUser.getStudentId());
        Assertions.assertEquals("IT", retrievedUser.getMajor());
    }

    // Admin will not be updated by this method
    @Test
    void testUpdateUser() {
        User user = userService.getUserById("2");
        user.setName("Updated Student User");
        userService.updateUser(user);
        User updatedUser = userService.getUserById("2");
        Assertions.assertEquals("Updated Student User", updatedUser.getName());
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById("1");
        User deletedUser = userService.getUserById("1");
        Assertions.assertNull(deletedUser);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        Assertions.assertEquals(3, users.size());

        Assertions.assertEquals("Admin User", users.get(0).getName());
        Assertions.assertEquals("Student User", users.get(1).getName());
        Assertions.assertEquals("External User", users.get(2).getName());

        Assertions.assertEquals("admin@test.com", users.get(0).getEmail());
        Assertions.assertEquals("student@test.com", users.get(1).getEmail());
        Assertions.assertEquals("external@test.com", users.get(2).getEmail());

        Assertions.assertEquals("ADMIN", users.get(0).getUserRole());
        Assertions.assertEquals("STUDENT", users.get(1).getUserRole());
        Assertions.assertEquals("EXTERNAL_BORROWER", users.get(2).getUserRole());

        Assertions.assertEquals("S1", ((Student) users.get(1)).getStudentId());
        Assertions.assertEquals("CS", ((Student) users.get(1)).getMajor());
        Assertions.assertEquals("123456789", ((ExternalBorrower) users.get(2)).getSocialId());
        Assertions.assertEquals("1234567890", ((ExternalBorrower) users.get(2)).getPhoneNumber());
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = userService.getAllStudents();
        Assertions.assertEquals(1, students.size());
        Assertions.assertEquals("Student User", students.get(0).getName());
        Assertions.assertEquals("S1", students.get(0).getStudentId());
        Assertions.assertEquals("CS", students.get(0).getMajor());
    }

    @Test
    void testGetAllExternalBorrowers() {
        List<ExternalBorrower> externalBorrowers = userService.getAllExternalBorrowers();
        Assertions.assertEquals(1, externalBorrowers.size());
        Assertions.assertEquals("External User", externalBorrowers.get(0).getName());
        Assertions.assertEquals("123456789", externalBorrowers.get(0).getSocialId());
        Assertions.assertEquals("1234567890", externalBorrowers.get(0).getPhoneNumber());
    }

    @Test
    void testGetUserById() {
        User retrievedUser = userService.getUserById("1");
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals("Admin User", retrievedUser.getName());
        Assertions.assertEquals("admin@test.com", retrievedUser.getEmail());
    }

    @Test
    void testCountUser() {
        int count = userService.countUser();
        Assertions.assertEquals(3, count);
    }
}
