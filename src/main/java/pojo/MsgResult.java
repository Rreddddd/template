package pojo;

import java.util.HashMap;
import java.util.Map;

public class MsgResult {

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
        return new MsgResult(0, null);
    }

    public static MsgResult failure() {
        return new MsgResult(1, null);
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
