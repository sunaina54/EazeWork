package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.StoreLocationModel;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.StoreListAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Manjunath on 28-03-2017.
 */

public class StoreListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    public static String TAG = "StoreListFragment";

    private StoreListAdapter adapter;
    private ArrayList<StoreLocationModel> list = new ArrayList<>();
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.store_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_store);
        adapter = new StoreListAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        requestAPI();
        return rootView;
    }

    private void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity)getActivity()).showHideProgress(true);
        CommunicationManager.getInstance().sendPostRequest(this, AppRequestJSONString.getLocationList(),
                CommunicationConstant.API_GET_LOCATION_LIST, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StoreLocationModel model = list.get(position);
        mUserActionListener.performUserAction(IAction.EDIT_STORE_VIEW, null, model);
    }

    @Override
    public void validateResponse(ResponseData response) {
        if (response.isSuccess() && !isSessionValid(response.getResponseData())) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_LOCATION_LIST:
                JSONObject json;
                try {
                    json = new JSONObject(response.getResponseData());
                    JSONObject mainJSONObject = json.optJSONObject("GetLocationListResult");
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        String getLocationListResult = json.optJSONObject("GetLocationListResult").toString();
                        JSONArray array = new JSONObject(getLocationListResult).optJSONArray("list");
                        if (array.length() > 0) {
                            ((LinearLayout) rootView.findViewById(R.id.llLocation)).setVisibility(View.GONE);
                            populateViews(array);
                        } else {
                            ((LinearLayout) rootView.findViewById(R.id.llLocation)).setVisibility(View.VISIBLE);
                        }

                    }

                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e(TAG, e.getMessage(), e);
                }
                break;
        }


        super.validateResponse(response);
        MainActivity.isAnimationLoaded = true;
        ((MainActivity)getActivity()).showHideProgress(false);
    }

    private void populateViews(JSONArray array) {
        StoreLocationModel model = new StoreLocationModel(array);
        list = model.getmStoreList();
        if (adapter != null) {
            adapter.refresh(list);
        }
    }
}
