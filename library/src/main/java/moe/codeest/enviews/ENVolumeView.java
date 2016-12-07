package moe.codeest.enviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by codeest on 16/11/13.
 *
 * 嗯..这个控件很带感 隔着屏幕都能感受到声音有木有~
 */

public class ENVolumeView extends View{

    private static final int STATE_SILENT = 0;

    private static final int STATE_VOLUME = 1;

    private static final int STATE_VIBRATE = 2;

    private static final int DEFAULT_LINE_COLOR = Color.WHITE;

    private static final int DEFAULT_BG_LINE_COLOR = 0xff64696d;

    private static final int DEFAULT_LINE_WIDTH = 10;

    private static final int DEFAULT_BG_LINE_WIDTH = 10;

    private Paint mPaint, mBgPaint;

    private Path mPath, mDstPath;

    private PathMeasure mPathMeasure;

    private float mFraction;

    private int mVolumeValue;

    private float mCurrentState = STATE_SILENT;

    private float mWidth, mHeight;

    private float mCenterX, mCenterY;

    private float mBaseLength, mPathLength;

    private ValueAnimator vibrateAnim;

    public ENVolumeView(Context context) {
        super(context);
    }

    public ENVolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.volume);
        int lineColor = ta.getColor(R.styleable.volume_volume_line_color, DEFAULT_LINE_COLOR);
        int lineWidth = ta.getInteger(R.styleable.volume_volume_line_width, DEFAULT_LINE_WIDTH);
        int bgLineColor = ta.getInteger(R.styleable.volume_volume_bg_line_color, DEFAULT_BG_LINE_COLOR);
        int bgLineWidth = ta.getInteger(R.styleable.volume_volume_bg_line_width, DEFAULT_BG_LINE_WIDTH);
        ta.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineWidth);

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgPaint.setStrokeJoin(Paint.Join.ROUND);
        mBgPaint.setColor(bgLineColor);
        mBgPaint.setStrokeWidth(bgLineWidth);

        mPath = new Path();
        mDstPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w * 5 / 6;
        mHeight = h;
        mBaseLength = mWidth / 6;
        mCenterX = w / 2;
        mCenterY = h / 2;

        mPath.moveTo(mCenterX - 3 * mBaseLength, mCenterY);
        mPath.lineTo(mCenterX - 3 * mBaseLength, mCenterY - 0.5f * mBaseLength);
        mPath.lineTo(mCenterX - 2 * mBaseLength, mCenterY - 0.5f * mBaseLength);
        mPath.lineTo(mCenterX, mCenterY - 2 * mBaseLength);
        mPath.lineTo(mCenterX, mCenterY + 2 * mBaseLength);
        mPath.lineTo(mCenterX - 2 * mBaseLength, mCenterY + 0.5f * mBaseLength);
        mPath.lineTo(mCenterX - 3 * mBaseLength, mCenterY + 0.5f * mBaseLength);
        mPath.close();
        mPathMeasure.setPath(mPath, false);
        mPathLength = mPathMeasure.getLength();

        vibrateAnim = ValueAnimator.ofFloat(1.f, 100.f);
        vibrateAnim.setDuration(100);
        vibrateAnim.setInterpolator(new LinearInterpolator());
        vibrateAnim.setRepeatCount(ValueAnimator.INFINITE);
        vibrateAnim.setRepeatMode(ValueAnimator.REVERSE);
        vibrateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentState != STATE_VIBRATE) {
            if (mFraction <= 0.5) {
                mDstPath.reset();   //嗷~ 在这画喇叭
                mPathMeasure.getSegment(0, mPathLength * 0.38f - mPathLength * 0.13f * mFraction, mDstPath, true);
                canvas.drawPath(mDstPath, mBgPaint);
                mDstPath.reset();
                mPathMeasure.getSegment(mPathLength * 0.62f + mPathLength * 0.13f * mFraction, mPathLength, mDstPath, true);
                canvas.drawPath(mDstPath, mBgPaint);

                canvas.save();  //嗷~ 在这画X
                canvas.translate(- mFraction * mBaseLength * 2, 0);
                canvas.drawLine(mCenterX - mBaseLength * (0.5f - mFraction) , mCenterY - mBaseLength * (0.5f - mFraction)
                        ,mCenterX + mBaseLength * (0.5f - mFraction), mCenterY + mBaseLength * (0.5f - mFraction), mBgPaint);
                canvas.drawLine(mCenterX - mBaseLength * (0.5f - mFraction), mCenterY + mBaseLength * (0.5f - mFraction)
                        ,mCenterX + mBaseLength * (0.5f - mFraction), mCenterY - mBaseLength * (0.5f - mFraction), mBgPaint);
                canvas.restore();
            } else {
                mDstPath.reset();   //嗷~ 在这画喇叭
                mPathMeasure.getSegment(0, mPathLength * 0.25f + mPathLength * 0.13f * (mFraction - 0.5f), mDstPath, true);
                canvas.drawPath(mDstPath, mBgPaint);
                mDstPath.reset();
                mPathMeasure.getSegment(mPathLength * 0.75f - mPathLength * 0.13f * (mFraction - 0.5f), mPathLength, mDstPath, true);
                canvas.drawPath(mDstPath, mBgPaint);
                mDstPath.reset();
                mPathMeasure.getSegment(0, mPathLength * 0.38f / 0.5f * (mFraction - 0.5f), mDstPath, true);
                canvas.drawPath(mDstPath, mPaint);
                mDstPath.reset();
                mPathMeasure.getSegment(mPathLength - mPathLength * 0.38f / 0.5f * (mFraction - 0.5f), mPathLength, mDstPath, true);
                canvas.drawPath(mDstPath, mPaint);

                canvas.save();  //嗷~ 在这画小声波
                canvas.translate( - (1 - mFraction) * mBaseLength * 2, 0);
                canvas.drawArc(mCenterX - 0.5f * mBaseLength - mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY - mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterX - 0.5f * mBaseLength + mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY + mBaseLength / 0.5f * (mFraction - 0.5f), -55, 110 ,false, mBgPaint);
                int sVolume = mVolumeValue;
                if (sVolume > 50)
                    sVolume = 50;
                canvas.drawArc(mCenterX - 0.5f * mBaseLength - mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY - mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterX - 0.5f * mBaseLength + mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY + mBaseLength / 0.5f * (mFraction - 0.5f), -55 / 50 * sVolume, 110 / 50 * sVolume  ,false, mPaint);
                canvas.restore();

                canvas.save();  //嗷~ 在这画大声波
                canvas.translate( - (1 - mFraction) * mBaseLength * 3, 0);
                canvas.drawArc(mCenterX - 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY - 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterX + 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY + 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f), -55, 110 ,false, mBgPaint);
                int lVolume = mVolumeValue - 50;
                if (lVolume < 0)
                    lVolume = 0;
                canvas.drawArc(mCenterX - 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY - 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterX + 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f),
                        mCenterY + 1.6f * mBaseLength / 0.5f * (mFraction - 0.5f), -55 / 50 * lVolume, 110 / 50 * lVolume ,false, mPaint);
                canvas.restore();
            }
        } else {
            mDstPath.reset();   //嗷~ 在这画喇叭
            mPathMeasure.getSegment(0, mPathLength * 0.38f, mDstPath, true);
            canvas.drawPath(mDstPath, mPaint);
            mDstPath.reset();
            mPathMeasure.getSegment(mPathLength - mPathLength * 0.38f, mPathLength, mDstPath, true);
            canvas.drawPath(mDstPath, mPaint);

            canvas.save();  //嗷~ 在这画小声波
            canvas.translate( - (1 - mFraction) * mBaseLength / 5 * 2 * mVolumeValue / 100, 0);
            canvas.drawArc(mCenterX - 1.5f * mBaseLength,
                    mCenterY - mBaseLength,
                    mCenterX + 0.5f * mBaseLength,
                    mCenterY + mBaseLength, -55, 110 ,false, mBgPaint);
            int sVolume = mVolumeValue;
            if (sVolume > 50)
                sVolume = 50;
            canvas.drawArc(mCenterX - 1.5f * mBaseLength,
                    mCenterY - mBaseLength,
                    mCenterX + 0.5f * mBaseLength,
                    mCenterY + mBaseLength, -55 / 50 * sVolume, 110 / 50 * sVolume  ,false, mPaint);
            canvas.restore();

            canvas.save();  //嗷~ 在这画大声波
            canvas.translate( - (1 - mFraction) * mBaseLength / 5 * 2 * mVolumeValue / 100, 0);
            canvas.drawArc(mCenterX - 1.6f * mBaseLength,
                    mCenterY - 1.6f * mBaseLength,
                    mCenterX + 1.6f * mBaseLength,
                    mCenterY + 1.6f * mBaseLength, -55, 110 ,false, mBgPaint);
            int lVolume = mVolumeValue - 50;
            if (lVolume < 0)
                lVolume = 0;
            canvas.drawArc(mCenterX - 1.6f * mBaseLength,
                    mCenterY - 1.6f * mBaseLength,
                    mCenterX + 1.6f * mBaseLength,
                    mCenterY + 1.6f * mBaseLength, -55 / 50 * lVolume, 110 / 50 * lVolume ,false, mPaint);
            canvas.restore();
        }
    }

    private void closeVolume() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.f, 100.f);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction = 1 - valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    private void openVolume() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.f, 100.f);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new LinearInterpolator());
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
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mVolumeValue == 0) {
                    closeVolume();
                } else {
                    startVibration();
                }
            }
        });
    }

    private void startVibration() {
        mCurrentState = STATE_VIBRATE;
        if (!vibrateAnim.isRunning()) {
            vibrateAnim.start();
        }
    }

    private void stopVibration() {
        if (vibrateAnim.isRunning()) {
            vibrateAnim.cancel();
        }
    }

    public void updateVolumeValue(int value) {
        if (value < 0 || value > 100) {
            return;
        }
        mVolumeValue = value;
        if (value == 0 && mCurrentState != STATE_SILENT) {
            mCurrentState = STATE_SILENT;
            stopVibration();
            closeVolume();
        } else if (value != 0 && mCurrentState == STATE_SILENT){
            mCurrentState = STATE_VOLUME;
            openVolume();
        }
    }
}
