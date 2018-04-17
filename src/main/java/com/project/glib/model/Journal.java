package com.project.glib.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "journal")
public class Journal extends Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "journal_name")
    private String name;

    @Column(name = "issue")
    private int issue;

    @Column(name = "editor")
    private String editor;

    @Column(name = "note")
    private String note;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    @Column(name = "picture")
    private String picture;

    private HashSet<Keyword> keywords;

    public Journal(String title, String author, String name, int issue, String editor, String note, int price, int count, String picture, HashSet<Keyword> keywords) {
        this.title = title;
        this.author = author;
        this.name = name;
        this.issue = issue;
        this.editor = editor;
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
        Journal journal = (Journal) o;
        return id == journal.id &&
                issue == journal.issue &&
                price == journal.price &&
                count == journal.count &&
                Objects.equals(title, journal.title) &&
                Objects.equals(author, journal.author) &&
                Objects.equals(name, journal.name) &&
                Objects.equals(editor, journal.editor) &&
                Objects.equals(note, journal.note) &&
                Objects.equals(picture, journal.picture) &&
                Objects.equals(keywords, journal.keywords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, author, name, issue, editor, note, price, count, picture, keywords);
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", issue=" + issue +
                ", editor='" + editor + '\'' +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
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
