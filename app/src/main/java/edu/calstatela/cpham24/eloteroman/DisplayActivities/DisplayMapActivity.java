package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.NetworkUtils.DataJsonUtils;
import edu.calstatela.cpham24.eloteroman.NetworkUtils.NetworkUtils;
import edu.calstatela.cpham24.eloteroman.NetworkUtils.VendorItem;
import edu.calstatela.cpham24.eloteroman.R;

public class DisplayMapActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "DisplayMapActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static final float DEFAULT_ZOOM = (float) 16.5; // shows the neighborhood
    private static final int LOCATIONSLOADER = 69;

    private String username;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context context;
    private ArrayList<VendorItem> vendors;
    private LatLng currentLoc;
    private ArrayList<Marker> markers;
    private HandlerThread handlerThread;
    private Handler handler;
    private Runnable runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);

        username = getIntent().getExtras().getString("username");
        context = this;
        markers = null;
        handler = null;
        handlerThread = null;
        runner = null;

        findViewById(R.id.quick_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Comments", " push me ");
                String query = ((TextView)findViewById(R.id.quick_search_text)).getText().toString();
                Intent i = new Intent(context, DisplaySearchActivity.class);
                i.putExtra("query", query);
                startActivity(i);
            }
        });

        findViewById(R.id.appbar).bringToFront();
        findViewById(R.id.profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DisplayProfileActivity.class);
                i.putExtra("username", username);
                startActivity(i);
            }
        });
        findViewById(R.id.locator_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);
                getCurrentLocation();
            }
        });
    }

    // overriding onStart to load from db every time the app starts/resumes
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "revived, restoring connections");
        if(markers != null) {
            updateMarkers();
        }
    }

    // overriding onStop to "clean up" every time the app is shut down
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "died, severing connections");
        if(handler != null) { // kills the handler task
            handler.removeCallbacks(runner);
            handler = null;
            runner = null;
            handlerThread.quit();
            handlerThread = null;
        }
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
                    currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "my current position is " + location.getLatitude() + ", " + location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, DEFAULT_ZOOM), 1000, null);
                    loadData();
                }
            }
        });
    }

    // return the distance from user in minutes
    private double getTimeFromUser(double lat, double lon) {
        // adjust this constant to change the reading
        final double USER_SPEED = 67.8; // in meters per hour
        return SphericalUtil.computeDistanceBetween(currentLoc, new LatLng(lat, lon)) / USER_SPEED;
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
                        vendors = queryVendorData();

                        try {
                            for(int i=0; i<vendors.size(); i++) {
                                VendorItem v = vendors.get(i);
                                if(v.img_url != null)
                                    vendors.get(i).img = Picasso.with(context).load(v.img_url).get();
                            }
                        } catch (Exception e) {
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

                // the following code sets a custom marker to be displayed instead of the default marker
                markers = new ArrayList<Marker>();
                Display d = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                d.getSize(size);
                int width = size.x;
                int height = size.y;

                Bitmap original = BitmapFactory.decodeResource(context.getResources(), R.drawable.cart_icon);
                Bitmap scaled = Bitmap.createScaledBitmap(original, width/10, width/10, true);

                for(int i=0; i<vendors.size(); i++) {
                    VendorItem v = vendors.get(i);
                    Log.d(TAG, "added a marker for " + v.cart_name);
                    Log.d(TAG, "location: " + v.location.latitude + ", " + v.location.longitude);

                    double time = getTimeFromUser(v.location.latitude, v.location.longitude);

                    Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(v.location.latitude, v.location.longitude)).title(v.cart_name).snippet((int)Math.round(time) + " mins away").icon(BitmapDescriptorFactory.fromBitmap(scaled)));
                    markers.add(m);

                    vendors.get(i).marker_id = m.getId();

                    Log.d(TAG, "distance: " + time + " mins");
                }

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

                // enables interaction with the info window
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        VendorItem vendor = null;

                        for(int i=0; i<vendors.size(); i++) { // find a vendor item based on position
                            LatLng pos = marker.getPosition();
                            VendorItem v = vendors.get(i);
                            if(v.location.latitude == pos.latitude && v.location.longitude == pos.longitude)
                                vendor = v;
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        vendor.img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] buffer = stream.toByteArray();

                        MapDialogFragment df = new MapDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("id", vendor.id);
                        args.putString("cart_name", vendor.cart_name);
                        args.putString("owner_name", vendor.owner_name);
                        args.putString("days", vendor.days);
                        args.putString("hours", vendor.hours);
                        args.putBoolean("working", vendor.in_service);
                        args.putByteArray("img", buffer);
                        df.setArguments(args);

                        final VendorItem vendor_inner = vendor;

                        df.setCallback(new MapDialogFragment.MapDialogCallback() {
                            @Override
                            public void OnMoreInfoClick(String id) {
                                Intent i = new Intent(context, DisplayVendorActivity.class);
                                i.putExtra("vendor_id", id);
                                startActivity(i);
                            }

                            @Override
                            public void OnDirectionsClick(String id) {
                                // todo: send lat long to built-in Maps
                                Log.d(TAG, "user chose to view directions");
                                // Creates an intent to load walking directions
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + vendor_inner.location.latitude + "," + vendor_inner.location.longitude + "&mode=w");
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }

                            @Override
                            public void OnBackClick(String id) {
                                // do nothing
                                Log.d(TAG, "user chose to cancel");
                            }
                        });

                        df.show(getFragmentManager(), "vendor");
                    }
                });

                // enables custom info window view
                mMap.setInfoWindowAdapter(new EloteroInfoWindowAdapter(context, vendors));

                updateMarkers();
            }

            @Override
            public void onLoaderReset(Loader<Void> loader) {
                // doesn't really do anything but is required for the interface
            }
        }).forceLoad();
    }

    private ArrayList<VendorItem> queryVendorData() {
        ArrayList<VendorItem> temp_vendors = null;
        URL vendorUrl = NetworkUtils.buildVendorUrl();

        try {
            String vendorJson = NetworkUtils.getResponseFromHttpUrl(vendorUrl);
            temp_vendors = DataJsonUtils.parseVendorsFromJson(vendorJson);
        } catch (Exception e) { // catch all exceptions
            e.printStackTrace();
        }

        return temp_vendors;
    }

    private int findMarkerIndex(Marker m) {
        Log.d(TAG, "looking for " + m.getId());
        for(int i=0; i<vendors.size(); i++) {
            Log.d(TAG, "found " + vendors.get(i).marker_id);
            if(m.getId().equals(vendors.get(i).marker_id))
                return i;
        }

        return -1;
    }

    private void updateMarkers() {
        handlerThread = new HandlerThread(TAG + "Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        runner = new Runnable() {
            @Override
            public void run() {
                ArrayList<VendorItem> temp = queryVendorData(); // will run in separate thread

                for(int i=0; i<temp.size(); i++) {
                    if(temp.get(i).id.equals(vendors.get(i).id)) {
                        vendors.get(i).location.latitude = temp.get(i).location.latitude;
                        vendors.get(i).location.longitude = temp.get(i).location.longitude;
                    }
                }

                Handler mainHandler = new Handler(context.getMainLooper());
                mainHandler.post(new Runnable() { // this will happen on main thread now
                    @Override
                    public void run() {
                        for(int i=0; i<markers.size(); i++) {
                            int j = findMarkerIndex(markers.get(i));

                            markers.get(i).setPosition(new LatLng(vendors.get(j).location.latitude, vendors.get(j).location.longitude));
                        }

                        Toast.makeText(context, "updated marker positions", Toast.LENGTH_SHORT).show();
                    }
                });

                handler.postDelayed(this, 10000);
            }
        };

        handler.postDelayed(runner, 10000);
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
