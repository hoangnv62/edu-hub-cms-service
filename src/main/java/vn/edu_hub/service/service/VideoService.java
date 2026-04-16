package vn.edu_hub.service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

    public String uploadVideo(MultipartFile file) {
        return "success";
    }
}
