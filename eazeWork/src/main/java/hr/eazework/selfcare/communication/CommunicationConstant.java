package hr.eazework.selfcare.communication;


import android.os.Build;

public class CommunicationConstant {
    public static final String EAZE_WORK_REQUEST_DEMO_URL = "http://www.eazework.com/contact-us/request-a-demo.aspx";
    private static final String SERVER_LOCATION_Stagging = "www.eazework.com/stage";//"/mc_dev/selfcare/v2/proxy/";
	private static final String SERVER_LOCATION_Test = "www.eazework.net/test";
	public static String UrlFile="http://www.eazework.net/test";
	public static String PROJECT_PREFERENCE="www.eazework.net/test";
	private static final String SERVER_LOCATION_Production = "www.eazework.com";
	private static boolean isProduction=false;
	private static boolean isTestServer=true;
	private static String SERVER_LOCATION_BASE=(isTestServer?SERVER_LOCATION_Test:isProduction?SERVER_LOCATION_Production:SERVER_LOCATION_Stagging);
	public static final int TIME_OUT_DURATION = 60 * 1000;

    public static void setServerLocation(boolean isTestServer, boolean isProduction) {
        CommunicationConstant.SERVER_LOCATION_BASE=(isTestServer?SERVER_LOCATION_Test:isProduction?SERVER_LOCATION_Production:SERVER_LOCATION_Stagging);
    }

	public static final boolean ByPass_Build = false;
	public static String userMSISDN = null;
	public static String forgotPassMSISDN = null;
	public static String appPlatformName = null;
	public static String appPlatformVersion = Build.VERSION.RELEASE;
	public static String appClientVersion = null;
	public static String appDeviceResolution = null;
	public static String passwordFlag = null;
	public static String API_Key = "ide83c8c20-ed72-4253-8813-8729fe2a316b";

