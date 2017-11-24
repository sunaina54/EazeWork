package hr.eazework.com.ui.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import hr.eazework.com.MainActivity;
import hr.eazework.com.model.TimeInOutDetails;

public class EventDataSource {
    private String[] allColumns = { SqliteHelper.COLUMN_ID,
            SqliteHelper.COLUMN_USERNAME, SqliteHelper.COLUMN_CLOCKDATE,
            SqliteHelper.COLUMN_ACTIONTYPE, SqliteHelper.COLUMN_COMMENTS,
            SqliteHelper.COLUMN_LATITUDE, SqliteHelper.COLUMN_LONGITUDE,
            SqliteHelper.COLUMN_ACTIONIMAGE,SqliteHelper.COLUMN_ISPHUSHED, };
    private Context context;
    protected SQLiteDatabase database;
    protected SqliteHelper dbHelper;

    public EventDataSource(Context context) {
        this.context = context;
        initSqliteHelper();
        open();
    }

    protected void initSqliteHelper() {
        dbHelper = new SqliteHelper(context);
    }

    protected void open() throws SQLException {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteCantOpenDatabaseException e) {
            Crashlytics.logException(e);
        }
    }

    public void close() {
        database.close();
        dbHelper.close();
    }
    public boolean isOpen() {
        return database.isOpen();
    }

    public void insertTimeInOutDetails(TimeInOutDetails data) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COLUMN_USERNAME, data.getmUsername());
        values.put(SqliteHelper.COLUMN_CLOCKDATE, data.getmClockDate());
        values.put(SqliteHelper.COLUMN_ACTIONTYPE, data.getmActionType());
        values.put(SqliteHelper.COLUMN_COMMENTS, data.getmComments());
        values.put(SqliteHelper.COLUMN_LATITUDE, data.getmLatitude());
        values.put(SqliteHelper.COLUMN_LONGITUDE, data.getmLongitude());
        values.put(SqliteHelper.COLUMN_ACTIONIMAGE, data.getmActionImage());
        values.put(SqliteHelper.COLUMN_ISPHUSHED, data.getIsPushed());
        database.insert(SqliteHelper.TABLE_TIMEINOUT, null, values);
    }


    public ArrayList<TimeInOutDetails> getTimeInOutDetails(String username) {
        ArrayList<TimeInOutDetails> list = new ArrayList<TimeInOutDetails>();
        String orderBy = SqliteHelper.COLUMN_CLOCKDATE + " ASC";

        String where = SqliteHelper.COLUMN_ISPHUSHED + "='false'" + " AND " + SqliteHelper.COLUMN_USERNAME + " = '" + username + "'" ;
        Cursor cursor = database.query(SqliteHelper.TABLE_TIMEINOUT, allColumns,
                where, null, null, null, orderBy);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(TimeInOutDetails.fromCursor(cursor));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return list;
    }
    public void updateIsPushed(TimeInOutDetails d){
        String where = SqliteHelper.COLUMN_ID + " =" + d.getId();
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COLUMN_ISPHUSHED,"true");
        database.update(SqliteHelper.TABLE_TIMEINOUT,values,where,null);
    }
    public TimeInOutDetails getLastEntry(){

        String where = SqliteHelper.COLUMN_ID +" = (select max(" + SqliteHelper.COLUMN_ID + ") from " + SqliteHelper.TABLE_TIMEINOUT + ");" ;
        TimeInOutDetails details = null;
        try {
            Cursor cursor = database.query(SqliteHelper.TABLE_TIMEINOUT, allColumns, where, null, null, null, null);
            cursor.moveToFirst();
            details = TimeInOutDetails.fromCursor(cursor);
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public String getTableAsString() {
        Log.d(MainActivity.TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n",SqliteHelper.TABLE_TIMEINOUT);
        Cursor allRows  = database.rawQuery("SELECT * FROM " + SqliteHelper.TABLE_TIMEINOUT, null);
        if (allRows != null && allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

    public void clearTimeOutEntry(String username) {
        String where = SqliteHelper.COLUMN_USERNAME +" = '"+username + "' AND " +SqliteHelper.COLUMN_COMMENTS + " = " + "'TimeOut'";
        String order = "datetime(" + SqliteHelper.COLUMN_CLOCKDATE +  ") DESC";
        Cursor cursor = database.query(SqliteHelper.TABLE_TIMEINOUT,allColumns,where,null,null,null,order);
        String id = null;
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getString(0);
            String where2 = SqliteHelper.COLUMN_ID + " = " + id + "";
            database.delete(SqliteHelper.TABLE_TIMEINOUT,where2,null);
        }
        cursor.close();
    }

    public boolean isAllDataPushed(String username) {
        String where = SqliteHelper.COLUMN_ISPHUSHED + "='false'" + " AND " + SqliteHelper.COLUMN_USERNAME + " = '" + username + "'" ;

        Cursor cursor = database.query(SqliteHelper.TABLE_TIMEINOUT,allColumns,where,null,null,null,null);
        if(cursor.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void clearDatabase() {
        database.delete(SqliteHelper.TABLE_TIMEINOUT,null,null);
    }
}
