package hr.eazework.mframe.caching.sqlight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import hr.eazework.com.application.MyApplication;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.util.MLogger;

public class MySqliteHelper extends SQLiteOpenHelper {

	// public static variables for caching image table
	public static final String TABLE_NAME_CACHING = "RESPONSE_CACHING_TABLE";
	public static final String COLUMN_CACHING_API_ID = "apiCachingId";
	public static final String COLUMN_CACHING_ID = "cachingId";
	public static final String COLUMN_CACHING_DATA = "cachingData";
	public static final String COLUMN_CACHING_TIME = "cachingTime";
	public static final String COLUMN_CACHING_DURATION = "cachingDuration";
	public static final String COLUMN_CACHING_SPECIAL_COMMENT = "specialComment";

	private static final String DATABASE_NAME = "selfcare.db";
	private static final int DATABASE_VERSION = 2;

	// Database creation sql statement

	/* Caching table create sql statement */

	private static final String TABLE_CREATE_CACHING = "create table "
			+ TABLE_NAME_CACHING + "(" + COLUMN_CACHING_ID
			+ " integer primary key autoincrement, " + COLUMN_CACHING_API_ID
			+ " text, " + COLUMN_CACHING_DATA + " text, " + COLUMN_CACHING_TIME
			+ " text, " + COLUMN_CACHING_DURATION + " text, "
			+ COLUMN_CACHING_SPECIAL_COMMENT + " text );";

	private static MySqliteHelper INSTANCE = new MySqliteHelper(
			MyApplication.getAppContext());

	public static MySqliteHelper getInstance() {
		if (INSTANCE != null)
			INSTANCE = new MySqliteHelper(MyApplication.getAppContext());
		return INSTANCE;
	}

	private MySqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CREATE_CACHING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySqliteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		String msisdn = getCacheData(ModelManager.MSISDN, db);
//		String password = getCacheData(ModelManager.PASSWORD, db);
		this.msisdn = msisdn;
//		this.password = password;
		if (msisdn != null && password != null) {
			Encryption encryption;
			try {
				encryption = new Encryption(new byte[16]);
				byte[] apiData = Base64.decode(this.msisdn, Base64.NO_WRAP);
				
				msisdn = new String(encryption.encrypt(apiData));
				apiData = Base64.decode(this.password, Base64.NO_WRAP);
				
				password = new String(encryption.encrypt(apiData));
			} catch (Exception e) {
				MLogger.error(getClass().getName(), e.getMessage());
			}
//			this.msisdn = msisdn;
//			this.password = password;
		}
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CACHING);
		onCreate(db);
		insertCacheData(ModelManager.MSISDN, this.msisdn,
				System.currentTimeMillis() + "", "9223372036854775807", null,
				db);
//		insertCacheData(ModelManager.PASSWORD, this.password,
//				System.currentTimeMillis() + "", "9223372036854775807", null,
//				db);
	}

	String msisdn;
	String password;

	public String getCacheData(int apiId, SQLiteDatabase database) {
		String data = null;
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_CACHING, null,
				MySqliteHelper.COLUMN_CACHING_API_ID + "=" + "'" + apiId + "'",
				null, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Long cachedTime = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(MySqliteHelper.COLUMN_CACHING_TIME)));
			Long cachedDuration = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DURATION)));
			Long currentTime = System.currentTimeMillis();// / 1000;

			if ((currentTime > cachedTime)
					&& (currentTime - cachedTime) < cachedDuration) {
				data = cursor.getString(cursor
						.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DATA));
			}
		}
		Encryption encryption;
		try {
			encryption = new Encryption(new byte[16]);
			byte[] apiData = Base64.decode(data, Base64.NO_WRAP);
			data = new String(encryption.decrypt(apiData));
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
		cursor.close();
		return data;
	}

	public void insertCacheData(int rowId, String data, String currentTime,
			String duration, String specialComment, SQLiteDatabase database) {
		ContentValues values = new ContentValues();
		if (rowId > 0) {
			values.put(MySqliteHelper.COLUMN_CACHING_API_ID, "" + rowId);
			values.put(MySqliteHelper.COLUMN_CACHING_DATA, data);
			values.put(MySqliteHelper.COLUMN_CACHING_TIME, currentTime);
			values.put(MySqliteHelper.COLUMN_CACHING_DURATION, duration);
			if (specialComment != null) {
				values.put(MySqliteHelper.COLUMN_CACHING_SPECIAL_COMMENT,
						specialComment);
			}
		}
		if (rowId > 0) {
			if (specialComment != null) {
				int rowDeleted = database.delete(
						MySqliteHelper.TABLE_NAME_CACHING,
						MySqliteHelper.COLUMN_CACHING_SPECIAL_COMMENT + " = '"
								+ specialComment + "'" + " AND "
								+ MySqliteHelper.COLUMN_CACHING_API_ID + "="
								+ "'" + rowId + "'", null);
				MLogger.debug(getClass().getName(), "row deleted : "
						+ rowDeleted);
			} else {
				database.delete(MySqliteHelper.TABLE_NAME_CACHING,
						MySqliteHelper.COLUMN_CACHING_API_ID + "=" + "'"
								+ rowId + "'", null);
			}
		}
		long insertId = database.insert(MySqliteHelper.TABLE_NAME_CACHING,
				null, values);
	}
}
