package com.project.glib.model;

import javax.persistence.*;

@Entity
//@Table(name = "BOOKS")
public class Book extends Document {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String bookName;

    @Column(name = "author")
    private String bookAuthor;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "edition")
    private String edition;

    @Column(name = "edition_year")
    private int year;

    @Column(name = "price")
    private int price;

    @Column(name = "isBestSeller")
    private boolean isBestSeller;

    public Book() {
    }

    public Book(String bookName, String bookAuthor, String publisher, String edition, int year, int price, boolean isBestSeller) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.publisher = publisher;
        this.edition = edition;
        this.year = year;
        this.price = price;
        this.isBestSeller = isBestSeller;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition='" + edition + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", isBestSeller=" + isBestSeller +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public boolean isBestSeller() {
        return isBestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        isBestSeller = bestSeller;
    }
}
