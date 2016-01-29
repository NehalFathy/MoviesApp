package com.example.nona.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ///////////////////////////////////////////////////

        if (findViewById(R.id.mainFrame)==null){
            // Phone Layout has been loaded
            if (savedInstanceState == null){

                mTwoPane = false;

             getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieFragment())
                    .commit();

            }
        }
        else{
            //Tablet layout has been loaded

            mTwoPane = true;

            FragmentManager fragmentManager=getSupportFragmentManager();
            Bundle bundel = new Bundle();
            bundel.putString("Type" , "Tablet");

            MovieFragment fragment = new MovieFragment();
            fragment.setArguments(bundel);

            fragmentManager.beginTransaction().replace(R.id.leftFrame,fragment).commit();

        }

//        if (savedInstanceState == null){
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MovieFragment())
//                    .commit();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Movie m) {

        if (mTwoPane)
        {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            Log.d("check","creating bundle");
            Bundle bundel = new Bundle();
            bundel.putSerializable("MyMovie", m);
            DetailActivity.DetailFragment fragment = new DetailActivity.DetailFragment();
            fragment.setArguments(bundel);

            getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.mainFrame, fragment)
                                        .commit();

        }

        else
        {
            Intent intent =    new Intent(this, DetailActivity.class).putExtra("MyMovie", m);
            startActivity(intent);

        }
    }
}
