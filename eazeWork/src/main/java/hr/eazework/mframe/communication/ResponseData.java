package hr.eazework.mframe.communication;

public class ResponseData {
	private boolean isSuccess;
	private String responceData;
	private RequestData requestData;
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getResponseData() {
		return responceData;
	}
	public void setResponceData(String responceData) {
		this.responceData = responceData;
	}
	public RequestData getRequestData() {
		return requestData;
	}
	public void setRequestData(RequestData requestData) {
		this.requestData = requestData;
	}
	
}
