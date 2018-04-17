package com.project.glib.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

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

    @Column(name = "picture")
    private String picture;

    private HashSet<Keyword> keywords;

    public Book(String title, String author, String publisher, String edition, int year, String note, int price, int count, String picture, HashSet<Keyword> keywords) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        this.year = year;
        this.note = note;
        this.price = price;
        this.count = count;
        this.picture = picture;
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                year == book.year &&
                price == book.price &&
                count == book.count &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(edition, book.edition) &&
                Objects.equals(note, book.note) &&
                Objects.equals(picture, book.picture) &&
                Objects.equals(keywords, book.keywords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, author, publisher, edition, year, note, price, count, picture, keywords);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition='" + edition + '\'' +
                ", year=" + year +
                ", note='" + note + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", picture='" + picture + '\'' +
                ", keywords=" + keywords +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "documents_keywords",
            joinColumns = @JoinColumn(name = "doc_vir_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id"))
    public HashSet<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(HashSet<Keyword> keywords) {
        this.keywords = keywords;
    }
}
