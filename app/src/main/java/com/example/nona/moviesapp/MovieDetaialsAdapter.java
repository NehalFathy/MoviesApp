package com.example.nona.moviesapp;

import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetaialsAdapter extends BaseAdapter {

    Activity activity;
    Movie movie;
    LayoutInflater inflater;


    public MovieDetaialsAdapter(){

    }


    //it has a single movie
    public MovieDetaialsAdapter(Activity activity, Movie details) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.movie = details;
    }

    @Override
    public int getCount() {
        return 1+ movie.reviewsList.size() + movie.trailerList.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    public Trailer getTrailer(int position) {
         Trailer tr = movie.getTrailerList().get(position);
        return tr;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    //To get the type of row
    String getType(int position){
        if(position==0)
            return "details";
        else if(position  <=  movie.trailerList.size())
            return "trailer";
        else
            return "review";
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String LOG_TAG =MovieDetaialsAdapter.class.getSimpleName() ;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int resource = 0;

        // Here you set ‘resource’ with the correct layout, for the row
        // given by the parameter ‘position.’

        switch (getType(position)){

            case "details": resource = R.layout.row_details;
                break;
            case "trailer": resource = R.layout.row_trailers;
                break;
            case "review" : resource = R.layout.row_reviews;
                break;
        }

        convertView = inflater.inflate(resource,parent,false);
        // Here initialize the contents of the newly created view.
        switch (resource){

            case R.layout.row_details:

                String title =  movie.getTitle();
                String date =   movie.getRelease();
                String plot =   movie.getOverview();
                String average= movie.getAverage_Vote();
                String url =    movie.getPoster();

                ((TextView) convertView.findViewById(R.id.Title_textView))
                        .setText(title);
                ((TextView) convertView.findViewById(R.id.Rating_textView))
                        .setText(average + " / 10");
                ((TextView) convertView.findViewById(R.id.Date_textView))
                        .setText(date);
                TextView plot1 = (TextView) convertView.findViewById(R.id.plot_textView);
                plot1.setMovementMethod(new ScrollingMovementMethod());
                plot1.setText(plot);

                ImageView image = (ImageView) convertView.findViewById(R.id.poster_imageView);
                Picasso.with(activity).load(url).fit().centerCrop().into(image);

                final ImageButton imbut = (ImageButton) convertView.findViewById(R.id.fav_Button);
                //imbut.setImageResource(android.R.drawable.btn_);

                    MovieDbHelper MH = new MovieDbHelper(activity);
                final boolean check = MH.checkIfExist(movie.getId());
                Log.d(LOG_TAG , String.valueOf(check));
                if (check==true){
                    imbut.setImageResource(android.R.drawable.btn_star_big_on);

                }

                imbut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(check==false) {

                            imbut.setImageResource(android.R.drawable.btn_star_big_on);
                            //movie.setIsFavorite();
                            MovieDbHelper MH = new MovieDbHelper(activity);
                            MH.AddMovie(movie);
                           // MH.AddMovie(MH , movie);
                            Toast.makeText(activity, "Movie saved", Toast.LENGTH_SHORT).show();

                        }
                        else if(check==true)
                        {
                            Toast.makeText(activity, "It seems already favorite !", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                // Reading all contacts
                Log.d("Reading: ", "Reading all movies..");
                List<Movie> listmovies = MH.getAllMovies();
                int i =0;
                for (Movie cn : listmovies) {
                    String log = "Id: "+cn.getId()+" ,Name: " + cn.getTitle() + " ,Poster: " + cn.getPoster() +" ,average: " + cn.getAverage_Vote()
                                 +" ,overview: " + cn.getOverview() + " ,Release "+ cn.getRelease();
                    // Writing Contacts to log
                    Log.d("Name: ", log);
                    Log.d("Name: ", Integer.toString(i));
                    i++;
                }

                break;

            case R.layout.row_trailers:
                Trailer trailer =  movie.getTrailerList().get(position -1);
                String trailer_name = trailer.getTrailer();
                TextView text = (TextView) convertView.findViewById(R.id.Trailer_textView);
                text.setText(trailer_name);
                break;

            case R.layout.row_reviews:
                Reviews review  = movie.getReviewsList().get(position - 1-  movie.getTrailerList().size());
                TextView auther = (TextView) convertView.findViewById(R.id.Author_TextView);
                auther.setText(review.getAuthor());

                TextView rev = (TextView) convertView.findViewById(R.id.Review_textView);
                rev.setText( review.getReview());
                break;

        }


     return convertView;
    }


}
