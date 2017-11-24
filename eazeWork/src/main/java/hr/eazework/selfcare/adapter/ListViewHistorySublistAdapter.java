package hr.eazework.selfcare.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import hr.eazework.com.R;
import hr.eazework.com.model.HistoryTimelineModel;

/**
 * Created by allsmartlt218 on 26-12-2016.
 */

public class ListViewHistorySublistAdapter extends ArrayAdapter<HistoryTimelineModel> {
    private Context context;
    private int resource;
    private ArrayList<HistoryTimelineModel> list;
    public ListViewHistorySublistAdapter(Context context, int resource, ArrayList<HistoryTimelineModel> list) {
        super(context,resource,list );
        this.context = context;
        this.list = list;
        this.resource = resource;
    }

    public void refresh(ArrayList<HistoryTimelineModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Nullable
    @Override
    public HistoryTimelineModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.history_tracking_item,parent,false);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.tvTimeFrom);

            holder.grayTop = (TextView) convertView.findViewById(R.id.tvGrayTop);
            holder.grayBottom = (TextView) convertView.findViewById(R.id.tvGrayBottom);
            holder.tvColor = (TextView) convertView.findViewById(R.id.tvColor);
            holder.tvLocationStatus = (TextView) convertView.findViewById(R.id.tvLocationStatus);
            holder.layoutFrom = (RelativeLayout) convertView.findViewById(R.id.llFromDate);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HistoryTimelineModel c = getItem(position);
        String description = c.getmTypeDesc();

        if (position == 0) {
            if(description.equalsIgnoreCase("In-Time")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element, null));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element));
                }
                holder.grayTop.setVisibility(View.GONE);
                if (getCount() == 1) {
                    holder.grayBottom.setVisibility(View.GONE);
                } else {
                    holder.grayBottom.setVisibility(View.VISIBLE);
                }
            }
        } else if (position == list.size() - 1) {
            if(description.equalsIgnoreCase("Out-Time")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_gray, null));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_gray));
                }
                holder.grayTop.setVisibility(View.VISIBLE);
                holder.grayBottom.setVisibility(View.GONE);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!TextUtils.isEmpty(description) && (description.equalsIgnoreCase("In-Time") ||
                            (description.equalsIgnoreCase("Break-In") || description.equalsIgnoreCase("Location-In")))) {
                        holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green, null));
                    } else {
                        holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red, null));
                    }

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (!TextUtils.isEmpty(description) && (description.equalsIgnoreCase("In-Time") ||
                            (description.equalsIgnoreCase("Break-In") || description.equalsIgnoreCase("Location-In")))) {
                        holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green));
                    } else {
                        holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red));
                    }
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!TextUtils.isEmpty(description) && (description.equalsIgnoreCase("In-Time") ||
                        (description.equalsIgnoreCase("Break-In") || description.equalsIgnoreCase("Location-In")))) {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green, null));
                } else {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red, null));
                }

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (!TextUtils.isEmpty(description) && (description.equalsIgnoreCase("In-Time") ||
                        (description.equalsIgnoreCase("Break-In") || description.equalsIgnoreCase("Location-In")))) {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_tracking_element_green));
                } else {
                    holder.tvColor.setBackground(context.getResources().getDrawable(R.drawable.history_time_in_element_red));
                }
            }

            holder.grayTop.setVisibility(View.VISIBLE);
            holder.grayBottom.setVisibility(View.VISIBLE);
        }
        holder.time.setText(c.getmTime());
        holder.tvLocationStatus.setText(description);

        return convertView;
    }

    public static class ViewHolder {
        TextView time;
        TextView grayTop;
        TextView grayBottom;
        TextView tvColor ;
        TextView tvLocationStatus ;
        RelativeLayout layoutFrom;

        int position;
    }
}
