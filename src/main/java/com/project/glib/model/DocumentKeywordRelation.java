package com.project.glib.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "document_keyword")
public class DocumentKeywordRelation {
    public static final String TYPE = "DOCUMENT_KEYWORD_RELATION";

    @Column(name = "doc_vir_id")
    private long docVirId;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "keyword_id")
    private long keywordId;

    protected DocumentKeywordRelation() {
    }

    public DocumentKeywordRelation(long docVirId, String docType, long keywordId) {
        this.docVirId = docVirId;
        this.docType = docType;
        this.keywordId = keywordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentKeywordRelation that = (DocumentKeywordRelation) o;
        return docVirId == that.docVirId &&
                keywordId == that.keywordId &&
                Objects.equals(docType, that.docType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(docVirId, docType, keywordId);
    }

    @Override
    public String toString() {
        return "DocumentKeywordRelation{" +
                "docVirId=" + docVirId +
                ", docType='" + docType + '\'' +
                ", keywordId=" + keywordId +
                '}';
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

    public long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(long keywordId) {
        this.keywordId = keywordId;
    }
}
