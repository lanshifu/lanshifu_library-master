package library.lanshifu.com.myapplication.surfaceview;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.widget.colorselect.ColorPanelView;
import library.lanshifu.com.myapplication.widget.popu.CustomPopWindow;

public class SurfaceViewActivity extends BaseToolBarActivity {


    @Bind(R.id.doodleview)
    DoodleView mDoodleView;
    private AlertDialog mColorDialog;
    private AlertDialog mShapeDialog;
    private AlertDialog mPaintDialog;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_surface_view;
    }

    @Override
    protected void onViewCreate() {

        setTitle("涂鸦");
        addTBMore(new String[]{"画笔颜色", "画笔大小", "形状", "重置", "保存"});
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDoodleView.back();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        switch (title) {
            case "画笔颜色":
                showColorDialog();
                break;

            case "画笔大小":
                showSizeDialog();
                break;
            case "形状":
                showShapeDialog();
                break;

            case "重置":
                mDoodleView.reset();
                break;

            case "保存":
                String path = mDoodleView.saveBitmap(mDoodleView);
                Loge("onOptionsItemSelected: " + path);
                showShortToast("保存图片的路径为：" + path);
                break;

            default:

                break;
        }

        return true;
    }


    /**
     * 显示选择画笔颜色的对话框
     */
    private void showColorDialog() {
//        if (mColorDialog == null) {
//            mColorDialog = new AlertDialog.Builder(this)
//                    .setTitle("选择颜色")
//                    .setSingleChoiceItems(new String[]{"蓝色", "红色", "黑色"}, 0,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which) {
//                                        case 0:
//                                            mDoodleView.setColor("#0000ff");
//                                            break;
//                                        case 1:
//                                            mDoodleView.setColor("#ff0000");
//                                            break;
//                                        case 2:
//                                            mDoodleView.setColor("#272822");
//                                            break;
//                                        default:
//                                            break;
//                                    }
//                                    dialog.dismiss();
//                                }
//                            }).create();
//        }
//        mColorDialog.show();

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = View.inflate(this,R.layout.layout_color_select,null);
        ColorPanelView colorPanelView = view.findViewById(R.id.color_pannelview);
        colorPanelView.setOnColorChangedListener(new ColorPanelView.OnColorChangedListener() {
            @Override
            public void onColorChanged(ColorPanelView view, int color) {
                showShortToast(""+color);
//                mDoodleView.setColor(RGB(color)+"");
                Loge("color:"+color);
                if(dialog != null){n
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }


    /**
     * 显示选择画笔粗细的对话框
     */
    private void showSizeDialog() {
        if (mPaintDialog == null) {
            mPaintDialog = new AlertDialog.Builder(this)
                    .setTitle("选择画笔粗细")
                    .setSingleChoiceItems(new String[]{"细", "中", "粗"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mDoodleView.setSize(dip2px(5));
                                            break;
                                        case 1:
                                            mDoodleView.setSize(dip2px(10));
                                            break;
                                        case 2:
                                            mDoodleView.setSize(dip2px(15));
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mPaintDialog.show();
    }

    /**
     * 显示选择画笔形状的对话框
     */
    private void showShapeDialog() {
        if (mShapeDialog == null) {
            mShapeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
                    .setSingleChoiceItems(new String[]{"路径", "直线", "矩形", "圆形", "实心矩形", "实心圆"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mDoodleView.setType(DoodleView.ActionType.Path);
                                            break;
                                        case 1:
                                            mDoodleView.setType(DoodleView.ActionType.Line);
                                            break;
                                        case 2:
                                            mDoodleView.setType(DoodleView.ActionType.Rect);
                                            break;
                                        case 3:
                                            mDoodleView.setType(DoodleView.ActionType.Circle);
                                            break;
                                        case 4:
                                            mDoodleView.setType(DoodleView.ActionType.FillEcRect);
                                            break;
                                        case 5:
                                            mDoodleView.setType(DoodleView.ActionType.FilledCircle);
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mShapeDialog.show();
    }


    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
