package moe.codeest.enviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by codeest on 16/11/6.
 * 最简单的一个，先拿这个热热身~
 */

public class ENRefreshView extends View{

    private static final int DEFAULT_LINE_COLOR = Color.WHITE;

    private static final int DEFAULT_LINE_WIDTH = 14;

    private static final int DEFAULT_DURATION = 2000;

    private Paint mPaint;

    private int mWidth, mHeight;

    private int mCircleRadius;

    private float mArrowLength;

    private float mArrowVarLength;

    private float mArcVarLength;

    private int mCenterX, mCenterY;

    private float mArrowX, mArrowY;

    private float mFraction;

    private int mDuration;

    public ENRefreshView(Context context) {
        super(context);
    }

    public ENRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.refresh);
        int color = ta.getColor(R.styleable.refresh_refresh_line_color, DEFAULT_LINE_COLOR);
        int width = ta.getInteger(R.styleable.refresh_refresh_line_width, DEFAULT_LINE_WIDTH);
        ta.recycle();
        mDuration = DEFAULT_DURATION;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mCircleRadius = mCenterX / 3 * 2;
        mArrowLength = mWidth / 6;
        mArrowX = (float) (- mCircleRadius * Math.sin(30 * Math.PI / 180));
        mArrowY = (float) (- mCircleRadius * Math.cos(30 * Math.PI / 180));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFraction <= 0.2) {                     //转上半圈
            mArrowVarLength = (mArrowLength / 0.4f * (0.4f - mFraction));
            mArcVarLength = 119.99f / 0.2f * mFraction;
        } else if (mFraction <= 0.4) {              //转下半圈
            mArrowVarLength = (mArrowLength / 0.4f * (0.4f - mFraction));
            mArcVarLength = 119.99f / 0.2f * (0.4f - mFraction);
        } else if (mFraction <= 0.55) {             //再弹.
            mArrowVarLength = (mArrowLength * 1.4f / 0.15f * (mFraction - 0.4f));
            mArcVarLength = - 28 / 0.15f * (mFraction - 0.4f);
        } else if (mFraction <= 0.7) {              //再弹..
            mArrowVarLength = mArrowLength * 1.4f - mArrowLength * 0.7f / 0.15f * (mFraction - 0.55f);
            mArcVarLength = - 28 + 36 / 0.15f * (mFraction - 0.55f);
        } else if (mFraction <= 0.85) {             //再弹...
            mArrowVarLength = mArrowLength * 0.7f + mArrowLength * 0.5f / 0.15f * (mFraction - 0.7f);
            mArcVarLength = 14 - 20 / 0.15f * (mFraction - 0.7f);
        } else {                                    //好吧...再弹 =。=
            mArrowVarLength = mArrowLength * 1.2f - mArrowLength * 0.2f / 0.15f * (mFraction - 0.85f);
            mArcVarLength = - 6 + 6 / 0.15f * (mFraction - 0.85f);
        }
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        if (mFraction <= 0.4) {
            canvas.rotate((float) (-10 + 360 / 0.4 * mFraction));
        } else {
            canvas.rotate(-10);
        }
        canvas.drawArc( - mCircleRadius, - mCircleRadius, mCircleRadius, mCircleRadius, 0 - mArcVarLength, 240 + mArcVarLength, false, mPaint);
        canvas.drawLine(mArrowX, mArrowY,mArrowX - mArrowVarLength, mArrowY, mPaint);
        canvas.drawLine(mArrowX, mArrowY, mArrowX, mArrowY + mArrowVarLength, mPaint);
        canvas.restore();
    }

    public void startRefresh() {
        mArrowLength = mWidth / 6;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.f, 100.f);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }
}
