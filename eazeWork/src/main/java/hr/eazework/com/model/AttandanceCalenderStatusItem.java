package hr.eazework.com.model;

import java.io.Serializable;

import org.json.JSONObject;

public class AttandanceCalenderStatusItem implements Serializable{
	private String attendType;
	private String markDate;
	private String status;
	private String statusDesc;
	private String timeIn;
	private String timeOut;
	public AttandanceCalenderStatusItem(JSONObject jsonObject) {
		attendType=jsonObject.optString("AttendType","");
		markDate=jsonObject.optString("MarkDate","");
		status=jsonObject.optString("Status","");
		statusDesc=jsonObject.optString("StatusDesc","");
		timeIn=jsonObject.optString("TimeIn","--:--");
		timeOut=jsonObject.optString("TimeOut","--:--");
	}
	
	public String getAttendType() {
		return attendType;
	}

	public void setAttendType(String attendType) {
		this.attendType = attendType;
	}

	public String getMarkDate() {
		return markDate;
	}

	public void setMarkDate(String markDate) {
		this.markDate = markDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	
}
