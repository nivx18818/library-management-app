package app.libmgmt.service;

import app.libmgmt.dao.UserDAO;
import app.libmgmt.model.User;
import app.libmgmt.model.Student;
import app.libmgmt.model.ExternalBorrower;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    private String hashPassword(String password, byte[] salt) throws Exception {
        int iterations = 10000;
        int keyLength = 256;
    
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
    
        return Base64.getEncoder().encodeToString(hash);
    }
    

    private byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16]; 
        sr.nextBytes(salt);
        return salt;
    }

    public void addUser(User user) {
        try {
            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(user.getPassword(), salt);
            user.resetPassword(hashedPassword); 
            user.setSalt(Base64.getEncoder().encodeToString(salt)); 
    
            userDAO.addUser(user); 
            System.out.println("User added successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error adding user", e);
        } catch (Exception e) {
            throw new ServiceException("Error hashing password", e);
        }
    }

    public void updateUser(User user) {
        try {
            // String hashedPassword = hashPassword(user.getPassword(), generateSalt());
            // user.resetPassword(hashedPassword);
            userDAO.updateUser(user);
            System.out.println("User updated successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error updating user", e);
        } catch (Exception e) {
            throw new ServiceException("Error hashing password", e);
        }
    }

    public void deleteUserById(String userId) {
        try {
            userDAO.deleteUserById(userId);
            System.out.println("User deleted successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error deleting user", e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            throw new ServiceException("Error getting all users", e);
        }
    }

    public List<Student> getAllStudents() {
        try {
            return userDAO.getAllStudents();
        } catch (SQLException e) {
            throw new ServiceException("Error getting all students", e);
        }
    }

    public List<ExternalBorrower> getAllExternalBorrowers() {
        try {
            return userDAO.getAllExternalBorrowers();
        } catch (SQLException e) {
            throw new ServiceException("Error getting all external borrowers", e);
        }
    }

    public User getUserById(String userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (SQLException e) {
            throw new ServiceException("Error getting user by id", e);
        }
    }

    public String fetchUserNameFromUserId(String userId) {
        try {
            return userDAO.fetchUserNameFromUserId(userId);
        } catch (SQLException e) {
            throw new ServiceException("Error fetching user name from user id", e);
        }
    }

    public boolean verifyPassword(String userId, String password) throws SQLException {
        try {
            Map<String, Object> passwordData = userDAO.getPasswordHashAndSalt(userId);
            if (passwordData.isEmpty()) {
                System.out.println("Account not found");
                return false; 
            }
    
            String storedHash = (String) passwordData.get("password");
            byte[] salt = (byte[]) passwordData.get("salt"); 
    
            String hashedPassword = hashPassword(password, salt); 
            System.out.println("Password hash: " + hashedPassword);
    
            return hashedPassword.equals(storedHash); 
        } catch (SQLException e) {
            throw new ServiceException("Error checking account", e);
        } catch (Exception e) {
            throw new ServiceException("Error hashing password during login", e);
        }
    }

}
