package app.libmgmt.util;

public class EnumUtils {
    public enum NavigationButton {
        DASHBOARD, CATALOG, BOOKS, USERS, LOGOUT
    }

    public enum UserType {
        ADMIN,
        STUDENT,
        GUEST
    }

    public enum PopupList {
        FORGOT_PASSWORD,
        ADD_BOOK,
        BORROWED_BOOK_CATALOG,
        BOOK_VIEW,
        BOOK_EDIT,
        BOOK_DELETE,
        ALL_BORROWED_BOOKS_VIEW,
        USER_EDIT,
        STUDENT_DELETE,
        GUEST_DELETE,
        CHANGE_CREDENTIALS,
        LOGOUT,
        USER_VIEW,
        ADD_USER,
        RETURN_BOOK,
        ACQUIRE_BOOK,
        EMPTY_DATA_NOTIFICATION,
        API
    }

    public static String[] UETMajor = {"CN1 - Information Technology", "CN2 - Computer Engineering",
            "CN3 - Engineering Physics", "CN4 - Mechanical Engineering", "CN5 - Construction " +
            "Engineering Technology", "CN6 - Mechatronic Engineering Technology", "CN7 - " +
            "Aerospace Technology", "CN8 - Computer Science", "CN9 - Electronic Engineering Technology - " +
            "Telecommunications", "CN10 - Agricultural Technology", "CN11 - Control and " +
            "Automation Engineering", "CN12 - Artificial intelligence", "CN13 - Energy " +
            "Engineering Technology", "CN14 - Information Systems", "CN15 - Computer Networks and" +
            " Data Communications", "CN17 - Computers and Robots"};
}
