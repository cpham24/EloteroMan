package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.ReviewBit;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.Vender;

import static android.content.ContentValues.TAG;

/**
 * Created by Johnson on 8/3/2017.
 */

public class NetworkForRev {

    public static final String ELO_BASE_URL =
            "http://162.243.112.34:3000/EloteroMan/findCartsWhere";


    public static URL makeURL(String own){
        Uri uri;

        uri = Uri.parse(ELO_BASE_URL).buildUpon()
                //.appendQueryParameter("ownerName", own)
                .build();



        Log.d(TAG, "U who meeeeeeeeeeeeeee");

        URL url = null;
        try{
            String urlString= uri.toString();
            Log.d(TAG, "Url: yo " + urlString);
            url = new URL(uri.toString());

        }
        catch(MalformedURLException e) {
            e.printStackTrace();

        }

        return url;
    }

    public static URL makeURL(){
        Uri uri;

        uri = Uri.parse("162.243.112.34:3000/EloteroMan/getReviews").buildUpon()
                .build();



        Log.d(TAG, "U who meeeeeeeeeeeeeee");

        URL url = null;
        try{
            String urlString= uri.toString();
            Log.d(TAG, "Url: yo " + urlString);
            url = new URL(uri.toString());

        }
        catch(MalformedURLException e) {
            e.printStackTrace();

        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }
            else {
                return null;
            }
        }

        finally {
            urlConnection.disconnect();
        }
    }




    public static ArrayList<ReviewBit> parseJSON(String json) throws JSONException {

        ArrayList<ReviewBit> result = new ArrayList<>();
        JSONArray items = new JSONArray(json);
        Log.d(TAG , " oh ma " + json);

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String name = item.getString("review-userId");
            String rate = item.getString("rating(#1-10)");
            String rev = item.getString("review");

            ReviewBit repo = new ReviewBit(name, rate, rev);
            result.add(repo);
        }


        return result;
    }


    public static Vender parseJSONfor(String json) throws JSONException {

        JSONArray items = new JSONArray(json);
        Log.d(TAG , " oh ma " + json);

        JSONObject item = items.getJSONObject(0);
        String Vname = item.getString("ownerName");
        String Cname = item.getString("cartName");
        String days = item.getString("days");
        String hours = item.getString("hours");
        String work = item.getString("currentlyInService");

        Vender repo = new Vender(Vname, Cname, days, hours, work);



        return repo;
    }

}
