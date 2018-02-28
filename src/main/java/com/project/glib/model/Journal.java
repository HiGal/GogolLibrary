package com.project.glib.model;

import javax.persistence.*;

@Entity
public class Journal extends Document {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String journalTitle;

    @Column(name = "author")
    private String journalAuthor;

    @Column(name = "journal_name")
    private String journalName;

    @Column(name = "issue")
    private int issue;

    @Column(name = "editor")
    private String editor;


    @Column(name = "price")
    private int price;

    protected Journal() {
    }

    public Journal(String journalTitle, String journalAuthor, String journalName, int issue, String editor, int price) {
        this.journalTitle = journalTitle;
        this.journalAuthor = journalAuthor;
        this.journalName = journalName;
        this.issue = issue;
        this.editor = editor;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Journals{" +
                "id=" + id +
                ", journalTitle='" + journalTitle + '\'' +
                ", journalAuthor='" + journalAuthor + '\'' +
                ", journalName='" + journalName + '\'' +
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

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getJournalAuthor() {
        return journalAuthor;
    }

    public void setJournalAuthor(String journalAuthor) {
        this.journalAuthor = journalAuthor;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
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
