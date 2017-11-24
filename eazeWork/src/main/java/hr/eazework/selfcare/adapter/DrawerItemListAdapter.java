package hr.eazework.selfcare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;




import hr.eazework.com.R;
import hr.eazework.com.model.DrawerItemModel;

public class DrawerItemListAdapter extends BaseAdapter{
private ArrayList<DrawerItemModel> mData;
private Context mContext;

public DrawerItemListAdapter(ArrayList<DrawerItemModel> list,Context context) {
	mData=list;
	mContext=context;
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public DrawerItemModel getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder _Holder;
		if(convertView!=null){
			_Holder=(ViewHolder) convertView.getTag();
		}
		else{
			_Holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.drawer_item_layout, parent, false);
			_Holder.mTitle=(TextView) convertView.findViewById(R.id.tv_item_title);
			_Holder.mIcon=(ImageView) convertView.findViewById(R.id.img_item_icon);
			_Holder.itemCount=(TextView) convertView.findViewById(R.id.tv_item_count);
			convertView.setTag(_Holder);
		}
		_Holder.mTitle.setText(getItem(position).getmTitle());
		_Holder.mIcon.setImageResource(getItem(position).getmTitleIconId());
		if(getItem(position).isItemCountVisible()){
			_Holder.itemCount.setVisibility(View.VISIBLE);
			_Holder.itemCount.setText(""+getItem(position).getItemCount());
		}
		else{
			_Holder.itemCount.setVisibility(View.GONE);
		}
		return convertView;
	}
	class ViewHolder{
		TextView mTitle;
		ImageView mIcon;
		TextView itemCount;
	}
	public void updateData(ArrayList<DrawerItemModel> arrayList){
		mData=arrayList;
		notifyDataSetChanged();
	}
}
