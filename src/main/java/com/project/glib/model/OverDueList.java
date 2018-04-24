package com.project.glib.model;


public class OverDueList {
    public String name;
    public String surname;
    public String phone;
    public String title;
    public String author;
    public int overdueDays;

    public OverDueList() {
    }

    public OverDueList(String name, String surname, String phone, String title, String author, int overdueDays) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.title = title;
        this.author = author;
        this.overdueDays = overdueDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }
}

