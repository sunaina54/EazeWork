package hr.eazework.com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 19-01-2018.
 */

public class TourReqDetail implements Serializable {
    private int ApprovalLevel;
    private String ReqID;
    private TourReasonListModel TourReason;
    private ArrayList<CustomFieldsModel> CustomFields;
    private ArrayList<SupportDocsItemModel> Attachments;
    private String ForEmpID;
    private String StartDate;
    private String EndDate;
    private String TravelFrom;
    private String TravelTo;
    private String AccomodationYN;
    private String AccomodationDet;
    private String TicketYN;
    private String TicketDet;
    private String Remark;
    private String Button;

    public int getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(int approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

    public TourReasonListModel getTourReason() {
        return TourReason;
    }

    public void setTourReason(TourReasonListModel tourReason) {
        TourReason = tourReason;
    }

    public ArrayList<CustomFieldsModel> getCustomFields() {
        return CustomFields;
    }

    public void setCustomFields(ArrayList<CustomFieldsModel> customFields) {
        CustomFields = customFields;
    }

    public ArrayList<SupportDocsItemModel> getAttachments() {
        return Attachments;
    }

    public void setAttachments(ArrayList<SupportDocsItemModel> attachments) {
        Attachments = attachments;
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

    public String getAccomodationYN() {
        return AccomodationYN;
    }

    public void setAccomodationYN(String accomodationYN) {
        AccomodationYN = accomodationYN;
    }

    public String getAccomodationDet() {
        return AccomodationDet;
    }

    public void setAccomodationDet(String accomodationDet) {
        AccomodationDet = accomodationDet;
    }

    public String getTicketYN() {
        return TicketYN;
    }

    public void setTicketYN(String ticketYN) {
        TicketYN = ticketYN;
    }

    public String getTicketDet() {
        return TicketDet;
    }

    public void setTicketDet(String ticketDet) {
        TicketDet = ticketDet;
    }

    public String getRemarks() {
        return Remark;
    }

    public void setRemarks(String remarks) {
        Remark = remarks;
    }

    public String getButton() {
        return Button;
    }

    public void setButton(String button) {
        Button = button;
    }
}
