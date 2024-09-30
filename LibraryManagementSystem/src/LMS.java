import java.sql.*;
import java.util.Scanner;

public class LMS {

    private static final Scanner scanner = new Scanner(System.in);

    public static class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/librarydb";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static Connection getConnection() {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome To The Library Management System");
            System.out.println("1.Add a New Book");
            System.out.println("2.Update Book Details");
            System.out.println("3.Delete a Book");
            System.out.println("4.Search for a Book");
            System.out.println("5.Add a New Member");
            System.out.println("6.Loan a Book");
            System.out.println("7.Return a Book");
            System.out.println("8.Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    searchBook();
                    break;
                case 5:
                    addMember();
                    break;
                case 6:
                    loanBook();
                    break;
                case 7:
                    returnBook();
                    break;
                case 8:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Book Operations
    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter year published: ");
        int yearPublished = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO books (title, author, publisher, year_published) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, publisher);
            statement.setInt(4, yearPublished);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new book was inserted successfully!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateBook() {
        System.out.print("Enter Book ID To Update:- ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter New Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter New Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter New Publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter New Year Published: ");
        int yearPublished = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, year_published = ? WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, publisher);
            statement.setInt(4, yearPublished);
            statement.setInt(5, bookId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book details were updated successfully!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteBook() {
        System.out.print("Enter Book ID To Delete:- ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Book was deleted successfully!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void searchBook() {
        System.out.print("Enter Search Term (Title/Author/Year):- ");
        String searchTerm = scanner.nextLine();

        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR year_published LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                int yearPublished = resultSet.getInt("year_published");
                System.out.printf("ID: %d, Title: %s, Author: %s, Publisher: %s, Year: %d%n", bookId, title, author, publisher, yearPublished);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Member Operations
    private static void addMember() {
        System.out.print("Enter Member Name:- ");
        String name = scanner.nextLine();
        System.out.print("Enter Member Email:- ");
        String email = scanner.nextLine();
        System.out.print("Enter Member Phone:- ");
        String phone = scanner.nextLine();

        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new member was inserted successfully!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Loan Operations
    private static void loanBook() {
        System.out.print("Enter Book ID To Loan:- ");
        int bookId = scanner.nextInt();
        System.out.print("Enter Member ID:- ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String sql = "INSERT INTO loans (book_id, member_id, loan_date, return_date) VALUES (?, ?, CURDATE(), NULL)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.setInt(2, memberId);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book was loaned successfully!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void returnBook() {
        System.out.print("Enter loan ID to return: ");
        int loanId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String sql = "UPDATE loans SET return_date = CURDATE() WHERE loan_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book was returned successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
