package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.ReviewBit;
import edu.calstatela.cpham24.eloteroman.R;

public class DisplayVendorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ReviewBit>>,
        VendProAdapt.ItemClickListener,
        AdapterView.OnItemSelectedListener {

    static final String TAG = "mainactivity";
    private TextView vendName;
    private TextView cartName;
    private TextView days;
    private TextView hours;
    private TextView curWork;

    private ProgressBar progress;
    private RecyclerView rv;
    private VendProAdapt adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_vendor);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        vendName = (TextView) findViewById(R.id.venN);
        cartName = (TextView) findViewById(R.id.carN);
        days = (TextView) findViewById(R.id.dayN);
        hours = (TextView) findViewById(R.id.houN);
        curWork = (TextView) findViewById(R.id.curN);



        rv = (RecyclerView)findViewById(R.id.recyclerViewReview);


        rv.setLayoutManager(new LinearLayoutManager(this));


        load("Melody Song");

    }



    /*
        @Override
        public void onItemClick(int clickedItemIndex) {

        }
    */
    public void load(String name) {

        Bundle red = new Bundle();
        red.putString("vendy", name);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(1, red, this).forceLoad();

    }






    @Override
    public Loader<ArrayList<ReviewBit>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<ReviewBit>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public ArrayList<ReviewBit> loadInBackground() {

                URL url;

                url = NetworkForRev.makeURL();
                //Vender ron = null;
                ArrayList<ReviewBit> result = null;

                String red = null;


                try{
                    red = NetworkForRev.getResponseFromHttpUrl(url);
                    Log.d(TAG , "url: sho " + url.toString() + " oh ");

                    result = NetworkForRev.parseJSON(red);


                }
                catch(Exception e){
                    e.printStackTrace();
                }

                //This whole block is for the textviews.
                //Ignore this, focus on the other try/catch
                /*

                url = NetworkForRev.makeURL(args.getString("vendy"));
                Log.d("red" , "url: ko " + url.toString() + " oh ");

                try{
                    red = NetworkForRev.getResponseFromHttpUrl(url);
                    Log.d(TAG , "url: mo " + url.toString() + " oh ");

                    ron = NetworkForRev.parseJSONfor(red);


                }
                catch(Exception e){
                    e.printStackTrace();
                }


                if (ron == null && result == null) {
                    Log.d(TAG, "Wrong");
                    try{
                        red = NetworkForRev.getResponseFromHttpUrl(url);
                        Log.d(TAG , "url: mo " + url.toString() + " oh ");

                        ron = NetworkForRev.parseJSONfor(red);


                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

                vendName.setText(ron.getName());
                cartName.setText(ron.getCName());
                days.setText(ron.getDesc());
                hours.setText(ron.getWorkHour());

                if (ron.getWork() == true) {
                    curWork.setText("Currently Working!");
                }

                else {
                    curWork.setText("Not Working...");
                }
*/
                return result;
            }

        };
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<ReviewBit>> loader, final ArrayList<ReviewBit> s) {

        Log.d(TAG, " Here");

        progress.setVisibility(View.GONE);
        if(s != null){
            VendProAdapt adapter = new VendProAdapt(s, new VendProAdapt.ItemClickListener() {
                @Override
                public void onItemClick(int clickedItemIndex) {
                    //click stuff
                }
            });
            rv.setAdapter(adapter);

        }


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ReviewBit>> loader) {

    }


    //Sort spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onItemClick(int clickedItemIndex) {

    }
}
