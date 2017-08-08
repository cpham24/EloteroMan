package edu.calstatela.cpham24.eloteroman.VendorUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        View row = layoutInflater.inflate(R.layout.vendor_foods,parent,false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Item)holder).foodName.setText(items.get(position).get(0));
        ((Item)holder).foodPrice.setText(items.get(position).get(1));
        ((Item)holder).foodDescription.setText(items.get(position).get(2));
        ImageView foodImage = ((Item)holder).foodImage;
        Picasso.with(context).load(items.get(position).get(3)).into(foodImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
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
