package org.example.assignment2;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a book
 */
public class Book {
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyRight;
    private ArrayList<Author> authors;

    /**
     * Constructor for a book
     * @param isbn isbn of the book being created
     * @param title title of the book being created
     * @param editionNumber edition number of the book being created
     * @param copyRight copy right of the book being created
     */
    public Book(String isbn, String title, int editionNumber, String copyRight) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyRight = copyRight;
        authors = new ArrayList<>();
    }

    /**
     * @return the isbn
     */
    public String getISBN() {
        return isbn;
    }

    /**
     * Sets a new isbn for the book
     * @param isbn new isbn for the book
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets a new title for the book
     * @param title new title for the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the edition number
     */
    public int getEditionNumber() {
        return editionNumber;
    }

    /**
     * Sets a new edition number for the book
     * @param editionNumber new edition number for the book
     */
    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    /**
     * @return the copy right
     */
    public String getCopyright() {
        return copyRight;
    }

    /**
     * Sets a new copy right for the book
     * @param copyRight new copy right for the book
     */
    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    /**
     * @return the authors of the book
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Adds an author to the book
     * @param author author to be added to the book
     */
    public void addAuthor(Author author) {
        authors.add(author);
    }
}

