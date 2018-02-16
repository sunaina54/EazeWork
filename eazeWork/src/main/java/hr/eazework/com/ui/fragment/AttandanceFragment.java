package hr.eazework.com.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import hr.calender.caldroid.CaldroidFragment;
import hr.calender.caldroid.CaldroidListener;
import hr.calender.caldroid.CalendarHelper;
import hr.calender.hirondelle.date4j.DateTime;
import hr.eazework.com.TimeModificationActivity;
import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.AttandanceCalenderStatusItem;
import hr.eazework.com.model.AttandanceCalenderStatusResult;
import hr.eazework.com.model.CheckInOutModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.BackdatedAttendanceActivity;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.DateTimeUtil;
import hr.eazework.com.ui.util.MLogger;
import hr.eazework.com.ui.util.StringUtils;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.AppRequestJSONString;
import hr.eazework.selfcare.communication.CommunicationConstant;
import hr.eazework.selfcare.communication.CommunicationManager;


public class AttandanceFragment extends BaseFragment {

    public static final String TAG = "AttandanceFragment";
    private CaldroidFragment caldroidFragment;
    private ArrayList<DateTime> mleavesList;
    private ArrayList<DateTime> mHalfDayList;
    private Date mSelectedDate;
    private int mSelectedMonth;
    private int mSelectedYear;
    private String currentDate;
    private ArrayList<AttandanceCalenderStatusItem> attandanceCalenderStatusItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.attandance_detail_root_container, container, false);
        MainActivity.updataProfileData(getActivity(), rootView);

        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setUpCalender(savedInstanceState);
                updateSelectedDateDetail(Calendar.getInstance().getTime());
            }
        }, 500);
        ((MainActivity) getActivity()).showHideProgress(true);
        return rootView;
    }

    private void setUpCalender(Bundle savedInstanceState) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
        // caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
            args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultDark);
            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        try {
            FragmentTransaction t = getChildFragmentManager().beginTransaction();
            t.replace(R.id.calendar_attandance, caldroidFragment);
            t.commit();
        } catch (Exception exception) {
            Crashlytics.logException(exception);
        }
        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                mSelectedDate = date;
                updateSelectedDateDetail(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int month = caldroidFragment.getCurrentMonth();
                int year = caldroidFragment.getCurrentYear();
                if (calendar.get(Calendar.MONTH) != month) {
                    onChangeMonth(month, year);
                    if (calendar.get(Calendar.YEAR) > year) {
                        caldroidFragment.nextMonth();
                    } else if (calendar.get(Calendar.YEAR) < year) {
                        caldroidFragment.prevMonth();
                    } else if (calendar.get(Calendar.MONTH) > month) {
                        caldroidFragment.nextMonth();
                    } else {
                        caldroidFragment.prevMonth();
                    }
                }

                attandanceCalenderStatusItem = AttandanceCalenderStatusResult.getInstance().getAttandanceCalenderStatusItems();
                Log.d("TAG", "Attendance " + attandanceCalenderStatusItem);
                Date dateObj = new Date(date.getTime());
                //Log.d("TAG","CURRENT DATE : "+dateObj);
                //currentDate= Utility.convertStringIntoDate(String.valueOf(dateObj));
                currentDate = DateTimeUtil.convertTimeMillisIntoStringDate(date.getTime(), "dd/MM/yyyy");
                showLog(AttandanceFragment.class, currentDate);
                for (AttandanceCalenderStatusItem item : attandanceCalenderStatusItem) {
                    Log.d("TAG", "Mark Date " + item.getMarkDate());
                    if (item.getMarkDate().equalsIgnoreCase(currentDate)) {
                        if (item.getTimeModYN().equalsIgnoreCase(AppsConstant.YES)) {
                            Intent theIntent = new Intent(getActivity(), TimeModificationActivity.class);
                            TimeModificationActivity.attandanceCalenderStatusItem = item;
                            startActivityForResult(theIntent, TimeModificationActivity.TIMEMODIFICATIONREQUESTCODE);

                        }

                        if (item.getBackDateAttendYN().equalsIgnoreCase(AppsConstant.YES)) {
                            Intent theIntent = new Intent(getActivity(), BackdatedAttendanceActivity.class);
                            BackdatedAttendanceActivity.attandanceCalenderStatusItem = item;
                            startActivityForResult(theIntent, BackdatedAttendanceActivity.TIMEMODIFICATIONREQUESTCODE);

                        }

                        break;
                    }
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                mSelectedMonth = month - 1;
                mSelectedYear = year;
                final Calendar calendar = Calendar.getInstance();
                calendar.set(mSelectedYear, mSelectedMonth, 1);
                if ((mSelectedMonth == Calendar.getInstance().get(Calendar.MONTH)) || !AttandanceCalenderStatusResult.getInstance().isContains(String.format("%1$td/%1$tm/%1$tY", calendar))) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            String empAttendanceInput = AppRequestJSONString.getEmpAttendanceCalendarStatus(String.format("%1$td/%1$tm/%1$tY", calendar), "" + Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + String.format("%1$tm/%1$tY", calendar));
                            CommunicationManager.getInstance().sendPostRequest(AttandanceFragment.this, empAttendanceInput, CommunicationConstant.API_GET_EMP_ATTENDANCE_CALENDER_STATUS, true);
                        }
                    }, 100);
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                /*
				 * Toast.makeText(getActivity(), "Long click " +
				 * formatter.format(date), Toast.LENGTH_SHORT).show();
				 */
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                    ((MainActivity) getActivity()).showHideProgress(false);
                    rootView.findViewById(R.id.ll_bottom_main_container).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.ll_top_main_container).setBackgroundResource(R.drawable.focus_style_gray);
					/*
					 * Toast.makeText(getActivity(), "Caldroid view is created",
					 * Toast.LENGTH_SHORT).show();
					 */
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showLog(AttandanceFragment.class,"ResultCode "+ resultCode+"" + data);
        if (requestCode == TimeModificationActivity.TIMEMODIFICATIONREQUESTCODE && resultCode==1) {
            mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
            showLog(AttandanceFragment.class, "ResultCode1 " + resultCode + "");
        }
    }

    protected void updateSelectedDateDetail(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            ((TextView) rootView.findViewById(R.id.tv_selected_date)).setText("" + calendar.get(Calendar.DAY_OF_MONTH));
            AttandanceCalenderStatusItem attandanceCalenderStatusItem = AttandanceCalenderStatusResult.getInstance().getStatusItem(String.format("%1$td/%1$tm/%1$tY", calendar));

            if (inCurrentDate(calendar)) {
                CheckInOutModel inOutModel = ModelManager.getInstance().getCheckInOutModel();
                if (inOutModel != null && inOutModel.isCheckedIn()) {
                    ((TextView) rootView.findViewById(R.id.tv_date_in_time)).setText(inOutModel.getCheckInTime().equalsIgnoreCase("null") ? "--:--" : inOutModel.getCheckInTime());
                } else {
                    ((TextView) rootView.findViewById(R.id.tv_date_in_time)).setText("--:--");
                }
                if (inOutModel != null && inOutModel.isCheckedOut()) {
                    ((TextView) rootView.findViewById(R.id.tv_date_out_time)).setText(inOutModel.getCheckOutTime().equalsIgnoreCase("null") ? "--:--" : inOutModel.getCheckOutTime());
                } else {
                    ((TextView) rootView.findViewById(R.id.tv_date_out_time))
                            .setText("--:--");
                }

            } else if (attandanceCalenderStatusItem != null) {
                String timeIn = attandanceCalenderStatusItem.getTimeIn();
                String timeOut = attandanceCalenderStatusItem.getTimeOut();
                ((TextView) rootView.findViewById(R.id.tv_date_in_time)).setText(StringUtils.isNotEmptyAndNULLString(timeIn) ? timeIn : "--:--");
                ((TextView) rootView.findViewById(R.id.tv_date_out_time)).setText(StringUtils.isNotEmptyAndNULLString(timeOut) ? timeOut : "--:--");
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
                ((TextView) rootView.findViewById(R.id.tv_date_in_time)).setText(getString(R.string.msg_holiday));
                ((TextView) rootView.findViewById(R.id.tv_date_out_time)).setText("");
            } else {
                ((TextView) rootView.findViewById(R.id.tv_date_in_time)).setText("--:--");
                ((TextView) rootView.findViewById(R.id.tv_date_out_time)).setText("--:--");
            }
            ((TextView) rootView.findViewById(R.id.tv_selected_date_day)).setText(CalendarHelper.getStringDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
        } catch (Exception exception) {
            Crashlytics.logException(exception);
        }
    }

    private boolean inLeaveDate(Calendar calendar) {
        for (DateTime dateTime : mleavesList) {
            if (dateTime.getDay() == calendar.get(Calendar.DAY_OF_MONTH)
                    && dateTime.getMonth() == (calendar.get(Calendar.MONTH) + 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean inCurrentDate(Calendar calendar) {
        Calendar dateTime = Calendar.getInstance();
        if (dateTime.get(Calendar.DAY_OF_MONTH) == calendar
                .get(Calendar.DAY_OF_MONTH)
                && dateTime.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && dateTime.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();
        if (caldroidFragment != null) {

            mleavesList = new ArrayList<DateTime>();
            mHalfDayList = new ArrayList<DateTime>();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            caldroidFragment.setLeaveDates(mleavesList);
            caldroidFragment.setHalfDayDates(mHalfDayList);
        }
    }

    @Override
    public void validateResponse(ResponseData response) {
        if (response.isSuccess() && !isSessionValid(response.getResponseData())) {
            mUserActionListener.performUserAction(IAction.LOGIN_VIEW, null, null);
            return;
        }
        switch (response.getRequestData().getReqApiId()) {
            case CommunicationConstant.API_GET_EMP_ATTENDANCE_CALENDER_STATUS:
                MLogger.debug("", "" + response.getResponseData());
                try {
                    JSONObject jsonObject = new JSONObject(response.getResponseData());
                    JSONObject responseMainJson = jsonObject.optJSONObject("GetEmpAttendanceCalendarStatusResult");
                    if (responseMainJson != JSONObject.NULL && responseMainJson.optInt("ErrorCode", -1) == 0) {
                        AttandanceCalenderStatusResult.getInstance().updateAttandanceCalenderStatusItems(responseMainJson.optJSONArray("attendCalStatusList"));
                        Log.d("TAG", "Month Data : " + responseMainJson.optJSONArray("attendCalStatusList").toString());
                    }
                    updateSelectedDateDetail(mSelectedDate);
                    caldroidFragment.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                AttandanceCalenderStatusResult.getInstance();
                break;
            default:
                break;
        }
    }
}
