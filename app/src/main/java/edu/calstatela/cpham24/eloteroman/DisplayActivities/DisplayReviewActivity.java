package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.R;

/**
 * Created by Dezval on 8/7/2017.
 */

public class DisplayReviewActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Context context;
    private float rating;
    private RatingBar ratingBar;
    private TextView cartName;
    private EditText vendorReview;
    private Button submitReview;
    private String cartNameStr;
    private String vendorID;
    private String URL = "/addReviewtoCart" +
            "/59839fc527d84b73931b8891" +
            "?userId=5966d6d0dfefa30520fd222c " +
            "&rating=4.5" +
            "&comment=You got to try the churros with caramel!";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_a_rating);
        context = this;

        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if(extras != null) {
            rating = extras.getFloat("rating");
            cartNameStr = extras.getString("cartName");
            vendorID = extras.getString("vendor_id");
        }

        ratingBar = (RatingBar) findViewById(R.id.ratingBarVendorReviewPage);
        ratingBar.setRating(rating);
        cartName = (TextView) findViewById(R.id.vendorNameRatingPage);
        cartName.setText(cartNameStr);
        vendorReview = (EditText) findViewById(R.id.vendorReviewEditText);
        submitReview = (Button) findViewById(R.id.submitReviewButton);

        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "";
                URL = buildAddCartURL(String.valueOf(ratingBar.getRating()),vendorReview.getText().toString());
                sendReview(URL);
            }
        });

    }

    public String buildAddCartURL(String rating, String comment){
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http")
                .encodedAuthority("162.243.112.34:3000")
                .appendPath("Eloteroman")
                .appendPath("addReviewtoCart")
                .appendPath(vendorID)
                .appendQueryParameter("userId","5966d6d0dfefa30520fd222c ")
                .appendQueryParameter("rating",rating)
                .appendQueryParameter("comment", comment);
        return uriBuilder.build().toString();
    }

    private void sendReview(String myURL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray foodItems = null;
                ArrayList<String> vendorData = new ArrayList<String>();
//                try {
//                    Toast.makeText(DisplayReviewActivity.this, response.getString("ok"), Toast.LENGTH_LONG).show();
//                    if(response.getString("ok").equals(1)){
                        Toast.makeText(DisplayReviewActivity.this, "Review Submitted", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, DisplayVendorActivity.class);
                        i.putExtra("vendor_id",vendorID);
                        startActivity(i);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
