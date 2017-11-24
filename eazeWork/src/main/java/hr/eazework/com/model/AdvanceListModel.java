package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 29-08-2017.
 */

public class AdvanceListModel extends GenericResponse implements Serializable {

    private int AdvanceID;
    private String ApprovalLevel;
    private String ApprovedAmount;
    private String BalAmount;
    private String CurrencyCode;
    private int EmpAdvReqStatus;
    private String FinalApprover;
    private int ForEmpID;
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
    private int ReqStatus;
    private int Source;
    private String StatusDesc;

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

    public int getEmpAdvReqStatus() {
        return EmpAdvReqStatus;
    }

    public void setEmpAdvReqStatus(int empAdvReqStatus) {
        EmpAdvReqStatus = empAdvReqStatus;
    }

    public String getFinalApprover() {
        return FinalApprover;
    }

    public void setFinalApprover(String finalApprover) {
        FinalApprover = finalApprover;
    }

    public int getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
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

    public int getReqStatus() {
        return ReqStatus;
    }

    public void setReqStatus(int reqStatus) {
        ReqStatus = reqStatus;
    }

    public int getSource() {
        return Source;
    }

    public void setSource(int source) {
        Source = source;
    }

    public String getStatusDesc() {
        return StatusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        StatusDesc = statusDesc;
    }

    static public AdvanceListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
