package org.example.assignment2;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent an author
 */
public class Author {
    private int authorID;
    private String firstName;
    private String lastName;
    private ArrayList<Book> books;

    /**
     * Constructor for an author
     * @param authorID id of the author being created
     * @param firstName first name of the author being created
     * @param lastName last name of the author being created
     */
    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = new ArrayList<>();
    }

    /**
     * @return the authorID
     */
    public int getAuthorID() {
        return authorID;
    }

    /**
     * Gets the first name of the author
     * @return firstName of the author
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets a new first name for the author
     * @param firstName new first name for the author
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the author
     * @return lastName of the author
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets a new last name for the author
     * @param lastName new last name for the author
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the books of the author
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Adds a book to the author's list of books
     * @param book book to be added to the author's list of books
     */
    public void addBook(Book book) {
        books.add(book);
    }
}
