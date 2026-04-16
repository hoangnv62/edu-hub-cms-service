package vn.edu_hub.service.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {
    String upload(MultipartFile file);
}
