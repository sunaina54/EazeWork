package hr.eazework.com.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import hr.eazework.com.model.CheckInOutModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TeamMember;

/**
 * Created by AllSmart-LT008 on 10/22/2016.
 */

@SuppressLint("AppCompatCustomView")
public class ShiftTimeView extends TextView {

    Calendar mCalendar;
    private final static String m12 = "HH:mm:ss";
    private final static String m24 = "k:mm";
    private FormatChangeObserver mFormatChangeObserver;
    private Runnable mTicker;
    private Handler mHandler;
    private boolean mTickerStopped = false;

    public final String TAG = this.getClass().getName();

    String mFormat;

    public ShiftTimeView(Context context) {
        super(context);
        initClock(context);
    }

    public ShiftTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        Resources r = context.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped) return;

                CheckInOutModel model = ModelManager.getInstance().getCheckInOutModel();
                if (model != null) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa", Locale.ENGLISH);
                    String timeInTimeZone = model.getCurrentDateTimeWithTimeZone();
                    String timeIn = model.getInDateTime();
                    String timeOut = model.getOutDateTime();
                    long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
                    long elapsedMillis = (currentTimeInMillis - model.getLastFetchedDate().getTime());

                    Date start = null;
                    Date end = null;
                    long timeDiffInMillis = 0;
                    try {
                        Log.d(TAG, timeIn);
                        start = format.parse(timeIn);
                        String endTimeStr = timeInTimeZone;
                        if (!TextUtils.isEmpty(timeOut) && !"null".equals(timeOut)) {
                            endTimeStr = timeOut;
                        }
                        end = format.parse(endTimeStr);
                        timeDiffInMillis = ((end.getTime() + elapsedMillis) - start.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }
                    setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeDiffInMillis),
                            TimeUnit.MILLISECONDS.toMinutes(timeDiffInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiffInMillis)),
                            TimeUnit.MILLISECONDS.toSeconds(timeDiffInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiffInMillis))));


                    invalidate();
                    long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);
                    mHandler.postAtTime(mTicker, next);

                }
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return false;// return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

}
