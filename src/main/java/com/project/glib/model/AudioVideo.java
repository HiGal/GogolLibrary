package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "audio_video")
public class AudioVideo extends Document {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    public AudioVideo() {
    }

    public AudioVideo(String title, String author, int price, int count) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.count = count;
    }

    @Override
    public String toString() {
        return "AudioVideo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price + '\'' +
                ", count=" + count +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
