package library.lanshifu.com.myapplication.model;

import java.util.List;

/**
 * Created by Administrator on 2017/9/3.
 */

public class GaoKaoBean {

    /**
     * code : -3
     * message : 请正确输入考生信息！
     * method : gklq.search
     * debug : []
     */

    private int code;
    private String message;
    private String method;
    private List<?> debug;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<?> getDebug() {
        return debug;
    }

    public void setDebug(List<?> debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        return "GaoKaoBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", method='" + method + '\'' +
                ", debug=" + debug +
                '}';
    }
}
