package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book extends Document {
    public static final String BESTSELLER = "BESTSELLER";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String bookAuthor;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "edition")
    private String edition;

    @Column(name = "year")
    private int year;

    @Column(name = "note")
    private String note;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    protected Book() {
    }

    public Book(String title, String bookAuthor, String publisher, String edition, int year, String note, int price, int count) {
        this.title = title;
        this.bookAuthor = bookAuthor;
        this.publisher = publisher;
        this.edition = edition;
        this.year = year;
        this.note = note;
        this.price = price;
        this.count = count;
    }

    public Book(Book book) {
        this.title = book.title;
        this.bookAuthor = book.bookAuthor;
        this.publisher = book.publisher;
        this.edition = book.edition;
        this.year = book.year;
        this.note = book.note;
        this.price = book.price;
        this.count = book.count;
    }

    public Book(Book book, int count) {
        this.title = book.title;
        this.bookAuthor = book.bookAuthor;
        this.publisher = book.publisher;
        this.edition = book.edition;
        this.year = book.year;
        this.note = book.note;
        this.price = book.price;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition='" + edition + '\'' +
                ", year=" + year +
                ", note=" + note +
                ", price=" + price +
                ", count=" + count +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
