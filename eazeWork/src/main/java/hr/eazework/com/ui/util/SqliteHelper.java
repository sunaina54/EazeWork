package hr.eazework.com.ui.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "attendancetracker.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_TIMEINOUT = "tblTimeInOut";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_CLOCKDATE = "clockDate";
    public static final String COLUMN_ACTIONTYPE = "actionType";
    public static final String COLUMN_COMMENTS = "comments";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ACTIONIMAGE = "actionImage";
    public static final String COLUMN_ISPHUSHED = "isPushed";

    protected SQLiteDatabase database;
    protected Context context;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        this.database = database;
        createEventsTable();
    }

    protected void createEventsTable() {
        String sql = "CREATE TABLE '" + SqliteHelper.TABLE_TIMEINOUT
                + "' ( '_id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'"+COLUMN_CLOCKDATE+"' DATETIME," + "'"+COLUMN_USERNAME+"' VARCHAR ( 100 ),'"+COLUMN_ACTIONTYPE+"' VARCHAR ( 100 ),"
                + "'"+COLUMN_COMMENTS+"' VARCHAR ( 100 ), '"+COLUMN_LATITUDE+"' VARCHAR ( 100 )," + "'"+COLUMN_LONGITUDE+"' VARCHAR ( 100 ),"
                + "'"+COLUMN_ACTIONIMAGE+"' VARCHAR ( 100 ), '"+COLUMN_ISPHUSHED+"' VARCHAR ( 100 ))";
        database.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMEINOUT);
        onCreate(db);
    }
}