package library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server;

import android.app.Activity;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import library.lanshifu.com.myapplication.Constant;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.FileInfo;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.FileUtils;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.TextUtils;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.ClassifyUtils;

/**
 * Created by 蓝师傅 on 2017/3/11.
 */

public class IndexResUriHandler implements ResUriHandler {

    private static final String TAG = "IndexResUriHandler";

    public static final String DOWNLOAD_PREFIX = "http://192.168.43.1:3999/download/";
    public static final String IMAGE_PREFIX = "http://192.168.43.1:3999/image/";
    public static final String DEFAULT_IMAGE_PATH = "http://192.168.43.1:3999/image/logo.png";

    private Activity mActivity;
    Map<String, FileInfo> sFileInfoMap = null;


    public IndexResUriHandler(Activity activity){
        this.mActivity = activity;
    }



    public IndexResUriHandler(Activity activity, Map<String, FileInfo> fileMap){
        this.mActivity = activity;
        this.sFileInfoMap = fileMap;
    }

    @Override
    public boolean matches(String uri) {

        return uri == null || uri.equals("") || uri.equals("/");
    }

    @Override
    public void handler(Request request) {
        //1.get the local index.html 获取本地html文件
        String indexHtml = null;

        try {
            InputStream is = mActivity.getAssets().open("index.html");
            indexHtml = IOStreamUtils.inputStreamToString(is);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //2.send the data to the client like http protocal
        //发送数据给客户端，像http协议
        if(request.getUnderlySocket() != null && indexHtml != null){
            OutputStream outputStream = null;
            PrintStream printStream = null;
            try {
                outputStream = request.getUnderlySocket().getOutputStream();
                printStream = new PrintStream(outputStream);
                printStream.println("HTTP/1.1 200 OK");
//                printStream.println("Content-Length:" + indexHtml.length());
                printStream.println("Content-Type:text/html");
                printStream.println("Cache-Control:no-cache");
                printStream.println("Pragma:no-cache");
                printStream.println("Expires:0");
                printStream.println();

                indexHtml = convert(indexHtml);

                byte[] bytes = indexHtml.getBytes("UTF-8");
                printStream.write(bytes);

                printStream.flush();
                printStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }finally {

                if(outputStream != null){
                    try {
                        outputStream.close();
                        outputStream = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(printStream != null){
                    try {
                        printStream.close();
                        printStream = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        }

    }

    @Override
    public void destroy() {
        mActivity = null;

    }


    /**
     * 转换
     * @param indexHtml
     * @return
     */
    public String convert(String indexHtml) {
        StringBuilder allFileListInfoHtmlBuilder = new StringBuilder();
        int count = this.sFileInfoMap.size();
        indexHtml = indexHtml.replaceAll("\\{app_avatar\\}", DEFAULT_IMAGE_PATH);
        indexHtml = indexHtml.replaceAll("\\{app_path\\}", DOWNLOAD_PREFIX);
        indexHtml = indexHtml.replaceAll("\\{app_name\\}", this.mActivity.getResources().getString(R.string.app_name));
        String ssid = TextUtils.isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE;
        indexHtml = indexHtml.replaceAll("\\{file_share\\}", ssid);
        indexHtml = indexHtml.replaceAll("\\{file_count\\}", String.valueOf(count));

        List<FileInfo> apkInfos = ClassifyUtils.filter(this.sFileInfoMap, FileInfo.TYPE_APK);
        List<FileInfo> jpgInfos = ClassifyUtils.filter(this.sFileInfoMap, FileInfo.TYPE_JPG);
        List<FileInfo> mp3Infos = ClassifyUtils.filter(this.sFileInfoMap, FileInfo.TYPE_MP3);
        List<FileInfo> mp4Infos = ClassifyUtils.filter(this.sFileInfoMap, FileInfo.TYPE_MP4);

        try {
            String apkInfosHtml = getClassifyFileInfoListHtml(apkInfos, FileInfo.TYPE_APK);
            String jpgInfosHtml = getClassifyFileInfoListHtml(jpgInfos, FileInfo.TYPE_JPG);
            String mp3InfosHtml = getClassifyFileInfoListHtml(mp3Infos, FileInfo.TYPE_MP3);
            String mp4InfosHtml = getClassifyFileInfoListHtml(mp4Infos, FileInfo.TYPE_MP4);

            allFileListInfoHtmlBuilder.append(apkInfosHtml);
            allFileListInfoHtmlBuilder.append(jpgInfosHtml);
            allFileListInfoHtmlBuilder.append(mp3InfosHtml);
            allFileListInfoHtmlBuilder.append(mp4InfosHtml);
            Log.i(TAG, " 应用列表:"+allFileListInfoHtmlBuilder.toString());
            indexHtml = indexHtml.replaceAll("\\{file_list_template\\}", allFileListInfoHtmlBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return indexHtml;
    }


    /**
     * 获取大类别的Html字符串
     * @param fileInfos
     * @param type
     * @return
     * @throws IOException
     */
    private String getClassifyFileInfoListHtml(List<FileInfo> fileInfos, int type) throws IOException{
        if(fileInfos == null || fileInfos.size() <= 0){
            return "";
        }

        String classifyHtml = IOStreamUtils.inputStreamToString(mActivity.getAssets().open(Constant.NAME_CLASSIFY_TEMPLATE));

        String className = "";
        switch (type){
            case FileInfo.TYPE_APK:{
                className = mActivity.getResources().getString(R.string.str_apk_desc);
                break;
            }
            case FileInfo.TYPE_JPG:{
                className = mActivity.getResources().getString(R.string.str_jpeg_desc);
                break;
            }
            case FileInfo.TYPE_MP3:{
                className = mActivity.getResources().getString(R.string.str_mp3_desc);
                break;
            }
            case FileInfo.TYPE_MP4:{
                className = mActivity.getResources().getString(R.string.str_mp4_desc);
                break;
            }

        }
        classifyHtml = classifyHtml.replaceAll("\\{class_name\\}", className);
        classifyHtml = classifyHtml.replaceAll("\\{class_count\\}", String.valueOf(fileInfos.size()));
        classifyHtml = classifyHtml.replaceAll("\\{file_list\\}", getFileInfoListHtml(fileInfos));

        return classifyHtml;
    }

    /**
     * 获取指定文件类型的的html字符串
     * @param fieInfos
     * @throws IOException
     */
    private String getFileInfoListHtml(List<FileInfo> fieInfos) throws IOException {
        StringBuilder sb = new StringBuilder();
        for(FileInfo fileInfo : fieInfos){
            String fileInfoHtml = IOStreamUtils.inputStreamToString(mActivity.getAssets().open(Constant.NAME_FILE_TEMPLATE));
            fileInfoHtml = fileInfoHtml.replaceAll("\\{file_avatar\\}", IMAGE_PREFIX + FileUtils.getFileName(fileInfo.getFilePath()));
            fileInfoHtml = fileInfoHtml.replaceAll("\\{file_name\\}", FileUtils.getFileName(fileInfo.getFilePath()));
            fileInfoHtml = fileInfoHtml.replaceAll("\\{file_size\\}", FileUtils.getFileSize(fileInfo.getSize()));
            fileInfoHtml = fileInfoHtml.replaceAll("\\{file_path\\}", DOWNLOAD_PREFIX + FileUtils.getFileName(fileInfo.getFilePath()));

            sb.append(fileInfoHtml);
        }

        return sb.toString();
    }


}
