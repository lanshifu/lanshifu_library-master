package library.lanshifu.com.myapplication.widget.colorselect;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;

/**
 * Created by lanxiaobin on 2017/8/22.
 */

public class ColorPicker {

    private static int argb = 0;

    public static void show(Context context, final OnColorSelectListener listener) {


        ColorPickerView colorPickerView = new ColorPickerView(context);
        colorPickerView.setOnColorBackListener(new ColorPickerView.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
                argb = Color.argb(a, r, g, b);
            }
        });

        new AlertDialog.Builder(context)
                .setTitle("颜色选择")
                .setView(colorPickerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.selectColor(argb);
                        }
                    }
                }).show();


    }


    public interface OnColorSelectListener {
        void selectColor(int rgb);
    }

}
