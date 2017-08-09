package library.lanshifu.com.lsf_library.commwidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/8/9.
 */

public class FloawLayout extends FrameLayout{

    private int mWidth;
    private int mHeight;

    private int widthSpace = 16;
    private int heightSpace = 16;

    public FloawLayout(@NonNull Context context) {
        super(context);
    }

    public FloawLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloawLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = widthSize;
        mHeight = heightSize;

        int allWidth = paddingLeft + paddingRight + widthSpace ;
        int allHeight = 0;
        int maxChildHeight = 0 ;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams params = child.getLayoutParams();
            child.measure(getChildMeasureSpec(widthMeasureSpec,paddingLeft+paddingRight,params.width),
                    getChildMeasureSpec(heightMeasureSpec,paddingTop+paddingBottom,params.height)
            );

            if(i == 0){
                maxChildHeight = child.getMeasuredHeight();
            }
            if(maxChildHeight < child.getMeasuredHeight()){
                //判断最大控件高
                maxChildHeight = child.getMeasuredHeight();
            }

            if(allHeight == 0){
                allHeight = child.getMeasuredHeight()+paddingTop + paddingBottom + heightSpace*2;
            }
            allWidth += (child.getMeasuredWidth() +child.getPaddingLeft() +child.getPaddingRight() + widthSpace);

            if(allWidth >= mWidth){

                //换行，高度为最大控件高度,总宽度为第一个控件宽
                allHeight += (maxChildHeight + heightSpace);
                allWidth = paddingLeft +paddingRight +child.getMeasuredWidth() + widthSpace;
                //默认第一个控件高度最大
                maxChildHeight = child.getMeasuredHeight();
            }

        }
//        mHeight = allHeight;
        if(heightMode == MeasureSpec.AT_MOST){
            //处理高度是包裹内容的情况
            mHeight = allHeight;
        }

        setMeasuredDimension(mWidth,mHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int mTop = getPaddingTop() + heightSpace;
        int mLeft = getPaddingLeft()+ widthSpace;
        int mRight = 0;
        int maxChildHeight = 0; //最大空间高

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if(maxChildHeight<child.getMeasuredHeight()){
                maxChildHeight = child.getMeasuredHeight();
            }

            mRight = mLeft+child.getMeasuredWidth();

            if(mRight > mWidth){
                mTop +=maxChildHeight;
                mTop += heightSpace;
                mLeft = getPaddingLeft()+ widthSpace;
                mRight = mLeft+child.getMeasuredWidth()+ heightSpace;
                maxChildHeight = 0; //让最大高度只影响当前一行
            }

            child.layout(mLeft,mTop,mRight,mTop+child.getMeasuredHeight());

            mLeft +=child.getMeasuredWidth()+ widthSpace + heightSpace;

        }


    }

}
