package edu.calstatela.cpham24.eloteroman.DisplayActivities.utilities;

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

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.location;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.user;

import static android.content.ContentValues.TAG;

/**
 * Created by Ambrosio on 7/31/2017.
 */

public class JNetworkUtils {
    private static final String TAG = JNetworkUtils.class.getSimpleName();

    private static final String ELOTEROMAN_BASE_URL =
            "http://162.243.112.34:3000/Eloteroman";



    private static final String COLLECTION_USER="user";
    private static final String COLLECTION_REVIEW="review";
    private static final String COLLECTION_LOCATION="location";
    private static final String COLLECTION_CART="cart";
    private static final String COLLECTION_FOOD="food";

    private static final String ID_PARAM="id";


    public static URL buildUrlCollectionFieldLike(String collection, String field, String like){
        Uri builtUri=
                Uri.parse(ELOTEROMAN_BASE_URL+"/"+collection).buildUpon()
                        .appendQueryParameter(field,like)
                        .build();
        URL url=null;

        try{
            url=new URL (builtUri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built Uri: " + url);
        return url;
    }

    public static URL buildUrlGetOneUser(String id){
        Log.d("Comments", "_______________________buildUrl______________________");
        Uri builtUri=
                Uri.parse(ELOTEROMAN_BASE_URL+"/"+"getOneUser").buildUpon()
                        .appendQueryParameter(ID_PARAM,id)
                        .build();
        URL url=null;

        try{
            url=new URL (builtUri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built Uri: " + url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Log.d("Comments", "_______________________getResponse______________________");
        try{
            Log.d("Comments", "_______________________IsItHere1?______________________");
            InputStream in = urlConnection.getInputStream();
            Log.d("Comments", "_______________________IsItHere2?______________________");
            Scanner scanner = new Scanner(in);
            Log.d("Comments", "_______________________IsItHere3?______________________");
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else {
                return null;
            }
        } finally{
            urlConnection.disconnect();
        }
    }

    public static user parseUserJSON(String json) throws JSONException {
        Log.d("Comments", "_______________________parseUserJason______________________");
        user result;
        String FoodCartId="";
        ArrayList<String> favoriteFoodCartIds = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("favoriteFoodCarts");

        String username = main.getString("username");
        String name = main.getString("name");
        String avatar = main.getString("avatar");
        String isPublic = main.getString("public");


        for(int i = 0; i < items.length(); i++){
            FoodCartId= items.getString(i);
            favoriteFoodCartIds.add(FoodCartId);
            Log.d("Comments",FoodCartId );

        }

        result= new user(username,name,avatar,isPublic,favoriteFoodCartIds);
        Log.d("Comments", result.getUsername());

        return result;
    }

    public static ArrayList<location> parseLocationJSON(String json) throws JSONException {
        ArrayList<location> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("articles");


        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String locationId = item.getString("locationId");
            String cartId = item.getString("cartId");
            String street = item.getString("street");
            String city = item.getString("city");
            String zip = item.getString("zip");
            Double currentLatitude= Double.valueOf(item.getString("currentLatitude"));
            Double currentLongitude= Double.valueOf(item.getString("currentLongitude"));
            Double previousLatitude= Double.valueOf(item.getString("previousLatitude"));
            Double previousLongitude= Double.valueOf(item.getString("previousLongitude"));
            location Loc = new location( locationId,  cartId,  street,  city,  zip,  currentLatitude,currentLongitude,previousLatitude,previousLongitude);

            result.add(Loc);
        }

        return result;
    }


}
