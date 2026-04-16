package vn.edu_hub.service.utils;

import org.springframework.web.multipart.MultipartFile;
import vn.edu_hub.service.constants.ApiResponseCode;
import vn.edu_hub.service.exception.BusinessException;

import java.util.Set;
import java.util.UUID;

public class FileUtils {
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    public static void validateImageFile(MultipartFile file){
        if(file == null || file.isEmpty()){
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tệp tin không được để trống");
        }
        String fileName = file.getOriginalFilename();
        if(fileName == null || !fileName.contains(".")){
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tệp tin không hợp lệ");
        }
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!IMAGE_EXTENSIONS.contains(extension)) {
            throw new BusinessException(ApiResponseCode.BAD_REQUEST,"Định dạng hình ảnh không được hỗ trợ");
        }
    }

    public static void validateDocumentFile(MultipartFile file){
        if(file == null || file.isEmpty()){
            throw new BusinessException(ApiResponseCode.BAD_REQUEST, "Tệp tin không được để trống");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
            (!contentType.equals("application/pdf")
             && !contentType.equals("application/msword")
             && !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

            throw new BusinessException(ApiResponseCode.BAD_REQUEST,"Định dạng tệp tin không được hỗ trợ");
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains("."))
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }
}
