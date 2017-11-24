package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 19-09-2017.
 */

public class SaveExpenseItem implements Serializable {

    private String ReqID;
    private String Description;
    private String CurrencyCode;
    private int ForEmpID;
    private String EmpName;
    private int ClaimTypeID;
    private int ProjectID;
    private String ProjectName;
    private String ReqStatus;
    private String ApproverName;
    private String ReqRemark;
    private String claimTypeDesc;
    private String totalExpenseClaimedAmount;
    private String netAmount;
    private String ApproverID;
    private ArrayList<DocListModel> DocList;
    private ArrayList<AdvanceListItemModel> AdvanceList;
    private ArrayList<RequestRemarksItem> RequestRemarks;
    private ArrayList<LineItemsModel> LineItems;
    private ArrayList<ExpensePaymentDetailsItem> PaymentDetails;
    private DocValidationModel DocValidation;
    private AdvanceAdjustmentResponseModel advanceAdjustmentResponseModel;

    public ArrayList<ExpensePaymentDetailsItem> getPaymentDetails() {
        return PaymentDetails;
    }

    public void setPaymentDetails(ArrayList<ExpensePaymentDetailsItem> paymentDetails) {
        PaymentDetails = paymentDetails;
    }

    public ArrayList<RequestRemarksItem> getRequestRemarks() {
        return RequestRemarks;
    }

    public void setRequestRemarks(ArrayList<RequestRemarksItem> requestRemarks) {
        RequestRemarks = requestRemarks;
    }

    public AdvanceAdjustmentResponseModel getAdvanceAdjustmentResponseModel() {
        return advanceAdjustmentResponseModel;
    }

    public void setAdvanceAdjustmentResponseModel(AdvanceAdjustmentResponseModel advanceAdjustmentResponseModel) {
        this.advanceAdjustmentResponseModel = advanceAdjustmentResponseModel;
    }

    public DocValidationModel getDocValidation() {
        return DocValidation;
    }

    public void setDocValidation(DocValidationModel docValidation) {
        DocValidation = docValidation;
    }

    public String getTotalExpenseClaimedAmount() {
        return totalExpenseClaimedAmount;
    }

    public void setTotalExpenseClaimedAmount(String totalExpenseClaimedAmount) {
        this.totalExpenseClaimedAmount = totalExpenseClaimedAmount;
    }

    public String getReqStatus() {
        return ReqStatus;
    }

    public void setReqStatus(String reqStatus) {
        ReqStatus = reqStatus;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getApproverID() {
        return ApproverID;
    }

    public void setApproverID(String approverID) {
        ApproverID = approverID;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getApproverName() {
        return ApproverName;
    }

    public void setApproverName(String approverName) {
        ApproverName = approverName;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getClaimTypeDesc() {
        return claimTypeDesc;
    }

    public void setClaimTypeDesc(String claimTypeDesc) {
        this.claimTypeDesc = claimTypeDesc;
    }

    public ArrayList<DocListModel> getDocList() {
        return DocList;
    }

    public void setDocList(ArrayList<DocListModel> docList) {
        DocList = docList;
    }

    public String getReqRemark() {
        return ReqRemark;
    }

    public void setReqRemark(String reqRemark) {
        ReqRemark = reqRemark;
    }

    public int getClaimTypeID() {
        return ClaimTypeID;
    }

    public void setClaimTypeID(int claimTypeID) {
        ClaimTypeID = claimTypeID;
    }

    public int getProjectID() {
        return ProjectID;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public ArrayList<AdvanceListItemModel> getAdvanceList() {
        return AdvanceList;
    }

    public void setAdvanceList(ArrayList<AdvanceListItemModel> advanceList) {
        AdvanceList = advanceList;
    }

    public ArrayList<LineItemsModel> getLineItems() {
        return LineItems;
    }

    public void setLineItems(ArrayList<LineItemsModel> lineItems) {
        LineItems = lineItems;
    }

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    static public SaveExpenseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SaveExpenseItem.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
