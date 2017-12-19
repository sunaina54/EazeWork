package hr.eazework.com.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import hr.eazework.com.BuildConfig;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.CheckInOutModel;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.ExpenseStatusData;
import hr.eazework.com.model.ExpenseStatusModel;
import hr.eazework.com.model.GeoCoderModel;
import hr.eazework.com.model.LeaveBalanceModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MainItemModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.PendingCountModel;
import hr.eazework.com.model.SalaryMonthModel;
import hr.eazework.com.model.TeamMember;
import hr.eazework.com.model.TypeWiseListModel;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.AttendanceUtil;
import hr.eazework.com.ui.util.EventDataSource;
import hr.eazework.com.ui.util.GPSTracker;
import hr.eazework.com.ui.util.GeoCoder;
import hr.eazework.com.ui.util.GeoUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.MainProfileItemListAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

import static hr.eazework.com.ui.util.Utility.requestToEnableGPS;
import static hr.eazework.com.ui.util.Utility.saveEmpConfig;


public class HomeFragment extends BaseFragment implements OnItemClickListener, OnRefreshListener {

    public static final String TAG = "HomeFragment";
    ArrayList<MainItemModel> itemList;
    private ListView listView;
    private MainProfileItemListAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private int currentReqType;
    private String latitude = "";
    private String longitude = "";
    private Preferences preferences;
    private boolean isFromSplash = true;
    private boolean isFromLogin = true;


