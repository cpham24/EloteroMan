package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
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

import edu.calstatela.cpham24.eloteroman.VendorUtils.Adapter;
import edu.calstatela.cpham24.eloteroman.R;

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
    private String getOneCartURL = "http://162.243.112.34:3000/Eloteroman/getOneCart?id=";
    private String TAG = "DEBUG";

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(this,FoodItemsList));

        ownerName = (TextView) findViewById(R.id.ownerNameTextView);
        cartName = (TextView) findViewById(R.id.cartNameTextView);
        cartImage = (ImageView) findViewById(R.id.cartImageView);
        workingHours = (TextView) findViewById(R.id.workingHoursTextView);
        workingNow = (TextView) findViewById(R.id.workingNowTextView);

        getCartInfo(getOneCartURL + vendorID);


    }

    // comment
    private void getCartInfo(String myURL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray foodItems = null;
                        try {
                            ownerName.setText(response.getString("ownerName"));
                            cartName.setText(response.getString("cartName"));
                            if(!response.getString("picture").equals("NA")){
                                Picasso.with(context).load(response.getString("picture")).into(cartImage);
                            }
                            workingHours.setText("Hours: " + response.getString("hours"));
                            workingNow.setText(response.getString("currentlyInService"));
                            foodItems = response.getJSONArray("foodList");
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
}
