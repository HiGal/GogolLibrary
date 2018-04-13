package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "logger")
public class Logger {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "id_user")
    private long userId;

    @Column(name = "doc_vir_id")
    private long virtDocId;

    @Column(name = "phys_id_doc")
    private long physDocId;

    @Column(name = "action")
    private String action;

    @Column(name = "date")
    private long date;


    public Logger() {

    }

    public Logger(long userId, long virtDocId, long physDocId, String action, long date) {
        this.userId = userId;
        this.virtDocId = virtDocId;
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
                ", virtDocId =" + virtDocId +
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

    public long getVirtDocId() {
        return virtDocId;
    }

    public void setVirtDocId(long virtDocId) {
        this.virtDocId = virtDocId;
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
