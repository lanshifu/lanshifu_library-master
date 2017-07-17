package library.lanshifu.com.myapplication.popu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.Dictitem;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.widget.popu.CustomPopWindow;
import library.lanshifu.com.myapplication.widget.popu.ListPopWindow;

public class PopuDemoActivity extends BaseToolBarActivity {

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.showDown)
    Button showDown;
    @Bind(R.id.showUp)
    Button showUp;
    @Bind(R.id.showPopu)
    Button showPopu;
    @Bind(R.id.showList)
    Button showList;
    @Bind(R.id.showColorChange)
    Button showColorChange;
    @Bind(R.id.showAnim)
    Button showAnim;
    private CustomPopWindow mCustomPopWindow;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_popu_demo;
    }

    @Override
    protected void onViewCreate() {

    }



    @OnClick({R.id.tv_result, R.id.showDown, R.id.showUp, R.id.showPopu, R.id.showList, R.id.showColorChange, R.id.showAnim})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.showDown:
                showPopBottom();
                break;
            case R.id.showUp:
                showPopTop();
                break;
            case R.id.showPopu:
                showPopMenu();
                break;
            case R.id.showList:
                showPopListView();
                break;
            case R.id.showColorChange:
                showPopTopWithDarkBg();
                break;
            case R.id.showAnim:
                useInAndOutAnim();
                break;
        }
    }

    private void showPopBottom(){
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(R.layout.pop_layout1)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .create()
                .showAsDropDown(showDown,0,10);
    }

    private void showPopTop(){
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(R.layout.pop_layout2)
                .create();
        popWindow .showAsDropDown(showUp,0,  - (showUp.getHeight() + popWindow.getHeight()));
        //popWindow.showAtLocation(mButton1, Gravity.NO_GRAVITY,0,0);
    }

    /**
     * 显示PopupWindow 同时背景变暗
     */
    private void showPopTopWithDarkBg(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu,null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e("TAG","onDismiss");
                    }
                })
                .create()
                .showAsDropDown(showColorChange,0,20);
    }

    private void useInAndOutAnim(){
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(R.layout.pop_layout1)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle)
                .create()
                .showAsDropDown(showAnim,0,10);
    }

    private void showPopMenu(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu,null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .create()
                .showAsDropDown(showPopu,0,20);


    }


    private void showPopListView(){
//        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list,null);
//        //处理popWindow 显示内容
//        handleListView(contentView);
//        //创建并显示popWindow
//        mListPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
//                .setView(contentView)
//                .size(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)//显示大小
//                .create()
//                .showAsDropDown(showList,0,20);


        ListPopWindow listPopWindow = new ListPopWindow.Builder(this)
                .setData(mockData())
                .SetOnPopupItemSelectListener(new ListPopWindow.OnPopupItemSelectListener() {
                    @Override
                    public void select(int position, Dictitem dictitem) {
                        T.showShort("点击了："+dictitem.getDictname());
                    }
                })
//                .SetOnPopupDismissListener(new ListPopWindow.OnPopupDismissListener() {
//                    @Override
//                    public void onDismiss() {
//
//                    }
//                })
                .build()
                .showAsDropDown(showList);
    }

    private void handleListView(View contentView){
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(new CommonAdapter<String>(this,R.layout.list_item,mockData()) {

            @Override
            protected void convert(ViewHolder holder, final String s, int position) {
                holder.setText(R.id.title,s);
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.showShort("点击了："+s);
                    }
                });
            }
        });

    }

    private List<String> mockData(){
        List<String> data = new ArrayList<>();
        for (int i=0;i<10;i++){
            if(i>5){
                data.add("z这个是啦啦啦:"+i);
            }else {
                data.add("Item:"+i);
            }
        }

        return data;
    }


    /**
     * 处理弹出显示内容、点击事件等逻辑
     * @param contentView
     */
    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow !=null){
                    mCustomPopWindow.dissmiss();
                }
                String showContent = "";
                switch (v.getId()){
                    case R.id.menu1:
                        showContent = "点击 Item菜单1";
                        break;
                    case R.id.menu2:
                        showContent = "点击 Item菜单2";
                        break;
                    case R.id.menu3:
                        showContent = "点击 Item菜单3";
                        break;
                    case R.id.menu4:
                        showContent = "点击 Item菜单4";
                        break;
                    case R.id.menu5:
                        showContent = "点击 Item菜单5" ;
                        break;
                }
                T.showShort("点击了："+showContent);
            }
        };
        contentView.findViewById(R.id.menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.menu2).setOnClickListener(listener);
        contentView.findViewById(R.id.menu3).setOnClickListener(listener);
        contentView.findViewById(R.id.menu4).setOnClickListener(listener);
        contentView.findViewById(R.id.menu5).setOnClickListener(listener);
    }
}
