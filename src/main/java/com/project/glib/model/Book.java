package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book extends Document {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "is_bestseller")
    private boolean isBestSeller;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    protected Book() {
    }

    public Book(String title, String bookAuthor, String publisher, String edition, int year, int price, boolean isBestSeller, int count) {
        this.title = title;
        this.bookAuthor = bookAuthor;
        this.publisher = publisher;
        this.edition = edition;
        this.year = year;
        this.price = price;
        this.isBestSeller = isBestSeller;
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
                ", isBestSeller=" + isBestSeller +
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

    public String getBookName() {
        return title;
    }

    public void setBookName(String bookName) {
        this.title = bookName;
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
