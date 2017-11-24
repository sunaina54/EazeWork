package hr.eazework.selfcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.R;
import hr.eazework.com.model.StoreLocationModel;

/**
 * Created by Manjunath on 28-03-2017.
 */

public class StoreListAdapter extends BaseAdapter {

    private List<StoreLocationModel> list;
    private LayoutInflater inflater;

    public StoreListAdapter(Context context,List<StoreLocationModel> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(ArrayList<StoreLocationModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StoreLocationModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        StoreLocationModel storeLocationModel = getItem(position);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.store_list_item,parent,false);
            holder = new ViewHolder();
            holder.lTitle=(TextView) convertView.findViewById(R.id.tv_left_title);
            holder.lSUbTitle=(TextView) convertView.findViewById(R.id.tv_left_sub_title);
            holder.rTitle=(TextView) convertView.findViewById(R.id.tv_right_title);
            holder.rSubTitle=(TextView) convertView.findViewById(R.id.tv_right_sub_title);
            holder.leftLayout=(LinearLayout) convertView.findViewById(R.id.ll_left_container);
            holder.rightLayout=(LinearLayout) convertView.findViewById(R.id.ll_right_container);
            holder.rightIcon=(ImageView) convertView.findViewById(R.id.img_next_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lTitle.setText(storeLocationModel.getmOfficeName());
        holder.lSUbTitle.setText(storeLocationModel.getmOfficeCode());
        holder.rTitle.setText("Employees");
        holder.rSubTitle.setText(storeLocationModel.getmEmpCount());

        return convertView;
    }

    class ViewHolder {
        TextView lTitle,lSUbTitle,rTitle,rSubTitle;
        LinearLayout leftLayout,rightLayout;
        ImageView rightIcon;
    }

}
