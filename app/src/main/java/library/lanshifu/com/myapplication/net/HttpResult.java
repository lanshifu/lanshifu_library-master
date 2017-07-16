package library.lanshifu.com.myapplication.net;

import library.lanshifu.com.myapplication.net.api.ApiConstant;

/**
 * Created by lWX385269 lanshifu on 2017/4/10.
 */

public class HttpResult<T> {

    public int code;
    public String msg;
    public T data;


    public boolean isSuccess() {
        return code == ApiConstant.SUCCESS;
    }

    public boolean isEmpty() {
        return code == ApiConstant.EMPTY;
    }

    public boolean isNoMore() {
        return code == ApiConstant.NOMORE;
    }
}