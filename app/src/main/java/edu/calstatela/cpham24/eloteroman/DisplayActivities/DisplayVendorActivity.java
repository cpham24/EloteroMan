package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import edu.calstatela.cpham24.eloteroman.R;

public class DisplayVendorActivity extends AppCompatActivity {

    private Context context;

    private RequestQueue requestQueue;
    private String vendorID;
    private TextView ownerName;
    private TextView cartName;
    private ImageView cartImage;
    private String getOneCartURL = "http://162.243.112.34:3000/Eloteroman/getOneCart?id=";

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

        ownerName = (TextView) findViewById(R.id.ownerNameTextView);
        cartName = (TextView) findViewById(R.id.cartNameTextView);
        cartImage = (ImageView) findViewById(R.id.cartImageView);

        getCartInfo(getOneCartURL + vendorID);


    }

    // comment
    private void getCartInfo(String myURL){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ownerName.setText(response.getString("ownerName"));
                            cartName.setText(response.getString("cartName"));
                            Picasso.with(context).load(response.getString("picture")).into(cartImage);
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
