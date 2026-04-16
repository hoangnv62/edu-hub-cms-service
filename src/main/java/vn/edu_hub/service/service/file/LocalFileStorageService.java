package vn.edu_hub.service.service.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class LocalFileStorageService implements IFileDeleteService, IFileUploadService, IFileDownloadService {
    @Override
    public void delete(String fileName) {

    }

    @Override
    public Resource download(MultipartFile file) {
        return null;
    }

    @Override
    public String upload(MultipartFile file) {
        return "";
    }
}
