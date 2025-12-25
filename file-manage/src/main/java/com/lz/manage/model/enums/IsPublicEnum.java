package com.lz.manage.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 是否公开 枚举
 * 对应字典类型：is_public
 */
@Getter
public enum IsPublicEnum {

    /**
     * 私有
     */
    IS_PUBLIC_0("0", "私有"),

    /**
     * 公开
     */
    IS_PUBLIC_1("1", "公开");

    private static final Map<String, IsPublicEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (IsPublicEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    IsPublicEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举对象
     *
     * @param value 枚举值
     * @return 对应枚举（Optional）
     */
    public static Optional<IsPublicEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
