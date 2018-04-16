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

    @Column(name = "phys_doc_id")
    private long physDocId;

    @Column(name = "action")
    private String action;

    @Column(name = "date")
    private long date;


    public Logger() {

    }

    public Logger(long userId, long physDocId, String action, long date) {
        this.userId = userId;
        this.physDocId = physDocId;
        this.action = action;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id =" + id +
                ", userId =" + userId +
                ", physDocId =" + physDocId +
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

    public long getPhysDocId() {
        return physDocId;
    }

    public void setPhysDocId(long physDocId) {
        this.physDocId = physDocId;
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
}
