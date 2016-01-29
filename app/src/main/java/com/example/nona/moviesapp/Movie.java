package com.example.nona.moviesapp;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Movie implements Serializable  {

    String title;
    String Poster;
    String overview;
    String Average_Vote;
    String Release;
    String id;
    ArrayList<Reviews> reviewsList;
    ArrayList<Trailer> trailerList;

    boolean IsFavorite ;  //initially the movie is not favourite


    public Movie(String poster, String title , String overview , String average_Vote , String release , String id , ArrayList<Trailer> trailerList, ArrayList<Reviews> reviewList) {
        Poster = poster;
        this.title = title;
        this.overview = overview;
        this.Average_Vote=average_Vote;
        this.Release = release;
        this.id= id;
        this.trailerList = trailerList;
        this.reviewsList = reviewList;

        IsFavorite = false;
    }

    public Movie(String poster, String title , String overview , String average_Vote , String release , String id ) {
        Poster = poster;
        this.title = title;
        this.overview = overview;
        this.Average_Vote=average_Vote;
        this.Release = release;
        this.id= id;

    }

    public ArrayList<Reviews> getReviewsList() {
        return reviewsList;
    }

    public ArrayList<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setReviewsList(ArrayList<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public void setTrailerList(ArrayList<Trailer> trailerList) {
        this.trailerList = trailerList;
    }

    public String getPoster() {
        return Poster;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getAverage_Vote() {
        return Average_Vote;
    }

    public String getRelease() {
        return Release;
    }

    public String getId() {
        return id;
    }


    public boolean CheckisFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite() {
        IsFavorite = true;
    }

   public JSONObject toJsonObject(){
       JSONObject object=new JSONObject();

   return object;
   }
}
