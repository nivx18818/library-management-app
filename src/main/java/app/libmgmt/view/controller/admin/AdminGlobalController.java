package app.libmgmt.view.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import app.libmgmt.model.Book;
import app.libmgmt.util.AnimationUtils;
import app.libmgmt.util.EnumUtils;
import app.libmgmt.service.BookService;

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
    private final ObservableList<Book> observableBooksData = FXCollections.observableArrayList();
    private final BookService bookService = new BookService();

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

    public List<Book> preLoadBooksData() {
        // List<Book> data = new ArrayList<>();
        // data.add(new Book(new String[]{"1", "https://i.imgur.com/1.jpg", "The Alchemist", "Novel", "Paulo Coelho", "10", "HarperCollins", "1988"}));
        // data.add(new Book(new String[]{"2", "https://i.imgur.com/2.jpg", "The Da Vinci Code", "Mystery", "Dan Brown", "5", "Doubleday", "2003"}));
        // data.add(new Book(new String[]{"3", "https://i.imgur.com/3.jpg", "The Great Gatsby", "Novel", "F. Scott Fitzgerald", "7", "Scribner", "1925"}));
        // return data;
        return bookService.getAllBooks();
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
    public void insertBooksData(String[] bookData) {
        // data format: [id, coverURL, name, type, author, quantity, publisher, publishedDate]
        Book book = new Book(bookData);
        // test
        // String[] newArray = new String[bookData.length + 1];
        // newArray[0] = 23 + "";
        // System.arraycopy(bookData, 0, newArray, 1, bookData.length);
        // getInstance().observableBooksData.add(newArray);
        getInstance().observableBooksData.add(book);
        System.out.println("Book data: " + book.getPublishedDate());
        System.out.println("Book data: " + bookData[0] + " " + bookData[1] + " " + bookData[2] + " " + bookData[3] + " " + bookData[4] + " " + bookData[5] + " " + bookData[6] + " " + bookData[7]);
        // TODO: Insert book data to database
        bookService.addBook(book); 
    }

    public void updateBookData(String[] updatedData, String bookId) {
        // for (int i = 0; i < observableBooksData.size(); i++) {
        //     String[] existingData = observableBooksData.get(i);
        //     if (existingData[0].equals(bookId)) {
        //         observableBooksData.set(i, updatedData);
        //         return;
        //     }
        // }
        Book book = new Book(updatedData);
        bookService.updateBook(book);
        AdminBooksLayoutController.getInstance().refreshBooksList();
    }

    public void deleteBookDataById(String id) {
        // for (String[] data : observableBooksData) {
        //     if (data[0].equals(id)) {
        //         observableBooksData.remove(data);
        //         break;
        //     }
        // }
        bookService.deleteBook(id);
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

    //fetchBooksFromDatabase
    public List<Book> fetchBooksFromDatabase() {
        return bookService.getAllBooks();
    }

    // Getter and Setter Methods for Data
    public List<String[]> getBorrowedBooksData() {
        return borrowedBooksData;
    }

    public List<String[]> getAdminData() {
        return adminsData;
    }

    public ObservableList<Book> getObservableBookData() {
        return observableBooksData;
    }

    public ObservableList<String[]> getObservableUsersData(EnumUtils.UserType userType) {
        return getUserListByType(userType);
    }

    public int getTotalUsersCount() {
        return studentsData.size() + guestsData.size();
    }

    public void setObservableBookData(List<Book> data) {
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
