package vn.edu_hub.service.controller;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu_hub.service.dto.response.DashboardTeacherResDTO;
import vn.edu_hub.service.security.CurrentUserId;
import vn.edu_hub.service.service.DashboardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashBoardController {
    DashboardService dashboardService;
    @GetMapping("/teacher")
    public ResponseEntity<@NonNull DashboardTeacherResDTO>  getDashboardTeacher(@CurrentUserId Long id){
        return ResponseEntity.ok(dashboardService.getDashboardTeacher(id));
    }

    @GetMapping("/student")
    public ResponseEntity<@NonNull DashboardTeacherResDTO>  getDashboardStudent(@CurrentUserId Long id){
        return ResponseEntity.ok(dashboardService.getDashboardStudent(id));
    }
}
