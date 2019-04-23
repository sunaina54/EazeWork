package hr.eazework.com.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import hr.eazework.com.model.GetEmpWFHResponseItem;
import hr.eazework.com.model.HolidayList;
import hr.eazework.com.model.HolidayListResponseModel;
import hr.eazework.com.model.MenuItemModel;
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
    public static String screenName = "AttandanceFragment";
    private CaldroidFragment caldroidFragment;
    private ArrayList<DateTime> mleavesList;
    private ArrayList<DateTime> mHalfDayList;
    private Date mSelectedDate;
    private int mSelectedMonth;
    private int mSelectedYear;
    private String currentDate;
    private TextView dayStatusTV;
    private LinearLayout holidayLl;
    private RecyclerView holidayRecyclerView;
    private HolidayAdapter holidayAdapter;
    private Button markAttendanceBTN, timeModificationBTN;
    private ArrayList<AttandanceCalenderStatusItem> attandanceCalenderStatusItem;
    private AttandanceCalenderStatusItem item = null;
    private HolidayList holidayList;
    private LinearLayout errorHolidayLl,tourLl,wfhLl,leaveLl;
    private TextView shiftTimeTV, weeklyOffTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.attandance_detail_root_container, container, false);
        dayStatusTV = (TextView) rootView.findViewById(R.id.dayStatusTV);
        errorHolidayLl = (LinearLayout) rootView.findViewById(R.id.errorHolidayLl);
        errorHolidayLl.setVisibility(View.GONE);
        shiftTimeTV = (TextView) rootView.findViewById(R.id.shiftTimeTV);
        weeklyOffTV = (TextView) rootView.findViewById(R.id.weeklyOffTV);

        tourLl= (LinearLayout) rootView.findViewById(R.id.tourLl);
        tourLl.setVisibility(View.GONE);
        wfhLl= (LinearLayout) rootView.findViewById(R.id.wfhLl);
        wfhLl.setVisibility(View.GONE);
        leaveLl= (LinearLayout) rootView.findViewById(R.id.leaveLl);
        leaveLl.setVisibility(View.GONE);
        MenuItemModel menuItemModel = ModelManager.getInstance().getMenuItemModel();
        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.TOUR_REQUEST);
            if (itemModel != null && itemModel.isAccess()) {
                tourLl.setVisibility(View.VISIBLE);
            }
        }

        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.WORK_FROM_HOME);
            if (itemModel != null && itemModel.isAccess()) {
                wfhLl.setVisibility(View.VISIBLE);
            }
        }

        if (menuItemModel != null) {
            MenuItemModel itemModel = menuItemModel.getItemModel(MenuItemModel.LEAVE_KEY);
            if (itemModel != null && itemModel.isAccess()) {
                leaveLl.setVisibility(View.VISIBLE);
            }
        }
        markAttendanceBTN = (Button) rootView.findViewById(R.id.markAttendanceBTN);
        timeModificationBTN = (Button) rootView.findViewById(R.id.timeModificationBTN);
        holidayLl = (LinearLayout) rootView.findViewById(R.id.holidayLl);
        holidayRecyclerView = (RecyclerView) rootView.findViewById(R.id.holidayRecyclerView);
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

        holidayListRequest();
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
        showLog(AttandanceFragment.class, "ResultCode " + resultCode + "" + data);
        if (requestCode == TimeModificationActivity.TIMEMODIFICATIONREQUESTCODE && resultCode == 1) {
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
                dayStatusTV.setText(attandanceCalenderStatusItem.getStatusDesc());
                ((TextView) rootView.findViewById(R.id.tv_date_in_time)).setText(StringUtils.isNotEmptyAndNULLString(timeIn) ? timeIn : "--:--");
                ((TextView) rootView.findViewById(R.id.tv_date_out_time)).setText(StringUtils.isNotEmptyAndNULLString(timeOut) ? timeOut : "--:--");
                attendanceData(date);
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
            case CommunicationConstant.API_GET_HOLIDAY_LIST:
                String str = response.getResponseData();
                Log.d("TAG", "WFH Response : " + str);
                HolidayListResponseModel holidayListResponseModel = HolidayListResponseModel.create(str);
                if (holidayListResponseModel != null && holidayListResponseModel.getGetHolidayListResult() != null
                        && holidayListResponseModel.getGetHolidayListResult().getErrorCode().equalsIgnoreCase(AppsConstant.SUCCESS)) {
                    shiftTimeTV.setText(holidayListResponseModel.getGetHolidayListResult().getShiftDet());
                    weeklyOffTV.setText(holidayListResponseModel.getGetHolidayListResult().getWeeklyOffDet());
                    if (holidayListResponseModel.getGetHolidayListResult().getHolidayList() != null && holidayListResponseModel.getGetHolidayListResult().getHolidayList().size() > 0) {
                        refresh(holidayListResponseModel.getGetHolidayListResult().getHolidayList());
                    }
                }

                break;
            default:
                break;
        }
    }

    private void attendanceData(Date date) {
        markAttendanceBTN.setVisibility(View.GONE);
        timeModificationBTN.setVisibility(View.GONE);
        attandanceCalenderStatusItem = AttandanceCalenderStatusResult.getInstance().getAttandanceCalenderStatusItems();
        Log.d("TAG", "Attendance " + attandanceCalenderStatusItem);
        Date dateObj = new Date(date.getTime());
        currentDate = DateTimeUtil.convertTimeMillisIntoStringDate(date.getTime(), "dd/MM/yyyy");
        showLog(AttandanceFragment.class, currentDate);

        for (final AttandanceCalenderStatusItem item1 : attandanceCalenderStatusItem) {
            Log.d("TAG", "Mark Date " + item1.getMarkDate());
            if (item1.getMarkDate().equalsIgnoreCase(currentDate)) {

                item = item1;
                if (item.getBackDateAttendYN().equalsIgnoreCase(AppsConstant.YES)) {
                    markAttendanceBTN.setVisibility(View.VISIBLE);
                    markAttendanceBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent theIntent = new Intent(getActivity(), BackdatedAttendanceActivity.class);
                            BackdatedAttendanceActivity.attandanceCalenderStatusItem = item;
                            startActivityForResult(theIntent, BackdatedAttendanceActivity.TIMEMODIFICATIONREQUESTCODE);
                        }
                    });
                }

                if (item.getTimeModYN().equalsIgnoreCase(AppsConstant.YES)) {
                    timeModificationBTN.setVisibility(View.VISIBLE);
                    timeModificationBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent theIntent = new Intent(getActivity(), TimeModificationActivity.class);
                            TimeModificationActivity.attandanceCalenderStatusItem = item;
                            startActivityForResult(theIntent, TimeModificationActivity.TIMEMODIFICATIONREQUESTCODE);
                        }
                    });


                }
                break;
            }
        }


    }

    private void holidayListRequest() {
        CommunicationManager.getInstance().sendPostRequest(this,
                AppRequestJSONString.holidayListRequest(),
                CommunicationConstant.API_GET_HOLIDAY_LIST, true);
    }

    private void refresh(ArrayList<HolidayList> holidayLists) {

        if (holidayLists != null && holidayLists.size() > 0 && holidayLists.get(0) != null) {
            errorHolidayLl.setVisibility(View.GONE);
            holidayAdapter = new HolidayAdapter(holidayLists);
            holidayRecyclerView.setAdapter(holidayAdapter);
            holidayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            holidayAdapter.notifyDataSetChanged();
        } else {
            errorHolidayLl.setVisibility(View.VISIBLE);
        }
    }

    private class HolidayAdapter extends
            RecyclerView.Adapter<HolidayAdapter.MyViewHolder> {
        private ArrayList<HolidayList> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView holidayTV, holidayDateTV, holidayDayTV, holidayDescTV;
            private LinearLayout holidayHeaderLl;


            public MyViewHolder(View v) {
                super(v);

                holidayTV = (TextView) v.findViewById(R.id.holidayTV);
                holidayDateTV = (TextView) v.findViewById(R.id.holidayDateTV);
                holidayDayTV = (TextView) v.findViewById(R.id.holidayDayTV);
                holidayDescTV = (TextView) v.findViewById(R.id.holidayDescTV);
                holidayHeaderLl = (LinearLayout) v.findViewById(R.id.holidayHeaderLl);


            }

        }

        public void addAll(List<HolidayList> list) {

            dataSet.addAll(list);
            notifyDataSetChanged();
        }

        public HolidayAdapter(List<HolidayList> data) {
            this.dataSet = (ArrayList<HolidayList>) data;

        }

        @Override
        public HolidayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holiday_item, parent, false);
            //view.setOnClickListener(MainActivity.myOnClickListener);
            HolidayAdapter.MyViewHolder myViewHolder = new HolidayAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final HolidayAdapter.MyViewHolder holder, final int listPosition) {
            final HolidayList item = dataSet.get(listPosition);
            holder.holidayHeaderLl.setVisibility(View.GONE);
            if (listPosition == 0) {
                holder.holidayHeaderLl.setVisibility(View.VISIBLE);
            }

            holder.holidayTV.setText(item.getHolidayDesc());
            holder.holidayDateTV.setText(item.getHolidayDate());
            holder.holidayDayTV.setText(item.getWeekDay());
            holder.holidayDescTV.setText(item.getHolidayTypeDesc());

        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public void clearDataSource() {
            dataSet.clear();
            notifyDataSetChanged();
        }
    }
}
