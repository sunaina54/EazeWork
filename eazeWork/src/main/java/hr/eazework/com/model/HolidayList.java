package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 19-02-2018.
 */

public class HolidayList implements Serializable {
    private String HolidayDate;
    private String HolidayDesc;
    private String HolidayType;
    private String WeekDay;
    private String HolidayTypeDesc;

    public String getHolidayTypeDesc() {
        return HolidayTypeDesc;
    }

    public void setHolidayTypeDesc(String holidayTypeDesc) {
        HolidayTypeDesc = holidayTypeDesc;
    }

    public String getHolidayDate() {
        return HolidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        HolidayDate = holidayDate;
    }

    public String getHolidayDesc() {
        return HolidayDesc;
    }

    public void setHolidayDesc(String holidayDesc) {
        HolidayDesc = holidayDesc;
    }

    public String getHolidayType() {
        return HolidayType;
    }

    public void setHolidayType(String holidayType) {
        HolidayType = holidayType;
    }

    public String getWeekDay() {
        return WeekDay;
    }

    public void setWeekDay(String weekDay) {
        WeekDay = weekDay;
    }
}

