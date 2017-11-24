package hr.eazework.selfcare.communication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;

import hr.eazework.com.application.MyApplication;
import hr.eazework.com.model.AdjustmentExpenseItem;
import hr.eazework.com.model.AdvanceAdjustmentRequestModel;
import hr.eazework.com.model.AdvanceAdjustmentResponseModel;
import hr.eazework.com.model.AdvanceApprovalRequestModel;
import hr.eazework.com.model.AdvanceDataRequestModel;
import hr.eazework.com.model.AdvanceItemModel;
import hr.eazework.com.model.AdvanceListItemModel;
import hr.eazework.com.model.AdvanceLoginDataRequestModel;
import hr.eazework.com.model.AdvanceSummaryRequestModel;
import hr.eazework.com.model.ApproverExpenseModel;
import hr.eazework.com.model.CategoryExpenseModel;
import hr.eazework.com.model.CategoryRequestModel;
import hr.eazework.com.model.DocListModel;
import hr.eazework.com.model.ExpenseRequestModel;
import hr.eazework.com.model.FileInfoModel;
import hr.eazework.com.model.GetAdvanceDetailRequestModel;
import hr.eazework.com.model.GetApproverDetailsRequestModel;
import hr.eazework.com.model.GetHeadDetailsWithPolicyRequestModel;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.LocationDetailsModel;
import hr.eazework.com.model.MappedEmployee;
import hr.eazework.com.model.MemberReqInputModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.PeriodicExpenseRequest;
import hr.eazework.com.model.ProjectExpenseModel;
import hr.eazework.com.model.ProjectRequestModel;
import hr.eazework.com.model.SaveExpenseItem;
import hr.eazework.com.model.SaveExpenseModel;
import hr.eazework.com.model.SaveExpenseRequestModel;
import hr.eazework.com.model.SearchOnBehalfItem;
import hr.eazework.com.model.SupportDocsItemModel;
import hr.eazework.com.model.ViewExpenseClaimRequestModel;
import hr.eazework.com.model.VisibilityExpenseItem;
import hr.eazework.com.model.VisibilityExpenseModel;
import hr.eazework.com.ui.util.CommonValues;
import hr.eazework.com.ui.util.DeviceUtil;
import hr.eazework.com.ui.util.SharedPreference;
import hr.eazework.mframe.caching.manager.DataCacheManager;


public class AppRequestJSONString {

