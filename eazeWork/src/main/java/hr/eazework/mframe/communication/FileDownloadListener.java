package hr.eazework.mframe.communication;

import android.net.Uri;

public interface FileDownloadListener {
	void onFileDownloadComplete(boolean status,Uri location);
}
