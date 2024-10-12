# expected project structure

src/
└── main/
    ├── java/
    │   └── app/
    │       └── libmgmt/
    │           ├── Main.java
    │           ├── controller/
    │           │   ├── BookController.java
    │           │   └── LoanController.java
    │           ├── view/
    │           │   └── controller/
    │           │       ├── BookListViewController.java
    │           │       └── BookDetailViewController.java
    │           ├── model/
    │           │   ├── Book.java # Represents a book in the library.
    │           │   ├── User.java # Represents a user (student or librarian).
    │           │   ├── Loan.java # Represents the loan of a book to a user.
    │           │   ├── Author.java # Represents an author of books.
    │           │   └── Category.java # Represents a book category or genre.
    │           ├── dao/
    │           ├── service/ # Business logic
    │           │   ├── BookService.java
    │           │   ├── LoanService.java
    │           │   └── UserService.java
    │           ├── exceptions/ # Custom exceptions
    │           │   ├── DatabaseException.java
    │           │   └── NotFoundException.java
    │           └── util/ # Utility classes
    │               └── DateUtils.java
    └── resources/
        ├── log4j2.xml
        ├── database/
        │   └── my-database-file.db
        └── fxml/
            └── my-view-file.fxml


# Business logic: It can be useful to add a service/ package between controller/ and dao/. This way, your business logic (like processing data or handling complex transactions) is separate from data access (DAO) and UI logic (controller).

# Custom exceptions: Adding a dedicated exceptions/ package can help manage custom exceptions for handling errors (e.g., DatabaseException, BookNotFoundException).

# Utility classes: Consider adding a logging configuration file in resources/ for logging purposes (log4j2.xml or logging.properties) to ensure effective debugging and monitoring.
