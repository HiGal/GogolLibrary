package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "id_user")
    private long id_user;

    @Column(name = "message")
    private String message;

    @Column(name = "id_doc")
    private long id_doc;

    @Column(name = "doc_type")
    private String doc_type;

    @Column(name = "is_read")
    private boolean is_read;

    public Messages() {

    }

    public Messages(long id_user, String message, long id_doc, String doc_type, boolean is_read) {
        this.id_user = id_user;
        this.message = message;
        this.id_doc = id_doc;
        this.doc_type = doc_type;
        this.is_read = is_read;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", id_user=" + id_user +
                ", message=" + message +
                ", id_doc=" + id_doc +
                ", doc_type=" + doc_type +
                ", is_read=" + is_read +
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public boolean getIsRead() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}
