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
    private long userId;

    @Column(name = "message")
    private String message;

    @Column(name = "id_doc")
    private long docPhysId;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "is_read")
    private boolean isRead;

    public Messages() {
    }

    public Messages(long userId, String message, long docPhysId, String docType, boolean isRead) {
        this.userId = userId;
        this.message = message;
        this.docPhysId = docPhysId;
        this.docType = docType;
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", userId=" + userId +
                ", message=" + message +
                ", docPhysId=" + docPhysId +
                ", docType=" + docType +
                ", isRead=" + isRead +
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDocPhysId() {
        return docPhysId;
    }

    public void setDocPhysId(long docPhysId) {
        this.docPhysId = docPhysId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }
}
