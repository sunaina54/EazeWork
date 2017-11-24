package hr.eazework.mframe.caching.manager;

import java.io.ByteArrayOutputStream;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import hr.eazework.com.application.MyApplication;
import hr.eazework.com.ui.util.MLogger;
import hr.eazework.mframe.caching.sqlight.CachDataSource;
import hr.eazework.mframe.caching.sqlight.Encryption;
import hr.eazework.mframe.caching.sqlight.ImageStoreHelper;
import hr.eazework.security.EncryptionDecryption;
import hr.eazework.security.SecurityAPIExcption;


//import hr.eazework.selfcare.activity.feature.ApplicationManager;
//import hr.eazework.selfcare.sqlight.CachDataSource;

public class DataCacheManager {

	private static DataCacheManager Instance = new DataCacheManager();

	CachDataSource dataSource = CachDataSource.getInstance();

	public static DataCacheManager getInstance() {
		if (Instance != null)
			Instance = new DataCacheManager();
		return Instance;
	}

	private DataCacheManager() {
	}

	@SuppressWarnings("finally")
	public boolean setDataCaching(int apiValue, String apiData, long duration,
			String msisdn) {
		boolean retVal = false;
		try {
			// Line added to enable encryption for all the requests @manas
			// 07-10-2014
			apiData = checkForEncryption(apiValue, apiData);
			// end

			if (apiValue <= 0)
				retVal = false;
			else {
				// MLogger.debug("setDataCaching","");
				Long currentTimeLong = System.currentTimeMillis();// / 1000;
				// Long durationLong = Long.parseLong(duration);
				// durationLong *= 60;
				String durationStr = "0";
				if (duration > 0) {
					durationStr = "" + duration;
					String currentTimeString = currentTimeLong.toString();
					dataSource.open(MyApplication.getAppContext());
					dataSource.insertCacheData(apiValue, apiData,
							currentTimeString, durationStr, msisdn);
					dataSource.close();
					retVal = true;
				}
			}
		} catch (Exception e) {
			retVal = false;
		} finally {
			return retVal;
		}
	}

