package edu.calstatela.cpham24.eloteroman.NetworkUtils;

import android.net.Uri;
import android.util.Log;

import com.android.volley.RequestQueue;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by bill on 6/19/17.
 */

public final class NetworkUtils {
    // tag for logging
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // the url to the news api
    private static final String ELOTERO_API_URL = "http://162.243.112.34:3000/EloteroMan/";

    // api queries
    private static final String ELOTERO_QUERY_LOCATION_ALL = "getLocations";
    private static final String ELOTERO_QUERY_LOCATION_ONE = "getOneLocation";
    private static final String ELOTERO_QUERY_LOCATION_CONDITIONAL = "findLocationWhere";
    private static final String ELOTERO_QUERY_VENDOR_ALL = "getCarts";
    private static final String ELOTERO_QUERY_VENDOR_ONE = "getOneCart";
    private static final String ELOTERO_QUERY_VENDOR_CONDITIONAL = "findCartWhere";

    // parameter names for news api
    final static String PARAM_ID = "_id";

    // helper function to handle errors while building URL for all functions underneath
    private static URL buildUrl(Uri builtUri) {
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to query for all locations from the server
     *
     * @param none
     * @return The URL to use to query the news api server
     */
    public static URL buildLocationUrl() {
        Uri builtUri = Uri.parse(ELOTERO_API_URL + ELOTERO_QUERY_LOCATION_ALL);

        return buildUrl(builtUri);
    }

    /**
     * Builds the URL used to query for a specific location id from server
     *
     * @param none
     * @return The URL to use to query the news api server
     */
    public static URL buildLocationUrl(String id) {
        Uri builtUri = Uri.parse(ELOTERO_API_URL + ELOTERO_QUERY_LOCATION_ONE).buildUpon()
                .appendQueryParameter(PARAM_ID, id)
                .build();

        return buildUrl(builtUri);
    }

    public static URL buildVendorUrl() {
        Uri builtUri = Uri.parse(ELOTERO_API_URL + ELOTERO_QUERY_VENDOR_ALL);

        return buildUrl(builtUri);
    }

    public static URL buildVendorUrl(String id) {
        Uri builtUri = Uri.parse(ELOTERO_API_URL + ELOTERO_QUERY_VENDOR_ONE).buildUpon()
                .appendQueryParameter(PARAM_ID, id)
                .build();

        return buildUrl(builtUri);
    }

    /**
     * Gets data from a URLConnection, taken from CSNS
     *
     * @param url the URL object to connect to
     * @return raw response from the URL in String form
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String res = null;
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext())
                res = scanner.next();
        } finally {
            urlConnection.disconnect();
        }

        return res;
    }
}
