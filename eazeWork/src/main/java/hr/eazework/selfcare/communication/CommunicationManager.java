package hr.eazework.selfcare.communication;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


import hr.eazework.com.ui.util.MLogger;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.CommunicationManagerBase;
import hr.eazework.mframe.communication.IENUMCommunication;
import hr.eazework.mframe.communication.RequestData;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.mframe.communication.ResponseDataAcceptType;
import hr.eazework.selfcare.communication.IBaseResponse;

public class CommunicationManager extends CommunicationManagerBase {
    private static final String TAG = CommunicationManager.class.getName();
    private static boolean staticResponse;
    private static String msisdn = "";
    String url1 = "";
    public static String msisdn_forg = "";
    private static String device_client_id = "";
    private static String DEVICE_RESOLUTION = "";
    static CommunicationManager instance;
    Dialog progressDialog;
    static HashMap<Integer, Dialog> dialogMap;

    private boolean primaryNeeded = false;

    private boolean SECURE_CONNECTION = false;

    public static CommunicationManager getInstance() {
        if (instance == null) {
            instance = new CommunicationManager();
            dialogMap = new HashMap<Integer, Dialog>();
        }
        return instance;
    }

    public void sendPostRequest(IBaseResponse objListener,String jsonDAta, int requestType, boolean isRefresh) {
        Vector<Object> requestVector = new Vector<Object>();
        requestVector.add(getURL(requestType, false));
   //     Log.d(TAG,"This Service is called " + getURL(requestType, false) + " at " + objListener.toString());
        requestVector.add(jsonDAta);
        requestVector.add(requestType);
        sendPostRequest(requestVector, objListener, true, isRefresh);
    }

    // ////////////////////////////////////////////////////////////////////////////////////
    public void sendGetRequest(Vector<Object> vReqParam,
                               IBaseResponse objListner, boolean isAnimate, boolean isRefresh) {
        try {
            sendRequest(vReqParam, objListner, IENUMCommunication.REQ_GET,
                    isAnimate, isRefresh);
        } catch (Exception e) {
        }
    }

    public void sendPostRequest(Vector<Object> vReqParam,
                                IBaseResponse objListner, boolean isAnimate, boolean isRefresh) {
        try {
            sendRequest(vReqParam, objListner, IENUMCommunication.REQ_POST, isAnimate, isRefresh);
        } catch (Exception e) {
            MLogger.debug(getClass().getName(), "Exception In Send Post Req::"
                    + e);
        }

    }

