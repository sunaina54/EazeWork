package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 31-01-2018.
 */

public class TimeModificationItem implements Serializable {
    private String ForEmpID;
    private String AttendID;
    private String BreakID;
    private String ReqTime;
    private String ReqOutTime;
    private String Remark;
    private String ApprovalLevel;
    private String Status;

    public String getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(String forEmpID) {
        ForEmpID = forEmpID;
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

    public String getReqTime() {
        return ReqTime;
    }

    public void setReqTime(String reqTime) {
        ReqTime = reqTime;
    }

    public String getReqOutTime() {
        return ReqOutTime;
    }

    public void setReqOutTime(String reqOutTime) {
        ReqOutTime = reqOutTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
