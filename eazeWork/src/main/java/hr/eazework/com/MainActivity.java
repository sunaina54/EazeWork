package hr.eazework.com;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import hr.eazework.com.geofence.GeolocationService;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.SalaryMonthModel;
import hr.eazework.com.model.StoreLocationModel;
import hr.eazework.com.model.TeamMember;
import hr.eazework.com.ui.customview.CustomBuilder;
import hr.eazework.com.ui.fragment.AddExpenseClaimFragment;
import hr.eazework.com.ui.fragment.AddExpenseFragment;
import hr.eazework.com.ui.fragment.AdvanceApprovalFragment;
import hr.eazework.com.ui.fragment.AdvanceRequestFragment;
import hr.eazework.com.ui.fragment.AdvanceRequestSummaryFragment;
import hr.eazework.com.ui.fragment.ApproveScreen;
import hr.eazework.com.ui.fragment.AttandanceFragment;
import hr.eazework.com.ui.fragment.AttendanceHistory;
import hr.eazework.com.ui.fragment.BaseFragment;
import hr.eazework.com.ui.fragment.ChangePasswordFragment;
import hr.eazework.com.ui.fragment.CreateEmployeeFragment;
import hr.eazework.com.ui.fragment.CreateNewLeaveFragment;
import hr.eazework.com.ui.fragment.CreateStoreFragment;
import hr.eazework.com.ui.fragment.EditAdvanceApprovalFragment;
import hr.eazework.com.ui.fragment.EditExpenseApprovalFragment;
import hr.eazework.com.ui.fragment.EditStoreFragment;
import hr.eazework.com.ui.fragment.EditTeamMember;
import hr.eazework.com.ui.fragment.EditViewExpenseClaimFragment;
import hr.eazework.com.ui.fragment.ExpenseApprovalFragment;
import hr.eazework.com.ui.fragment.ExpenseClaimSummaryFragment;
import hr.eazework.com.ui.fragment.HistoryTrackFragment;
import hr.eazework.com.ui.fragment.HomeFragment;
import hr.eazework.com.ui.fragment.LeaveBalanceDetailFragment;
import hr.eazework.com.ui.fragment.LoginFragment;
import hr.eazework.com.ui.fragment.MarkAttendance;
import hr.eazework.com.ui.fragment.NavigationDrawerFragment;
import hr.eazework.com.ui.fragment.OutdoorDutyRequestFragment;
import hr.eazework.com.ui.fragment.PaySlipDownloadFragment;
import hr.eazework.com.ui.fragment.PendingActivityFragment;
import hr.eazework.com.ui.fragment.PendingEmployeeApprovalFragment;
import hr.eazework.com.ui.fragment.SplashFragment;
import hr.eazework.com.ui.fragment.StoreListFragment;
import hr.eazework.com.ui.fragment.TeamMemberHistory;
import hr.eazework.com.ui.fragment.TeamMemberList;
import hr.eazework.com.ui.fragment.TeamMemberProfile;
import hr.eazework.com.ui.fragment.TourRequestFragment;
import hr.eazework.com.ui.fragment.UserProfile;
import hr.eazework.com.ui.fragment.ViewDataBase;
import hr.eazework.com.ui.fragment.ViewPaySlipFragment;
import hr.eazework.com.ui.fragment.WorkFromHomeRequestFragment;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.interfaces.UserActionListner;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.BUNDLE_KEYS;
import hr.eazework.com.ui.util.EventDataSource;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, UserActionListner, IBaseResponse {


    public static String TAG = "MainActivity";

    public static boolean geofencesAlreadyRegistered = false;


    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    public NavigationDrawerFragment mNavigationDrawerFragment;
    public ImageView menuPlus;
    public ArrayList<String> menuList = new ArrayList<>();
    private String paySlipHeader = "";

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Preferences preferences;
    public DrawerLayout mDrawerLayout;
    public static MainActivity mInstance;
    private long backPressedTime;
    public static Toolbar toolbar;
    public static ImageView ibRight;
    public TextView edit;
    private ImageView headerImage;
    public static long LIST_ANIM_OUT_TIME = 300;


    public static boolean isAnimationLoaded = false;

    public ArrayList<String> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<String> menuList) {
        this.menuList = menuList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = new Preferences(getApplicationContext());
        mInstance = this;

        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Token "+ token);


        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = "";// getTitle();
        headerImage = (ImageView) findViewById(R.id.img_company_logo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ibRight = (ImageView) findViewById(R.id.ibRight);
        edit = (TextView) findViewById(R.id.tv_edit);
        setSupportActionBar(toolbar);

        menuPlus = (ImageView) findViewById(R.id.plus_create_new);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout, toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new SplashFragment()).commit();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUserAction(IAction.EDIT_TEAM_MEMBER, null, null);
            }
        });


        menuPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBuilder builder = new CustomBuilder(MainActivity.this, "Create New", true);
                builder.setSingleChoiceItems(menuList, null, new CustomBuilder.OnClickListener() {
                    @Override
                    public void onClick(CustomBuilder builder, Object selectedObject) {
                        if (selectedObject.toString().equalsIgnoreCase("Location")) {
                            performUserAction(IAction.CREATE_LOCATION_VIEW, null, null);
                        } else if (selectedObject.toString().equalsIgnoreCase("Employee")) {
                            performUserAction(IAction.CREATE_EMPLOYEE_VIEW, null, null);
                        } else if (selectedObject.toString().equalsIgnoreCase("Leave")) {
                            performUserAction(IAction.CREATE_NEW_LEAVE, null, null);
                        }else if (selectedObject.toString().equalsIgnoreCase("Advance")) {
                            performUserAction(IAction.ADVANCE_REQUEST, null, null);
                        } else if (selectedObject.toString().equalsIgnoreCase("Expense")) {
                            performUserAction(IAction.ADD_EXPENSE_CLAIM, null, null);
                        }else if (selectedObject.toString().equalsIgnoreCase("Work From Home")) {
                            performUserAction(IAction.WORK_FROM_HOME, null, null);
                        } else if (selectedObject.toString().equalsIgnoreCase("Outdoor Duty")) {
                            performUserAction(IAction.OUTDOOR_DUTY, null, null);
                        } else if (selectedObject.toString().equalsIgnoreCase("Tour")) {
                            performUserAction(IAction.TOUR, null, null);
                        }
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    public void onNavigationDrawerItemSelected(int position, String title) {
        // update the main content by replacing fragments

        if (title.equalsIgnoreCase(getString(R.string.title_home))) {
            performUserAction(IAction.HOME_VIEW, null, null);
        } else if (title.equalsIgnoreCase(getString(R.string.title_profile))) {
            performUserAction(IAction.USER_PROFILE, null, null);
        } else if (title.equalsIgnoreCase("Location")) {
            performUserAction(IAction.STORE_LIST_VIEW, null, null);
        } else if (title.equalsIgnoreCase("Create Employee")) {
            performUserAction(IAction.CREATE_EMPLOYEE_VIEW, null, null);
        } else if (title.equalsIgnoreCase("Database")) {
            performUserAction(IAction.VIEW_DATABASE, null, null);
        } else if (title.equalsIgnoreCase("Create Location")) {
            performUserAction(IAction.CREATE_LOCATION_VIEW, null, null);
        } else if (title
                .equalsIgnoreCase(getString(R.string.title_notification))) {
            new AlertCustomDialog(MainActivity.this,
                    "Currently no notification available");
        } else if (title
                .equalsIgnoreCase(getString(R.string.msg_change_password))) {
            performUserAction(IAction.CHANGE_PASSWORD, null, null);
        } else if (title.equalsIgnoreCase("Calendar")) {
            performUserAction(IAction.ATTANDANCE_CALENDER, null, null);
        } else if (title.equalsIgnoreCase(getString(R.string.title_logout))) {
            new AlertCustomDialog(MainActivity.this, "Do you want to logout ?",
                    getString(R.string.dlg_cancel),
                    getString(R.string.dlg_confirm), new AlertCustomDialog.AlertClickListener() {

                @Override
                public void onPositiveBtnListener() {
                    if (Utility.isNetworkAvailable(getApplicationContext())) {
                        EventDataSource dataSource = new EventDataSource(getApplicationContext());
                        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
                        if (loginUserModel != null && loginUserModel.getUserModel() != null) {
                            String username = loginUserModel.getUserModel().getUserName();
                            if (dataSource != null && dataSource.isAllDataPushed(username)) {
                                showHideProgress(true);
                                CommunicationManager
                                        .getInstance()
                                        .sendPostRequest(
                                                MainActivity.this,
                                                AppRequestJSONString
                                                        .getLogOutData(),
                                                CommunicationConstant.API_LOGOUT_USER,
                                                true);
                            } else {
                                if (Utility.isNetworkAvailable(getApplicationContext())) {
                                } else {
                                    new AlertCustomDialog(
                                            MainActivity.this,
                                            "Location Details Not Uploaded Please Connect To Internet");
                                }
                            }
                        }
                    } else {
                        new AlertCustomDialog(
                                MainActivity.this,
                                getString(R.string.msg_internet_connection));
                    }
                }

                @Override
                public void onNegativeBtnListener() {
                    // TODO Auto-generated method stub

                }
            });
        } else {
            if (ModelManager.getInstance().getMenuItemModel() == null)
                return;
            MenuItemModel itemModel = ModelManager.getInstance()
                    .getMenuItemModel().getItemModelByDesc(title);
            if (itemModel != null) {
                if (MenuItemModel.LEAVE_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                    performUserAction(IAction.LEAVE_BALANCE_DETAIL, null, null);
                } else if (MenuItemModel.PAY_SLIP_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                    SalaryMonthModel salaryData = ModelManager.getInstance()
                            .getSalaryMonthModel();
                    if (salaryData == null || salaryData.getMonths() == null
                            || salaryData.getMonths().size() <= 0) {
                        new AlertCustomDialog(this,
                                "Currently no pay slip available");
                    } else {
                        /*
                         * new AlertCustomDialog(getActivity(),
						 * "This feature is under development");
						 */
                        performUserAction(IAction.PAY_SLIP_VIEW, null, null);
                    }
                } else if (MenuItemModel.CREATE_EXPENSE_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                    performUserAction(IAction.EXPENSE_CLAIM_SUMMARY, null, null);
                } else if (MenuItemModel.CREATE_ADVANCE_KEY.equalsIgnoreCase(itemModel.getmObjectId())) {
                    performUserAction(IAction.ADVANCE_EXPENSE_SUMMARY, null, null);
                }
            }
        }
    }

    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void performUserAction(int pActionType, View pView, Object pData) {

        //	menuPlus.setVisible(false);

        switch (pActionType) {

            case IAction.VIEW_PAY_SLIP:

                if (isFragmentExistsInBackStack(ViewPaySlipFragment.TAG)) {
                    if (getTopFragment() instanceof ViewPaySlipFragment)
                        return;
                    popBackStack(ViewPaySlipFragment.TAG, 0);
                    getTopFragment().getArguments().putSerializable(BUNDLE_KEYS.SELECTED_MONTH, (SalaryMonthModel) pData);

                } else {
                    ViewPaySlipFragment detailFragment = new ViewPaySlipFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_KEYS.SELECTED_MONTH, (SalaryMonthModel) pData);
                    detailFragment.setArguments(bundle);
                    addFragment(R.id.content_frame, detailFragment, ViewPaySlipFragment.TAG);
                }
                break;
            case IAction.USER_PROFILE:

                if (isFragmentExistsInBackStack(UserProfile.TAG)) {
                    if (getTopFragment() instanceof UserProfile)
                        return;
                    popBackStack(UserProfile.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new UserProfile(), UserProfile.TAG);
                }
                break;

            case IAction.PENDING_APPROVAL:

                if (isFragmentExistsInBackStack(PendingActivityFragment.TAG)) {
                    if (getTopFragment() instanceof PendingActivityFragment)
                        return;
                    popBackStack(PendingActivityFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new PendingActivityFragment(),
                            PendingActivityFragment.TAG);
                }
                break;

            case IAction.EXPENSE_APPROVAL:

                if (isFragmentExistsInBackStack(PendingActivityFragment.TAG)) {
                    if (getTopFragment() instanceof ExpenseApprovalFragment)
                        return;
                    popBackStack(ExpenseApprovalFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new ExpenseApprovalFragment(),
                            ExpenseApprovalFragment.TAG);
                }
                break;
            case IAction.ADVANCE_APPROVAL:

                if (isFragmentExistsInBackStack(PendingActivityFragment.TAG)) {
                    if (getTopFragment() instanceof AdvanceApprovalFragment)
                        return;
                    popBackStack(AdvanceApprovalFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new AdvanceApprovalFragment(),
                            AdvanceApprovalFragment.TAG);
                }
                break;
            case IAction.MEMBER_APPROVAL:

                if (isFragmentExistsInBackStack(PendingEmployeeApprovalFragment.TAG)) {
                    if (getTopFragment() instanceof PendingEmployeeApprovalFragment)
                        return;
                    popBackStack(PendingEmployeeApprovalFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new PendingEmployeeApprovalFragment(),
                            PendingEmployeeApprovalFragment.TAG);
                }
                break;

            case IAction.APPROVE_SCREEN:

                if (isFragmentExistsInBackStack(ApproveScreen.TAG)) {
                    if (getTopFragment() instanceof ApproveScreen)
                        return;
                    popBackStack(ApproveScreen.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new ApproveScreen(),
                            ApproveScreen.TAG);
                }
                break;

            case IAction.CHANGE_PASSWORD:

                if (isFragmentExistsInBackStack(ChangePasswordFragment.TAG)) {
                    if (getTopFragment() instanceof ChangePasswordFragment)
                        return;
                    popBackStack(ChangePasswordFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new ChangePasswordFragment(),
                            ChangePasswordFragment.TAG);
                }
                break;
            case IAction.PAY_SLIP_VIEW_DOWNLOAD:
                if (isFragmentExistsInBackStack(PaySlipDownloadFragment.TAG)) {
                    if (getTopFragment() instanceof PaySlipDownloadFragment)
                        return;
                    popBackStack(PaySlipDownloadFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new PaySlipDownloadFragment(),
                            PaySlipDownloadFragment.TAG);
                }
                break;
            case IAction.PAY_SLIP_VIEW:
                if (isFragmentExistsInBackStack(PaySlipDownloadFragment.TAG)) {
                    if (getTopFragment() instanceof PaySlipDownloadFragment)
                        return;
                    popBackStack(PaySlipDownloadFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new PaySlipDownloadFragment(),
                            PaySlipDownloadFragment.TAG);
                }
                break;
            case IAction.SPLASH_VIEW:
                if (isFragmentExistsInBackStack(SplashFragment.TAG)) {
                    if (getTopFragment() instanceof SplashFragment)
                        return;
                    popBackStack(SplashFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new SplashFragment(),
                            SplashFragment.TAG);
                }
                break;
            case IAction.HOME_VIEW:
                if (isFragmentExistsInBackStack(HomeFragment.TAG)) {
                    if (getTopFragment() instanceof HomeFragment)
                        return;
                    if (preferences == null) {
                        preferences = new Preferences(getApplicationContext());
                    }
                    if (pData == null) {
                        preferences.saveBoolean(AppsConstant.ISFROMSPLASH, false);
                        preferences.saveBoolean(AppsConstant.ISFROMLOGIN, false);
                        preferences.commit();
                    }
                    popBackStack(HomeFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new HomeFragment(), HomeFragment.TAG);
                }
                break;
            case IAction.LOGIN_VIEW:

                if (isFragmentExistsInBackStack(LoginFragment.TAG)) {
                    if (getTopFragment() instanceof LoginFragment)
                        return;
                    try {
                        popBackStack(LoginFragment.TAG, 0);
                    } catch (IllegalStateException e) {
                        Crashlytics.logException(e);
                    }
                } else {
                    addFragment(R.id.content_frame, new LoginFragment(), LoginFragment.TAG);
                }
                break;
            case IAction.ATTANDANCE_CALENDER:

                if (isFragmentExistsInBackStack(AttandanceFragment.TAG)) {
                    if (getTopFragment() instanceof AttandanceFragment)
                        return;
                    popBackStack(AttandanceFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new AttandanceFragment(),
                            AttandanceFragment.TAG);
                }
                break;

            case IAction.ATTANDANCE_HISTORY:
                if (isFragmentExistsInBackStack(AttendanceHistory.TAG)) {
                    if (getTopFragment() instanceof AttendanceHistory)
                        return;
                    popBackStack(AttendanceHistory.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new AttendanceHistory(), AttendanceHistory.TAG);
                }
                break;

            case IAction.LEAVE_BALANCE_DETAIL:

                if (isFragmentExistsInBackStack(LeaveBalanceDetailFragment.TAG)) {
                    if (getTopFragment() instanceof LeaveBalanceDetailFragment)
                        return;
                    popBackStack(LeaveBalanceDetailFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame,
                            new LeaveBalanceDetailFragment(),
                            LeaveBalanceDetailFragment.TAG);
                }
                break;

            case IAction.CREATE_NEW_LEAVE:

                if (isFragmentExistsInBackStack(CreateNewLeaveFragment.TAG)) {
                    if (getTopFragment() instanceof CreateNewLeaveFragment)
                        return;
                    popBackStack(CreateNewLeaveFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new CreateNewLeaveFragment(),
                            CreateNewLeaveFragment.TAG);
                }
                break;

            case IAction.EXPENSE_CLAIM_SUMMARY:
                if (isFragmentExistsInBackStack(ExpenseClaimSummaryFragment.TAG)) {
                    if (getTopFragment() instanceof ExpenseClaimSummaryFragment)
                        return;
                    popBackStack(ExpenseClaimSummaryFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new ExpenseClaimSummaryFragment(),
                            ExpenseClaimSummaryFragment.TAG);
                }
                break;
            case IAction.ADVANCE_EXPENSE_SUMMARY:
                if (isFragmentExistsInBackStack(AdvanceRequestSummaryFragment.TAG)) {
                    if (getTopFragment() instanceof AdvanceRequestSummaryFragment)
                        return;
                    popBackStack(AdvanceRequestSummaryFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new AdvanceRequestSummaryFragment(),
                            AdvanceRequestSummaryFragment.TAG);
                }
                break;
            case IAction.ADD_EXPENSE_CLAIM:
                if (isFragmentExistsInBackStack(AddExpenseClaimFragment.TAG)) {
                    if (getTopFragment() instanceof AddExpenseClaimFragment)
                        return;
                    popBackStack(AddExpenseClaimFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new AddExpenseClaimFragment(),
                            AddExpenseClaimFragment.TAG);
                }
                break;
            case IAction.ADVANCE_REQUEST:
                if (isFragmentExistsInBackStack(AdvanceRequestFragment.TAG)) {
                    if (getTopFragment() instanceof AdvanceRequestFragment)
                        return;
                    popBackStack(AdvanceRequestFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new AdvanceRequestFragment(),
                            AdvanceRequestFragment.TAG);
                }
                break;
            case IAction.WORK_FROM_HOME:
                if (isFragmentExistsInBackStack(WorkFromHomeRequestFragment.TAG)) {
                    if (getTopFragment() instanceof WorkFromHomeRequestFragment)
                        return;
                    popBackStack(WorkFromHomeRequestFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new WorkFromHomeRequestFragment(),
                            WorkFromHomeRequestFragment.TAG);
                }
                break;
            case IAction.OUTDOOR_DUTY:
                if (isFragmentExistsInBackStack(OutdoorDutyRequestFragment.TAG)) {
                    if (getTopFragment() instanceof OutdoorDutyRequestFragment)
                        return;
                    popBackStack(OutdoorDutyRequestFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new OutdoorDutyRequestFragment(),
                            OutdoorDutyRequestFragment.TAG);
                }
                break;
            case IAction.TOUR:
                if (isFragmentExistsInBackStack(TourRequestFragment.TAG)) {
                    if (getTopFragment() instanceof TourRequestFragment)
                        return;
                    popBackStack(TourRequestFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new TourRequestFragment(),
                            TourRequestFragment.TAG);
                }
                break;
            case IAction.CREATE_EMPLOYEE_VIEW:
                if (isFragmentExistsInBackStack(CreateEmployeeFragment.TAG)) {
                    if (getTopFragment() instanceof CreateEmployeeFragment)
                        return;
                    popBackStack(CreateEmployeeFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new CreateEmployeeFragment(),
                            CreateEmployeeFragment.TAG);
                }
                break;
            case IAction.CREATE_LOCATION_VIEW:
                if (isFragmentExistsInBackStack(CreateStoreFragment.TAG)) {
                    if (getTopFragment() instanceof CreateStoreFragment)
                        return;
                    popBackStack(CreateStoreFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new CreateStoreFragment(),
                            CreateStoreFragment.TAG);
                }
                break;

            case IAction.TEAM_MEMBER_LIST:

                String option = null;
                if (pData != null) {
                    Bundle bundle = (Bundle) pData;
                    option = bundle.getString(AppsConstant.OPTION_SELECTED);
                }

                if (TextUtils.isEmpty(option) && isFragmentExistsInBackStack(TeamMemberList.TAG)) {
                    if (getTopFragment() instanceof TeamMemberList)
                        return;
                    popBackStack(TeamMemberList.TAG, 0);
                } else {

                    Fragment f = new TeamMemberList();

                    if (pData != null) {
                        f.setArguments((Bundle) pData);
                    }


                    addFragment(R.id.content_frame, f,
                            TeamMemberList.TAG);
                }

                break;
            case IAction.TIME_IN:
                if (isFragmentExistsInBackStack(MarkAttendance.TAG)) {
                    if (getTopFragment() instanceof MarkAttendance)
                        return;
                    popBackStack(MarkAttendance.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new MarkAttendance(), MarkAttendance.TAG);
                    Intent intent = new Intent(getApplicationContext(), GeolocationService.class);
                    startService(intent);

                }
                break;

            case IAction.TEAM_MEMBER_HISTORY:
                if (isFragmentExistsInBackStack(TeamMemberHistory.TAG)) {
                    if (getTopFragment() instanceof TeamMemberHistory)
                        return;
                    popBackStack(TeamMemberHistory.TAG, 0);
                } else {
                    Fragment fragment = new TeamMemberHistory();
                    if (pData != null) {
                        fragment.setArguments((Bundle) pData);
                    }
                    addFragment(R.id.content_frame, fragment,
                            TeamMemberHistory.TAG);
                }
                break;

            case IAction.HISTORY_TRACK_VIEW:

                if (isFragmentExistsInBackStack(HistoryTrackFragment.TAG)) {
                    if (getTopFragment() instanceof HistoryTrackFragment)
                        return;
                    popBackStack(HistoryTrackFragment.TAG, 0);
                } else {
                    HistoryTrackFragment f = new HistoryTrackFragment();
                    if (pData != null) {
                        f.setCurrentItemContext((Map) pData);
                    }
                    addFragment(R.id.content_frame, f, HistoryTrackFragment.TAG);
                }
                break;

            case IAction.TEAM_MEMBER_PROFILE:

                if (isFragmentExistsInBackStack(TeamMemberProfile.TAG)) {
                    if (getTopFragment() instanceof TeamMemberProfile)
                        return;
                    popBackStack(TeamMemberProfile.TAG, 0);
                } else {
                    Fragment fragment = new TeamMemberProfile();
                    if (pData != null) {
                        fragment.setArguments((Bundle) pData);
                    }

                    addFragment(R.id.content_frame, fragment,
                            TeamMemberProfile.TAG);
                }
                break;

            case IAction.EDIT_TEAM_MEMBER:

                if (isFragmentExistsInBackStack(EditTeamMember.TAG)) {
                    if (getTopFragment() instanceof EditTeamMember)
                        return;
                    popBackStack(EditTeamMember.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new EditTeamMember(),
                            EditTeamMember.TAG);
                }
                break;


            case IAction.EDIT_STORE_VIEW:

                if (isFragmentExistsInBackStack(EditStoreFragment.TAG)) {
                    if (getTopFragment() instanceof EditStoreFragment)
                        return;
                    popBackStack(EditStoreFragment.TAG, 0);
                } else {
                    Fragment fragment = new EditStoreFragment();
                    Bundle b = new Bundle();
                    if (pData != null) {
                        b.putString(AppsConstant.OFFICE_ID, ((StoreLocationModel) pData).getmOfficeID());
                        fragment.setArguments(b);
                    }

                    addFragment(R.id.content_frame, fragment,
                            EditStoreFragment.TAG);
                }
                break;
            case IAction.STORE_LIST_VIEW:
                if (isFragmentExistsInBackStack(StoreListFragment.TAG)) {
                    if (getTopFragment() instanceof StoreListFragment)
                        return;
                    popBackStack(StoreListFragment.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new StoreListFragment(),
                            StoreListFragment.TAG);
                }
                break;
            case IAction.VIEW_DATABASE:

                if (isFragmentExistsInBackStack(ViewDataBase.TAG)) {
                    if (getTopFragment() instanceof ViewDataBase)
                        return;
                    popBackStack(ViewDataBase.TAG, 0);
                } else {
                    addFragment(R.id.content_frame, new ViewDataBase(),
                            ViewDataBase.TAG);
                }
                break;
        }

    }

    /**
     * utility method for adding fragment
     *
     * @param containerId viewgroup id to add fragment in.
     * @param fragment    fragment instance to be added.
     * @param tag         tag to be associated with this transaction.
     */
    public void addFragment(final int containerId, final Fragment fragment, final String tag) {
        // fragment.setRetainInstance(true);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment topFragment = getTopFragment();

        try {

            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            setFragmentCustomAnimations(fragmentTransaction);
            fragmentTransaction.replace(containerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();

        } catch (Exception e) {
            try {
                final FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception exception) {
            }

        }
    }

    protected void setFragmentCustomAnimations(
            FragmentTransaction pFragmentTransaction) {

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "";// getString(R.string.title_section1);
                break;
            case 2:
                mTitle = "";// getString(R.string.title_section2);
                break;
            case 3:
                mTitle = "";// getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.home) {
            if (!(getTopFragment() instanceof HomeFragment)) {
                onBackPressed();
                return false;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void onDrawerOpenClose(boolean isOpen) {

        View view = findViewById(R.id.tv_actionbar_title_container);
        if (view != null) {
            if (isOpen) {
                mNavigationDrawerFragment.getDrawableToggle().setHomeAsUpIndicator(R.drawable.img_arrow_back);
                animateToGone(view, -1);
            } else {
                mNavigationDrawerFragment.getDrawableToggle().setHomeAsUpIndicator(R.drawable.menu_icon);
                animateToVisible(view, -1);
            }
        }
    }

    @SuppressLint("NewApi")
    public static void animateToGone(final View view, long time) {
        if (time == -1) {
            time = LIST_ANIM_OUT_TIME;
        }
        if (Utility.isNotLowerVersion(VERSION_CODES.HONEYCOMB)) {

            for (Animator anim : Utility.getAnimators(view, false)) {
                if (anim.isRunning()) {
                } else {
                    anim.setDuration(LIST_ANIM_OUT_TIME).start();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (view != null)
                                view.setVisibility(View.GONE);

                        }
                    }, time);
                }
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    public static void animateToVisible(final View view, long time) {
        if (time == -1) {
            time = LIST_ANIM_OUT_TIME;
        }
        if (Utility.isNotLowerVersion(VERSION_CODES.HONEYCOMB)) {

            if (view != null)
                view.setVisibility(View.VISIBLE);
            for (Animator anim : Utility.getAnimators(view, true)) {
                if (anim.isRunning()) {
                } else {
                    anim.setDuration(time).start();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (view != null)
                                view.setVisibility(View.VISIBLE);

                        }
                    }, time);
                }
            }
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * utility method to find fragment existence in back stack.
     *
     * @param tag tag of fragment which is to be searched.same as provided
     *            during adding
     * @return true if exist.
     */
    protected boolean isFragmentExistsInBackStack(String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) != null)
            return true;
        else
            return false;
    }

    /**
     * utility method to pop top fragment
     */
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * utility method for poping back provided tag fragment with
     * inclusive/exclusive flag
     *
     * @param tag  tag to identify fragment.
     * @param flag inclusive/exclusive flag
     */
    public void popBackStack(String tag, int flag) {
        getSupportFragmentManager().popBackStack(tag, flag);
    }

    /**
     * helper method to retrieve top fragment
     *
     * @return top fragment
     */
    public Fragment getTopFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);

    }

    public void updateHeader(String title, String subTitle) {
		/*
		 * TextView tvTitle =
		 * (TextView)findViewById(R.id.tv_actionbar_page_title); TextView
		 * tvSubTitle =
		 * (TextView)findViewById(R.id.tv_actionbar_page_sub_title);
		 * if(subTitle!=null){ tvSubTitle.setText(subTitle); } else {
		 * tvSubTitle.setText(""); tvSubTitle.setVisibility(View.GONE); }
		 * if(title!=null){ tvTitle.setText(title); } else {
		 * tvTitle.setText(getString(R.string.app_name)); }
		 */
    }

    public void updateHeaderImage() {
        EmployeeProfileModel profileModel = ModelManager.getInstance().getEmployeeProfileModel();
        if (profileModel != null && profileModel.getmCompanyLogo() != null && !profileModel.getmCompanyLogo().equalsIgnoreCase("")) {

            String companyLogo = CommunicationConstant.getMobileCareURl() + profileModel.getmCompanyLogo()/* + "?tyn=" + Calendar.getInstance().getTimeInMillis()*/;
            Log.e("ImagePath", companyLogo);
            Picasso picasso = Picasso.with(getApplicationContext());
            RequestCreator requestCreator = picasso.load(companyLogo)/*.into(headerImage)*/;
            requestCreator.networkPolicy(NetworkPolicy.NO_CACHE);
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE);
            requestCreator.into(headerImage);
        }
    }


    public String getPaySlipHeader() {
        return paySlipHeader;
    }

    public void setPaySlipHeader(String paySlipHeader) {
        this.paySlipHeader = paySlipHeader;
    }

    @SuppressLint("NewApi")
    public void showHideActionBarLogo(boolean isShow) {
        findViewById(R.id.img_company_logo).setVisibility(isShow ? View.VISIBLE : View.GONE);
        mTitle = getHeaderTitle();


        if (((BaseFragment) getTopFragment()).isShowPaySlipHeader()) {
            getSupportActionBar().setTitle(getPaySlipHeader());
        } else {
            getSupportActionBar().setTitle(mTitle);
        }


        final View.OnClickListener originalToolbarListener = mNavigationDrawerFragment.getDrawableToggle().getToolbarNavigationClickListener();
        menuPlus = (ImageView) findViewById(R.id.plus_create_new);
        if (((BaseFragment) getTopFragment()).isShowPlusMenu()) {
            if (getMenuList().size() > 0) {
                menuPlus.setVisibility(View.VISIBLE);
            } else {
                menuPlus.setVisibility(View.GONE);
            }
        } else {
            menuPlus.setVisibility(View.GONE);

        }

        if (((BaseFragment) getTopFragment()).isShowEditTeamButtons()) {
            findViewById(R.id.rl_edit_team_member).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rl_edit_team_member).setVisibility(View.GONE);
        }

        if (((BaseFragment) getTopFragment()).isShowEditTeam()) {
            MenuItemModel itemModel = ModelManager.getInstance().getMenuItemModel();
            if (itemModel != null) {
                MenuItemModel menuItemModel = itemModel.getItemModel(MenuItemModel.EDIT_PROFILE_KEY);
                if (menuItemModel != null && menuItemModel.isAccess()) {
                    edit.setVisibility(View.VISIBLE);
                } else {
                    edit.setVisibility(View.GONE);
                }
            }


        } else {
            findViewById(R.id.tv_edit).setVisibility(View.GONE);
        }


        if (!(getTopFragment() instanceof HomeFragment)) {

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayUseLogoEnabled(true);

            getSupportActionBar().setDisplayShowHomeEnabled(false);
            mNavigationDrawerFragment.getDrawableToggle().setDrawerIndicatorEnabled(false);

            if (((BaseFragment) getTopFragment()).isShowLeaveButtons() || ((BaseFragment) getTopFragment()).isShowEditTeamButtons()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mNavigationDrawerFragment.getDrawableToggle().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.img_arrow_back));
            }
            mNavigationDrawerFragment.getDrawableToggle().setToolbarNavigationClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mNavigationDrawerFragment.getDrawableToggle().setDrawerIndicatorEnabled(false);
            mNavigationDrawerFragment.getDrawableToggle().setHomeAsUpIndicator(R.drawable.menu_icon);
            //      mNavigationDrawerFragment.getDrawableToggle().setToolbarNavigationClickListener(originalToolbarListener);
            mNavigationDrawerFragment.getDrawableToggle().setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, v.getId() + "");
                    if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                        mDrawerLayout.closeDrawer(Gravity.START, true);
                        mNavigationDrawerFragment.getDrawableToggle().setHomeAsUpIndicator(R.drawable.menu_icon);
                    } else {
                        mNavigationDrawerFragment.getDrawableToggle().setHomeAsUpIndicator(R.drawable.img_arrow_back);
                        mDrawerLayout.openDrawer(Gravity.START, true);
                    }
                }
            });
        }
    }

    private String getHeaderTitle() {
        Fragment fragment = getTopFragment();
        if (fragment instanceof HomeFragment) {
            return "";
        } else if (fragment instanceof TeamMemberProfile) {
            return "Profile";
        } else if (fragment instanceof UserProfile) {
            return "Profile";
        } else if (fragment instanceof AttandanceFragment) {
            return "Calendar";
        } else if (fragment instanceof StoreListFragment) {
            return "Location";
        } else if (fragment instanceof HistoryTrackFragment) {
            return "History Track";
        } else if (fragment instanceof AttendanceHistory) {
            return getString(R.string.msg_history);
        } else if (fragment instanceof ApproveScreen) {
            return "Approvals";
        } else if (fragment instanceof TeamMemberHistory) {
            return "Member History";
        } else if (fragment instanceof MarkAttendance) {
            return getString(R.string.msg_map_screen);
        } else if (fragment instanceof LeaveBalanceDetailFragment) {
            return getString(R.string.msg_leaves);
        } else if (fragment instanceof TeamMemberList) {
            return getString(R.string.msg_team_members);
        } else if (fragment instanceof PaySlipDownloadFragment) {
            return "View Payslip";
        } else if (fragment instanceof ChangePasswordFragment) {
            return getString(R.string.msg_change_password);
        } else if (fragment instanceof PendingActivityFragment) {
            return "Pending Leaves";
        } else if (fragment instanceof PendingEmployeeApprovalFragment) {
            return "Pending Employees";
        } else if (fragment instanceof ExpenseClaimSummaryFragment) {
            return "Expense Claim Summary";
        } else if (fragment instanceof AdvanceRequestSummaryFragment) {
            return "Advance Summary";
        } else if (fragment instanceof ExpenseApprovalFragment) {
            return "Expense Approval";
        } else if (fragment instanceof AdvanceApprovalFragment) {
            return "Advance Approval";
        } else {
            return "";
        }
    }

    public void showHideActionBar(boolean isShow) {
        View actionBarView = findViewById(R.id.fl_actionBarContainer);
        if (isShow) {
            if (actionBarView != null) {
                actionBarView.setVisibility(View.VISIBLE);
            }
            getSupportActionBar().show();
            getSupportActionBar().setShowHideAnimationEnabled(true);
            if (mDrawerLayout != null)
                mDrawerLayout
                        .setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            if (actionBarView != null) {
                actionBarView.setVisibility(View.GONE);
            }
            getSupportActionBar().hide();
            getSupportActionBar().setShowHideAnimationEnabled(true);
            if (mDrawerLayout != null)
                mDrawerLayout
                        .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onBackPressed() {
        if (isAnimationLoaded) {

            if (mDrawerLayout.isDrawerOpen(mNavigationDrawerFragment.getView())) {
                mDrawerLayout.closeDrawers();
                return;
            }
            try {
                Fragment fragment = getTopFragment();
                if (fragment != null
                        && (fragment instanceof LoginFragment
                        || fragment instanceof SplashFragment || fragment instanceof HomeFragment)) {
                    long t = System.currentTimeMillis();
                    if (t - backPressedTime > 2000) {
                        backPressedTime = t;
                        Toast.makeText(this, "Press back again to exit",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        finish();

                    }

                } else if (fragment != null && (fragment instanceof StoreListFragment || fragment instanceof LeaveBalanceDetailFragment || fragment instanceof AttendanceHistory || fragment instanceof AttandanceFragment || fragment instanceof PaySlipDownloadFragment || fragment instanceof ApproveScreen || fragment instanceof UserProfile || fragment instanceof ChangePasswordFragment || fragment instanceof MarkAttendance)) {
                    performUserAction(IAction.HOME_VIEW, null, null);
                } else if (fragment instanceof TeamMemberList) {
                    if (TeamMember.loopCount == 0) {
                        performUserAction(IAction.HOME_VIEW, null, null);
                    } else {
                        TeamMember.loopCount--;
                        TeamMember.setmCurrentEmpId(TeamMember.getmPreviousEmpId());
                        Bundle b = new Bundle();
                        b.putString(AppsConstant.OPTION_SELECTED, "subTeamMembers");
                        performUserAction(IAction.TEAM_MEMBER_LIST, null, b);
                    }
                } else {
                    super.onBackPressed();
                }
            } catch (Exception exception) {

                Crashlytics.log(exception.getMessage());
                Crashlytics.logException(exception);
            }
        }
    }

    public static void updataProfileData(Context context, View rootView) {
        EmployeeProfileModel model = ModelManager.getInstance().getEmployeeProfileModel();

        if (model != null) {
            Preferences preferences = new Preferences(context);
            int bgColor = Utility.getBgColorCode(context, preferences);
            int bgTextColor = Utility.getTextColorCode(preferences);
            toolbar.setTitleTextColor(bgTextColor);
            toolbar.setBackgroundColor(bgColor);
            ibRight.setBackgroundColor(bgColor);
            Utility.setCorpBackground(context, rootView);

            ((TextView) rootView.findViewById(R.id.tv_department)).setVisibility(View.GONE);
            ((TextView) rootView.findViewById(R.id.tv_role)).setVisibility(View.GONE);


            ((TextView) rootView.findViewById(R.id.tv_profile_name)).setText(model.getmName());
            if (model.getmDepartmentYN().equalsIgnoreCase("Y")) {
                ((TextView) rootView.findViewById(R.id.tv_department)).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.tv_department)).setText(context.getString(R.string.msg_department) + " " + model.getmDepartment());
            }
            if (model.getmDesignationYN().equalsIgnoreCase("Y")) {
                ((TextView) rootView.findViewById(R.id.tv_role)).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.tv_role)).setText((context.getString(R.string.msg_role) + " " + model.getmRole()));
            }
            ((TextView) rootView.findViewById(R.id.tv_employee)).setText(context.getString(R.string.msg_employee_id) + " " + model.getmEmpCode());
            String profilePhoto = CommunicationConstant.getMobileCareURl() + model.getmImageUrl();
            Picasso picasso = Picasso.with(context);
            picasso.invalidate(profilePhoto);
            picasso.load(profilePhoto).fit().into((ImageView) rootView.findViewById(R.id.img_user_img));
            Log.d("Image", CommunicationConstant.getMobileCareURl() + model.getmImageUrl());
        } else {
            ((TextView) rootView.findViewById(R.id.tv_profile_name)).setText("Guest");
            ((TextView) rootView.findViewById(R.id.tv_department)).setText(context.getString(R.string.msg_department) + "NA");
            ((TextView) rootView.findViewById(R.id.tv_role)).setText(context.getString(R.string.msg_role) + "NA");
            ((TextView) rootView.findViewById(R.id.tv_employee)).setText(context.getString(R.string.msg_employee_id) + "0000");
        }
    }


    @Override
    public void validateResponse(ResponseData response) {
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_LOGOUT_USER:
                showHideProgress(false);
                try {
                    JSONObject jsonObject = new JSONObject(response.getResponseData());
                    if (!jsonObject.getJSONObject("LogOutUserResult").optBoolean("IsLoggedIn", true)) {
                        ModelManager.getInstance().logutUser();
                        setMenuList(new ArrayList<String>());
                        performUserAction(IAction.LOGIN_VIEW, null, true);
                        Preferences preferences = new Preferences(getApplicationContext());
                        preferences.clearPreferences();
                        preferences.commit();
                        EventDataSource dataSource = new EventDataSource(getApplicationContext());
                        dataSource.clearDatabase();
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

    public void showHideProgress(boolean isShow) {
        View view = findViewById(R.id.ll_progress_container);
        Log.d(MainActivity.TAG, "View found in show progress for activity: " + view);
        if (view != null) {
            view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }

    }


    public void updateNavigation() {
        mNavigationDrawerFragment.updateDrawerData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if ((fragment instanceof AddExpenseFragment)||
                    (fragment instanceof AdvanceRequestFragment) || (fragment instanceof EditAdvanceApprovalFragment) ||
                    (fragment instanceof AdvanceApprovalFragment) || (fragment instanceof EditViewExpenseClaimFragment)
                    || (fragment instanceof EditExpenseApprovalFragment) || (fragment instanceof OutdoorDutyRequestFragment)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
