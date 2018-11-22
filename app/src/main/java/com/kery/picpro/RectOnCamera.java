package com.kery.picpro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/10/24.
 */

public class RectOnCamera extends View {
    private static final String TAG = "CameraSurfaceView";
    private int mScreenWidth;
    private int mScreenHeight;
    private Paint mPaint;
    private RectF mRectF;
    // 圆
    private Point centerPoint;
    private int radio;
    private RectF mRectF_1, mRectF_2, mRectF_3, mRectF_4;
    float strokeW = 15f;
    private Context mContext;

    public RectOnCamera(Context context) {
        this(context, null);
    }

    public RectOnCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectOnCamera(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenMetrix(context);
        initView(context);
        mContext = context;
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
        Log.e(TAG, mScreenHeight + "<----->" + mScreenWidth);
    }

    private void initView(Context context) {
        int marginLeft = (int) (mScreenWidth * 0.15);
        int marginRight = (int) (mScreenWidth * 0.2);
        int marginTop = (int) (mScreenHeight * 0.18);
        mRectF = new RectF(marginLeft, marginTop, mScreenWidth - marginRight, mScreenHeight - marginTop);

        mRectF_1 = new RectF(0, 0, mScreenWidth, marginTop);
        mRectF_2 = new RectF(0, mScreenHeight - marginTop, mScreenWidth, mScreenHeight);
        mRectF_3 = new RectF(0, marginTop, marginLeft, mScreenHeight - marginTop);
        mRectF_4 = new RectF(mScreenWidth - marginRight, marginTop, mScreenWidth, mScreenHeight - marginTop);

        centerPoint = new Point(mScreenWidth - marginRight / 2, mScreenHeight / 2);
        radio = (int) (marginRight * 0.2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();

        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(180);
        //画边框
        canvas.drawRect(mRectF_1, mPaint);
        canvas.drawRect(mRectF_2, mPaint);
        canvas.drawRect(mRectF_3, mPaint);
        canvas.drawRect(mRectF_4, mPaint);

        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(strokeW);
//        canvas.drawRect(mRectF, mPaint);
        canvas.drawLine(mRectF.left - strokeW / 2, mRectF.top, mRectF.left + 120, mRectF.top, mPaint);
        canvas.drawLine(mRectF.left, mRectF.top, mRectF.left, mRectF.top + 120, mPaint);

        canvas.drawLine(mRectF.right - 120, mRectF.top, mRectF.right + strokeW / 2, mRectF.top, mPaint);
        canvas.drawLine(mRectF.right, mRectF.top, mRectF.right, mRectF.top + 120, mPaint);

        canvas.drawLine(mRectF.left - strokeW / 2, mRectF.bottom, mRectF.left + 120, mRectF.bottom, mPaint);
        canvas.drawLine(mRectF.left, mRectF.bottom, mRectF.left, mRectF.bottom - 120, mPaint);

        canvas.drawLine(mRectF.right - 120, mRectF.bottom, mRectF.right + strokeW / 2, mRectF.bottom, mPaint);
        canvas.drawLine(mRectF.right, mRectF.bottom, mRectF.right, mRectF.bottom - 120, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);// 空心
        mPaint.setColor(Color.WHITE);
        Log.e(TAG, "onDraw");
        mPaint.setStrokeWidth(strokeW / 2);
        canvas.drawCircle(centerPoint.x, centerPoint.y, radio, mPaint);// 外圆

        mPaint.setStyle(Paint.Style.FILL);// 空心
        canvas.drawCircle(centerPoint.x, centerPoint.y, radio - strokeW, mPaint); // 内圆

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inMutable = true;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.light_off, options);
//        canvas.drawBitmap(bitmap, mRectF_4.centerX() - mRectF_4.width() / 3.5f, mRectF_4.top - mRectF_4.width() / 3, mPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() > centerPoint.x - radio &&
                event.getX() < centerPoint.x + radio &&
                event.getY() > centerPoint.y - radio &&
                event.getY() < centerPoint.y + radio) {
            Log.e(TAG, "点击了按钮了");
            mTakePicLsn.takePicture();
//            customCameraPreview.takePhoto(jpeg);
        } else if (event.getX() > mRectF.left &&
                event.getX() < mRectF.right &&
                event.getY() > mRectF.top &&
                event.getY() < mRectF.bottom) {
            Log.e(TAG, "开始聚焦");
            customCameraPreview.focus();
        }
        return super.onTouchEvent(event);
    }

    public void setPicListener(TakePicLsn lsn) {
        this.mTakePicLsn = lsn;
    }

    private TakePicLsn mTakePicLsn;

    public interface TakePicLsn {
        void takePicture();
    }


    CustomCameraPreview customCameraPreview;

    public void setCamera(CustomCameraPreview c) {
        this.customCameraPreview = c;
    }
}
