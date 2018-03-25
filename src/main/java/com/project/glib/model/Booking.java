package com.project.glib.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
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
    private boolean isActive;

    @Column(name = "priority")
    private int priority;

    protected Booking() {
    }

    public Booking(long idUser, long idDoc, String docType, String shelf, long bookingDate, boolean isActive, int priority) {
        this.idUser = idUser;
        this.idDoc = idDoc;
        this.docType = docType;
        this.shelf = shelf;
        this.bookingDate = bookingDate;
        this.isActive = isActive;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id &&
                idUser == booking.idUser &&
                idDoc == booking.idDoc &&
                bookingDate == booking.bookingDate &&
                isActive == booking.isActive &&
                priority == booking.priority &&
                Objects.equals(docType, booking.docType) &&
                Objects.equals(shelf, booking.shelf);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, idUser, idDoc, docType, shelf, bookingDate, isActive, priority);
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
                ", isActive=" + isActive +
                ", priority=" + priority +
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
