package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 07-02-2018.
 */

public class AttendanceReqDetail implements Serializable{
    private String ApprovalLevel;
    private String AttendID;
    private String BreakID;
    private String EmpCode;
    private int EmpID;
    private String ExistingOutTime;
    private String ExistingTime;
    private String MarkDate;
    private String Name;
    private String Remark;
    private String ReqCategoryDesc;
    private String ReqCategoryID;
    private String ReqOutTime;
    private String ReqTime;
    private String Status;
    private String PendWithName;
    private String ReqCode;
    private String ReqID;
    private String ReqType;
    private String StatusDesc;
    private String SubmittedBy;
    private String[] Buttons;
    private ArrayList<RemarkListItem> RemarkList;

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }

    public String getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getAttendID() {
        return AttendID;
    }

    public void setAttendID(String attendID) {
        AttendID = attendID;
    }

    public String getBreakID() {
        return BreakID;
    }

    public void setBreakID(String breakID) {
        BreakID = breakID;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int empID) {
        EmpID = empID;
    }

    public String getExistingOutTime() {
        return ExistingOutTime;
    }

    public void setExistingOutTime(String existingOutTime) {
        ExistingOutTime = existingOutTime;
    }

    public String getExistingTime() {
        return ExistingTime;
    }

    public void setExistingTime(String existingTime) {
        ExistingTime = existingTime;
    }

    public String getMarkDate() {
        return MarkDate;
    }

    public void setMarkDate(String markDate) {
        MarkDate = markDate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getReqCategoryDesc() {
        return ReqCategoryDesc;
    }

    public void setReqCategoryDesc(String reqCategoryDesc) {
        ReqCategoryDesc = reqCategoryDesc;
    }

    public String getReqCategoryID() {
        return ReqCategoryID;
    }

    public void setReqCategoryID(String reqCategoryID) {
        ReqCategoryID = reqCategoryID;
    }

    public String getReqOutTime() {
        return ReqOutTime;
    }

    public void setReqOutTime(String reqOutTime) {
        ReqOutTime = reqOutTime;
    }

    public String getReqTime() {
        return ReqTime;
    }

    public void setReqTime(String reqTime) {
        ReqTime = reqTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPendWithName() {
        return PendWithName;
    }

    public void setPendWithName(String pendWithName) {
        PendWithName = pendWithName;
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

    public String getReqType() {
        return ReqType;
    }

    public void setReqType(String reqType) {
        ReqType = reqType;
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
}
