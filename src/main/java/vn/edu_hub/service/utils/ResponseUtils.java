package vn.edu_hub.service.utils;

import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.dto.response.CommonResponseDTO;

public class ResponseUtils {
    public static CommonResponseDTO success() {
        return CommonResponseDTO.builder()
                .status(ApiResponseCode.SUCCESS.getCode())
                .response(ApiResponseCode.SUCCESS.getError())
                .build();
    }
}
