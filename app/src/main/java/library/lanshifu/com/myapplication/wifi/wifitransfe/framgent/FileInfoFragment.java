package library.lanshifu.com.myapplication.wifi.wifitransfe.framgent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.utils.AnimationUtils;
import library.lanshifu.com.myapplication.wifi.wifitransfe.AppContext;
import library.lanshifu.com.myapplication.wifi.wifitransfe.ChooseFileActivity;
import library.lanshifu.com.myapplication.wifi.wifitransfe.adapter.FileInfoAdapter;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.FileInfo;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.FileUtils;

/**
 * Created by 蓝师傅 on 2017/3/5.
 */

public class FileInfoFragment extends Fragment {

    @Bind(R.id.gv)
    GridView gv;
    @Bind(R.id.pb)
    ProgressBar pb;
    private int mType = FileInfo.TYPE_APK;
    private List<FileInfo> mFileInfoList;
    private FileInfoAdapter mFileInfoAdapter;

    public FileInfoFragment() {
        super();
    }


    @SuppressLint("ValidFragment")
    public FileInfoFragment(int type) {
        super();
        this.mType = type;
    }

    public static FileInfoFragment newInstance(int type) {
        FileInfoFragment fragment = new FileInfoFragment(type);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apk, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, rootView);


        if(mType == FileInfo.TYPE_APK){ //应用
            gv.setNumColumns(4);
        }else if(mType == FileInfo.TYPE_JPG){ //图片
            gv.setNumColumns(3);
        }else if(mType == FileInfo.TYPE_MP3){ //音乐
            gv.setNumColumns(1);
        }else if(mType == FileInfo.TYPE_MP4){ //视频
            gv.setNumColumns(1);
        }

        //Android6.0 requires android.permission.READ_EXTERNAL_STORAGE
        init();//初始化界面

        return rootView;
    }



    private void init() {
        if(mType == FileInfo.TYPE_APK){
            new GetFileInfoListTask(getContext(), FileInfo.TYPE_APK).executeOnExecutor(AppContext.MAIN_EXECUTOR);
        }else if(mType == FileInfo.TYPE_JPG){
            new GetFileInfoListTask(getContext(), FileInfo.TYPE_JPG).executeOnExecutor(AppContext.MAIN_EXECUTOR);
        } else if (mType == FileInfo.TYPE_MP3) {
            new GetFileInfoListTask(getContext(), FileInfo.TYPE_MP3).executeOnExecutor(AppContext.MAIN_EXECUTOR);
        } else if (mType == FileInfo.TYPE_MP4) {
            new GetFileInfoListTask(getContext(), FileInfo.TYPE_MP4).executeOnExecutor(AppContext.MAIN_EXECUTOR);
        }

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo fileInfo = mFileInfoList.get(position);
                if (AppContext.getAppContext().isExist(fileInfo)) {
                    AppContext.getAppContext().delFileInfo(fileInfo);
                    updateSelectedView();
                } else {
                    //1.添加任务
                    AppContext.getAppContext().addFileInfo(fileInfo);
                    //2.添加任务 动画
                    View startView = null;
                    View targetView = null;

                    startView = view.findViewById(R.id.iv_shortcut);
                    if (getActivity() != null && (getActivity() instanceof ChooseFileActivity)) {
                        ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getActivity();
                        targetView = chooseFileActivity.getSelectedView();
                    }
                    AnimationUtils.setAddTaskAnimation(getActivity(), startView, targetView, null);
                }

                mFileInfoAdapter.notifyDataSetChanged();
            }
        });
    }



    class GetFileInfoListTask extends AsyncTask<String, Integer, List<FileInfo>>{

        Context sContext = null;
        int sType = FileInfo.TYPE_APK;
        List<FileInfo> sFileInfoList = null;

        public GetFileInfoListTask(Context sContext, int type) {
            this.sContext = sContext;
            this.sType = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected List<FileInfo> doInBackground(String... params) {
            if(sType == FileInfo.TYPE_APK){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{ FileInfo.EXTEND_APK});
                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_APK);
            }else if(sType == FileInfo.TYPE_JPG){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{ FileInfo.EXTEND_JPG, FileInfo.EXTEND_JPEG});
                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_JPG);
            }else if(sType == FileInfo.TYPE_MP3){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{ FileInfo.EXTEND_MP3});
                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_MP3);
            }else if(sType == FileInfo.TYPE_MP4){
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{ FileInfo.EXTEND_MP4});
                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_MP4);
            }

            mFileInfoList = sFileInfoList;

            return sFileInfoList;
        }


        @Override
        protected void onPostExecute(List<FileInfo> fileInfos) {
            hideProgressBar();
            if(sFileInfoList != null && sFileInfoList.size() > 0){
                if(mType == FileInfo.TYPE_APK){ //应用
                    mFileInfoAdapter = new FileInfoAdapter(sContext, sFileInfoList, FileInfo.TYPE_APK);
                }else if(mType == FileInfo.TYPE_JPG){ //图片
                    mFileInfoAdapter = new FileInfoAdapter(sContext, sFileInfoList, FileInfo.TYPE_JPG);
                }else if(mType == FileInfo.TYPE_MP3){ //音乐
                    mFileInfoAdapter = new FileInfoAdapter(sContext, sFileInfoList, FileInfo.TYPE_MP3);
                }else if(mType == FileInfo.TYPE_MP4){ //视频
                    mFileInfoAdapter = new FileInfoAdapter(sContext, sFileInfoList, FileInfo.TYPE_MP4);
                }
                if(gv !=null){
                    gv.setAdapter(mFileInfoAdapter);
                }
            }else{
                T.showShort("暂时找不到应用信息");
            }
        }
    }

    /**
     * 显示进度
     */
    public void showProgressBar(){
        if(pb != null) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏进度
     */
    public void hideProgressBar(){
        if(pb != null && pb.isShown()) {
            pb.setVisibility(View.GONE);
        }
    }


    /**
     * 更新FileInfoAdapter
     */
    public void updateFileInfoAdapter(){
        if(mFileInfoAdapter != null){
            mFileInfoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 更新ChoooseActivity选中View
     */
    private void updateSelectedView(){
        if(getActivity() != null && (getActivity() instanceof ChooseFileActivity)){
            ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getActivity();
            chooseFileActivity.getSelectedView();
        }
    }


    @Override
    public void onResume() {
        updateFileInfoAdapter();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}