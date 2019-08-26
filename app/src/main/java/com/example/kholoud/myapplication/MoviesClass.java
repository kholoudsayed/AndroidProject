package com.example.kholoud.myapplication;

import java.io.Serializable;

public class MoviesClass implements Serializable {
//DEF*******************************************
    private String id;
    private String title;
    private String releaseDate;
    private String vote;
    private String popularity;
    private String synopsis;
    private String image;
    private String backdrop;

    //constractors empty
    public MoviesClass() {
    }

    public MoviesClass(String id, String title, String releaseDate, String vote, String popularity, String synopsis, String image, String backdrop) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.vote = vote;
        this.popularity = popularity;
        this.synopsis = synopsis;
        this.image = image;
        this.backdrop = backdrop;
    }
// getter and setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.title = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVote() {
        return vote;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getImage() {
        return image;
    }

    public String getBackdrop() {
        return backdrop;
    }

}
