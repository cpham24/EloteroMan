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

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.favoriteCart;
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
    private static final String USERNAME_PARAM="name";


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

    //example http://162.243.112.34:3000/Eloteroman/getOneCart?id=5983e79f0df161632aa3ad62
    public static URL buildUrlGetOneCart(String id){

        Uri builtUri=
                Uri.parse(ELOTEROMAN_BASE_URL+"/"+"getOneCart").buildUpon()
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

    //example http://162.243.112.34:3000/Eloteroman/findUsersWhere?username=Bear
    public static URL buildUrlGetOneUserWithUsername(String username){

        Uri builtUri=
                Uri.parse(ELOTEROMAN_BASE_URL+"/"+"findUsersWhere").buildUpon()
                        .appendQueryParameter(USERNAME_PARAM,username)
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

        try{

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);

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

        user result;
        String FoodCartId="";
        ArrayList<String> favoriteFoodCartIds = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("favoriteFoodCarts");

        String id=main.getString("_id");
        String username = main.getString("username");
        String name = main.getString("name");
        String avatar = main.getString("avatar");
        String isPublic = main.getString("public");



        for(int i = 0; i < items.length(); i++){
            FoodCartId= items.getString(i);
            favoriteFoodCartIds.add(FoodCartId);
            Log.d("Comments",FoodCartId );

        }

        result= new user(id,username,name,avatar,isPublic,favoriteFoodCartIds);
        Log.d("Comments", result.getUsername());

        return result;
    }

    public static user parseLoginUserJSON(String json) throws JSONException {

        user result;
        String FoodCartId="";
        ArrayList<String> favoriteFoodCartIds = new ArrayList<>();
        JSONArray mainArr=new JSONArray(json);
        JSONObject main = mainArr.getJSONObject(0);
        JSONArray items = main.getJSONArray("favoriteFoodCarts");

        String id=main.getString("_id");
        String username = main.getString("username");
        String name = main.getString("name");
        String avatar = main.getString("avatar");
        String isPublic = main.getString("public");



        for(int i = 0; i < items.length(); i++){
            FoodCartId= items.getString(i);
            favoriteFoodCartIds.add(FoodCartId);
            Log.d("Comments",FoodCartId );

        }

        result= new user(id,username,name,avatar,isPublic,favoriteFoodCartIds);
        Log.d("Comments", result.getUsername());

        return result;
    }

    public static favoriteCart parseFavoriteCartJSON(String json) throws JSONException {

        favoriteCart result;

        JSONObject main = new JSONObject(json);



        String id = main.getString("_id");
        String name = main.getString("cartName");
        String address = main.getString("street");
        String time = main.getString("hours");
        String url = main.getString("picture");
        String inService = main.getString("currentlyInService");




        result= new favoriteCart(id,name,address,time,url,inService);
        Log.d("Comments", result.getId());

        return result;
    }


}