    // private void sendRequest(Vector<Object> vReqParam, Object obj, int
    // reqType,
    // boolean isAnimate) throws Exception {
    // int requestID = (Integer) vReqParam.get(2);
    // MyProfileModel2 profileModel = ModelManager.getInstance()
    // .getMyProfileModel2();
    // String data = null;
    // if (profileModel != null && profileModel.getMSISDN() != null)
    // if (requestID == CommunicationConstant.API_APP_VIEW_MY_PROFILE_OTHER) {
    // data = null;
    // } else {
    // data = DataCacheManager.getInstance().getDataCaching(requestID,
    // profileModel.getMSISDN());
    // }
    // else {
    // if (requestID == CommunicationConstant.API_APP_CREATE_PROFILE_REQ
    // || requestID == CommunicationConstant.API_APP_VIEW_MY_PROFILE_OTHER) {
    // data = null;
    // } else {
    // data = DataCacheManager.getInstance().getDataCaching(requestID);
    // }
    // }
    //
    // if (data == null) {
    // if (vReqParam.size() > 3) {
    // String fileName = (String) vReqParam.get(3);
    // String response;
    // if (obj instanceof BaseActivity)
    // response = Utilities.LoadFile(fileName, false,
    // (BaseActivity) obj);
    // else
    // response = Utilities.LoadFile(fileName, false,
    // ((BaseFragment) obj).getActivity());
    // sendReponseToUIController(obj, response, requestID);
    // } else {
    // if (isConnectionValid(obj)) {
    //
    // if (obj instanceof BaseFragment) {
    // BaseFragment baseFragment = (BaseFragment) obj;
    // if (isAnimate) {
    // progressDialog = baseFragment.getDialog(
    // baseFragment.getActivity(), requestID);
    // if (progressDialog != null) {
    // progressDialog.show();
    // dialogMap.put(requestID, progressDialog);
    // }
    // }
    // }
    // if (obj instanceof BaseActivity) {
    // BaseActivity baseActivity = (BaseActivity) obj;
    // if (isAnimate) {
    // progressDialog = baseActivity.getDialog(
    // baseActivity, requestID);
    // if (progressDialog != null) {
    // progressDialog.show();
    // dialogMap.put(requestID, progressDialog);
    // }
    // }
    // }
    // if (obj instanceof SupportMapFragment) {
    // BaseActivity baseActivity = (BaseActivity) ((SupportMapFragment) obj)
    // .getActivity();
    // if (isAnimate) {
    // progressDialog = baseActivity.getDialog(
    // baseActivity, requestID);
    // if (progressDialog != null) {
    // progressDialog.show();
    // dialogMap.put(requestID, progressDialog);
    // }
    // }
    // }
    // String url = (String) vReqParam.elementAt(0);
    // url1 = url;
    // String xml = (String) vReqParam.elementAt(1);
    // Hashtable<String, String> hashtable = getHeaders();
    // // if(requestID==CommunicationConstant.API_APP_FREE_BIE)///this
    // // needs to remove for release
    // // hashtable.put("X-MobileCare-MSISDN", "9099359362");
    // sendRequestToNativeCommunicationHandler(requestID, url,
    // xml, hashtable, obj, reqType, SECURE_CONNECTION,
    // CommunicationConstant.TIME_OUT_DURATION,
    // ResponseDataAcceptType.ACCEPT_TYPE_JSON);
    // }
    // }
    // } else {
    // sendReponseToUIController(obj, data, requestID);
    // }
    // }

    private void sendRequest(Vector<Object> vReqParam, Object obj, int reqType,
                             boolean isAnimate, boolean isRefresh) throws Exception {

        if (isConnectionValid(obj)) {

            int requestID = (Integer) vReqParam.get(2);
            String data = null;
            /*if (!isRefresh) {
					if (requestID == CommunicationConstant.API_APP_CREATE_PROFILE_REQ
							|| requestID == CommunicationConstant.API_APP_VIEW_MY_PROFILE_OTHER
							|| requestID == CommunicationConstant.API_APP_VIEW_MY_PROFILE_OTHER2) {
						data = null;
					} else {
						data = DataCacheManager.getInstance().getDataCaching(
								requestID);
					}
			}*/

            if (data == null) {
                String url = (String) vReqParam.elementAt(0);
                url1 = url;
                String xml = (String) vReqParam.elementAt(1);
                Hashtable<String, String> hashtable = getHeaders();

                // if(requestID==CommunicationConstant.API_APP_FREE_BIE)///this
                // needs to remove for release
					/*MLogger.debug("CommunicationManager",
							"hit goneeeeeeeeeeeeeee");*/
                sendRequestToNativeCommunicationHandler(requestID, url,
                        xml, hashtable, obj, reqType, SECURE_CONNECTION,
                        CommunicationConstant.TIME_OUT_DURATION,
                        ResponseDataAcceptType.ACCEPT_TYPE_JSON);
            } else {

                RequestData reqObj = new RequestData("", "", getHeaders(), obj, reqType, SECURE_CONNECTION,
                        CommunicationConstant.TIME_OUT_DURATION,
                        ResponseDataAcceptType.ACCEPT_TYPE_JSON);
                ResponseData responseData = new ResponseData();
                responseData.setRequestData(reqObj);
                responseData.setResponceData(data);
                sendReponseToUIController(obj, responseData);
            }

        }
    }

