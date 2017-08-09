package edu.calstatela.cpham24.eloteroman.VendorUtils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
 * Created by Dezval on 8/6/2017.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private ArrayList<ArrayList<String>> items;

    public Adapter(Context context, ArrayList<ArrayList<String>> items){
        this.items = items;
        this.context = context;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row;
        if(viewType == 2){
            row = layoutInflater.inflate(R.layout.vendor_data,parent,false);
            VendorData vendorData = new VendorData(row);
            return vendorData;
        }else{
            row = layoutInflater.inflate(R.layout.vendor_foods,parent,false);
            Item item = new Item(row);
            return item;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            ((VendorData)holder).ownerName.setText(items.get(position).get(0));
            ((VendorData)holder).cartName.setText(items.get(position).get(1));
            Picasso.with(context).load(items.get(position).get(2)).into(((VendorData)holder).cartImage);
            ((VendorData)holder).workingHours.setText(items.get(position).get(3));
            if(items.get(position).get(4).contains("false")){
                ((VendorData)holder).workingNow.setText("Not Available");
                ((VendorData)holder).workingNow.setTextColor(Color.RED);
            }else if(items.get(position).get(4).contains("true")){
                ((VendorData)holder).workingNow.setText("Available");
                ((VendorData)holder).workingNow.setTextColor(Color.GREEN);
            }
//            ((VendorData)holder).workingNow.setText(items.get(position).get(4));
            ((VendorData)holder).ratingBar.setRating(Float.parseFloat(items.get(position).get(5)));
            ((VendorData)holder).ratingCount.setText(items.get(position).get(6));

        }else {
            ((Item) holder).foodName.setText(items.get(position).get(0));
            ((Item) holder).foodPrice.setText("$" + items.get(position).get(1));
            ((Item) holder).foodDescription.setText(items.get(position).get(2));
            ImageView foodImage = ((Item) holder).foodImage;
            Picasso.with(context).load(items.get(position).get(3)).into(foodImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        // add here your booleans or switch() to set viewType at your needed
        // I.E if (position == 0) viewType = 1; etc. etc.
        if(position == 0){
            viewType = 2;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public int getViewTypeCount() {
        return 2;
    }

    public class VendorData extends RecyclerView.ViewHolder{
        TextView ownerName, cartName, workingHours, workingNow, ratingCount;
        ImageView cartImage;
        RatingBar ratingBar;
        public VendorData(View itemView) {
            super(itemView);
            ownerName = (TextView) itemView.findViewById(R.id.ownerNameTextView);
            cartName = (TextView) itemView.findViewById(R.id.cartNameTextView);
            cartImage = (ImageView) itemView.findViewById(R.id.cartImageView);
            workingHours = (TextView) itemView.findViewById(R.id.workingHoursTextView);
            workingNow = (TextView) itemView.findViewById(R.id.workingNowTextView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.vendorRatingBar);
            ratingCount = (TextView) itemView.findViewById(R.id.howManyReviewsTextView);
        }
    }

    public class Item extends RecyclerView.ViewHolder{
        TextView foodName, foodPrice, foodDescription;
        ImageView foodImage;
        public Item(View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.vendorFoodItemTextView);
            foodPrice = (TextView) itemView.findViewById(R.id.vendorFoodPriceTextView);
            foodDescription = (TextView) itemView.findViewById(R.id.vendorFoodDescriptionTextView);
            foodImage = (ImageView) itemView.findViewById(R.id.vendorFoodItemImageView);
        }
    }
}
