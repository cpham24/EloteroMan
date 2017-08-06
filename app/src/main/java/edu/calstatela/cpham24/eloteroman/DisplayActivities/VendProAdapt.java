package edu.calstatela.cpham24.eloteroman.DisplayActivities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.ReviewBit;
import edu.calstatela.cpham24.eloteroman.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Johnson on 8/3/2017.
 */

public class VendProAdapt extends RecyclerView.Adapter<VendProAdapt.ItemHolder>{


    private ArrayList<ReviewBit> data;
    ItemClickListener listener;

    private Context context;


    public VendProAdapt(ArrayList<ReviewBit> data, ItemClickListener listener ){
        this.data = data;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.revplace, parent, shouldAttachToParentImmediately);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView rate;
        TextView content;


        ItemHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.revName);
            rate = (TextView)view.findViewById(R.id.revRate);
            content = (TextView)view.findViewById(R.id.revCon);

            view.setOnClickListener(this);
        }

        public void bind(int pos){
            ReviewBit repo = data.get(pos);

            name.setText(repo.getReviewName());
            rate.setText(repo.getReviewRating());
            content.setText(repo.getReviewContent());

            Log.d(TAG , " hmm");

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }



}
