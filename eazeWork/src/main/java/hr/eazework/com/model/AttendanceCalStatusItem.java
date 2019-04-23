package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 23-02-2018.
 */

public class AttendanceCalStatusItem implements Serializable {

    private String AttendID;
    private String AttendType;
    private String BackDateAttendYN;
    private String DayStatusDesc;
    private String InDate;
    private String MarkDate;
    private String OutDate;
    private String Status;
    private String StatusDesc;
    private String TimeIn;
    private String TimeModYN;
    private String TimeOut;

    public String getAttendID() {
        return AttendID;
    }

    public void setAttendID(String attendID) {
        AttendID = attendID;
    }

    public String getAttendType() {
        return AttendType;
    }

    public void setAttendType(String attendType) {
        AttendType = attendType;
    }

    public String getBackDateAttendYN() {
        return BackDateAttendYN;
    }

    public void setBackDateAttendYN(String backDateAttendYN) {
        BackDateAttendYN = backDateAttendYN;
    }

    public String getDayStatusDesc() {
        return DayStatusDesc;
    }

    public void setDayStatusDesc(String dayStatusDesc) {
        DayStatusDesc = dayStatusDesc;
    }

    public String getInDate() {
        return InDate;
    }

    public void setInDate(String inDate) {
        InDate = inDate;
    }

    public String getMarkDate() {
        return MarkDate;
    }

    public void setMarkDate(String markDate) {
        MarkDate = markDate;
    }

    public String getOutDate() {
        return OutDate;
    }

    public void setOutDate(String outDate) {
        OutDate = outDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public String getTimeIn() {
        return TimeIn;
    }

    public void setTimeIn(String timeIn) {
        TimeIn = timeIn;
    }

    public String getTimeModYN() {
        return TimeModYN;
    }

    public void setTimeModYN(String timeModYN) {
        TimeModYN = timeModYN;
    }

    public String getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(String timeOut) {
        TimeOut = timeOut;
    }
}
