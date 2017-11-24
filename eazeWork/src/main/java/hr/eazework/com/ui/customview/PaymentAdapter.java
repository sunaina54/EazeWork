package hr.eazework.com.ui.customview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.BaseActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.ExpensePaymentDetailsItem;
import hr.eazework.com.ui.fragment.ViewExpenseClaimSummaryFragment;

/**
 * Created by Dell3 on 20-11-2017.
 */

public class PaymentAdapter extends
        RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    private ArrayList<ExpensePaymentDetailsItem> dataSet;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView dateTV, modeTV, detailsTV, amountTV;

        public MyViewHolder(View v) {
            super(v);
            dateTV = (TextView) v.findViewById(R.id.dateTV);
            modeTV = (TextView) v.findViewById(R.id.modeTV);
            detailsTV = (TextView) v.findViewById(R.id.detailsTV);
            amountTV = (TextView) v.findViewById(R.id.amountTV);

        }
    }

    public void addAll(List<ExpensePaymentDetailsItem> list) {

        dataSet.addAll(list);
        notifyDataSetChanged();
    }

    public PaymentAdapter(List<ExpensePaymentDetailsItem> data) {
        this.dataSet = (ArrayList<ExpensePaymentDetailsItem>) data;

    }

    @Override
    public PaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_payment_item, parent, false);
        PaymentAdapter.MyViewHolder myViewHolder = new PaymentAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final PaymentAdapter.MyViewHolder holder, final int listPosition) {

        final ExpensePaymentDetailsItem item = dataSet.get(listPosition);
        holder.dateTV.setText(item.getPymtDate());
        holder.modeTV.setText(item.getPymtModeDesc());
        holder.detailsTV.setText(item.getPymtDetails());
        holder.amountTV.setText(item.getAmount());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void clearDataSource() {
        dataSet.clear();
        notifyDataSetChanged();
    }
}
