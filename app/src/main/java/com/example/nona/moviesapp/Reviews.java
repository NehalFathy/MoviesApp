package com.example.nona.moviesapp;

public class Reviews {

    String review;
    String Author;
    String id;
    public Reviews(String review , String author , String id  ) {
        this.review = review;
        this.Author =author;
        this.id=id;
    }


    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getId() {
        return id;
    }
}
