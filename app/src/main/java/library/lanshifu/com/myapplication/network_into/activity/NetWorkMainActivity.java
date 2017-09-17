package library.lanshifu.com.myapplication.network_into.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.PrefUtil;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;

/**
 * Created by lanshifu on 2017/9/17.
 */

public class NetWorkMainActivity extends BaseToolBarActivity {

    /**
     *
     arp -s用于在arp表中添加一个静态的arp记录，目的是防止arp病毒
     arp -s的用法如下：
     arp -s 192.168.56.21 oo-55-66-77-88-99
     里面的192.168.56.21，代表新建立的静态arp的ip
     oo-55-66-77-88-99表示新建立的静态arp的MAC地址
     arp -s 是用来手动绑定网络地址(IP)对应的物理地址(MAC)
     网络执法官的原理是干扰你学习到网关正确的MAC地址。你可以用arp -s来手动绑定，举例：你网关的IP是192.168.1.1，
     网关的MAC地址是00-11-22-aa-bb-cc，那么你可以使用"arp -s 192.168.1.1 00-11-22-aa-bb-cc"
     然后你用arp -a就可以看到
     Internet Address Physical Address Type
     192.168.0.1 00-11-22-aa-bb-cc static

     arp -d 这是清除arp缓存
     arp -s 192.168.4.1 00-0f-e2-3e-a6-0e 将ip和mac地址邦定，一般是网关的

     */
    @Bind(R.id.main_open_protect_head)
    TextView mainOpenProtectHead;
    @Bind(R.id.cb_open_close_protect)
    CheckBox cbOpenCloseProtect;
    @Bind(R.id.main_arp_way_head)
    TextView mainArpWayHead;
    @Bind(R.id.btn_arp_cheat_way)
    Button btnArpCheatWay;
    @Bind(R.id.main_http_server_head)
    TextView mainHttpServerHead;
    @Bind(R.id.btn_http_server)
    Button btnHttpServer;
    @Bind(R.id.main_hijack_history_head)
    TextView mainHijackHistoryHead;
    @Bind(R.id.rl_history)
    RelativeLayout rlHistory;
    @Bind(R.id.bt_scanf)
    Button btScanf;

    private String protect_cmds = String.format("arp -s %s %s", NetworkHelper.getGateway(), NetworkHelper.getGatewayMac());
    private String close_protect_cmds = String.format("arp -d %s", NetworkHelper.getGateway());

    @Override
    protected int getLayoutid() {
        return R.layout.activity_network_main;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("网络渗透工具");
        initCheckBox();
        NetworkHelper.initWifiInfo();
        requestPermission();

    }

    private void requestPermission() {
        new RxPermissions(this)
                .request(android
                        .Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new MyObserver<Boolean>() {
                    @Override
                    public void _onNext(Boolean aBoolean) {
                        if (aBoolean){
//                            showShortToast("授权成功");
                        }
                    }

                    @Override
                    public void _onError(String e) {
//                        showErrorToast("权限拒绝");
                    }
                });
    }

    private void initCheckBox() {
        Boolean is_protected = PrefUtil.getInstance().getBoolean("is_protected");
        cbOpenCloseProtect.setChecked(is_protected);
        if (is_protected) {
            doShellCommand(close_protect_cmds, protect_cmds);
        }

        cbOpenCloseProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    doShellCommand(close_protect_cmds, protect_cmds);
                } else {
                    doShellCommand(close_protect_cmds);
                }
                PrefUtil.getInstance().putBoolean("is_protected",isChecked);
            }
        });

        String[] root = new String[]{"echo root"};
        if (!Shell.cmd(root).isSuccessful()) {
            showErrorToast("设备没有root");
            return;
        }

        String[] easyBoxComd = new String[]{"which killall"};
        CommandResult cmd = Shell.cmd(easyBoxComd);
        Loge(""+cmd.toString());
        if (cmd.isSuccessful()) {
            showErrorToast("busyBox没有安装"+cmd.toString());
            return;
        }

    }


    private void doShellCommand(final String... comds) {
        Observable.create(new ObservableOnSubscribe<CommandResult>() {
            @Override
            public void subscribe(ObservableEmitter<CommandResult> observableEmitter) throws Exception {
                CommandResult commandResult = Shell.SU.run(comds);
                observableEmitter.onNext(commandResult);
                observableEmitter.onComplete();

            }
        }).compose(RxSchedulerHelper.<CommandResult>io_main())
                .subscribe(new MyObserver<CommandResult>() {
                    @Override
                    public void _onNext(CommandResult commandResult) {
                        String stdout1 = commandResult.getStdout();
                        showShortToast("操作成功"+stdout1);
                    }

                    @Override
                    public void _onError(String e) {
                        showErrorToast(e);

                    }
                });
    }


    @OnClick({R.id.btn_arp_cheat_way, R.id.btn_http_server, R.id.bt_scanf, R.id.rl_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_arp_cheat_way:
                showShortToast("方式");
                break;
            case R.id.btn_http_server:
                startActivity(new Intent(this,TinyServiceActivity.class));
                break;
            case R.id.bt_scanf:
                startActivity(new Intent(this,NetworkScanfActivity.class));
                break;
            case R.id.rl_history:
                showShortToast("历史");
                break;
        }
    }
}
