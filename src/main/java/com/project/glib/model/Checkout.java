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

    @Column(name = "idUser")
    private long userId;

    @Column(name = "idDoc")
    private long docPhysId;

    @Column(name = "docType")
    private String docType;

    @Column(name = "checkoutTime")
    private long checkoutTime;

    @Column(name = "returnTime")
    private long returnTime;

    @Column(name = "isRenewed")
    private boolean isRenewed;

    @Column(name = "shelf")
    private String shelf;

    protected Checkout() {
    }

    public Checkout(long userId, long docPhysId, String docType, long checkoutTime, long returnTime, boolean isRenewed, String shelf) {

        this.userId = userId;
        this.docPhysId = docPhysId;
        this.docType = docType;
        this.checkoutTime = checkoutTime;
        this.returnTime = returnTime;
        this.isRenewed = isRenewed;
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
                isRenewed == checkout.isRenewed &&
                Objects.equals(docType, checkout.docType) &&
                Objects.equals(shelf, checkout.shelf);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, docPhysId, docType, checkoutTime, returnTime, isRenewed, shelf);
    }

    @Override
    public String toString() {
        return "Checkout{" +
                "id=" + id +
                ", userId=" + userId +
                ", docPhysId=" + docPhysId +
                ", docType='" + docType + '\'' +
                ", checkoutTime=" + checkoutTime +
                ", returnTime=" + returnTime +
                ", isRenewed=" + isRenewed +
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

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
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

    public boolean isRenewed() {
        return isRenewed;
    }

    public void setRenewed(boolean renewed) {
        this.isRenewed = renewed;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
}
