package hr.eazework.com.ui.util;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hr.eazework.com.MainActivity;
import hr.eazework.com.model.History;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.UserModel;
import hr.eazework.com.ui.fragment.BaseFragment;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.interfaces.UserActionListner;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Manjunath on 11-04-2017.
 */

public class AttendanceUtil {

    public static String getRequestTypeString(int currentReqType){
        if(currentReqType == 0){
            return "TimeIn";
        }else if(currentReqType == 1){
            return "BreakIn";
        }else if(currentReqType == 2){
            return "BreakOut";
        }else if(currentReqType == 3){
            return "TimeOut";
        }else if(currentReqType == 4){
            return "Cancel TimeOut";
        }else if(currentReqType == 6){
            return "InLocation";
        }else if(currentReqType == 6){
            return "OutLocation";
        }

        return null;
    }

    public static int getRequestType(String status) {
        if(status.equalsIgnoreCase("TimeIn")) {
            return 0;
        } else if("TimeOut".equalsIgnoreCase(status)) {
            return 3;
        } else if("InLocation".equalsIgnoreCase(status)) {
            return 6;
        } else if("OutLocation".equalsIgnoreCase(status)) {
            return 7;
        }
        return -1;
    }


    public static void performAttendanceAction(Activity activity, BaseFragment currentFragment, int currentReqType,String latitude,String longitude,String fileName,
                                               String fileExtension, String imagePath, String formattedAddress, String geoRadius, String geoDistance) {
        String geoAddress = "";
        if (activity != null) {
            MainActivity.isAnimationLoaded = false;
            ((MainActivity) activity).showHideProgress(true);
        }

        if(!TextUtils.isEmpty(formattedAddress)) {
            geoAddress = formattedAddress;
        }


        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        if(loginUserModel != null) {
            UserModel userModel = loginUserModel.getUserModel();
            if(userModel != null) {
                String empId = userModel.getEmpId();
                String binaryFile;
                if(!TextUtils.isEmpty(imagePath) && !"null".equals(imagePath)){
                    binaryFile = ImageUtil.encodeImage(imagePath);
                } else {
                    binaryFile = "";
                }
                String attendanceJSONString = AppRequestJSONString.getMarkAttendanceData(empId, currentReqType,latitude,longitude,fileName,fileExtension,binaryFile,geoAddress,geoRadius,geoDistance);
                CommunicationManager.getInstance().sendPostRequest(currentFragment,attendanceJSONString,CommunicationConstant.API_MARK_ATTANDANCE,true);
                currentFragment.showHideProgressView(true);
            }
        }
    }

    public static void initiateHistoryTrackFragment(View view, String empId, String markDate, int position, History model, UserActionListner mUserActionListener) {
        Map b = new HashMap();
        b.put(AppsConstant.EMP_ID,empId);
        b.put(AppsConstant.MARK_DATE,markDate);
        b.put(AppsConstant.POSITION_KEY,position);
        b.put(AppsConstant.HISTORY_KEY, History.getCurrentHistoryItem());
        mUserActionListener.performUserAction(IAction.HISTORY_TRACK_VIEW,view,b);
    }

}
