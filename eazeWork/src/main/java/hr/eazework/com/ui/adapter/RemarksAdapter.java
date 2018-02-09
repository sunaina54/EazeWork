package hr.eazework.com.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.RemarkListItem;
import hr.eazework.com.model.RequestRemarksItem;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class RemarksAdapter extends
        RecyclerView.Adapter<RemarksAdapter.MyViewHolder> {
    private ArrayList<RemarkListItem> dataSet;
    private Context context;
    private String screen;
    private LinearLayout errorLinearLayout;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView dateTV, nameTV, remarksReasonTV, remarksStatusTV;

        public MyViewHolder(View v) {
            super(v);
            dateTV = (TextView) v.findViewById(R.id.dateTV);
            nameTV = (TextView) v.findViewById(R.id.nameTV);
            remarksReasonTV = (TextView) v.findViewById(R.id.remarksReasonTV);
            remarksStatusTV = (TextView) v.findViewById(R.id.remarksStatusTV);

        }
    }

    public void addAll(List<RemarkListItem> list) {

        dataSet.addAll(list);
        notifyDataSetChanged();
    }

    public RemarksAdapter(List<RemarkListItem> data) {
        this.dataSet = (ArrayList<RemarkListItem>) data;

    }
    public RemarksAdapter(ArrayList<RemarkListItem> myDataset, Context context, String screen, LinearLayout errorLinearLayout) {
        dataSet = myDataset;
        this.context=context;
        this.screen=screen;
        this.errorLinearLayout=errorLinearLayout;


    }
    @Override
    public RemarksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.remarks_item_layout, parent, false);
       RemarksAdapter.MyViewHolder myViewHolder = new RemarksAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final RemarksAdapter.MyViewHolder holder, final int listPosition) {

        final RemarkListItem item = dataSet.get(listPosition);
        holder.dateTV.setText(item.getDate());
        holder.nameTV.setText(item.getName());
        holder.remarksReasonTV.setText(item.getRemark());
        holder.remarksStatusTV.setText(item.getStatus());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void clearDataSource() {
        dataSet.clear();
        notifyDataSetChanged();
    }
}
