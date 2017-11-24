package hr.eazework.selfcare.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.MainItemModel;

public class MainProfileItemListAdapter extends BaseAdapter{
private ArrayList<MainItemModel> dataList;
private LayoutInflater inflater;
	private Context context;
//private ImageLoader imageLoader;
private boolean notFirstTime;
public MainProfileItemListAdapter(Context context,ArrayList<MainItemModel> list) {
	dataList=list;
	this.context = context;
	inflater=LayoutInflater.from(context);
//	imageLoader=new ImageLoader((Activity) context);
}
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		MainItemModel model=dataList.get(position);

		if(convertView==null){
			convertView=inflater.inflate(R.layout.list_item_main_layout, parent, false);
			holder=new ViewHolder();
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
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}

		holder.lTitle.setText(model.getmLeftTitle());
		holder.lSUbTitle.setText(model.getmLeftSubTitle());
		if(model.isRightLayoutVisible()){
			holder.rightLayout.setVisibility(View.VISIBLE);
			holder.rTitle.setText(model.getmRightTitle());
			if(model.isUnderLined()){
				//Changes by wahid
			//holder.rSubTitle.setText(Html.fromHtml("<u>"+model.getmRightSubTitle()+"</u>"));
				holder.rSubTitle.setText(model.getmRightSubTitle());
			}
			else {
				holder.rSubTitle.setText(model.getmRightSubTitle());
			}
		}
		else {
			holder.rightLayout.setVisibility(View.GONE);
		}
		if(model.isImageResource()){
			holder.imgIcon.setImageResource(model.getImgResource());
			holder.imgIcon.setVisibility(View.VISIBLE);
			holder.imgRoundIcon.setVisibility(View.GONE);
		} else{
			holder.imgIcon.setVisibility(View.GONE);
			holder.imgRoundIcon.setVisibility(View.VISIBLE);
			holder.imgRoundIcon.setBackgroundResource(model.getImgResource());
			/*imageLoader.loadImage(model.getImgUrl(),
					holder.imgRoundIcon);*/
			Picasso.with(context).load(model.getImgUrl())
								 .fit()
								 .into(holder.imgRoundIcon);
		}
		if(model.isImageVisible()){
			holder.rightIcon.setVisibility(View.VISIBLE);
		}
		else{
			holder.rightIcon.setVisibility(View.GONE);
		}
		if(!notFirstTime)
		MainActivity.animateToVisible(convertView, -1);
		return convertView;
	}
	class ViewHolder{
		TextView lTitle,lSUbTitle,rTitle,rSubTitle;
		LinearLayout leftLayout,rightLayout;
		ImageView imgIcon,rightIcon,imgRoundIcon;
	}
	public void updateData(ArrayList<MainItemModel> arrayList){
		dataList=arrayList;
		notFirstTime=true;
		notifyDataSetChanged();
	}
}