    @Override
    public void refreshUi() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setShowPlusMenu(true);
        this.setShowEditTeamButtons(false);
        showHideProgressView(true);
        MainActivity.isAnimationLoaded = false;
        getHomeData();
        MenuItemModel model = ModelManager.getInstance().getMenuItemModel();
        if (model == null) {
            getMenuData();
        }
    }

    private void getMenuData() {

        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getSallarySlipMonthData(),
                CommunicationConstant.API_GET_MENU_DATA, true);
    }

    private void getHomeData() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getHomeData(),
                CommunicationConstant.API_GET_HOME_DATA, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.home_root_container, container, false);
        if (preferences == null) {
            preferences = new Preferences(getContext());
        }
        if (ModelManager.getInstance().getEmployeeProfileModel() != null) {
            MainActivity.updataProfileData(getActivity(), rootView);
        }


        Log.d("DeviceInfo", Build.VERSION.SDK_INT + " Release " + Build.VERSION.RELEASE + " " + BuildConfig.VERSION_CODE + " " + BuildConfig.VERSION_NAME + " " + Build.MODEL + " " + Build.MANUFACTURER);

        populateHomeData();
        listView = (ListView) rootView.findViewById(R.id.list_profile_items);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        refreshLayout.setOnRefreshListener(this);
        if (Utility.isInclusiveAndAboveMarshmallow()) {
            refreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary_blue_dark, null), getResources().getColor(R.color.accent, null),
                    getResources().getColor(R.color.primary_pink, null), getResources().getColor(R.color.primary_text_grey, null));
        } else {
            refreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary_blue_dark), getResources().getColor(R.color.accent),
                    getResources().getColor(R.color.primary_pink), getResources().getColor(R.color.primary_text_grey));
        }

        mAdapter = new MainProfileItemListAdapter(getContext(), itemList);
        listView.setAdapter(mAdapter);
        rootView.findViewById(R.id.btn_check_in_out).setOnClickListener(this);
        (rootView.findViewById(R.id.btn_check_breack)).setOnClickListener(this);
        listView.setOnItemClickListener(this);
        CheckInOutModel checkInOutModel = ModelManager.getInstance().getCheckInOutModel();

        if (checkInOutModel != null) {
            CheckInOutModel model = checkInOutModel;
            updateInOutData(model.isCheckedIn(), model.isCheckedOut(), model.isBreakIn(), model.isBreakOut());
        }
        hideTimeInButtons();
        if (ModelManager.getInstance().getMenuItemModel() == null) {
            showHideProgressView(true);
        } else {
            showHideProgressView(false);
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                refreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        if (loginUserModel != null) {
            UserModel userModel = loginUserModel.getUserModel();
            if (userModel != null && !TextUtils.isEmpty(userModel.getUserName())) {
                Crashlytics.setUserName("Username : " + userModel.getUserName() + " EmpID : " + userModel.getEmpId());
                preferences.saveString(preferences.USERNAME, userModel.getUserName());
                preferences.commit();
            }
        }


        rootView.findViewById(R.id.ll_main_sub_layout_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
                if (menuItemModel != null) {
                    MenuItemModel model = menuItemModel.getItemModel(MenuItemModel.VIEW_PROFILE_KEY);
                    if (model != null && model.isAccess()) {
                        mUserActionListener.performUserAction(IAction.USER_PROFILE, null, null);
                    }
                }
            }
        });


        return rootView;
    }

    private void updateInitialTimeInOutButton(CheckInOutModel checkInOutModel) {
        if (checkInOutModel.isCheckedIn() && !checkInOutModel.isCheckedOut()) {
            ((TextView) rootView.findViewById(R.id.btn_check_in_out)).setText(getString(R.string.msg_check_out));
        } else {
            ((TextView) rootView.findViewById(R.id.btn_check_in_out)).setText(getString(R.string.msg_check_in));
        }
    }

    private void hideTimeInButtons() {
        rootView.findViewById(R.id.btn_check_in_out).setVisibility(View.GONE);
        rootView.findViewById(R.id.btn_check_breack).setVisibility(View.GONE);
    }

    private void showTimeInButtons() {
        rootView.findViewById(R.id.btn_check_in_out).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.btn_check_breack).setVisibility(View.VISIBLE);
    }


    private void updateInOutData(boolean isCheckedIn, boolean isCheckedOut, boolean isBreakIn, boolean isBreakOut) {
        CheckInOutModel model = ModelManager.getInstance().getCheckInOutModel();
        View checkBreakView = rootView.findViewById(R.id.btn_check_breack);
        View checkInOutButton = rootView.findViewById(R.id.btn_check_in_out);
        TextView checkInOutButtonTextView = (TextView) checkInOutButton;
        if (isCheckedIn && !isCheckedOut) {
            if (model != null && model.isMarkAttandanceEnable()) {
                checkInOutButtonTextView.setText(getString(R.string.msg_check_out));

                if (Utility.isInclusiveAndAboveMarshmallow()) {
                    checkInOutButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_light_blue, null));
                } else {
                    checkInOutButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_light_blue));
                }

                checkInOutButtonTextView.setTag(3);
                checkInOutButton.setVisibility(View.VISIBLE);


                if (model.getAttendanceLevel() == 3) {
                    TextView checkBreakTextView = (TextView) checkBreakView;
                    if (isBreakIn && !isBreakOut) {
                        checkBreakTextView.setText(getString(R.string.msg_break_out));
                        checkBreakView.setTag(2);
                        checkBreakView.setVisibility(View.VISIBLE);
                    } else if (isBreakOut || !isBreakIn) {
                        checkBreakTextView.setText(getString(R.string.msg_break_in));
                        checkBreakView.setTag(1);
                        checkBreakView.setVisibility(View.VISIBLE);
                    }
                } else {
                    checkBreakView.setVisibility(View.GONE);
                }
            } else {
                checkInOutButton.setVisibility(View.GONE);
                checkBreakView.setVisibility(View.GONE);
            }
        } else if (isCheckedOut) {

            if (model != null && model.isMarkAttandanceEnable()) {
                checkBreakView.setVisibility(View.GONE);
                if (model.getAttendanceLevel() == 1) {
                    if (isCheckedIn) {
                        checkInOutButton.setVisibility(View.GONE);
                    } else {
                        checkInOutButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    checkInOutButtonTextView.setText(getString(R.string.msg_cancel_out));
                    if (Utility.isInclusiveAndAboveMarshmallow()) {
                        checkInOutButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_accent, null));
                    } else {
                        checkInOutButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_accent));
                    }
                    checkInOutButton.setTag(4);
                    checkInOutButton.setVisibility(View.VISIBLE);
                }
            } else {
                checkInOutButton.setVisibility(View.GONE);
            }
        } else {
            String buttonText = getString(R.string.msg_check_in);
            if (model.getAttendanceLevel() == 1) {
                buttonText = getString(R.string.msg_mark);
            }
            setDisplayTimeInOutButton(checkBreakView, checkInOutButton, checkInOutButtonTextView, buttonText);

            if (Utility.isInclusiveAndAboveMarshmallow()) {
                checkInOutButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_light_blue, null));
            } else {
                checkInOutButtonTextView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_light_blue));
            }
        }

        for (MainItemModel item : itemList) {
            if (item.getmLeftTitle().equalsIgnoreCase(getString(R.string.msg_attandance))) {
                Calendar calendar = Calendar.getInstance();
                if (model != null && model.isMarkAttandanceEnable()) {

                    if (isCheckedIn && !isCheckedOut) {
                        item.setmRightTitle(getString(R.string.msg_today_in_time));
                        item.setmRightSubTitle(String.format(getString(R.string.in_out_time_format), calendar));
                        item.setmRightSubTitle(model.getCheckInTime());
                    } else if (isCheckedIn && isCheckedOut) {
                        item.setmRightTitle(getString(R.string.msg_today_out_time));
                        item.setmRightSubTitle(String.format(getString(R.string.in_out_time_format), calendar));
                        item.setmRightSubTitle(model.getCheckOutTime());
                    } else {
                        item.setmRightTitle(getString(R.string.msg_today_in_time));
                        item.setmRightSubTitle(isCheckedIn ? ("" + model.getCheckOutTime()) : "--:--");
                    }
                } else {
                    item.setmRightSubTitle("");
                }
            }
        }

        if (!(model != null && model.isMarkAttandanceEnable())) {
            checkBreakView.setVisibility(View.GONE);
            checkInOutButton.setVisibility(View.GONE);
        }
        mAdapter.updateData(itemList);

        MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();

        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.ATTENDANCE_MARKING);
            if (itemModel != null && itemModel.isAccess()) {
                if (model.getAttendanceLevel() == 1) {
                    if (model.isCheckedIn()) {
                        checkInOutButtonTextView.setVisibility(View.GONE);
                    } else {
                        checkInOutButtonTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    checkInOutButtonTextView.setVisibility(View.VISIBLE);
                }
            } else {
                checkInOutButtonTextView.setVisibility(View.GONE);
            }
        }

    }

    private void setDisplayTimeInOutButton(View checkBreakView, View checkInOutButton, TextView checkInOutButtonTextView, String string) {
        checkBreakView.setVisibility(View.GONE);
        checkInOutButton.setVisibility(View.VISIBLE);
        checkInOutButton.setTag(0);
        checkInOutButtonTextView.setText(string);
    }

    private void updateHomeData() {
        ((MainActivity) getActivity()).updateNavigation();
        SalaryMonthModel salaryData = ModelManager.getInstance().getSalaryMonthModel();
        if (salaryData != null) {
            ArrayList<SalaryMonthModel> months = salaryData.getMonths();
            if (months.size() > 0) {
                for (MainItemModel item : itemList) {
                    if (item.getmLeftTitle().equalsIgnoreCase(getString(R.string.msg_pay_slip))) {
                        item.setmRightSubTitle(months.get(0).getmMontTitle());
                    }
                }
                mAdapter.updateData(itemList);
            }

        }
        CheckInOutModel checkInOutModel = ModelManager.getInstance().getCheckInOutModel();
        if (checkInOutModel != null) {
            updateInOutData(checkInOutModel.isCheckedIn(), checkInOutModel.isCheckedOut(), checkInOutModel.isBreakIn(), checkInOutModel.isBreakOut());
        }


    }

    private void populateHomeData() {
        if (itemList == null) {
            itemList = new ArrayList<MainItemModel>();
        } else {
            itemList.clear();
        }
        MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();

        if (menuItemModel == null)
            return;

        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.ATTANDANCE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                if (ModelManager.getInstance().getCheckInOutModel() != null && ModelManager.getInstance().getCheckInOutModel().isMarkAttandanceEnable()) {
                    MainItemModel item=new MainItemModel(itemModel.getmObjectDesc(),
                            getString(R.string.msg_mark_attandance),
                            getString(R.string.msg_today_in_time), "--:--",
                            R.drawable.attendance_red);
                    item.setObjectId(itemModel.getmObjectId());
                    itemList.add(item);
                } else {
                    MainItemModel item=new MainItemModel(getString(R.string.msg_attandance),
                            getString(R.string.msg_mark_attandance),
                            R.drawable.attendance_red, false, true);
                    item.setObjectId(itemModel.getmObjectId());
                    itemList.add(item);
                }


            }
            itemModel = menuItemModel.getItemModel(MenuItemModel.TEAM_KEY);

            if (itemModel != null && itemModel.isAccess()) {
                EmployeeProfileModel employeeProfileModel = ModelManager.getInstance().getEmployeeProfileModel();
                MainItemModel item=new MainItemModel(itemModel.getmObjectDesc(),
                        getString(R.string.msg_team),
                        "View Team",
                        "" + (employeeProfileModel == null ? "0" :
                                employeeProfileModel.getmTeamSize()), R.drawable.team_blue);
                item.setObjectId(itemModel.getmObjectId());
                itemList.add(item);
            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.LEAVE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                LeaveBalanceModel leaveBalanceModel = ModelManager.getInstance()
                        .getLeaveBalanceModel();
                MainItemModel item=new MainItemModel(itemModel.getmObjectDesc(),
                        getString(R.string.msg_view_leaves),
                        getString(R.string.msg_leave_balance), ""
                        + (leaveBalanceModel == null ? "0" : leaveBalanceModel.getmAvailable()),
                        R.drawable.leave_red);
                item.setObjectId(itemModel.getmObjectId());
                itemList.add(item);
            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.PAY_SLIP_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                SalaryMonthModel salaryData = ModelManager.getInstance().getSalaryMonthModel();

                if (salaryData != null) {
                    MainItemModel item = new MainItemModel(itemModel.getmObjectDesc(),
                            getString(R.string.msg_check_pay_slip),
                            getString(R.string.msg_last_pay_slip),
                            getString(R.string.msg_download), R.drawable.payslip_blue,
                            true);
                    item.setObjectId(itemModel.getmObjectId());
                    itemList.add(item);
                }
            }
            itemModel = menuItemModel.getItemModel(MenuItemModel.APPROVAL_KEY);

            if ((itemModel != null && itemModel.isAccess())) {
                PendingCountModel pendingCountModel = ModelManager.getInstance()
                        .getPendingCountModel();
                MainItemModel item=new MainItemModel(itemModel.getmObjectDesc(),
                        "Check approvals", getString(R.string.msg_pending_approval), "" + (pendingCountModel == null ? "0"
                        : pendingCountModel.getmPendingCount()),
                        R.drawable.manager_approval);
                item.setObjectId(itemModel.getmObjectId());
                itemList.add(item);
            }else{
                if( menuItemModel.getItemModel(MenuItemModel.EXPENSE_KEY).isAccess() ||
                        menuItemModel.getItemModel(MenuItemModel.ADVANCE_KEY).isAccess() ||
                        menuItemModel.getItemModel(MenuItemModel.EMPLOYEE_APPROVAL_KEY).isAccess()){
                    PendingCountModel pendingCountModel = ModelManager.getInstance()
                            .getPendingCountModel();
                    MainItemModel item=new MainItemModel(itemModel.getmObjectDesc(),
                            "Check approvals", getString(R.string.msg_pending_approval), "" + (pendingCountModel == null ? "0"
                            : pendingCountModel.getmPendingCount()),
                            R.drawable.manager_approval);
                    item.setObjectId(itemModel.getmObjectId());
                    itemList.add(item);
                }


            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.LOCATION_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                TypeWiseListModel locationCountModel = ModelManager.getInstance()
                        .getLocationCountModel();
                MainItemModel item=new MainItemModel(itemModel.getmObjectDesc(),
                        "View Location Details", "Total", "" + (locationCountModel == null ? "0"
                        : locationCountModel.getList().get(0).getValue()),
                        R.drawable.location_blue);
                item.setObjectId(itemModel.getmObjectId());
                itemList.add(item);
            }


            itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_ADVANCE_KEY);
            if(itemModel!=null && itemModel.isAccess()) {
                ExpenseStatusModel expenseStatusModel = ModelManager.getInstance().getExpenseStatusModel();
                if (expenseStatusModel != null && expenseStatusModel.getExpenseStatusData()!=null) {

                        if (expenseStatusModel.getExpenseStatusData().get(0) != null) {
                            ExpenseStatusData expenseStatusData = expenseStatusModel.getExpenseStatusData().get(0);
                            MainItemModel item = new MainItemModel("Advance",
                                    getString(R.string.msg_advance_detail), getString(R.string.advance_balance),
                                    "" + (expenseStatusData == null ? "0" :expenseStatusData.getCurrencyCode()+ " " + expenseStatusData.getAmount()), R.drawable.advance_expense, true);
                            item.setObjectId(itemModel.getmObjectId());
                            itemList.add(item);
                        }

                }
            }

            itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_EXPENSE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                ExpenseStatusModel expenseStatusModel = ModelManager.getInstance().getExpenseStatusModel();
                if (expenseStatusModel != null && expenseStatusModel.getExpenseStatusData()!=null
                        && expenseStatusModel.getExpenseStatusData().size()>0) {
                    if(expenseStatusModel.getExpenseStatusData().get(1)!=null) {
                        ExpenseStatusData expenseStatusData= expenseStatusModel.getExpenseStatusData().get(1);
                        MainItemModel item = new MainItemModel("Expense",
                                getString(R.string.msg_expense), getString(R.string.expense_balance),
                                "" + (expenseStatusData == null ? "0" : expenseStatusData.getCurrencyCode() + " "+ expenseStatusData.getAmount()), R.drawable.expense_claim, true);
                        item.setObjectId(itemModel.getmObjectId());
                        itemList.add(item);
                    }
                }


            }

            ArrayList<String> list = new ArrayList<>();

            itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_LOCATION);
            if (itemModel != null && itemModel.isAccess()) {
                list.add("Location");
            }
            itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_EMPLOYEE);
            if (itemModel != null && itemModel.isAccess()) {
                list.add("Employee");
            }
            itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_LEAVE);
            if (itemModel != null && itemModel.isAccess()) {
                list.add("Leave");
            }
            itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_ADVANCE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                list.add("Advance");
            }
            itemModel = menuItemModel.getItemModel(MenuItemModel.CREATE_EXPENSE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                list.add("Expense");
            }
           /* list.add("Work From Home");
            list.add("Outdoor Duty");
            list.add("Tour");*/
            if (list.size() > 0) {
                ((MainActivity) getActivity()).setMenuList(list);
                ((MainActivity) getActivity()).menuPlus.setVisibility(View.VISIBLE);
            } else {
                ((MainActivity) getActivity()).menuPlus.setVisibility(View.GONE);
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_in_out:
            case R.id.btn_check_breack:
                if (Utility.isLocationEnabled(getContext())) {
                    if (Utility.isNetworkAvailable(getContext())) {
                        EmployeeProfileModel employeeProfileModel = ModelManager.getInstance().getEmployeeProfileModel();
                        if (PermissionUtil.checkLocationPermission(getContext())) {
                            if (employeeProfileModel != null) {
                                String selifie = employeeProfileModel.getmSelfieYN();
                                String geofence = employeeProfileModel.getmGeofencingYN();
                                Log.d(MainActivity.TAG, "This is selfie response " + selifie);

                                GPSTracker gps = new GPSTracker(getContext());
                                latitude = gps.getLatitude() + "";
                                longitude = gps.getLongitude() + "";


                                if (v != null) {
                                    currentReqType = (int) (v.getTag() != null ? v.getTag() : 2);
                                    preferences = new Preferences(getContext());
                                    preferences.saveInt("currentReqType", currentReqType);
                                    preferences.commit();
                                    if (currentReqType == 0 || currentReqType == 3) {
                                        if (!TextUtils.isEmpty(selifie) && selifie.equalsIgnoreCase("N") && !TextUtils.isEmpty(geofence) && geofence.equalsIgnoreCase("N")) {
                                            //     performAttandanceAction(currentReqType);
                                            getLocationAddress(latitude, longitude);
                                        } else {
                                            mUserActionListener.performUserAction(IAction.TIME_IN, null, null);
                                        }
                                    } else if (currentReqType == 4) {
                                        EventDataSource dataSource = new EventDataSource(getContext());
                                        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();

                                        if (loginUserModel != null) {
                                            UserModel userModel = loginUserModel.getUserModel();
                                            if (userModel != null) {
                                                dataSource.clearTimeOutEntry(userModel.getUserName());
                                            }
                                        }
                                        if (v != null) {
                                            getLocationAddress(latitude, longitude);
                                        }

                                    } else if (currentReqType == 1 || currentReqType == 2) {
                                        getLocationAddress(latitude, longitude);
                                    }
                                }
                            }
                        } else {
                            PermissionUtil.askLocationPermision(this);
                        }
                    } else {
                        new AlertCustomDialog(getActivity(), getString(R.string.msg_internet_connection));
                    }
                } else {
                    requestToEnableGPS(getContext(), preferences);
                }
                break;
            default:
                break;
        }
        super.onClick(v);
    }


    private void getLocationAddress(String lat, String lon) {
        GeoCoderAddress coder = new GeoCoderAddress();
        double latitude = 0;
        double longitude = 0;
        try {
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lon);
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
        coder.execute(GeoUtil.getGeoCoderUrl(latitude, longitude));
    }

    private void performAttandanceAction(int currentReqType2, String latitude, String longitude, String geoAddress) {
        AttendanceUtil.performAttendanceAction(getActivity(), this, currentReqType, latitude, longitude, null, null, null, geoAddress, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainItemModel model = itemList.get(position);
        MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();

        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(model.getObjectId());
            if (MenuItemModel.ATTANDANCE_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                mUserActionListener.performUserAction(IAction.ATTANDANCE_HISTORY, null, null);
            } else if (MenuItemModel.LEAVE_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                mUserActionListener.performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
            } else if (MenuItemModel.PAY_SLIP_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                SalaryMonthModel salaryData = ModelManager.getInstance()
                        .getSalaryMonthModel();
                if (salaryData == null || salaryData.getMonths() == null
                        || salaryData.getMonths().size() <= 0) {
                    new AlertCustomDialog(getActivity(),
                            "Currently no pay slip available");
                } else {
                    mUserActionListener.performUserAction(IAction.PAY_SLIP_VIEW, null, null);
                }
            } else if (MenuItemModel.APPROVAL_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                mUserActionListener.performUserAction(IAction.APPROVE_SCREEN, null, null);
            } else if (MenuItemModel.LOCATION_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                mUserActionListener.performUserAction(IAction.STORE_LIST_VIEW, null, null);
            } else if (MenuItemModel.TEAM_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                TeamMember.setmCurrentEmpId("");
                mUserActionListener.performUserAction(IAction.TEAM_MEMBER_LIST, null, null);
            } else if (MenuItemModel.CREATE_EXPENSE_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                mUserActionListener.performUserAction(IAction.EXPENSE_CLAIM_SUMMARY, null, null);
            }else if (MenuItemModel.CREATE_ADVANCE_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                mUserActionListener.performUserAction(IAction.ADVANCE_EXPENSE_SUMMARY, null, null);
            }
        }
    }

    private void updateAttendanceStatusMethods(String attendanceStatusResult) {
        ModelManager.getInstance().setCheckInOutModel(attendanceStatusResult);

        showTimeInButtons();

        if (ModelManager.getInstance().getCheckInOutModel() != null) {
            CheckInOutModel model = ModelManager.getInstance().getCheckInOutModel();
            updateInitialTimeInOutButton(model);
            updateInOutData(model.isCheckedIn(), model.isCheckedOut(), model.isBreakIn(), model.isBreakOut());
        }
    }

    private void updateLocationCountMethods(JSONObject mainJSONObject) {
        JSONArray jsonTypeWiseListForLocationCount = mainJSONObject.optJSONArray("list");
        TypeWiseListModel locationCountModel = new TypeWiseListModel(jsonTypeWiseListForLocationCount);
        ModelManager.getInstance().setLocationCountModel(locationCountModel);

        for (MainItemModel item : itemList) {
            if (item.getmLeftTitle().equalsIgnoreCase("Locations")) {
                TypeWiseListModel countModel = ModelManager.getInstance().getLocationCountModel();
                item.setmRightSubTitle(countModel == null ? "0" : countModel.getList().get(0).getValue());
            }
        }
        mAdapter.updateData(itemList);

    }

    private void updateEmpPendingApprovalCount(JSONObject mainJSONObject) {
        JSONArray pendingCountList = mainJSONObject.optJSONArray("list");
        PendingCountModel pendingCountModel = new PendingCountModel(pendingCountList);
        pendingCountModel.setmPendingCount(mainJSONObject.optString("PendingCount", ""));
        ModelManager.getInstance().setPendingCountModel(pendingCountModel);

        for (MainItemModel item : itemList) {
            if (item.getmLeftTitle().equalsIgnoreCase("Approval")) {
                item.setmRightSubTitle(pendingCountModel == null ? "0" : pendingCountModel.getmPendingCount());
            }
        }
        mAdapter.updateData(itemList);

    }

    private void updateEmpPendingApprovalReqCount(JSONObject mainResponseJson) {
        int totalPendingRequests = mainResponseJson.optInt("ReqCount", 0);
        ModelManager.getInstance().setUserTotalPendingRequests(totalPendingRequests);
    }

    private void updateEmpProfileData(JSONObject responseData) {
        ModelManager.getInstance().setEmployeeProfileModel(responseData);
        populateHomeData();
        updateHomeData();
        saveEmpConfig(preferences);
        ((MainActivity) getActivity()).updateHeaderImage();
        LinearLayout llProfileMessage = (LinearLayout) rootView.findViewById(R.id.llProfileMessage);
        TextView tvProfileMessage = (TextView) rootView.findViewById(R.id.tvProfileMessage);
        EmployeeProfileModel employeeProfileModel = ModelManager.getInstance().getEmployeeProfileModel();
        if (employeeProfileModel != null && employeeProfileModel.getmProfileMsgYN().equalsIgnoreCase("Y")) {
            llProfileMessage.setVisibility(View.VISIBLE);
            tvProfileMessage.setText(employeeProfileModel.getmProfileMsg());
        } else {
            llProfileMessage.setVisibility(View.GONE);
        }

    }

    private void updatePaySlipMethods(JSONObject salaryData) {
        String salaryMonthResult = salaryData.toString();
        ModelManager.getInstance().setSalaryMonthModel(salaryMonthResult);
        updateHomeData();
    }

    private void updateExpenseMethods(JSONObject expenseData){
        String expenseResult=expenseData.toString();
        ModelManager.getInstance().setExpenseStatusModel(expenseResult);
        populateHomeData();
        updateHomeData();

    }

    private void updateEmpLeaveBalanceMethods(JSONObject jsonObject) {
        String getEmpLeaveBalanceResult = jsonObject.toString();
        ModelManager.getInstance().setLeaveBalanceModel(getEmpLeaveBalanceResult);
        for (MainItemModel item : itemList) {
            if (item.getmLeftTitle().equalsIgnoreCase(getString(R.string.msg_leaves))) {
                LeaveBalanceModel leaveBalanceModel = ModelManager.getInstance().getLeaveBalanceModel();
                if (leaveBalanceModel != null) {
                    item.setmRightSubTitle("" + leaveBalanceModel.getmAvailable());
                }
            }
        }
        mAdapter.updateData(itemList);
    }

    @Override
    public void validateResponse(ResponseData response) {
        refreshLayout.setRefreshing(false);
        MainActivity.isAnimationLoaded = true;
        showHideProgressView(false);
        String responseData = response.getResponseData();
        if (response.isSuccess() && !isSessionValid(responseData)) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        switch (response.getRequestData().getReqApiId()) {

            case CommunicationConstant.API_GET_HOME_DATA:

                Log.d("TAG","Home Data response : "+responseData);
                try {
                    JSONObject mainObj = new JSONObject(responseData);
                    if ((mainObj.optJSONObject("GetHomeDataResult")).getInt("ErrorCode") == 0) {
                        JSONObject object = mainObj.optJSONObject("GetHomeDataResult");

                        JSONObject attendanceStatusData = object.optJSONObject("AttendanceStatus");
                        updateAttendanceStatusMethods(attendanceStatusData.toString());

                        JSONObject empLeaveBalanceData = object.optJSONObject("EmpLeaveBalance");
                        updateEmpLeaveBalanceMethods(empLeaveBalanceData);

                        JSONObject empLocationCountData = object.optJSONObject("EmpLocationCount");
                        updateLocationCountMethods(empLocationCountData);

                        JSONObject empPendingApprovalCount = object.optJSONObject("EmpPendingApprovalCount");
                        updateEmpPendingApprovalCount(empPendingApprovalCount);

                        JSONObject empPendingApprovalReqCountData = object.optJSONObject("EmpPendingApprovalReqCount");
                        updateEmpPendingApprovalReqCount(empPendingApprovalReqCountData);

                        JSONObject salaryMonthData = object.optJSONObject("SalaryMonth");
                        updatePaySlipMethods(salaryMonthData);

                        JSONObject empProfileData = object.optJSONObject("EmpProfile");
                        updateEmpProfileData(empProfileData);
                        JSONObject expenseData=object.optJSONObject("ExpenseStatus");
                        Log.d("Expense Result",expenseData.toString());
                        updateExpenseMethods(expenseData);



                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                }
                break;

            case CommunicationConstant.API_GET_MENU_DATA:

                try {
                    JSONObject getMenuDataResult = (new JSONObject(responseData)).optJSONObject("GetMenuDataResult");
                    JSONArray menuJsonArray = getMenuDataResult.optJSONArray("menuDataList");
                    ModelManager.getInstance().setMenuItemModel(menuJsonArray);
                    populateHomeData();
                    updateHomeData();
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e(TAG, e.getMessage(), e);
                    updateHomeData();
                }
                break;


            case CommunicationConstant.API_MARK_ATTANDANCE:
                try {
                    if (((new JSONObject(responseData)).getJSONObject("MarkAttendanceResult")).getInt("ErrorCode") == 0) {
                        CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getHomeData(),
                                CommunicationConstant.API_GET_HOME_DATA, true);
                    } else {
                        String errorMessage = ((new JSONObject(responseData)).getJSONObject("MarkAttendanceResult")).optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getContext(), errorMessage, "Ok", true, new AlertCustomDialog.AlertClickListener() {
                            @Override
                            public void onPositiveBtnListener() {
                                onCreate(null);
                            }

                            @Override
                            public void onNegativeBtnListener() {

                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    Crashlytics.logException(e);
                }
                break;


            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isFromLogin = preferences.getBoolean(AppsConstant.ISFROMLOGIN, false);
        isFromSplash = preferences.getBoolean(AppsConstant.ISFROMSPLASH, false);
        if (!isFromSplash && !isFromLogin) {
            onCreate(null);
        }

    }


    @Override
    public void onRefresh() {
        onCreate(null);

    }

    private class GeoCoderAddress extends GeoCoder {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result = s;
            GeoCoderModel address = null;
            JSONObject parentObj = null;
            try {
                if (!TextUtils.isEmpty(result)) {
                    parentObj = new JSONObject(result);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                Crashlytics.log(AppsConstant.PRIORITY_MEDIUM, TAG, result);
                Crashlytics.logException(e);
            }
            if (parentObj != null) {
                address = new GeoCoderModel(parentObj.optJSONArray("results"));
            }
            if (address != null) {
                performAttandanceAction(currentReqType, latitude, longitude, address.getmCompleteAddress());
            }
        }
    }

}
