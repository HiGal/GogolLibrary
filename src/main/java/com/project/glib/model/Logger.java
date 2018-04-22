package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "logger")
public class Logger {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "doc_virt_id")
    private long docVirtid;

    @Column(name = "type")
    private String type;

    @Column(name = "action")
    private String action;

    @Column(name = "date")
    private long date;


    public Logger() {

    }

    public Logger(long userId, long docVirtid, String action, long date, String type) {
        this.userId = userId;
        this.docVirtid = docVirtid;
        this.action = action;
        this.date = date;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id =" + id +
                ", userId =" + userId +
                ", docVirtid =" + docVirtid +
                ", type = " + type +
                ", action =" + action +
                ", date =" + date +
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

    public long getDocVirtid() {
        return docVirtid;
    }

    public void setDocVirtid(long docVirtid) {
        this.docVirtid = docVirtid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
