package library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server;

/**
 * Created by 蓝师傅 on 2017/3/11.
 */

public interface ResUriHandler {

    /**
     * is matches the specify uri
     * @param uri
     * @return
     */
    boolean matches(String uri);


    /**
     * handler the request which matches the uri
     * @param request
     */
    void handler(Request request);

    /**
     * releas some resource when finish the handler
     */
    void destroy();
}
