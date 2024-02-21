package org.example.assignment2;

import java.sql.*;
import java.sql.DriverManager;
import java.util.*;

/**
 * This class is responsible for managing the database for the Book Application
 */
public class BookDatabaseManager {

    /**
     * Connection Properties
     */
    static final String TITLES_TABLE = "titles";
    static final String AUTHORS_TABLE = "authors";
    static final String AUTHOR_ISBN_TABLE = "authorisbn";

    /**
     * Lists to store information from the database of books, authors, and authorISBNs
     */
    private ArrayList<Book> booksList;
    private ArrayList<Author> authorsList;
    private ArrayList<ArrayList<String>> authorISBNList;


    /**
     * Connection Properties to connect to the database
     */
    private static final String DB_URL = DBProperties.JAVA_TEST_DB_URL;
    private static final String USER = DBProperties.USER;
    private static final String PASS = DBProperties.PASS;

    /**
     * Constructor for the BookDatabaseManager class
     */
    public BookDatabaseManager() {
        booksList = new ArrayList<>();
        authorsList = new ArrayList<>();
        authorISBNList = new ArrayList<>();

        loadBooks();
        loadAuthors();
        loadAuthorISBN();
        createRelationships();
    }

    /**
     * Loads all Books and puts them in an array list
     */
    public void loadBooks() {
        booksList.clear(); // Clears the books list to avoid duplicates

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM " + TITLES_TABLE;
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String isbn = resultSet.getString("isbn");
                String title = resultSet.getString("title");
                int editionNumber = resultSet.getInt("editionNumber");
                String copyRight = resultSet.getString("copyRight");

                Book book = new Book(isbn, title, editionNumber, copyRight);

                booksList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all Authors and puts them in an array list
     */
    public void loadAuthors() {
        authorsList.clear(); // Clears the authors list to avoid duplicates

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM " + AUTHORS_TABLE;
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int authorID = resultSet.getInt("authorID");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                Author author = new Author(authorID, firstName, lastName);

                authorsList.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all AuthorISBNs and puts them in an array list
     */
    public void loadAuthorISBN() {
        authorISBNList.clear(); // Clears the authorISBN list to avoid duplicates

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM " + AUTHOR_ISBN_TABLE;
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ArrayList<String> authorISBN = new ArrayList<>();

                String authorID = String.valueOf(resultSet.getInt("authorID"));
                String isbn = resultSet.getString("isbn");

                authorISBN.add(authorID);
                authorISBN.add(isbn);

                authorISBNList.add(authorISBN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the relationships between books and authors
     */
    public void createRelationships() {
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM " + AUTHOR_ISBN_TABLE;
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int authorID = resultSet.getInt("authorID");
                String isbn = resultSet.getString("isbn");

                for (Book book : booksList) {
                    if (book.getISBN().equals(isbn)) {
                        for (Author author : authorsList) {
                            if (author.getAuthorID() == authorID) {
                                book.addAuthor(author);
                            }
                        }
                    }
                }

                for (Author author : authorsList) {
                    if (author.getAuthorID() == authorID) {
                        for (Book book : booksList) {
                            if (book.getISBN().equals(isbn)) {
                                author.addBook(book);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new Book Object with an author and inserts the new book in the database. If the ISBN exists it will
     * not create a new book. If the author does not exist, it will not create a new book.
     */
    public void createBook(Book newBook, String authorFirstName, String authorLastName) {
        boolean authorExists = false;

        // Checks to see if the book already exists
        for (Book book : booksList) {
            if (book.getISBN().equals(newBook.getISBN())) {
                System.out.println("Book already exists");
                return;
            }
        }

        for (Author author : authorsList) {
            if (author.getFirstName().equals(authorFirstName) && author.getLastName().equals(authorLastName)) {
                authorExists = true;

                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String query = "INSERT INTO " + TITLES_TABLE + " (isbn, title, editionNumber, copyRight) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, newBook.getISBN());
                    preparedStatement.setString(2, newBook.getTitle());
                    preparedStatement.setInt(3, newBook.getEditionNumber());
                    preparedStatement.setString(4, newBook.getCopyright());
                    preparedStatement.executeUpdate();

                    query = "INSERT INTO " + AUTHOR_ISBN_TABLE + " (authorID, isbn) VALUES (?, ?)";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, author.getAuthorID());
                    preparedStatement.setString(2, newBook.getISBN());
                    preparedStatement.executeUpdate();

                    Book book = new Book(newBook.getISBN(), newBook.getTitle(), newBook.getEditionNumber(), newBook.getCopyright());
                    booksList.add(book);
                    book.addAuthor(author);
                    author.addBook(book);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        loadBooks();
        loadAuthors();
        loadAuthorISBN();
        createRelationships();

        if (!authorExists) {
            System.out.println("Author does not exist.");
        }
    }

    /**
     * Reads a book from the database given the ISBN
     */
    public void readBook(String isbn) {
        for (Book book : booksList) {
            if (book.getISBN().equals(isbn)) {
                System.out.println("ISBN: " + book.getISBN());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Edition Number: " + book.getEditionNumber());
                System.out.println("Copy Right: " + book.getCopyright());
                System.out.println("Author(s): ");
                for (Author author : book.getAuthors()) {
                    System.out.println(author.getFirstName() + " " + author.getLastName());
                }
                System.out.println();
            }
        }
    }

    /**
     * Reads all books from the database
     */
    public void readBooks() {
        for (Book book : booksList) {
            System.out.println("ISBN: " + book.getISBN());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Edition Number: " + book.getEditionNumber());
            System.out.println("Copy Right: " + book.getCopyright());
            System.out.println("Author(s): ");
            for (Author author : book.getAuthors()) {
                System.out.println(author.getFirstName() + " " + author.getLastName());
            }
            System.out.println();
        }
    }

    /**
     * Updates a book in the database given the ISBN. If the ISBN does not exist, it will not update the book
     */
    public void updateBook(String isbn, String title, int editionNumber, String copyRight) {
        boolean bookExists = false;

        for (Book book : booksList) {
            if (book.getISBN().equals(isbn)) {
                bookExists = true;

                book.setTitle(title);
                book.setEditionNumber(editionNumber);
                book.setCopyRight(copyRight);

                try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String query = "UPDATE " + TITLES_TABLE + " SET title = ?, editionNumber = ?, copyRight = ? WHERE isbn = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, title);
                    preparedStatement.setInt(2, editionNumber);
                    preparedStatement.setString(3, copyRight);
                    preparedStatement.setString(4, isbn);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!bookExists) {
            System.out.println("Book does not exist.");
        }
    }

    /**
     * Deletes a book from the database given the ISBN. If the ISBN does not exist, it will not delete the book.
     */
    public void deleteBook(String isbn) {
        boolean bookExists = false;
        Book bookToDelete = null;

        for (Book book : booksList) {
            if (book.getISBN().equals(isbn)) {
                bookExists = true;
                bookToDelete = book;
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String query = "DELETE FROM " + AUTHOR_ISBN_TABLE + " WHERE isbn = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, isbn);
                    preparedStatement.executeUpdate();

                    query = "DELETE FROM " + TITLES_TABLE + " WHERE isbn = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, isbn);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!bookExists) {
            System.out.println("Book does not exist.");
        }
        booksList.remove(bookToDelete);
    }

    /**
     * Creates an author from the database given the first and last name. If the author already exists, it will not create a new author
     */
    public void createAuthor(String authorFirstName, String authorLastName) {
        for (Author author : authorsList) {
            if (author.getFirstName().equals(authorFirstName) && author.getLastName().equals(authorLastName)) {
                System.out.println("Author already exists");
                return;
            }
        }

        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO " + AUTHORS_TABLE + " (firstname, lastname) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, authorFirstName);
            preparedStatement.setString(2, authorLastName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadAuthors();
    }

    /**
     * Reads an author from the database given the first and last name
     */
    public void readAuthor(String firstName, String lastName) {
        for (Author author : authorsList) {
            if (author.getFirstName().equals(firstName) && author.getLastName().equals(lastName)) {
                System.out.println("Author ID: " + author.getAuthorID());
                System.out.println("First Name: " + author.getFirstName());
                System.out.println("Last Name: " + author.getLastName());
                System.out.println("Books: ");

                for (Book book : author.getBooks()) {
                    System.out.println(book.getTitle());
                }
                System.out.println();
            }
        }
    }

    /**
     * Reads all authors from the database
     */
    public void readAuthors() {
        for (Author author : authorsList) {
            System.out.println("Author ID: " + author.getAuthorID());
            System.out.println("First Name: " + author.getFirstName());
            System.out.println("Last Name: " + author.getLastName());
            System.out.println("Books: ");

            for (Book book : author.getBooks()) {
                System.out.println(book.getTitle());
            }
            System.out.println();
        }
    }

    /**
     * Updates an author in the database given the first and last name. If the author does not exist, it will not
     * update the author
     */
    public void updateAuthor(String firstName, String lastName, String newFirstName, String newLastName) {
        boolean authorExists = false;

        for (Author author : authorsList) {
            if (author.getFirstName().equals(firstName) && author.getLastName().equals(lastName)) {
                authorExists = true;

                author.setFirstName(newFirstName);
                author.setLastName(newLastName);

                try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String query = "UPDATE " + AUTHORS_TABLE + " SET firstName = ?, lastName = ? WHERE authorID = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, newFirstName);
                    preparedStatement.setString(2, newLastName);
                    preparedStatement.setInt(3, author.getAuthorID());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!authorExists) {
            System.out.println("Author does not exist.");
        }
    }

    /**
     * Deletes an author from the database given the first and last name. If the author does not exist, it will not
     * delete the author
     */
    public void deleteAuthor(String firstName, String lastName) {
        boolean authorExists = false;
        Author authorToDelete = null;

        for (Author author : authorsList) {
            if (author.getFirstName().equals(firstName) && author.getLastName().equals(lastName)) {
                authorExists = true;
                authorToDelete = author;

                try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String query = "DELETE FROM " + AUTHORS_TABLE + " WHERE authorID = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, author.getAuthorID());
                    preparedStatement.executeUpdate();

                    query = "DELETE FROM " + AUTHOR_ISBN_TABLE + " WHERE authorID = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, author.getAuthorID());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!authorExists) {
            System.out.println("Author does not exist.");
        }
        authorsList.remove(authorToDelete);
    }

    public static void main(String[] args) {
        BookDatabaseManager bookDatabaseManager = new BookDatabaseManager();
        bookDatabaseManager.loadBooks();
        bookDatabaseManager.loadAuthors();
        bookDatabaseManager.loadAuthorISBN();
        bookDatabaseManager.createRelationships();

    }
}



