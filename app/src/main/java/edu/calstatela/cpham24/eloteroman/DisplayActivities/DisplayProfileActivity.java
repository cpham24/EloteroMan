package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.favoriteCart;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.user;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.utilities.JNetworkUtils;
import edu.calstatela.cpham24.eloteroman.DisplayActivities.utilities.favoriteCartAdapter;
import edu.calstatela.cpham24.eloteroman.R;

public class DisplayProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {

    TextView usernameTV;
    TextView nameTV;
    TextView isPublicTV;
    TextView foodCartsTV;
    ImageView pictureIV;
    ProgressBar mProgress;
    private SharedPreferences userPrefs;
    String USER_PREFS="user";
    Context context=this;
    final String id = "596d335cb158f84ac6fb474d";
    static final String TAG = "profileActivity";
    String user_id;
    user current_user;
    int USER_LOADER_ID=1;
    private Bundle bundleForLoader = null;
    private RecyclerView mRecyclerView;
    private favoriteCartAdapter mcartAdapter;
    ArrayList<favoriteCart> cartList=new ArrayList<favoriteCart>();
    ArrayList<String> ids;
    String imageUrl;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_carts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mProgress = (ProgressBar) findViewById(R.id.userProgressBar);
        usernameTV =(TextView) findViewById(R.id.usernameView);
        nameTV =(TextView) findViewById(R.id.nameView);
        isPublicTV =(TextView) findViewById(R.id.isPublicView);
        foodCartsTV =(TextView) findViewById(R.id.favoriteFoodCartsView);
        pictureIV =(ImageView) findViewById(R.id.avatarView);

        userPrefs = getSharedPreferences(USER_PREFS, 0);
        username = getIntent().getExtras().getString("username");

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
            favoriteCart current_cart;

            @Override
            public void onStartLoading() {
                super.onStartLoading();
                //makes progress bar visible
                if (current_user==null)
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



            if(current_user!=null) {
                ids = current_user.getFavoriteFoodCarts();
                if (ids != null) {
                    for (int i = 0; i < ids.size(); i++) {
                        URL cart_url = JNetworkUtils.buildUrlGetOneCart(ids.get(i));
                        try {


                            Log.d("Comments", "_______________________here1?______________________");
                            String cart_json = JNetworkUtils.getResponseFromHttpUrl(cart_url);
                            Log.d("Comments", "_______________________here2?______________________");
                            current_cart = JNetworkUtils.parseFavoriteCartJSON(cart_json);
                            Log.d("Comments", "_______________________here3?______________________");



                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Comments", "_______________________here4?______________________");

                        if(current_cart!=null) {
                            cartList.add(current_cart);
                        }
                    }
                }


            }



                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader,Void data) {
        Log.d("Comments", "_______________________onLoadFinished______________________");
        if(current_user!=null){
            mProgress.setVisibility(View.INVISIBLE);
            usernameTV.setText("Username: "+current_user.getUsername());
            nameTV.setText("Name: "+current_user.getName());
            isPublicTV.setText("Public: "+current_user.getIsPublic());
            imageUrl=current_user.getAvatar();
            if(imageUrl != null) {
                Picasso.with(context)
                        .load(imageUrl)
                        .into(pictureIV);
            }

            if(cartList!=null) {
                mcartAdapter = new favoriteCartAdapter(cartList, new favoriteCartAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex, String cartId) {

                        Log.d(TAG, String.format("id: %s", cartId));
                        Toast.makeText(context, "id: "+cartId, Toast.LENGTH_SHORT).show();
                    }
                });
                mRecyclerView.setAdapter(mcartAdapter);
            }

        }

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
