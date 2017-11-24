package hr.eazework.mframe.caching.sqlight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import hr.eazework.com.ui.util.MLogger;


public class CachDataSource {
	private SQLiteDatabase database;
	private MySqliteHelper dbHelper;

	private static CachDataSource INSTANCE = new CachDataSource();

	public static CachDataSource getInstance() {
		if (INSTANCE != null)
			INSTANCE = new CachDataSource();
		return INSTANCE;
	}

	private CachDataSource() {
		dbHelper = MySqliteHelper.getInstance();
	}

	public void open(Context c) throws SQLException {
		try {
			if (database == null)
				database = dbHelper.getWritableDatabase();
			else if (!database.isOpen()) {
				database = dbHelper.getWritableDatabase();
			}

		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
	}

	public void close() {
		// MLogger.debug("CachedDataSource", "Closed");
		dbHelper.close();
		database.close();
	}

	public void insertCacheData(int rowId, String data, String currentTime,
			String duration, String specialComment) {
		ContentValues values = new ContentValues();
		if (rowId > 0) {
			values.put(MySqliteHelper.COLUMN_CACHING_API_ID, ""+rowId);
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
								+  "'"+rowId +"'", null);
				MLogger.debug(getClass().getName(),"row deleted : "+rowDeleted);
			} else {
				database.delete(MySqliteHelper.TABLE_NAME_CACHING,
						MySqliteHelper.COLUMN_CACHING_API_ID + "=" + "'"+rowId +"'",
						null);
			}
		}
		long insertId = database.insert(MySqliteHelper.TABLE_NAME_CACHING,
				null, values);
	}

	public void removeCacheData() {
		database.delete(MySqliteHelper.TABLE_NAME_CACHING, null, null);
	}

	public boolean removeCacheDataByApiId(int apiId) {
		int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_CACHING,
				MySqliteHelper.COLUMN_CACHING_API_ID + "=" + "'"+apiId +"'", null);
		if (deletedRows > 0)
			return true;
		else
			return false;
	}

	public boolean removeCacheDataByApiId(int apiId, String msisdn) {
		int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_CACHING,
				MySqliteHelper.COLUMN_CACHING_API_ID + "=" + "'"+apiId +"'" + " AND "
						+ MySqliteHelper.COLUMN_CACHING_SPECIAL_COMMENT + " = "
						+"'"+ msisdn+"'", null);
		if (deletedRows > 0)
			return true;
		else
			return false;
	}

	public boolean removeCacheDataByApiMSISDN(String msisdn) {
		int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_CACHING,
				MySqliteHelper.COLUMN_CACHING_SPECIAL_COMMENT + " = " +"'"+ msisdn+"'",
				null);
		if (deletedRows > 0)
			return true;
		else
			return false;
	}

	public String getCacheData(int apiId) {
		String data = null;
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_CACHING, null,
				MySqliteHelper.COLUMN_CACHING_API_ID + "=" + "'"+apiId +"'", null, null,
				null, null);

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
			} else {
				removeCacheDataByApiId(apiId);
			}
		}

		cursor.close();
		return data;
	}

	public String getCacheData(int apiId, String specialComment) {
		String data = null;
		Cursor cursor = database
				.query(MySqliteHelper.TABLE_NAME_CACHING, null,
						MySqliteHelper.COLUMN_CACHING_API_ID + " = '" + apiId+ "'"
								+ " AND "
								+ MySqliteHelper.COLUMN_CACHING_SPECIAL_COMMENT
								+ " = '" + specialComment + "'", null, null,
						null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			Long cachedTime = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(MySqliteHelper.COLUMN_CACHING_TIME)));
			Long cachedDuration = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DURATION)));
			Long currentTime = System.currentTimeMillis();// // 1000;

			if ((currentTime > cachedTime)
					&& (currentTime - cachedTime) < cachedDuration) {
				data = cursor.getString(cursor
						.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DATA));
			} else {
				removeCacheDataByApiId(apiId, specialComment);
			}
		}

		cursor.close();
		return data;
	}

	public String getCacheDataBySpecialComment(String specialComment) {
		String data = null;
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_CACHING, null,
				MySqliteHelper.COLUMN_CACHING_SPECIAL_COMMENT + " = '"
						+ specialComment + "'", null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String apiId = cursor.getString(cursor
					.getColumnIndex(MySqliteHelper.COLUMN_CACHING_API_ID));
			
			Long cachedTime = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(MySqliteHelper.COLUMN_CACHING_TIME)));
			Long cachedDuration = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DURATION)));
			Long currentTime = System.currentTimeMillis();
			if ((currentTime > cachedTime)
					&& (currentTime - cachedTime) < cachedDuration) {
				data = cursor.getString(cursor
						.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DATA));
			} else {
				removeCacheDataByApiId(Integer.parseInt(apiId));
			}
		}
		cursor.close();
		return data;
	}
}
