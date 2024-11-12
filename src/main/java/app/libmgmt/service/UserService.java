package app.libmgmt.service;

import app.libmgmt.dao.UserDAO;
import app.libmgmt.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService() throws SQLException {
        this.userDAO = new UserDAO();
    }

    public void addUser(User user) {
        try {
            userDAO.addUser(user);
            System.out.println("User added successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error adding user", e);
        }
    }

    public void updateUser(User user) {
        try {
            userDAO.updateUser(user);
            System.out.println("User updated successfully");
        } catch (SQLException e) {
            throw new ServiceException("Error updating user", e);
        }
    }

    public void deleteUser(User user) {
        try {
            userDAO.deleteUser(user);
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

    public User getUserById(int userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (SQLException e) {
            throw new ServiceException("Error getting user by id", e);
        }
    }
}
