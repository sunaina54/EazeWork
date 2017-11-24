package hr.eazework.com.model;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import hr.eazework.com.ui.util.StringUtils;

public class CheckInOutModel {
	private boolean isCheckedIn;
	private boolean isCheckedOut;
	private String checkInTime;
	private String checkOutTime;
	private String breakInTime;
	private String breakOutTime;
    private String CurrentTimeWithTimeZone;
	private int attendanceLevel;
	private boolean isBreakIn;
	private boolean isBreakOut;
	private boolean isLogoutEnable;
    private String inDate;
    private String outDate;
    private String inDateTime;
    private String outDateTime;
    private Date lastFetchedDate = null;
    private String CurrentDateTimeWithTimeZone;
	public CheckInOutModel() {
		// TODO Auto-generated constructor stub
	}
	public CheckInOutModel(String response) {
		try {
			JSONObject jsonObject=new JSONObject(response);
			checkInTime=jsonObject.optString("InTime", "");
			checkOutTime=jsonObject.optString("OutTime", "");
			breakInTime=jsonObject.optString("BreakOut", "");
			breakOutTime=jsonObject.optString("BreakIn", "");
			isLogoutEnable=jsonObject.optString("MarkAttendanceYN", "N").equalsIgnoreCase("Y");
			attendanceLevel =jsonObject.optInt("ATTLevel", 2);
            inDate = jsonObject.optString("InDate","");
            outDate = jsonObject.optString("OutDate","");
            CurrentDateTimeWithTimeZone=jsonObject.optString("CurrentDateTimeWithTimeZone","");
            lastFetchedDate = Calendar.getInstance().getTime();//keep the reference of last fetch date to use as reference to cacl time diff
            CurrentTimeWithTimeZone=jsonObject.optString("CurrentTimeWithTimeZone","");

			if(StringUtils.isNotEmptyAndNULLString(breakInTime) &&!breakInTime.equalsIgnoreCase("01-01-0001 00:00")){
				isBreakIn=true;
			} else {
				isBreakIn=false;
			}
			
			if(StringUtils.isNotEmptyAndNULLString(breakOutTime) &&!breakOutTime.equalsIgnoreCase("01-01-0001 00:00")){
				isBreakOut=true;
			} else {
				isBreakOut=false;
			}

			if((StringUtils.isNotEmptyAndNULLString(inDate) && StringUtils.isNotEmptyAndNULLString(checkInTime))) {
                inDateTime = inDate + " " + checkInTime;
                isCheckedIn = true;
            } else {
                inDateTime = "";
                isCheckedIn = false;
            }
            if((StringUtils.isNotEmptyAndNULLString(outDate) && StringUtils.isNotEmptyAndNULLString(checkOutTime))) {
                outDateTime = outDate + " " + checkOutTime;
                isCheckedOut = true;
            } else {
                outDateTime = "";
                isCheckedOut = false;
            }

		} catch (JSONException e) {
			e.printStackTrace();
            Crashlytics.logException(e);
		}
	}

    public String getInDateTime() {
        return inDateTime;
    }

    public void setInDateTime(String inDateTime) {
        this.inDateTime = inDateTime;
    }

    public String getOutDateTime() {
        return outDateTime;
    }

    public void setOutDateTime(String outDateTime) {
        this.outDateTime = outDateTime;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getCurrentDateTimeWithTimeZone() {
        return CurrentDateTimeWithTimeZone;
    }

    public void setCurrentDateTimeWithTimeZone(String currentDateTimeWithTimeZone) {
        CurrentDateTimeWithTimeZone = currentDateTimeWithTimeZone;
    }

    public String getCurrentTimeWithTimeZone() {
        return CurrentTimeWithTimeZone;
    }

    public void setCurrentTimeWithTimeZone(String currentTimeWithTimeZone) {
        CurrentTimeWithTimeZone = currentTimeWithTimeZone;
    }

    public boolean isLogoutEnable() {
        return isLogoutEnable;
    }

    public boolean isCheckedIn() {
		return isCheckedIn;
	}

	public void setCheckedIn(boolean isCheckedIn) {
		this.isCheckedIn = isCheckedIn;
	}

	public boolean isCheckedOut() {
		return isCheckedOut;
	}

	public void setCheckedOut(boolean isCheckedOut) {
		this.isCheckedOut = isCheckedOut;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public String getBreakInTime() {
		return breakInTime;
	}
	public void setBreakInTime(String breakInTime) {
		this.breakInTime = breakInTime;
	}
	public String getBreakOutTime() {
		return breakOutTime;
	}
	public void setBreakOutTime(String breakOutTime) {
		this.breakOutTime = breakOutTime;
	}
	public boolean isBreakIn() {
		return isBreakIn;
	}
	public void setBreakIn(boolean isBreakIn) {
		this.isBreakIn = isBreakIn;
	}
	public boolean isBreakOut() {
		return isBreakOut;
	}
	public void setBreakOut(boolean isBreakOut) {
		this.isBreakOut = isBreakOut;
	}
	public int getAttendanceLevel() {
		return attendanceLevel;
	}
	public void setAttendanceLevel(int attendanceLevel) {
		this.attendanceLevel = attendanceLevel;
	}
	public boolean isMarkAttandanceEnable() {
		return isLogoutEnable;
	}
	public void setLogoutEnable(boolean isLogoutEnable) {
		this.isLogoutEnable = isLogoutEnable;
	}

    public Date getLastFetchedDate() {
        return lastFetchedDate;
    }

    public void setLastFetchedDate(Date lastFetchedDate) {
        this.lastFetchedDate = lastFetchedDate;
    }
}
