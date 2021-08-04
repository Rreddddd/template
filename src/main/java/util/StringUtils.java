package util;

public abstract class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean isBlank(Object obj) {
        return obj == null || isBlank(obj.toString());
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }
}
