package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 30-08-2017.
 */

public class GetAdvanceDetailResultModel extends GenericResponse implements Serializable {
    private int AdvanceID;
    private String ApprovalLevel;
    private String ApprovedAmount;
    private String BalAmount;
    private String PaidAmount;
    private String TotalAmount;
    private String CurrencyCode;
    private String EmpAdvReqStatus;
    private String FinalApprover;
    private String ForEmpID;
    private String FromButton;
    private String HideYN;
    private String Name;
    private String PendWith;
    private String Reason;
    private String ReasonCode;
    private String Remarks;
    private String ReqAmount;
    private String ReqCode;
    private String ReqDate;
    private int ReqID;
    private String ReqStatus;
    private String Source;
    private String StatusDesc;
    private ArrayList<RequestRemarksItem> RequestRemarks;
    private ArrayList<PaymentDetailsItem> PaymentDetails;
    private ArrayList<AdjustmentItem> Adjustments;
    private ArrayList<SupportDocsItemModel> SupportDocs;

    public ArrayList<RequestRemarksItem> getRequestRemarks() {
        return RequestRemarks;
    }

    public void setRequestRemarks(ArrayList<RequestRemarksItem> requestRemarks) {
        RequestRemarks = requestRemarks;
    }

    public ArrayList<PaymentDetailsItem> getPaymentDetails() {
        return PaymentDetails;
    }

    public void setPaymentDetails(ArrayList<PaymentDetailsItem> paymentDetails) {
        PaymentDetails = paymentDetails;
    }

    public ArrayList<AdjustmentItem> getAdjustments() {
        return Adjustments;
    }

    public void setAdjustments(ArrayList<AdjustmentItem> adjustments) {
        Adjustments = adjustments;
    }

    public ArrayList<SupportDocsItemModel> getSupportDocs() {
        return SupportDocs;
    }

    public void setSupportDocs(ArrayList<SupportDocsItemModel> supportDocs) {
        SupportDocs = supportDocs;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getAdvanceID() {
        return AdvanceID;
    }

    public void setAdvanceID(int advanceID) {
        AdvanceID = advanceID;
    }

    public String getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getApprovedAmount() {
        return ApprovedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        ApprovedAmount = approvedAmount;
    }

    public String getBalAmount() {
        return BalAmount;
    }

    public void setBalAmount(String balAmount) {
        BalAmount = balAmount;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getEmpAdvReqStatus() {
        return EmpAdvReqStatus;
    }

    public void setEmpAdvReqStatus(String empAdvReqStatus) {
        EmpAdvReqStatus = empAdvReqStatus;
    }

    public String getFinalApprover() {
        return FinalApprover;
    }

    public void setFinalApprover(String finalApprover) {
        FinalApprover = finalApprover;
    }

    public String getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(String forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getFromButton() {
        return FromButton;
    }

    public void setFromButton(String fromButton) {
        FromButton = fromButton;
    }

    public String getHideYN() {
        return HideYN;
    }

    public void setHideYN(String hideYN) {
        HideYN = hideYN;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPendWith() {
        return PendWith;
    }

    public void setPendWith(String pendWith) {
        PendWith = pendWith;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getReqAmount() {
        return ReqAmount;
    }

    public void setReqAmount(String reqAmount) {
        ReqAmount = reqAmount;
    }

    public String getReqCode() {
        return ReqCode;
    }

    public void setReqCode(String reqCode) {
        ReqCode = reqCode;
    }

    public String getReqDate() {
        return ReqDate;
    }

    public void setReqDate(String reqDate) {
        ReqDate = reqDate;
    }

    public int getReqID() {
        return ReqID;
    }

    public void setReqID(int reqID) {
        ReqID = reqID;
    }

    public String getReqStatus() {
        return ReqStatus;
    }

    public void setReqStatus(String reqStatus) {
        ReqStatus = reqStatus;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        PaidAmount = paidAmount;
    }

    static public GetAdvanceDetailResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetAdvanceDetailResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
