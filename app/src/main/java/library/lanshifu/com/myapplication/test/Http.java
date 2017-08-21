package library.lanshifu.com.myapplication.test;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Created by lanxiaobin on 2017/8/17.
 */

public class Http {

    String baseURL = "http://10.15.67.10:8080/phptest1/index.php";
    //    String URL = baseURL+"?name="+editText1.getText().toString()+"&password="+editText2.getText().toString();
    String URL = "";

    private void login(String url) {

        new Thread() {

            @Override
            public void run() {

                try {
                    HttpGet httpGet = new HttpGet(URL);

                    HttpParams params = new BasicHttpParams();
//          采用UTF-8编码格式
                    params.setParameter("charset", HTTP.UTF_8);
//                   设置连接超时时间为8秒
                    HttpConnectionParams.setConnectionTimeout(params, 8 * 1000);
//                   设置数据请求时间为8秒
                    HttpConnectionParams.setSoTimeout(params, 8 * 1000);
//                   新建一个Http客户端对象
                    HttpClient httpClient = new DefaultHttpClient(params);
//                    用该对象发送GET请求，返回一个Http响应
                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                   判断是否获得正确的响应，然后执行相应的操作
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                  这里要注意一下返回的结果用UTF-8编码，否则当返回结果有中文时可能会出现乱码。最好所有的地方都统一编码
                        String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                        if (strResult.equals("loginsuccess")) {
//                      登录成功 ToDO
                            Log.d("debug", strResult);
                        } else {
//                      登录失败 ToDo
                            Log.d("debug", strResult);
                        }
                    }
                } catch (Exception e) {
                    Log.d("debug", "GETFAILED:" + e.toString());
                }

            }
        }.start();

    }
}
