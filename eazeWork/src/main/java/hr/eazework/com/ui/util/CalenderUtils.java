package hr.eazework.com.ui.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 08-Dec-15.
 */
public class CalenderUtils {
    public static final String DateMonthDashedFormate = "dd-MM-yyyy";
    public static final String YearMonthDashedFormate = "yyyy-MM-dd";
    public static final String DateMonthSlashFormate = "dd/MM/yyyy";
    public static final String MonthDateFormate = "MM/dd/yyyy";
    public static final String DateFormate = "yyyy-MM-dd HH:mm:ss";
    public static final String TimeSecFormate = "HH:mm:ss";
    public static final String TimeMinFormate = "HH:mm";
    public static final String DateFormateWithZone = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DateFormateWithoutZone = "yyyy-MM-dd'T'HH:mm:ss";

    public static boolean isDateGreater(String firstDate,String secondDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = sdf.parse(firstDate);
            Date date2 = sdf.parse(secondDate);
            if (date2.after(date1) || date2.equals(date1)) {
                return true;
            }
        }catch(Exception e){

        }
        return false;
    }
    public static boolean isDateEqual(String firstDate,String secondDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = sdf.parse(firstDate);
            Date date2 = sdf.parse(secondDate);
            if (date2.equals(date1)) {
                return true;
            }
        }catch(Exception e){

        }
        return false;
    }

    public static long getDateDifference(String cureentDate, String finalDate, String dateFormat) {
        long mills, days = 0;
        String strTime = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            Date Date1 = format.parse(finalDate);
            Date Date2 = format.parse(cureentDate);
            mills = Date1.getTime() - Date2.getTime();
            days = mills / (1000 * 60 * 60 * 24);
        } catch (Exception e) {

            Crashlytics.log(1,"CalenderUtils","CalenderUtils");
            Crashlytics.logException(e);

        }
        return days;

    }

    public static String getToday() {
        Date today = new Date();
        return DateFormat.format("dd/MM/yyyy", today).toString();
    }

    public static String get3monthsBack() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return DateFormat.format("dd/MM/yyyy", calendar).toString();
    }

    public static String getDayMonthWithSuffix(String strDate, String strFormate) {

        SimpleDateFormat sdf = new SimpleDateFormat(strFormate,Locale.ENGLISH);
        Date d = new Date();
        try {
            d = sdf.parse(strDate);
        } catch (Exception e) {

            Crashlytics.logException(e);
        }
        sdf.applyPattern("dd");
        int intDay = StringUtils.getInt(sdf.format(d));
        String suffix = "";
        if (intDay >= 11 && intDay <= 13) {
            suffix = "th";
        } else {
            int intCase = intDay % 10;
            switch (intCase) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
                default:
                    suffix = "th";
                    break;
            }
        }

        sdf.applyPattern("MMM");
        String StrMonth = sdf.format(d);

        return intDay + suffix + " " + StrMonth;

    }




    public static long getDateDifference(String currentDate, String finalDate) {
        return getDateDifference(currentDate, finalDate, "dd-MM-yyyy hh:mm:ss");
    }

    // 03-12-2016
    public static String getTimeDiffinMinute(String currentDate, String finalDate) {
        long mills, Hours = 0, Mins = 0, sec = 0;
        String strTime = "";

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date Date1 = format.parse(finalDate);
            Date Date2 = format.parse(currentDate);
            mills = Date1.getTime() - Date2.getTime();
            Hours = mills / (1000 * 60 * 60);
            Mins = (mills - (Hours * 1000 * 60 * 60)) / (1000 * 60);
            sec = (mills - (Hours * 1000 * 60 * 60) - (Mins * 1000 * 60)) / (1000);
        } catch (Exception e) {

            Crashlytics.logException(e);
        }

        if (Hours > 0) {
            strTime = (Hours >= 9 ? "" + Hours : "0" + Hours) + ":" + (Mins >= 9 ? "" + Mins : "0" + Mins) + ":" + (sec >= 9 ? "" + sec : "0" + sec);
        } else if (Mins > 0) {
            strTime = (Mins >= 9 ? "" + Mins : "0" + Mins) + ":" + (sec >= 9 ? "" + sec : "0" + sec);
        }
        return strTime;
    }

    public static String getCurrentDate(String formate) {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(formate,Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        System.out.println("Current time => " + c.getTime());
        return formattedDate;
    }

    public static String getCurrentDateWithZone() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("Z",Locale.ENGLISH);
        String gmt = sdf.format(date);
        SimpleDateFormat format = new SimpleDateFormat(DateFormateWithoutZone,Locale.ENGLISH);
        String formattedDate = format.format(date);

        return formattedDate+""+gmt;
    }

    public static String getLeaveFromDate(String date) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("Z");
        String gmt = sdf.format(today);
        return date+"T00:00:00" + gmt;
    }

    public static String getLeaveThruDate(String date) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("Z");
        String gmt = sdf.format(today);

        return date+"T23:59:59" + gmt;
    }

    public static String getTimeZoneDate(String formate, String time) {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("Z");
            String gmt = sdf.format(today);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CalenderUtils.DateFormate);
            Date date = null;
            try {
                date = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar mCalendar = Calendar.getInstance();
            if (date != null) {
                mCalendar.setTime(date);
            }
            return DateFormat.format(formate.replaceAll("Z$", gmt), mCalendar).toString();
    }

    public static String getDateMethod(String timeStamp, String formate){
        Date date;
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat(DateFormateWithZone,Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        try {
            if(!TextUtils.isEmpty(timeStamp)) {
                date = simpleDateFormat.parse(timeStamp);
                calendar.setTime(date);
            }
        } catch (ParseException e) {

            Crashlytics.logException(e);
        }
        return (String) DateFormat.format(formate,calendar);
    }

    public static String getDifferenceDate(String fromDate, String thruDate){
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat(YearMonthDashedFormate,Locale.ENGLISH);
        Date f = null,t=null;
        long difference = 0;
        Calendar c = Calendar.getInstance();
        try {
            f = simpleDateFormat.parse(fromDate);
            t = simpleDateFormat.parse(thruDate);
        } catch (ParseException e) {

            Crashlytics.logException(e);
        }
        if(f != null && t !=null) {
            difference = t.getTime() - f.getTime();
        }
        int days = (int) TimeUnit.MILLISECONDS.toDays(difference);
        if(days == 0) {
            return "1";
        } else if(days < 0) {
            return "-";
        } else {
            return (days+1) + "";
        }

    }

    public static String getYearMonthDashedFormate(String date){
        Date d;
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            d = simpleDateFormat.parse(date);
            calendar.setTime(d);
        } catch (ParseException e) {

            Crashlytics.logException(e);
        }
        return (String) DateFormat.format(YearMonthDashedFormate,calendar);
    }

    public static String get6MonthsBack(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-6);
        return DateFormat.format("dd/MM/yyyy",calendar).toString();
    }

    public static String get6MonthsForward(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,6);
        return DateFormat.format("dd/MM/yyyy",calendar).toString();
    }

    public static DatePickerDialog pickDateFromCalender(Context mContext, final TextView dobTextView,final TextView dayTV, String dateFormat) {
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance();

                calendar.set(year, monthOfYear, dayOfMonth);

                dayTV.setText(String.format("%1$tA", calendar));
                String formatedData = String.format("%1$td/%1$tm/%1$tY", calendar);
                dobTextView.setText(formatedData);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        return datePickerDialog;
    }
    public static DatePickerDialog pickYearMonthFromCalender(Context mContext, final TextView dobTextView,final TextView dayTV, String dateFormat) {
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar calendar = Calendar.getInstance();

                calendar.set(year, monthOfYear, dayOfMonth);

                dayTV.setText(String.format(Locale.US,"%tB",calendar));

                String formatedData = String.format("%1$tY", calendar);
                dobTextView.setText(formatedData);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        return datePickerDialog;
    }
}
