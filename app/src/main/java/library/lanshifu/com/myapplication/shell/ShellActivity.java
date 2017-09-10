package library.lanshifu.com.myapplication.shell;

import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import butterknife.Bind;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;

/**
 * Created by lanshifu on 2017/9/10.
 */

public class ShellActivity extends BaseToolBarActivity {
    @Bind(R.id.et_shell)
    EditText etShell;

   @Bind(R.id.tv_log)
    TextView tvLog;
   @Bind(R.id.scrollView)
   ScrollView scrollView;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_shell;
    }

    @Override
    protected void onViewCreate() {
        initEdittext();

        setTBTitle("shell");

    }

    private void initEdittext() {
        etShell.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    L.e("enter " +actionId);
                    String command = etShell.getText().toString();
                    tvLog.append("\n\n"+command);
                    etShell.setText("");
                    shell(command,"ls");
                }
                return true;
            }
        });

    }

    private void shell(final String... commands) {

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

                        String stdout1 = commandResult.getStdout();
                        tvLog.append("\r\n"+stdout1);
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                    }

                    @Override
                    public void _onError(String e) {
                        showLongToast(e);

                    }
                });
    }
}
