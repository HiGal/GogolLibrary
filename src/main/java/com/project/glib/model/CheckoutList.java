package com.project.glib.model;

public class CheckoutList {

    public String name;
    public String surname;
    public String phone;
    public String title;
    public String author;
    public String checkoutDate;
    public String returnDate;

    public CheckoutList() {
    }

    public CheckoutList(String name, String surname, String phone,
                        String title, String author, String checkoutDate, String returnDate) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.title = title;
        this.author = author;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
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

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
