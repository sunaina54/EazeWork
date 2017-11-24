package hr.eazework.mframe.communication;

public class AppResponseXML {
	// ///APP LEVEL ERROR CODES
	public final int APP_MSG_SERVER_CON_NOT_FOUND = 404;
	public final int APP_MSG_SERVER_CON_FAIL = -111;
	public final int APP_MSG_COMMUNICATION_ERROR = -112; // if 500 received from
															// server
	public final int APP_MSG_NO_CONNECTION = -115;

	public String getConnectionErrorXML(ResponseDataAcceptType acceptType) {
		if (acceptType.getType() == ResponseDataAcceptType.ACCEPT_TYPE_XML.type) {
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><mAppData><businessOutput><opStatus>"
					+ APP_MSG_SERVER_CON_FAIL
					+ "</opStatus><errorMessage>unable to process your request. please try again</errorMessage></businessOutput></mAppData>";
		} else {
			return "{  \"mAppData\": {    \"businessOutput\": {      \"opStatus\": \"   "
					+ APP_MSG_SERVER_CON_FAIL
					+ " \",      \"errorMessage\": \"unable to process your request. please try again\"    }  }}";
		}

	}

	public String getOfflineConnectionErrorXML(ResponseDataAcceptType acceptType) {
		if (acceptType.getType() == ResponseDataAcceptType.ACCEPT_TYPE_XML.type) {
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><mAppData><businessOutput><opStatus>"
					+ APP_MSG_NO_CONNECTION
					+ "</opStatus><errorMessage>No internet connection available</errorMessage></businessOutput></mAppData>";
		} else {
			return "{  \"mAppData\": {    \"businessOutput\": {      \"opStatus\": \"   "
					+ APP_MSG_NO_CONNECTION
					+ " \",      \"errorMessage\": \"No internet connection available\"    }  }}";
		}

	}

	public String getConnectionNOTFOUNDXML(ResponseDataAcceptType acceptType) {
		if (acceptType.getType() == ResponseDataAcceptType.ACCEPT_TYPE_XML.type) {
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><mAppData><businessOutput><opStatus>"
					+ APP_MSG_SERVER_CON_FAIL
					+ "</opStatus><errorMessage>unable to process your request. please try again</errorMessage></businessOutput></mAppData>";
		} else {
			return "{  \"mAppData\": {    \"businessOutput\": {      \"opStatus\": \"   "
					+ APP_MSG_SERVER_CON_NOT_FOUND
					+ " \",      \"errorMessage\": \"unable to process your request. please try again\"    }  }}";
		}

	}

	public String getCommunicationErrorXML(ResponseDataAcceptType acceptType) {
		if (acceptType.getType() == ResponseDataAcceptType.ACCEPT_TYPE_XML.type) {
			return "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><mAppData><businessOutput><opStatus>"
					+ APP_MSG_COMMUNICATION_ERROR
					+ "</opStatus><errorMessage>unable to process your request. please try again</errorMessage></businessOutput></mAppData>";
		} else {
			return "{  \"mAppData\": {    \"businessOutput\": {      \"opStatus\": \"   "
					+ APP_MSG_COMMUNICATION_ERROR
					+ " \",      \"errorMessage\": \"unable to process your request. please try again\"    }  }}";
		}

	}
}
