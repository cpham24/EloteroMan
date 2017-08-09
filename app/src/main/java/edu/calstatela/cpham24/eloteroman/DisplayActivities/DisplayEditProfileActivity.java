package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.utilities.JNetworkUtils;
import edu.calstatela.cpham24.eloteroman.R;

/**
 * Created by Ambrosio on 8/9/2017.
 */

public class DisplayEditProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {
    String id;
    String name;
    String username;
    String isPublic;

    String TAG="EDIT PROFILE";
    Bundle bundleForLoader;

    EditText nameET;
    EditText usernameET;
    Switch isPublicS;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_edit_profile);

        id = getIntent().getExtras().getString("user_id");
        name = getIntent().getExtras().getString("user_name");
        username = getIntent().getExtras().getString("user_username");
        isPublic = getIntent().getExtras().getString("user_isPublic");

        nameET = (EditText) findViewById(R.id.name_edit_ET);
        usernameET = (EditText) findViewById(R.id.username_edit_ET);
        isPublicS = (Switch) findViewById(R.id.profile_public_S);
        updateBtn = (Button) findViewById(R.id.update_Btn);

        nameET.setText(name);
        usernameET.setText(username);
        if(isPublic.equals("True")||isPublic.equals("true")){
            isPublicS.setChecked(true);
        }else{
            isPublicS.setChecked(false);
        }

        updateBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Log.d(TAG, "switch result: " + isPublicS.isChecked());
                name=nameET.getText().toString();
                username=usernameET.getText().toString();
                if(isPublicS.isChecked()){
                    isPublic="true";
                }else{
                    isPublic="false";
                }

                load();

            }
        });


        }

    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(0, bundleForLoader, this).forceLoad();

    }


    @Override
    public Loader<Void> onCreateLoader(int loader_id, Bundle args) {

        return new AsyncTaskLoader<Void>(this) {
            @Override
            public Void loadInBackground() {
                URL url= JNetworkUtils.buildUrlModifyUser(id,name, username, isPublic);
                Log.v(TAG, "load username: " + username);

                try {

                    JNetworkUtils.getResponseFromHttpUrl(url);



                } catch (IOException e) {
                    e.printStackTrace();

                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        Intent i = new Intent(this, DisplayProfileActivity.class);
        i.putExtra("username", name);
        startActivity(i);

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }
}