    public static String GetCorpEmpParam() {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    public static String getLoginData(String url, String uname, String pass) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("CorpUrl", url);
            subJson.put("Login", uname);
            subJson.put("Password", pass);
            subJson.put("OS", 1);
            subJson.put("OSVersion", DeviceUtil.getOsVersion());
            subJson.put("AppVersion", DeviceUtil.getAppVersion());
            subJson.put("DeviceMake", DeviceUtil.getDeviceMake());
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginCred", subJson);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String
    getCommonService(String paramType, String refCode) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("ParamType", paramType);
            jsonObject.put("RefCode", refCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getLocationList() {
        JSONObject jsonObject = getSimpleRequestSessionData();
        return jsonObject.toString();
    }

    public static String UpdateLocationData(LocationDetailsModel model, String flag) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject locationDeatil = new JSONObject();
        JSONObject fileInfo = new JSONObject();
        JSONArray mappedEmployee = new JSONArray();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            locationDeatil.put("OfficeID", model.getmOfficeId());
            locationDeatil.put("OfficeName", model.getmOfficeName());
            locationDeatil.put("OfficeType", model.getmOfficeType());
            locationDeatil.put("OfficeCode", model.getmOfficeCode());
            locationDeatil.put("Address1", model.getmAddress1());
            locationDeatil.put("Address2", model.getmAddress2());
            locationDeatil.put("City", model.getmCity());
            locationDeatil.put("PinCode", model.getmPincode());
            locationDeatil.put("CountryCode", model.getmCountryCode());
            locationDeatil.put("StateCode", model.getmStateCode());
            locationDeatil.put("StateOther", model.getmStateOther());
            locationDeatil.put("ISOCodeCountry", model.getmISOCodeCountry());
            locationDeatil.put("ISOCodeState", model.getmISOCodeState());
            locationDeatil.put("PhoneNo", model.getmPhone());
            locationDeatil.put("Latitude", model.getmLatitude());
            locationDeatil.put("Longitude", model.getmLongitude());
            locationDeatil.put("GeoLocation", model.getmGeoLocation());
            if (flag.equalsIgnoreCase("C")) {
                fileInfo.put("Name", "ForStore.jpg");
                fileInfo.put("Extension", ".jpg");
                fileInfo.put("Length", model.getmBinary().length());
                fileInfo.put("Base64Data", model.getmBinary());
            } else {
                fileInfo.put("Name", "");
                fileInfo.put("Extension", "");
                fileInfo.put("Length", "0");
                fileInfo.put("Base64Data", "");

            }
            locationDeatil.put("FileInfo", fileInfo);


            for (int i = 0; i < model.getMappedEmployees().size(); i++) {
                JSONObject obj = new JSONObject();
                MappedEmployee m = model.getMappedEmployees().get(i);
                obj.put("EmpID", m.getEmpCode());
                obj.put("Flag", m.getFlag());
                mappedEmployee.put(i, obj);
            }
            locationDeatil.put("MappedEmployeeList", mappedEmployee);
            jsonObject.put("loginData", subJson);
            jsonObject.put("locationDetail", locationDeatil);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public static String getLocationDetails(String officeID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("officeID", officeID);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getLogOutData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginCred", subJson);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getEmpLeavesData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("empID", empId);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /*{
        "loginData":{"DeviceID":"JamesBond007","SessionID":"E47BA0AD-9CDB-4960-96B5-69900765CF0F"},
        "leaveReqDetail":{ "ForEmpID":"22279","LeaveID":"C008000001","StartDate":"26/09/2015","EndDate":"28/09/2015"}

    }*/
    public static String GetLeaveReqTotalDays(String empId, String leaveId, String startDate, String endDate) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject reqDetail = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            reqDetail.put("ForEmpID", empId);
            reqDetail.put("LeaveID", leaveId);
            reqDetail.put("StartDate", startDate);
            reqDetail.put("EndDate", endDate);
            jsonObject.put("leaveReqDetail", reqDetail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getValidateLoginData(boolean isPassCheck, String password) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            if (isPassCheck) {
                subJson.put("Password", password);
            }
            jsonObject.put("loginCred", subJson);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getLeaveBalanceData(String empId, String leaveId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("empID", empId);
            jsonObject.put("leaveID", leaveId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * @param empId
     * @param reqType 0=InTime
     *                2=BreakOut
     *                1=BreakIn
     *                3=OutTime
     *                4=Cancel OutTime
     * @return
     */
    public static String getMarkAttandanceData(Context context, String empId, int reqType) {

        return getMarkAttendanceData(empId, reqType, null, null, null, null, null, null, null, null);
    }

    public static String getMarkAttendanceData(String empId, int reqType,
                                               String latitude, String longitude, String fileName,
                                               String fileExtension, String binaryFile,
                                               String location, String geoRadius, String geoDistance) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject attendanceJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);

            attendanceJson.put("ReqType", reqType);
            attendanceJson.put("MarkSource", 2);
            attendanceJson.put("ForEmpID", empId);
            attendanceJson.put("AttendID", 0);
            attendanceJson.put("PunchTime", "");
            attendanceJson.put("IPAddress", "");
            attendanceJson.put("Latitude", latitude);
            attendanceJson.put("Longitude", longitude);
            attendanceJson.put("Location", location);
            attendanceJson.put("GeoRadius", geoRadius);
            attendanceJson.put("GeoDistance", geoDistance);
            if (!TextUtils.isEmpty(fileName)) {
                JSONObject fileInfo = new JSONObject();
                fileInfo.put("Name", fileName);
                fileInfo.put("Extension", fileExtension);
                fileInfo.put("Length", binaryFile.length());
                fileInfo.put("Base64Data", binaryFile);
                attendanceJson.put("FileInfo", fileInfo);
            }
            jsonObject.put("attendance", attendanceJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public static String getAttandanseStatusData(String empId) {
        // {"loginData":{"DeviceID":"JamesBond007","SessionID":"87ED4A88-CFA4-472D-B476-3B5A94694DC4"},"attendance" : {"ForEmpID":"22279"}}
        JSONObject jsonObject = new JSONObject();
        JSONObject attandance = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            attandance.put("ForEmpID", empId);
            jsonObject.put("attendance", attandance);
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getAdvanceSummaryData() {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        GetAdvanceDetailRequestModel advanceDetailRequestModel= new GetAdvanceDetailRequestModel();
        advanceDetailRequestModel.setLoginData(loginData);
        Log.d("SummaryRequest",advanceDetailRequestModel.serialize());
        return advanceDetailRequestModel.serialize();
    }
    public static String getAdvanceApprovalData(String role) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        AdvanceApprovalRequestModel advanceApprovalRequestModel= new AdvanceApprovalRequestModel();
        advanceApprovalRequestModel.setLoginData(loginData);
        advanceApprovalRequestModel.setRole(role);
        Log.d("SummaryRequest",advanceApprovalRequestModel.serialize());
        return advanceApprovalRequestModel.serialize();
    }
    public static String getExpenseInitData() {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        ExpenseRequestModel expenseRequestModel=new ExpenseRequestModel();
        expenseRequestModel.setReqID(0);
        ViewExpenseClaimRequestModel viewExpenseClaimRequestModel= new ViewExpenseClaimRequestModel();
        viewExpenseClaimRequestModel.setLoginData(loginData);
        viewExpenseClaimRequestModel.setExpense(expenseRequestModel);
        Log.d("ExpenseInitData",viewExpenseClaimRequestModel.serialize());
        return viewExpenseClaimRequestModel.serialize();
    }

    public static String getExpenseApproverData(int claimType,int empId,String projectId) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        ApproverExpenseModel approverExpenseModel=new ApproverExpenseModel();
        approverExpenseModel.setReqID(0);
        approverExpenseModel.setClaimTypeID(String.valueOf(claimType));
        approverExpenseModel.setForEmpID(empId);
        approverExpenseModel.setProjectID(projectId);
        GetApproverDetailsRequestModel getApproverDetailsRequestModel= new GetApproverDetailsRequestModel();
        getApproverDetailsRequestModel.setLoginData(loginData);
        getApproverDetailsRequestModel.setExpense(approverExpenseModel);
        String expenseInitData=getApproverDetailsRequestModel.serialize();
        return expenseInitData;
    }

    public static  String searchOnBehalfRequest(SearchOnBehalfItem item){
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        item.setLoginData(loginData);
        String request=item.serialize();
        Log.d("TAG","Search On Behalf : "+request);
        return request;


    }

    public static String getAdvanceAdjustmentData(String currency,String empId) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        AdjustmentExpenseItem adjustmentExpenseItem=new AdjustmentExpenseItem();
        adjustmentExpenseItem.setCurrencyCode(currency);
        adjustmentExpenseItem.setForEmpID(empId);
        AdvanceAdjustmentRequestModel advanceAdjustmentRequestModel= new AdvanceAdjustmentRequestModel();
        advanceAdjustmentRequestModel.setLoginData(loginData);
        advanceAdjustmentRequestModel.setExpense(adjustmentExpenseItem);
        String expenseInitData=advanceAdjustmentRequestModel.serialize();
        return expenseInitData;
    }

    public static String getHeadDetailsWithPolicyData(int empId, int categoryId, String headId,String currency,int reqId) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        VisibilityExpenseItem visibilityExpenseItem=new VisibilityExpenseItem();
        visibilityExpenseItem.setReqID(reqId+"");
        visibilityExpenseItem.setForEmpID(empId);
        visibilityExpenseItem.setCurrencyCode(currency);
        VisibilityExpenseModel visibilityExpenseModel=new VisibilityExpenseModel();
        visibilityExpenseModel.setExpenseItem(visibilityExpenseItem);
        GetHeadDetailsWithPolicyRequestModel data= new GetHeadDetailsWithPolicyRequestModel();
        data.setLoginData(loginData);
        data.setExpense(visibilityExpenseModel);
        data.setExpCategory(String.valueOf(categoryId));
        data.setHeadID(headId);
        String headPolicyData=data.serialize();
        return headPolicyData;
    }

    public static String getCategoryData(int empId,int reqId) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        CategoryExpenseModel categoryExpenseModel=new CategoryExpenseModel();
        categoryExpenseModel.setReqID(reqId);
        categoryExpenseModel.setForEmpID(empId);
        CategoryRequestModel categoryRequestModel= new CategoryRequestModel();
        categoryRequestModel.setLoginData(loginData);
        categoryRequestModel.setExpense(categoryExpenseModel);
        String expenseCategoryData=categoryRequestModel.serialize();
        return expenseCategoryData;
    }

    public static String getPeriodicMonthData(int empId ,int reqId,String [] monthList) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        CategoryExpenseModel categoryExpenseModel=new CategoryExpenseModel();
        categoryExpenseModel.setReqID(reqId);
        categoryExpenseModel.setForEmpID(empId);
        PeriodicExpenseRequest periodicExpenseRequest= new PeriodicExpenseRequest();
        periodicExpenseRequest.setLoginData(loginData);
        periodicExpenseRequest.setExpense(categoryExpenseModel);
        periodicExpenseRequest.setMonthList(monthList);
        String expenseCategoryData=periodicExpenseRequest.serialize();
        return expenseCategoryData;
    }

