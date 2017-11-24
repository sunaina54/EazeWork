package hr.eazework.com.model;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import hr.eazework.com.application.MyApplication;
import hr.eazework.com.ui.util.MLogger;
import hr.eazework.mframe.caching.manager.DataCacheManager;


public class ModelManager {
	public static int MSISDN = 12001;
	public static int CLIENT_ID = 12002;
	// public static int PASSWORD = 12003;
	// public static int USER_ID = 12004;
	public static int USER_CACHING = 12005;
	public final static int DB_KEY_REG_PARTIAL_DONE = 15001;
	public final static int DB_KEY_REG_DONE = 15002;
	public final static int DB_KEY_MSISDN = 15003;
	public final static int DB_PRIM_MSISDN = 15004;
	public final static int DB_PRIM_DCID = 15005;
	public final static int DB_DELETED_NO = 15006;

	public final static int DB_SEC_PARTIAL_MSISDN = 15007;
	public final static int DB_SEC_PARTIAL_ALIAS = 15008;
	public final static int DB_SEC_PARTIAL_DCID = 15009;
	public final static int DB_SEC_PARTIAL_REG_DONE = 15010;
	public final static int DB_SEC_FULL_REG_DONE = 15011;

	public final static int DB_GPRS_NO = 150012;
	public final static int MSG_OTP = 150013;
	public final static int MSG_OTP_SEC = 150014;

	public final static String CLEARALL = "CLEARALL";

	public final static int DB_ALL_NUMBER_LIST = 150015;

	// public static int DB_UPDATED = 12006;
	private static ModelManager obj = null;
	private  LoginUserModel loginUserModel;
	private  LeaveBalanceModel leaveBalanceModel;
	private  CheckInOutModel checkInOutModel;
	private  EmployeeProfileModel employeeProfileModel;
	private  SalaryMonthModel salaryMonthModel;
	private EmpLeaveModel empLeaveModel;
	private LeaveModel approvedLeaveModel;
	private LeaveModel pendingLeaveModel;
	private LeaveTypeModel leaveTypeModel;
	private MenuItemModel menuItemModel;
	private int userTotalPendingRequests;
    private int totalPendingMemmberApprovals;
	private EmployeeLeaveModel employeeLeaveModel;
	private SalarySlipDataModel salarySlipDataModel;
	private LeaveModel consumedLeaveModel;
    private EmployeeDetailModel employeeDetailModel;
    private MemberApprovalModel memberApprovalModel;
    private PendingCountModel pendingCountModel;
    private TypeWiseListModel locationCountModel;
	private ExpenseStatusModel expenseStatusModel;




	private ModelManager() {
	}

	public static ModelManager getInstance() {
		if (obj == null) {
			obj = new ModelManager();
		}
		return obj;
	}

	public void clearSetData(boolean isProfileClear) {

	}

	public void clearALL() {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(MyApplication.getAppContext());
		Editor editor = preferences.edit();
		preferences = PreferenceManager
				.getDefaultSharedPreferences(MyApplication.getAppContext());
		editor = preferences.edit();
		editor.putBoolean("tc", false);
		editor.commit();
		// editor.putString("updated", "FALSE");
		// editor.commit();

		DataCacheManager.getInstance().removeCacheDataByID(
				ModelManager.DB_KEY_MSISDN);

		DataCacheManager.getInstance().removeCacheDataByID(
				ModelManager.CLIENT_ID);

		DataCacheManager.getInstance().clearCache();
		// CommunicationManager.setMsisdn(null);
		// CommunicationManager.setDevice_client_id(null);
	}

