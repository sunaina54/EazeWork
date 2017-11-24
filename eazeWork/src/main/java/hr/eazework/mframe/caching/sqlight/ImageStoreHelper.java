package hr.eazework.mframe.caching.sqlight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;

import hr.eazework.com.application.MyApplication;
import hr.eazework.com.ui.util.MLogger;

public class ImageStoreHelper {

	private static final String DB_TABLE = "appimages";
	private static final String KEY_NAME = "iconid";
	private static final String KEY_IMAGE = "icondata";
	private static final String TABLE_CREATE_CACHING = "CREATE TABLE "
			+ DB_TABLE + "(" + KEY_NAME + " TEXT, " + KEY_IMAGE + " BLOB);";

	public ImageStoreHelper(Context context, String name,
			CursorFactory factory, int version) {
	}

	private SQLiteDatabase db;

	private static ImageStoreHelper INSTANCE;

	public static ImageStoreHelper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ImageStoreHelper(MyApplication.getAppContext());
		}
		return INSTANCE;
	}

	private ImageStoreHelper(Context context) {
		db = MySqliteHelper.getInstance().getWritableDatabase();
		try {
			db.execSQL(TABLE_CREATE_CACHING);
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
	}

	public void addImage(String name, byte[] image) throws SQLiteException {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_IMAGE, image);
		try{
		db.delete(DB_TABLE, KEY_NAME + "=" + "'" + name + "'", null);
		}catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
		long id = db.insert(DB_TABLE, null, cv);

		MLogger.debug(getClass().getName(),"Inserted row id" + id);
	}

	public byte[] getImage(String name) throws SQLiteException {
		byte[] data = null;
		Cursor cursor = db.query(DB_TABLE, null, KEY_NAME + "=" + "'" + name
				+ "'", null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			data = cursor.getBlob(1);
		}
		cursor.close();
		return data;

	}
}
