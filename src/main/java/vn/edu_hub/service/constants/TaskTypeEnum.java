package vn.edu_hub.service.constants;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskTypeEnum {
    HOMEWORK(1),
    EXAM(2),
    TEST(3),
    ;

    private final int value;

    TaskTypeEnum(int label) {
        this.value = label;
    }

    private static final Map<String, TaskTypeEnum> LOOKUP_NAME;
    private static final Map<Integer, TaskTypeEnum> LOOKUP_VALUE;

    static {
        LOOKUP_NAME = new HashMap<>();
        LOOKUP_VALUE = new HashMap<>();
        for (TaskTypeEnum type : TaskTypeEnum.values()) {
            LOOKUP_NAME.put(type.name(), type);
            LOOKUP_VALUE.put(type.getValue(), type);
        }
    }

    public static TaskTypeEnum find(String name) {
        return LOOKUP_NAME.get(StringUtils.upperCase(name));
    }

    public static TaskTypeEnum find(int value) {
        return LOOKUP_VALUE.get(value);
    }
}