	public static void setInstance(ModelManager objectModelMgr) {
		try {
			if (obj == null && objectModelMgr != null) {
				MLogger.debug("ModelManager",
						"Assigning Model Manger Object From DB");
				obj = objectModelMgr;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



    public EmployeeDetailModel getEmployeeDetailModel() {
        return employeeDetailModel;
    }

    public void setEmployeeDetailModel(String response) {
        employeeDetailModel = new EmployeeDetailModel(response);
    }

    public LoginUserModel getLoginUserModel() {
		return loginUserModel;
	}

	public void setLoginUserModel(String response) {
		loginUserModel = new LoginUserModel(response);
	}

	public LeaveBalanceModel getLeaveBalanceModel() {
		return leaveBalanceModel;
	}

	public  void setLeaveBalanceModel(String response) {
		leaveBalanceModel = new LeaveBalanceModel(response);
	}

    public TypeWiseListModel getLocationCountModel() {
        return locationCountModel;
    }

    public void setLocationCountModel(TypeWiseListModel locationCountModel) {
        this.locationCountModel = locationCountModel;
    }

    public  CheckInOutModel getCheckInOutModel() {
		return checkInOutModel;
	}

	public  void setCheckInOutModel(String response) {
		checkInOutModel = new CheckInOutModel(response);
	}

	public  EmployeeProfileModel getEmployeeProfileModel() {
		return employeeProfileModel;
	}

	public  void setEmployeeProfileModel(String jsonString) {
		employeeProfileModel = new EmployeeProfileModel(jsonString);
	}

	public void setEmployeeProfileModel(JSONObject jsonObject){
        employeeProfileModel = new EmployeeProfileModel(jsonObject);
    }

	public  SalaryMonthModel getSalaryMonthModel() {
		return salaryMonthModel;
	}


	public  void setSalaryMonthModel(String jsonStrin) {
		salaryMonthModel = new SalaryMonthModel(jsonStrin);
	}

	public ExpenseStatusModel getExpenseStatusModel() {
		return expenseStatusModel;
	}

	public void setExpenseStatusModel(String jsonStrin) {
	expenseStatusModel=ExpenseStatusModel.create(jsonStrin);
	}

	public  void setEmpLeaveModel(String jsonStrin) {
		empLeaveModel = new EmpLeaveModel(jsonStrin);
	}

	public  EmpLeaveModel getEmpLeaveModel() {
		return empLeaveModel;
	}

	public  void setApprovedLeaveModel(String jsonStrin) {
		approvedLeaveModel = new LeaveModel(jsonStrin);
	}

    public PendingCountModel getPendingCountModel() {
        return pendingCountModel;
    }

    public void setPendingCountModel(PendingCountModel pendingCountModel) {
        this.pendingCountModel = pendingCountModel;
    }

    public  LeaveModel getApprovedLeaveModel() {
		return approvedLeaveModel;
	}

	public  void setConsumedLeaveModel(String jsonStrin) {
		consumedLeaveModel = new LeaveModel(jsonStrin);
	}

	public  LeaveModel getConsumedLeaveModel() {
		return consumedLeaveModel;
	}
	public  void setPendingLeaveModel(String jsonStrin) {
		pendingLeaveModel = new LeaveModel(jsonStrin);
	}

	public  LeaveModel getPendingLeaveModel() {
		return pendingLeaveModel;
	}
	public  void logutUser(){
        loginUserModel = null;
        leaveBalanceModel = null;
        checkInOutModel = null;
        employeeProfileModel = null;
        salaryMonthModel = null;
        empLeaveModel = null;
        approvedLeaveModel = null;
        pendingLeaveModel = null;
        leaveTypeModel = null;
        menuItemModel = null;

        employeeLeaveModel = null;
        salarySlipDataModel = null;
        consumedLeaveModel = null;
        employeeDetailModel = null;
        memberApprovalModel = null;
        pendingCountModel = null;
        locationCountModel = null;
		expenseStatusModel=null;
        obj = null;

    }

	public LeaveTypeModel getLeaveTypeModel() {
		return leaveTypeModel;
	}

	public void setLeaveTypeModel(LeaveTypeModel leaveTypeModel) {
		this.leaveTypeModel = leaveTypeModel;
	}

	public MenuItemModel getMenuItemModel() {
		return menuItemModel;
	}

	public void setMenuItemModel(JSONArray array) {
		this.menuItemModel = new MenuItemModel(array);
	}

	public int getUserTotalPendingRequests() {
		return userTotalPendingRequests;
	}

	public void setUserTotalPendingRequests(int userTotalPendingRequests) {
		this.userTotalPendingRequests = userTotalPendingRequests;
	}

    public int getTotalPendingMemmberApprovals() {
        return totalPendingMemmberApprovals;
    }

    public void setTotalPendingMemmberApprovals(int totalPendingMemmberApprovals) {
        this.totalPendingMemmberApprovals = totalPendingMemmberApprovals;
    }

    public EmployeeLeaveModel getEmployeePendingLeaveModel() {
		return employeeLeaveModel;
	}

	public void setEmployeePendingLeaveModel(EmployeeLeaveModel employeeLeaveModel) {
		this.employeeLeaveModel = employeeLeaveModel;
	}

	public SalarySlipDataModel getSalarySlipDataModel() {
		return salarySlipDataModel;
	}

	public void setSalarySlipDataModel(SalarySlipDataModel salarySlipDataModel) {
		this.salarySlipDataModel = salarySlipDataModel;
	}
	public MemberApprovalModel getMemberApprovalModel() {
        return memberApprovalModel;
    }
    public void setMemberApprovalModel(MemberApprovalModel memberApprovalModel){
        this.memberApprovalModel = memberApprovalModel;
    }
	
}
