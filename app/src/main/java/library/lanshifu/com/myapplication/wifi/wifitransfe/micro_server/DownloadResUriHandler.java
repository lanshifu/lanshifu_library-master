package library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import library.lanshifu.com.lsf_library.utils.FileUtil;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.FileInfo;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.FileUtils;


/**
 * the iamge resource uri handler
 * 1.match the uri like: http://hostname:port/image/xxxx.xx
 *
 * Created by mayubao on 2016/12/15.
 * Contact me 345269374@qq.com
 */
public class DownloadResUriHandler implements ResUriHandler {

    public static final String DOWNLOAD_PREFIX = "/download/";
    private static final String TAG = "DownloadResUriHandler";

    private String end = "\r\n";
    private Activity mActivity;


    public DownloadResUriHandler(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public boolean matches(String uri) {
        return uri.startsWith(DOWNLOAD_PREFIX);
    }

    @Override
    public void handler(Request request) {
        //1.get the image file name from the uri
        String uri = request.getUri();
        String fileName = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
        //bug :resolve chinese incorrect code
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        FileInfo fileInfo = FileUtils.getFileInfo(this.mActivity, fileName);

        Log.i(TAG, "uri =  "+uri);
        Log.i(TAG, "filename =  "+fileName);
        Log.i(TAG, "文件路径 = "+fileInfo.getFilePath());

        //2.check the local system has the file. if has, return the image file, else return 404 to the client
        Socket socket = request.getUnderlySocket();
        OutputStream os = null;
        PrintStream printStream = null;
        try {
            os = socket.getOutputStream();
            printStream = new PrintStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(fileInfo == null ){//not exist this file
            printStream.println("HTTP/1.1 404 NotFound");
            printStream.println();
        }else if(fileInfo.getSize() == 0){
            Log.i(TAG, "文件大小 = 0: ");
            printStream.println("HTTP/1.1 404 NotFound");
            printStream.println();

        }else{
            printStream.println("HTTP/1.1 200 OK");
            printStream.println("Content-Length:" + fileInfo.getSize());
            printStream.println("Content-Disposition: attachment; filename="+fileName);

            L.e("Content-Length:"+fileInfo.getSize());
            //二进制文件流。这样浏览器就会直接打开文件，而不是在浏览器内打开。
            printStream.println("Content-Type:application/octet-stream");
//            if(fileInfo.getFileType() == FileInfo.TYPE_APK){
//                printStream.println("Content-Type:application/octet-stream");
//            }else if(fileInfo.getFileType() == FileInfo.TYPE_JPG){
//                printStream.println("Content-Type:image/jpeg");
//            }else if(fileInfo.getFileType() == FileInfo.TYPE_MP3){
//                printStream.println("Content-Type:audio/x-pn-realaudio");
//            }else if(fileInfo.getFileType() == FileInfo.TYPE_MP4){
//                printStream.println("Content-Type:application/octet-stream");
//            }

//            printStream.println("Expires: 0");
//            printStream.println("Cache-Control: must-revalidate, post-check=0, pre-check=0");
//            printStream.println("Content-Transfer-Encoding: binary");
//            printStream.println("Pragma: public");
            printStream.println();

            File file = null;
            FileInputStream fis = null;
            try {
                if(fileName.trim().equals("") || fileName.trim().equals("/")){
                    Log.i(TAG, "文件名不存在: ");
                    file = new File(mActivity.getPackageCodePath());
                }else{
                    Log.i(TAG, "文件路径: "+fileInfo.getFilePath());
                    file = new File(fileInfo.getFilePath());
                }
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            // send the file to the client
            try {
                int len = 0;
                byte[] bytes = new byte[1024];
                while((len = fis.read(bytes)) != -1){
                    printStream.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(fis != null){
                    try {
                        fis.close();
                        fis = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        printStream.flush();
        printStream.close();

    }

    @Override
    public void destroy() {
        this.mActivity = null;
    }

}
