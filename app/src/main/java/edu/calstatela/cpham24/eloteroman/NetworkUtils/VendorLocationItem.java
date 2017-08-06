package edu.calstatela.cpham24.eloteroman.NetworkUtils;

/**
 * Created by bill on 8/5/17.
 */

public class VendorLocationItem {
    public String type;
    public double latitude;
    public double longitude;

    public VendorLocationItem() {
        // empty
    }

    public VendorLocationItem(String type, double latitude, double longitude) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