	public static String API_KEY_LOGIN_USER = "LoginService.svc/LogInUser";
	public static String API_KEY_LOGOUT_USER = "LoginService.svc/LogOutUser";
	public static String API_KEY_CHANGE_PASSWORD = "LoginService.svc/ChangePassword";
	public static String API_KEY_MARK_ATTANDANCE = "AttendanceService.svc/MarkAttendance";
	public static String API_KEY_ATTANDANCE_STATUS = "AttendanceService.svc/AttendanceStatus";
	public static String API_KEY_VALIDATE_LOGIN = "LoginService.svc/ValidateLogIn";
	public static String API_KEY_VALIDATE_PASSWORD = "LoginService.svc/ValidatePassword";
	public static final String API_KEY_EMP_LEAVE_BALANCE ="LeaveService.svc/GetEmpLeaveBalance";
	public static final String API_KEY_EMP_LEAVES ="LeaveService.svc/GetEmpLeaves";
	public static final String API_KEY_LEAVE_EMP_LIST = "LeaveService.svc/GetLeaveEmpList";
	public static final String API_KEY_LEAVE_REQ_TOTAL_DAY ="LeaveService.svc/GetLeaveReqTotalDays";
	public static final String API_KEY_EMP_RH_LEAVES = "LeaveService.svc/GetEmpRHLeaves";
	public static final String API_KEY_SAVE_LEAVE_REQUEST ="LeaveService.svc/SaveLeaveReq";
	public static final String API_KEY_USER_PROFILE_DETAILS ="LoginService.svc/EmpProfile";
	public static final String API_KEY_SALARY_SLIP_MONTH ="SalarySlipService.svc/SalaryMonth";
	public static final String API_KEY_GET_EMP_LEAVE_REQUESTS ="LeaveService.svc/GetEmpLeaveRequests";
	public static final String API_KEY_GET_EMP_LEAVE_BALANCES ="LeaveService.svc/GetEmpLeaveBalances";
	public static final String API_KEY_MENU_DATA ="LoginService.svc/GetMenuData";
	public static final String API_KEY_EMP_ATTENDANCE_CALENDER_STATUS ="AttendanceService.svc/GetEmpAttendanceCalendarStatus";
	public static final String API_KEY_GET_EMP_PENDING_APPROVAL_REQ_COUNT ="LeaveService.svc/GetEmpPendingApprovalReqCount";
	public static final String API_KEY_GET_EMP_PENDING_APPROVAL_REQUESTS ="LeaveService.svc/GetEmpPendingApprovalReqs";
	public static final String API_KEY_GET_UPDATE_PENDING_APPROVAL_STATUS ="LeaveService.svc/UpdateEmpPendingReq";
	public static final String API_KEY_GET_SALARY_SLIP_DATA ="SalarySlipService.svc/SalarySlipData";
    public static final String API_KEY_LOCATION_LIST = "LocationService.svc/GetLocationList";
	public static final String API_KEY_LOCATION_DETAIL = "LocationService.svc/GetLocationDetails";
    public static final String API_KEY_LOCATION_UPDATE = "LocationService.svc/UpdateLocation";
    public static final String API_KEY_TYPE_WISE_LIST = "CommonService.svc/GetTypeWiseList";
	public static final String API_KEY_TEAM_MEMBER_LIST = "TeamService.svc/GetTeamMemberList";
	public static final String API_KEY_ATTENDANCE_TIMELINE = "AttendanceService.svc/GetAttendanceTimeLine";
    public static final String API_KEY_EMPLOYEE_DETAIL = "TeamService.svc/GetEmployeeDetails";
    public static final String API_KEY_MEMBER_INPUT_FIELD = "TeamService.svc/GetMemberReqInputFields";
    public static final String APT_KEY_PENDING_MEMBER_APPROVAL = "TeamService.svc/GetPendingMemberReqList";
    public static final String API_KEY_MEMBER_PENDING_APPROVAL_STATUS = "TeamService.svc/UpdateMemberApprovalRejection";
    public static final String API_KEY_APPROVALS_COUNT = "CommonService.svc/GetEmpPendingApprovalCount";
    public static final String API_KEY_UPDATE_EMPLOYEE_DETAILS = "TeamService.svc/UpdateEmployeeDetails";
    public static final String APT_KEY_CREATE_EMPLOYEE = "TeamService.svc/UpdateMemberReqInputFields";
    public static final String API_KEY_GET_HOME_DATA = "CommonService.svc/GetHomeData";
	public static final String API_KEY_GET_ADVANCE_REQUEST_SUMMARY="ExpenseService.svc/GetAdvanceRequestSummary";
	public static final String API_KEY_GET_ADVANCE_DETAIL="ExpenseService.svc/GetAdvanceDetail";
	public static final String API_KEY_GET_ADVANCE_PAGE_INIT="ExpenseService.svc/GetAdvancePageInit";
	public static final String API_KEY_SAVE_ADVANCE="ExpenseService.svc/SaveAdvance";
	public static final String API_KEY_EXPENSE_CLAIM_SUMMARY="ExpenseService.svc/GetEmpExpense";
	public static final String API_KEY_GET_EXPENSE_CLAIM_DETAIL="ExpenseService.svc/GetExpenseDetail";
	public static final String API_KEY_GET_EXPENSE_PAGE_INIT="ExpenseService.svc/GetExpensePageInit";
	public static final String API_KEY_GET_APPROVER_DETAILS="ExpenseService.svc/GetApproverDetails";
	public static final String API_KEY_GET_PROJECT_LIST_DETAILS="ExpenseService.svc/EvtClaimTypeChange";
	public static final String API_KEY_GET_CATEGORY_LIST_DETAILS="ExpenseService.svc/GetCategoryList";
	public static final String API_KEY_GET_HEAD_DETAILS_WITH_POLICY="ExpenseService.svc/GetHeadDetailsWithPolicy";
	public static final String API_KEY_GET_SAVE_EXPENSE="ExpenseService.svc/SaveExpense";
	public static final String API_KEY_GET_ADVANCE_LIST_FOR_EXPENSE="ExpenseService.svc/GetAdvanceListForExpense";
	public static final String API_KEY_GET_EMPLOYEE_EXPENSE_APPROVAL="ExpenseService.svc/GetEmpExpenseApproval";
	public static final String API_KEY_GET_EMPLOYEE_ADVANCE_APPROVAL="ExpenseService.svc/GetEmpAdvanceApproval";
	public static final String API_KEY_GET_ADVANCE_APPROVAL_ROLE="ExpenseService.svc/GetAdvanceApprovalRole";
	public static final String API_KEY_SEARCH_ONBEHALF="LeaveService.svc/GetLeaveEmpList";
	public static final String API_KEY_GET_MONTH_LIST="ExpenseService.svc/ValidateMonthListForPeriodicExpense";
	//public static final String API_KEY_GET_MONTH_LIST="ExpenseService.svc/ValidateMonthListForPeriodicExpense";
	public static final String API_KEY_GET_CORPEMP_PARAM="CommonService.svc/GetCorpEmpParam";
	public static final String API_KEY_GET_WFH_EMP_LIST="WFHService.svc/GetWFHEmpList";
	public static final String API_KEY_GET_TOUR_EMP_LIST="TourService.svc/GetTourEmpList";
	public static final String API_KEY_GET_OD_EMP_LIST="ODService.svc/GetODEmpList";
	public static final String API_KEY_SAVE_OD_REQUEST="ODService.svc/SaveODReq";
	public static final String API_KEY_GET_DETAILS_ON_INPUT_CHANGE="WFHService.svc/GetDetailsOnInputChange";
	public static final String API_KEY_SAVE_WFH_REQUEST="WFHService.svc/SaveWFHReq";
	public static final String API_KEY_GET_DETAILS_ON_EMP_CHANGE="TourService.svc/GetDetailsOnEmpChange";
	//public static final String API_KEY_GET_EMP_WFH_REQUEST="WFHService.svc/GetEmpWFHRequests";
	public static final String API_KEY_GET_WFH_REQUEST_DETAIL="WFHService.svc/GetWFHRequestDetail";
	public static final String API_KEY_GET_OD_REQUEST_DETAIL="ODService.svc/GetODRequestDetail";
	public static final String API_KEY_GET_TOUR_CUSTOM_FIELD_LIST="TourService.svc/GetTourRequestCustomFieldList";
	public static final String API_KEY_GET_EMP_ATTENDANCE_REQUEST="AttendanceService.svc/GetEmpAttendanceRequests";
	public static final String API_KEY_WITHDRAW_WFH_REQUEST="WFHService.svc/WithdrawWFHRequest";
	public static final String API_KEY_WITHDRAW_OD_REQUEST="ODService.svc/WithdrawODRequest";
	public static final String API_KEY_GET_TOUR_REQUEST_DETAIL="TourService.svc/GetTourRequestDetail";
	public static final String API_KEY_WITHDRAW_TOUR_REQUEST="TourService.svc/WithdrawTourRequest";
	public static final String API_KEY_SAVE_TOUR_REQUEST="TourService.svc/SaveTourReq";
	public static final String API_KEY_REJECT_TOUR_REQUEST="TourService.svc/RejectTourRequest";
	public static final String API_KEY_REJECT_OD_REQUEST="ODService.svc/RejectODRequest";
	public static final String API_KEY_REJECT_WFH_REQUEST="WFHService.svc/RejectWFHRequest";
	public static final String API_KEY_APPROVE_WFH_REQUEST="WFHService.svc/ApproveWFHRequest";
	public static final String API_KEY_APPROVE_TOUR_REQUEST="TourService.svc/ApproveTourRequest";
	public static final String API_KEY_APPROVE_OD_REQUEST="ODService.svc/ApproveODRequest";
	public static final String API_KEY_TIME_MODIFICATION_REQUEST="AttendanceService.svc/TimeModification";
	public static final String API_KEY_APPROVE_LEAVE_REQUEST="LeaveService.svc/ApproveLeaveRequest";
	public static final String API_KEY_REJECT_LEAVE_REQUEST="LeaveService.svc/RejectLeaveRequest";
	public static final String API_KEY_GET_LEAVE_REQUEST_DETAIL="LeaveService.svc/GetLeaveRequestDetails";
	public static final String API_KEY_WITHDRAW_LEAVE_REQUEST="LeaveService.svc/WithdrawLeaveRequest";
	public static final String API_KEY_BACKDATED_ATTENDANCE_REQUEST="AttendanceService.svc/BackDateAttendance";
	public static final String API_KEY_FORGOT_CREDENTIALS_REQUEST="EmployeeService.svc/ForgetCredentials";
	public static final String API_KEY_GET_ATTENDANCE_DETAIL="AttendanceService.svc/GetAttendanceReqDetail";
	public static final String API_KEY_REJECT_ATTENDANCE_REQUEST="AttendanceService.svc/RejectRequest";
	public static final String API_KEY_APPROVE_ATTENDANCE_REQUEST="AttendanceService.svc/ApproveRequest";
	public static final String API_KEY_PENDING_APPROVAL_ATTENDANCE_REQUEST="AttendanceService.svc/GetEmpPendingApprovalAttendReqs";
	public static final String API_KEY_GET_HOLIDAY_LIST="CommonService.svc/GetHolidayList";
	public static final String API_KEY_LOGIN_USER_WITH_GOOGLE="LoginService.svc/LoginUserWithGoogle";
	public static final String API_KEY_UPLOAD_PROFILE_PIC="EmployeeService.svc/UploadProfilePic";
	public static final String API_KEY_GET_EMP_ATTENDANCE_DETAIL="AttendanceService.svc/GetEmpAttendanceDetail";


