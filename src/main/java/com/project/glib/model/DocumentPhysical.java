package com.project.glib.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "documents_physical")
public class DocumentPhysical {
    public static final String TYPE = "DOCUMENT_PHYSICAL";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "can_booked")
    private boolean canBooked;

    @Column(name = "id_doc")
    private long docVirId;

    @Column(name = "doc_type")
    private String docType;


//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "keyword_id")
//    private Set<Keyword> keywords;

    protected DocumentPhysical() {
    }

    public DocumentPhysical(String shelf, boolean canBooked, long docVirId, String docType, Set<Keyword> keywords) {
        this.shelf = shelf;
        this.canBooked = canBooked;
        this.docVirId = docVirId;
        this.docType = docType;
//        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentPhysical that = (DocumentPhysical) o;
        return id == that.id &&
                canBooked == that.canBooked &&
                docVirId == that.docVirId &&
                Objects.equals(shelf, that.shelf) &&
                Objects.equals(docType, that.docType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, shelf, canBooked, docVirId, docType);
    }

    @Override
    public String toString() {
        return "DocumentPhysical{" +
                "id=" + id +
                ", shelf='" + shelf + '\'' +
                ", canBooked=" + canBooked +
                ", docVirId=" + docVirId +
                ", docType='" + docType + '\'' +
//                ", keywords=" + keywords +
                '}';
    }

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

    public long getDocVirId() {
        return docVirId;
    }

    public void setDocVirId(long docVirId) {
        this.docVirId = docVirId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }


//    public Set<Keyword> getKeywords() {
//        return keywords;
//    }
//
//    public void setKeywords(Set<Keyword> keywords) {
//        this.keywords = keywords;
//    }
}
