package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.R;
import edu.calstatela.cpham24.eloteroman.VendorUtils.Adapter;

public class DisplayVendorActivity extends AppCompatActivity {

    private String[] temp = {"ITEM ONE", "ITEM TWO", "ITEM THREE", "ITEM FOUR", "ITEM FIVE"};
    private ArrayList<ArrayList<String>> FoodItemsList = new ArrayList<ArrayList<String>>();
    private ArrayList<String> foodItem = new ArrayList<String>();

    private RecyclerView recyclerView;

    private Context context;

    private RequestQueue requestQueue;
    private String vendorID;
    private TextView ownerName;
    private TextView cartName;
    private TextView workingHours;
    private TextView workingNow;
    private ImageView cartImage;
    private RatingBar ratingBar;
    private RatingBar leaveARatingBar;
    private TextView ratingCount;
    private String getOneCartURL = "http://162.243.112.34:3000/Eloteroman/getOneCart?id=";
    private String TAG = "DEBUG";
    private String ownerNameStr;
    private String cartNameStr;
    private String workingHoursStr;
    private String workingNowStr;
    private String cartImageStr;

    //TODO add a Call Button
    //TODO add a Navigation button with the below code
//    Uri gmmIntentUri = Uri.parse(“google.navigation:q=” + vendor_inner.location.latitude + “,” + vendor_inner.location.longitude + “&mode=w”);
//    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//    mapIntent.setPackage(“com.google.android.apps.maps”);
//    startActivity(mapIntent);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_vendor);
        context = this;

        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if(extras != null) {
            vendorID = extras.getString("vendor_id");
        }

        recyclerView = (RecyclerView) findViewById(R.id.foodListRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new Adapter(this,FoodItemsList));

        ownerName = (TextView) findViewById(R.id.ownerNameTextView);
        cartName = (TextView) findViewById(R.id.cartNameTextView);
        cartImage = (ImageView) findViewById(R.id.cartImageView);
        workingHours = (TextView) findViewById(R.id.workingHoursTextView);
        workingNow = (TextView) findViewById(R.id.workingNowTextView);
        ratingBar = (RatingBar) findViewById(R.id.vendorRatingBar);
        leaveARatingBar = (RatingBar) findViewById(R.id.ratingBarForLeavingARating);
        ratingCount = (TextView) findViewById(R.id.howManyReviewsTextView);

        getCartInfo(getOneCartURL + vendorID);

        // TODO Do not allow review submission if submitted already
        // TODO Find user review if exists and update user rating when vendor page loads
        leaveARatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent i = new Intent(context, DisplayReviewActivity.class);
                i.putExtra("rating", rating);
                i.putExtra("cartName", cartImageStr);
                i.putExtra("vendor_id", vendorID);
                startActivity(i);
            }
        });


    }

    public void goToReviews(View view){
        Intent i = new Intent(context, DisplayVendorReviewsActivity.class);
        i.putExtra("vendor_id", vendorID);
        i.putExtra("reviewsCount", ratingCount.getText());
        startActivity(i);
    }

    // comment
    private void getCartInfo(String myURL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray foodItems = null;
                        ArrayList<String> vendorData = new ArrayList<String>();
                        try {
                            ownerName.setText(response.getString("ownerName"));
                                vendorData.add(response.getString("ownerName")); // 0
                            cartName.setText(response.getString("cartName"));
                                vendorData.add(response.getString("cartName")); // 1
                            cartImageStr = response.getString("cartName");
                            if(!response.getString("picture").equals("NA")){
                                Picasso.with(context).load(response.getString("picture")).into(cartImage);
                                    vendorData.add(response.getString("picture")); // 2
                            }
                            workingHours.setText("Hours: " + response.getString("hours"));
                                vendorData.add("Hours: " + response.getString("hours")); // 3
                            setWorkingNow(response.getString("currentlyInService"));
                                vendorData.add(response.getString("currentlyInService")); // 4
                            String temp = setRating(response.getJSONArray("reviewList"));
                            ratingCount.setText(temp);
                                vendorData.add(getRating(response.getJSONArray("reviewList"),vendorData)); // 5, 6
                            foodItems = response.getJSONArray("foodList");
                            FoodItemsList.add(vendorData);
                            for(int i = 0; i < foodItems.length(); i++){
                                JSONObject jsonObject = (JSONObject) foodItems.get(i);
                                foodItem = new ArrayList<String>();
                                for(int j = 0; j < jsonObject.length(); j++){
                                    foodItem.add(jsonObject.getString("name"));
                                    foodItem.add(jsonObject.getString("price"));
                                    foodItem.add(jsonObject.getString("description"));
                                    foodItem.add(jsonObject.getString("foodImage"));
                                }
                                FoodItemsList.add(foodItem);
                                Log.d(TAG, jsonObject.getString("name"));
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

    public void setWorkingNow(String input){
        workingNow.setText(input);
        if(input.contains("false")){
            workingNow.setText("Not Available");
            workingNow.setTextColor(Color.RED);
        }else if(input.contains("true")){
            workingNow.setText("Available");
            workingNow.setTextColor(Color.GREEN);
        }
    }

    public String setRating (JSONArray reviewsList) throws JSONException {
        float rating = 0;
        float counter = 0;
        if(reviewsList.length() == 0){
            ratingBar.setRating(0);
            return String.valueOf((int)0) + " Review";
        }
        for(int i = 0; i < reviewsList.length(); i++){
            JSONObject review = (JSONObject) reviewsList.get(i);
            rating += Float.valueOf(review.getString("rating"));
            counter++;
        }
        ratingBar.setRating(rating/counter);
        if((int)counter > 1){
            return String.valueOf((int)counter + " Reviews");
        }else{
            return String.valueOf((int)counter + " Review");
        }
    }

    public String getRating (JSONArray reviewsList, ArrayList<String> arrayList) throws JSONException {
        float rating = 0;
        float counter = 0;
        if(reviewsList.length() == 0){
            arrayList.add("0");
            return String.valueOf((int)0) + " Review";
        }
        for(int i = 0; i < reviewsList.length(); i++){
            JSONObject review = (JSONObject) reviewsList.get(i);
            rating += Float.valueOf(review.getString("rating"));
            counter++;
        }
        arrayList.add(String.valueOf(rating/counter));
        if((int)counter > 1){
            return String.valueOf((int)counter + " Reviews");
        }else{
            return String.valueOf((int)counter + " Review");
        }
    }
}
