package vn.edu_hub.service.exception;

import vn.edu_hub.service.dto.response.IApiResponse;

public class CallApiException extends BaseException {
    public CallApiException() {
    }

    public CallApiException(String code, String message) {
        super(code, message);
    }

    public CallApiException(String code, String message, String messageDescription) {
        super(code, message, messageDescription);
    }

    public CallApiException(IApiResponse apiResponse) {
        super(apiResponse);
    }
}
