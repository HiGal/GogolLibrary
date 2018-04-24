package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "logger")
public class Logger {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "action")
    private String action;

    @Column(name = "date")
    private long date;


    public Logger() {

    }

    public Logger(String action, long date) {

        this.action = action;
        this.date = date;

    }

    @Override
    public String toString() {
        return "Messages{" +
                "id =" + id +
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
