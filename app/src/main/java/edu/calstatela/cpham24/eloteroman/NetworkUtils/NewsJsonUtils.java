package edu.calstatela.cpham24.eloteroman.NetworkUtils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bill on 6/29/17.
 */

// mirrored from Sunshine example and adapted to NewsAPI's response
public final class NewsJsonUtils {

    public static ArrayList<LocationItem> parseLocationsFromJson(Context context, String locationJsonStr)
            throws JSONException {
        /* Parameters of each location item */
        final String API_LOCATION_ID                 = "_id";
        final String API_LOCATION_CARTID             = "cartId";
        final String API_LOCATION_STATIONARY         = "stationaryLocation";
        final String API_LOCATION_STATIONARY_STREET  = "street";
        final String API_LOCATION_STATIONARY_CITY    = "city";
        final String API_LOCATION_STATIONARY_STATE   = "state";
        final String API_LOCATION_STATIONARY_ZIP     = "zip";
        final String API_LOCATION_CURRENT_MOBILE_LAT = "currentMobileLatitude";
        final String API_LOCATION_CURRENT_MOBILE_LON = "currentMobileLongitude";
        final String API_LOCATION_PREV_MOBILE_LAT    = "previousMobileLatitude";
        final String API_LOCATION_PREV_MOBILE_LON    = "previousMobileLongitude";

        /* String array to hold each day's weather String */
        ArrayList<LocationItem> locationData = new ArrayList<LocationItem>();

        JSONArray locationArray = new JSONArray(locationJsonStr);

        for (int i = 0; i < locationArray.length(); i++) {
            JSONObject location = locationArray.getJSONObject(i);

            String id = location.getString(API_LOCATION_ID);
            String cart_id = location.getString(API_LOCATION_CARTID);
            JSONObject sl = location.getJSONObject(API_LOCATION_STATIONARY);
            String stationary_street = sl.getString(API_LOCATION_STATIONARY_STREET);
            String stationary_city = sl.getString(API_LOCATION_STATIONARY_CITY);
            String stationary_state = sl.getString(API_LOCATION_STATIONARY_STATE);
            String stationary_zip = sl.getString(API_LOCATION_STATIONARY_ZIP);
            LocationItem.StationaryLocation stationary_location = new LocationItem.StationaryLocation(stationary_street, stationary_city, stationary_state, stationary_zip);
            double current_mobile_lat = location.getDouble(API_LOCATION_CURRENT_MOBILE_LAT);
            double current_mobile_lon = location.getDouble(API_LOCATION_CURRENT_MOBILE_LON);
            double prev_mobile_lat = location.getDouble(API_LOCATION_PREV_MOBILE_LAT);
            double prev_mobile_lon = location.getDouble(API_LOCATION_PREV_MOBILE_LON);

            locationData.add(new LocationItem(id, cart_id, stationary_location, current_mobile_lat, current_mobile_lon, prev_mobile_lat, prev_mobile_lon));
        }

        return locationData;
    }
}
