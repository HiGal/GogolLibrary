package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "checkout")
public class Checkout {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "idUser")
    private long idUser;

    @Column(name = "idDoc")
    private long idDoc;

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

    public Checkout(long idUser, long idDoc, String docType, long checkoutTime, long returnTime, boolean isRenewed, String shelf) {

        this.idUser = idUser;
        this.idDoc = idDoc;
        this.docType = docType;
        this.checkoutTime = checkoutTime;
        this.returnTime = returnTime;
        this.isRenewed = isRenewed;
        this.shelf = shelf;
    }

    @Override
    public String toString() {
        return "Checkout{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idDoc=" + idDoc +
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
