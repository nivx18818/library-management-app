package app.libmgmt.view.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.libmgmt.util.AnimationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UserGlobalController {

    private static UserGlobalController controller;

    // Observable Lists for managing data
    private final ObservableList<String[]> borrowedBooksData = FXCollections
            .observableArrayList(preLoadBorrowedBooksData());
    private final ObservableList<String[]> returnedBooksData = FXCollections
            .observableArrayList(preLoadReturnedBooksData());
    private final ObservableList<String[]> observableBooksData = FXCollections.observableArrayList(preLoadBooksData());

    private final Set<String> returnedBookIds = new HashSet<>();

    @FXML
    private Pane backgroundPane;

    @FXML
    private HBox globalFormContainer;

    @FXML
    private Pane pagingPane;

    @FXML
    private StackPane stackPaneContainer;

    // Constructor and Singleton Pattern
    public UserGlobalController() {
        controller = this;
    }

    public static UserGlobalController getInstance() {
        return controller;
    }

    @FXML
    public void initialize() {
        System.out.println("User Global Form initialized");
        AnimationUtils.fadeInRight(pagingPane, 1);
    }

    // Data Pre-loading Methods
    private List<String[]> preLoadBorrowedBooksData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "A1", "https://marketplace.canva" +
                ".com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg",
                "The Great Gatsby", "11/11/2024", "17/11/2024" });
        data.add(new String[] { "B2",
                "https://marketplace.canva.com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg",
                "Story Book", "11/11/2024", "17/11/2024" });
        data.add(new String[] { "C3",
                "https://th.bing.com/th/id/OIP.FTtvbIR52uSVcUrvWBENsAHaLG?w=1170&h=1753&rs=1&pid=ImgDetMain",
                "Story Book", "11/11/2024", "17/11/2024" });
        data.add(new String[] { "D4",
                "https://cdn11.bigcommerce.com/s-ep2pzxabtm/images/stencil/1280w/products/395/816/healinggrief__40416.1554653848.jpg?c=2",
                "Story Book", "11/11/2024", "17/11/2024" });
        return data;
    }

    private List<String[]> preLoadReturnedBooksData() {
        // TODO: Load returned books data from database

        List<String[]> data = new ArrayList<>();
        // data.add(new String[] { "1",
        // "https://th.bing.com/th/id/OIP.FTtvbIR52uSVcUrvWBENsAHaLG?w=1170&h=1753&rs=1&pid=ImgDetMain",
        // "Story Book", "2", "11/11/2024", "17/11/2024" });
        // data.add(new String[] { "1",
        // "https://cdn11.bigcommerce.com/s-ep2pzxabtm/images/stencil/1280w/products/395/816/healinggrief__40416.1554653848.jpg?c=2",
        // "Story Book", "2", "11/11/2024", "17/11/2024" });
        return data;
    }

    public List<String[]> preLoadBooksData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { "A1", "https://marketplace.canva" +
                ".com/EAFaQMYuZbo/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg",
                "The Great Gatsby", "Education", "F. Scott Fitzgerald", "3", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "B2", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcS66i6hTBkniGtDdwxyi4hA3PFm2mJ0GUIDxw&s", "To Kill a Mockingbird", "Education",
                "Harper Lee", "4", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "C3", "https://thuviensach.vn/img/news/2022/09/larger/1011-1984-1" +
                ".jpg?v=8882", "1984", "Education", "George Orwell", "0", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "D4", "https://play-lh.googleusercontent" +
                ".com/f1jkKDk5wKz1CZMyNjOR7klTu-ORIZs9sBMWSOVtd09GE6ulfiW5M4FmWrS54CZmCDiZ", "Pride & Prejudice",
                "Education", "J.D. Salinger", "12", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "E5", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcRYqVdifswPLs8J53knQLpfO0dYIVMq4Mu14w&s", "Sherlock Holmes", "Detective",
                "Arthur Conan Doyle", "32", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "F6", "https://encrypted-tbn0.gstatic" +
                ".com/images?q=tbn:ANd9GcQapwj529X6xqxmWUlrZAbQLhi-jEpU1-gx8A&s", "Dracula", "Horror", "Bram Stoker",
                "0", "NXB Trẻ", "13/08/2024" });
        data.add(new String[] { "G7", "https://www.thejapanshop" +
                ".com/cdn/shop/products/new_doc_91_1_1280x.jpg?v=1571438916", "Doraemon", "Comic", "Fujko F Fujio", "0",
                "NXB Trẻ", "13/08/2024" });
        return data;
    }

    // CRUD Methods
    public void addReturnedBook(String id) {
        for (String[] data : borrowedBooksData) {
            if (data[0].equals(id)) {
                // TODO: Add returned book to database
                returnedBooksData.add(data);
                returnedBookIds.add(id);
                break;
            }
        }
    }

    public String[] getBookDataById(String id) {
        for (String[] data : observableBooksData) {
            if (data[0].equals(id)) {
                return data;
            }
        }
        return null;
    }

    // Getter Methods for Data
    public ObservableList<String[]> getBorrowedBooksData() {
        return borrowedBooksData;
    }

    public ObservableList<String[]> getObservableBooksData() {
        return observableBooksData;
    }

    public ObservableList<String[]> getReturnedBooksData() {
        return returnedBooksData;
    }

    public Set<String> getReturnedBookIds() {
        return returnedBookIds;
    }

    // Add method to check if book is returned
    public boolean isBookReturned(String bookId) {
        return returnedBookIds.contains(bookId);
    }

    // Getter Methods for UI components
    public Pane getPagingPane() {
        return pagingPane;
    }

    public Pane getBackgroundPane() {
        return backgroundPane;
    }

    public StackPane getStackPaneContainer() {
        return stackPaneContainer;
    }

}