    ;

    private Hashtable<String, String> getHeaders() {
        Hashtable<String, String> header = new Hashtable<String, String>();
        header.put("dataType", "json");
        header.put("processdata", "true");
        header.put("async", "false");
        header.put("Content-Type", "application/json; charset=utf-8");
        return header;
    }

    private boolean isConnectionValid(Object object) {
        boolean isValid = true;
        //return Utilities.isNetworkAvailable(activity);
        // if (!isValid) {
        // showError(
        // "It seems your internet connection is not working.Please connect the internet on your device to use this application.",
        // activity);
        // }
        //}
        return isValid;

    }

    protected void showError(final String msg, final Activity activity) {
        try {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {new AlertCustomDialog(activity, msg);
                }
            });

        } catch (Exception e) {
            MLogger.error(getClass().getName(), e.getMessage());
        }

    }

    @Override
    public void sendReponseToUIController(Object listener, ResponseData response) {

        try {

            progressDialog = dialogMap.get(response.getRequestData().getReqApiId());
            if (progressDialog != null) {
				/*TextView text1 = (TextView) progressDialog
						.findViewById(R.id.message);
				int id = (Integer) text1.getTag();
				if (id == requestID) {
					if (progressDialog.isShowing())
						progressDialog.dismiss();
				}*/
            }
        } catch (Exception e) {
            MLogger.error(getClass().getName(), e.getMessage());
        }
        try {
            // if (requestID == CommunicationConstant.API_APP_REGISTRATION_OTP
            // && listener instanceof SSO_AddAccountFragment) {
            // if (response.contains("opStatus")) {
            //
            // String s =
            // "{\"businessOutput\":{\"cacheDataInMins\":\"0\",\"currentDate\":\"05-Feb-2015 09:01 PM\",\"opStatus\":\"0\",\"errorMessage\":\"OTP_EXPIRED\"}}";
            // response = s;
            // }
            // }
            ((IBaseResponse) listener).validateResponse(response);
        } catch (Exception e) {
            MLogger.error(getClass().getName(), e.getMessage());
        }

    }

    private String getURL(int requestID, boolean isHttps) {
        String key = "";
        switch (requestID) {
            case CommunicationConstant.API_LOGIN_USER:
                key = CommunicationConstant.API_KEY_LOGIN_USER;
                break;
            case CommunicationConstant.API_LOGOUT_USER:
                key = CommunicationConstant.API_KEY_LOGOUT_USER;
                break;
            case CommunicationConstant.API_ATTANDANCE_STATUS:
                key = CommunicationConstant.API_KEY_ATTANDANCE_STATUS;
                break;
            case CommunicationConstant.API_CHANGE_PASSWORD:
                key = CommunicationConstant.API_KEY_CHANGE_PASSWORD;
                break;
            case CommunicationConstant.API_MARK_ATTANDANCE:
                key = CommunicationConstant.API_KEY_MARK_ATTANDANCE;
                break;
            case CommunicationConstant.API_VALIDATE_PASSWORD:
                key = CommunicationConstant.API_KEY_VALIDATE_PASSWORD;
                break;
            case CommunicationConstant.API_VALIDATE_LOGIN:
                key = CommunicationConstant.API_KEY_VALIDATE_LOGIN;
                break;
            case CommunicationConstant.API_EMP_LEAVE_BALANCE:
                key = CommunicationConstant.API_KEY_EMP_LEAVE_BALANCE;
                break;
            case CommunicationConstant.API_EMP_LEAVES:
                key = CommunicationConstant.API_KEY_EMP_LEAVES;
                break;
            case CommunicationConstant.API_LEAVE_EMP_LIST:
                key = CommunicationConstant.API_KEY_LEAVE_EMP_LIST;
                break;
            case CommunicationConstant.API_LEAVE_REQ_TOTAL_DAY:
                key = CommunicationConstant.API_KEY_LEAVE_REQ_TOTAL_DAY;
                break;
            case CommunicationConstant.API_EMP_RH_LEAVES:
                key = CommunicationConstant.API_KEY_EMP_RH_LEAVES;
                break;
            case CommunicationConstant.API_SAVE_LEAVE_REQUEST:
                key = CommunicationConstant.API_KEY_SAVE_LEAVE_REQUEST;
                break;
            case CommunicationConstant.API_USER_PROFILE_DETAILS:
                key = CommunicationConstant.API_KEY_USER_PROFILE_DETAILS;
                break;
            case CommunicationConstant.API_SALARY_SLIP_MONTH:
                key = CommunicationConstant.API_KEY_SALARY_SLIP_MONTH;
                break;
            case CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_APPROOVED:
            case CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_CONSUMED:
                key = CommunicationConstant.API_KEY_GET_EMP_LEAVE_REQUESTS;
                break;
            case CommunicationConstant.API_GET_EMP_LEAVE_REQUESTS_PENDING:
                key = CommunicationConstant.API_KEY_GET_EMP_LEAVE_REQUESTS;
                break;
            case CommunicationConstant.API_GET_EMP_LEAVE_BALANCES:
                key = CommunicationConstant.API_KEY_GET_EMP_LEAVE_BALANCES;
                break;
            case CommunicationConstant.API_GET_MENU_DATA:
                key = CommunicationConstant.API_KEY_MENU_DATA;
                break;
            case CommunicationConstant.API_GET_EMP_ATTENDANCE_CALENDER_STATUS:
                key = CommunicationConstant.API_KEY_EMP_ATTENDANCE_CALENDER_STATUS;
                break;
            case CommunicationConstant.API_GET_EMP_PENDING_APPROVAL_REQ_COUNT:
                key = CommunicationConstant.API_KEY_GET_EMP_PENDING_APPROVAL_REQ_COUNT;
                break;
            case CommunicationConstant.API_GET_EMP_PENDING_APPROVAL_REQUESTS:
                key = CommunicationConstant.API_KEY_GET_EMP_PENDING_APPROVAL_REQUESTS;
                break;
            case CommunicationConstant.API_GET_UPDATE_PENDING_APPROVAL_STATUS:
                key = CommunicationConstant.API_KEY_GET_UPDATE_PENDING_APPROVAL_STATUS;
                break;
            case CommunicationConstant.API_GET_SALARY_SLIP_DATA:
                key = CommunicationConstant.API_KEY_GET_SALARY_SLIP_DATA;
                break;
            case CommunicationConstant.API_GET_LOCATION_LIST:
                key = CommunicationConstant.API_KEY_LOCATION_LIST;
                break;
            case CommunicationConstant.API_GET_LOCATION_DETAIL:
                key = CommunicationConstant.API_KEY_LOCATION_DETAIL;
                break;
            case CommunicationConstant.API_GET_LOCATION_UPDATE:
                key = CommunicationConstant.API_KEY_LOCATION_UPDATE;
                break;
            case CommunicationConstant.API_GET_TYPE_WISE_LIST:
                key = CommunicationConstant.API_KEY_TYPE_WISE_LIST;
                break;
            case CommunicationConstant.API_GET_TEAM_MEMBER_LIST:
                key = CommunicationConstant.API_KEY_TEAM_MEMBER_LIST;
                break;
            case CommunicationConstant.API_GET_ATTENDANCE_TIMELINE:
                key = CommunicationConstant.API_KEY_ATTENDANCE_TIMELINE;
                break;
            case CommunicationConstant.API_GET_EMPLOYEE_DETAIL:
                key = CommunicationConstant.API_KEY_EMPLOYEE_DETAIL;
                break;
            case CommunicationConstant.API_GET_MEMBER_INPUT_FIELD:
                key = CommunicationConstant.API_KEY_MEMBER_INPUT_FIELD;
                break;
            case CommunicationConstant.APT_GET_PENDING_MEMBER_APPROVAL:
                key = CommunicationConstant.APT_KEY_PENDING_MEMBER_APPROVAL;
                break;
            case CommunicationConstant.API_GET_APPROVALS_COUNT:
                key = CommunicationConstant.API_KEY_APPROVALS_COUNT;
                break;
            case CommunicationConstant.API_GET_UPDATE_EMPLOYEE_DETAILS:
                key = CommunicationConstant.API_KEY_UPDATE_EMPLOYEE_DETAILS;
                break;
            case CommunicationConstant.APT_GET_CREATE_EMPLOYEE:
                key = CommunicationConstant.APT_KEY_CREATE_EMPLOYEE;
                break;
            case CommunicationConstant.API_GET_MEMBER_PENDING_APPROVAL_STATUS:
                key = CommunicationConstant.API_KEY_MEMBER_PENDING_APPROVAL_STATUS;
                break;
            case CommunicationConstant.API_GET_HOME_DATA:
                key = CommunicationConstant.API_KEY_GET_HOME_DATA;
                break;
            case CommunicationConstant.API_GET_ADVANCE_REQUEST_SUMMARY:
                key = CommunicationConstant.API_KEY_GET_ADVANCE_REQUEST_SUMMARY;
                break;
            case CommunicationConstant.API_GET_ADVANCE_DETAIL:
                key = CommunicationConstant.API_KEY_GET_ADVANCE_DETAIL;
                break;
            case CommunicationConstant.API_GET_ADVANCE_PAGE_INIT:
                key = CommunicationConstant.API_KEY_GET_ADVANCE_PAGE_INIT;
                break;
            case CommunicationConstant.API_GET_SAVE_ADVANCE:
                key = CommunicationConstant.API_KEY_SAVE_ADVANCE;
                break;
            case CommunicationConstant.API_GET_EXPENSE_CLAIM_SUMMARY:
                key = CommunicationConstant.API_KEY_EXPENSE_CLAIM_SUMMARY;
                break;
            case CommunicationConstant.API_GET_EXPENSE_CLAIM_DETAIL:
                key = CommunicationConstant.API_KEY_GET_EXPENSE_CLAIM_DETAIL;
                break;
            case CommunicationConstant.API_GET_EXPENSE_PAGE_INIT:
                key = CommunicationConstant.API_KEY_GET_EXPENSE_PAGE_INIT;
                break;
            case CommunicationConstant.API_GET_APPROVER_DETAILS:
                key = CommunicationConstant.API_KEY_GET_APPROVER_DETAILS;
                break;
            case CommunicationConstant.API_GET_PROJECT_LIST_DETAILS:
                key = CommunicationConstant.API_KEY_GET_PROJECT_LIST_DETAILS;
                break;
            case CommunicationConstant.API_GET_CATEGORY_LIST_DETAILS:
                key = CommunicationConstant.API_KEY_GET_CATEGORY_LIST_DETAILS;
                break;
            case CommunicationConstant.API_GET_HEAD_DETAILS_WITH_POLICY:
                key = CommunicationConstant.API_KEY_GET_HEAD_DETAILS_WITH_POLICY;
                break;
            case CommunicationConstant.API_GET_SAVE_EXPENSE:
                key = CommunicationConstant.API_KEY_GET_SAVE_EXPENSE;
                break;
            case CommunicationConstant.API_GET_ADVANCE_LIST_FOR_EXPENSE:
                key = CommunicationConstant.API_KEY_GET_ADVANCE_LIST_FOR_EXPENSE;
                break;
            case CommunicationConstant.API_GET_EMPLOYEE_EXPENSE_APPROVAL:
                key = CommunicationConstant.API_KEY_GET_EMPLOYEE_EXPENSE_APPROVAL;
                break;
            case CommunicationConstant.API_GET_EMPLOYEE_ADVANCE_APPROVAL:
                key = CommunicationConstant.API_KEY_GET_EMPLOYEE_ADVANCE_APPROVAL;
                break;
            case CommunicationConstant.API_GET_ADVANCE_APPROVAL_ROLE:
                key = CommunicationConstant.API_KEY_GET_ADVANCE_APPROVAL_ROLE;
                break;
            case CommunicationConstant.API_SEARCH_ONBEHALF:
                key = CommunicationConstant.API_KEY_SEARCH_ONBEHALF;
                break;
            case CommunicationConstant.API_GET_MONTH_LIST:
                key = CommunicationConstant.API_KEY_GET_MONTH_LIST;
                break;
            case CommunicationConstant.API_GET_CORPEMP_PARAM:
                key = CommunicationConstant.API_KEY_GET_CORPEMP_PARAM;
                break;
            case CommunicationConstant.API_GET_WFH_EMP_LIST:
                key = CommunicationConstant.API_KEY_GET_WFH_EMP_LIST;
                break;
            case CommunicationConstant.API_GET_TOUR_EMP_LIST:
                key = CommunicationConstant.API_KEY_GET_TOUR_EMP_LIST;
                break;
            case CommunicationConstant.API_GET_OD_EMP_LIST:
                key = CommunicationConstant.API_KEY_GET_OD_EMP_LIST;
                break;
            case CommunicationConstant.API_GET_DETAILS_ON_INPUT_CHANGE:
                key = CommunicationConstant.API_KEY_GET_DETAILS_ON_INPUT_CHANGE;
                break;
            case CommunicationConstant.API_SAVE_OD_REQUEST:
                key = CommunicationConstant.API_KEY_SAVE_OD_REQUEST;
                break;
            case CommunicationConstant.API_SAVE_WFH_REQUEST:
                key = CommunicationConstant.API_KEY_SAVE_WFH_REQUEST;
                break;
            case CommunicationConstant.API_GET_DETAILS_ON_EMP_CHANGE:
                key = CommunicationConstant.API_KEY_GET_DETAILS_ON_EMP_CHANGE;
                break;
            case CommunicationConstant.API_GET_WFH_REQUEST_DETAIL:
                key = CommunicationConstant.API_KEY_GET_WFH_REQUEST_DETAIL;
                break;
            case CommunicationConstant.API_GET_TOUR_CUSTOM_FIELD_LIST:
                key = CommunicationConstant.API_KEY_GET_TOUR_CUSTOM_FIELD_LIST;
                break;
            case CommunicationConstant.API_GET_EMP_ATTENDANCE_REQUEST:
                key = CommunicationConstant.API_KEY_GET_EMP_ATTENDANCE_REQUEST;
                break;
            case CommunicationConstant.API_GET_OD_REQUEST_DETAIL:
                key = CommunicationConstant.API_KEY_GET_OD_REQUEST_DETAIL;
                break;
            case CommunicationConstant.API_WITHDRAW_WFH_REQUEST:
                key = CommunicationConstant.API_KEY_WITHDRAW_WFH_REQUEST;
                break;
            case CommunicationConstant.API_WITHDRAW_OD_REQUEST:
                key = CommunicationConstant.API_KEY_WITHDRAW_OD_REQUEST;
                break;
            case CommunicationConstant.API_GET_TOUR_REQUEST_DETAIL:
                key = CommunicationConstant.API_KEY_GET_TOUR_REQUEST_DETAIL;
                break;
            case CommunicationConstant.API_WITHDRAW_TOUR_REQUEST:
                key = CommunicationConstant.API_KEY_WITHDRAW_TOUR_REQUEST;
                break;
            case CommunicationConstant.API_SAVE_TOUR_REQUEST:
                key = CommunicationConstant.API_KEY_SAVE_TOUR_REQUEST;
                break;
            case CommunicationConstant.API_REJECT_TOUR_REQUEST:
                key = CommunicationConstant.API_KEY_REJECT_TOUR_REQUEST;
                break;
            case CommunicationConstant.API_REJECT_OD_REQUEST:
                key = CommunicationConstant.API_KEY_REJECT_OD_REQUEST;
                break;
            case CommunicationConstant.API_REJECT_WFH_REQUEST:
                key = CommunicationConstant.API_KEY_REJECT_WFH_REQUEST;
                break;
            case CommunicationConstant.API_APPROVE_WFH_REQUEST:
                key = CommunicationConstant.API_KEY_APPROVE_WFH_REQUEST;
                break;
            case CommunicationConstant.API_APPROVE_TOUR_REQUEST:
                key = CommunicationConstant.API_KEY_APPROVE_TOUR_REQUEST;
                break;
            case CommunicationConstant.API_APPROVE_OD_REQUEST:
                key = CommunicationConstant.API_KEY_APPROVE_OD_REQUEST;
                break;
            case CommunicationConstant.API_TIME_MODIFICATION_REQUEST:
                key = CommunicationConstant.API_KEY_TIME_MODIFICATION_REQUEST;
                break;
            case CommunicationConstant.API_APPROVE_LEAVE_REQUEST:
                key = CommunicationConstant.API_KEY_APPROVE_LEAVE_REQUEST;
                break;
            case CommunicationConstant.API_REJECT_LEAVE_REQUEST:
                key = CommunicationConstant.API_KEY_REJECT_LEAVE_REQUEST;
                break;
            case CommunicationConstant.API_GET_LEAVE_REQUEST_DETAIL:
                key = CommunicationConstant.API_KEY_GET_LEAVE_REQUEST_DETAIL;
                break;
            case CommunicationConstant.API_WITHDRAW_LEAVE_REQUEST:
                key = CommunicationConstant.API_KEY_WITHDRAW_LEAVE_REQUEST;
                break;
            case CommunicationConstant.API_BACKDATED_ATTENDANCE_REQUEST:
                key = CommunicationConstant.API_KEY_BACKDATED_ATTENDANCE_REQUEST;
                break;
            case CommunicationConstant.API_FORGOT_CREDENTIALS_REQUEST:
                key = CommunicationConstant.API_KEY_FORGOT_CREDENTIALS_REQUEST;
                break;
            case CommunicationConstant.API_GET_ATTENDANCE_DETAIL:
                key = CommunicationConstant.API_KEY_GET_ATTENDANCE_DETAIL;
                break;
            case CommunicationConstant.API_REJECT_ATTENDANCE_REQUEST:
                key = CommunicationConstant.API_KEY_REJECT_ATTENDANCE_REQUEST;
                break;
            case CommunicationConstant.API_APPROVE_ATTENDANCE_REQUEST:
                key = CommunicationConstant.API_KEY_APPROVE_ATTENDANCE_REQUEST;
                break;
            case CommunicationConstant.API_PENDING_APPROVAL_ATTENDANCE_REQUEST:
                key = CommunicationConstant.API_KEY_PENDING_APPROVAL_ATTENDANCE_REQUEST;
                break;
        }
        return CommunicationConstant.getMobileCareURl() + "/WCFService/" + key;
    }

    public static String getMsisdn() {
        return msisdn;
    }

    public static void setMsisdn(String msisdn) {
        CommunicationManager.msisdn = msisdn;
    }

    public static String getDevice_client_id() {
        return device_client_id;
    }

    public static void setDevice_client_id(String device_client_id) {
        CommunicationManager.device_client_id = device_client_id;
    }

    public static String getDEVICE_RESOLUTION() {
        return DEVICE_RESOLUTION;
    }

    public static void setDEVICE_RESOLUTION(String dEVICE_RESOLUTION) {
        DEVICE_RESOLUTION = dEVICE_RESOLUTION;
    }

    public void setPrimaryNeeded(boolean primaryNeeded) {
        this.primaryNeeded = primaryNeeded;
    }
}
