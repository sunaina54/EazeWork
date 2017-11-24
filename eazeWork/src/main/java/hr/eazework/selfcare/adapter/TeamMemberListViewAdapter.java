package hr.eazework.selfcare.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hr.eazework.com.R;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TeamMember;
import hr.eazework.selfcare.communication.CommunicationConstant;

/**
 * Created by Manjunath on 23-03-2017.
 */

public class TeamMemberListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TeamMember> list;
    private int resource;
    private LayoutInflater inflater;

    public TeamMemberListViewAdapter(Context context, int resource, ArrayList<TeamMember> list) {
        this.context = context;
        this.resource = resource;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(ArrayList<TeamMember> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TeamMember getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        TeamMember member = getItem(position);
        if(convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder=new TeamMemberListViewAdapter.ViewHolder();
            holder.lTitle=(TextView) convertView.findViewById(R.id.tv_left_title);
            holder.lSUbTitle=(TextView) convertView.findViewById(R.id.tv_left_sub_title);
            holder.rTitle=(TextView) convertView.findViewById(R.id.tv_right_title);
            holder.rSubTitle=(TextView) convertView.findViewById(R.id.tv_right_sub_title);
            holder.leftLayout=(LinearLayout) convertView.findViewById(R.id.ll_left_container);
            holder.rightLayout=(LinearLayout) convertView.findViewById(R.id.ll_right_container);
            holder.rightIcon=(ImageView) convertView.findViewById(R.id.img_next_icon);
            holder.imgIcon=(ImageView) convertView.findViewById(R.id.img_icon);
            holder.imgRoundIcon=(ImageView) convertView.findViewById(R.id.img_icon_rounded);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lTitle.setText(member.getmName());
        holder.lSUbTitle.setText(member.getmDesignation());
        holder.rTitle.setText("Day's Status");
        holder.rSubTitle.setText(member.getmDayStatus());

   //     Picasso.with(context).load(CommunicationConstant.getMobileCareURl() + member.getmImageUrl()).fit().into(holder.imgIcon);

        String imagePath = member.getmImageUrl();
        loadProfileImage(imagePath,holder.imgIcon);

        MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
        if(menuItemModel != null ) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.ATTANDANCE_KEY);
            if(itemModel != null && itemModel.isAccess()) {
                holder.rTitle.setVisibility(View.VISIBLE);
                holder.rSubTitle.setVisibility(View.VISIBLE);
            } else {
                holder.rTitle.setVisibility(View.INVISIBLE);
                holder.rSubTitle.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }
    class ViewHolder {
        TextView lTitle,lSUbTitle,rTitle,rSubTitle;
        LinearLayout leftLayout,rightLayout;
        ImageView imgIcon,rightIcon,imgRoundIcon;
    }

    public void loadProfileImage(String imagePath, final ImageView ivTimeLineImageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            Picasso.with(context).load(CommunicationConstant.getMobileCareURl() + imagePath).fit().placeholder(R.drawable.profile_image).into(ivTimeLineImageView,
                    new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            ivTimeLineImageView.setImageResource(R.drawable.profile_image);
                        }
                    });
        } else {
            ivTimeLineImageView.setImageResource(R.drawable.profile_image);
        }
    }
}
