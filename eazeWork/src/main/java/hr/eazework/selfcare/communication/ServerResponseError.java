package hr.eazework.selfcare.communication;

public interface ServerResponseError {
/////SERVER RESPONSE ERROR CODES///

	/**
	 * success
	 */
	public final int SUCCESS                    = 0 ;
	/**
	 * Invalid Request
	 */
	public final int INVALID_REQUEST            = 1;
	/**
	 * Internal Server Error
	 */
	public final int INTERNAL_SERVER_ERROR      =2;
	public final int INTEGRATION_COMPONENT_ERROR=3;
	public final int REGISTRATION_ERROR         =4;
	public final int INVALID_OPERATOR_MSISDN    =5;
	public final int ALREADY_REGISTERED_MSISDN  =6;
	public final int USER_ID_ALREADY_REGISTERED =7;
	public final int TIME_OUT                   =8;
	public final int NO_DATA_AVAILABLE          =9;
	public final int REQUEST_BLOCKED            =10;
	public final int MSISDN_NOT_PRESENT         =11;
	public final int REGISTRATION_PARTIAL       =12;
	public final int DEVICE_CLIENT_ID_EXPIRED   =13;
	public final int API_SECRET_EXPIRED			=14;
	public final int FORCE_LOGOUT				=15;
	public final int FLUSH_CACHE				=16;
	public final int MANDATORY_PARAMETER_MISSING=17;
	public final int MANDATORY_PARAMETER_INCORRECT=18;
	public final int UNKNOWN_ERROR				=19;
	public final int ALGO_NOT_SUPPORTED			=20;
	public final int AUTHENTICATION_ERROR = 22;
}
