
package vn.edu_hub.service.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.dto.response.ApiErrorResponse;
import vn.edu_hub.service.exception.BaseException;
import vn.edu_hub.service.exception.BusinessException;
import vn.edu_hub.service.exception.CallApiException;
import vn.edu_hub.service.exception.InternalException;
import vn.edu_hub.service.utils.CommonUtils;

import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends Throwable {

    private final MessageSource messageSource;

    /**
     * 404 – Không tìm thấy endpoint
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorResponse handleNotFound(Exception e) {
        log.error("handleNotFound(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.RESOURCE_NOT_FOUND);
    }

    /**
     * 400 – Dữ liệu không hợp lệ
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handleValidationException(Exception e) {
        log.error("handleValidationException(): {}", ExceptionUtils.getStackTrace(e));

        String message = null;
        if (e instanceof MethodArgumentNotValidException ex) {
            message = ex.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("; "));
        } else if (e instanceof ConstraintViolationException ex) {
            message = ex.getConstraintViolations().iterator().next().getMessage();
        } else {
            message = e.getMessage();
        }

        return new ApiErrorResponse(ApiResponseCode.BAD_REQUEST.getCode(),
                ApiResponseCode.BAD_REQUEST.getError(), message);
    }

    /**
     * 405 – Gửi sai phương thức HTTP
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ApiErrorResponse handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupported(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 401 – Sai thông tin đăng nhập hoặc chưa xác thực
     */
    @ExceptionHandler({BadCredentialsException.class, InsufficientAuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiErrorResponse handleAuthenticationException(Exception e) {
        log.error("handleAuthenticationException(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.UNAUTHORIZED.getCode(),
                ApiResponseCode.UNAUTHORIZED.getError(), "Phiên đăng nhập đã hết hạn hoặc không hợp lệ");
    }

    /**
     * 403 – Không đủ quyền
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.FORBIDDEN.getCode(),
                ApiResponseCode.FORBIDDEN.getError(), "Bạn không có quyền truy cập tài nguyên này");
    }

    /**
     * 404 – Không tìm thấy dữ liệu trong DB
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiErrorResponse handleEmptyResult(EmptyResultDataAccessException e) {
        log.error("handleEmptyResult(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.ENTITY_NOT_FOUND.getCode(),
                ApiResponseCode.ENTITY_NOT_FOUND.getError(), "Không tìm thấy dữ liệu phù hợp");
    }

    /**
     * BusinessException – Lỗi nghiệp vụ do dev định nghĩa
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.error("handleBusinessException(): {}", ex.toString());

        int code = Integer.parseInt(ex.getCode());
        String desc;
        if (ex.getParams() != null) {
            desc = (ex.getMessageDescription() != null) ? ex.getMessageDescription() : CommonUtils.getMessage(messageSource, request, ex.getMessage(), ex.getParams().toArray());
        } else
            desc = (ex.getMessageDescription() != null) ? ex.getMessageDescription() : CommonUtils.getMessage(messageSource, request, ex.getMessage());

        return ResponseEntity.status(code)
                .body(new ApiErrorResponse(ex.getCode(), ex.getMessage(), desc));
    }

    /**
     * InternalException – Lỗi nội bộ hệ thống
     */
    @ExceptionHandler(InternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorResponse handleInternalException(InternalException ex, HttpServletRequest request) {
        log.error("handleInternalException(): {}", ExceptionUtils.getStackTrace(ex));
        String desc = ex.getMessageDescription() != null ? ex.getMessageDescription()
                : CommonUtils.getMessage(messageSource, request, ex.getMessage());
        return new ApiErrorResponse(ex.getCode(), ex.getMessage(), desc);
    }

    /**
     * 400 – Sai định dạng ngày/giờ khi parse thủ công
     */
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handleDateTimeParseException(DateTimeParseException e) {
        log.error("handleDateTimeParseException(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.BAD_REQUEST.getCode(),
                ApiResponseCode.BAD_REQUEST.getError(), "Định dạng ngày/giờ không hợp lệ: " + e.getParsedString());
    }

    /**
     * 400 – JSON sai định dạng
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("handleHttpMessageNotReadable(): {}", ExceptionUtils.getStackTrace(ex));
        return new ApiErrorResponse(ApiResponseCode.BAD_REQUEST.getCode(),
                ApiResponseCode.BAD_REQUEST.getError(), "Dữ liệu JSON không hợp lệ");
    }

    /**
     * 500 – Các lỗi runtime không xác định
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorResponse handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 500 – Các lỗi tổng quát khác
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorResponse handleException(Exception e) {
        log.error("handleException(): {}", ExceptionUtils.getStackTrace(e));
        return new ApiErrorResponse(ApiResponseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CallApiException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ApiErrorResponse handleCallApiException(CallApiException ex, HttpServletRequest request) {
        log.error("handleCallApiException(): {}", ex.toString());
        return generateApiErrorResponse(ex, request);
    }

    private ApiErrorResponse generateApiErrorResponse(BaseException ex, HttpServletRequest request) {
        String desc = (ex.getMessageDescription() != null) ? ex.getMessageDescription() : CommonUtils.getMessage(messageSource, request, ex.getMessage());
        return new ApiErrorResponse(ex.getCode(), ex.getMessage(), desc);
    }
}

