package library.lanshifu.com.myapplication.shell;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.lanshifu.fileprovdider7.FileProvider7;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.FileUtil;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import library.lanshifu.com.myapplication.ui.FileDisplayActivity;

/**
 * Created by lanshifu on 2017/9/9.
 */

public class FileManagerActivity extends BaseToolBarActivity {
    @Bind(R.id.et_shell)
    EditText etShell;
    @Bind(R.id.bt_wx)
    Button btWx;
    @Bind(R.id.bt_my)
    Button btMy;
    @Bind(R.id.tv_path)
    TextView tvPath;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private String path_wx_file = "/data/data/com.tencent.mm/";
    private String path_data_data = "/data/data/";
    private final static String key_shell = "shell";

    private List<String> mData = new ArrayList<>();
    private BaseQuickAdapter adapter;
    private String ls = "ls";
    private String prePath = "/";

    @Override
    protected int getLayoutid() {
        return R.layout.activity_filemanager;
    }

    @Override
    protected void onViewCreate() {

        setTBTitle("文件管理");
        addTBMore(new String[]{"shell命令"});

        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_filelist, mData) {

            @Override
            protected void convert(BaseViewHolder baseViewHolder, final String fileName) {

//                final boolean dir = FileUtil.isDir(prePath + "/" + fileName);
                final boolean dir = true;

                baseViewHolder.setText(R.id.tv_filename, fileName);
                baseViewHolder.setBackgroundRes(R.id.ll_bg,dir ? R.color.yellow : R.color.white);
                baseViewHolder.getView(R.id.tv_filename).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String filename = prePath + "/" + fileName;
                        if(filename.endsWith(".docx")||filename.endsWith(".pdf")
                                ||filename.endsWith(".pptx")
                                ||filename.endsWith(".txt")
                                ||filename.endsWith(".xlsx")){
                            Intent intent = new Intent(mContext, FileDisplayActivity.class);
                            intent.putExtra("path",filename);
                            startActivity(intent);
                            return;
                        }

                        //判断当前点击的是不是文件夹
                        if(dir){
                            String cd2wx = "cd " + fileName;
                            shell(cd2wx,ls);
                            shell("pwd");
                        }else {
                            showShortToast("打开文件");
                            Intent intent =openFile(filename);
                            startActivity(intent);
                        }


                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.openLoadAnimation(4);
//        adapter.openLoadAnimation();

        initEdittext();

        shell(ls);
        shell("pwd");
    }

    private void initEdittext() {
//        etShell.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                   String command = etShell.getText().toString();
//                    shell(command,ls);
//                }
//                return true;
//            }
//        });
    }




    private void shell(final String... commands) {
        final boolean isPwd = commands[0].equals("pwd");

        Observable.create(new ObservableOnSubscribe<CommandResult>() {
            @Override
            public void subscribe(ObservableEmitter<CommandResult> observableEmitter) throws Exception {

                CommandResult commandResult = Shell.SU.run(commands);
                observableEmitter.onNext(commandResult);
                observableEmitter.onComplete();

            }
        }).compose(RxSchedulerHelper.<CommandResult>io_main())
                .subscribe(new MyObserver<CommandResult>() {
                    @Override
                    public void _onNext(CommandResult commandResult) {

                        if(isPwd){
                            String path = commandResult.getStdout();
                            tvPath.setText(path);
                            prePath = path;
                            return;
                        }
                        List<String> stdout = commandResult.stdout;
                        if (stdout.size() > 0) {
                        } else {
                            showShortToast("没有数据");
                        }
                        adapter.replaceData(stdout);

                    }

                    @Override
                    public void _onError(String e) {
                        showLongToast(e);

                    }
                });
    }

    @Override
    public void onBackPressed() {
        String cd = "cd ..";
        shell(cd,ls);
        shell("pwd");
    }

//    @OnClick({R.id.bt_wx, R.id.bt_my})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.bt_wx:
//                shell(path_wx_file, ls);
//                break;
//            case R.id.bt_my:
//                shell(path_data_data, ls);
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            Shell.SU.closeConsole();
        }
    }


    public  Intent openFile(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(path);
        Uri uri = FileProvider7.getUriForFile(FileManagerActivity.this,file);
//        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, FileUtil.getMIMEType(file));
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if("shell命令".equals(item.getTitle())){

            startActivity(new Intent(this,ShellActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
