package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class ExpenseItemListModel extends GenericResponse implements Serializable {
    private String AdvanceList;
    private int ApproverID;
    private int ClaimTypeID ;
    private String CurrencyCode;
    private String Description;
    private String DocList;
    private String DocListLineItem;
    private String EmpCode;
    private int ForEmpID ;
    private String LineItems;
    private String Name;
    private int PendEmpID;
    private String PendingWith;
    private int ProjectID ;
    private String ReqCode;
    private String ReqDate;
    private int ReqID ;
    private int ReqStatus;
    private String ReqStatusDesc;
    private String ShowProject;
    private String VerifierID;
    private String[] buttons;

    public String[] getButtons() {
        return buttons;
    }

    public void setButtons(String[] buttons) {
        this.buttons = buttons;
    }

    public String getAdvanceList() {
        return AdvanceList;
    }

    public void setAdvanceList(String advanceList) {
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

    public String getDocList() {
        return DocList;
    }

    public void setDocList(String docList) {
        DocList = docList;
    }

    public String getDocListLineItem() {
        return DocListLineItem;
    }

    public void setDocListLineItem(String docListLineItem) {
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

    public String getLineItems() {
        return LineItems;
    }

    public void setLineItems(String lineItems) {
        LineItems = lineItems;
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

    public String getVerifierID() {
        return VerifierID;
    }

    public void setVerifierID(String verifierID) {
        VerifierID = verifierID;
    }

    static public ExpenseItemListModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ExpenseItemListModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
