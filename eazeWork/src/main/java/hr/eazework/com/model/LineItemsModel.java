package hr.eazework.com.model;

import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class LineItemsModel extends GenericResponse implements Serializable {
    private GetHeadDetailsWithPolicyResultModel resultModel;
    private int i;
    private String Amount2;
    private String AmtApprovedLevel1;
    private String AmtApprovedLevel2;
    private String AmtVerified;
    private String CAID;
    private int CategoryID;
    private String CategoryDesc;
    private String ClaimAmt;
    private String DateFrom;
    private String DateTo;
    private String ExceptionYN;
    private String Flag;
    private String HeadID;
    private String InputUnit;
    private String LimitedTo;
    private String LineItemDetail;
    private int LineItemID ;
    private String PolicyCurrencyCode;
    private String PolicyID;
    private String PolicyLimitValue;
    private String PolicyStatus;
    private String PolicyStatusDesc;
    private String PolicyUnitValue;
    private String HeadDesc;
    private String LabelInput;
    private String LabelAmount;
    private String LabelPeriod;
    private String LabelRight;
    private String AmtApproved;
    private String unit;
    private String PolicyUnitDesc;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public static  String DATE_FROM_TAG="DateFrom";
    public static  String DATE_TO_TAG="DateTo";
    public static  String CLAIM_AMT_TAG="ClaimAmt";
    public static  String CLAIM_HEAD_TAG="HeadDesc";
    public static  String INPUT_UNIT_TAG="InputUnit";
    public  static String LINE_ITEM_DETAIL_LABEL="LineItemDetail";

    public GetHeadDetailsWithPolicyResultModel getResultModel() {
        return resultModel;
    }

    public void setResultModel(GetHeadDetailsWithPolicyResultModel resultModel) {
        this.resultModel = resultModel;
    }

    public String getPolicyUnitDesc() {
        return PolicyUnitDesc;
    }

    public void setPolicyUnitDesc(String policyUnitDesc) {
        PolicyUnitDesc = policyUnitDesc;
    }

    private ArrayList<SupportDocsItemModel> DocListLineItem;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<SupportDocsItemModel> getDocListLineItem() {
        return DocListLineItem;
    }

    public void setDocListLineItem(ArrayList<SupportDocsItemModel> docListLineItem) {
        DocListLineItem = docListLineItem;
    }

    public String getAmtApproved() {
        return AmtApproved;
    }

    public void setAmtApproved(String amtApproved) {
        AmtApproved = amtApproved;
    }

    public String getLabelInput() {
        return LabelInput;
    }

    public void setLabelInput(String labelInput) {
        LabelInput = labelInput;
    }

    public String getLabelAmount() {
        return LabelAmount;
    }

    public void setLabelAmount(String labelAmount) {
        LabelAmount = labelAmount;
    }

    public String getLabelPeriod() {
        return LabelPeriod;
    }

    public void setLabelPeriod(String labelPeriod) {
        LabelPeriod = labelPeriod;
    }

    public String getLabelRight() {
        return LabelRight;
    }

    public void setLabelRight(String labelRight) {
        LabelRight = labelRight;
    }

    public String getCategoryDesc() {
        return CategoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        CategoryDesc = categoryDesc;
    }

    public String getHeadDesc() {
        return HeadDesc;
    }

    public void setHeadDesc(String headDesc) {
        HeadDesc = headDesc;
    }

    public String getAmount2() {
        return Amount2;
    }

    public void setAmount2(String amount2) {
        Amount2 = amount2;
    }

    public String getAmtApprovedLevel1() {
        return AmtApprovedLevel1;
    }

    public void setAmtApprovedLevel1(String amtApprovedLevel1) {
        AmtApprovedLevel1 = amtApprovedLevel1;
    }

    public String getAmtApprovedLevel2() {
        return AmtApprovedLevel2;
    }

    public void setAmtApprovedLevel2(String amtApprovedLevel2) {
        AmtApprovedLevel2 = amtApprovedLevel2;
    }

    public String getAmtVerified() {
        return AmtVerified;
    }

    public void setAmtVerified(String amtVerified) {
        AmtVerified = amtVerified;
    }

    public String getCAID() {
        return CAID;
    }

    public void setCAID(String CAID) {
        this.CAID = CAID;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getClaimAmt() {
        return ClaimAmt;
    }

    public void setClaimAmt(String claimAmt) {
        ClaimAmt = claimAmt;
    }

    public String getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(String dateFrom) {
        DateFrom = dateFrom;
    }

    public String getDateTo() {
        return DateTo;
    }

    public void setDateTo(String dateTo) {
        DateTo = dateTo;
    }

    public String getExceptionYN() {
        return ExceptionYN;
    }

    public void setExceptionYN(String exceptionYN) {
        ExceptionYN = exceptionYN;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getHeadID() {
        return HeadID;
    }

    public void setHeadID(String headID) {
        HeadID = headID;
    }

    public String getInputUnit() {
        return InputUnit;
    }

    public void setInputUnit(String inputUnit) {
        InputUnit = inputUnit;
    }

    public String getLimitedTo() {
        return LimitedTo;
    }

    public void setLimitedTo(String limitedTo) {
        LimitedTo = limitedTo;
    }

    public String getLineItemDetail() {
        return LineItemDetail;
    }

    public void setLineItemDetail(String lineItemDetail) {
        LineItemDetail = lineItemDetail;
    }

    public int getLineItemID() {
        return LineItemID;
    }

    public void setLineItemID(int lineItemID) {
        LineItemID = lineItemID;
    }

    public String getPolicyCurrencyCode() {
        return PolicyCurrencyCode;
    }

    public void setPolicyCurrencyCode(String policyCurrencyCode) {
        PolicyCurrencyCode = policyCurrencyCode;
    }

    public String getPolicyID() {
        return PolicyID;
    }

    public void setPolicyID(String policyID) {
        PolicyID = policyID;
    }

    public String getPolicyLimitValue() {
        return PolicyLimitValue;
    }

    public void setPolicyLimitValue(String policyLimitValue) {
        PolicyLimitValue = policyLimitValue;
    }

    public String getPolicyStatus() {
        return PolicyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        PolicyStatus = policyStatus;
    }

    public String getPolicyStatusDesc() {
        return PolicyStatusDesc;
    }

    public void setPolicyStatusDesc(String policyStatusDesc) {
        PolicyStatusDesc = policyStatusDesc;
    }

    public String getPolicyUnitValue() {
        return PolicyUnitValue;
    }

    public void setPolicyUnitValue(String policyUnitValue) {
        PolicyUnitValue = policyUnitValue;
    }

    static public LineItemsModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LineItemsModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
