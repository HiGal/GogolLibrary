package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_user")
    private long id_user;

    @Column(name = "id_doc")
    private long id_doc;

    @Column(name = "doc_type")
    private String doc_type;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "booking_date")
    private long booking_date;

    public Booking() {
    }

    public Booking(long id_user, long id_doc, String doc_type, String shelf, long booking_date) {
        this.id_user = id_user;
        this.id_doc = id_doc;
        this.doc_type = doc_type;
        this.shelf = shelf;
        this.booking_date = booking_date;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", id_user=" + id_user +
                ", id_doc=" + id_doc +
                ", doc_type='" + doc_type + '\'' +
                ", shelf='" + shelf + '\'' +
                ", booking_date=" + booking_date +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public long getId_doc() {
        return id_doc;
    }

    public void setId_doc(long id_doc) {
        this.id_doc = id_doc;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public long getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(long booking_date) {
        this.booking_date = booking_date;
    }
}
