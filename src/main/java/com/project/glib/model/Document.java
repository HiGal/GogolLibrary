package com.project.glib.model;

import javax.persistence.*;

@Entity
public class Document {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_book")
    private String idBook;

    @Column(name = "id_journal")
    private String idJournal;

    @Column(name = "id_av")
    private String idAV;

    @Column(name = "shelf")
    private int shelf;

    @Column(name = "canCheckOut")
    private String canCheckOut;


    @Column(name = "isReference")
    private int isReference;

    protected Document() {
    }

    public Document(String idBook, String idJournal, String idAV, int shelf, String canCheckOut, int isReference) {
        this.idBook = idBook;
        this.idJournal = idJournal;
        this.idAV = idAV;
        this.shelf = shelf;
        this.canCheckOut = canCheckOut;
        this.isReference = isReference;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", idBook='" + idBook + '\'' +
                ", idJournal='" + idJournal + '\'' +
                ", idAV='" + idAV + '\'' +
                ", shelf=" + shelf +
                ", canCheckOut='" + canCheckOut + '\'' +
                ", isReference=" + isReference +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdBook() {
        return idBook;
    }

    public void setIdBook(String idBook) {
        this.idBook = idBook;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public String getIdAV() {
        return idAV;
    }

    public void setIdAV(String idAV) {
        this.idAV = idAV;
    }

    public int getShelf() {
        return shelf;
    }

    public void setShelf(int shelf) {
        this.shelf = shelf;
    }

    public String getCanCheckOut() {
        return canCheckOut;
    }

    public void setCanCheckOut(String canCheckOut) {
        this.canCheckOut = canCheckOut;
    }

    public int getIsReference() {
        return isReference;
    }

    public void setIsReference(int isReference) {
        this.isReference = isReference;
    }
}
