package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-01-2018.
 */

public class GetEmpWFHResponseItem implements Serializable{
    private String ApprovalLevel;
    private String EndDate;
    private String LeaveName;
    private String Remarks;
    private String ReqCode;
    private String ReqID;
    private String StartDate;
    private String Status;
    private String TotalDays;
    private String WithdrawYN;
    private String PendWithName;
    private String RequestTypeDesc;
    private String StatusDesc;
    private String[] Buttons;
    private String screenName;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public String getPendWithName() {
        return PendWithName;
    }

    public void setPendWithName(String pendWithName) {
        PendWithName = pendWithName;
    }

    public String getRequestTypeDesc() {
        return RequestTypeDesc;
    }

    public void setRequestTypeDesc(String requestTypeDesc) {
        RequestTypeDesc = requestTypeDesc;
    }

    public String getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getLeaveName() {
        return LeaveName;
    }

    public void setLeaveName(String leaveName) {
        LeaveName = leaveName;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getReqCode() {
        return ReqCode;
    }

    public void setReqCode(String reqCode) {
        ReqCode = reqCode;
    }

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTotalDays() {
        return TotalDays;
    }

    public void setTotalDays(String totalDays) {
        TotalDays = totalDays;
    }

    public String getWithdrawYN() {
        return WithdrawYN;
    }

    public void setWithdrawYN(String withdrawYN) {
        WithdrawYN = withdrawYN;
    }
}
