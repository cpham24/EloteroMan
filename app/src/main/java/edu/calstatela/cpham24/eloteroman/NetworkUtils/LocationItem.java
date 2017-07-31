package edu.calstatela.cpham24.eloteroman.NetworkUtils;

/**
 * Created by bill on 7/30/17.
 */

public class LocationItem {
    public String id;
    public String cart_id;
    public StationaryLocation stationary_location;
    public double current_mobile_lat;
    public double current_mobile_lon;
    public double prev_mobile_lat;
    public double prev_mobile_lon;

    public LocationItem() {
        // empty constructor
    }

    public LocationItem(String id, String cart_id, StationaryLocation stationary_location, double current_mobile_lat, double current_mobile_lon, double prev_mobile_lat, double prev_mobile_lon) {
        this.id = id;
        this.cart_id = cart_id;
        this.stationary_location = stationary_location;
        this.current_mobile_lat = current_mobile_lat;
        this.current_mobile_lon = current_mobile_lon;
        this.prev_mobile_lat = prev_mobile_lat;
        this.prev_mobile_lon = prev_mobile_lon;
    }

    static class StationaryLocation {
        public String street;
        public String city;
        public String state;
        public String zip;

        public StationaryLocation() {
            // empty constructor
        }

        public StationaryLocation(String street, String city, String state, String zip) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zip = zip;
        }
    }
}
