package com.project.glib.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "audio_video")
public class AudioVideo extends Document {
    @Id
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

    @Column(name = "picture")
    private String picture;

    private HashSet<Keyword> keywords;

    public AudioVideo(String title, String author, int price, int count, String picture, HashSet<Keyword> keywords) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.count = count;
        this.picture = picture;
        this.keywords = keywords;
    }

    public AudioVideo(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioVideo that = (AudioVideo) o;
        return id == that.id &&
                price == that.price &&
                count == that.count &&
                Objects.equals(title, that.title) &&
                Objects.equals(author, that.author) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(keywords, that.keywords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, author, price, count, picture, keywords);
    }

    @Override
    public String toString() {
        return "AudioVideo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", picture='" + picture + '\'' +
                ", keywords=" + keywords +
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "documents_keywords",
            joinColumns = @JoinColumn(name = "doc_vir_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id"))
    public HashSet<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(HashSet<Keyword> keywords) {
        this.keywords = keywords;
    }
}
