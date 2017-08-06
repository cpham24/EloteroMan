package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.URL;
import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.SearchPageUtils.AdvancedSearchFrag;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.SearchPageUtils.EloAdapt;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.SearchPageUtils.NetworkUtils;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.Vender;
import edu.calstatela.cpham24.eloteroman.R;

public class DisplaySearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Vender>>,
        EloAdapt.ItemClickListener, AdapterView.OnItemSelectedListener, AdvancedSearchFrag.OnDialogCloseListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final String TAG = "mainactivity";
    LocationManager locationManager;
    Location LastLocation;
    private ProgressBar progress;
    private RecyclerView rv;
    private EloAdapt adapter;
    private EditText search;
    private Button searchBut;
    private Button advSe;
    Spinner spinner;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private double longitude;
    private double latitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);

        int LOCATION_REFRESH_TIME = 1000;
        int LOCATION_REFRESH_DISTANCE = 5;

        /*
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
                */

        handlePermissionsAndGetLocation();


        progress = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.searchQuery);
        searchBut = (Button) findViewById(R.id.findVend);
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                Log.d(TAG, " cmon " + search.getText().toString() );

                //Doesn't take any queries from user, just simply displays all carts for testing purposes
                load(search.getText().toString());

            }
        });


        advSe = (Button) findViewById(R.id.advancedSearch);

        advSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                FragmentManager fm = getSupportFragmentManager();
                AdvancedSearchFrag frag = new AdvancedSearchFrag();
                frag.show(fm, "advSearchThing");


            }
        });

        spinner = (Spinner) findViewById(R.id.choice_spinner);


        ArrayAdapter<CharSequence> spindapter = ArrayAdapter.createFromResource(this,
                R.array.simpleSort, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        spindapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spindapter);

        spinner.setOnItemSelectedListener(this);



        rv = (RecyclerView)findViewById(R.id.recyclerViewSearch);


        rv.setLayoutManager(new LinearLayoutManager(this));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Log.d(TAG, " cccc ");

        Log.d(TAG, " wwww " + latitude + " and " + longitude);


        load(null);


    }

//location code provided by this sample
//https://github.com/keithweaver/Android-Samples/blob/master/Location/GetLocationAndroidM/app/src/main/java/com/weaverprojects/getlocationandroidm/MainActivity.java
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Accepted
                    getLocation();
                } else {
                    // Denied
                    Toast.makeText(this, "LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void handlePermissionsAndGetLocation() {
        Log.v(TAG, "handlePermissionsAndGetLocation");
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
            return;
        }
        getLocation();//if already has permission
    }


    protected void getLocation() {
        Log.v(TAG, "GetLocation");
        int LOCATION_REFRESH_TIME = 1000;
        int LOCATION_REFRESH_DISTANCE = 5;

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Log.v("WEAVER_", "Has permission");
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        } else {
            Log.v("WEAVER_", "Does not have permission");
        }

    }



    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //code
            System.out.println("onLocationChanged");

            LastLocation = location;

            Log.d(TAG, "Latitude:" + String.valueOf(location.getLatitude()) + "\n" +
                    "Longitude:" + String.valueOf(location.getLongitude()));


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
            //turns off gps services
        }
    };





    public void closeDialog(String ca, String ve, String fo, String le, String ri, String on, String tw, String dw) {
        Log.d(TAG, " where2 " + ca);

        load(ca, ve, fo, le, ri, on, tw, dw);


    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */


    @Override
    public void onItemClick(int clickedItemIndex) {

    }

    public void load(String basic) {

        Bundle red = new Bundle();
        red.putString("shed", basic);
        red.putString("find", null);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        LastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (LastLocation != null) {
            longitude = LastLocation.getLongitude();
            latitude = LastLocation.getLatitude();

        }

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(1, red, this).forceLoad();

    }



    public void load(String ca, String ve, String fo, String le, String ri, String on, String tw, String dw) {
        Log.d(TAG, " where3 " + ca);
        Bundle place = new Bundle();
        place.putString("cartName", ca);
        place.putString("ownerName", ve);
        place.putString("foodName", fo);
        place.putString("leftTime", le);
        place.putString("rightTime", ri);
        place.putString("leftDayTime", on);
        place.putString("rightDayTime", tw);
        place.putString("theDay", dw);


        Log.d(TAG, " bunch ");
        Bundle red = new Bundle();
        red.putBundle("lot", place);
        red.putString("find", "advanced");
        red.putString("shed", null);




        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        LastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (LastLocation != null) {
            longitude = LastLocation.getLongitude();
            latitude = LastLocation.getLatitude();

        }


        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(1, red, this).forceLoad();

    }





    @Override
    public Loader<ArrayList<Vender>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Vender>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public ArrayList<Vender> loadInBackground() {

                URL url;

                url = NetworkUtils.makeURL();

                ArrayList<Vender> result = new ArrayList<>();
                String red = null;
                try{
                    red = NetworkUtils.getResponseFromHttpUrl(url);
                    Log.d("red" , "url: " + url.toString() + " oh " + args.getString("find"));

                    if (args.getString("find") != null) {
                        Log.d(TAG, " where4 " + args.getBundle("lot"));
                        result = NetworkUtils.parseJSON(red, args.getBundle("lot"), longitude, latitude);

                    }

                    else {
                        Log.d(TAG, " where7 " + args.getString("shed"));
                        result = NetworkUtils.parseJSON(red, args.getString("shed"), longitude, latitude);
                        Log.d(TAG, " where12 " + result);
                    }



                }catch(Exception e){
                    Log.d(TAG, " where13 " + e);
                    e.printStackTrace();
                }


                Log.d(TAG, " where5 " + result);
                return result;
            }

        };
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Vender>> loader, final ArrayList<Vender> s) {

        Log.d(TAG, " Here");

        progress.setVisibility(View.GONE);
        if(s == null){
            EloAdapt adapter = new EloAdapt(s, new EloAdapt.ItemClickListener() {
                @Override
                public void onItemClick(int clickedItemIndex) {
                    //click stuff
                    //probably start activity to go to vendor page
                }
            });
            rv.setAdapter(adapter);


            Toast toast = Toast.makeText(this, " Sorry, no cart carries that ", Toast.LENGTH_LONG);
            toast.show();
        }

        else {
            EloAdapt adapter = new EloAdapt(s, new EloAdapt.ItemClickListener() {
                @Override
                public void onItemClick(int clickedItemIndex) {
                    //click stuff
                    //probably start activity to go to vendor page
                }
            });
            rv.setAdapter(adapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Vender>> loader) {

    }


    //Sort spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, " woah " + location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
