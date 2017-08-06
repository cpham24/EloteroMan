package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.URL;
import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.NetworkUtils.LocationItem;
import edu.calstatela.cpham24.eloteroman.NetworkUtils.NetworkUtils;
import edu.calstatela.cpham24.eloteroman.NetworkUtils.DataJsonUtils;
import edu.calstatela.cpham24.eloteroman.NetworkUtils.VendorItem;
import edu.calstatela.cpham24.eloteroman.R;

public class DisplayMapActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "DisplayMapActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final float DEFAULT_ZOOM = (float) 17.3; // shows the neighborhood
    private static final int LOCATIONSLOADER = 69;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context context;
    private ArrayList<VendorItem> vendors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);

        context = this;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                getCurrentLocation();
                loadData();
            }
        });
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "my current position is " + location.getLatitude() + ", " + location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, (float) 10.0));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM), 2000, null);
                }
            }
        });
    }

    private void loadData() {
        LoaderManager lm = getSupportLoaderManager();
        lm.restartLoader(LOCATIONSLOADER, null, new LoaderManager.LoaderCallbacks<Void>() {
            // overriding onCreateLoader to start the AsyncTaskLoader
            @Override
            public Loader<Void> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Void>(context) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        //mProgressIndicator.setVisibility(View.VISIBLE);
                        Log.d(TAG, "starting async task to load articles in background");
                    }

                    @Override
                    public Void loadInBackground() {
                        // get all vendors
                        vendors = null;
                        URL vendorUrl = NetworkUtils.buildVendorUrl();

                        try {
                            String vendorJson = NetworkUtils.getResponseFromHttpUrl(vendorUrl);
                            vendors = DataJsonUtils.parseVendorsFromJson(vendorJson);
                        } catch (Exception e) { // catch all exceptions
                            e.printStackTrace();
                        }

                        return null;
                    }
                };
            }

            // overriding onLoadFinished to load from db into view
            @Override
            public void onLoadFinished(Loader<Void> loader, Void data) {
                Log.d(TAG, "loaded data for " + vendors.size() + " vendors from network");

                for(int i=0; i<vendors.size(); i++) {
                    VendorItem v = vendors.get(i);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(v.location.latitude, v.location.longitude)).title(v.cart_name));
                    Log.d(TAG, "added a marker for " + v.cart_name);

                    // enables interaction with the markers
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            VendorItem vendor = null;

                            for(int i=0; i<vendors.size(); i++) { // find a vendor item based on position
                                LatLng pos = marker.getPosition();
                                VendorItem v = vendors.get(i);
                                if(v.location.latitude == pos.latitude && v.location.longitude == pos.longitude)
                                    vendor = v;
                            }

                            if(vendor != null)
                                Log.d(TAG, "user clicked on " + vendor.cart_name + " owned by " + vendor.owner_name);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onLoaderReset(Loader<Void> loader) {
                // doesn't really do anything but is required for the interface
            }
        }).forceLoad();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "permission request answered");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Log.d(TAG, "permission granted");
                return;
        }
        // this means the device will keep asking for permission until it is granted
        // TODO: display a proper error message
        getCurrentLocation();
    }
}
