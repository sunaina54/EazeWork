package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 20-11-2017.
 */

public class ExpensePaymentDetailsItem implements Serializable {
    private String Amount;
    private String ConversionRate;
    private String CurrencyCode;
    private String PymtDate;
    private String PymtDetails;
    private String PymtModeDesc;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getConversionRate() {
        return ConversionRate;
    }

    public void setConversionRate(String conversionRate) {
        ConversionRate = conversionRate;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getPymtDate() {
        return PymtDate;
    }

    public void setPymtDate(String pymtDate) {
        PymtDate = pymtDate;
    }

    public String getPymtDetails() {
        return PymtDetails;
    }

    public void setPymtDetails(String pymtDetails) {
        PymtDetails = pymtDetails;
    }

    public String getPymtModeDesc() {
        return PymtModeDesc;
    }

    public void setPymtModeDesc(String pymtModeDesc) {
        PymtModeDesc = pymtModeDesc;
    }
}
