package com.example.nona.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nona.moviesapp.MovieContract.MovieEntry;

import java.util.ArrayList;
import java.util.List;
//import com.example.nona.moviesapp.MovieContract.ReviewEntry;
//import com.example.nona.moviesapp.MovieContract.TrailerEntry;


/**
 * Created by Nona on 09/01/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private  final String LOG_TAG =MovieDbHelper.class.getSimpleName() ;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOG_TAG , "database created");
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold movies

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE," +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.Column_POSTER + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_AVERAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE + " TEXT NOT NULL " +
                " );";



        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

        Log.d(LOG_TAG , "Table created");
//        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
//        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(db);
    }


    public void AddMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_ID , movie.getId() );
        values.put(MovieEntry.COLUMN_TITLE    , movie.getTitle());
        values.put(MovieEntry.Column_POSTER   , movie.getPoster());
        values.put(MovieEntry.COLUMN_OVERVIEW , movie.getOverview());
        values.put(MovieEntry.COLUMN_AVERAGE  , movie.getAverage_Vote());
        values.put(MovieEntry.COLUMN_RELEASE  , movie.getRelease());

        // Inserting Row
        db.insert(MovieEntry.TABLE_NAME, null, values);

        db.close(); // Closing database connection
    }




    public boolean checkIfExist(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        /*
        public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)

        columns ::	A list of which "columns" to return. Passing null will return all columns,
                   which is discouraged to prevent reading data from storage that isn't going to be used.

       selection ::	A filter declaring which "rows" to return, formatted as an SQL WHERE clause (excluding the WHERE itself).
                    Passing null will return all rows for the given table.

       selectionArgs ::	You may include ?s in selection, which will be replaced by the values from selectionArgs,
                       in order that they appear in the selection. The values will be bound as Strings.

       return >> A Cursor object, which is positioned before the first entry

         */

        Cursor cursor = db.query(MovieEntry.TABLE_NAME ,  new String[] { MovieEntry.COLUMN_MOVIE_ID,
                        MovieEntry.COLUMN_TITLE, MovieEntry.Column_POSTER , MovieEntry.COLUMN_RELEASE ,
                        MovieEntry.COLUMN_AVERAGE , MovieEntry.COLUMN_OVERVIEW }, MovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[] { id }, null, null, null, null);


        //check if anything exist in cursor
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }




    // Getting All Movies
    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<Movie>();

    /*
    public Cursor rawQuery (String sql, String[] selectionArgs)

    sql	          :: the SQL query. The SQL string must not be ; terminated
    selectionArgs :: You may include ?s in where clause in the query,
                    which will be replaced by the values from selectionArgs. The values will be bound as Strings
     */

        // Select All columns
        String selectQuery = "SELECT  * FROM " + MovieEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Movie m = new Movie();
                String id = cursor.getString(0);
                Log.d(LOG_TAG +"id" ,id);
                String title = cursor.getString(1);
                Log.d(LOG_TAG +"title" ,title);
                String poster = cursor.getString(2);
                Log.d(LOG_TAG + "poster" , poster);
                String overview =cursor.getString(3);
                Log.d(LOG_TAG + "overview" , overview);
                String average = cursor.getString(4);
                Log.d(LOG_TAG + "average" , average);
                String release = cursor.getString(5);
                Log.d(LOG_TAG + "release" , release);


                Movie m = new Movie(poster,title,overview,average,release,id);
                // Adding contact to list
                movieList.add(m);
            } while (cursor.moveToNext());
        }

        // return contact list
        return movieList;
    }



}
