package vn.edu_hub.service.exception;

import lombok.Getter;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.dto.response.IApiResponse;

import java.util.List;

@Getter
public class BusinessException extends BaseException {
    List<String> params = null;

    public BusinessException() {
    }

    public BusinessException(ApiResponseCode apiResponseCode, String messageDescription) {
        super(apiResponseCode.getCode(), apiResponseCode.getError(), messageDescription);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(String code, String message, String messageDescription) {
        super(code, message, messageDescription);
    }

    public BusinessException(IApiResponse iApiResponse) {
        super(iApiResponse);
    }

    public BusinessException(IApiResponse iApiResponse, List<String> params) {
        super(iApiResponse);
        this.params = params;
    }

    public BusinessException(IApiResponse iApiResponse, String errorDescription) {
        super(iApiResponse);
        this.messageDescription = errorDescription;
    }
}
