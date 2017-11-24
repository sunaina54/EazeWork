package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 30-10-2017.
 */

public class ExpenseStatusData implements Serializable{
    private String Amount;
    private String CurrencyCode;
    private String Label;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }
}
