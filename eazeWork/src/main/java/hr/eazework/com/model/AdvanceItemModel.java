package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 01-09-2017.
 */

public class AdvanceItemModel implements Serializable {
    private String FromButton;
    private String ReqID;
    private String Remarks;
    private String ReqAmount;
    private String CurrencyCode;
    private int ForEmpID;
    private String ReasonCode;
    private String Reason;
    private int Source;
    private String ReqStatus;
    private String ApprovalLevel;
    private ArrayList<SupportDocsItemModel> SupportDocs;

    public ArrayList<SupportDocsItemModel> getSupportDocs() {
        return SupportDocs;
    }

    public void setSupportDocs(ArrayList<SupportDocsItemModel> supportDocs) {
        SupportDocs = supportDocs;
    }

    public String getApprovalLevel() {
        return ApprovalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        ApprovalLevel = approvalLevel;
    }

    public String getFromButton() {
        return FromButton;
    }

    public void setFromButton(String fromButton) {
        FromButton = fromButton;
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

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public int getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public int getSource() {
        return Source;
    }

    public void setSource(int source) {
        Source = source;
    }


    static public AdvanceItemModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceItemModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }


}
