package hr.eazework.mframe.communication;

public class ResponseDataAcceptType {
	int type = -1;
	String acceptTypeToken = "";

	private ResponseDataAcceptType(){}
	private ResponseDataAcceptType(int typeAccept, String acceptString){
		type = typeAccept;
		acceptTypeToken = acceptString;
	}
	public final static ResponseDataAcceptType ACCEPT_TYPE_JSON = new ResponseDataAcceptType(1, "application/json");
	public final static ResponseDataAcceptType ACCEPT_TYPE_XML = new ResponseDataAcceptType(2, "application/xml");
	

	public int getType() {
		return type;
	}
	public String getAcceptTypeToken() {
		return acceptTypeToken;
	}
}
