package edu.calstatela.cpham24.eloteroman.DisplayActivities.SearchPageUtils;

import android.net.Uri;
import android.os.Bundle;
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

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.Vender;

import static android.content.ContentValues.TAG;

/**
 * Created by Johnson on 7/30/2017.
 */

public class NetworkUtils {


    public static final String ELO_BASE_URL =
            "http://162.243.112.34:3000/EloteroMan/";

    public static final String getCarts = "getCarts";
    public static final String getReviews = "findReviewsWhere";

    final static String API_LOCATION_ID = "_id";

    public static final String PARAM_REV = "cartId";


    public static URL makeURL(){
        Uri uri;

        uri = Uri.parse(ELO_BASE_URL + getCarts).buildUpon()
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


    public static URL makeURLrev(String cart){
        Uri uri;

        uri = Uri.parse(ELO_BASE_URL + getCarts).buildUpon()
                .build();



        Log.d(TAG, "U who meeeeeeeeeeeeeee");

        URL url = null;
        try{
            String urlString= uri.toString();
            Log.d(TAG, "Url: ffgo " + urlString);
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




    public static ArrayList<Vender> parseJSON(String json, String check, String sorted, double longitude, double latitude) throws JSONException {

        ArrayList<Vender> result = new ArrayList<>();
        JSONArray items = new JSONArray(json);
        Log.d(TAG , " oh ma " + json);

        Log.d(TAG , "is it huh? " + check);

        for (int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String rot = item.getString(API_LOCATION_ID);
            Log.d(TAG , " find the id: " + rot);
            String picture = item.getString("picture");
            String Oname = item.getString("ownerName");
            String name = item.getString("cartName");
            String stree = item.getString("street");
            String cit = item.getString("city");
            String desc = item.getString("days");
            String WoH = item.getString("hours");
            String WoY = item.getString("currentlyInService");
            JSONArray foods = item.getJSONArray("foodList");
            JSONArray revs = item.getJSONArray("reviewList");
            JSONObject loc = item.getJSONObject("location");
            JSONArray fair = loc.getJSONArray("coordinates");
            String[] there = new String[2];



            for (int g = 0; g < there.length; ++g) {
                there[g] = fair.getString(g);
                Log.d(TAG, " let see " + there[g]);
            }

            double lat = 0.0;
            double lon = 0.0;

            if (there[0] != null && there[0].length() > 0 && !there[0].contains("null")) {
                Log.d(TAG, " you aint trying " + there[0] + there[0].length());
                lat = Double.parseDouble(there[0]);
            }

            else {
                continue;
            }

            if (there[1] != null && there[1].length() > 0 && !there[1].contains("null")) {
                lon = Double.parseDouble(there[1]);
            }

            else {
                continue;
            }

            Log.d(TAG , " come " + Oname + " + " + check);

            if (check != null) {
                Log.d(TAG , " come1 " + i);
                if (name.toLowerCase().contains(check.toLowerCase()) || desc.toLowerCase().contains(check.toLowerCase()) ||
                        WoH.toLowerCase().contains(check.toLowerCase()) || WoY.toLowerCase().contains(check.toLowerCase()) ||
                        Oname.toLowerCase().contains(check.toLowerCase()) || stree.toLowerCase().contains(check.toLowerCase())){
                    Log.d(TAG , " name here " + name);
                    Vender repo = new Vender(name, desc, WoH, WoY, lat, lon, latitude, longitude,
                            sorted, rot, picture, stree, cit);
                    result.add(repo);
                    for (int k = 0; k < revs.length(); ++k) {
                        JSONObject r = revs.getJSONObject(k);
                        double rate = Double.parseDouble(r.getString("rating"));
                        repo.setRateMe(rate);
                    }
                    Log.d(TAG, " where8 " + result);
                }

                else {
                    for (int y = 0; y < foods.length(); y++) {
                        Log.d(TAG, " where14 " + result);
                        JSONObject foodStuff = foods.getJSONObject(y);
                        Log.d(TAG, " where15 " + foodStuff);
                        String foodName = foodStuff.getString("name");

                        if (foodName.toLowerCase().contains(check.toLowerCase())) {
                            Vender repo = new Vender(name, desc, WoH, WoY, lat, lon, latitude, longitude,
                                    sorted, rot, picture, stree, cit);
                            result.add(repo);
                            Log.d(TAG, " where9 " + result);
                            for (int k = 0; k < revs.length(); ++k) {
                                JSONObject r = revs.getJSONObject(k);
                                double rate = Double.parseDouble(r.getString("rating"));
                                repo.setRateMe(rate);
                            }
                            break;
                        }
                    }
                }

                Log.d(TAG, " where10 " + result + " so ");

            }

            else {
                Vender repo = new Vender(name, desc, WoH, WoY, lat, lon, latitude, longitude,
                        sorted, rot, picture, stree, cit);
                result.add(repo);

                for (int k = 0; k < revs.length(); ++k) {
                    JSONObject r = revs.getJSONObject(k);
                    double rate = Double.parseDouble(r.getString("rating"));
                    repo.setRateMe(rate);
                }
            }

            Log.d(TAG, " where11 " + result + " lllo ");
        }

        Log.d(TAG, " where6 " + result);

        return result;
    }


    public static ArrayList<Vender> parseJSON(String json, Bundle check, double longitutde, double latitude) throws JSONException {
        ArrayList<Vender> result = new ArrayList<>();

        JSONArray items = new JSONArray(json);
        Log.d(TAG , " oh ma " + json);

        Log.d(TAG , "is it huh? " + check);
        String sorted = check.getString("sorted");

        for (int i = 0; i < items.length(); ++i) {

            JSONObject item = items.getJSONObject(i);
            String rot = item.getString(API_LOCATION_ID);
            String picture = item.getString("picture");
            String Oname = item.getString("ownerName");
            String name = item.getString("cartName");
            String desc = item.getString("days");
            String stree = item.getString("street");
            String cit = item.getString("city");
            String WoH = item.getString("hours");
            String WoY = item.getString("currentlyInService");
            JSONArray foods = item.getJSONArray("foodList");
            JSONArray revs = item.getJSONArray("reviewList");
            JSONObject loc = item.getJSONObject("location");
            JSONArray fair = loc.getJSONArray("coordinates");
            String[] there = new String[2];

            Log.d(TAG , " come " + Oname + " + " + check.getString("ownerName"));

            for (int g = 0; g < there.length; ++g) {
                there[g] = fair.getString(g);
            }

            double lat = 0;
            double lon = 0;

            if (there[0] != null && there[0].length() > 0 && !there[0].contains("null")) {
                lat = Double.parseDouble(there[0]);
            }

            else {
                continue;
            }

            if (there[1] != null && there[1].length() > 0 && !there[1].contains("null")) {
                lon = Double.parseDouble(there[1]);
            }

            else {
                continue;
            }



            if(check.getString("ownerName") != null) {
                if (!(Oname.toLowerCase().contains(check.getString("ownerName").toLowerCase()))) {
                    Log.d(TAG , " where0 " + Oname);
                    continue;
                }
            }

            if(check.getString("cartName") != null) {
                if (!(name.toLowerCase().contains(check.getString("cartName").toLowerCase()))) {
                    Log.d(TAG , " where0 " + Oname + " cart ");
                    continue;
                }
            }

            if(check.getString("street") != null) {
                if (!(stree.toLowerCase().contains(check.getString("street").toLowerCase()))) {
                    Log.d(TAG , " where0 " + stree + " address ");
                    continue;
                }
            }

            if(check.getString("theDay") != null && !(desc.toLowerCase().contains("daily")) &&
                    !(desc.toLowerCase().contains("nightly"))
                    ) {
                if (!(desc.toLowerCase().contains(check.getString("theDay").toLowerCase()))){
                    Log.d(TAG , " where0 " + Oname + " day ");
                    continue;
                }

                else if (desc.toLowerCase().contains("weekdays")) {
                    if (!(check.getString("theDay").toLowerCase().contains("monday")) ||
                            !(check.getString("theDay").toLowerCase().contains("tuesday")) ||
                            !(check.getString("theDay").toLowerCase().contains("wednesday")) ||
                            !(check.getString("theDay").toLowerCase().contains("thursday")) ||
                            !(check.getString("theDay").toLowerCase().contains("friday"))
                            ) {
                        continue;
                    }

                }

                else if (desc.toLowerCase().contains("weekends")) {
                    if (!(check.getString("theDay").toLowerCase().contains("saturday")) ||
                            !(check.getString("theDay").toLowerCase().contains("sunday"))
                            ) {
                        continue;
                    }
                }


            }

            if(check.getString("leftTime") != null && check.getString("leftDayTime") != null) {
                if (!(WoH.substring(0,5).toLowerCase().contains(check.getString("leftTime").toLowerCase() +
                        check.getString("leftDayTime")))) {
                    Log.d(TAG , " where0 " + Oname + " time ");
                    continue;
                }
            }

            if(check.getString("rightTime") != null && check.getString("rightDayTime") != null) {
                if (!(WoH.substring(4,WoH.length() - 1).toLowerCase().contains(check.getString("rightTime").toLowerCase() +
                        check.getString("rightDayTime")))) {
                    Log.d(TAG , " where0 " + Oname + " time ");
                    continue;
                }
            }

            if(check.getString("foodName") != null) {
                Log.d(TAG , " where0 " + Oname + " food ");
                boolean hasOne = false;
                for (int y = 0; y < foods.length(); ++y) {
                    JSONObject foodStuff = foods.getJSONObject(y);
                    String foodName = foodStuff.getString("name");

                    if (foodName.toLowerCase().contains(check.getString("foodName").toLowerCase())) {
                        hasOne = true;
                    }

                    if (hasOne == true) {
                        break;
                    }
                }

                if (hasOne == false) {
                    continue;
                }

            }


            Vender repo = new Vender(name, desc, WoH, WoY, lat, lon, latitude, longitutde,
                    sorted, rot, picture, stree, cit);
            result.add(repo);

            for (int k = 0; k < revs.length(); ++k) {
                Log.d(TAG , " oh ma rev " + json);
                JSONObject r = revs.getJSONObject(k);
                double rate = Double.parseDouble(r.getString("rating"));
                repo.setRateMe(rate);
            }


        }



        return result;

    }


    public static void parseJSONfor(String json, Vender change) throws JSONException {

        JSONArray items = new JSONArray(json);
        Log.d(TAG , " oh ma rev " + json);

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            JSONArray revs = item.getJSONArray("reviewList");

            for (int k = 0; k < revs.length(); ++k) {
                JSONObject r = revs.getJSONObject(k);
                double rate = Double.parseDouble(r.getString("rating"));
                change.setRateMe(rate);
            }



        }

    }



}
