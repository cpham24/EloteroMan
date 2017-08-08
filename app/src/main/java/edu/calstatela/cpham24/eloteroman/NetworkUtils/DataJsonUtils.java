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
public final class DataJsonUtils {

    public static LocationItem parseLocationFromJson(JSONObject location)
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

        String id = location.getString(API_LOCATION_ID);
        String cart_id = location.getString(API_LOCATION_CARTID);
        JSONObject sl = location.getJSONObject(API_LOCATION_STATIONARY);
        String stationary_street = sl.getString(API_LOCATION_STATIONARY_STREET);
        String stationary_city = sl.getString(API_LOCATION_STATIONARY_CITY);
        String stationary_state = sl.getString(API_LOCATION_STATIONARY_STATE);
        String stationary_zip = sl.getString(API_LOCATION_STATIONARY_ZIP);
        LocationItem.StationaryLocation stationary_location = new LocationItem.StationaryLocation(stationary_street, stationary_city, stationary_state, stationary_zip);
        double current_mobile_lat = (location.has(API_LOCATION_CURRENT_MOBILE_LAT) && !location.getString(API_LOCATION_CURRENT_MOBILE_LAT).equals("none")) ? location.getDouble(API_LOCATION_CURRENT_MOBILE_LAT) : 0.0;
        double current_mobile_lon = (location.has(API_LOCATION_CURRENT_MOBILE_LON) && !location.getString(API_LOCATION_CURRENT_MOBILE_LON).equals("none")) ? location.getDouble(API_LOCATION_CURRENT_MOBILE_LON) : 0.0;
        double prev_mobile_lat = (location.has(API_LOCATION_PREV_MOBILE_LAT) && !location.getString(API_LOCATION_PREV_MOBILE_LAT).equals("none")) ? location.getDouble(API_LOCATION_PREV_MOBILE_LAT) : 0.0;
        double prev_mobile_lon = (location.has(API_LOCATION_PREV_MOBILE_LON) && !location.getString(API_LOCATION_PREV_MOBILE_LON).equals("none")) ? location.getDouble(API_LOCATION_PREV_MOBILE_LON) : 0.0;

        return new LocationItem(id, cart_id, stationary_location, current_mobile_lat, current_mobile_lon, prev_mobile_lat, prev_mobile_lon);
    }

    public static VendorLocationItem parseVendorLocationFromJson(JSONObject vendor_location)
            throws JSONException {
        /* Parameters of each location item */
        final String API_VENDOR_LOCATION_TYPE                  = "type";
        final String API_VENDOR_LOCATION_COORDINATES           = "coordinates";

        String type = vendor_location.getString(API_VENDOR_LOCATION_TYPE);
        JSONArray coordinates = vendor_location.getJSONArray(API_VENDOR_LOCATION_COORDINATES);
        double latitude = coordinates.getDouble(1);
        double longitude = coordinates.getDouble(0);

        return new VendorLocationItem(type, latitude, longitude);
    }

    public static FoodItem parseFoodFromJson(JSONObject food)
            throws JSONException{
        /* Parameters of each location item */
        final String API_FOOD_NAME        = "name";
        final String API_FOOD_DESCRIPTION = "description";
        final String API_FOOD_PRICE       = "price";

        String name = food.getString(API_FOOD_NAME);
        String description = food.getString(API_FOOD_DESCRIPTION);
        double price = (food.has(API_FOOD_PRICE) && !food.getString(API_FOOD_PRICE).equals("none")) ? food.getDouble(API_FOOD_PRICE) : 0.0;

        return new FoodItem(name, description, price);
    }

    public static ArrayList<LocationItem> parseLocationsFromJson(String locationJsonStr)
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

        /* ArrayList to hold each location data */
        ArrayList<LocationItem> locationData = new ArrayList<LocationItem>();

        JSONArray locationArray = new JSONArray(locationJsonStr);

        for (int i = 0; i < locationArray.length(); i++) {
            JSONObject location = locationArray.getJSONObject(i);

            locationData.add(parseLocationFromJson(location));
        }

        return locationData;
    }

    public static ArrayList<VendorItem> parseVendorsFromJson(String vendorJsonStr)
            throws JSONException {
        /* Parameters of each location item */
        final String API_VENDOR_ID          = "_id";
        final String API_VENDOR_OWNER_NAME  = "ownerName";
        final String API_VENDOR_CART_NAME   = "cartName";
        final String API_VENDOR_DAYS        = "days";
        final String API_VENDOR_HOURS       = "hours";
        final String API_VENDOR_IN_SERVICE  = "currentlyInService";
        final String API_VENDOR_LOCATION = "location";
        final String API_VENDOR_FOOD_LIST   = "foodList";

        /* ArrayList to hold each vendor data */
        ArrayList<VendorItem> vendorData = new ArrayList<VendorItem>();

        JSONArray vendorArray = new JSONArray(vendorJsonStr);

        for (int i = 0; i < vendorArray.length(); i++) {
            JSONObject vendor = vendorArray.getJSONObject(i);

            String id = vendor.getString(API_VENDOR_ID);
            String owner_name = vendor.getString(API_VENDOR_OWNER_NAME);
            String cart_name = vendor.getString(API_VENDOR_CART_NAME);
            String hours = vendor.getString(API_VENDOR_HOURS);
            String days = vendor.getString(API_VENDOR_DAYS);
            boolean in_service = vendor.getBoolean(API_VENDOR_IN_SERVICE);
            VendorLocationItem location = parseVendorLocationFromJson(vendor.getJSONObject(API_VENDOR_LOCATION));
            JSONArray food_list_json = vendor.getJSONArray(API_VENDOR_FOOD_LIST);
            ArrayList<FoodItem> food_list = new ArrayList<FoodItem>();

            for(int j=0; j < food_list_json.length(); j++) {
                JSONObject food = food_list_json.getJSONObject(j);
                food_list.add(parseFoodFromJson(food));
            }

            vendorData.add(new VendorItem(id, owner_name, cart_name, hours, days, in_service, location, food_list));
        }

        return vendorData;
    }
}
