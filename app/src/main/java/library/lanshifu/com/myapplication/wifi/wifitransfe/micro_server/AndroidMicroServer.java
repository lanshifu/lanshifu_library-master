package library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 蓝师傅 on 2017/3/11.
 */

public class AndroidMicroServer {
    private static final String TAG = "AndroidMicroServer";

    List<ResUriHandler> mResUriHandlerList = new ArrayList<ResUriHandler>();

    private int mPort;

    /**
     * the server socket
     */
    private ServerSocket mServerSocket;

    /**
     *  the thread pool which handle the incoming request
     */
    private ExecutorService mThreadPool = Executors.newCachedThreadPool();


    /**
     * the flag which the micro server enable
     */
    private boolean mIsEnable = true;



    /**
     * register the resource uri handler
     * @param resUriHandler
     */
    public void resgisterResUriHandler(ResUriHandler resUriHandler){
        this.mResUriHandlerList.add(resUriHandler);
    }

    /**
     * unresigter all the resource uri hanlders
     */
    public void unresgisterResUriHandlerList(){
        for(ResUriHandler resUriHandler : mResUriHandlerList){
            resUriHandler.destroy();
            resUriHandler = null;
        }
    }



    public AndroidMicroServer(int port){
        this.mPort = port;
    }

    /**
     * 开启socket服务
     * start the android micro server
     */
    public void start(){

        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "start the android micro server");
                try {
                    mServerSocket = new ServerSocket(mPort);

                    while(mIsEnable){
                        Log.i(TAG, "Socket.accept()，等待客户端消息中...");
                        Socket socket = mServerSocket.accept();
                        hanlderSocketAsyn(socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * handle the incoming socket
     * @param socket
     */
    private void hanlderSocketAsyn(final Socket socket) {
        mThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                //1. auto create request object by the parameter socket
                Request request = createRequest(socket);

                //2. loop the mResUriHandlerList, and assign the task to the specify ResUriHandler
                for(ResUriHandler resUriHandler : mResUriHandlerList){
                    if(!resUriHandler.matches(request.getUri())){
                        continue;
                    }

                    resUriHandler.handler(request);
                }
            }
        });
    }


    /**
     * create the requset object by the specify socket
     *
     * @param socket
     * @return
     */
    private Request createRequest(Socket socket) {
        Request request = new Request();
        request.setUnderlySocket(socket);
        try {
            //Get the reqeust line
            SocketAddress socketAddress = socket.getRemoteSocketAddress();
            InputStream is = socket.getInputStream();
            String requestLine = IOStreamUtils.readLine(is);
            requestLine = URLDecoder.decode(requestLine, "UTF-8");
            String requestType = requestLine.split(" ")[0];
            //这里有问题，如果名称有空格怎么取
//            String requestUri = requestLine.split(" ")[1];
            String requestUri = requestLine.replace("GET ","").replace(" HTTP/1.1","").trim();

//            //解决URL中文乱码的问题
            requestUri = URLDecoder.decode(requestUri, "UTF-8");
            Log.i(TAG,  "requestLine ="+ requestLine);
            Log.i(TAG,  "requestUri ="+ requestUri);
            request.setUri(requestUri);


            //Get the header line
            String header = "";
            while((header = IOStreamUtils.readLine(is)) != null){
                header = URLDecoder.decode(header, "UTF-8");
                String headerKey = header.split(":")[0];
                String headerVal = header.split(":")[1];
                request.addHeader(headerKey, headerVal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG,  "---------------------------\r\n request  = " + request.toString()+"\r\n" +
                "---------------------------------");

        return request;
    }


    /**
     * stop the android micro server
     */
    public void stop(){
        if(mIsEnable){
            mIsEnable = false;
        }

        //release resource
        unresgisterResUriHandlerList();

        if(mServerSocket != null){
            try {
//                mServerSocket.accept(); //fuck ! fix the problem， block the main thread
                mServerSocket.close();
                mServerSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
