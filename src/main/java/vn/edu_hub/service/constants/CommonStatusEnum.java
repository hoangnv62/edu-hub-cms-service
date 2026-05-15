package vn.edu_hub.service.constants;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CommonStatusEnum {
    ACTIVE(1), INACTIVE(-1), FINISHED(2);
    private final int value;

    // hằng số tĩnh nên chỉ được tạo 1 lần duy nhất
    static final Map<String, CommonStatusEnum> BY_NAME_LOOKUP;
    static final Map<Integer, CommonStatusEnum> BY_VALUE_LOOKUP;

    static {
        BY_NAME_LOOKUP = new HashMap<>();
        BY_VALUE_LOOKUP = new HashMap<>();
        for (CommonStatusEnum value : values()) {
            BY_VALUE_LOOKUP.put(value.getValue(), value);
            BY_NAME_LOOKUP.put(value.name(), value);
        }
    }

    CommonStatusEnum(int value) {
        this.value = value;
    }

    public static CommonStatusEnum find(String name) {
        return BY_NAME_LOOKUP.get(StringUtils.upperCase(name));
    }

    public static CommonStatusEnum find(int value) {
        return BY_VALUE_LOOKUP.get(value);
    }
}
