package moe.codeest.enviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by codeest on 2016/11/15.
 *
 * 我还在好奇里面装的究竟是啤酒还是橙汁 0v0
 */

public class ENLoadingView extends View {

    private static final int STATE_SHOW = 0;

    private static final int STATE_HIDE = 1;

    private static final int DEFAULT_RIPPLE_SPEED = 2;

    private Paint mPaint[], mBeerPaint[], mBubblePaint[];

    private Path mPath, mBgPath;

    private ValueAnimator valueAnim;

    private boolean isItemReady[];

    private float mFraction[];

    private int mCurrentState;

    private float mWidth, mHeight;

    private float mCenterX, mCenterY;

    private float mBaseLength;
    private float mBgBaseLength;
    private float mCurrentRippleX[];

    public ENLoadingView(Context context) {
        super(context);
    }

    public ENLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint[4];
        mBeerPaint = new Paint[4];
        mBubblePaint = new Paint[4];
        isItemReady = new boolean[4];

        for (int i = 0; i< 4 ; i++) {
            mPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint[i].setStyle(Paint.Style.STROKE);
            mPaint[i].setStrokeCap(Paint.Cap.ROUND);
            mPaint[i].setStrokeJoin(Paint.Join.ROUND);
            mPaint[i].setColor(Color.parseColor("#f0cc36"));
            mPaint[i].setStrokeWidth(9);

            mBeerPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBeerPaint[i].setStyle(Paint.Style.FILL);
            mBeerPaint[i].setColor(Color.parseColor("#fbce0f"));

            mBubblePaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBubblePaint[i].setStyle(Paint.Style.FILL);
            mBubblePaint[i].setColor(Color.parseColor("#f5fba1"));
        }

