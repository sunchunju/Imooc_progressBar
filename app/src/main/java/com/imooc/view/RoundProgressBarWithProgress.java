package com.imooc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.imooc.progressbar.R;

/**
 * Created by suncj1 on 2016/10/25.
 */

public class RoundProgressBarWithProgress extends HorizontalProgressbarWithProgress {

    private int mRadius = dp2px(30);

    private int mMaxpaintWidth;

    public RoundProgressBarWithProgress(Context context) {
        this(context,null);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        mReachHeight = (int) (mUnReachHeight*2.5f);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress, 0, 0);
        mRadius = (int) a.getDimension(R.styleable.RoundProgressBarWithProgress_radius, 10);

        a.recycle();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //没看懂
        mMaxpaintWidth = Math.max(mReachHeight, mUnReachHeight);
        //默认四个Padding一致
        int expect = mRadius*2 + mMaxpaintWidth + getPaddingLeft() + getPaddingRight();  //默认值

        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        int readWidth = Math.min(width, height);

        mRadius = (readWidth - getPaddingLeft() - getPaddingRight() - mMaxpaintWidth)/2;

        setMeasuredDimension(readWidth, readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);   //文字宽度
        float textHeight = (mPaint.descent()+mPaint.ascent())/2;  //文字高度

        canvas.save();
        canvas.translate(getPaddingLeft()+mMaxpaintWidth/2, getPaddingTop()+mMaxpaintWidth/2);  //移动坐标到指定位置
        mPaint.setStyle(Paint.Style.STROKE);

        //draw unreach bar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius, mPaint);

        //draw reach bar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress()*1.0f/getMax()*360; //弧度

        RectF rectF = new RectF(0,0,mRadius*2, mRadius*2);
        canvas.drawArc(rectF, 0, sweepAngle, false, mPaint);

        //draw text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(text, mRadius - textWidth/2, mRadius - textHeight, mPaint);

        canvas.restore();

    }
}
