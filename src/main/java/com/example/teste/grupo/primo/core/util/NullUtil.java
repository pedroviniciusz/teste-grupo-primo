package com.example.teste.grupo.primo.core.util;

public class NullUtil {

    private NullUtil() {}

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean isNotNull(Object value) {
        return value != null;
    }

    public static boolean isNullOrEmpty(String value) {
        return (value == null) || (value.trim().isEmpty());
    }

}
