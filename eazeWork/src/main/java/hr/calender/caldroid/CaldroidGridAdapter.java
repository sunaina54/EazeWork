package hr.calender.caldroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hr.calender.hirondelle.date4j.DateTime;
import hr.eazework.com.R;
import hr.eazework.com.model.AttandanceCalenderStatusItem;
import hr.eazework.com.model.AttandanceCalenderStatusResult;

/**
 * The CaldroidGridAdapter provides customized view for the dates gridview
 * 
 * @author thomasdao
 */
public class CaldroidGridAdapter extends BaseAdapter {
	protected ArrayList<DateTime> datetimeList;
	protected int month;
	protected int year;
	protected Context context;
	protected ArrayList<DateTime> disableDates;
	protected ArrayList<DateTime> selectedDates;
	protected ArrayList<DateTime> leaveDates;
	protected ArrayList<DateTime> compOffDates;
	protected ArrayList<DateTime> halfDayDates;

	// Use internally, to make the search for date faster instead of using
	// indexOf methods on ArrayList
	protected HashMap<DateTime, Integer> disableDatesMap = new HashMap<DateTime, Integer>();
	protected HashMap<DateTime, Integer> selectedDatesMap = new HashMap<DateTime, Integer>();
	protected HashMap<DateTime, Integer> leaveDatesMap = new HashMap<DateTime, Integer>();
	protected HashMap<DateTime, Integer> compOffDatesMap = new HashMap<DateTime, Integer>();
	protected HashMap<DateTime, Integer> halfDayDatesMap = new HashMap<DateTime, Integer>();

	protected DateTime minDateTime;
	protected DateTime maxDateTime;
	protected DateTime today;
	protected int startDayOfWeek;
	protected boolean sixWeeksInCalendar;
	protected boolean squareTextViewCell;
	protected int themeResource;
	protected Resources resources;

	protected int defaultCellBackgroundRes = -1;
	protected ColorStateList defaultTextColorRes;

	/**
	 * caldroidData belongs to Caldroid
	 */
	protected HashMap<String, Object> caldroidData;
	/**
	 * extraData belongs to client
	 */
	protected HashMap<String, Object> extraData;

