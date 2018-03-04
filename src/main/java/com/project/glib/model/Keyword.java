package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "keywords")
public class Keyword {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "keyword")
    private String keyword;

    public Keyword(){
    }

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
