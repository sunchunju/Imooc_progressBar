package com.imooc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.imooc.progressbar.R;

/**
 * Created by suncj1 on 2016/10/19.
 */

public class HorizontalProgressbarWithProgress extends ProgressBar {

    //属性默认值
    private final static int DEFAULT_TEXT_SIZE = 10;  //sp
    private final static int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private final static int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
    private final static int DEFAULT_HEIGHT_UNREACH = 2;  //dp
    private final static int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private final static int DEFAULT_HEIGHT_REACH = 2;  //dp
    private final static int DEFAULT_TEXT_OFFSET = 10;  //dp


    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnReachColor = DEFAULT_COLOR_UNREACH;
    private int mUnReachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    private Paint mPaint = new Paint();

    private int mRealWidth;

    public HorizontalProgressbarWithProgress(Context context) {
        this(context,null); //调用两个构造参数的方法
    }

    //一般使用的是两个构造参数的方法，方便使用布局文件
    public HorizontalProgressbarWithProgress( Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressbarWithProgress, 0, 0);

        mTextSize = (int) a.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_size, mTextSize);
        mTextColor = a.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_text_color, mTextColor);
        mUnReachColor = a.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_color, mUnReachColor);
        mUnReachHeight = (int) a.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_height, mUnReachHeight);
        mReachColor = a.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_reach_color, mReachColor);
        mReachHeight = (int) a.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_reach_height, mReachHeight);
        mTextOffset = (int) a.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_offset, mTextOffset);

        mPaint.setTextSize(mTextSize);

        a.recycle();

    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int height = heightMeasure(heightMeasureSpec);

        setMeasuredDimension(widthSize, height);
    }

    private int heightMeasure(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        int result = 0;

        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            result = (int) (mPaint.descent() - mPaint.ascent() + getMax());
            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        //draw line
        mRealWidth = getWidth();
        mPaint.setStrokeWidth(mReachHeight);
        canvas.drawLine(0,0,mRealWidth, 0, mPaint);

        //draw text
        

    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    private int sp2px(int spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dpValue
     * scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    private int dp2px(int dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
