package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

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

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Welcome to " + cart_name + " run by " + owner_name)
                .setPositiveButton("More Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(callback != null)
                            callback.OnMoreInfoClick(cart_id);
                    }
                })
                .setNegativeButton("Directions", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(callback != null)
                            callback.OnDirectionsClick(cart_id);
                    }
                })
                .setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(callback != null)
                            callback.OnBackClick(cart_id);
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