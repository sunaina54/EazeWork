package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 10-01-2018.
 */

public class OdReqDetailModel implements Serializable {
    private int ApprovalLevel;
    private String ReqID;
    private String ForEmpID;
    private String Date;
    private String StartTime;
    private String EndTime;
    private String Remark;
    private String Place;
    private String Button;
    private ArrayList<SupportDocsItemModel> Attachments;

    public ArrayList<SupportDocsItemModel> getAttachments() {
        return Attachments;
    }

    public void setAttachments(ArrayList<SupportDocsItemModel> attachments) {
        Attachments = attachments;
    }

    public int getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(int approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getButton() {
        return Button;
    }

    public void setButton(String button) {
        Button = button;
    }

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

    public String getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(String forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getRemarks() {
        return Remark;
    }

    public void setRemarks(String remarks) {
        Remark = remarks;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}
