package hr.eazework.com.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.eazework.com.R;
import hr.eazework.com.model.LeaveModel;

@SuppressLint("ValidFragment")
public class LeavePagerFragment extends BaseFragment {
    private LeaveModel leaveModel;

    @SuppressLint("ValidFragment")
    public LeavePagerFragment(LeaveModel model) {
        leaveModel = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.leave_pager_item_layout, container, false);
        ((TextView) rootView.findViewById(R.id.tv_leave_count)).setText(String.format("%2.1f", leaveModel.getmLeaveBalance()));
        ((TextView) rootView.findViewById(R.id.tv_leave_type)).setText("" + leaveModel.getmLeaveName());
        ((TextView) rootView.findViewById(R.id.tv_apply_current_leave)).setText(String.format(getString(R.string.msg_apply_leave_format), "" + leaveModel.getmLeaveName()));
        return rootView;
    }
}
