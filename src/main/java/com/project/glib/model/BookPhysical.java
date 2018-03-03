package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "books_physical")
public class BookPhysical {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "can_booked")
    private boolean can_booked;

    @Column(name = "is_reference")
    private boolean is_reference;

    @Column(name = "id_book")
    private long id_book;

    public BookPhysical() {
    }

    public BookPhysical(String shelf, boolean can_booked, boolean is_reference, long id_book) {

        this.shelf = shelf;
        this.can_booked = can_booked;
        this.is_reference = is_reference;
        this.id_book = id_book;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", shelf ='" + shelf + '\'' +
                ", can_booked ='" + can_booked + '\'' +
                ", is_reference ='" + is_reference + '\'' +
                ", id_book ='" + id_book + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public boolean isCan_booked() {
        return can_booked;
    }

    public void setCan_booked(boolean can_booked) {
        this.can_booked = can_booked;
    }

    public boolean isIs_reference() {
        return is_reference;
    }

    public void setIs_reference(boolean is_reference) {
        this.is_reference = is_reference;
    }

    public long getId_book() {
        return id_book;
    }

    public void setId_book(long id_book) {
        this.id_book = id_book;
    }
}
