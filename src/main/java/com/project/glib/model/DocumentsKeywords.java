package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "documents_keywords")
public class DocumentsKeywords {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "doc_phys_id")
    private long docPhysId;

    @Column(name = "keyword_id")
    private long keywordId;

    public DocumentsKeywords() {
    }

    public DocumentsKeywords(long docPhysId, long keywordId) {
        this.docPhysId = docPhysId;
        this.keywordId = keywordId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDocPhysId() {
        return docPhysId;
    }

    public void setDocPhysId(long docPhysId) {
        this.docPhysId = docPhysId;
    }

    public long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(long keywordId) {
        this.keywordId = keywordId;
    }
}
