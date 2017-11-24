package hr.eazework.com.model;

import java.io.Serializable;

/**
 * Created by Dell3 on 29-08-2017.
 */

public class GenericResponse implements Serializable {
    private String ErrorCode;
    private String ErrorMessage;
    private String MessageType;

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }
}