    public static String getProjectData(int claimType,int empId) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        ProjectExpenseModel projectExpenseModel=new ProjectExpenseModel();
        projectExpenseModel.setForEmpID(empId);
        projectExpenseModel.setClaimTypeID(String.valueOf(claimType));
        ProjectRequestModel projectRequestModel= new ProjectRequestModel();
        projectRequestModel.setLoginData(loginData);
        projectRequestModel.setExpense(projectExpenseModel);
        String expenseInitData=projectRequestModel.serialize();
        return expenseInitData;
    }
    public static String getExpenseClaimSummaryData() {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        GetAdvanceDetailRequestModel advanceDetailRequestModel= new GetAdvanceDetailRequestModel();
        advanceDetailRequestModel.setLoginData(loginData);
        Log.d("ExpenseClaimRequest",advanceDetailRequestModel.serialize());
        return advanceDetailRequestModel.serialize();
    }

    public static String getExpenseClaimData(String fromButton,String approverName,String approverID,String requestId,ArrayList<LineItemsModel> lineItemsModels,
                                             ArrayList<AdvanceListItemModel> advanceListItemModels, String description,
                                             String remarks, String currencyCode, int claimTypeId, String projectId, ArrayList<DocListModel> supportDocsItemModels,
                                             String empId,String reqSatus) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
       /* if(lineItemsModels.size()>0){
            for(int i=0;i<lineItemsModels.size();i++){
                LineItemsModel model=lineItemsModels.get(i);
                ArrayList<SupportDocsItemModel> docsItemModels=model.getDocListLineItem();
                if(docsItemModels!=null && docsItemModels.size()>0){
                    for(int j=0;j<docsItemModels.size();i++){
                      SupportDocsItemModel supDoc=docsItemModels.get(j);
                       // supDoc.set

                    }
                }
            }
        }*/

        if(supportDocsItemModels!=null && supportDocsItemModels.size()>0){
            for(int i=0; i<supportDocsItemModels.size();i++){
                DocListModel model=supportDocsItemModels.get(i);
                model.setBitmap(null);
                supportDocsItemModels.set(i,model);
            }
        }
        SaveExpenseItem saveExpenseItem=new SaveExpenseItem();
        saveExpenseItem.setDocList(supportDocsItemModels);

        saveExpenseItem.setLineItems(lineItemsModels);
        saveExpenseItem.setAdvanceList(advanceListItemModels);
        saveExpenseItem.setDescription(description);
        saveExpenseItem.setReqRemark(remarks);
        saveExpenseItem.setReqStatus(reqSatus);
        saveExpenseItem.setCurrencyCode(currencyCode);
        saveExpenseItem.setClaimTypeID(claimTypeId);
        saveExpenseItem.setReqID(requestId);
        saveExpenseItem.setApproverID(approverID);
        saveExpenseItem.setApproverName(approverName);
        saveExpenseItem.setProjectID(Integer.parseInt(projectId));
        saveExpenseItem.setForEmpID(Integer.parseInt(empId));
        //saveExpenseItem.setProjectID(Integer.parseInt(projectId));

        SaveExpenseModel saveExpenseModel=new SaveExpenseModel();
        saveExpenseModel.setFromButton(fromButton);
        saveExpenseModel.setSource("2");
        saveExpenseModel.setExpenseItem(saveExpenseItem);



        SaveExpenseRequestModel saveExpenseRequestModel= new SaveExpenseRequestModel();
        saveExpenseRequestModel.setLoginData(loginData);
        saveExpenseRequestModel.setExpense(saveExpenseModel);

        String claimRequest=saveExpenseRequestModel.serialize();
        Log.d("TAG","Expense Claim Request : "+claimRequest);
        return claimRequest;
    }



    public static String getAdvanceRequestData(String fromButton,String reqId,String approvalLevel,String remarks, String reqAmount, String currencyCode, String forEmpId, String reasonCode, String reason , ArrayList<SupportDocsItemModel> docsItemModels) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        AdvanceItemModel advanceItemModel=new AdvanceItemModel();
        advanceItemModel.setFromButton("Submit");
        advanceItemModel.setFromButton(fromButton);
        advanceItemModel.setReqID(reqId);
        advanceItemModel.setRemarks(remarks);
        advanceItemModel.setReqAmount(reqAmount);
        advanceItemModel.setApprovalLevel(approvalLevel);
        advanceItemModel.setCurrencyCode(currencyCode);
        advanceItemModel.setForEmpID(Integer.parseInt(forEmpId));
        advanceItemModel.setReasonCode(reasonCode);
        advanceItemModel.setReason(reason);
        advanceItemModel.setSource(2);
        advanceItemModel.setReqStatus("1");
       /* if(docsItemModels!=null && docsItemModels.size()>0){
            for(int i=0;i<docsItemModels.size();i++){
                SupportDocsItemModel itemModel=docsItemModels.get(i);
                //itemModel.set
                docsItemModels.set(i,itemModel);
            }
        }*/
        advanceItemModel.setSupportDocs(docsItemModels);
        AdvanceDataRequestModel advanceDataRequestModel= new AdvanceDataRequestModel();
        advanceDataRequestModel.setLoginData(loginData);
        advanceDataRequestModel.setAdvance(advanceItemModel);
        String AdvanceRequest=advanceDataRequestModel.serialize();
        return AdvanceRequest;
    }

    public static String getViewAdvanceSummaryData(int requestId,int advanceId) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        AdvanceSummaryRequestModel summaryRequestModel = new AdvanceSummaryRequestModel();
        summaryRequestModel.setReqID(requestId);
        summaryRequestModel.setAdvanceID(advanceId);
        summaryRequestModel.setLoginData(loginData);
        Log.d("SummaryRequest",summaryRequestModel.serialize());
        return summaryRequestModel.serialize();
    }



    public static String getViewExpenseClaimSummaryData(int requestId) {
        AdvanceLoginDataRequestModel loginData = new AdvanceLoginDataRequestModel();
        loginData.setDeviceID(MyApplication.getDeviceId());
        loginData.setSessionID(SharedPreference.getSessionId());
        ExpenseRequestModel expenseRequestModel=new ExpenseRequestModel();
        expenseRequestModel.setReqID(requestId);
        ViewExpenseClaimRequestModel summaryRequestModel = new ViewExpenseClaimRequestModel();
        summaryRequestModel.setExpense(expenseRequestModel);
        summaryRequestModel.setLoginData(loginData);
        Log.d("ClaimSummaryRequest",summaryRequestModel.serialize());
        return summaryRequestModel.serialize();
    }

    public static String getChangePasswordData(String oldPass, String newPAss) {
        JSONObject jsonObject = new JSONObject();
        JSONObject attandance = new JSONObject();
        try {

            attandance.put("CorpUrl", DataCacheManager.getInstance().getDataCaching(CommonValues.DB_KEY_CORP_URL));
            attandance.put("Login", ModelManager.getInstance().getLoginUserModel().getUserModel().getLoginId());
            attandance.put("Password", oldPass);
            attandance.put("NewPassword", newPAss);
            attandance.put("DeviceID", MyApplication.getDeviceId());
            attandance.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginCred", attandance);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getSallarySlipMonthData() {
        JSONObject jsonObject = getSimpleRequestSessionData();
        return jsonObject.toString();

    }

    @NonNull
    public static String getSimpleRequestSessionDataStr() {
        return getSimpleRequestSessionData().toString();
    }

    @NonNull
    public static JSONObject getSimpleRequestSessionData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getSallarySlipDataData(String month) {

        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("month", month);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    public static String getUpdatePendingStatusData(String action, Object requestId, Object status, Object approvalLevel) {

        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("reqID", requestId);
            jsonObject.put("status", status);
            jsonObject.put("approvalLevel", -1);
            jsonObject.put("action", action);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();

    }

    public static String getUpdatePendingEmployeeStatusData(String action,
                                                            String requestId,
                                                            String status,
                                                            String approvalLevel) {

        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("reqID", requestId);
            jsonObject.put("reqStatus", status);
            jsonObject.put("approvalLevel", approvalLevel);
            jsonObject.put("action", action);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();

    }

    public static String getEmpAttendanceCalendarStatus(String dateFrom, String dateTo) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("dateFrom", dateFrom);
            jsonObject.put("dateTo", dateTo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getEmpLeaveRequestsData(String dateFrom, String dateTo, boolean isPendingOrApprooved, boolean isConsumed) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("dateFrom", dateTo);
            jsonObject.put("dateTo", dateFrom);
            jsonObject.put("flag", isConsumed ? "C" : isPendingOrApprooved ? "P" : "A");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getSaveLeaveRequestData(String empId, String leaveId, String startDate, String endDate, String totalDays, String remark) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject leaveReq = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            leaveReq.put("ReqID", "0");
            leaveReq.put("ForEmpID", empId);
            leaveReq.put("LeaveID", leaveId);
            leaveReq.put("StartDate", startDate);
            leaveReq.put("EndDate", endDate);
            leaveReq.put("TotalDays", totalDays);
            leaveReq.put("Remarks", remark);
            jsonObject.put("leaveReqDetail", leaveReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /*{
        "loginData":{"DeviceID":"JamesBond007","SessionID":"5DCF6BA8-47D1-40AF-9333-87032E08D43A"},
        "leaveReqDetail":{"ReqID":"0", "ForEmpID":"22279","LeaveID":"C008000001",
         "StartDate":"09/06/2015","EndDate":"10/06/2015","TotalDays":"2","Remarks":"Urgent work"}

    }*/
    public static String getEmpLeaveBalancesData(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("empID", empId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getEmployeeDetails(String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("ForEmpID", empId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getTeamList(String subLevel, String empId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            /*subJson.put("CorpUrl","mobile");
            subJson.put("Login","admin");
            subJson.put("Password","a12345");*/
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("subLevel", subLevel);
            if (!TextUtils.isEmpty(empId)) {
                jsonObject.put("forEmpID", empId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    public static String getHistory(String from, String to, String empID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("dateFrom", from);
            jsonObject.put("dateTo", to);
            jsonObject.put("sortOrder", "D");
            if (TextUtils.isEmpty(empID)) {
                jsonObject.put("forEmpID", "0");
            } else {
                jsonObject.put("forEmpID", empID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getAttendanceTimeLine(String empId, String attendId, String markDate) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject requestJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            requestJson.put("ForEmpID", empId);
            requestJson.put("AttendID", attendId);
            requestJson.put("MarkDate", markDate);
            jsonObject.put("request", requestJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getMemberRequestInpFields(String reqId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject requestJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            jsonObject.put("ReqID", reqId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getUpdateEmployeeDetail(String empId, String managerID, String workLocationID) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject employeeData = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            employeeData.put("EmpID", empId);
            employeeData.put("ManagerID", managerID);
            employeeData.put("WorkLocationID", workLocationID);
            jsonObject.put("employeeData", employeeData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getCreateEmployee(ArrayList<MemberReqInputModel> list) {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        JSONObject reqData = new JSONObject();
        JSONObject reqDataSub = new JSONObject();

        JSONArray field = new JSONArray();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            jsonObject.put("loginData", subJson);
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = new JSONObject();
                MemberReqInputModel m = list.get(i);
                obj.put("TranID", "0");
                obj.put("FieldCode", m.getmFieldCode());
                obj.put("FieldValue", m.getmFieldValue());
                if (list.get(i).getmFieldTypeID().equalsIgnoreCase("4") || list.get(i).getmFieldTypeID().equalsIgnoreCase("99") || list.get(i).getmFieldTypeID().equalsIgnoreCase("66")) {
                    JSONObject fileInfo = new JSONObject();
                    FileInfoModel fileInfoModel = list.get(i).getmFileInfoModel();
                    fileInfo.put("Name", fileInfoModel.getmName());
                    fileInfo.put("Extension", fileInfoModel.getmExtension());
                    fileInfo.put("Length", fileInfoModel.getmLength());
                    fileInfo.put("Base64Data", fileInfoModel.getmBase64Data());
                    obj.put("FileInfo", fileInfo);

                }
                field.put(i, obj);
            }
            reqDataSub.put("ReqID", "0");
            reqDataSub.put("ReqStatus", "");
            reqDataSub.put("Fields", field);
            jsonObject.put("requestData", reqDataSub);


            Crashlytics.log(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG","Employee Jason data : "+jsonObject.toString());
        return jsonObject.toString();
    }

    public static String getHomeData() {
        JSONObject jsonObject = new JSONObject();
        JSONObject subJson = new JSONObject();
        try {
            subJson.put("DeviceID", MyApplication.getDeviceId());
            subJson.put("SessionID", SharedPreference.getSessionId());
            subJson.put("OS", 1);
            subJson.put("OSVersion", DeviceUtil.getOsVersion());
            subJson.put("AppVersion", DeviceUtil.getAppVersion());
            subJson.put("DeviceMake", DeviceUtil.getDeviceMake());
            jsonObject.put("loginData", subJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
