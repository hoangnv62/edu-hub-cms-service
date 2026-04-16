package vn.edu_hub.service.service.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileDownloadService {
    Resource download(MultipartFile file);
}
