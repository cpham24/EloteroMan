package edu.calstatela.cpham24.eloteroman.DisplayActivities.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.favoriteCart;
import edu.calstatela.cpham24.eloteroman.R;


/**
 * Created by Ambrosio on 8/6/2017.
 */

public class favoriteCartAdapter extends RecyclerView.Adapter<favoriteCartAdapter.favoriteCartAdapterViewHolder>{
    private Context context;
    public static final String TAG = "favoriteCartadapter";
    private ArrayList<favoriteCart> favoriteCarts;
    ItemClickListener listener;



    public favoriteCartAdapter(ArrayList<favoriteCart> favoriteCarts, ItemClickListener listener){
        this.favoriteCarts= favoriteCarts;
        this.listener=listener;
    }

    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex,String cartId);
    }

    @Override
    public favoriteCartAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context=viewGroup.getContext();
        int layoutIdforListItem= R.layout.favorite_cart_list_item;
        LayoutInflater infalter = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=infalter.inflate(layoutIdforListItem,viewGroup,shouldAttachToParentImmediately);
        favoriteCartAdapterViewHolder holder=new favoriteCartAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(favoriteCartAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return favoriteCarts.size();
    }

    public class favoriteCartAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView address;
        TextView time;
        TextView id;
        ImageView picture;
        ImageView serviceImage;
        String cartId;


        favoriteCartAdapterViewHolder(View itemView) {
            super(itemView);
            id=(TextView) itemView.findViewById(R.id.cart_id);
            name =(TextView) itemView.findViewById(R.id.cart_name);
            address =(TextView) itemView.findViewById(R.id.cart_address);
            time =(TextView) itemView.findViewById(R.id.cart_time);
            picture=(ImageView) itemView.findViewById(R.id.cart_image);
            serviceImage=(ImageView) itemView.findViewById(R.id.cart_available);

            itemView.setOnClickListener(this);
        }

        void bind(int pos){
            favoriteCart cart=favoriteCarts.get(pos);
            id.setText(cart.getId());
            cartId=cart.getId();
            name.setText(cart.getName());
            address.setText(cart.getAddress());
            time.setText(cart.getTime());

            if(cart.getUrl() != null) {
                Picasso.with(context)
                        .load(cart.getUrl())
                        .into(picture);
            }

            if(cart.getIsInService().equals("true")||cart.getIsInService().equals("True")){
                serviceImage.setVisibility(View.VISIBLE);
            }
            else serviceImage.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            listener.onItemClick(pos, cartId);

        }
    }
}
