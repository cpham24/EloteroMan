package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    Boolean swiped=false;
    ImageButton logoutBtn;
    Button editBtn;
    String cartId;

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
        logoutBtn   = (ImageButton) findViewById(R.id.logout_btn);
        editBtn = (Button) findViewById(R.id.edit_user_btn);

        userPrefs = getSharedPreferences(USER_PREFS, 0);
        username = getIntent().getExtras().getString("username");



        Boolean isLoggedIn=userPrefs.getBoolean("isLoggedIn",false);

        if(isLoggedIn||username!=null){
            if(isLoggedIn){
            user_id=userPrefs.getString("id","");
            Toast.makeText(this, "User id: " +user_id, Toast.LENGTH_SHORT).show();}
            else{

                Toast.makeText(this, "Username: " +username, Toast.LENGTH_SHORT).show();
            }

        }else{
            launchLogin();
        }

        logoutBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                userPrefs = getSharedPreferences(USER_PREFS, 0);
                SharedPreferences.Editor edit=userPrefs.edit();
                edit.putString("id",null);
                edit.putBoolean("isLoggedIn",false);
                edit.commit();
                launchLogin();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DisplayEditProfileActivity.class);
                i.putExtra("user_id", current_user.getId());
                i.putExtra("user_name", current_user.getName());
                i.putExtra("user_username", current_user.getUsername());
                i.putExtra("user_isPublic", current_user.getIsPublic());
                startActivity(i);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id = (int) viewHolder.getLayoutPosition();
                swiped=true;
                cartId = cartList.get(id).getId();
                load();
                Log.d(TAG, "passing id: " + cartId);

            }
        }).attachToRecyclerView(mRecyclerView);

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

            }

            @Override
            public Void loadInBackground() {
                URL url;
                URL urlSwipe;

                if(swiped){
                    urlSwipe = JNetworkUtils.buildUrlDeleteFavoriteCartFromUser(user_id,cartId);


                }else{
                    urlSwipe = JNetworkUtils.buildUrlDeleteFavoriteCartFromUser("","");
                }

                if (username != null) {
                    url = JNetworkUtils.buildUrlGetOneUserWithUsername(username);
                } else {
                    url = JNetworkUtils.buildUrlGetOneUser(user_id);
                }



                try {
                    if(swiped){
                        JNetworkUtils.getResponseFromHttpUrl(urlSwipe);
                        swiped=false;
                        cartId=null;
                        cartList.clear();
                    }
                    String json = JNetworkUtils.getResponseFromHttpUrl(url);
                    if(username!=null){
                        current_user = JNetworkUtils.parseLoginUserJSON(json);
                    }else{
                        current_user = JNetworkUtils.parseUserJSON(json);}


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



                            String cart_json = JNetworkUtils.getResponseFromHttpUrl(cart_url);
                            current_cart = JNetworkUtils.parseFavoriteCartJSON(cart_json);




                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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
        if(current_user!=null){
            mProgress.setVisibility(View.GONE);
            usernameTV.setText(current_user.getUsername());
            nameTV.setText(current_user.getName());
            if(current_user.getIsPublic().equals("true")||current_user.getIsPublic().equals("True"))
                isPublicTV.setText("Account is public");
            else
                isPublicTV.setText("Account is not public");
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

                        Intent i = new Intent(context, DisplayVendorActivity.class);
                        i.putExtra("vendor_id", cartId);
                        startActivity(i);
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

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(USER_LOADER_ID, bundleForLoader, this).forceLoad();

    }

    public void launchLogin() {
        Intent i = new Intent(this, DisplayLoginActivity.class);
        startActivity(i);
    }

}
