package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.History;
import hr.eazework.com.model.HistoryTimelineModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.adapter.ListViewHistorySublistAdapter;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;

/**
 * Created by Manjunath on 24-03-2017.
 */

public class HistoryTrackFragment extends BaseFragment {
    private TextView date, timeIn, timeOut, hour;
    private ListView listView;
    private LinearLayout layout;
    private ImageView ivTimeIn, ivTimeOut;
    private Map currentItemContext = null;
    private LinearLayout llTimeInOut;

    ArrayList<HistoryTimelineModel> list = new ArrayList<>();

    public static String TAG = "HistoryTrackFragment";

    private ListViewHistorySublistAdapter adapter;

    private String attendId = "";
    private String markDate = "";
    private String empId = "";
    private History history = null;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setShowPlusMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.history_list_track_fragment, container, false);
        date = (TextView) rootView.findViewById(R.id.tvHistoryDate);
        timeIn = (TextView) rootView.findViewById(R.id.tvHistoryTimeIn);
        timeOut = (TextView) rootView.findViewById(R.id.tvHistoryTimeOut);
        listView = (ListView) rootView.findViewById(R.id.lvHistorySub);
        hour = (TextView) rootView.findViewById(R.id.tvHistoryTime);
        layout = (LinearLayout) rootView.findViewById(R.id.llEvents);
        ivTimeIn = (ImageView) rootView.findViewById(R.id.ivTimeIn);
        ivTimeOut = (ImageView) rootView.findViewById(R.id.ivTimeOut);
        llTimeInOut = (LinearLayout) rootView.findViewById(R.id.llTimeInOut);

        adapter = new ListViewHistorySublistAdapter(getContext(), R.layout.history_list_track_fragment, list);
        listView.setAdapter(adapter);
        System.out.println(list.size() + " list size providing");

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).popBackStack();
            }
        });

        if (currentItemContext != null) {
            markDate = (String) currentItemContext.get(AppsConstant.MARK_DATE);
            empId = (String) currentItemContext.get(AppsConstant.EMP_ID);
            position = (int) currentItemContext.get(AppsConstant.POSITION_KEY);
            history = ((History) currentItemContext.get(AppsConstant.HISTORY_KEY)).getmHistoryList().get(position);
        }


        if (history != null) {
            date.setText(history.getmMarkDate());
            timeIn.setText(history.getmTimeIn());
            timeOut.setText(history.getmTimeOut());
            hour.setText(history.getmStatusDesc());
        }

        requestAPI();

        return rootView;
    }

    private void requestAPI() {
        MainActivity.isAnimationLoaded = false;
        ((MainActivity)getActivity()).showHideProgress(true);
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.getAttendanceTimeLine(empId, "0", markDate),
                CommunicationConstant.API_GET_ATTENDANCE_TIMELINE, false);
    }

    @Override
    public void validateResponse(ResponseData response) {
        MainActivity.isAnimationLoaded = true;
        ((MainActivity)getActivity()).showHideProgress(false);
        super.validateResponse(response);
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_ATTENDANCE_TIMELINE:
                JSONObject json;
                try {
                    json = new JSONObject(response.getResponseData());
                    JSONObject getAttendanceTimeLineResult = json.optJSONObject("GetAttendanceTimeLineResult");
                    JSONObject mainJSONObject = getAttendanceTimeLineResult;
                    if (mainJSONObject.optInt("ErrorCode", -1) != 0) {
                        String errorMessage = mainJSONObject.optString("ErrorMessage", "Failed");
                        new AlertCustomDialog(getActivity(), errorMessage);
                    } else {
                        JSONArray array = new JSONObject(getAttendanceTimeLineResult.toString()).optJSONArray("attendanceTimeLine");
                        final String selfieYN = getAttendanceTimeLineResult.optString("SelfieYN","N");
                        final String timeInPath = getAttendanceTimeLineResult.optString("ImagePath", "");
                        final String timeOutPath = getAttendanceTimeLineResult.optString("ImagePathOut", "");

                        if(selfieYN.equalsIgnoreCase("y")) {
                            llTimeInOut.setVisibility(View.VISIBLE);
                        } else {
                            llTimeInOut.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(timeInPath) && history != null && !TextUtils.isEmpty(history.getmTimeIn())) {
                            activateImage(timeInPath,ivTimeIn);
                        } else {
                            deactivateImage(ivTimeIn);
                        }

                        if (!TextUtils.isEmpty(timeOutPath) && history != null && !TextUtils.isEmpty(history.getmTimeOut())) {
                            activateImage(timeOutPath,ivTimeOut);
                        } else {
                            deactivateImage(ivTimeOut);
                        }


                        populateViews(array);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    Crashlytics.logException(e);
                }
                break;
        }


    }

    public void deactivateImage(ImageView imageView) {
        imageView.setImageResource(R.drawable.na);
        imageView.setEnabled(false);
    }

    public void activateImage(final String timeInPath,ImageView imageView) {
        loadTimeLineImage(timeInPath, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(timeInPath);
            }
        });
    }

    public void showImage(String timeInPath) {
        StorePhotoFragment f = new StorePhotoFragment();
        FragmentManager fm = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("store_photo", timeInPath);
        f.setArguments(bundle);
        f.show(fm, "Photo_Dialog");
    }

    public void loadTimeLineImage(String imagePath, final ImageView ivTimeLineImageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            String path = CommunicationConstant.getMobileCareURl() + imagePath;
            Picasso picasso = Picasso.with(getContext());
            RequestCreator requestCreator = picasso.load(path)/*.into(headerImage)*/;
            requestCreator.networkPolicy(NetworkPolicy.NO_CACHE);
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE);
            requestCreator.placeholder(R.drawable.camera);
            requestCreator.fit();
            requestCreator.into(ivTimeLineImageView,
                    new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            ivTimeLineImageView.setImageResource(R.drawable.na);
                            ivTimeLineImageView.setEnabled(false);
                        }
                    });
        } else {
            ivTimeLineImageView.setImageResource(R.drawable.na);
            ivTimeLineImageView.setEnabled(false);
        }
    }

    private void populateViews(JSONArray array) {
        HistoryTimelineModel model = new HistoryTimelineModel(array);
        list = model.getmTimelineList();
        if (adapter != null) {
            adapter.refresh(list);
        }
    }

    public Map getCurrentItemContext() {
        return currentItemContext;
    }

    public void setCurrentItemContext(Map currentItemContext) {
        this.currentItemContext = currentItemContext;
    }
}
