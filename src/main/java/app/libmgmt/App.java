package app.libmgmt;

import app.libmgmt.dao.BookDAO;
import app.libmgmt.model.Book;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Library Management ---");
            System.out.println("1. Add a new book");
            System.out.println("2. Update a book");
            System.out.println("3. Delete a book");
            System.out.println("4. View all books");
            System.out.println("5. Search books by condition");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    // Add a new book
                    Book newBook = createBook(scanner);
                    bookDAO.add(newBook);
                    break;
                case 2:
                    // Update a book
                    System.out.print("Enter ISBN of the book to update: ");
                    String isbnToUpdate = scanner.nextLine();
                    Book updatedBook = createBook(scanner);
                    updatedBook.setIsbn(isbnToUpdate); // Set the ISBN to the one provided
                    bookDAO.update(updatedBook);
                    break;
                case 3:
                    // Delete a book
                    System.out.print("Enter ISBN of the book to delete: ");
                    String isbnToDelete = scanner.nextLine();
                    Book bookToDelete = new Book(isbnToDelete, null, null, null, null, 0, 0, 0, 0);
                    bookDAO.delete(bookToDelete);
                    break;
                case 4:
                    // View all books
                    ArrayList<Book> books = bookDAO.selectAll();
                    displayBooks(books);
                    break;
                case 5:
                    // Search books by condition
                    System.out.print("Enter search condition (e.g., title LIKE '%Java%'): ");
                    String condition = scanner.nextLine();
                    ArrayList<Book> searchResults = bookDAO.selectByCondition(condition);
                    displayBooks(searchResults);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }

    // Helper method to create a new Book from user input
    private static Book createBook(Scanner scanner) {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Published Date (yyyy-mm-dd): ");
        String publishedDate = scanner.nextLine();
        System.out.print("Enter Publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter Cover URL: ");
        String coverUrl = scanner.nextLine();
        System.out.print("Enter Available Amount: ");
        int availableAmount = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter Author ID: ");
        int authorId = scanner.nextInt();
        System.out.print("Enter Category ID: ");
        int categoryId = scanner.nextInt();
        System.out.print("Enter Admin ID: ");
        int adminId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        return new Book(isbn, title, publishedDate, publisher, coverUrl, availableAmount, authorId, categoryId, adminId);
    }

    // Helper method to display the list of books
    private static void displayBooks(ArrayList<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.printf("%-15s %-30s %-15s %-20s %-10s %-10s %-10s %-10s\n",
                    "ISBN", "Title", "Published Date", "Publisher", "Amount", "AuthorID", "CategoryID", "AdminID");
            for (Book book : books) {
                System.out.printf("%-15s %-30s %-15s %-20s %-10d %-10d %-10d %-10d\n",
                        book.getIsbn(), book.getTitle(), book.getPublishedDate(), book.getPublisher(),
                        book.getAvailableAmount(), book.getAuthorId(), book.getCategoryId(), book.getAdminId());
            }
        }
    }
}
