package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.user;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.utilities.JNetworkUtils;
import edu.calstatela.cpham24.eloteroman.R;

public class DisplayProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {

    TextView usernameTV;
    TextView nameTV;
    TextView isPublicTV;
    TextView foodCartsTV;
    ProgressBar mProgress;
    private SharedPreferences userPrefs;
    final String id = "596d335cb158f84ac6fb474d";
    String USER_PREFS="user";
    String user_id;
    user current_user;
    int USER_LOADER_ID=1;
    private Bundle bundleForLoader = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);
        mProgress = (ProgressBar) findViewById(R.id.userProgressBar);
        usernameTV =(TextView) findViewById(R.id.usernameView);
        nameTV =(TextView) findViewById(R.id.nameView);
        isPublicTV =(TextView) findViewById(R.id.isPublicView);
        foodCartsTV =(TextView) findViewById(R.id.favoriteFoodCartsView);



        userPrefs = getSharedPreferences(USER_PREFS, 0);
        SharedPreferences.Editor edit=userPrefs.edit();
        edit.putString("id",id);
        edit.putBoolean("isLoggedIn",true);
        edit.commit();

        Boolean isLoggedIn=userPrefs.getBoolean("isLoggedIn",false);

        if(isLoggedIn){
            user_id=userPrefs.getString("id","");
            Toast.makeText(this, "User id: " +id, Toast.LENGTH_SHORT).show();
            Log.d("Comments", "_______________________wasAlreadyLoggedIn______________________");
        }else{
            Log.d("Comments", "_______________________notLoggedIn______________________");
            user_id=id;
        }
        Log.d("Comments", "_______________________onCreate______________________");
        load();

    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {



        return new AsyncTaskLoader<Void>(this) {

            @Override
            public void onStartLoading() {
                super.onStartLoading();
                //makes progress bar visible
                mProgress.setVisibility(View.VISIBLE);
                Log.d("Comments", "_______________________onStartLoading______________________");
            }

            @Override
            public Void loadInBackground() {
                Log.d("Comments", "_______________________callingBuildURL______________________");
                URL url=JNetworkUtils.buildUrlGetOneUser(user_id);

                Log.d("Comments", "_______________________onLoadInBackground______________________");
                try {
                    String json = JNetworkUtils.getResponseFromHttpUrl(url);
                    current_user = JNetworkUtils.parseUserJSON(json);

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
    public void onLoadFinished(Loader<Void> loader,Void data) {
        Log.d("Comments", "_______________________onLoadFinished______________________");
        mProgress.setVisibility(View.INVISIBLE);
        usernameTV.setText("Username: "+current_user.getUsername());
        nameTV.setText("Name: "+current_user.getName());
        isPublicTV.setText("Public: "+current_user.getIsPublic());
        foodCartsTV.setText("Favorite Food Carts : "+current_user.getFavoriteFoodCarts());

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {
    }

    public void load() {
        Log.d("Comments", "_______________________load______________________");
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(USER_LOADER_ID, bundleForLoader, this).forceLoad();

    }
}
