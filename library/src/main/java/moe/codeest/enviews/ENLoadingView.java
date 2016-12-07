package moe.codeest.enviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by codeest on 2016/11/15.
 *
 * 我还在好奇里面装的究竟是啤酒还是橙汁 0v0
 */

public class ENLoadingView extends SurfaceView {

    private static final int STATE_SHOW = 0;

    private static final int STATE_HIDE = 1;

    private static final int DEFAULT_RIPPLE_SPEED = 2;

    private static final float DEFAULT_MOVE_SPEED = 0.01f;

    private Paint mPaint[], mBeerPaint[], mBubblePaint[];

    private Path mPath, mBgPath;

    private Thread mThread;

    private boolean isItemReady[];

    private float mCurrentRippleX[];

    private float mFraction[];

    private float mTemp = 0;

    private int mCurrentState;

    private float mWidth, mHeight;

    private float mCenterX, mCenterY;

    private SurfaceHolder surfaceHolder;

    private float mBaseLength, mBgBaseLength;

    public ENLoadingView(Context context) {
        super(context);
        init();
    }

    public ENLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint[4];
        mBeerPaint = new Paint[4];
        mBubblePaint = new Paint[4];
        mPath = new Path();
        mBgPath = new Path();
        isItemReady = new boolean[4];
        mFraction = new float[4];
        mCurrentRippleX = new float[4];
        mCurrentState = STATE_HIDE;

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
        surfaceHolder = getHolder();
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if (mThread != null) {
                    mThread.interrupt();
                    mThread = null;
                }
            }
        });
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
        for (int i = 0; i < 4 ;i++) {
            mCurrentRippleX[i] = - 2 * mBgBaseLength;
        }

        mPath.reset();
        mPath.moveTo(0, mCenterY + 2 * mBaseLength);
        mPath.lineTo(0, mCenterY);
        mPath.lineTo(mBaseLength / 4, mCenterY - mBaseLength);
        mPath.lineTo(mBaseLength / 4, mCenterY - 1.5f * mBaseLength);
        mPath.lineTo(mBaseLength * 3 / 4, mCenterY - 1.5f * mBaseLength);
        mPath.lineTo(mBaseLength * 3 / 4, mCenterY - mBaseLength);
        mPath.lineTo(mBaseLength, mCenterY);
        mPath.lineTo(mBaseLength, mCenterY + 2 * mBaseLength);
        mPath.close();
    }

    private Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (mCurrentState == STATE_SHOW) {
                    Thread.sleep(5);
                    flush();
                    draw();
                }
                if (mCurrentState == STATE_HIDE) {
                    clearCanvas();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void flush() {
        if (mTemp >= 1)
            mTemp = 0;
        mTemp += DEFAULT_MOVE_SPEED;
        for (int i = 0;i < 4 ; i++) {
            float temp = mTemp - i * 0.25f;
            if (temp < 0)
                temp += 1;
            mFraction[i] = temp;
            if (mFraction[0] > i * 0.25f && !isItemReady[i]) {
                isItemReady[i] = true;
            }
        }
    }

    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas == null)
            return;
        mPaint[0].setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));  //使用这只画笔清屏，清屏后恢复画笔
        canvas.drawPaint(mPaint[0]);
        mPaint[0].setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_OVER));
        for (int i = 0; i < 4 ; i++) {
            drawItem(canvas, mFraction[i], i);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
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
        mThread = new Thread(animRunnable);
        mThread.start();
    }

    public void hide() {
        if (mCurrentState == STATE_HIDE) {
            return;
        }
        mCurrentState = STATE_HIDE;
        resetData();
    }

    private void clearCanvas() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas == null)
            return;
        mPaint[0].setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
        canvas.drawPaint(mPaint[0]);
        mPaint[0].setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_OVER));
        surfaceHolder.unlockCanvasAndPost(canvas);
        mThread.interrupt();
        mThread = null;
    }

    private void resetData() {
        for (int i = 0; i<4 ; i++) {
            isItemReady[i] = false;
            mCurrentRippleX[i] = - 2 * mBgBaseLength;
        }
        mTemp = 0;
    }
}