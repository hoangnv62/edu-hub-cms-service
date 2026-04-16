package vn.edu_hub.service.constants;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MediaConstant {
    IMAGE(1), VIDEO(2), DOCUMENT(3);

    private final int value;

    MediaConstant(int value) {
        this.value = value;
    }

    public static MediaConstant find(int value) {
        return Arrays.stream(MediaConstant.values())
                .filter(e -> e.value == value)
                .findAny()
                .orElse(null);
    }

    public static MediaConstant find(String value) {
        return Arrays.stream(MediaConstant.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }
}
