package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 22-01-2018.
 */

public class TourSummaryRequestDetail implements Serializable {
    private String ApprovalLevel;
    private String Button;
    private String[] Buttons;
    private String ForEmpID ;
    private String ForEmpName;
    private String PendEmpID;
    private String PendWithName;
    private String Remark;
    private String ReqCode;
    private String ReqID;
    private String ReqStatus ;
    private String StatusDesc;
    private String SubmittedBy;
    private String AccomodationDet;
    private String AccomodationYN;
    private String Description;
    private String EndDate;
    private String StartDate;
    private String TicketDet;
    private TourReasonListModel TourReason;
    private String TicketYN;
    private String TotalDays;
    private String TravelFrom;
    private String TravelTo;
    private ArrayList<SupportDocsItemModel> Attachments;
    private ArrayList<CustomFieldsModel> CustomFields;
    private ArrayList<RemarkListItem> RemarkList;

    public String getTotalDays() {
        return TotalDays;
    }

    public void setTotalDays(String totalDays) {
        TotalDays = totalDays;
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

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
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

    public String getPendEmpID() {
        return PendEmpID;
    }

    public void setPendEmpID(String pendEmpID) {
        PendEmpID = pendEmpID;
    }

    public String getPendWithName() {
        return PendWithName;
    }

    public void setPendWithName(String pendWithName) {
        PendWithName = pendWithName;
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

    public String getReqStatus() {
        return ReqStatus;
    }

    public void setReqStatus(String reqStatus) {
        ReqStatus = reqStatus;
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

    public String getAccomodationDet() {
        return AccomodationDet;
    }

    public void setAccomodationDet(String accomodationDet) {
        AccomodationDet = accomodationDet;
    }

    public String getAccomodationYN() {
        return AccomodationYN;
    }

    public void setAccomodationYN(String accomodationYN) {
        AccomodationYN = accomodationYN;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getTicketDet() {
        return TicketDet;
    }

    public void setTicketDet(String ticketDet) {
        TicketDet = ticketDet;
    }

    public String getTicketYN() {
        return TicketYN;
    }

    public void setTicketYN(String ticketYN) {
        TicketYN = ticketYN;
    }

    public TourReasonListModel getTourReason() {
        return TourReason;
    }

    public void setTourReason(TourReasonListModel tourReason) {
        TourReason = tourReason;
    }

    public String getTravelFrom() {
        return TravelFrom;
    }

    public void setTravelFrom(String travelFrom) {
        TravelFrom = travelFrom;
    }

    public String getTravelTo() {
        return TravelTo;
    }

    public void setTravelTo(String travelTo) {
        TravelTo = travelTo;
    }

    public ArrayList<SupportDocsItemModel> getAttachments() {
        return Attachments;
    }

    public void setAttachments(ArrayList<SupportDocsItemModel> attachments) {
        Attachments = attachments;
    }

    public ArrayList<CustomFieldsModel> getCustomFields() {
        return CustomFields;
    }

    public void setCustomFields(ArrayList<CustomFieldsModel> customFields) {
        CustomFields = customFields;
    }

    public ArrayList<RemarkListItem> getRemarkList() {
        return RemarkList;
    }

    public void setRemarkList(ArrayList<RemarkListItem> remarkList) {
        RemarkList = remarkList;
    }
}
