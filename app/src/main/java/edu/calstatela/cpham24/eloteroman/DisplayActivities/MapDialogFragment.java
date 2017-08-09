package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.calstatela.cpham24.eloteroman.R;

/**
 * Created by bill on 8/8/17.
 */

public class MapDialogFragment extends DialogFragment {
    private String id;
    private String cart_name;
    private String owner_name;
    private MapDialogCallback callback;

    public void setCallback(MapDialogCallback callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String cart_id = this.getArguments().getString("id");
        String cart_name = this.getArguments().getString("cart_name");
        String owner_name = this.getArguments().getString("owner_name");
        String days = this.getArguments().getString("days");
        String hours = this.getArguments().getString("hours");
        byte[] buffer = this.getArguments().getByteArray("img");
        boolean working = this.getArguments().getBoolean("working");

        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.alert_dialog_view, null);

        ImageView iv = (ImageView)view.findViewById(R.id.alert_img);
        iv.setImageBitmap(BitmapFactory.decodeByteArray(buffer, 0, buffer.length));

        TextView title = (TextView)view.findViewById(R.id.alert_title);
        TextView snippet = (TextView)view.findViewById(R.id.alert_snippet);

        SpannableString titleText = new SpannableString(cart_name);
        titleText.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.colorAccent)), 0, titleText.length(), 0);
        title.setText(titleText);

        String work = "Active";
        int workColor = Color.parseColor("#00af00");
        if(!working) {
            work = "Inactive";
            workColor = Color.parseColor("#af0000");
        }

        SpannableString snippetText = new SpannableString(owner_name + "\n" + days + ", " + hours + "\n" + work);
        snippetText.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, snippetText.length() - work.length(), 0);
        snippetText.setSpan(new ForegroundColorSpan(workColor), snippetText.length() - work.length(), snippetText.length(), 0);
        snippet.setText(snippetText);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(callback != null)
                            callback.OnBackClick(cart_id);
                    }
                })
                .setNegativeButton("Directions", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(callback != null)
                            callback.OnDirectionsClick(cart_id);
                    }
                })
                .setNeutralButton("More Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(callback != null)
                            callback.OnMoreInfoClick(cart_id);
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface MapDialogCallback {
        public void OnMoreInfoClick(String id);
        public void OnDirectionsClick(String id);
        public void OnBackClick(String id);
    }
}