package hr.eazework.selfcare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import hr.eazework.com.R;
import hr.eazework.com.model.History;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class ListViewHistoryAdapter extends ArrayAdapter<History> {

    protected ArrayList<History> list;
    protected int resourceId;
    protected LayoutInflater inflater;
    protected Context context;

    public ListViewHistoryAdapter(Context context, int resource, ArrayList<History> list) {
        super(context,resource,list);
        this.context = context;
        this.resourceId = resource;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    public void  refresh( ArrayList<History> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Nullable
    public History getItem(int position) {

        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHistoryAdapter.ViewHolder holder;
        History member = getItem(position);
        if(convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false);
            holder=new ListViewHistoryAdapter.ViewHolder();
            holder.date=(TextView) convertView.findViewById(R.id.tvHistoryDate);
            holder.timeIn=(TextView) convertView.findViewById(R.id.tvHistoryTimeIn);
            holder.timeOut=(TextView) convertView.findViewById(R.id.tvHistoryTimeOut);
            holder.hour=(TextView) convertView.findViewById(R.id.tvHistoryTime);

            convertView.setTag(holder);
        } else {
            holder = (ListViewHistoryAdapter.ViewHolder) convertView.getTag();
        }
        if(member != null) {
            holder.date.setText(member.getmMarkDate());
            if(TextUtils.isEmpty(member.getmTimeIn()) || member.getmTimeIn().equals("null")) {
                holder.timeIn.setText("");
            } else {
                holder.timeIn.setText(member.getmTimeIn());
            }

            if(TextUtils.isEmpty(member.getmTimeOut()) || member.getmTimeOut().equals("null")) {
                holder.timeOut.setText("");
            } else {
                holder.timeOut.setText(member.getmTimeOut());
            }

            if(TextUtils.isEmpty(member.getmStatusDesc()) || member.getmStatusDesc().equals("null")) {
                holder.hour.setText("");
            } else {
                holder.hour.setText(member.getmStatusDesc());
            }


        }


        return convertView;
    }
    class ViewHolder {
        TextView date,timeIn,timeOut,hour;

    }
}
