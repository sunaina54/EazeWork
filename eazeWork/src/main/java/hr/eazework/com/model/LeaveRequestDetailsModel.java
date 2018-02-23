package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 01-02-2018.
 */

public class LeaveRequestDetailsModel implements Serializable {
    private String ApprovalLevel;
    private String Button;
    private int ForEmpID;
    private int PendEmpID;
    private String Remark;
    private String ReqCode;
    private int ReqID;
    private int ReqStatus ;
    private String HalfDayFS;
    private String EndDate;
    private String StartDate ;
    private String TotalDays;
    private String ForEmpName;
    private String PendWithName;
    private String StatusDesc;
    private String SubmittedBy;
    private String AttachmentMandatoryYN;
    private ArrayList<RemarkListItem> RemarkList;
    private PartialDay PartialDay;
    private String AvailableDays;
    private String ConsumedDays;
    private String LeaveDesc;
    private String LeaveID;
    private String[] Buttons;
    private ArrayList<SupportDocsItemModel> Attachments;

    public String getHalfDayFS() {
        return HalfDayFS;
    }

    public void setHalfDayFS(String halfDayFS) {
        HalfDayFS = halfDayFS;
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

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
    }

    public int getPendEmpID() {
        return PendEmpID;
    }

    public void setPendEmpID(int pendEmpID) {
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

    public String getAttachmentMandatoryYN() {
        return AttachmentMandatoryYN;
    }

    public void setAttachmentMandatoryYN(String attachmentMandatoryYN) {
        AttachmentMandatoryYN = attachmentMandatoryYN;
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

    public String getAvailableDays() {
        return AvailableDays;
    }

    public void setAvailableDays(String availableDays) {
        AvailableDays = availableDays;
    }

    public String getConsumedDays() {
        return ConsumedDays;
    }

    public void setConsumedDays(String consumedDays) {
        ConsumedDays = consumedDays;
    }

    public String getLeaveDesc() {
        return LeaveDesc;
    }

    public void setLeaveDesc(String leaveDesc) {
        LeaveDesc = leaveDesc;
    }

    public String getLeaveID() {
        return LeaveID;
    }

    public void setLeaveID(String leaveID) {
        LeaveID = leaveID;
    }

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }

    public ArrayList<SupportDocsItemModel> getAttachments() {
        return Attachments;
    }

    public void setAttachments(ArrayList<SupportDocsItemModel> attachments) {
        Attachments = attachments;
    }
}
