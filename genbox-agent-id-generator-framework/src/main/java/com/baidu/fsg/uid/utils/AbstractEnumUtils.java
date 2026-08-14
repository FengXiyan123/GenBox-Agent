package com.baidu.fsg.uid.utils;

import org.springframework.util.Assert;

/**
 * 工具类。
 */
public abstract class AbstractEnumUtils {

    public static <T extends ValuedEnum<V>, V> T parse(Class<T> clz, V value) {
        Assert.notNull(clz, "clz can not be null");
        if (value == null) {
            return null;
        }

        for (T t : clz.getEnumConstants()) {
            if (value.equals(t.value())) {
                return t;
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        if (name == null) {
            return null;
        }

        return Enum.valueOf(enumType, name);
    }

}
