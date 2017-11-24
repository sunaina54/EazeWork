package hr.eazework.com.ui.util;

import android.text.Html;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Aritra on 4/26/2016.
 */
public class StringUtils {
    public static int getInt(String integer)
    {
        int reqInteger = 0;
        try {

            if (!TextUtils.isEmpty(integer) && !integer.equalsIgnoreCase("null"))
                reqInteger = Integer.parseInt(integer);
        }
        catch ( Exception e)
        {
            Crashlytics.log(1,"StringUtils","StringUtils");
            Crashlytics.logException(e);

        }
        finally {
            return reqInteger;
        }
    }

    public static long getLong(String integer) {
        long reqInteger = 0;

        if( TextUtils.isEmpty(integer)|| integer.equalsIgnoreCase("null"))
            return reqInteger;
        try {
            reqInteger = Long.parseLong(integer);
        }
        catch (Exception e)
        {

            Crashlytics.log(1,"StringUtils","StringUtils");
            Crashlytics.logException(e);
        }



        return reqInteger;
    }

    public static float getFloat(String integer) {
        float reqInteger = 0;

        if(integer == null || TextUtils.isEmpty(integer))
            return reqInteger;

        reqInteger = Float.parseFloat(integer);

        return reqInteger;
    }
    public static double getDouble(String integer) {
        double reqInteger = 0;

        if(integer == null || TextUtils.isEmpty(integer))
            return reqInteger;
        try {
            reqInteger = Double.parseDouble(integer);
        } catch (NumberFormatException e) {
            Crashlytics.logException(e);
            reqInteger = 0;
        }
        return reqInteger;
    }

    public static boolean isNotEmptyAndNULLString(String string){
        return (!string.equalsIgnoreCase("") && !string.equalsIgnoreCase("null"));
    }
}
