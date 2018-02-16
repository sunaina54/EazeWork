package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;

import hr.eazework.com.model.MainItemModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.PendingCountModel;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.MainProfileItemListAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Manjunath on 02-04-2017.
 */

public class ApproveScreen extends BaseFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ApproveScreen";
    private MainProfileItemListAdapter adapter;
    ArrayList<MainItemModel> itemList;
    private ListView listView;

    private void requestApi() {
        // to check count and display
        MainActivity.isAnimationLoaded = false;
        CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getLocationList(), CommunicationConstant.API_GET_APPROVALS_COUNT, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.user_profile_root_container, container, false);
        populateHomeData();
        android.widget.FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        rootView.findViewById(R.id.ll_main_sub_layout_container).setLayoutParams(params);
        rootView.findViewById(R.id.img_material_icon).setVisibility(View.INVISIBLE);
        listView = (ListView) rootView.findViewById(R.id.list_profile_items);
        adapter = new MainProfileItemListAdapter(getActivity(), itemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        requestApi();
        return rootView;
    }

    private void populateHomeData() {
        if (itemList == null) {
            itemList = new ArrayList<MainItemModel>();
        } else {
            itemList.clear();
        }

        ModelManager modelManager = ModelManager.getInstance();
        MenuItemModel menuItemModel = modelManager.getMenuItemModel();
        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.APPROVAL_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                PendingCountModel model = modelManager.getPendingCountModel();
                String count = "";
                if (model.getPendingList() != null) {
                    for (int i = 0; i < model.getPendingList().size(); i++) {
                        PendingCountModel countModel = model.getPendingList().get(i);
                        if (countModel.getmReqDesc().equalsIgnoreCase("Leave")) {
                            count = countModel.getmCount();
                        }
                    }
                }
                itemList.add(new MainItemModel(getString(R.string.msg_leaves), "", getString(R.string.msg_pending), "" + count, R.drawable.manager_approval));
            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.APPROVAL_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                PendingCountModel model = modelManager.getPendingCountModel();
                String count = "";
                if (model.getPendingList() != null) {
                    for (int i = 0; i < model.getPendingList().size(); i++) {
                        PendingCountModel countModel = model.getPendingList().get(i);
                        if (countModel.getmReqDesc().equalsIgnoreCase("Attendance")) {
                            count = countModel.getmCount();
                        }
                    }
                }
                itemList.add(new MainItemModel("Attendance", "", "Pending", "" + count, R.drawable.manager_approval));
            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.EMPLOYEE_APPROVAL_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                PendingCountModel model = modelManager.getPendingCountModel();
                String count = "";
                if (model.getPendingList() != null) {
                    for (int i = 0; i < model.getPendingList().size(); i++) {
                        PendingCountModel countModel = model.getPendingList().get(i);
                        if (countModel.getmReqDesc().equalsIgnoreCase("Employee")) {
                            count = countModel.getmCount();
                        }
                    }
                }
                itemList.add(new MainItemModel("Employee", "", "Pending", "" + count, R.drawable.team_blue));
            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.EXPENSE_KEY);
            if(itemModel!=null && itemModel.isAccess()){
                PendingCountModel model = modelManager.getPendingCountModel();
                String count = "";
                if (model.getPendingList() != null) {
                    for (int i = 0; i < model.getPendingList().size(); i++) {
                        PendingCountModel countModel = model.getPendingList().get(i);
                        if (countModel.getmReqDesc().equalsIgnoreCase("Expense")) {
                            count = countModel.getmCount();
                        }
                    }
                }
                itemList.add(new MainItemModel("Expense", "","Pending","" + count, R.drawable.expense_claim));
            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.ADVANCE_KEY);
            if(itemModel!=null && itemModel.isAccess()){
                PendingCountModel model = modelManager.getPendingCountModel();
                String count = "";
                if (model.getPendingList() != null) {
                    for (int i = 0; i < model.getPendingList().size(); i++) {
                        PendingCountModel countModel = model.getPendingList().get(i);
                        if (countModel.getmReqDesc().equalsIgnoreCase("Advance")) {
                            count = countModel.getmCount();
                        }
                    }
                }
                itemList.add(new MainItemModel("Advance", "","Pending","" + count, R.drawable.advance_expense));
            }
            if (adapter != null) {
                adapter.updateData(itemList);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (itemList.get(position).getmLeftTitle().equalsIgnoreCase("Employee")) {
            mUserActionListener.performUserAction(IAction.MEMBER_APPROVAL, null, null);
        } else if (itemList.get(position).getmLeftTitle().equalsIgnoreCase("Leave")) {
            mUserActionListener.performUserAction(IAction.PENDING_APPROVAL, null, null);
        }else if (itemList.get(position).getmLeftTitle().equalsIgnoreCase("Attendance")) {
            mUserActionListener.performUserAction(IAction.ATTENDANCE, null, null);
        }else if (itemList.get(position).getmLeftTitle().equalsIgnoreCase("Expense")) {
            mUserActionListener.performUserAction(IAction.EXPENSE_APPROVAL, null, null);
        }else if (itemList.get(position).getmLeftTitle().equalsIgnoreCase("Advance")) {
            mUserActionListener.performUserAction(IAction.ADVANCE_APPROVAL, null, null);
        }

    }

    private void populateLeaves() {
        ModelManager modelManager = ModelManager.getInstance();
        PendingCountModel model = modelManager.getPendingCountModel();
        MenuItemModel menuItemModel = modelManager.getMenuItemModel();
        MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.EMPLOYEE_APPROVAL_KEY);

        if (model != null) {
            ArrayList<PendingCountModel> list = model.getPendingList();
            String empCount = "";
            if (model.getmCounts() == null) {
                empCount = model.getmCounts().get("Employee");
            }

            if (list != null && list.size() > 0) {
                for (MainItemModel m : itemList) {
                    if (m.getmLeftTitle().equalsIgnoreCase(itemModel.getmObjectDesc())) {
                        m.setmRightSubTitle(empCount);
                    }
                }
                adapter.updateData(itemList);
            }

        }
    }

    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        super.validateResponse(response);
        try {
            JSONObject json = new JSONObject(response.getResponseData());
            JSONObject mainJSONObject = json.optJSONObject("GetEmpPendingApprovalCountResult");
            PendingCountModel employeeLeaveModel = new PendingCountModel(mainJSONObject.optJSONArray("list"));
            employeeLeaveModel.setmPendingCount(mainJSONObject.optString("PendingCount", ""));
            ModelManager.getInstance().setPendingCountModel(employeeLeaveModel);
            populateHomeData();
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
            Crashlytics.logException(e);
        }
    }
}
