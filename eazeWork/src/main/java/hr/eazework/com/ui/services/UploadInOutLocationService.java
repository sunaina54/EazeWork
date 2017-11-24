package hr.eazework.com.ui.services;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.model.CheckInOutModel;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TimeInOutDetails;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.ui.util.AttendanceUtil;
import hr.eazework.com.ui.util.EventDataSource;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;
import hr.eazework.selfcare.communication.IBaseResponse;

/**
 * Created by allsmartlt218 on 20-12-2016.
 */

public class UploadInOutLocationService extends IntentService {

    private Preferences preference;
    private static TransactionProcessListner transactionProcessListner;
    private static Transactions currentTransaction= Transactions.NONE;
    private static TransactionSatus currentTransactionSatus= TransactionSatus.NONE;
    private String response = "";
    private static final String TAG = UploadInOutLocationService.class.getName();

    public enum Transactions{
        NONE,
        TIMEINOUT,
    }

    public enum TransactionSatus{
        START,
        ERROR_NO_INTERNETCONNECTION,
        ERROR_TIMEOUT,
        ERROR_EXCEPTION,
        SUCCESS,
        FAILURE,
        NODATA,
        END,
        NONE
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadInOutLocationService() {
        super("UploadInOutLocationService");
    }
    public interface TransactionProcessListner {
        public void transactionStatus(Transactions transactions, TransactionSatus transactionSatus);
        public void error(TransactionSatus transactionSatus);
        public void currentTransaction(Transactions transactions);
    }

    public static void setTransactionProcessListner(TransactionProcessListner transaction){
        transactionProcessListner=transaction;
        updateTransactionStatus();
    }
    public static void resetTransactionProcessListner(){
        transactionProcessListner=null;
        currentTransaction= Transactions.NONE;
        currentTransactionSatus= TransactionSatus.NONE;
    }
    private static void updateTransactionStatus(){
        try {
            if(transactionProcessListner!=null){
                transactionProcessListner.currentTransaction(currentTransaction);
                transactionProcessListner.transactionStatus(currentTransaction, currentTransactionSatus);
            }
        } catch (Exception e) {

            Crashlytics.log(1,"UploadTransaction","UploadTransaction");
            Crashlytics.logException(e);
        }
    }

    private void markAttendanceAPI(Transactions transaction){
        try {

            //LogUtils.errorLog("UploadInOutLocationService", "markAttendanceAPI "+transaction);
            switch (transaction) {
                case TIMEINOUT:
                    uploadTimeInOut();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private IBaseResponse attendance = new IBaseResponse() {
        @Override
        public void validateResponse(ResponseData response) {
            switch (response.getRequestData().getReqApiId()) {
                case CommunicationConstant.API_MARK_ATTANDANCE:
                    try {
                        if (((new JSONObject(response.getResponseData()))
                                .getJSONObject("MarkAttendanceResult"))
                                .getInt("ErrorCode") == 0) {

                            LoginUserModel userModel = ModelManager.getInstance()
                                    .getLoginUserModel();
                            CommunicationManager.getInstance().sendPostRequest(
                                    this,
                                    AppRequestJSONString
                                            .getAttandanseStatusData(userModel
                                                    .getUserModel().getEmpId()),
                                    CommunicationConstant.API_ATTANDANCE_STATUS, true);

                        } else {
                            new AlertCustomDialog(
                                    getApplicationContext(),
                                    ""
                                            + ((new JSONObject(response
                                            .getResponseData()))
                                            .getJSONObject("MarkAttendanceResult"))
                                            .optString("ErrorMessage", ""));

                        }
                    } catch (JSONException e) {
                    }
                    break;

            }
        }
    } ;




    private void uploadTimeInOut() {
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        // get data base
        if (loginUserModel != null) {

            UserModel userModel = loginUserModel.getUserModel();

            if (userModel != null) {
                String username = userModel.getUserName();

                final EventDataSource dataSource = new EventDataSource(getApplicationContext());
                ArrayList<TimeInOutDetails> detailsArrayList = dataSource.getTimeInOutDetails(username);

                // upload to server // Asc time // first success than second
                if (detailsArrayList != (null) && detailsArrayList.size() > 0) {
                    for (final TimeInOutDetails d : detailsArrayList) {
                        // imgae server server path
                        String actionImage = d.getmActionImage();
                        String imagePath = "";
                        if (!TextUtils.isEmpty(actionImage)) {
                            imagePath = ImageUtil.encodeImage(actionImage);
                        }
                        if ((!TextUtils.isEmpty(actionImage) && !TextUtils.isEmpty(imagePath)) || TextUtils.isEmpty(imagePath)) {


                            int refId = 0;
                            Log.e(TAG, refId + "");
                            try {
                                refId = AttendanceUtil.getRequestType(d.getmComments());
                                Log.e(TAG, refId + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                                Log.e(TAG, refId + "");
                            }

                            String empId = userModel.getEmpId();

                            //                  requestMarkAttendanceAPI(empId,refId,d.getmLatitude(),d.getmLongitude(),"ForPhoto","jpg",imagePath);

                            CommunicationManager.getInstance().sendPostRequest(new IBaseResponse() {
                                @Override
                                public void validateResponse(ResponseData response) {
                                    try {
                                        JSONObject responseJson = new JSONObject(response.getResponseData());
                                        JSONObject markAttendanceResult = (responseJson.getJSONObject("MarkAttendanceResult"));
                                        if (markAttendanceResult.getInt("ErrorCode") == 0) {
                                            dataSource.updateIsPushed(d);
                                            Log.e(MainActivity.TAG, "Successful Upload");
                                        } else {
                                           /* new AlertCustomDialog(getApplicationContext(),""
                                                            + ((new JSONObject(response
                                                            .getResponseData()))
                                                            .getJSONObject("MarkAttendanceResult"))
                                                            .optString("ErrorMessage", ""));*/
                                            Log.d(UploadInOutLocationService.TAG, "Failed uploading Attendance in Update Transaction, details: " + markAttendanceResult.optString("ErrorMessage", ""));
                                        }
                                    } catch (JSONException e) {
                                        Crashlytics.logException(e);
                                    }

                                }
                            }, AppRequestJSONString.getMarkAttendanceData(empId, refId, d.getmLatitude(), d.getmLongitude(), "ForPhoto", ".jpg", imagePath, "",null,null), CommunicationConstant.API_MARK_ATTANDANCE, false);
                        }
                    }
                }
            }

        } else {
            Log.d(MainActivity.TAG,"Failed to upload");
        }
        // update database

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preference = new Preferences(this);
        String strComments = intent.getStringExtra("strComments");
        //ClockIn, ClockOut, InLocation, OutLocation, TimeIn,TimeOut
        if(Utility.isNetworkAvailable(getApplicationContext())){
            currentTransaction= Transactions.NONE;
            currentTransactionSatus= TransactionSatus.START;
            updateTransactionStatus();
            CheckInOutModel checkInOutModel = ModelManager.getInstance().getCheckInOutModel();
            markAttendanceAPI(Transactions.TIMEINOUT);
            //markAttendanceAPI(Transactions.BUTTON_UPDATE);
            currentTransaction= Transactions.NONE;
            currentTransactionSatus= TransactionSatus.END;
            updateTransactionStatus();

            Log.d(UploadInOutLocationService.TAG,"This was in Service handle intent, saving data to db");
        }else{

            if(transactionProcessListner!=null)
                transactionProcessListner.error(TransactionSatus.ERROR_NO_INTERNETCONNECTION);
        }
    }
}
