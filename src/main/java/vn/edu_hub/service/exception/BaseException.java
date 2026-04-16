package vn.edu_hub.service.exception;

import lombok.Getter;
import vn.edu_hub.service.dto.response.IApiResponse;

public class BaseException extends RuntimeException {
    @Getter
    protected String code;
    protected String message;

    @Getter
    protected String messageDescription;

    public BaseException() {

    }

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(IApiResponse apiResponse) {
        this.code = apiResponse.getCode();
        this.message = apiResponse.getError();
    }

    public BaseException(String code, String message, String messageDescription) {
        super(message);
        this.code = code;
        this.message = message;
        this.messageDescription = messageDescription;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("""
                BaseException{
                    code = %s
                    message = %s
                    messageDescription = %s
                }
                """, code, message, messageDescription);
    }
}
