package hr.eazework.selfcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.Chips;
import hr.eazework.com.ui.interfaces.OnRemoveListener;

/**
 * Created by Manjunath on 27-03-2017.
 */

public class ChipViewRecyclerAdapter extends RecyclerView.Adapter<ChipViewRecyclerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Chips> list;
    private OnRemoveListener listener;

    public ChipViewRecyclerAdapter(Context context,List<Chips> list, OnRemoveListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_chip,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void refreshData(List<Chips> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void RemoveItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void AddItem(Chips chips,int position) {
        list.add(chips);
        notifyItemInserted(position);
    }

    public Chips getItem(int position){
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chips chips = list.get(position);
        holder.setData(chips,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;
        ImageButton close;


        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.ivPhoto);
            text = (TextView) itemView.findViewById(R.id.tvName);
            close = (ImageButton) itemView.findViewById(R.id.ibClose);
            close.setVisibility(View.GONE);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemRemoved(getAdapterPosition());

                }
            });
        }

        public void setData(Chips chips, int position) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.icon_profile);
            text.setText(chips.getDescription());
            close.setImageResource(R.drawable.ic_close);
        }
    }
}
