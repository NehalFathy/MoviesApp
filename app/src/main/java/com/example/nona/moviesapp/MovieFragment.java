package com.example.nona.moviesapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    MovieAdapter adapter;
    GridView gridview;
    public MovieFragment() {
       // adapter = new MovieAdapter(getContext() , new ArrayList<Movie>());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        adapter = new MovieAdapter();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridview = (GridView)rootView.findViewById(R.id.gridview_movies);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie m = adapter.getItem(position);

                String x = m.getTitle();
                Log.i("moviiiiiiiiiiiiiiii", x);

                ((Callback)getActivity()).onItemSelected(m);



            }
        });
        return rootView;
    }

    //***********************************LOAD DATA ******************************************************************//

    private void updateMovie() {
        MovieTask task = new MovieTask();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = sharedPrefs.getString(getString(R.string.pref_movie_key),
                          getString(R.string.pref_most_popular));
        Log.d("sort type 444", sortType);
        if(sortType.equals("vote_average.desc") || sortType.equals("popularity.desc"))
        {
            task.execute(sortType);
        }

        else if (sortType.equals("favorite"))
        {
            MovieDbHelper MH = new MovieDbHelper(getActivity());

            // Reading all contacts
            Log.d("Reading from main: ", "Reading all movies..");
            List<Movie> listmovies = MH.getAllMovies();
            int i =0;
            for (Movie cn : listmovies) {
                String log = "Id: "+cn.getId()+" ,Name: " + cn.getTitle() + " ,Poster: " + cn.getPoster() +" ,average: " + cn.getAverage_Vote()
                        +" ,overview: " + cn.getOverview() + " ,Release "+ cn.getRelease() ;
                // Writing Contacts to log
                Log.d("Namem: ", log);
                Log.d("Namem: ", Integer.toString(i));
                i++;
            }

            ArrayList<Movie> allmovie = new  ArrayList<Movie>(listmovies);

            adapter = new MovieAdapter(getContext(), allmovie);
            //gridview.setAdapter(new MovieAdapter(getActivity(), movies));
            gridview.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();  //as its helper method we can call it from onstart
    }


    //************************************HANDLER *************************************************************///
        public interface Callback {
            /**
             * DetailFragmentCallback for when an item has been selected.
             */
            public void onItemSelected(Movie movie);
        }


    //*************************************ASYNCTASK  ******************************************************************//

    public class MovieTask extends AsyncTask<String , Void , ArrayList<Movie>> {


        private  final String LOG_TAG =MovieTask.class.getSimpleName() ;


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String MovieJsonStr = null;

            try {

               // URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+ Keys.MOVIE_API_KEY );

               // Log.v(LOG_TAG, "Built URI " +url.toString() );


                final String MOVIE_BASE_URL =
                          "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM = "sort_by";
                final String APPKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])  //zero position in the the string we passed in execute
                        .appendQueryParameter(APPKEY_PARAM, Keys.MOVIE_API_KEY )
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

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

                MovieJsonStr = buffer.toString();
            }
            catch (IOException e)
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
                return getMovieDataFromJson(MovieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }


        //fn to parse the json response

        private ArrayList<Movie> getMovieDataFromJson(String  MovieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String M_LIST = "results";
            final String M_OVERVIEW = "overview";
            final String M_TITLE = "original_title";
            final String M_AVGVOTE = "vote_average";
            final String M_DATE = "release_date";
            final String M_ID = "id";


            JSONObject forecastJson = new JSONObject(MovieJsonStr);     //turn json string into json object
            JSONArray resultsArray = forecastJson.getJSONArray(M_LIST);   //look for the list array

            int sizelist = resultsArray.length();

            Movie[] resultStrs = new Movie[sizelist];

            for(int i = 0; i < sizelist; i++) {

                String imjurl;
                String poster ;
                String name , overview , AvgVote;
                String release ;
                String m_id;


                // Get the JSON object representing the movie represented by the index i
                JSONObject SingleMovie = resultsArray.getJSONObject(i);

                imjurl = SingleMovie.getString( "poster_path");
                poster = "http://image.tmdb.org/t/p/" + "w185" +imjurl;
                name = SingleMovie.getString(M_TITLE);
                overview = SingleMovie.getString(M_OVERVIEW);
                AvgVote = SingleMovie.getString(M_AVGVOTE);
                release = SingleMovie.getString(M_DATE);
                m_id =SingleMovie.getString(M_ID);

                resultStrs[i] = new Movie(poster ,name , overview , AvgVote ,release,m_id , new ArrayList<Trailer>(), new ArrayList<Reviews>());
            }

            //just for check ... print every element in the string array
            for (Movie s : resultStrs) {
                Log.v(LOG_TAG, "Movie entry: " + s.getTitle());
            }

            ArrayList<Movie> resultStr=  new ArrayList<Movie>(Arrays.asList(resultStrs));

            return resultStr;
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            if (movies != null) {
                adapter = new MovieAdapter(getContext(), movies);
                //gridview.setAdapter(new MovieAdapter(getActivity(), movies));
                gridview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Log.i("movies" , movies.size()+"");

            }
        }
    }




}
