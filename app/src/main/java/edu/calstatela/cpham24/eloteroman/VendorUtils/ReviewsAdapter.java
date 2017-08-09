package edu.calstatela.cpham24.eloteroman.VendorUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.R;

/**
 * Created by Dezval on 8/7/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private ArrayList<ArrayList<String>> items;

    public ReviewsAdapter(Context context, ArrayList<ArrayList<String>> items){
        this.items = items;
        this.context = context;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(R.layout.individual_review,parent,false);
        ReviewsAdapter.Item item = new ReviewsAdapter.Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("ADAPTER_DEBUG", items.get(position).get(0));
        ((Item) holder).usersNameTextView.setText(items.get(position).get(0));
        ((Item) holder).vendorRatingBar.setRating(Float.parseFloat(items.get(position).get(1)));
        ((Item) holder).userCommentTextView.setText(items.get(position).get(2));
        ImageView foodImage = ((Item) holder).userImageView;
        Picasso.with(context).load(items.get(position).get(3)).into(foodImage);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        // add here your booleans or switch() to set viewType at your needed
        // I.E if (position == 0) viewType = 1; etc. etc.
        return viewType;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



//    public int getViewTypeCount() {
//        return 2;
//    }

    public class Item extends RecyclerView.ViewHolder{
        TextView usersNameTextView, userCommentTextView;
        RatingBar vendorRatingBar;
        ImageView userImageView;
        public Item(View itemView) {
            super(itemView);
            usersNameTextView = (TextView) itemView.findViewById(R.id.usersNameTextView);
            userCommentTextView = (TextView) itemView.findViewById(R.id.userCommentTextView);
            vendorRatingBar = (RatingBar) itemView.findViewById(R.id.vendorRatingBar);
            userImageView = (ImageView) itemView.findViewById(R.id.userImageView);
        }
    }
}
