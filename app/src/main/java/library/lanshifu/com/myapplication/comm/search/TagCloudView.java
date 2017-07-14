package library.lanshifu.com.myapplication.comm.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import library.lanshifu.com.lsf_library.utils.ViewIdGenerator;
import library.lanshifu.com.myapplication.R;


/**
 * Author:  [xWX371834\许纯震].
 * Date:    2016/9/14.
 * Description:
 */
public class TagCloudView extends FrameLayout implements View.OnClickListener{
    private List<Integer> positions = new ArrayList<Integer>(); // 用来存放每个tag的id，tag的位置与集合的index一致
    private OnTagClickListener onTagClickListener;

    public TagCloudView(Context context) {
        this(context, null);
    }

    public TagCloudView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagCloudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽度必须固定
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new RuntimeException("朋友，宽度都不确定让人怎么计算比较呢！");
        }
        // 只处理高度为wrap_content的情况
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            int totalHeight = getPaddingTop() + getPaddingBottom();

            int count = getChildCount();
            int widthUsed = getPaddingLeft() + getPaddingRight();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                widthUsed += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                if (widthUsed > widthSize) {
                    totalHeight += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                    widthUsed = getPaddingLeft() + getPaddingRight() + child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                }
                if (i == count - 1) {
                    totalHeight += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                }
            }
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(totalHeight, heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 放置子View， 考虑padding和margin的情况
        left = getPaddingLeft();
        top = getPaddingTop();

        int totalWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int usedWidth = 0;
        int usedHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            if (usedWidth + measuredWidth + params.leftMargin + params.rightMargin > totalWidth) {
                usedWidth = 0;
                usedHeight += measuredHeight + params.bottomMargin + params.topMargin;
            }

            child.layout(left + params.leftMargin + usedWidth, top + params.topMargin + usedHeight,
                    left + params.leftMargin + usedWidth + measuredWidth,
                    top + params.topMargin + usedHeight + measuredHeight);

            usedWidth += measuredWidth + params.rightMargin + params.leftMargin;
        }
    }

    /**
     * 设置标签
     *
     * @param tags
     */
    public void setTags(List<String> tags){
        positions.clear();
        removeAllViews();
        for(int i = 0; i< tags.size(); i++ ){
            String text = tags.get(i);
            createTag(text);
        }
        invalidate();
    }

    private void createTag(String text) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int margin = (int)getContext().getResources().getDimension(R.dimen.spacing_1x);
        params.leftMargin = params.rightMargin = params.topMargin = params.bottomMargin = margin;
        TextView textView = (TextView) View.inflate(getContext(), R.layout.search_tag, null);
        int id = ViewIdGenerator.generateViewId();
        textView.setText(text);
        textView.setId(id);
        positions.add(id);
        textView.setOnClickListener(this);
        addView(textView, params);
    }

    /**
     * 添加一个标签
     *
     * @param str
     */
    public void addTag(String str){
        createTag(str);
        invalidate();
    }

    /**
     * 移除一个标签
     *
     * @param position
     */
    public void removeTag(int position){
        removeViewAt(position);
        positions.remove(position);
        invalidate();
    }

    /**
     * 移除所有标签
     */
    public void removeAllTags(){
        removeAllViews();
        invalidate();
    }

    @Override
    public void onClick(View v) {
        if( onTagClickListener!=null){
            int i = positions.indexOf(v.getId());
            if( i!= -1){
                onTagClickListener.onTagClick(i);
            }
        }
    }


    public interface OnTagClickListener{
        void onTagClick(int position);
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener){
        this.onTagClickListener = onTagClickListener;
    }
}
