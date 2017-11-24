package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 16-08-2017.
 */

public class ExpenseDetailItemModel implements Serializable {
    private String expense;
    private String detail;
    private String date;
    private String policy;
    private String amount;

    public ExpenseDetailItemModel(String expense, String detail, String date, String policy, String amount) {
        this.expense = expense;
        this.detail = detail;
        this.date = date;
        this.policy = policy;
        this.amount = amount;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