	public static final int API_LOGIN_USER = 0;
	public static final int API_LOGOUT_USER = 1;
	public static final int API_CHANGE_PASSWORD = 2;
	public static final int API_MARK_ATTANDANCE = 3;
	public static final int API_ATTANDANCE_STATUS = 4;
	public static final int API_VALIDATE_LOGIN = 5;
	public static final int API_EMP_LEAVE_BALANCE = 6;
	public static final int API_EMP_LEAVES = 7;
	public static final int API_LEAVE_EMP_LIST = 8;
	public static final int API_LEAVE_REQ_TOTAL_DAY = 9;
	public static final int API_EMP_RH_LEAVES = 10;
	public static final int API_SAVE_LEAVE_REQUEST = 11;
	public static final int API_USER_PROFILE_DETAILS = 12;
	public static final int API_SALARY_SLIP_MONTH = 13;
	public static final int API_GET_EMP_LEAVE_REQUESTS_APPROOVED = 14;
	public static final int API_GET_EMP_LEAVE_REQUESTS_PENDING = 15;
	public static final int API_GET_EMP_LEAVE_BALANCES = 16;
	public static final int API_GET_MENU_DATA = 17;
	public static final int API_GET_EMP_ATTENDANCE_CALENDER_STATUS = 18;
	public static final int API_VALIDATE_PASSWORD = 19;
	public static final int API_GET_EMP_PENDING_APPROVAL_REQ_COUNT = 20;
	public static final int API_GET_EMP_PENDING_APPROVAL_REQUESTS = 21;
	public static final int API_GET_UPDATE_PENDING_APPROVAL_STATUS = 22;
	public static final int API_GET_SALARY_SLIP_DATA = 23;
	public static final int API_GET_EMP_LEAVE_REQUESTS_CONSUMED = 24;
    public static final int API_GET_LOCATION_LIST = 25;
	public static final int API_GET_LOCATION_DETAIL = 26;
    public static final int API_GET_LOCATION_UPDATE = 27;
    public static final int API_GET_TYPE_WISE_LIST = 28;
    public static final int API_GET_TEAM_MEMBER_LIST = 29;
	public static final int API_GET_ATTENDANCE_TIMELINE = 30;
    public static final int API_GET_EMPLOYEE_DETAIL = 31;
    public static final int API_GET_MEMBER_INPUT_FIELD = 32;
    public static final int APT_GET_PENDING_MEMBER_APPROVAL = 33;
    public static final int API_GET_MEMBER_PENDING_APPROVAL_STATUS = 34;
    public static final int API_GET_APPROVALS_COUNT = 35;
    public static final int API_GET_UPDATE_EMPLOYEE_DETAILS = 36;
    public static final int APT_GET_CREATE_EMPLOYEE = 37;
    public static final int API_GET_HOME_DATA = 38;
	public static final int API_GET_ADVANCE_REQUEST_SUMMARY=39;
	public static final int API_GET_ADVANCE_DETAIL=40;
	public static final int API_GET_ADVANCE_PAGE_INIT=41;
	public static final int API_GET_SAVE_ADVANCE=42;
	public static final int API_GET_EXPENSE_CLAIM_SUMMARY=43;
	public static final int API_GET_EXPENSE_CLAIM_DETAIL=44;
	public static final int API_GET_EXPENSE_PAGE_INIT=45;
	public static final int API_GET_APPROVER_DETAILS=46;
	public static final int API_GET_PROJECT_LIST_DETAILS=47;
	public static final int API_GET_CATEGORY_LIST_DETAILS=48;
	public static final int API_GET_HEAD_DETAILS_WITH_POLICY=49;
	public static final int API_GET_SAVE_EXPENSE=50;
	public static final int API_GET_ADVANCE_LIST_FOR_EXPENSE=51;
	public static final int API_GET_EMPLOYEE_EXPENSE_APPROVAL=52;
	public static final int API_GET_EMPLOYEE_ADVANCE_APPROVAL=53;
	public static final int API_GET_ADVANCE_APPROVAL_ROLE=54;
	public static final int API_SEARCH_ONBEHALF=55;
	public static final int API_GET_MONTH_LIST=56;
	public static final int API_GET_CORPEMP_PARAM=57;
	public static final int API_GET_WFH_EMP_LIST=58;
	public static final int API_GET_TOUR_EMP_LIST=59;
	public static final int API_GET_OD_EMP_LIST=60;
	public static final int API_GET_DETAILS_ON_INPUT_CHANGE=61;
	public static final int API_SAVE_OD_REQUEST=62;
	public static final int API_SAVE_WFH_REQUEST=63;
	public static final int API_GET_DETAILS_ON_EMP_CHANGE=64;
	public static final int	API_GET_EMP_ATTENDANCE_REQUEST=65;
	public static final int	API_GET_OD_REQUEST_DETAIL=66;
	public static final int	API_GET_WFH_REQUEST_DETAIL=67;
	public static final int	API_WITHDRAW_WFH_REQUEST=68;
	public static final int	API_WITHDRAW_OD_REQUEST=69;
	public static final int	API_GET_TOUR_CUSTOM_FIELD_LIST=70;
	public static final int	API_GET_TOUR_REQUEST_DETAIL=71;
	public static final int	API_WITHDRAW_TOUR_REQUEST=72;
	public static final int API_SAVE_TOUR_REQUEST=73;
	public static final int API_REJECT_TOUR_REQUEST=74;
	public static final int API_REJECT_OD_REQUEST=75;
	public static final int API_REJECT_WFH_REQUEST=76;
	public static final int API_APPROVE_WFH_REQUEST=77;
	public static final int API_APPROVE_TOUR_REQUEST=78;
	public static final int API_APPROVE_OD_REQUEST=79;
	public static final int API_TIME_MODIFICATION_REQUEST=80;
	public static final int API_APPROVE_LEAVE_REQUEST=81;
	public static final int API_REJECT_LEAVE_REQUEST=82;
	public static final int API_GET_LEAVE_REQUEST_DETAIL=83;
	public static final int	API_WITHDRAW_LEAVE_REQUEST=84;
	public static final int	API_BACKDATED_ATTENDANCE_REQUEST=85;
	public static final int	API_FORGOT_CREDENTIALS_REQUEST=86;
	public static final int API_GET_ATTENDANCE_DETAIL=87;
	public static final int API_REJECT_ATTENDANCE_REQUEST=88;
	public static final int API_APPROVE_ATTENDANCE_REQUEST=89;
	public static final int API_PENDING_APPROVAL_ATTENDANCE_REQUEST=90;
	public static final int API_GET_HOLIDAY_LIST=91;
	public static final int API_LOGIN_USER_WITH_GOOGLE=92;
	public static final int API_UPLOAD_PROFILE_PIC=93;
	public static final int API_GET_EMP_ATTENDANCE_DETAIL=94;

