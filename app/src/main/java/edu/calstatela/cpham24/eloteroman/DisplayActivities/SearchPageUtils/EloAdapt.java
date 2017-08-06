package edu.calstatela.cpham24.eloteroman.DisplayActivities.SearchPageUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.calstatela.cpham24.eloteroman.DisplayActivities.data.Vender;
import edu.calstatela.cpham24.eloteroman.R;

import static android.content.ContentValues.TAG;


/**
 * Created by Johnson on 7/31/2017.
 */

public class EloAdapt extends RecyclerView.Adapter<EloAdapt.ItemHolder>{


    private ArrayList<Vender> data;
    ItemClickListener listener;

    private Context context;


    public EloAdapt(ArrayList<Vender> data, ItemClickListener listener ){
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

        View view = inflater.inflate(R.layout.ven, parent, shouldAttachToParentImmediately);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() { return this.data.size(); }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView desc;
        TextView worH;
        TextView worY;

        ItemHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.venderName);
            desc = (TextView)view.findViewById(R.id.venderDesc);
            worH = (TextView)view.findViewById(R.id.venderHour);
            worY = (TextView)view.findViewById(R.id.venderIsWork);

            view.setOnClickListener(this);
        }

        public void bind(int pos){
            Vender repo = data.get(pos);

            name.setText(repo.getName());
            desc.setText(repo.getDesc());
            worH.setText(repo.getWorkHour());

            if (repo.getWork() == true) {
                worY.setText("Currently Working");
            }

            else {
                worY.setText("Not Working");
            }

            Log.d(TAG , " hmm");

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }




}
