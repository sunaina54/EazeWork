package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class ViewExpenseItemModel extends GenericResponse implements Serializable {
    private ArrayList<AdvanceListItemModel> AdvanceList ;
    private int ApproverID ;
    private int ClaimTypeID ;
    private String ClaimTypeDesc;
    private String CurrencyCode;
    private String Description;
    private ArrayList<DocListModel> DocList ;
    private String[] DocListLineItem ;
    private String EmpCode;
    private int ForEmpID ;
    private String Name;
    private int PendEmpID ;
    private String PendingWith;
    private int ProjectID ;
    private String ReqCode;
    private String ReqDate;
    private String ProjectName;
    private int ReqID ;
    private int ReqStatus ;
    private String ReqStatusDesc;
    private String ShowProject;
    private String ApproverName;
    private int VerifierID ;
    private String TotalExpenseClaimed;
    private String NetAmountToBePaid;
    private int SubmitteByEmpID;
    private String SubmittedByName;
    private String ReqRemark;
    private ArrayList<LineItemsModel> LineItems;
    private ArrayList<RequestRemarksItem> RequestRemarks;
    private ArrayList<ExpensePaymentDetailsItem> PaymentDetails;

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

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getApproverName() {
        return ApproverName;
    }

    public void setApproverName(String approverName) {
        ApproverName = approverName;
    }

    public String getReqRemark() {
        return ReqRemark;
    }

    public void setReqRemark(String reqRemark) {
        ReqRemark = reqRemark;
    }

    public int getSubmitteByEmpID() {
        return SubmitteByEmpID;
    }

    public void setSubmitteByEmpID(int submitteByEmpID) {
        SubmitteByEmpID = submitteByEmpID;
    }

    public String getSubmittedByName() {
        return SubmittedByName;
    }

    public void setSubmittedByName(String submittedByName) {
        SubmittedByName = submittedByName;
    }

    public String getClaimTypeDesc() {
        return ClaimTypeDesc;
    }

    public void setClaimTypeDesc(String claimTypeDesc) {
        ClaimTypeDesc = claimTypeDesc;
    }

    public String getTotalExpenseClaimed() {
        return TotalExpenseClaimed;
    }

    public void setTotalExpenseClaimed(String totalExpenseClaimed) {
        TotalExpenseClaimed = totalExpenseClaimed;
    }

    public String getNetAmountToBePaid() {
        return NetAmountToBePaid;
    }

    public void setNetAmountToBePaid(String netAmountToBePaid) {
        NetAmountToBePaid = netAmountToBePaid;
    }

    public ArrayList<AdvanceListItemModel> getAdvanceList() {
        return AdvanceList;
    }

    public void setAdvanceList(ArrayList<AdvanceListItemModel> advanceList) {
        AdvanceList = advanceList;
    }

    public int getApproverID() {
        return ApproverID;
    }

    public void setApproverID(int approverID) {
        ApproverID = approverID;
    }

    public int getClaimTypeID() {
        return ClaimTypeID;
    }

    public void setClaimTypeID(int claimTypeID) {
        ClaimTypeID = claimTypeID;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<DocListModel> getDocList() {
        return DocList;
    }

    public void setDocList(ArrayList<DocListModel> docList) {
        DocList = docList;
    }

    public String[] getDocListLineItem() {
        return DocListLineItem;
    }

    public void setDocListLineItem(String[] docListLineItem) {
        DocListLineItem = docListLineItem;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public int getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPendEmpID() {
        return PendEmpID;
    }

    public void setPendEmpID(int pendEmpID) {
        PendEmpID = pendEmpID;
    }

    public String getPendingWith() {
        return PendingWith;
    }

    public void setPendingWith(String pendingWith) {
        PendingWith = pendingWith;
    }

    public int getProjectID() {
        return ProjectID;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
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

    public String getReqStatusDesc() {
        return ReqStatusDesc;
    }

    public void setReqStatusDesc(String reqStatusDesc) {
        ReqStatusDesc = reqStatusDesc;
    }

    public String getShowProject() {
        return ShowProject;
    }

    public void setShowProject(String showProject) {
        ShowProject = showProject;
    }

    public int getVerifierID() {
        return VerifierID;
    }

    public void setVerifierID(int verifierID) {
        VerifierID = verifierID;
    }

    public ArrayList<LineItemsModel> getLineItems() {
        return LineItems;
    }

    public void setLineItems(ArrayList<LineItemsModel> lineItems) {
        LineItems = lineItems;
    }

    static public ViewExpenseItemModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ViewExpenseItemModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
