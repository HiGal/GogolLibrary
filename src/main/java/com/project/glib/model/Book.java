package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "BOOKS")
public class Book {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "BOOKS_TITLE")
    private String bookTitle;

    @Column(name = "BOOKS_AUTHOR")
    private String bookAuthor;

    @Column(name = "BOOKS_EDITION")
    private String bookEdition;

    @Column(name = "BOOKS_YEAR")
    private int year;

    @Column(name = "BOOKS_PRICE")
    private int price;

    @Column(name = "BOOKS_IS_REFERENCE")
    private boolean isReference;

    @Column(name = "BOOKS_COUNT")
    private int bookCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookTitle='" + bookTitle + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookEdition='" + bookEdition + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", isReference=" + isReference +
                ", bookCount=" + bookCount +
                '}';
    }
}
