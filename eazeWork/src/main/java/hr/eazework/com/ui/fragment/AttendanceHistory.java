package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.History;
import hr.eazework.com.model.LoginUserModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.util.AttendanceUtil;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.ListViewHistoryAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Manjunath on 20-03-2017.
 */

public class AttendanceHistory extends BaseFragment implements AdapterView.OnItemClickListener{
    private ArrayList<History> list = new ArrayList<>();
    protected ListView listView;
    public static String TAG = "AttendanceHistory";

    private History model = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
        rootView = inflater.inflate(R.layout.history_list,container,false);
        listView = (ListView) rootView.findViewById(R.id.expandableList);
        requestAPI();
        return rootView;
    }

    private void requestAPI () {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity)getActivity()).showHideProgress(true);
        String historyInput = AppRequestJSONString.getHistory(get3monthsBack(), getToday(), "0");
        CommunicationManager.getInstance().sendPostRequest(this, historyInput, CommunicationConstant.API_GET_EMP_ATTENDANCE_CALENDER_STATUS,false);
        Log.d(MainActivity.TAG,getToday() + "  this is date with 3 months gap " + get3monthsBack());
    }

    private String getToday() {
        Date today = new Date();
        return DateFormat.format("dd/MM/yyyy",today).toString();
    }

    private String get3monthsBack() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        return DateFormat.format("dd/MM/yyyy",calendar).toString();
    }

    @Override
    public void validateResponse(ResponseData response) {
        super.validateResponse(response);
        JSONObject json;
        try {
            json = new JSONObject(response.getResponseData());
            JSONObject mainJSONObject = json.optJSONObject("GetEmpAttendanceCalendarStatusResult");
            if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                String failedString = mainJSONObject.optString("ErrorMessage", "Failed");
                new AlertCustomDialog(getActivity(), failedString);
            } else {
                JSONArray array = mainJSONObject.optJSONArray("attendCalStatusList");
                populateViews(array);
            }

        } catch (JSONException e) {
            Log.e(TAG,e.getMessage(),e);
            Crashlytics.logException(e);
        }
        MainActivity.isAnimationLoaded = true;
        ((MainActivity)getActivity()).showHideProgress(false);
    }

    private void populateViews(JSONArray array) {
        model = new History(array);
        list = model.getmHistoryList();

        ListViewHistoryAdapter adapter = new ListViewHistoryAdapter(getContext(),R.layout.history_list_item,model.getmHistoryList());
        if(adapter != null) {
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        History.setCurrentHistoryItem(model);
        LoginUserModel loginUserModel = ModelManager.getInstance().getLoginUserModel();
        if(loginUserModel != null && loginUserModel.getUserModel() != null) {
            String empId = loginUserModel.getUserModel().getEmpId();
            AttendanceUtil.initiateHistoryTrackFragment(view,empId,model.getmHistoryList().get(position).getmMarkDate(), position, model, mUserActionListener);
        }
    }
}
