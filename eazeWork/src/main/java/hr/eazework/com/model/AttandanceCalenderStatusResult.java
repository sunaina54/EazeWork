package hr.eazework.com.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class AttandanceCalenderStatusResult {
	private static AttandanceCalenderStatusResult mInstance;
	private ArrayList<AttandanceCalenderStatusItem> attandanceCalenderStatusItems;

	public static AttandanceCalenderStatusResult getInstance() {
		if (mInstance == null) {
			mInstance = new AttandanceCalenderStatusResult();
		}
		return mInstance;
	}

	public ArrayList<AttandanceCalenderStatusItem> getAttandanceCalenderStatusItems() {
		return attandanceCalenderStatusItems;
	}

	public void updateAttandanceCalenderStatusItems(
			ArrayList<AttandanceCalenderStatusItem> attandanceCalenderStatusItems) {
		if (attandanceCalenderStatusItems == null) {
			this.attandanceCalenderStatusItems = attandanceCalenderStatusItems;
		} else {
			for (AttandanceCalenderStatusItem attandanceCalenderStatusItem : attandanceCalenderStatusItems) {
				if (!isContains(attandanceCalenderStatusItem.getMarkDate())) {
					this.attandanceCalenderStatusItems
							.add(attandanceCalenderStatusItem);
				}
			}
		}
	}

	public void updateAttandanceCalenderStatusItems(JSONArray array) {
		if (attandanceCalenderStatusItems == null) {
			this.attandanceCalenderStatusItems = new ArrayList<>();
		} else {
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					try {
						AttandanceCalenderStatusItem attandanceCalenderStatusItem = new AttandanceCalenderStatusItem(
								array.getJSONObject(i));
						if (!isContains(attandanceCalenderStatusItem.getMarkDate())) {
							this.attandanceCalenderStatusItems
									.add(attandanceCalenderStatusItem);
						} else {
							updateAttandance(attandanceCalenderStatusItem);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void updateAttandance(AttandanceCalenderStatusItem item) {
		for (AttandanceCalenderStatusItem attItem:attandanceCalenderStatusItems) {
			if(attItem.getMarkDate().equalsIgnoreCase(item.getMarkDate())){
				attItem.setAttendType(item.getAttendType());
				attItem.setStatus(item.getStatus());
				attItem.setStatusDesc(item.getStatusDesc());
				attItem.setTimeIn(item.getTimeIn());
				attItem.setTimeOut(item.getTimeOut());
			}
		}
	}
	public Boolean isContains(String string) {
		if(attandanceCalenderStatusItems==null){
			attandanceCalenderStatusItems=new ArrayList<>();
			return false;
		}
		for (AttandanceCalenderStatusItem attandanceCalenderStatusItem : attandanceCalenderStatusItems) {
			if (attandanceCalenderStatusItem.getMarkDate().equalsIgnoreCase(
					string)) {
				return true;
			}
		}
		return false;
	}

	public AttandanceCalenderStatusItem getStatusItem(String string) {
		if(attandanceCalenderStatusItems==null){
			attandanceCalenderStatusItems=new ArrayList<>();
			return null;
		}
		for (AttandanceCalenderStatusItem attandanceCalenderStatusItem : attandanceCalenderStatusItems) {
			if (attandanceCalenderStatusItem.getMarkDate().equalsIgnoreCase(
					string)) {
				return attandanceCalenderStatusItem;
			}
		}
		return null;
	}
}
