package vn.edu_hub.service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.edu_hub.service.dto.response.DashboardTeacherResDTO;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardService {
    public DashboardTeacherResDTO getDashboardTeacher(Long id)
    {
        return DashboardTeacherResDTO.builder().build();
    }

    public DashboardTeacherResDTO getDashboardStudent(Long id)
    {
        return DashboardTeacherResDTO.builder().build();
    }
}
