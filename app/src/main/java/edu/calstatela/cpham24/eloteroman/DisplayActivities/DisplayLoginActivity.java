package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.user;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.utilities.JNetworkUtils;
import edu.calstatela.cpham24.eloteroman.R;

public class DisplayLoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>{

    private final String TAG="loginActivity";
    Button loginBtn;
    EditText usernameET;
    String username;
    Bundle bundleForLoader;
    user current_user;
    private SharedPreferences userPrefs;
    String USER_PREFS="user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_login);

        loginBtn=(Button)findViewById(R.id.login_button);
        usernameET=(EditText) findViewById(R.id.usernameET);

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                username=usernameET.getText().toString();
                load();
            }
        });
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(this) {
            @Override
            public Void loadInBackground() {
                URL url= JNetworkUtils.buildUrlGetOneUserWithUsername(username);


                try {

                    String json = JNetworkUtils.getResponseFromHttpUrl(url);
                    if(json!=null&&json.length()>5)
                        current_user = JNetworkUtils.parseLoginUserJSON(json);

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        if(current_user!=null){
        Toast.makeText(this,"success id: "+current_user.getId(),Toast.LENGTH_SHORT).show();
            userPrefs = getSharedPreferences(USER_PREFS, 0);
            SharedPreferences.Editor edit=userPrefs.edit();
            edit.putString("id",current_user.getId());
            edit.putBoolean("isLoggedIn",true);
            edit.commit();
            Intent i = new Intent(this, DisplayProfileActivity.class);
            startActivity(i);



        }
        else{
            Toast.makeText(this,"I pity the fool",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(0, bundleForLoader, this).forceLoad();

    }
}
