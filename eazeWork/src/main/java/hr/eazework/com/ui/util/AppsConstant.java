package hr.eazework.com.ui.util;

import android.graphics.Color;
import android.graphics.Typeface;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 7/17/2016.
 * */
public class AppsConstant {


    public static String RESUBMIT="Resubmit",RETURN="Return",
            DELETE="Delete",SAVE_DRAFT="Save",SUBMIT="Submit",
            APPROVE="Approve",REJECT="Reject",WITHDRAW="Withdraw",DRAFT="Draft",PRESENT="Present",ABSENT="Absent",LEAVE_EDIT="3",
            LEAVE_WITHDRAWAL="5",TOUR_EDIT="11",TOUR_WITHDRAWAL="12",VIEW_ACTION="V",EDIT_ACTION="E"
            ,OD_EDIT="32",OD_WITHDRAWAL="33",WFH_EDIT="13",WFH_WITHDRAWAL="14";
    public  static  final String SUCCESS="0";

    public static final int ADD_EXPENSE_CLAIM_FRAGMENT =1;
    public static final int VIEW_EDIT_EXPENSE_CLAIM_FRAGMENT =2;
    public static final int APPROVE_EDIT_EXPENSE_CLAIM_FRAGMENT =3;

    public static final int PRIORITY_MEDIUM = 2;
    public static final String GEOFENCE_ACTION = "hr.eazework.com.ACTION_GEOFENCE_RECEIVER";
    public static final String ISFROMSPLASH = "isFromSplash";
    public static final String ISFROMLOGIN = "isFromLogin";
    public static final String FCM_TOKEN = "fcmToken";
    public static String OFFICE_ID = "OfficeID";
    public static boolean isProduction=false;
    public static boolean isDebug=false;
    public static final int FRONT_CAMREA_OPEN = 1;
    public static final int BACK_CAMREA_OPEN = 2;
    public static String POSITION_KEY = "positionKey";
    public static String HISTORY_KEY = "historyKey";
    public static String EMP_ID = "EmpID";
    public static String ATTEND_ID = "attendID";
    public static String OPTION_SELECTED = "optionSelected";
    public static String MARK_DATE = "markDate";
    public static String SELFIE = "selfieYN";
    public static String GEOFENCE = "geofenceYN";
    public static String DATE_KEY = "DATE_KEY";
    public static int DEVICE_DISPLAY_HEIGHT_DEFAULT = 800;
    public static int DEVICE_DISPLAY_WIDTH_DEFAULT = 400;
    public static String DEFAULTRADIUS = "0";
    public static String DELETE_FLAG="D";
    public static String OLD_FLAG="O";
    public static String NEW_FLAG="N";
    public static Typeface RobotoSlabRegular;
    public static String URL ="URL";
    public static String USER ="USER";
    public static String PASSWORD ="PASSWORD";
    public static String FILE ="FILE";
    public static String FILEPURPOSE ="FILEPURPOSE";
    public static String METHOD ="METHOD";
    public static String REASON ="REASON";
    public static String PARAMS ="PARAMS";
    public static String GET ="GET";
    public static String GEOEVENT = "GeoEvent";
    public static String ORGANIZATIONID ="ORG_OPPO";
    public static String PUT = "PUT";
    public static String POST ="POST";
    public static final int IMAGE_PHOTO = 2;
    public static final int IMAGE_AADHAR = 3;
    public static final int IMAGE_ADDRESS_PROOF = 4;
    public static final int IMAGE_STORE = 5;
    public static int RunningLoaderCount =0;
    public static String TAG = "lstech.aos.debug";
    public static String LOCATION_UPDATE = "LOCATION_UPDATE";
    public static final List<String> offlineAttendanceStatuses = new ArrayList<>();
    public final static int REQ_CAMERA = 1003;

    public static String DATE_FORMATE="dd/MM/yyyy";
    public static String DATE_YEAR_MONTH_FORMAT="MMM yyyy";
    public static int PERIODIC_EXPENSE=4;
    public static int IMAGE_QUALITY=100;
    public static String PENDING = "P";
    public static String APPROVED = "A";

    public static String YES = "Y";
    public static String NO = "N";

    public static String CLASSICAL_TOUR ="C017000024";
    public static String ADVENTURE="C017000025";
    public static String FAMILY_PACKAGE="C017000026";
    public static String STUDENT_SPECIAL="C017000027";
    public static String RELIGIOUS_TRAVEL="C017000029" ;
    public static String PHOTOGRAPHY="C017000030";

    public static String OD="OD";
    public static String WFH="Work from home" ;
    public static String TOUR="Tour";
    public static String TIME_MODIFICATION="Time Modification";
    public static String BACK_DATED_ATTENDANCE="Attendance";
    public static String SAVE_AS_DRAFT="Save";

    public static String PENDING_APPROVAL="PendingActivityFragment";


    public final static int GEOFENCE_STROKE_COLOR = Color.argb(50, 0, 127, 255);
    public final static int GEOFENCE_FILL_COLOR = Color.argb(100, 137, 207, 240);
    public final static String ADVANCE="AdvanceHome";
    public  final static String EXOENSE="ExpenseHome";
    static{
        offlineAttendanceStatuses.add("InLocation");
        offlineAttendanceStatuses.add("OutLocation");
    }
    public static  String EDIT="EDIT",ADD="ADD",VIEW="VIEW";

}