	public void setAdapterDateTime(DateTime dateTime) {
		this.month = dateTime.getMonth();
		this.year = dateTime.getYear();
		this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year,
				startDayOfWeek, sixWeeksInCalendar);
	}

	// GETTERS AND SETTERS
	public ArrayList<DateTime> getDatetimeList() {
		return datetimeList;
	}

	public DateTime getMinDateTime() {
		return minDateTime;
	}

	public void setMinDateTime(DateTime minDateTime) {
		this.minDateTime = minDateTime;
	}

	public DateTime getMaxDateTime() {
		return maxDateTime;
	}

	public void setMaxDateTime(DateTime maxDateTime) {
		this.maxDateTime = maxDateTime;
	}

	public ArrayList<DateTime> getDisableDates() {
		return disableDates;
	}

	public void setDisableDates(ArrayList<DateTime> disableDates) {
		this.disableDates = disableDates;
	}

	public ArrayList<DateTime> getSelectedDates() {
		return selectedDates;
	}

	public void setSelectedDates(ArrayList<DateTime> selectedDates) {
		this.selectedDates = selectedDates;
	}

	public ArrayList<DateTime> getLeaveDates() {
		return leaveDates;
	}

	public ArrayList<DateTime> getcompOffDates() {
		return compOffDates;
	}

	public void setLeaveDates(ArrayList<DateTime> selectedDates) {
		this.leaveDates = selectedDates;
	}

	public void setcompOffDates(ArrayList<DateTime> selectedDates) {
		this.compOffDates = selectedDates;
	}

	public ArrayList<DateTime> gethalfDayDates() {
		return halfDayDates;
	}

	public void setHalfDayDates(ArrayList<DateTime> selectedDates) {
		this.halfDayDates = selectedDates;
	}

	public int getThemeResource() {
		return themeResource;
	}

	public HashMap<String, Object> getCaldroidData() {
		return caldroidData;
	}

	public void setCaldroidData(HashMap<String, Object> caldroidData) {
		this.caldroidData = caldroidData;

		// Reset parameters
		populateFromCaldroidData();
	}

	public HashMap<String, Object> getExtraData() {
		return extraData;
	}

	public void setExtraData(HashMap<String, Object> extraData) {
		this.extraData = extraData;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param month
	 * @param year
	 * @param caldroidData
	 * @param extraData
	 */
	public CaldroidGridAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData) {
		super();
		this.month = month;
		this.year = year;
		this.context = context;
		this.caldroidData = caldroidData;
		this.resources = context.getResources();

		this.extraData = extraData;
		// Get data from caldroidData
		populateFromCaldroidData();
	}

	/**
	 * Retrieve internal parameters from caldroid data
	 */
	@SuppressWarnings("unchecked")
	private void populateFromCaldroidData() {
		disableDates = (ArrayList<DateTime>) caldroidData
				.get(CaldroidFragment.DISABLE_DATES);
		if (disableDates != null) {
			disableDatesMap.clear();
			for (DateTime dateTime : disableDates) {
				disableDatesMap.put(dateTime, 1);
			}
		}

		selectedDates = (ArrayList<DateTime>) caldroidData
				.get(CaldroidFragment.SELECTED_DATES);
		if (selectedDates != null) {
			selectedDatesMap.clear();
			for (DateTime dateTime : selectedDates) {
				selectedDatesMap.put(dateTime, 1);
			}
		}

		halfDayDates = (ArrayList<DateTime>) caldroidData
				.get(CaldroidFragment.HALF_DAY_DATES);
		if (halfDayDates != null) {
			halfDayDatesMap.clear();
			for (DateTime dateTime : halfDayDates) {
				halfDayDatesMap.put(dateTime, 1);
			}
		}

		leaveDates = (ArrayList<DateTime>) caldroidData
				.get(CaldroidFragment.LEAVE_DATES);
		if (leaveDates != null) {
			leaveDatesMap.clear();
			for (DateTime dateTime : leaveDates) {
				leaveDatesMap.put(dateTime, 1);
			}
		}

		compOffDates = (ArrayList<DateTime>) caldroidData
				.get(CaldroidFragment.COMP_OFF_DATES);
		if (compOffDates != null) {
			compOffDatesMap.clear();
			for (DateTime dateTime : compOffDates) {
				compOffDatesMap.put(dateTime, 1);
			}
		}

		minDateTime = (DateTime) caldroidData
				.get(CaldroidFragment._MIN_DATE_TIME);
		maxDateTime = (DateTime) caldroidData
				.get(CaldroidFragment._MAX_DATE_TIME);
		startDayOfWeek = (Integer) caldroidData
				.get(CaldroidFragment.START_DAY_OF_WEEK);
		sixWeeksInCalendar = (Boolean) caldroidData
				.get(CaldroidFragment.SIX_WEEKS_IN_CALENDAR);
		squareTextViewCell = (Boolean) caldroidData
				.get(CaldroidFragment.SQUARE_TEXT_VIEW_CELL);

		// Get theme
		themeResource = (Integer) caldroidData
				.get(CaldroidFragment.THEME_RESOURCE);

		this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year,
				startDayOfWeek, sixWeeksInCalendar);

		getDefaultResources();
	}

	// This method retrieve default resources for background and text color,
	// based on the Caldroid theme
	private void getDefaultResources() {
		Context wrapped = new ContextThemeWrapper(context, themeResource);

		// Get style of normal cell or square cell in the theme
		Resources.Theme theme = wrapped.getTheme();
		TypedValue styleCellVal = new TypedValue();
		if (squareTextViewCell) {
			theme.resolveAttribute(R.attr.styleCaldroidSquareCell,
					styleCellVal, true);
		} else {
			theme.resolveAttribute(R.attr.styleCaldroidNormalCell,
					styleCellVal, true);
		}

		// Get default background of cell
		TypedArray typedArray = wrapped.obtainStyledAttributes(
				styleCellVal.data, R.styleable.Cell);
		defaultCellBackgroundRes = typedArray.getResourceId(
				R.styleable.Cell_android_background, -1);
		defaultTextColorRes = typedArray
				.getColorStateList(R.styleable.Cell_android_textColor);
		typedArray.recycle();
	}

	public void updateToday() {
		today = CalendarHelper.convertDateToDateTime(new Date());
	}

	protected DateTime getToday() {
		if (today == null) {
			today = CalendarHelper.convertDateToDateTime(new Date());
		}
		return today;
	}

	@SuppressWarnings("unchecked")
	protected void setCustomResources(DateTime dateTime, View backgroundView,
			TextView textView) {
		// Set custom background resource
		HashMap<DateTime, Integer> backgroundForDateTimeMap = (HashMap<DateTime, Integer>) caldroidData
				.get(CaldroidFragment._BACKGROUND_FOR_DATETIME_MAP);
		if (backgroundForDateTimeMap != null) {
			// Get background resource for the dateTime
			Integer backgroundResource = backgroundForDateTimeMap.get(dateTime);

			// Set it
			if (backgroundResource != null) {
				backgroundView.setBackgroundResource(backgroundResource);
			}
		}

		// Set custom text color
		HashMap<DateTime, Integer> textColorForDateTimeMap = (HashMap<DateTime, Integer>) caldroidData
				.get(CaldroidFragment._TEXT_COLOR_FOR_DATETIME_MAP);
		if (textColorForDateTimeMap != null) {
			// Get textColor for the dateTime
			Integer textColorResource = textColorForDateTimeMap.get(dateTime);

			// Set it
			if (textColorResource != null) {
				textView.setTextColor(resources.getColor(textColorResource));
			}
		}
	}

	private void resetCustomResources(CellView cellView) {
		cellView.setBackgroundResource(defaultCellBackgroundRes);
		cellView.setTextColor(defaultTextColorRes);
	}

	/**
	 * Customize colors of text and background based on states of the cell
	 * (disabled, active, selected, etc)
	 * <p/>
	 * To be used only in getView method
	 * 
	 * @param position
	 * @param cellView
	 */
	protected void customizeTextView(int position, View convertView) {

		CellView cellView = (CellView) convertView
				.findViewById(R.id.calendar_tv);
		// Get the padding of cell so that it can be restored later
		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);

		cellView.resetCustomStates();
		resetCustomResources(cellView);

		convertView.findViewById(R.id.view_selected_date).setVisibility(
				View.GONE);
		convertView.findViewById(R.id.tv_date_status).setVisibility(View.GONE);
		if (dateTime.equals(getToday())) {
			cellView.addCustomState(CellView.STATE_TODAY);
            int dpi =  context.getResources().getDisplayMetrics().densityDpi;
            Log.d("DPI",dpi+ "  this is dpi");
            if(dpi > 401) {
                cellView.setTextSize(context.getResources().getDimension(
                        R.dimen.text_10));

            } else {
                cellView.setTextSize(context.getResources().getDimension(
                        R.dimen.font_14));

            }

		}

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			cellView.addCustomState(CellView.STATE_PREV_NEXT_MONTH);
		}

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDatesMap
						.containsKey(dateTime))) {

			cellView.addCustomState(CellView.STATE_DISABLED);
		}
		if (AttandanceCalenderStatusResult.getInstance().isContains(
				"" + dateTime.format("DD/MM/YYYY"))) {
			AttandanceCalenderStatusItem attandanceCalenderStatusItem = AttandanceCalenderStatusResult
					.getInstance().getStatusItem(
							"" + dateTime.format("DD/MM/YYYY"));
			Log.d("TAG","date : "+attandanceCalenderStatusItem.getMarkDate()+" Status : "+attandanceCalenderStatusItem.getStatus());
			int state = -1;
			try {
				state = Integer.parseInt(attandanceCalenderStatusItem
						.getStatus());
			} catch (Exception e) {
				// TODO: handle exception
			}
			/**
			 * 0=Present 1,2=Leave 3=Weekly off or complusory holiday 4=Not yet
			 * joined / resigned 5=Calendar Not Defined 6=RH Type Leave 7=Absent
			 * */
			switch (state) {
			case 0:
				cellView.addCustomState(CellView.STATE_PRESENT);
				// showSubTitle(convertView, "CL", CellView.STATE_LEAVE);
				break;
			case 1:
			case 2:
				cellView.addCustomState(CellView.STATE_LEAVE);
				showSubTitle(convertView, "L", CellView.STATE_LEAVE);
				break;
			case 3:
				Log.d("TAG","Comp Off"+CellView.STATE_COMP_OFF);
				cellView.addCustomState(CellView.STATE_COMP_OFF);
				showSubTitle(convertView, "", CellView.STATE_COMP_OFF);
			case 4:
				// showSubTitle(convertView, "HD", CellView.STATE_HALF_DAY);
				break;
			case 6:
				cellView.addCustomState(CellView.STATE_HALF_DAY);
				showSubTitle(convertView, "RH", CellView.STATE_HALF_DAY);
				break;
			case 7:
				cellView.addCustomState(CellView.STATE_ABSENT);
				showSubTitle(convertView, "A", CellView.STATE_ABSENT);
			default:
				break;
			}
			cellView.addCustomState(CellView.STATE_SELECTED);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(CalendarHelper.convertDateTimeToDate(dateTime));
			if (cal.get(Calendar.DAY_OF_WEEK) % 6 == 1) {
				cellView.addCustomState(CellView.STATE_COMP_OFF);
			}
		}
		// Customize for selected dates
		/*
		 * if (selectedDates != null && selectedDatesMap.containsKey(dateTime))
		 * { cellView.addCustomState(CellView.STATE_SELECTED); }
		 * 
		 * Calendar cal = Calendar.getInstance();
		 * cal.setmTime(CalendarHelper.convertDateTimeToDate(dateTime)); if
		 * (cal.get(Calendar.DAY_OF_WEEK) % 6 == 1) {
		 * cellView.addCustomState(CellView.STATE_COMP_OFF); } else // Customize
		 * for Comp Off dates if (compOffDates != null &&
		 * compOffDatesMap.containsKey(dateTime)) {
		 * cellView.addCustomState(CellView.STATE_COMP_OFF);
		 * showSubTitle(convertView, "OFF", CellView.STATE_COMP_OFF); } else //
		 * Customize for leave dates if (leaveDates != null &&
		 * leaveDatesMap.containsKey(dateTime)) {
		 * cellView.addCustomState(CellView.STATE_LEAVE);
		 * showSubTitle(convertView, "CL", CellView.STATE_LEAVE); } else
		 * 
		 * // Customize for halfDay dates if (halfDayDates != null &&
		 * halfDayDatesMap.containsKey(dateTime)) {
		 * cellView.addCustomState(CellView.STATE_HALF_DAY);
		 * showSubTitle(convertView, "HD", CellView.STATE_HALF_DAY); }
		 */

		cellView.refreshDrawableState();
		// Set text
		cellView.setText("" + dateTime.getDay());

		// Set custom color if required
		setCustomResources(dateTime, cellView, cellView);

		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);
	}

	private void showSubTitle(View convertView, String string, int stateHalfDay) {
		((TextView) convertView.findViewById(R.id.tv_date_status))
				.setText(string);
		convertView.findViewById(R.id.view_selected_date).setVisibility(
				View.VISIBLE);
		if (stateHalfDay == CellView.STATE_HALF_DAY)
			convertView.findViewById(R.id.view_selected_date)
					.setBackgroundColor(
							context.getResources().getColor(
									R.color.primary_blue_light));
		else if (stateHalfDay == CellView.STATE_ABSENT)
			convertView.findViewById(R.id.view_selected_date)
					.setBackgroundColor(
							context.getResources().getColor(
									R.color.primary_dark));
		else if (stateHalfDay == CellView.STATE_LEAVE)
			convertView.findViewById(R.id.view_selected_date)
					.setBackgroundColor(
							context.getResources().getColor(
									R.color.primary_pink));
		else if (stateHalfDay == CellView.STATE_COMP_OFF)
			convertView.findViewById(R.id.view_selected_date)
					.setBackgroundColor(
							context.getResources().getColor(
									R.color.primary_blue));

		if (stateHalfDay == CellView.STATE_COMP_OFF)
			convertView.findViewById(R.id.tv_date_status).setVisibility(
					View.VISIBLE);
		else
			convertView.findViewById(R.id.tv_date_status).setVisibility(
					View.VISIBLE);

		((TextView) convertView.findViewById(R.id.tv_date_status))
				.setTextColor(context.getResources().getColor(
						android.R.color.darker_gray));
	}

	@Override
	public int getCount() {
		return this.datetimeList.size();
	}

	@Override
	public Object getItem(int position) {
		return datetimeList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LayoutInflater localInflater = CaldroidFragment.getLayoutInflater(
				context, inflater, themeResource);

		// For reuse
		if (convertView == null) {
			if (squareTextViewCell) {
				convertView = localInflater.inflate(R.layout.square_date_cell,
						null);
			} else {
				convertView = localInflater.inflate(R.layout.normal_date_cell,
						null);
			}
		}

		customizeTextView(position, convertView);

		return convertView;
	}

}
