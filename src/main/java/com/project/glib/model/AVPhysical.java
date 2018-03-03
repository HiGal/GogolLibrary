package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "av_physical")
public class AVPhysical {

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

    @Column(name = "id_av")
    private long id_av;

    public AVPhysical() {
    }

    public AVPhysical(String shelf, boolean can_booked, boolean is_reference, long id_av) {

        this.shelf = shelf;
        this.can_booked = can_booked;
        this.is_reference = is_reference;
        this.id_av = id_av;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", shelf ='" + shelf + '\'' +
                ", can_booked ='" + can_booked + '\'' +
                ", is_reference ='" + is_reference + '\'' +
                ", id_book ='" + id_av + '\'' +
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

    public long getId_av() {
        return id_av;
    }

    public void setId_av(long id_av) {
        this.id_av = id_av;
    }
}
