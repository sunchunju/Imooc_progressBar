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


    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnReachColor = DEFAULT_COLOR_UNREACH;
    protected int mUnReachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int mReachColor = DEFAULT_COLOR_REACH;
    protected int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();

    protected int mRealWidth;

    private int mProgress;

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
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); //宽度一定是有个值的，不可能是wrap content,所以不需要测量模式
        int height = heightMeasure(heightMeasureSpec);

        setMeasuredDimension(widthSize, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight(); //实际绘制区域宽度
    }

    private int heightMeasure(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        int result = 0;

        if (mode == MeasureSpec.EXACTLY){
            //精确值
            result = size;
        }else {
            //控件的高度值应该是mReachHeight  mUnReachHeight  mTextHeight这三种高度的最大值决定
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());  //字体高度
            result = getPaddingTop()+getPaddingBottom()+Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));
            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);  //移动坐标

        //draw reach bar
        float radio = getProgress()*1.0f/getMax();  //绘制的长度占mRealWidth的百分比
        boolean noNeedUnRech = false;   //不需要绘制未到达区域
        String text = getProgress()+"%";
        int textWidth = (int) mPaint.measureText(text);
        float progressX = radio*mRealWidth;
        if (progressX + textWidth > mRealWidth){
            progressX = mRealWidth - textWidth;
            noNeedUnRech = true;
        }

        float endX = progressX - mTextOffset/2;  //去除mTextOffset
        if (endX > 0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }

        //draw text
        mPaint.setColor(mTextColor);
        int y = (int)(-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text, progressX, y, mPaint);

        //draw unreach bar
        if(!noNeedUnRech){
            float start = progressX+mTextOffset/2+textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    protected int sp2px(int spValue) {
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
    protected int dp2px(int dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (progress > getMax()){
            mProgress = getMax();
        }else {
            mProgress = progress;
            postInvalidate();
        }
    }

    @Override
    public synchronized int getProgress() {
        return mProgress;
    }
}
