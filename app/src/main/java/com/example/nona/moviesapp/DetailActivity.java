package com.example.nona.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // choose to designate a Toolbar as the action bar for an Activity
        setSupportActionBar(toolbar);

        //this to show the up arrow in detailed activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null){
            // update selected fragment and title
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //*************************       FRAGMENT    ************************************//

    public static class DetailFragment extends Fragment {

        ArrayList<Trailer> trailerArrayList= new ArrayList<>();
        ArrayList<Reviews> reviewArrayList = new ArrayList<>();
        ListView listview;
        MovieDetaialsAdapter adapter;
        Movie mov;
        Movie NewMovie;

        //String Movie_id;

        int Tflag =0;
        int Rflag =0;


        public DetailFragment() {
        }

//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//           ///here we defrantiate between tablet or phone with
//           if (getArguments() == null){
//
//               Intent intent = getActivity().getIntent();
//               if (intent != null && intent.hasExtra("MyMovie")){
//
//                   mov = (Movie)intent.getSerializableExtra("MyMovie");
//                   String  Movie_id = mov.getId();
//
//                   //adapter=new MovieDetaialsAdapter(getActivity(),mov);
//                   adapter =new MovieDetaialsAdapter();
//
//                   TrailerTask T_task = new TrailerTask();
//                   T_task.execute(Movie_id);
//
//               }
//
//               else
//               {
//                   mov = (Movie)getArguments().getSerializable("MyMovie");
//                   String  Movie_id = mov.getId();
//
//                   //adapter=new MovieDetaialsAdapter(getActivity(),mov);
//                   adapter =new MovieDetaialsAdapter();
//
//                   TrailerTask T_task = new TrailerTask();
//                   T_task.execute(Movie_id);
//
//
//               }
//
//           }
////            Intent intent = getActivity().getIntent();
////            if (intent != null && intent.hasExtra("MyMovie")){
////
////                mov = (Movie)intent.getSerializableExtra("MyMovie");
////                String  Movie_id = mov.getId();
////
////                //adapter=new MovieDetaialsAdapter(getActivity(),mov);
////                adapter =new MovieDetaialsAdapter();
////
////                TrailerTask T_task = new TrailerTask();
////                T_task.execute(Movie_id);
////
////            }
//       }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            listview  = (ListView) rootView.findViewById(R.id.Details_listView);


            ///here we defrantiate between tablet or phone with
            if (getArguments() == null){

                Intent intent = getActivity().getIntent();
                if (intent != null && intent.hasExtra("MyMovie")){

                    mov = (Movie)intent.getSerializableExtra("MyMovie");
                    String  Movie_id = mov.getId();

                    //adapter=new MovieDetaialsAdapter(getActivity(),mov);
                    adapter =new MovieDetaialsAdapter();

                    TrailerTask T_task = new TrailerTask();
                    T_task.execute(Movie_id);



                    //************** edit**************//

                    String id    =mov.getId();
                    String title =mov.getTitle();
                    String poster = mov.getPoster();
                    String overview = mov.getOverview();
                    String average = mov.getRelease();
                    String release = mov.getRelease();

                    NewMovie = new Movie(poster , title, overview , average , release, id , new ArrayList<Trailer>(), new ArrayList<Reviews>());



                }
            }


            else
            {
                mov = (Movie)getArguments().getSerializable("MyMovie");
                String  Movie_id = mov.getId();

                //adapter=new MovieDetaialsAdapter(getActivity(),mov);
                adapter =new MovieDetaialsAdapter();

                TrailerTask T_task = new TrailerTask();
                T_task.execute(Movie_id);

                //************** edit**************//

                String id    =mov.getId();
                String title =mov.getTitle();
                String poster = mov.getPoster();
                String overview = mov.getOverview();
                String average = mov.getRelease();
                String release = mov.getRelease();

                NewMovie = new Movie(poster , title, overview , average , release, id , new ArrayList<Trailer>(), new ArrayList<Reviews>());


            }



            //String title = mov.getTitle();
            String title = NewMovie.getTitle();
            getActivity().setTitle(title);


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String type = adapter.getType(position);
                    Log.v("THE LISTENE", type);

                    if(type.equals("trailer")){
                        //Trailer t = mov.getTrailerList().get(position -1); //trailer object
                        Trailer t = NewMovie.getTrailerList().get(position -1); //trailer object

                        String url = t.getPath();
                        Log.v("THE PATH", url);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    }

                }
            });

                return rootView;
        }

        //***********************************LOAD DATA ******************************************************************//


        //*************************************ASYNCTASK 1 ******************************************************************//

        public class TrailerTask extends AsyncTask<String, Void , ArrayList<Trailer>> {


            private  final String LOG_TAG =TrailerTask.class.getSimpleName() ;


            String id;
            @Override
            protected ArrayList<Trailer> doInBackground(String... params) {

               id = params[0];
                Log.v(LOG_TAG, "Built ID " + id);

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String TrailerJsonStr = null;

                try {

                    //                 http://api.themoviedb.org/3/movie/140607/videos?api_key=8bc08a9df4be6a1e5535904818094ebe
                    URL url = new URL("http://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+ Keys.MOVIE_API_KEY );
                    Log.v(LOG_TAG, "Built URI " + url);

                    // Create the request to website, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();   //can't do this on main thread


                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }

                    TrailerJsonStr = buffer.toString();
                } catch (IOException e)
                {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    return null;
                }
                finally
                {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }


                //after connecting to server and read in input stream ,, parse that stream
                try {
                    return getTrailerDataFromJson(TrailerJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }


            //fn to parse the json response

            private ArrayList<Trailer> getTrailerDataFromJson(String TrailerJsonStr)
                    throws JSONException {

                // These are the names of the JSON objects that need to be extracted.
                final String M_LIST = "results";


                JSONObject forecastJson = new JSONObject(TrailerJsonStr);     //turn json string into json object
                JSONArray resultsArray = forecastJson.getJSONArray(M_LIST);   //look for the list array

                int sizelist = resultsArray.length();

                Trailer[] resultStrs = new Trailer[sizelist];
                for(int i = 0; i < sizelist; i++) {

                    String name ;
                    String m_key;
                    String Tid;


                    // Get the JSON object representing the movie represented by the index i
                    JSONObject SingleTrailer = resultsArray.getJSONObject(i);

                    name = SingleTrailer.getString("name");
                    m_key = SingleTrailer.getString("key");
                    Tid = SingleTrailer.getString("id");

                    //             https://www.youtube.com/watch?v=SUXWAEX2jlg
                    String path = "https://www.youtube.com/watch?v="+ m_key;


                    Log.v(LOG_TAG, "the youtube url : " + path);
                    resultStrs[i] = new Trailer(name , path, Tid);
                    Log.v(LOG_TAG, "the youtube url : " + resultStrs[i].getPath());


                }

                //just for check ... print every element in the string array
                for (Trailer s : resultStrs) {
                    Log.v(LOG_TAG, "Movie entry: " + s.Path);
                }

                ArrayList<Trailer> resultStr=  new ArrayList<Trailer>(Arrays.asList(resultStrs));

                Tflag =1;

                return resultStr;

            }


            @Override
            protected void onPostExecute(ArrayList<Trailer> trailer) {

               // new MovieDetaialsAdapter()

                //set the trailers list of that movie
                trailerArrayList = trailer;
               // mov.setTrailerList(trailerArrayList);
                NewMovie.setTrailerList(trailerArrayList);
                ReviewsTask Rtask = new ReviewsTask();
                Rtask.execute(id);

                }
            }

        //*************************************ASYNCTASK 2  ******************************************************************//

        public class ReviewsTask extends AsyncTask<String, Void , ArrayList<Reviews>> {


            private  final String LOG_TAG =ReviewsTask.class.getSimpleName() ;


            @Override
            protected ArrayList<Reviews> doInBackground(String... params) {

                //id of current movie
                String id = params[0];

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String ReviewJsonStr = null;

                try {

                    //                 http://api.themoviedb.org/3/movie/140607/reviews?api_key=8bc08a9df4be6a1e5535904818094ebe
                    URL url = new URL("http://api.themoviedb.org/3/movie/"+id+"/reviews?api_key="+ Keys.MOVIE_API_KEY );
                    Log.v(LOG_TAG, "Built URI " + url);

                    // Create the request to website, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();   //can't do this on main thread


                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }

                    ReviewJsonStr = buffer.toString();
                } catch (IOException e)
                {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    return null;
                }
                finally
                {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }


                //after connecting to server and read in input stream ,, parse that stream
                try {
                    return getReviewDataFromJson(ReviewJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }


            //fn to parse the json response

            private ArrayList<Reviews> getReviewDataFromJson(String TrailerJsonStr)
                    throws JSONException {

                // These are the names of the JSON objects that need to be extracted.
                final String M_LIST = "results";


                JSONObject forecastJson = new JSONObject(TrailerJsonStr);     //turn json string into json object
                JSONArray resultsArray = forecastJson.getJSONArray(M_LIST);   //look for the list array

                int sizelist = resultsArray.length();

                Reviews[] resultStrs = new Reviews[sizelist];
                for(int i = 0; i < sizelist; i++) {

                    String Author ;
                    String Content;
                    String Rid;


                    // Get the JSON object representing the movie represented by the index i
                    JSONObject SingleReview = resultsArray.getJSONObject(i);

                    Author = SingleReview.getString("author");
                    Content = SingleReview.getString("content");
                    Rid = SingleReview.getString("id");

                    resultStrs[i] = new Reviews( Content ,Author, Rid);
                }

                //just for check ... print every element in the string array
                for (Reviews s : resultStrs) {
                    Log.v(LOG_TAG, "Movie entry: " + s.Author);
                }

                ArrayList<Reviews> resultStr=  new ArrayList<Reviews>(Arrays.asList(resultStrs));

                Rflag =1;

                return resultStr;

            }


            @Override
            protected void onPostExecute(ArrayList<Reviews> Review) {


                // new MovieDetaialsAdapter()
                reviewArrayList = Review;
                //mov.setReviewsList(reviewArrayList);
                NewMovie.setReviewsList(reviewArrayList);
                //adapter = new MovieDetaialsAdapter(getActivity(), mov);
                adapter = new MovieDetaialsAdapter(getActivity(), NewMovie);
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();



            }
        }




    }




        }