	public static String ADD_EXPENSE_RESPONSE="addExpense";


	public static boolean isProduction() {
        return isProduction;
    }

    public static boolean isTestServer() {
        return isTestServer;
    }

    public static void setIsProduction(boolean isProduction) {
        CommunicationConstant.isProduction = isProduction;
    }

    public static void setIsTestServer(boolean isTestServer) {
        CommunicationConstant.isTestServer = isTestServer;
    }

    // ///SERVER RESPONSE ERROR CODES///
	public final int SUCCESS = 0;
	public final int INVALID_REQUEST = 1;
	public final int INTERNAL_SERVER_ERROR = 2;
	public final int INTEGRATION_COMPONENT_ERROR = 3;
	public final int REGISTRATION_ERROR = 4;
	public final int INVALID_OPERATOR_MSISDN = 5;
	public final int ALREADY_REGISTERED_MSISDN = 6;
	public final int USER_ID_ALREADY_REGISTERED = 7;
	public final int TIME_OUT = 8;
	public final int NO_DATA_AVAILABLE = 9;
	public final int REQUEST_BLOCKED = 10;
	public final int MSISDN_NOT_PRESENT = 11;
	public final int REGISTRATION_PARTIAL = 12;
	public final int DEVICE_CLIENT_ID_EXPIRED = 13;
	public final int API_SECRET_EXPIRED = 14;
	public final int FORCE_LOGOUT = 15;
	public final int FLUSH_CACHE = 16;
	public final int MANDATORY_PARAMETER_MISSING = 17;
	public final int MANDATORY_PARAMETER_INCORRECT = 18;
	public final int UNKNOWN_ERROR = 19;
	public final int ALGO_NOT_SUPPORTED = 20;
	public final int AUTHENTICATION_ERROR = 22;
	
	/**
	 * 
	 * @param isHTTPS
	 * @param requestID
	 * @return
	 */
	public static String getMobileCareURl() {
		String URL="";
		if(isTestServer){
			URL="http://";
		}
		else {
			URL="https://";
		}
			URL+=SERVER_LOCATION_BASE;
			
		return URL;
	}

}
