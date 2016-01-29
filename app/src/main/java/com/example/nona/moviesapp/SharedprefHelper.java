//package com.example.nona.moviesapp;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import org.json.JSONArray;
//
///**
// * Created by Nona on 09/01/2016.
// */
//public class SharedprefHelper {
//
//
//    public static final String MyPREFERENCES = "MyPrefs" ;
//
//    public void SetMovie(Context context,Movie m){
//
//        JSONArray array=getMovies(context);
//        array.put(m.toJsonObject().toString());
//        SharedPreferences sharedpreference = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        //sharedpreference.edit().putString("movieList",m)
//
//    }
//
////    public Movie Getmovie(){
////
////
////    }
//}
