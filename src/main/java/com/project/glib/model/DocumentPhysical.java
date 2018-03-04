package com.project.glib.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "documents_physical")
public class DocumentPhysical {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "can_booked")
    private boolean canBooked;

    @Column(name = "is_reference")
    private boolean isReference;

    @Column(name = "id_doc")
    private long idDoc;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "checkout_user")
    private String checkoutUser;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "keyword_id")
    private Set<Keyword> keywords;

    public DocumentPhysical() {
    }

    public DocumentPhysical(String shelf, boolean canBooked, boolean isReference, long idDoc, String docType, Set<Keyword> keywords) {
        this.shelf = shelf;
        this.canBooked = canBooked;
        this.isReference = isReference;
        this.idDoc = idDoc;
        this.docType = docType;
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "DocumentPhysical{" +
                "id=" + id +
                ", shelf='" + shelf + '\'' +
                ", canBooked=" + canBooked +
                ", isReference=" + isReference +
                ", idDoc=" + idDoc +
                ", docType='" + docType + '\'' +
                ", keywords=" + keywords +
                '}';
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public boolean isCanBooked() {
        return canBooked;
    }

    public void setCanBooked(boolean canBooked) {
        this.canBooked = canBooked;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
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

    public String getCheckoutUser() {
        return checkoutUser;
    }

    public void setCheckoutUser(String checkoutUser) {
        this.checkoutUser = checkoutUser;
    }

    public Set<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
    }
}
