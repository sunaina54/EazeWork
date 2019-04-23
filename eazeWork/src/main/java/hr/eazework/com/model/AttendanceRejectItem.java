package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 08-02-2018.
 */

public class AttendanceRejectItem implements Serializable {
    private String ReqID;
    private String ApprovalLevel;
    private String Status;
    private String Remark;

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
