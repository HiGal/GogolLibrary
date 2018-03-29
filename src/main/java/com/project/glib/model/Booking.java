package com.project.glib.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "booking")
public class Booking {
    public static final String TYPE = "BOOKING";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "id_document")
    private long docPhysId;

    @Column(name = "docType")
    private String docType;

    @Column(name = "bookingDate")
    private long bookingDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "priority")
    private int priority;

    @Column(name = "docVirId")
    private long docVirId;

    @Column(name = "shelf")
    private String shelf;


    protected Booking() {
    }

    public Booking(long userId, long docVirId, String docType, long docPhysId, long bookingDate, boolean isActive, int priority, String shelf) {
        this.userId = userId;
        this.docVirId = docVirId;
        this.docType = docType;
        this.docPhysId = docPhysId;
        this.bookingDate = bookingDate;
        this.isActive = isActive;
        this.priority = priority;
        this.shelf = shelf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id &&
                userId == booking.userId &&
                docPhysId == booking.docPhysId &&
                bookingDate == booking.bookingDate &&
                isActive == booking.isActive &&
                priority == booking.priority &&
                Objects.equals(docType, booking.docType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, docPhysId, docType, bookingDate, isActive, priority);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userId +
                ", docPhysId=" + docPhysId +
                ", docType='" + docType + '\'' +
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDocPhysId() {
        return docPhysId;
    }

    public void setDocPhysId(long docPhysId) {
        this.docPhysId = docPhysId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
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

    public long getDocVirId() {
        return docVirId;
    }

    public void setDocVirId(long docVirId) {
        this.docVirId = docVirId;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
}
