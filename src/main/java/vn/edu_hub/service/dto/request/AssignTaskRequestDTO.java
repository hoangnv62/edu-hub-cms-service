package vn.edu_hub.service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignTaskRequestDTO {
    List<AssignTaskItemRequestDTO> tasks;
}
