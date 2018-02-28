package com.project.glib.model;

import javax.persistence.*;

@Entity
public class Journal extends Document {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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


    @Column(name = "price")
    private int price;

    protected Journal() {
    }

    public Journal(String title, String author, String name, int issue, String editor, int price) {
        this.title = title;
        this.author = author;
        this.name = name;
        this.issue = issue;
        this.editor = editor;
        this.price = price;
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
                ", price=" + price +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
