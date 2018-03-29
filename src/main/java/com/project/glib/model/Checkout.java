package com.project.glib.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "checkout")
public class Checkout {
    public static final String TYPE = "CHECKOUT";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "doc_phys_id")
    private long docPhysId;

    @Column(name = "checkout_time")
    private long checkoutTime;

    @Column(name = "return_time")
    private long returnTime;

    @Column(name = "shelf")
    private String shelf;

    protected Checkout() {
    }

    public Checkout(long userId, long docPhysId, long checkoutTime, long returnTime, String shelf) {
        this.userId = userId;
        this.docPhysId = docPhysId;
        this.checkoutTime = checkoutTime;
        this.returnTime = returnTime;
        this.shelf = shelf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checkout checkout = (Checkout) o;
        return id == checkout.id &&
                userId == checkout.userId &&
                docPhysId == checkout.docPhysId &&
                checkoutTime == checkout.checkoutTime &&
                returnTime == checkout.returnTime &&
                Objects.equals(shelf, checkout.shelf);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, docPhysId, checkoutTime, returnTime, shelf);
    }

    @Override
    public String toString() {
        return "Checkout{" +
                "id=" + id +
                ", userId=" + userId +
                ", docPhysId=" + docPhysId +
                ", checkoutTime=" + checkoutTime +
                ", returnTime=" + returnTime +
                ", shelf='" + shelf + '\'' +
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

    public long getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(long checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(long returnTime) {
        this.returnTime = returnTime;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
}
