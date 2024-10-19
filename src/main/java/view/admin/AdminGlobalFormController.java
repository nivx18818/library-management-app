package view.admin;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import util.Animation;

import java.util.ArrayList;
import java.util.List;

public class AdminGlobalFormController {


    private static AdminGlobalFormController controller;
    @FXML
    private Pane pagingPane;
    private final List<String[]> borrowedBooksData = preLoadBorrowedBooksData();
    private final List<String[]> booksData = preLoadBooksData();
    private final List<String[]> usersData = preLoadUsersData();
    private final List<String[]> adminData = preLoadAdminData();

    public AdminGlobalFormController() {
        controller = this;
    }

    public static AdminGlobalFormController getInstance() {
        return controller;
    }

    public void initialize() {
        System.out.println("Admin Global Form initialized");

        Animation.fadeInRight(pagingPane);
    }

    public Pane getPagingPane() {
        return pagingPane;
    }

    public void setPagingPane(Pane pagingPane) {
        this.pagingPane = pagingPane;
    }

    public List<String[]> preLoadBorrowedBooksData() {
        List<String[]> data = new ArrayList<>();

        data.add(new String[]{"Hoang Duy Thinh", "23020708", "1", "13-04-2024", "14-08-2024"});
        data.add(new String[]{"Luis Suarez", "23020710", "1", "13-11-2024", "14-08-2024"});
        data.add(new String[]{"Picasso", "23020714", "1", "13-08-2022", "14-08-2024"});
        data.add(new String[]{"Van Gogh", "23020715", "1", "13-08-2024", "14-08-2024"});
        data.add(new String[]{"Cristiano Ronaldo", "23020712", "1", "13-08-2024", "14-08-2024"});
        data.add(new String[]{"Lionel Messi", "23020713", "1", "13-08-2024", "14-08-2024"});

        return data;
    }

    public List<String[]> preLoadBooksData() {
        List<String[]> data = new ArrayList<>();

        data.add(new String[]{"1", "https://play-lh.googleusercontent.com/EeGWXdlTJKCf7wU7_oXaLc1YUGp4fLpd_I6d7WmUrtuj5B7Jl982cxLaypzS5Q9ijhN0lXfxi9hrZljL7Ts"
                , "The Great Gatsby", "Education", "F. Scott Fitzgerald", "Borrowed"});
        data.add(new String[]{"2", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS66i6hTBkniGtDdwxyi4hA3PFm2mJ0GUIDxw&s", "To " +
                "Kill a Mockingbird", "Education", "Harper Lee", "Available"});
        data.add(new String[]{"3", "https://thuviensach.vn/img/news/2022/09/larger/1011-1984-1.jpg?v=8882", "1984", "Education", "George Orwell",
                "Available"});
        data.add(new String[]{"4", "https://play-lh.googleusercontent.com/f1jkKDk5wKz1CZMyNjOR7klTu-ORIZs9sBMWSOVtd09GE6ulfiW5M4FmWrS54CZmCDiZ", "Pride & Prejudice",
                "Education", "J.D. Salinger", "Available"});
        data.add(new String[]{"5", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYqVdifswPLs8J53knQLpfO0dYIVMq4Mu14w&s", "Sherlock Holmes",
                "Detective", "Arthur Conan Doyle", "Borrowed"});
        data.add(new String[]{"6", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQapwj529X6xqxmWUlrZAbQLhi-jEpU1-gx8A&s", "Dracula", "Horror",
                "Bram Stoker", "Available"});
        data.add(new String[]{"7", "https://www.thejapanshop.com/cdn/shop/products/new_doc_91_1_1280x.jpg?v=1571438916", "Doraemon",
                "Comic", "Fujko F Fujio", "Borrowed"});

        return data;
    }

    public List<String[]> preLoadUsersData() {
        List<String[]> data = new ArrayList<>();

        data.add(new String[]{"Student", "Hoang Duy Thinh", "CN1 - Information Technology",
                "23020708@vnu.edu.vn", "23020708"});
        data.add(new String[]{"External Borrower", "Pham Manh Hung", "0916621455", "hungmanh" +
                "@gmail.com", "037205005003"});

        return data;
    }

    public List<String[]> preLoadAdminData() {
        List<String[]> data = new ArrayList<>();

        data.add(new String[]{"H D Thịnh", "23020708@vnu.edu.vn"});
        data.add(new String[]{"N X Vinh", "23020101@vnu.edu.vn"});
        data.add(new String[]{"L M Tường", "23020102@vnu.edu.vn"});
        data.add(new String[]{"N Đ Nguyên", "23020103@vnu.edu.vn"});

        return data;
    }


    public List<String[]> getBorrowedBooksData() {
        return borrowedBooksData;
    }

    public List<String[]> getBooksData() {
        return booksData;
    }

    public List<String[]> getUsersData() {
        return usersData;
    }

    public List<String[]> getAdminData() {
        return adminData;
    }

}
