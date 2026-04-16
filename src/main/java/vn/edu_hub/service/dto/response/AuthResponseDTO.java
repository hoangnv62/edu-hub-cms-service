package vn.edu_hub.service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponseDTO {
    String accessToken;
    String tokenType;
    Integer expiresIn;
    Long created;
    String refreshToken;
    UserResponseDTO user;
}
