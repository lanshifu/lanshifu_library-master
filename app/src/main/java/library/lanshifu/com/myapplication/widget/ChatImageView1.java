package library.lanshifu.com.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;


public class ChatImageView1 extends ImageView {
    private Bitmap srcBitmap;
    private Paint paint;
    private BitmapShader mBitmapShader;
    private Matrix mMatrix;
    private int width;
    private int height;
    private Paint borderPaint; //边框的画笔

    private int mOrientation; //方向
    private int mborderColor; //边框颜色

    public ChatImageView1(Context context) {
        super(context);
        init();
    }


    public ChatImageView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性值
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ChatImageView1);
        mOrientation = ta.getInt(R.styleable.ChatImageView1_orientation, 0);
        mborderColor = ta.getColor(R.styleable.ChatImageView1_borderColor, Color.GRAY);
        ta.recycle();

        init();
    }

    public ChatImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        paint = new Paint();
        paint.setAntiAlias(true);

        borderPaint = new Paint();
        borderPaint.setColor(mborderColor);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1);



    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (getWidth() > 0 && getHeight() > 0 && srcBitmap!=null) {
            width = getWidth();
            height = getHeight();

            int mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());

            //设置缩放
            int bSize = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());
            float scale = mWidth * 1.0f / bSize;
            mMatrix.setScale(scale, scale);
            mBitmapShader.setLocalMatrix(mMatrix);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        super.onDraw(canvas);


        if(getDrawable() != null){
            Drawable drawable = getDrawable();
            if(drawable instanceof GlideBitmapDrawable){
                srcBitmap = ((GlideBitmapDrawable)drawable).getBitmap();
            }else if(drawable instanceof BitmapDrawable){
                srcBitmap = ((BitmapDrawable)drawable).getBitmap();
            }
            mBitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mMatrix = new Matrix();
        }

        if(srcBitmap == null){
            L.e("srcBitmap ==null");
            return;
        }
        mBitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mMatrix = new Matrix();
        paint.setShader(mBitmapShader);

        Path path = null;
        if (mOrientation == 0) {
            path = getLeftPath();
        } else {
            path = getRightPath();
        }
        canvas.drawPath(path, paint);
        //画边框
        canvas.drawPath(path, borderPaint);
    }


    /**
     * 画出左朝向的path
     *
     * @return
     */
    private Path getLeftPath() {
        Path path = new Path();

        path.moveTo(40, 0);
        path.lineTo(width - 20, 0);
        RectF oval = new RectF(width - 40, 0, width, 40);
        path.arcTo(oval, 270, 90, false); //false表示不闭口

        path.lineTo(width, height - 20);

        oval = new RectF(width - 40, height - 40, width, height);
        path.arcTo(oval, 0, 90, false);

        path.lineTo(40, height);
        oval = new RectF(20, height - 40, 60, height);
        path.arcTo(oval, 90, 90, false);

        path.lineTo(20, 60);
        path.lineTo(0, 20);
        path.lineTo(20, 20);
        oval = new RectF(20, 0, 60, 40);
        path.arcTo(oval, 180, 90, false);
        path.close();//封闭
        return path;
    }


    /**
     * 画出右朝向的path
     *
     * @return
     */
    private Path getRightPath() {
        Path path = new Path();

        path.moveTo(20, 0);
        path.lineTo(width - 40, 0);
        RectF oval = new RectF(width - 60, 0, width - 20, 40);
        path.arcTo(oval, 270, 90, false);

        path.lineTo(width, 20);
        path.lineTo(width - 20, 60);
        path.lineTo(width - 20, height - 20);

        oval = new RectF(width - 60, height - 40, width - 20, height);
        path.arcTo(oval, 0, 90, false);

        path.lineTo(20, height);
        oval = new RectF(0, height - 40, 40, height);
        path.arcTo(oval, 90, 90, false);

        path.lineTo(0, 20);
        oval = new RectF(0, 0, 40, 40);
        path.arcTo(oval, 180, 90, false);
        path.close();//封闭
        return path;
    }
}