package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 18-01-2018.
 */

public class ODRequestDetail implements Serializable {
    private String ApprovalLevel;
    private String Button;
    private int ForEmpID;
    private String PendEmpID;
    private String Remark;
    private String ReqCode;
    private int ReqID;
    private int ReqStatus ;
    private String TotalDays;
    private String ForEmpName;
    private String PendWithName;
    private String StatusDesc;
    private String SubmittedBy;
    private ArrayList<RemarkListItem> RemarkList;
    private ArrayList<SupportDocsItemModel> Attachments;
    private String EndTime;
    private String Place;
    private String StartTime;
    private String Date;
    private String[] Buttons;
    private int PendEmpId;

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
    }

    public int getPendEmpId() {
        return PendEmpId;
    }

    public void setPendEmpId(int pendEmpId) {
        PendEmpId = pendEmpId;
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

    public int getForEmpID() {
        return ForEmpID;
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

    public String getTotalDays() {
        return TotalDays;
    }

    public void setTotalDays(String totalDays) {
        TotalDays = totalDays;
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

    public ArrayList<RemarkListItem> getRemarkList() {
        return RemarkList;
    }

    public void setRemarkList(ArrayList<RemarkListItem> remarkList) {
        RemarkList = remarkList;
    }

    public ArrayList<SupportDocsItemModel> getAttachments() {
        return Attachments;
    }

    public void setAttachments(ArrayList<SupportDocsItemModel> attachments) {
        Attachments = attachments;
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

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }
}
