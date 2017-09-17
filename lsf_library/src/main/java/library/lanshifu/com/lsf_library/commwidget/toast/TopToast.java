package library.lanshifu.com.lsf_library.commwidget.toast;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import library.lanshifu.com.lsf_library.R;


/**
 * Created by Hammad Akram on 5/24/2017.
 */

public class TopToast implements View.OnClickListener {

    private static final int DEFAULT_VALUE = -100000;
    private static final String TAG = "TopToast";
    private static int mIcon = DEFAULT_VALUE;
    private static int mBackgroundColor = DEFAULT_VALUE;
    private static int mHeight = DEFAULT_VALUE;
    private static int mIconColorFilterColor = DEFAULT_VALUE;
    private static String mTitle = "";
    private static String mMessage = "";
    private static int mTitleColor = DEFAULT_VALUE;
    private static int mMessageColor = DEFAULT_VALUE;
    private static boolean mAutoHide = true;
    private static int mDuration = 3000;
    private static WeakReference<TopToastContentView> layoutWeakReference;
    private static WeakReference<Activity> contextWeakReference;
    private static boolean mIsCircular = false;
    private static OnSneakerClickListener mListener = null;
    private static Typeface mTypeFace = null;
    private ViewGroup viewGroup;

    /**
     * Constructor
     *
     * @param activity
     */
    private TopToast(Activity activity) {
        contextWeakReference = new WeakReference<>(activity);
    }

    /**
     * Create TopToast with activity reference
     *
     * @param activity
     * @return
     */
    public static TopToast with(Activity activity) {
        TopToast topToast = new TopToast(activity);
        setDefault();
        return topToast;
    }

