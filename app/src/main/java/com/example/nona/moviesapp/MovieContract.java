package com.example.nona.moviesapp;

import android.provider.BaseColumns;

/**
 * Created by Nona on 09/01/2016.
 */


//table names only
public class MovieContract {

    public static final class MovieEntry implements BaseColumns{

        //Movie Table
        public static final String  TABLE_NAME = "Movie";

        public static final String  COLUMN_MOVIE_ID = "Movie_id";

        public static final String  COLUMN_TITLE = "Title";

        public static final String Column_POSTER = "Poster";

        public static final String COLUMN_OVERVIEW = "Overview";

        public static final String COLUMN_AVERAGE= "Average";

        public static final String COLUMN_RELEASE = "Release";

    }

//    public static final class TrailerEntry implements BaseColumns{
//
//        public static final String TABLE_NAME ="Trailers";
//
//        public static final String COLUMN_TRAILER_MOVIE_ID = "Movie_id";
//
//        public static final String COLUMN_TRAILER_ID = "trailer_id";
//
//        public static final String COLUMN_TRAILER_NAME ="trailer_name";
//
//        public static final String COLUMN_TRAILER_PATH = "trailer_path";
//
//    }
//
//    public static final class ReviewEntry implements BaseColumns{
//
//        public static final String TABLE_NAME ="Reviews";
//
//        public static final String COLUMN_REVIEW_MOVIE_ID ="movie_id";
//
//        public static final String COLUMN_REVIEW_ID = "review_id";
//
//        public static final String COLUMN_REVIEW ="review";
//
//        public static final String COLUMN_REVIEW_AUTHOR = "author";
//    }
}
