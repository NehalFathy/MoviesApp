package com.example.nona.moviesapp;

public class Trailer {
    String Trailer;
    String Path;
    String id;

    public Trailer( String trailer , String path , String id ) {
        Path = path;
        Trailer = trailer;
        this.id=id;
    }


    public String getTrailer() {
        return Trailer;
    }

    public void setTrailer(String trailer) {
        Trailer = trailer;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getId() {
        return id;
    }
}
