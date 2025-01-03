# Project Structure

```txt
Directory structure:
└── ichigawr-library-management-app/
    ├── README.md
    ├── LICENSE
    ├── pom.xml
    ├── docs/
    │   ├── CONTRIBUTING.md
    │   ├── DatabaseERD.drawio
    │   ├── ModelClassDiagram.drawio
    │   └── project_structure.bash
    └── src/
        ├── main/
        │   ├── java/
        │   │   ├── module-info.java
        │   │   └── app/
        │   │       └── libmgmt/
        │   │           ├── App.java
        │   │           ├── dao/
        │   │           │   ├── BookDAO.java
        │   │           │   ├── DatabaseConnection.java
        │   │           │   ├── LoanDAO.java
        │   │           │   └── UserDAO.java
        │   │           ├── initializer/
        │   │           │   ├── AdminInitializer.java
        │   │           │   └── UserInitializer.java
        │   │           ├── model/
        │   │           │   ├── Admin.java
        │   │           │   ├── Book.java
        │   │           │   ├── ExternalBorrower.java
        │   │           │   ├── Loan.java
        │   │           │   ├── Student.java
        │   │           │   └── User.java
        │   │           ├── service/
        │   │           │   ├── BookService.java
        │   │           │   ├── LoanService.java
        │   │           │   ├── MultiThreadedJDBC.java
        │   │           │   ├── ServiceException.java
        │   │           │   ├── UserService.java
        │   │           │   └── external/
        │   │           │       └── GoogleBooksApiService.java
        │   │           ├── util/
        │   │           │   ├── AnimationUtils.java
        │   │           │   ├── ChangeScene.java
        │   │           │   ├── DateUtils.java
        │   │           │   ├── EnumUtils.java
        │   │           │   ├── QRCodeGenerator.java
        │   │           │   └── RegExPatterns.java
        │   │           └── view/
        │   │               └── controller/
        │   │                   ├── ChangeCredentialsDialogController.java
        │   │                   ├── EmptyDataNotificationDialogController.java
        │   │                   ├── ForgotPasswordDialogController.java
        │   │                   ├── LoadingPageController.java
        │   │                   ├── LoginController.java
        │   │                   ├── LogoutDialogController.java
        │   │                   ├── StandbyScreenController.java
        │   │                   ├── admin/
        │   │                   │   ├── AdminAddBookApiController.java
        │   │                   │   ├── AdminAddBookDialogController.java
        │   │                   │   ├── AdminAddUserDialogController.java
        │   │                   │   ├── AdminAllBorrowedBookBarController.java
        │   │                   │   ├── AdminAllBorrowedBookViewDialogController.java
        │   │                   │   ├── AdminBookBarApiController.java
        │   │                   │   ├── AdminBookBarController.java
        │   │                   │   ├── AdminBookEditDialogController.java
        │   │                   │   ├── AdminBookViewDialogController.java
        │   │                   │   ├── AdminBooksLayoutController.java
        │   │                   │   ├── AdminBorrowedBookViewBarController.java
        │   │                   │   ├── AdminBorrowedBookViewDialogController.java
        │   │                   │   ├── AdminBorrowedBooksBarController.java
        │   │                   │   ├── AdminBorrowedBooksLayoutController.java
        │   │                   │   ├── AdminDashboardController.java
        │   │                   │   ├── AdminDashboardOverdueBarController.java
        │   │                   │   ├── AdminDeleteConfirmationDialogController.java
        │   │                   │   ├── AdminGlobalController.java
        │   │                   │   ├── AdminHeaderController.java
        │   │                   │   ├── AdminNavigationController.java
        │   │                   │   ├── AdminUsersEditDialogController.java
        │   │                   │   ├── AdminUsersGuestBarController.java
        │   │                   │   ├── AdminUsersLayoutController.java
        │   │                   │   ├── AdminUsersStudentBarController.java
        │   │                   │   ├── AdminUsersViewDialogController.java
        │   │                   │   └── BookwormAdminBarController.java
        │   │                   └── user/
        │   │                       ├── UserBookBarController.java
        │   │                       ├── UserBooksLayoutController.java
        │   │                       ├── UserBorrowedBookBarController.java
        │   │                       ├── UserBorrowedBookViewBarController.java
        │   │                       ├── UserBorrowedBookViewDialogController.java
        │   │                       ├── UserBorrowedBooksConfirmationDialogController.java
        │   │                       ├── UserCatalogBorrowedBookBarController.java
        │   │                       ├── UserCatalogController.java
        │   │                       ├── UserDashboardController.java
        │   │                       ├── UserGlobalController.java
        │   │                       ├── UserHeaderController.java
        │   │                       ├── UserNavigationController.java
        │   │                       ├── UserReturnBookConfirmationDialogController.java
        │   │                       ├── UserReturnedBookViewBarController.java
        │   │                       └── UserReturnedBookViewDialogController.java
        │   └── resources/
        │       ├── assets/
        │       │   ├── gif/
        │       │   ├── icon/
        │       │   └── img/
        │       ├── database/
        │       │   └── database.db
        │       ├── fxml/
        │       │   ├── bookworm-admin-bar.fxml
        │       │   ├── change-credentials-dialog.fxml
        │       │   ├── empty-data-notification-dialog.fxml
        │       │   ├── forgot-password-dialog.fxml
        │       │   ├── loading-form.fxml
        │       │   ├── login-form.fxml
        │       │   ├── logout-dialog.fxml
        │       │   ├── standby-screen.fxml
        │       │   ├── admin/
        │       │   │   ├── admin-add-book-api-dialog.fxml
        │       │   │   ├── admin-add-book-dialog.fxml
        │       │   │   ├── admin-add-user-dialog.fxml
        │       │   │   ├── admin-all-borrowed-book-bar.fxml
        │       │   │   ├── admin-all-borrowed-books-view-dialog.fxml
        │       │   │   ├── admin-book-bar-api.fxml
        │       │   │   ├── admin-book-bar.fxml
        │       │   │   ├── admin-book-edit-dialog.fxml
        │       │   │   ├── admin-book-view-dialog.fxml
        │       │   │   ├── admin-books-form.fxml
        │       │   │   ├── admin-borrowed-book-bar.fxml
        │       │   │   ├── admin-borrowed-book-view-bar.fxml
        │       │   │   ├── admin-borrowed-book-view-dialog.fxml
        │       │   │   ├── admin-borrowed-books-form.fxml
        │       │   │   ├── admin-dashboard-overdue-bar.fxml
        │       │   │   ├── admin-dashboard.fxml
        │       │   │   ├── admin-delete-confirmation-dialog.fxml
        │       │   │   ├── admin-global-layout.fxml
        │       │   │   ├── admin-header.fxml
        │       │   │   ├── admin-navigation.fxml
        │       │   │   ├── admin-users-edit-dialog.fxml
        │       │   │   ├── admin-users-form.fxml
        │       │   │   ├── admin-users-guest-bar.fxml
        │       │   │   ├── admin-users-student-bar.fxml
        │       │   │   └── admin-users-view-dialog.fxml
        │       │   └── user/
        │       │       ├── user-book-bar.fxml
        │       │       ├── user-books-layout.fxml
        │       │       ├── user-borrowed-book-bar.fxml
        │       │       ├── user-borrowed-books-confirmation-dialog.fxml
        │       │       ├── user-borrowed-view-bar.fxml
        │       │       ├── user-borrowed-view-dialog.fxml
        │       │       ├── user-catalog-borowed-books-bar.fxml
        │       │       ├── user-catalog-form.fxml
        │       │       ├── user-dashboard.fxml
        │       │       ├── user-global-layout.fxml
        │       │       ├── user-header.fxml
        │       │       ├── user-navigation.fxml
        │       │       ├── user-return-book-confirmation-dialog.fxml
        │       │       ├── user-returned-book-view-bar.fxml
        │       │       └── user-returned-book-view-dialog.fxml
        │       └── style/
        │           ├── admin-add-book-dialog.css
        │           ├── admin-book-edit-dialog.css
        │           ├── admin-edit-dialog.css
        │           ├── login-form.css
        │           ├── base/
        │           │   └── common.css
        │           ├── components/
        │           │   ├── chart-pie.css
        │           │   ├── date-picker.css
        │           │   ├── logo-text.css
        │           │   ├── nav-bar.css
        │           │   ├── scroll-bar.css
        │           │   └── spinner.css
        │           └── utils/
        │               └── variables.css
        └── test/
            └── java/
                └── app/
                    └── libmgmt/
                        ├── AppTest.java
                        └── service/
                            ├── BookServiceTest.java
                            ├── LoanServiceTest.java
                            └── UserServiceTest.java
```