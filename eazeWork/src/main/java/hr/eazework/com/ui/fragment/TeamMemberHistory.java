package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.History;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.AttendanceUtil;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.ListViewHistoryAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

import static hr.eazework.com.ui.util.CalenderUtils.get3monthsBack;
import static hr.eazework.com.ui.util.CalenderUtils.getToday;

/**
 * Created by Manjunath on 02-04-2017.
 */

public class TeamMemberHistory extends BaseFragment implements AdapterView.OnItemClickListener {
    private ArrayList<History> list = new ArrayList<>();
    protected ListView listView;
    public static String TAG = "TeamMemberHistory";
    private String empId = "";
    private History model = null;
    private ListViewHistoryAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empId = getArguments().getString(AppsConstant.EMP_ID, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.history_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.expandableList);
        adapter = new ListViewHistoryAdapter(getContext(), R.layout.history_list_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        requestAPI();
        return rootView;
    }

    private void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity)getActivity()).showHideProgress(true);
        CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getHistory(get3monthsBack(), getToday(), empId), CommunicationConstant.API_GET_EMP_ATTENDANCE_CALENDER_STATUS, false);
    }


    @Override
    public void validateResponse(ResponseData response) {
        super.validateResponse(response);
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EMP_ATTENDANCE_CALENDER_STATUS:
                JSONObject json;
                try {
                    json = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = json.optJSONObject("GetEmpAttendanceCalendarStatusResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        String getEmpAttendanceCalendarStatusResult = json.optJSONObject("GetEmpAttendanceCalendarStatusResult").toString();
                        JSONArray array = new JSONObject(getEmpAttendanceCalendarStatusResult).optJSONArray("attendCalStatusList");
                        populateViews(array);
                    }

                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e(TAG, e.getMessage(), e);
                }
                break;
        }
        MainActivity.isAnimationLoaded = true;
        ((MainActivity)getActivity()).showHideProgress(false);

    }

    private void populateViews(JSONArray array) {
        model = new History(array);
        list = model.getmHistoryList();
        if (adapter != null) {
            adapter.refresh(list);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        History.setCurrentHistoryItem(model);
        History history = list.get(position);
        AttendanceUtil.initiateHistoryTrackFragment(view, empId, history.getmMarkDate(), position, model, mUserActionListener);
    }


}
