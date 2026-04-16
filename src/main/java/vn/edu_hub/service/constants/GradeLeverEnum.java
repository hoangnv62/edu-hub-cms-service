package vn.edu_hub.service.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum GradeLeverEnum {
    GRADE_1(1),
    GRADE_2(2),
    GRADE_3(3),
    GRADE_4(4),
    GRADE_5(5),
    GRADE_6(6),
    GRADE_7(7),
    GRADE_8(8),
    GRADE_9(9),
    GRADE_10(10),
    GRADE_11(11),
    GRADE_12(12),
    ;

    private final int value;

    GradeLeverEnum(int value) {
        this.value = value;
    }

    private static final Map<String, GradeLeverEnum> LOOKUP_NAME;
    private static final Map<Integer, GradeLeverEnum> LOOKUP_VALUE;

    static {
        LOOKUP_NAME = new HashMap<>();
        LOOKUP_VALUE = new HashMap<>();
        for (GradeLeverEnum value : GradeLeverEnum.values()) {
            LOOKUP_NAME.put(value.name(), value);
            LOOKUP_VALUE.put(value.getValue(), value);
        }
    }

    public static GradeLeverEnum find(String name) {
        return LOOKUP_NAME.get(name);
    }

    public static GradeLeverEnum find(int value) {
        return LOOKUP_VALUE.get(value);
    }
}
