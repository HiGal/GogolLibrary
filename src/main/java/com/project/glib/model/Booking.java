package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "idUser")
    private long idUser;

    @Column(name = "id_document")
    private long idDoc;

    @Column(name = "docType")
    private String docType;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "bookingDate")
    private long bookingDate;

    @Column(name = "is_active")
    private long is_active;

    @Column(name = "priority")
    private long priority;


    protected Booking() {
    }

    public Booking(long idUser, long idDoc, String docType, String shelf, long bookingDate) {
        this.idUser = idUser;
        this.idDoc = idDoc;
        this.docType = docType;
        this.shelf = shelf;
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idDoc=" + idDoc +
                ", docType='" + docType + '\'' +
                ", shelf='" + shelf + '\'' +
                ", bookingDate=" + bookingDate +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(long idDoc) {
        this.idDoc = idDoc;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public long getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(long bookingDate) {
        this.bookingDate = bookingDate;
    }

    public long getIs_active() {
        return is_active;
    }

    public void setIs_active(long is_active) {
        this.is_active = is_active;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }
}
