package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-02-2018.
 */

public class AttendanceItem implements Serializable {
    private String [] Buttons;
    private String ApprovalLevel;
    private String Details;
    private String ForEmpID;
    private String ForEmpName;
    private String Remark;
    private String ReqCode;
    private String ReqID;
    private String ReqType;
    private String ReqTypeDesc;
    private String Status;

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

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(String forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getForEmpName() {
        return ForEmpName;
    }

    public void setForEmpName(String forEmpName) {
        ForEmpName = forEmpName;
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

    public String getReqTypeDesc() {
        return ReqTypeDesc;
    }

    public void setReqTypeDesc(String reqTypeDesc) {
        ReqTypeDesc = reqTypeDesc;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
