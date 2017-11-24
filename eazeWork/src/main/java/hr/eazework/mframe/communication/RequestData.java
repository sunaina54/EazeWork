package hr.eazework.mframe.communication;

import java.util.Hashtable;

public class RequestData  {
	private Object act;
	private String requestUrl;
	private String requestXML;
	private Hashtable<String, String> headerList;
	private int reqType = 1; // 0 FOR GET , 1 FOR POST
	private boolean secureConnection = false;
	private int reqApiId = -1;
	private ResponseDataAcceptType acceptType = null;

	public RequestData(String url, String xml, Hashtable<String, String> hdr,
			Object actUI, int conReqType, boolean secureCon, int reqAPIID,
			ResponseDataAcceptType typeAccept) {
		setAct(actUI);
		setRequestUrl(url);
		setRequestXML(xml);
		setHeaderList(hdr);
		setReqType(conReqType);
		secureConnection = secureCon;
		reqApiId = reqAPIID;
		acceptType = typeAccept;
	}

	ResponseDataAcceptType getAcceptType() {
		return acceptType;
	}

	public int getReqApiId() {
		return reqApiId;
	}

	Object getAct() {
		return act;
	}

	void setAct(Object act) {
		this.act = act;
	}

	String getRequestUrl() {
		return requestUrl;
	}

	void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	String getRequestXML() {
		return requestXML;
	}

	void setRequestXML(String requestXML) {
		this.requestXML = requestXML;
	}

	Hashtable<String, String> getHeaderList() {
		return headerList;
	}

	void setHeaderList(Hashtable<String, String> headerList) {
		this.headerList = headerList;
	}

	public int getReqType() {
		return reqType;
	}

	private void setReqType(int reqType) {
		this.reqType = reqType;
	}

	public boolean getSecureConnection() {
		return secureConnection;
	}
}
