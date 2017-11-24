package hr.eazework.selfcare.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import hr.eazework.com.R;
import hr.eazework.com.model.LeaveTypeModel;
import hr.eazework.com.model.SalaryMonthModel;


public class SpinnerAdapter extends BaseAdapter {
	private ArrayList<LeaveTypeModel> leavedataList;
	private ArrayList<SalaryMonthModel> salarydataList;
	private ArrayList<String> rhLeaveList;
	private LayoutInflater inflater;
	private int lastSelectedPosition;
	private Context context;
	private boolean isLeaveType;
	private boolean isRhLeaaveType;
	private String titleText;

	public SpinnerAdapter(Context context, Object arrayList) {
		inflater = LayoutInflater.from(context);
		leavedataList = (ArrayList<LeaveTypeModel>) arrayList;
		lastSelectedPosition = 0;
		this.context = context;
		isLeaveType = true;
		titleText = "Please Select Leave Type";
	}

	public SpinnerAdapter(Context context, ArrayList<SalaryMonthModel> arrayList) {
		inflater = LayoutInflater.from(context);
		salarydataList = arrayList;
		lastSelectedPosition = 0;
		this.context = context;
		isLeaveType = false;
		titleText = "Please Select Month";
	}

	public SpinnerAdapter(Context context, ArrayList<String> arrayList,
			boolean isRHLeaves) {
		inflater = LayoutInflater.from(context);
		rhLeaveList = arrayList;
		lastSelectedPosition = 0;
		this.context = context;
		isLeaveType = false;
		isRhLeaaveType = true;
		titleText = "Please Select Date";
	}

	@Override
	public int getCount() {
		return isRhLeaaveType ? rhLeaveList.size()
				: isLeaveType ? leavedataList.size() : salarydataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (isRhLeaaveType ? rhLeaveList : isLeaveType ? leavedataList
				: salarydataList).get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setLastSelectedPosition(int lastSelectedPosition) {
		this.lastSelectedPosition = lastSelectedPosition;
	}

	public int getLastSelectedPosition() {
		return this.lastSelectedPosition;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_item_layout,
					parent, false);
			holder = new ViewHolder();
			holder.headerView = convertView.findViewById(R.id.ll_header);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.imageIcon = (ImageView) convertView
					.findViewById(R.id.img_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(isRhLeaaveType){
			holder.title.setText(rhLeaveList.get(position));
		} else
		if ((isLeaveType ? leavedataList : salarydataList).get(position) instanceof LeaveTypeModel) {
			holder.title.setText(((LeaveTypeModel) (isLeaveType ? leavedataList
					: salarydataList).get(position)).getLeaveName());
		} else if ((isLeaveType ? leavedataList : salarydataList).get(position) instanceof SalaryMonthModel) {
			holder.title
					.setText(((SalaryMonthModel) (isLeaveType ? leavedataList
							: salarydataList).get(position)).getmMontTitle());
		}
		if (position == lastSelectedPosition) {
			holder.imageIcon.setImageResource(R.drawable.radio_checked);
		} else {
			holder.imageIcon.setImageResource(R.drawable.radio_unchecked);
		}
		holder.headerView.setVisibility(position == 0 ? View.VISIBLE
				: View.GONE);
		((TextView)holder.headerView.findViewById(R.id.tv_header_title)).setText(titleText);
		return convertView;
	}

	public void updateDataLeave(ArrayList<LeaveTypeModel> leaveTypeList) {
		leavedataList = leaveTypeList;
		notifyDataSetChanged();

	}

	public void updateDataSalary(ArrayList<SalaryMonthModel> leaveTypeList) {
		salarydataList = leaveTypeList;
		notifyDataSetChanged();

	}



	class ViewHolder {
		View headerView;
		TextView title;
		ImageView imageIcon;
	}
}