        valueAnim = ValueAnimator.ofFloat(1.f, 100.f);
        valueAnim.setDuration(3000);
        valueAnim.setInterpolator(new LinearInterpolator());
        valueAnim.setRepeatCount(ValueAnimator.INFINITE);
        valueAnim.setRepeatMode(ValueAnimator.RESTART);
        valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                for (int i = 0;i < 4 ; i++) {
                    float temp = valueAnimator.getAnimatedFraction() - i * 0.25f;
                    if (temp < 0)
                        temp += 1;
                    mFraction[i] = temp;
                    if (mFraction[0] > i * 0.25f && !isItemReady[i]) {
                        isItemReady[i] = true;
                    }
                }
                invalidate();
            }
        });

        mCurrentState = STATE_HIDE;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = w / 2;
        mCenterY = h / 2;
        mBaseLength = w / 10;
        mBgBaseLength = w / 8;
        mFraction = new float[4];
        mCurrentRippleX = new float[4];
        for (int i = 0; i < 4 ;i++) {
            mCurrentRippleX[i] = - 2 * mBgBaseLength;
        }
        mPath = new Path();
        mPath.moveTo(0, mCenterY + 2 * mBaseLength);
        mPath.lineTo(0, mCenterY);
        mPath.lineTo(mBaseLength / 4, mCenterY - mBaseLength);
        mPath.lineTo(mBaseLength / 4, mCenterY - 1.5f * mBaseLength);
        mPath.lineTo(mBaseLength * 3 / 4, mCenterY - 1.5f * mBaseLength);
        mPath.lineTo(mBaseLength * 3 / 4, mCenterY - mBaseLength);
        mPath.lineTo(mBaseLength, mCenterY);
        mPath.lineTo(mBaseLength, mCenterY + 2 * mBaseLength);
        mPath.close();

        mBgPath = new Path();

        setAlpha(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 4 ; i++) {
            drawItem(canvas, mFraction[i], i);
        }
    }

    private void drawItem(Canvas canvas, float mFraction, int index) {
        if (!isItemReady[index]) {
            return;
        }
        canvas.save();
        canvas.translate(mFraction * mWidth ,0);
        float mCurrentY;
        if (mFraction < 0.1) {  //嗷~ 从这开始画封口器以及确定波浪线高度
            mBeerPaint[index].setAlpha((int) (255 * 10 * mFraction));
            mPaint[index].setAlpha((int) (255 * 10 * mFraction));
            mBubblePaint[index].setAlpha((int) (255 * 10 * mFraction));
            mCurrentY = mCenterY + 2.2f * mBaseLength;
            canvas.drawLine(0.1f * mBaseLength ,mCenterY - 2.5f * mBaseLength, 0.9f * mBaseLength, mCenterY - 2.5f * mBaseLength, mPaint[index]);
        } else if(mFraction > 0.7) {
            mBeerPaint[index].setAlpha((int) (255 / 0.3f * (1 - mFraction)));
            mPaint[index].setAlpha((int) (255  / 0.3f * (1 - mFraction)));
            mBubblePaint[index].setAlpha((int) (255  / 0.3f * (1 - mFraction)));
            mCurrentY = mCenterY - mBaseLength;
            if (mFraction <= 0.75) {
                canvas.drawLine(mBaseLength * 0.5f, mCenterY - 2.5f * mBaseLength,
                        mBaseLength * 0.5f, mCenterY - 2.5f * mBaseLength + mBaseLength / 0.05f * (mFraction - 0.7f), mPaint[index]);
                canvas.drawLine(0.1f * mBaseLength ,mCenterY - 2.5f * mBaseLength + mBaseLength / 0.05f * (mFraction - 0.7f),
                        0.9f * mBaseLength, mCenterY - 2.5f * mBaseLength + mBaseLength / 0.05f * (mFraction - 0.7f), mPaint[index]);
            } else {
                if (mFraction < 0.8) {
                    canvas.drawLine(mBaseLength * 0.5f, mCenterY - 2.5f * mBaseLength,
                            mBaseLength * 0.5f, mCenterY - 1.5f * mBaseLength - mBaseLength / 0.05f * (mFraction - 0.75f), mPaint[index]);
                }
                canvas.drawLine(mBaseLength / 4 - 6, mCenterY - 1.5f * mBaseLength - 6,
                        mBaseLength * 3 / 4 + 6, mCenterY - 1.5f * mBaseLength - 6, mPaint[index]);
            }
        } else {
            mCurrentY = mCenterY + 2.2f * mBaseLength - 3.2f * mBaseLength / 0.6f * (mFraction - 0.1f);
            canvas.drawLine(0.1f * mBaseLength ,mCenterY - 2.5f * mBaseLength, 0.9f * mBaseLength, mCenterY - 2.5f * mBaseLength, mPaint[index]);
        }
        canvas.drawPath(mPath, mPaint[index]);  //嗷~ 在这画酒瓶
        mCurrentRippleX[index] += DEFAULT_RIPPLE_SPEED;
        if (mCurrentRippleX[index] >= 0) {
            mCurrentRippleX[index] = - 2 * mBgBaseLength;
        }
        mBgPath.reset();
        mBgPath.moveTo(mCurrentRippleX[index], mCurrentY);
        for (int i = 0; i< 9 ; i++) {
            mBgPath.rQuadTo(mBgBaseLength / 2, mBgBaseLength / 8, mBgBaseLength, 0);
            mBgPath.rQuadTo(mBgBaseLength / 2, - mBgBaseLength / 8, mBgBaseLength, 0);
        }
        mBgPath.lineTo(mWidth, mHeight);
        mBgPath.lineTo(0, mHeight);
        mBgPath.close();
        canvas.clipPath(mPath);
        canvas.save();
        canvas.translate(- mBaseLength / 12, - mBaseLength / 10 - mBaseLength / 4 * (1 - mFraction));
        canvas.drawPath(mBgPath, mBubblePaint[index]);  //嗷~ 在这画啤酒沫
        canvas.restore();
        canvas.drawPath(mBgPath, mBeerPaint[index]);    //嗷~ 在这画啤酒
        canvas.restore();
    }

    public void show() {
        if (mCurrentState == STATE_SHOW) {
            return;
        }
        mCurrentState = STATE_SHOW;
        this.animate().alpha(1).setDuration(200).start();
        valueAnim.start();
    }

    public void hide() {
        if (mCurrentState == STATE_HIDE) {
            return;
        }
        mCurrentState = STATE_HIDE;
        this.animate().alpha(0).setDuration(200).start();
        valueAnim.cancel();
        resetData();
    }

    private void resetData() {
        for (int i = 0; i<4 ; i++) {
            isItemReady[i] = false;
            mCurrentRippleX[i] = - 2 * mBgBaseLength;
        }
    }
}