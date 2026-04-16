package vn.edu_hub.service.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequesterInfo implements Serializable {
    String requesterCode;
    String requesterName;
    long expirationInMilis = 0;

    @JsonCreator
    public RequesterInfo() {
    }
}
