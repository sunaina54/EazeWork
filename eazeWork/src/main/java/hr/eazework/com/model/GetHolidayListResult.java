package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 19-02-2018.
 */

public class GetHolidayListResult extends GenericResponse implements Serializable {
    private ArrayList<HolidayList> HolidayList;
    private String ShiftDet;
    private String WeeklyOffDet;

    public String getShiftDet() {
        return ShiftDet;
    }

    public void setShiftDet(String shiftDet) {
        ShiftDet = shiftDet;
    }

    public String getWeeklyOffDet() {
        return WeeklyOffDet;
    }

    public void setWeeklyOffDet(String weeklyOffDet) {
        WeeklyOffDet = weeklyOffDet;
    }

    public ArrayList<hr.eazework.com.model.HolidayList> getHolidayList() {
        return HolidayList;
    }

    public void setHolidayList(ArrayList<hr.eazework.com.model.HolidayList> holidayList) {
        HolidayList = holidayList;
    }
}
