package moe.codeest.enviews;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by codeest on 16/11/9.
 *
 * 我把它做成了SwitchView...不知道设计师是不是这个想法..  0.0
 */

public class ENScrollView extends View {

    private static final int STATE_SELECT = 0;

    private static final int STATE_UNSELECT = 1;

    private static final int DEFAULT_LINE_COLOR = Color.WHITE;

    private static final int DEFAULT_BG_LINE_COLOR = 0xff444a4f;

    private static final int DEFAULT_LINE_WIDTH = 10;

    private static final int DEFAULT_BG_LINE_WIDTH = 10;

    private static final int DEFAULT_DURATION = 700;

    private static final int DEFAULT_VIEW_TRANSLATION_X = 16;

    private static final boolean DEFAULT_STATE = false;

    private int mCurrentState = STATE_UNSELECT;

    private int mCurrentColor = DEFAULT_LINE_COLOR;

    private Paint mPaint, mBgPaint, mColorPaint;

    private Path mPath, mDstPath;

    private PathMeasure mPathMeasure;

    private RectF mRectF;

    private float mFraction;

    private float mWidth, mHeight;

    private float mCenterX, mCenterY;

    private float mCircleRadius;

    private float mPathLength;

    private int mLineColor, mBgLineColor, mDuration, mViewTranslationX;

    public ENScrollView(Context context) {
        super(context);
    }

    public ENScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.scroll);
        int lineColor = ta.getColor(R.styleable.scroll_scroll_line_color, DEFAULT_LINE_COLOR);
        int lineWidth = ta.getInteger(R.styleable.scroll_scroll_line_width, DEFAULT_LINE_WIDTH);
        int bgLineColor = ta.getInteger(R.styleable.scroll_scroll_bg_line_color, DEFAULT_BG_LINE_COLOR);
        int bgLineWidth = ta.getInteger(R.styleable.scroll_scroll_bg_line_width, DEFAULT_BG_LINE_WIDTH);
        boolean isSelected = ta.getBoolean(R.styleable.scroll_scroll_is_selected, DEFAULT_STATE);
        int viewTranslationX = ta.getInteger(R.styleable.scroll_scroll_view_translation_x, DEFAULT_VIEW_TRANSLATION_X);
        ta.recycle();

        mLineColor = lineColor;
        mBgLineColor = bgLineColor;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineWidth);

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgPaint.setColor(bgLineColor);
        mBgPaint.setStrokeWidth(bgLineWidth);

        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPaint.setStyle(Paint.Style.FILL);
        mColorPaint.setStrokeCap(Paint.Cap.ROUND);
        mColorPaint.setColor(mCurrentColor);

        mPath = new Path();
        mDstPath = new Path();
        mPathMeasure = new PathMeasure();

        mDuration = DEFAULT_DURATION;
        mViewTranslationX = viewTranslationX;
        if (isSelected) {
            mFraction = 1;
            mCurrentState = STATE_SELECT;
        } else {
            mFraction = 0;
            mCurrentState = STATE_UNSELECT;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h * 4 / 5;
        mWidth = h * 2.5f;  //控件长宽比定义为 5 : 2
        mCenterX = w / 2;
        mCenterY = h / 2;
        mCircleRadius = mHeight / 2;
        mRectF = new RectF(mCenterX - mCircleRadius * 2 , mCenterY - mCircleRadius,
                mCenterX + mCircleRadius * 2 , mCenterY + mCircleRadius);   //外圆矩形长宽比定义为 2 : 1
        mPath.addRoundRect(mRectF, mCircleRadius, mCircleRadius, Path.Direction.CW);
        mPathMeasure.setPath(mPath, false);
        mPathLength = mPathMeasure.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mFraction * mViewTranslationX, 0);
        canvas.drawRoundRect(mRectF, mCircleRadius, mCircleRadius, mBgPaint);   //嗷~ 在这画背景线
        mDstPath.reset();
        mPathMeasure.getSegment(0 ,mPathLength / 2 * mFraction, mDstPath, true);
        canvas.drawPath(mDstPath, mPaint);  //嗷~ 在这画上半圈
        mDstPath.reset();
        mPathMeasure.getSegment(mPathLength - mPathLength / 2 * mFraction ,mPathLength, mDstPath, true);
        canvas.drawPath(mDstPath, mPaint);  //嗷~ 在这画下半圈
        mColorPaint.setColor(mCurrentColor);
        if (mFraction <= 0.8 && mFraction > 0.2) {  //嗷~ 在这画球
            canvas.drawCircle(mCenterX - mCircleRadius + 2 * mCircleRadius * mFraction , mCenterY , mCircleRadius / 6 / 0.6f * (mFraction - 0.2f) , mColorPaint);
        } else if (mFraction > 0.8) {
            canvas.drawCircle(mCenterX - mCircleRadius + 2 * mCircleRadius * mFraction , mCenterY , mCircleRadius / 6 , mColorPaint);
        }
        canvas.restore();
    }

    public void select() {
        if (mCurrentState == STATE_SELECT) {
            return;
        }
        mCurrentState = STATE_SELECT;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                mBgLineColor, mLineColor);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = valueAnimator.getAnimatedFraction();
                mCurrentColor = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        valueAnimator.start();
    }

    public void unSelect() {
        if (mCurrentState == STATE_UNSELECT) {
            return;
        }
        mCurrentState = STATE_UNSELECT;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                mLineColor, mBgLineColor);
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = 1 - valueAnimator.getAnimatedFraction();
                mCurrentColor = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        valueAnimator.start();
    }

    public boolean isSelected() {
        return mCurrentState == 0;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }
}
