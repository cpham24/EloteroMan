package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.NetworkUtils.VendorItem;
import edu.calstatela.cpham24.eloteroman.*;


/**
 * Created by bill on 8/7/17.
 */

public class EloteroInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private ArrayList<VendorItem> vendors;
    private View mWindow;

    public EloteroInfoWindowAdapter(Context context, ArrayList<VendorItem> vendors) {
        this.context = context;
        this.vendors = vendors;
        mWindow = LayoutInflater.from(context).inflate(R.layout.elotero_info_window, null);
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
        // empty since window takes over
        render(marker, mWindow);
        return mWindow;
    }

    private void render(Marker marker, View view) {
        /*
        int badge;
        // Use the equals() method on a Marker to check for equals.  Do not use ==.
        if (marker.equals(mBrisbane)) {
            badge = R.drawable.badge_qld;
        } else if (marker.equals(mAdelaide)) {
            badge = R.drawable.badge_sa;
        } else if (marker.equals(mSydney)) {
            badge = R.drawable.badge_nsw;
        } else if (marker.equals(mMelbourne)) {
            badge = R.drawable.badge_victoria;
        } else if (marker.equals(mPerth)) {
            badge = R.drawable.badge_wa;
        } else {
            // Passing 0 to setImageResource will clear the image view.
            badge = 0;
        }
        */
        VendorItem vendor = null;

        for(int i=0; i<vendors.size(); i++) { // find a vendor item based on position
            LatLng pos = marker.getPosition();
            VendorItem v = vendors.get(i);
            if(v.location.latitude == pos.latitude && v.location.longitude == pos.longitude)
                vendor = v;
        }

        ImageView infoImage = (ImageView) view.findViewById(R.id.infoImage);
        if(vendor.img != null)
            infoImage.setImageBitmap(vendor.img);

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.infoTitle));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.colorAccent)), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.infoSnippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, snippet.length(), 0);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }
}
