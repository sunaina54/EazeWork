package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class WFHRequestDetailItem implements Serializable {
    private String ApprovalLevel;
    private String Button;
    private String ForEmpID;
    private String PendEmpID;
    private String Remark;
    private String ReqCode;
    private int ReqID;
    private int ReqStatus ;
    private String EndDate;
    private String StartDate ;
    private String TotalDays;
    private String ForEmpName;
    private String PendWithName;
    private String StatusDesc;
    private String SubmittedBy;
    private ArrayList<RemarkListItem> RemarkList;
    private PartialDay PartialDay;
    private String EndTime;
    private String Place;
    private String StartTime;
    private String Date;
    private String[] Buttons;
    private ArrayList<SupportDocsItemModel> Attachments;

    public ArrayList<SupportDocsItemModel> getAttachments() {
        return Attachments;
    }

    public void setAttachments(ArrayList<SupportDocsItemModel> attachments) {
        Attachments = attachments;
    }

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getForEmpName() {
        return ForEmpName;
    }

    public void setForEmpName(String forEmpName) {
        ForEmpName = forEmpName;
    }

    public String getPendWithName() {
        return PendWithName;
    }

    public void setPendWithName(String pendWithName) {
        PendWithName = pendWithName;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public String getSubmittedBy() {
        return SubmittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        SubmittedBy = submittedBy;
    }

    public String getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getButton() {
        return Button;
    }

    public void setButton(String button) {
        Button = button;
    }

    public String getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(String forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getPendEmpID() {
        return PendEmpID;
    }

    public void setPendEmpID(String pendEmpID) {
        PendEmpID = pendEmpID;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getReqCode() {
        return ReqCode;
    }

    public void setReqCode(String reqCode) {
        ReqCode = reqCode;
    }

    public int getReqID() {
        return ReqID;
    }

    public void setReqID(int reqID) {
        ReqID = reqID;
    }

    public int getReqStatus() {
        return ReqStatus;
    }

    public void setReqStatus(int reqStatus) {
        ReqStatus = reqStatus;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getTotalDays() {
        return TotalDays;
    }

    public void setTotalDays(String totalDays) {
        TotalDays = totalDays;
    }

    public ArrayList<RemarkListItem> getRemarkList() {
        return RemarkList;
    }

    public void setRemarkList(ArrayList<RemarkListItem> remarkList) {
        RemarkList = remarkList;
    }

    public hr.eazework.com.model.PartialDay getPartialDay() {
        return PartialDay;
    }

    public void setPartialDay(hr.eazework.com.model.PartialDay partialDay) {
        PartialDay = partialDay;
    }
}
