package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 10-01-2018.
 */

public class WFHRequestDetailModel implements Serializable {
    private int ApprovalLevel;
    private String ReqID;
    private String ForEmpID;
    private String StartDate;
    private String EndDate;
    private String Remark;
    private PartialDayModel PartialDay;
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

    public PartialDayModel getPartialDay() {
        return PartialDay;
    }

    public void setPartialDay(PartialDayModel partialDay) {
        PartialDay = partialDay;
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

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getRemarks() {
        return Remark;
    }

    public void setRemarks(String remarks) {
        Remark = remarks;
    }
}
