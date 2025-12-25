package com.lz.manage.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 是否同意 枚举
 * 对应字典类型：is_agree
 */
@Getter
public enum IsAgreeEnum {

    /** 待审核 */
    IS_AGREE_0("0", "待审核"),

    /** 同意 */
    IS_AGREE_1("1", "同意"),

    /** 拒绝 */
    IS_AGREE_2("2", "拒绝");

    private static final Map<String, IsAgreeEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (IsAgreeEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    IsAgreeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     */
    public static Optional<IsAgreeEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
