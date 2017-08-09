package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.R;
import edu.calstatela.cpham24.eloteroman.VendorUtils.ReviewsAdapter;

/**
 * Created by Dezval on 8/7/2017.
 */

public class DisplayVendorReviewsActivity extends AppCompatActivity {

    private String[] temp = {"ITEM ONE", "ITEM TWO", "ITEM THREE", "ITEM FOUR", "ITEM FIVE"};
    private ArrayList<ArrayList<String>> ReviewsList = new ArrayList<ArrayList<String>>();
    private ArrayList<String> Review = new ArrayList<String>();

    private RecyclerView recyclerView;

    private Context context;

    private RequestQueue requestQueue;
    private String vendorID;
    private TextView usersNameTextView;
    private TextView userCommentTextView;
    private ImageView userImageView;
    private RatingBar vendorRatingBar;
    private String getOneCartURL = "http://162.243.112.34:3000/Eloteroman/getOneCart?id=";
    private String getOneUserURL = "http://162.243.112.34:3000/EloteroMan/getOneUser?id=";
    private String TAG = "VENDOR_DEBUG";
    private ArrayList<String> USERS_NAMES = new ArrayList<String>();
    private ArrayList<String> USERS_IMGS = new ArrayList<String>();
    private ArrayList<String> USERS_IDS = new ArrayList<String>();
    private ArrayList<String> USERS_COMMENTS = new ArrayList<String>();
    private ArrayList<String> USERS_RATINGS = new ArrayList<String>();
    private Button backButton;
    private int reviewsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_vendor_reviews);
        context = this;

        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if(extras != null) {
            vendorID = extras.getString("vendor_id");
            reviewsCount = Integer.parseInt(extras.getString("reviewsCount").substring(0,extras.getString("reviewsCount").indexOf(" ")));
        }

        recyclerView = (RecyclerView) findViewById(R.id.reviewsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ReviewsAdapter(this,ReviewsList));

        usersNameTextView = (TextView) findViewById(R.id.usersNameTextView);
        userCommentTextView = (TextView) findViewById(R.id.userCommentTextView);
        userImageView = (ImageView) findViewById(R.id.userImageView);
        vendorRatingBar = (RatingBar) findViewById(R.id.vendorRatingBar);

        backButton = (Button) findViewById(R.id.returnToVendorButton);

        Log.d(TAG,"Getting User Reviews");
        getUserReviews(getOneCartURL + vendorID);






    }

    private void populateReviewsList(){
        for(int i = 0; i < USERS_IDS.size(); i++){
            Review = new ArrayList<String>();
                Log.d(TAG,"ADDING USER NAME " + USERS_NAMES.get(i) + " TO REVIEW, CURRENTLY INDEX: " + i);
                Review.add(USERS_NAMES.get(i));
                Review.add(USERS_RATINGS.get(i));
                Review.add(USERS_COMMENTS.get(i));
                Review.add(USERS_IMGS.get(i));
            ReviewsList.add(Review);
        }
        recyclerView.setAdapter(new ReviewsAdapter(this,ReviewsList));
    }

    // comment
    private synchronized void getUserReviews(String myURL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray reviewsList = null;
                try {
                    backButton.setText("Return to " + response.getString("cartName"));
                    reviewsList = response.getJSONArray("reviewList");
                    for(int i = 0; i < reviewsList.length(); i++){
                        JSONObject jsonObject = (JSONObject) reviewsList.get(i);
                        Log.d(TAG,"Getting User IDs: " + jsonObject.getString("userId"));
                        USERS_IDS.add(jsonObject.getString("userId"));
                        Log.d(TAG,"Checking User IDs: " + USERS_IDS.get(i));
                        USERS_RATINGS.add(jsonObject.getString("rating"));
                        USERS_COMMENTS.add(jsonObject.getString("comment"));
                    }

                    Log.d(TAG,"Getting User Info");
                    for(int i = 0; i < USERS_IDS.size(); i++){
                        Log.d(TAG,"Checking User IDs Above: " + USERS_IDS.get(i));
                        findUserInfo(getOneUserURL + USERS_IDS.get(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private synchronized void findUserInfo(String myURL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "users name from response: " + response.getString("name"));
                    USERS_NAMES.add(response.getString("name"));
                    USERS_IMGS.add(response.getString("avatar"));
                    Log.d(TAG,"USERS_IDS size: " + USERS_IDS.size());
                    if(USERS_NAMES.size() == reviewsCount){
                        populateReviewsList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
