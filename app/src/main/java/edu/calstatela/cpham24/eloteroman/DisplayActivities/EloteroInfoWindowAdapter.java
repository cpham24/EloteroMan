package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.NetworkUtils.VendorItem;

/**
 * Created by bill on 8/7/17.
 */

public class EloteroInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private ArrayList<VendorItem> vendors;

    public EloteroInfoWindowAdapter(Context context, ArrayList<VendorItem> vendors) {
        this.context = context;
        this.vendors = vendors;
    }

    public void updateVendors(ArrayList<VendorItem> vendors) {
        this.vendors = vendors;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
