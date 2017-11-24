package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 06-09-2017.
 */

public class GetExpensePageInitResultModel extends GenericResponse implements Serializable {
    private String AdvanceProcessYN;
    private String OnBehalfOfYN;
    private String TopLabel;
    private String[] Buttons;
    private ApproverItemModel Approver;
    private ClaimTypeListModel ClaimTypeList;
    private CurrencyListModel CurrencyList;
    private ViewGetExpenseDetailResultModel ExpenseDetail;
    private OnBehalfOfListModel OnBehalfOfList;
    private String ClaimTypeYN;
    private DocValidationModel DocValidation;
    private ArrayList<CategoryLineItemLabelItem> categoryLineItemLabel;

    public ArrayList<CategoryLineItemLabelItem> getCategoryLineItemLabel() {
        return categoryLineItemLabel;
    }

    public void setCategoryLineItemLabel(ArrayList<CategoryLineItemLabelItem> categoryLineItemLabel) {
        this.categoryLineItemLabel = categoryLineItemLabel;
    }

    public DocValidationModel getDocValidation() {
        return DocValidation;
    }

    public void setDocValidation(DocValidationModel docValidation) {
        DocValidation = docValidation;
    }

    public String getClaimTypeYN() {
        return ClaimTypeYN;
    }

    public void setClaimTypeYN(String claimTypeYN) {
        ClaimTypeYN = claimTypeYN;
    }

    public ApproverItemModel getApprover() {
        return Approver;
    }

    public void setApprover(ApproverItemModel approver) {
        Approver = approver;
    }

    public ClaimTypeListModel getClaimTypeList() {
        return ClaimTypeList;
    }

    public void setClaimTypeList(ClaimTypeListModel claimTypeList) {
        ClaimTypeList = claimTypeList;
    }

    public CurrencyListModel getCurrencyList() {
        return CurrencyList;
    }

    public void setCurrencyList(CurrencyListModel currencyList) {
        CurrencyList = currencyList;
    }

    public ViewGetExpenseDetailResultModel getExpenseDetail() {
        return ExpenseDetail;
    }

    public void setExpenseDetail(ViewGetExpenseDetailResultModel expenseDetail) {
        ExpenseDetail = expenseDetail;
    }

    public OnBehalfOfListModel getOnBehalfOfList() {
        return OnBehalfOfList;
    }

    public void setOnBehalfOfList(OnBehalfOfListModel onBehalfOfList) {
        OnBehalfOfList = onBehalfOfList;
    }

    public String getAdvanceProcessYN() {
        return AdvanceProcessYN;
    }

    public void setAdvanceProcessYN(String advanceProcessYN) {
        AdvanceProcessYN = advanceProcessYN;
    }

    public String getOnBehalfOfYN() {
        return OnBehalfOfYN;
    }

    public void setOnBehalfOfYN(String onBehalfOfYN) {
        OnBehalfOfYN = onBehalfOfYN;
    }

    public String getTopLabel() {
        return TopLabel;
    }

    public void setTopLabel(String topLabel) {
        TopLabel = topLabel;
    }

    public String[] getButtons() {
        return Buttons;
    }

    public void setButtons(String[] buttons) {
        Buttons = buttons;
    }

    static public GetExpensePageInitResultModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetExpensePageInitResultModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