	public boolean setDataCachingNoExpiry(int apiValue, String apiData) {
		boolean retVal = false;
		try {

			// if (apiValue != ModelManager.DB_KEY_REG_PARTIAL_DONE
			// && apiValue != ModelManager.DB_KEY_MSISDN) {
			apiData = checkForEncryption(apiValue, apiData);
			// }
			if (apiValue <= 0)
				retVal = false;
			else {
				Long currentTimeLong = System.currentTimeMillis();// / 1000;
				String currentTimeString = currentTimeLong.toString();
				String expDuration = "" + Long.parseLong("9223372036854775807");
				dataSource.open(MyApplication.getAppContext());
				dataSource.insertCacheData(apiValue, apiData,
						currentTimeString, expDuration, null);
				dataSource.close();
				retVal = true;
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	public boolean setDataCachingNoExpiry(int apiValue, String apiData,
			String msisdn) {
		boolean retVal = false;
		try {
			// if (apiValue != ModelManager.DB_KEY_REG_PARTIAL_DONE) {
			apiData = checkForEncryption(apiValue, apiData);
			// }
			if (apiValue <= 0)
				retVal = false;
			else {
				Long currentTimeLong = System.currentTimeMillis();// / 1000;
				String currentTimeString = currentTimeLong.toString();
				String expDuration = "" + Long.parseLong("9223372036854775807");
				dataSource.open(MyApplication.getAppContext());
				dataSource.insertCacheData(apiValue, apiData,
						currentTimeString, expDuration, msisdn);
				dataSource.close();
				retVal = true;
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}

	private String checkForEncryption(int apiValue, String apiData) {
		try {
			if (apiData == null)
				return null;
			{
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(MyApplication
								.getAppContext());
				String updated = preferences.getString("updated", "");
				if (updated != null) {
					if (updated.equalsIgnoreCase("TRUE")) {
						try {
							apiData = EncryptionDecryption.getEncryptData(
									"DATA_SEND", apiData);
							MLogger.debug(getClass().getName(),
									"encrpyted data is: " + apiData);

						} catch (hr.eazework.security.SecurityAPIExcption e) {
							MLogger.debug(getClass().getName(), e.getMessage());
						}
					} else {
						try {
							apiData = EncryptionDecryption.getEncryptData(
									"DATA_SEND", apiData);
							MLogger.debug(getClass().getName(),
									"encrpyted data is: " + apiData);
							// Editor editor = preferences.edit();
							// editor.putString("updated", "TRUE");
							// editor.commit();

						} catch (hr.eazework.security.SecurityAPIExcption e) {
							MLogger.debug(getClass().getName(), e.getMessage());
						}
					}
				}
			}
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
		return apiData;
	}

	private String checkForDecryption(int apiValue, String apiData) {
		try {
			if (apiData == null)
				return null;
			{
				String unChangedData = apiData;

				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(MyApplication
								.getAppContext());
				String updated = preferences.getString("updated", "");
				if (updated != null) {
					if (updated.equalsIgnoreCase("TRUE")) {
						try {
							apiData = EncryptionDecryption.getDecryptData(
									"DATA_SEND", apiData);

							MLogger.debug(getClass().getName(),
									"decrpyted data is: " + apiData);

						} catch (SecurityAPIExcption e) {
							e.printStackTrace();
							MLogger.error(getClass().getName(), e.getMessage());
						}
					} else {
						try {

							Encryption encryption = new Encryption(new byte[16]);
							byte[] data = Base64
									.decode(apiData, Base64.NO_WRAP);
							apiData = new String(encryption.decrypt(data));
						} catch (Exception e) {
							// TODO: Remove after release
							// apiData = unChangedData;
						}
					}
				}
			}
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
		return apiData;
	}

	public final String getDataCaching(int apiValue) {
		dataSource.open(MyApplication.getAppContext());

		String apiData = dataSource.getCacheData(apiValue);
		dataSource.close();

//		if (apiData != null && !(apiData.equals(""))) {
			apiData = checkForDecryption(apiValue, apiData);
//		}

		return apiData;
	}

	public final String getDataCaching(int apiValue, String msisdn) {
		dataSource.open(MyApplication.getAppContext());
		String apiData = null;
		if (msisdn == null) {
			apiData = dataSource.getCacheData(apiValue);
		} else {
			apiData = dataSource.getCacheData(apiValue, msisdn);
		}
//		if (apiData != null && !(apiData.equals(""))) {
			apiData = checkForDecryption(apiValue, apiData);
//		}
		dataSource.close();
		return apiData;
	}

	public final void clearCache() {
		dataSource.open(MyApplication.getAppContext());
		dataSource.removeCacheData();
		dataSource.close();
	}

	public final boolean removeCacheDataByID(int apiValue) {
		dataSource.open(MyApplication.getAppContext());
		boolean retVal = dataSource.removeCacheDataByApiId(apiValue);
		dataSource.close();
		return retVal;
	}

	public final boolean removeCacheDataByID(int apiValue, String msisdn) {
		dataSource.open(MyApplication.getAppContext());
		boolean retVal = dataSource.removeCacheDataByApiId(apiValue, msisdn);
		dataSource.close();
		return retVal;
	}

	public final boolean removeCacheDataByMSISDN(String msisdn) {
		dataSource.open(MyApplication.getAppContext());
		boolean retVal = dataSource.removeCacheDataByApiMSISDN(msisdn);
		dataSource.close();
		return retVal;
	}

	public void saveImage(String imageId, Bitmap imageData) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream(
					imageData.getWidth() * imageData.getHeight());
			imageData.compress(CompressFormat.PNG, 100, buffer);
			byte[] img = buffer.toByteArray();
			ImageStoreHelper.getInstance().addImage(imageId, img);
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
	}

	public Bitmap getImage(String imageId) {
		Bitmap bitmap = null;
		try {
			byte[] img = ImageStoreHelper.getInstance().getImage(imageId);
			Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
			bitmap = bmp;
		} catch (Exception e) {
			MLogger.error(getClass().getName(), e.getMessage());
		}
		return bitmap;
	}

}
