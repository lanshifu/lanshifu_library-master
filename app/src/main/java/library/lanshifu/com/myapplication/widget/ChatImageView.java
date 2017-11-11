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

import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;


public class ChatImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint paint;
    private Path muskPath;
    private int angleWidth = 30;//下角高度
    private Bitmap mRectMask;
    private Xfermode mXfermode;
    private float percent = 0.3f;//下角在底部的左边占据百分比
    private int roundRadius = 30;//圆角半径
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
        paint = new Paint();
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

        if (getWidth() > 0 && getHeight() > 0) {
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
        int left = this.getLeft();
        L.d("maskWidth "+maskWidth);
        L.d("maskHeight "+maskHeight);
        L.d("left  "+left);
        mRectMask = Bitmap.createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mRectMask);


        muskPath = new Path();
        muskPath.moveTo(roundRadius, 0); //起点
        muskPath.lineTo(maskWidth - roundRadius, 0); //拐点
        //右上角用当前端点、端点1(x1,y1)和端点2(x2,y2)这三个点所形成的夹角，然后绘制一段与夹角的两边相切并且半径为radius的圆上的弧线
        RectF rectF1 = new RectF(maskWidth - roundRadius * 2 - angleWidth, 0, maskWidth - angleWidth, roundRadius * 2);
        muskPath.arcTo(rectF1, 270, 90);

        muskPath.lineTo(maskWidth - angleWidth, maskHeight * percent);
        muskPath.lineTo(maskWidth, maskHeight * percent + angleWidth);
        muskPath.lineTo(maskWidth - angleWidth, maskHeight * percent + angleWidth * 2);
        muskPath.lineTo(maskWidth - angleWidth, maskHeight - roundRadius);

        //右下角
        RectF rectF2 = new RectF(maskWidth - angleWidth - roundRadius * 2, maskHeight - roundRadius * 2,
                maskWidth - angleWidth, maskHeight);
        muskPath.arcTo(rectF2, 0, 90);
        muskPath.lineTo(roundRadius, maskHeight);
        //左下角
        RectF ova3 = new RectF(0, maskHeight - roundRadius * 2, roundRadius * 2, maskHeight);
        muskPath.arcTo(ova3, 90, 90);
        muskPath.lineTo(0, roundRadius);
        //左上角
        RectF ova4 = new RectF(0, 0, roundRadius * 2, roundRadius * 2);
        muskPath.arcTo(ova4, 180, 90);

        canvas.drawPath(muskPath, paint);

    }


}