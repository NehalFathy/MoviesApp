package com.example.nona.moviesapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie>list;

    public MovieAdapter(){

    }

    public MovieAdapter(Context context, ArrayList<Movie> list) {
        this.context = context;
        this.list = list;
    }
    /*public MovieAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }*/



    void add(Movie movie){
        list.add(movie);
        notifyDataSetChanged();
    }

    void clear (){
        list.clear();
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */



    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Movie getItem(int position) {
        return list.get(position);    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // reuse views
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = inflater.inflate(R.layout.grid_single_item, null);
        }
        //TextView name = (TextView) convertView.findViewById(R.id.Single_grid_textview);
        //String x=list.get(position);

        Movie x=list.get(position);
        String url = x.getPoster();
            //name.setText(x);

        ImageView image = (ImageView) convertView.findViewById(R.id.grid_item_movies_imageView);

        //Picasso.with(context).load("http://image.tmdb.org/t/p/w185/fYzpM9GmpBlIC893fNjoWCwE24H.jpg").into(image);
        Picasso.with(context).load(url).fit().centerCrop().into(image);
        Log.e("4444444", url) ;


        //ImageView imageview = (ImageView) convertView.findViewById(R.id.grid_item_movies_imageView);
       // Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(image);


        return convertView;
    }
}
