package library.lanshifu.com.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import library.lanshifu.com.myapplication.R;


public class ChatImageView extends android.support.v7.widget.AppCompatImageView{

    private Paint paint;
    private Path muskPath;
    private int roundRadius;//圆角半径
    private int angleWidth;//下角高度
    private float percent=0.3f;//下角在底部的左边占据百分比
    private Bitmap mRectMask;
    private Xfermode mXfermode;
    private int angleTop = 100;

    private int mOrientation; //方向
    private int maskWidth;
    private int maskHeight;


    public ChatImageView(Context context) {
        this(context, null);
    }

    public ChatImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ChatImageView1);
        mOrientation = ta.getInt(R.styleable.ChatImageView1_orientation, 0);
        ta.recycle();

        // 关键方法
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int id = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
//        int id = canvas.saveLayer(0, 0, canvas.getMaximumBitmapWidth(), canvas.getMaximumBitmapHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (null != drawable) {
            createMask();
            // 关键方法
            paint.setXfermode(mXfermode);
            canvas.drawBitmap(mRectMask, 0, 0, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(id);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        maskWidth = getMeasuredWidth();
        maskHeight = getMeasuredHeight();

        if (getWidth() > 0 && getHeight() > 0 ) {
            maskWidth = getWidth();
            maskHeight = getHeight();

        }
    }

    /**
     * 获取上层蒙层
     */
    private void createMask() {

            maskWidth = getMeasuredWidth();
            maskHeight = getMeasuredHeight();

            mRectMask = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mRectMask);

            muskPath=new Path();
            muskPath.moveTo(roundRadius,0); //起点
            muskPath.lineTo(maskWidth -roundRadius,0); //拐点
            //右上角用当前端点、端点1(x1,y1)和端点2(x2,y2)这三个点所形成的夹角，然后绘制一段与夹角的两边相切并且半径为radius的圆上的弧线
            muskPath.arcTo(new RectF(maskWidth -roundRadius*2 - angleWidth,0, maskWidth - angleWidth,roundRadius*2),270,90);

            muskPath.lineTo(maskWidth - angleWidth, maskHeight * percent);
            muskPath.lineTo(maskWidth, maskHeight * percent + angleWidth);
            muskPath.lineTo(maskWidth - angleWidth, maskHeight * percent + angleWidth *2);
            muskPath.lineTo(maskWidth - angleWidth, maskHeight - roundRadius);

            //右下角
            muskPath.arcTo(new RectF(maskWidth - angleWidth -roundRadius*2, maskHeight -roundRadius*2,
                    maskWidth - angleWidth, maskHeight),0,90);
            muskPath.lineTo(roundRadius, maskHeight);
            //箭头
//            muskPath.lineTo(maskWidth*percent,maskHeight);
//            muskPath.lineTo(maskWidth*percent-angleWidth,maskHeight-angleWidth);
//            muskPath.lineTo(roundRadius,maskHeight);

            //左下角
            muskPath.arcTo(new RectF(0, maskHeight -roundRadius*2,roundRadius*2, maskHeight),90,90);
            muskPath.lineTo(0,roundRadius);
            //左上角
            muskPath.arcTo(new RectF(0,0,roundRadius*2,roundRadius*2),180,90);

            canvas.drawPath(muskPath,paint);

    }

}