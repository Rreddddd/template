package pojo;

import java.util.HashMap;
import java.util.Map;

public class MsgResult {

    public static final int SUCCESS_CODE = 0;
    public static final int FAILURE_CODE = 1;

    // 0成功 1错误
    private int errorCode;
    private String msg;
    private Map<Object, Object> data;

    public MsgResult() {
    }

    public MsgResult(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public void put(Object key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
    }

    public static MsgResult success() {
        return success(null);
    }

    public static MsgResult success(String msg) {
        return new MsgResult(SUCCESS_CODE, msg);
    }

    public static MsgResult failure() {
        return failure(null);
    }

    public static MsgResult failure(String msg) {
        return new MsgResult(FAILURE_CODE, msg);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<Object, Object> getData() {
        return data;
    }

    public void setData(Map<Object, Object> data) {
        this.data = data;
    }
}
