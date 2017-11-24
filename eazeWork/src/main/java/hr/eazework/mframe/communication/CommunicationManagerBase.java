package hr.eazework.mframe.communication;

import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

public abstract class CommunicationManagerBase {

	// abstract protected void sendRequest(Vector<Object> vReqParam, Object obj,
	// int reqType) throws Exception;

	abstract public void sendReponseToUIController(Object objListener,
			ResponseData responseXML);

	protected final void sendRequestToNativeCommunicationHandler(int reqApiId,
			String url, String xml, Hashtable<String, String> hdr,
			Object controllerListener, int httpMethod, boolean secureCon,
			int connectionTimeOut, ResponseDataAcceptType typeAccept)
			throws Exception {

		RequestData reqObj = new RequestData(url, xml, hdr, controllerListener,
				httpMethod, secureCon, reqApiId, typeAccept);
		NativeCommunicationHandler communicationHandler = new NativeCommunicationHandler(connectionTimeOut);
		execute(communicationHandler, reqObj);
	}

	@SuppressLint("NewApi")
	protected final void execute(NativeCommunicationHandler as,RequestData requestData) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1) {
			as.execute(requestData);
		} else {
			as.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,requestData);
		}
	}
}
