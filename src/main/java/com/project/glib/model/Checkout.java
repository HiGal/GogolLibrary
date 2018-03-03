package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "checkout")
public class Checkout {

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

    @Column(name = "checkout_time")
    private long checkout_time;

    @Column(name = "return_time")
    private long return_time;

    @Column(name = "is_renewed")
    private boolean is_renewed;

    public Checkout() {
    }

    public Checkout(long id_user, long id_doc, String doc_type, long checkout_time, long return_time, boolean is_renewed) {

        this.id_user = id_user;
        this.id_doc = id_doc;
        this.doc_type = doc_type;
        this.checkout_time = checkout_time;
        this.return_time = return_time;
        this.is_renewed = is_renewed;
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

    public long getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(long checkout_time) {
        this.checkout_time = checkout_time;
    }

    public long getReturn_time() {
        return return_time;
    }

    public void setReturn_time(long return_time) {
        this.return_time = return_time;
    }

    public boolean isIs_renewed() {
        return is_renewed;
    }

    public void setIs_renewed(boolean is_renewed) {
        this.is_renewed = is_renewed;
    }
}
