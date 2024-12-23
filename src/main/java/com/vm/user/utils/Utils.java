package com.vm.user.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    public static String stringToPattern(String value){
        return (value != null && !value.isEmpty()) ? ".*" + value + ".*" : "";
    }
}
