package com.project.glib.model;

import javax.persistence.*;

@Entity
public class AudioVideo extends Document {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String avTitle;

    @Column(name = "author")
    private String avAuthor;

    @Column(name = "price")
    private int price;

    protected AudioVideo() {
    }

    public AudioVideo(String avTitle, String avAuthor, int price) {
        this.avTitle = avTitle;
        this.avAuthor = avAuthor;
        this.price = price;
    }

    @Override
    public String toString() {
        return "AudioVideo{" +
                "id=" + id +
                ", avTitle='" + avTitle + '\'' +
                ", avAuthor='" + avAuthor + '\'' +
                ", price=" + price +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvTitle() {
        return avTitle;
    }

    public void setAvTitle(String avTitle) {
        this.avTitle = avTitle;
    }

    public String getAvAuthor() {
        return avAuthor;
    }

    public void setAvAuthor(String avAuthor) {
        this.avAuthor = avAuthor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