    /**
     * Hides the sneaker
     */
    public static void hide() {
        if (getLayout() != null) {
            getLayout().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_hide));
            getActivityDecorView().removeView(getLayout());
        }
    }

    /**
     * Return activity parent view
     *
     * @return
     */
    private static ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;

        decorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();

        return decorView;
    }

    /**
     * Sets the default values to the sneaker
     */
    private static void setDefault() {
        mTitle = "";
        mIcon = DEFAULT_VALUE;
        mIconColorFilterColor = DEFAULT_VALUE;
        mBackgroundColor = DEFAULT_VALUE;
        mAutoHide = true;
        mTitleColor = DEFAULT_VALUE;
        mMessageColor = DEFAULT_VALUE;
        mHeight = DEFAULT_VALUE;
        mIsCircular = false;
        mListener = null;
        mTypeFace = null;

        mBackgroundColor = Color.parseColor("#2bb600");
        mTitleColor = Color.parseColor("#FFFFFF");
        mMessageColor = Color.parseColor("#FFFFFF");
        mIconColorFilterColor = Color.parseColor("#FFFFFF");
        mIcon = R.drawable.ic_success;
    }


    /**
     * Return activity weak reference
     *
     * @return
     */
    private static Context getContext() {
        return contextWeakReference.get();
    }

    /**
     * Returns sneaker main layout weak reference
     *
     * @return
     */
    private static View getLayout() {
        return layoutWeakReference.get();
    }

    /**
     * Sets the title of the sneaker
     *
     * @param title string value of title
     * @return
     */
    private TopToast setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * Sets the title of the sneaker with color
     *
     * @param title string value of title
     * @param color Color resource for title text
     * @return
     */
    public TopToast setTitle(String title, int color) {
        mTitle = title;
        if (getContext() != null) {
            try {
                mTitleColor = ContextCompat.getColor(getContext(), color);
            } catch (Exception e) {
                mTitleColor = color;
            }
        }
        return this;
    }

    public TopToast setBackgroundColor(int color) {
        if (getContext() != null) {
            try {
                mBackgroundColor = ContextCompat.getColor(getContext(), color);
            } catch (Exception e) {
                mBackgroundColor = color;
            }
        }
        return this;
    }

    /**
     * Sets the message to sneaker
     *
     * @param message String value of message
     * @return
     */
    public TopToast setMessage(String message) {
        mMessage = message;
        return this;
    }

    /**
     * Sets the message to sneaker with color
     *
     * @param message String value of message
     * @param color   Color resource for message text
     * @return
     */
    public TopToast setMessage(String message, int color) {
        mMessage = message;
        if (getContext() != null) {
            try {
                mMessageColor = ContextCompat.getColor(getContext(), color);
            } catch (Exception e) {
                mMessageColor = color;
            }
        }
        return this;
    }

    /**
     * Sets the icon to sneaker
     *
     * @param icon Icon resource for sneaker
     * @return
     */
    public TopToast setIcon(int icon) {
        mIcon = icon;
        return this;
    }

    /**
     * Sets the icon to sneaker with circular option
     *
     * @param icon
     * @param isCircular If icon is round or not
     * @return
     */
    public TopToast setIcon(int icon, boolean isCircular) {
        mIcon = icon;
        mIsCircular = isCircular;
        return this;
    }

    public TopToast setIcon(int icon, int tintColor) {
        mIcon = icon;
        if (getContext() != null) {
            try {
                mIconColorFilterColor = ContextCompat.getColor(getContext(), tintColor);
            } catch (Exception e) {
                mIconColorFilterColor = tintColor;
            }
        }
        return this;
    }

    /**
     * Sets the icon to sneaker with circular option and icon tint
     *
     * @param icon
     * @param tintColor  Icon tint color
     * @param isCircular If icon is round or not
     * @return
     */
    public TopToast setIcon(int icon, int tintColor, boolean isCircular) {
        mIcon = icon;
        mIsCircular = isCircular;
        if (getContext() != null) {
            try {
                mIconColorFilterColor = ContextCompat.getColor(getContext(), tintColor);
            } catch (Exception e) {
                mIconColorFilterColor = tintColor;
            }
        }
        return this;
    }

    /**
     * Disable/Enable auto hiding sneaker
     *
     * @param autoHide
     * @return
     */
    public TopToast autoHide(boolean autoHide) {
        mAutoHide = autoHide;
        return this;
    }

    /**
     * Sets the height to sneaker
     *
     * @param height Height value for sneaker
     * @return
     */
    public TopToast setHeight(int height) {
        mHeight = height;
        return this;
    }

    /**
     * Sets the duration for sneaker.
     * After this duration sneaker will disappear
     *
     * @param duration
     * @return
     */
    public TopToast setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    /**
     * Sets the click listener to sneaker
     *
     * @param listener
     * @return
     */
    public TopToast setOnSneakerClickListener(OnSneakerClickListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * Set font for title and message
     *
     * @param typeface
     * @return
     */
    public TopToast setTypeface(Typeface typeface) {
        mTypeFace = typeface;
        return this;
    }

    /**
     * Shows sneaker with custom color
     *
     * @param backgroundColor Color resource for sneaker background color
     */
    public void show(int backgroundColor) {
        if (getContext() != null) {
            try {
                mBackgroundColor = ContextCompat.getColor(getContext(), backgroundColor);
            } catch (Exception e) {
                mBackgroundColor = backgroundColor;
            }
            show();
        }
    }


    /**
     * Shows warning sneaker with fixed icon, background color and icon color.
     * Icons, background and text colors for this are not customizable
     */
    public void sneakWarning() {
        mBackgroundColor = Color.parseColor("#ffc100");
        mTitleColor = Color.parseColor("#000000");
        mMessageColor = Color.parseColor("#000000");
        mIconColorFilterColor = Color.parseColor("#000000");
        mIcon = R.drawable.ic_warning;

        show();
    }

    /**
     * Shows error sneaker with fixed icon, background color and icon color.
     * Icons, background and text colors for this are not customizable
     */
    public void sneakError() {
        mBackgroundColor = Color.parseColor("#ff0000");
        mTitleColor = Color.parseColor("#FFFFFF");
        mMessageColor = Color.parseColor("#FFFFFF");
        mIconColorFilterColor = Color.parseColor("#FFFFFF");
        mIcon = R.drawable.ic_error;

        show();
    }

    /**
     * Shows success sneaker with fixed icon, background color and icon color.
     * Icons, background and text colors for this are not customizable
     */
    public void sneakSuccess() {
        mBackgroundColor = Color.parseColor("#2bb600");
        mTitleColor = Color.parseColor("#FFFFFF");
        mMessageColor = Color.parseColor("#FFFFFF");
        mIconColorFilterColor = Color.parseColor("#FFFFFF");
        mIcon = R.drawable.ic_success;

        show();
    }

    /**
     * Creates the view and sneaks in
     */
    public void show() {
        if (getContext() == null) {
            Log.e(TAG, "context is null 请先调用 TopToast.with(this)");
            return;
        }

        // Main layout
        final TopToastContentView layout = new TopToastContentView(getContext());
        layoutWeakReference = new WeakReference<>(layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight == DEFAULT_VALUE ? (getStatusBarHeight() + convertToDp(56)) : convertToDp(mHeight));
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(46, getStatusBarHeight(), 46, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setElevation(6);
        }

        // Background color
        layout.setBackgroundColor(mBackgroundColor);

        // Icon
        // If icon is set
        if (mIcon != DEFAULT_VALUE) {
            if (!mIsCircular) {
                AppCompatImageView ivIcon = new AppCompatImageView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(convertToDp(24), convertToDp(24));
                ivIcon.setLayoutParams(lp);

                ivIcon.setImageResource(mIcon);
                ivIcon.setClickable(false);
                if (mIconColorFilterColor != DEFAULT_VALUE) {
                    ivIcon.setColorFilter(mIconColorFilterColor);
                }
                layout.addView(ivIcon);
            } else {
                ImageView ivIcon = new ImageView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(convertToDp(24), convertToDp(24));
                ivIcon.setLayoutParams(lp);

                ivIcon.setImageResource(mIcon);
                ivIcon.setClickable(false);
                if (mIconColorFilterColor != DEFAULT_VALUE) {
                    ivIcon.setColorFilter(mIconColorFilterColor);
                }
                layout.addView(ivIcon);
            }
        }

        // Title and description
        LinearLayout textLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayout.setLayoutParams(textLayoutParams);
        textLayout.setGravity(Gravity.CENTER);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (!mTitle.isEmpty()) {
            TextView tvTitle = new TextView(getContext());
            tvTitle.setLayoutParams(lpTv);
            tvTitle.setGravity(Gravity.CENTER_VERTICAL);
            if (!mMessage.isEmpty())
                tvTitle.setPadding(46, 26, 26, 0); // Top padding if there is message
            else
                tvTitle.setPadding(46, 0, 26, 0); // No top padding if there is no message
            if (mTitleColor != DEFAULT_VALUE)
                tvTitle.setTextColor(mTitleColor);

            // typeface
            if (mTypeFace != null)
                tvTitle.setTypeface(mTypeFace);

            tvTitle.setTextSize(14);
            tvTitle.setText(mTitle);
            tvTitle.setClickable(false);
            textLayout.addView(tvTitle);
        }

        if (!mMessage.isEmpty()) {
            TextView tvMessage = new TextView(getContext());
            tvMessage.setLayoutParams(lpTv);
            tvMessage.setGravity(Gravity.CENTER_VERTICAL);
            if (!mTitle.isEmpty())
                tvMessage.setPadding(46, 0, 26, 26); // Bottom padding if there is title
            else
                tvMessage.setPadding(46, 0, 26, 0); // No bottom padding if there is no title
            if (mMessageColor != DEFAULT_VALUE)
                tvMessage.setTextColor(mMessageColor);

            // typeface
            if (mTypeFace != null)
                tvMessage.setTypeface(mTypeFace);

            tvMessage.setTextSize(12);
            tvMessage.setText(mMessage);
            tvMessage.setClickable(false);
            textLayout.addView(tvMessage);
        }
        layout.addView(textLayout);

        viewGroup = getActivityDecorView();
        getExistingOverlayInViewAndRemove(viewGroup);

        layout.setOnClickListener(this);
        //发送延时handler消息，如果view销毁，会自动取消消息，在SneakerContentView 中处理了
        layout.mHandler.sendEmptyMessageDelayed(TopToastContentView.WHAT, mDuration);
        layout.setOnHandlerCallBackListner(new TopToastContentView.OnHandlerCallBackListner() {
            @Override
            public void handlerMsg() {
                if (mAutoHide) {
                    layout.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //展示4秒 消失
                            getLayout().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_hide));
                            viewGroup.removeView(getLayout());
                        }
                    });
                }
            }
        });
        viewGroup.addView(layout);
        layout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_show));

    }


    /**
     * Gets the existing sneaker and removes before adding new one
     *
     * @param parent
     */
    public void getExistingOverlayInViewAndRemove(ViewGroup parent) {

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof TopToastContentView) {
                parent.removeView(child);
            } else if (child instanceof ViewGroup) {
                getExistingOverlayInViewAndRemove((ViewGroup) child);
            }
        }
    }

    /**
     * Returns status bar height.
     *
     * @return
     */
    private int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = ((Activity) getContext()).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;

        return statusBarHeight;
    }

    private int convertToDp(float sizeInDp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

    /**
     * TopToast on click
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onSneakerClick(view);
        }
        hide();
    }

    public interface OnSneakerClickListener {
        void onSneakerClick(View view);
    }
}
