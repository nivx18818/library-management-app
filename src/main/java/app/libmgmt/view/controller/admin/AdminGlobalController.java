package app.libmgmt.view.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.EnumUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminGlobalController {

    private static AdminGlobalController controller;

    // Data loaded when starting the application
    private final ObservableList<String[]> borrowedBooksData = FXCollections.observableArrayList(preLoadBorrowedBooksData());
    private final List<String[]> adminsData = preLoadAdminData();

    // Real time load data
    private final ObservableList<String[]> studentsData = FXCollections.observableArrayList();
    private final ObservableList<String[]> guestsData = FXCollections.observableArrayList();
    private final ObservableList<String[]> observableBooksData = FXCollections.observableArrayList();

    // FXML UI components
    @FXML
    private Pane pagingPane;
    @FXML
    private StackPane stackPaneContainer;
    @FXML
    private Pane backgroundPane;
    @FXML
    private HBox globalFormContainer;

    // Constructor and Singleton Pattern
    public AdminGlobalController() {
        controller = this;
    }

    public static AdminGlobalController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("Admin Global Form initialized");
        AnimationUtils.fadeInRight(pagingPane, 1);
    }

    // Data Pre-loading Methods
    private List<String[]> preLoadBorrowedBooksData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Hoang Duy Thinh", "23020708", "1", "13/04/2024", "14/08/2024"});
        data.add(new String[]{"Luis Suarez", "23020710", "1", "13/11/2024", "14/08/2024"});
        data.add(new String[]{"Picasso", "23020714", "1", "13/08/2022", "14/08/2024"});
        data.add(new String[]{"Van Gogh", "23020715", "1", "13/12/2024", "14/08/2024"});
        data.add(new String[]{"Cristiano Ronaldo", "23020712", "1", "13/08/2024", "14/08/2024"});
        data.add(new String[]{"Lionel Messi", "23020713", "1", "13/08/2024", "14/08/2024"});
        return data;
    }

    public List<String[]> preLoadBooksData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"A1", "https://marketplace.canva" +
                ".com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg", "The Great Gatsby", "Education", "F. Scott Fitzgerald", "3", "NXB Trẻ", "13/08/2024"});
        data.add(new String[]{"B2", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcS66i6hTBkniGtDdwxyi4hA3PFm2mJ0GUIDxw&s", "To Kill a Mockingbird", "Education", "Harper Lee", "4", "NXB Trẻ", "13/08/2024"});
        data.add(new String[]{"C3", "https://thuviensach.vn/img/news/2022/09/larger/1011-1984-1" +
                ".jpg?v=8882", "1984", "Education", "George Orwell", "0", "NXB Trẻ", "13/08/2024"});
        data.add(new String[]{"D4", "https://play-lh.googleusercontent" +
                ".com/f1jkKDk5wKz1CZMyNjOR7klTu-ORIZs9sBMWSOVtd09GE6ulfiW5M4FmWrS54CZmCDiZ", "Pride & Prejudice", "Education", "J.D. Salinger", "12", "NXB Trẻ", "13/08/2024"});
        data.add(new String[]{"E5", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcRYqVdifswPLs8J53knQLpfO0dYIVMq4Mu14w&s", "Sherlock Holmes", "Detective", "Arthur Conan Doyle", "32", "NXB Trẻ", "13/08/2024"});
        data.add(new String[]{"F6", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcQapwj529X6xqxmWUlrZAbQLhi-jEpU1-gx8A&s", "Dracula", "Horror", "Bram Stoker", "0", "NXB Trẻ", "13/08/2024"});
        data.add(new String[]{"G7", "https://www.thejapanshop" +
                ".com/cdn/shop/products/new_doc_91_1_1280x.jpg?v=1571438916", "Doraemon", "Comic", "Fujko F Fujio", "0", "NXB Trẻ", "13/08/2024"});
        return data;
    }

    public List<String[]> preLoadStudentsData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Student", "Hoang Duy Thinh", "CN1 - Information Technology", "23020708@vnu.edu.vn", "23020708"});
        data.add(new String[]{"Student", "Hoang Duy Thinh", "CN1 - Information Technology", "23020708@vnu.edu.vn", "23020709"});
        return data;
    }

    public List<String[]> preLoadGuestsData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Student", "Ho Hoai Ho", "0941512278", "2302ad21@vnu.edu.vn", "037205005003"});
        return data;
    }

    private List<String[]> preLoadAdminData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"H D Thịnh", "23020708@vnu.edu.vn"});
        data.add(new String[]{"N X Vinh", "23020101@vnu.edu.vn"});
        data.add(new String[]{"L M Tường", "23020102@vnu.edu.vn"});
        data.add(new String[]{"N Đ Nguyên", "23020103@vnu.edu.vn"});
        return data;
    }

    // CRUD Operations for Books
    public static void insertBooksData(String[] bookData) {
        // data format: [id, coverURL, name, type, author, quantity, publisher, publishedDate]

        // test
        String[] newArray = new String[bookData.length + 1];
        newArray[0] = 23 + "";
        System.arraycopy(bookData, 0, newArray, 1, bookData.length);
        getInstance().observableBooksData.add(newArray);

        // TODO: Insert book data to database
    }

    public void updateBookData(String[] updatedData, String bookId) {
        for (int i = 0; i < observableBooksData.size(); i++) {
            String[] existingData = observableBooksData.get(i);
            if (existingData[0].equals(bookId)) {
                observableBooksData.set(i, updatedData);
                return;
            }
        }
        System.out.println("Book data not found. Unable to update.");
    }

    public void deleteBookDataById(String id) {
        for (String[] data : observableBooksData) {
            if (data[0].equals(id)) {
                observableBooksData.remove(data);
                break;
            }
        }
    }

    // CRUD Operations for Users
    public void updateUserData(String[] updatedData, EnumUtils.UserType userType) {
        ObservableList<String[]> userList = getUserListByType(userType);
        for (int i = 0; i < userList.size(); i++) {
            String[] existingData = userList.get(i);
            if (existingData[4].equals(updatedData[4])) {
                userList.set(i, updatedData);
                return;
            }
        }
        System.out.println("User data not found. Unable to update.");
    }

    public void deleteUserById(EnumUtils.PopupList popupType, String id) {
        ObservableList<String[]> userList = popupType == EnumUtils.PopupList.STUDENT_DELETE ? studentsData : guestsData;
        for (String[] data : userList) {
            if (data[4].equals(id)) {
                userList.remove(data);
                return;
            }
        }
        System.out.println("User data not found. Unable to delete.");
    }

    // Helper Methods
    private ObservableList<String[]> getUserListByType(EnumUtils.UserType userType) {
        return userType == EnumUtils.UserType.STUDENT ? studentsData : guestsData;
    }

    // Getter and Setter Methods for Data
    public List<String[]> getBorrowedBooksData() {
        return borrowedBooksData;
    }

    public List<String[]> getAdminData() {
        return adminsData;
    }

    public ObservableList<String[]> getObservableBookData() {
        return observableBooksData;
    }

    public ObservableList<String[]> getObservableUsersData(EnumUtils.UserType userType) {
        return getUserListByType(userType);
    }

    public int getTotalUsersCount() {
        return studentsData.size() + guestsData.size();
    }

    public void setObservableBookData(List<String[]> data) {
        observableBooksData.setAll(data);
    }

    public void setStudentsData(List<String[]> data) {
        studentsData.setAll(data);
    }

    public void setGuestsData(List<String[]> data) {
        guestsData.setAll(data);
    }

    // Getter Methods for UI components
    public Pane getPagingPane() {
        return pagingPane;
    }

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

    public HBox getGlobalFormContainer() {
        return globalFormContainer;
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }

}
