package library.lanshifu.com.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.commwidget.popmenu.PopMenu;
import library.lanshifu.com.lsf_library.commwidget.popmenu.PopMenuItem;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.multList.MultListActivity;

public class MainActivity extends BaseToolBarActivity {


    @BindView(R.id.btn_single)
    Button btnSingle;
    @BindView(R.id.btn_multi)
    Button btnMulti;
    @BindView(R.id.btn_base)
    Button btnBase;
    @BindView(R.id.btn_mult)
    Button btnMult;
    @BindView(R.id.toolbar)
    Button toolbar;
    @BindView(R.id.popmenu)
    Button popmenu;


    private PopMenu popMenu;


    @Override
    protected boolean onIfShowTB() {
        return true;
    }

    @Override
    protected int getLayoutid() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("主页");

        doSomeThing();

        T.showShort(doSomeThing());
    }

    private String doSomeThing() {
        return "doSomeThing";
    }

    @OnClick({R.id.btn_single, R.id.btn_base, R.id.btn_multi, R.id.toolbar, R.id.btn_mult, R.id.popmenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_single:
                String[] str = new String[]{"1", "2", "1", "2", "1", "2", "1", "2"};
                new AlertDialog.Builder(this)
                        .setTitle("单选框")
                        .setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                T.showShort("点击了" + which);
                                dialog.dismiss();
                            }
                        })
                        .show();


                break;
            case R.id.btn_multi:
                String[] str2 = new String[]{"1", "2", "3", "4"};
                boolean[] select = new boolean[]{false, false, false, true};
                new AlertDialog.Builder(this)
                        .setTitle("多选框")
                        .setMultiChoiceItems(str2, select, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                T.showShort("点击了" + which + "，isChecked=" + isChecked);
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;


            case R.id.btn_base:
                startActivity(new Intent(this, Main2Activity.class));
                break;
            case R.id.toolbar:
                startActivity(new Intent(this, ToolBarDemoActivity.class));
                break;

            case R.id.btn_mult:
                startActivity(new Intent(this, MultListActivity.class));
                break;

            case R.id.popmenu:
                if (popMenu == null) {
                    initPopuMenu();
                }
                if (!popMenu.isShowing()) {
                    popMenu.show();
                }
                break;
        }
    }


    private void initPopuMenu() {
        popMenu = new PopMenu.Builder(this)
                .columnCount(5)
                .addMenuItem(new PopMenuItem(this, "流布局", R.mipmap.icon_menu1))
                .addMenuItem(new PopMenuItem(this, "菜单1", R.mipmap.icon_menu2))
                .addMenuItem(new PopMenuItem(this, "菜单1", R.mipmap.icon_menu3))
                .addMenuItem(new PopMenuItem(this, "菜单1", R.mipmap.icon_menu4))
                .addMenuItem(new PopMenuItem(this, "菜单1", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(this, "菜单1", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(this, "菜单1", R.mipmap.icon_menu5))
                .addMenuItem(new PopMenuItem(this, "菜单1", R.mipmap.icon_menu5))
                .setOnPopMenuItemListener(new PopMenu.OnPopMenuItemClickListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        showShortToast("菜单" + position);
                        if (position == 0) {
                            startActivity(new Intent(MainActivity.this, FlowTagDemoActivity.class));
                        }
                    }
                })
                .build();
    }

}
