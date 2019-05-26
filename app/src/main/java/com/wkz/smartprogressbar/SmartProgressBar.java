package com.wkz.smartprogressbar;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的进度条
 * 样式风格有水平、竖直、圆环、扇形、......
 *
 * @author wkz
 * @date 2019/05/05 21:23
 */
public class SmartProgressBar extends View {


    private static final float DEFAULT_WIDTH = 100F;
    private static final float DEFAULT_HEIGHT = 100F;
    private static final int DEFAULT_PROGRESS_COLOR = Color.BLUE;
    private static final int DEFAULT_PROGRESS_BAR_BG_COLOR = Color.WHITE;
    private static final int DEFAULT_PERCENT_TEXT_COLOR = Color.BLACK;
    private static final float DEFAULT_PERCENT_TEXT_SIZE = 15F;
    private static final int DEFAULT_BORDER_COLOR = Color.RED;
    private static final int DEFAULT_MAX = 100;


    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ShapeStyle {
        /**
         * 水平样式
         */
        int HORIZONTAL = 0;
        /**
         * 竖直样式
         */
        int VERTICAL = 1;
        /**
         * 圆环样式
         */
        int RING = 2;
        /**
         * 扇形样式
         */
        int SECTOR = 3;
    }


    /**
     * 进度条背景颜色
     **/
    private int mProgressBarBgColor = DEFAULT_PROGRESS_BAR_BG_COLOR;
    /**
     * 进度颜色
     */
    private int mProgressStartColor = DEFAULT_PROGRESS_COLOR;
    private int mProgressCenterColor = DEFAULT_PROGRESS_COLOR;
    private int mProgressEndColor = DEFAULT_PROGRESS_COLOR;
    /**
     * 边框颜色
     */
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    /**
     * 边框宽度
     */
    private float mBorderWidth;
    /**
     * 进度提示文字大小
     **/
    private float mPercentTextSize = DEFAULT_PERCENT_TEXT_SIZE;
    /**
     * 进度提示文字颜色
     **/
    private int mPercentTextColor = DEFAULT_PERCENT_TEXT_COLOR;
    /**
     * 进度条中心X坐标
     **/
    private float mCenterX;
    /**
     * 进度条中心Y坐标
     **/
    private float mCenterY;
    /**
     * 进度条样式
     **/
    private int mShapeStyle = ShapeStyle.HORIZONTAL;
    /**
     * 圆环/扇形圆角半径
     **/
    private float mRadius;
    /**
     * 水平、竖直进度条圆角半径
     */
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;
    private float[] mRadii;
    /**
     * 进度最大值
     **/
    private int mMax = DEFAULT_MAX;
    /**
     * 进度值
     **/
    private int mProgress;
    /**
     * 进度文字是否显示百分号
     **/
    private boolean mIsShowPercentSign;
    /**
     * 进度文字是否显示
     */
    private boolean mIsShowPercentText;
    /**
     * 进度画笔
     */
    private Paint mProgressPaint;
    /**
     * 进度条背景画笔
     */
    private Paint mProgressBarBgPaint;
    /**
     * 进度百分比字体画笔
     */
    private Paint mPercentTextPaint;
    /**
     * 边框画笔
     */
    private Paint mBorderPaint;
    /**
     * 绘制路径
     */
    private Path mPath;

    public SmartProgressBar(Context context) {
        this(context, null);
    }

    public SmartProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            initAttributes(context, attrs, defStyleAttr);
        }
        init();
    }

    /**
     * 初始化自定义属性
     */
    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SmartProgressBar, defStyleAttr, 0);
        try {
            mMax = attributes.getInteger(R.styleable.SmartProgressBar_spb_max, DEFAULT_MAX);
            mProgress = attributes.getInteger(R.styleable.SmartProgressBar_spb_progress, 0);
            mProgressStartColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_start_color, DEFAULT_PROGRESS_COLOR);
            mProgressCenterColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_center_color, DEFAULT_PROGRESS_COLOR);
            mProgressEndColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_end_color, DEFAULT_PROGRESS_COLOR);
            mProgressBarBgColor = attributes.getColor(R.styleable.SmartProgressBar_spb_progress_bar_bg_color, DEFAULT_PROGRESS_BAR_BG_COLOR);
            mIsShowPercentText = attributes.getBoolean(R.styleable.SmartProgressBar_spb_show_percent_text, false);
            mIsShowPercentSign = attributes.getBoolean(R.styleable.SmartProgressBar_spb_show_percent_sign, false);
            mPercentTextColor = attributes.getColor(R.styleable.SmartProgressBar_spb_percent_text_color, DEFAULT_PERCENT_TEXT_COLOR);
            mPercentTextSize = attributes.getDimension(R.styleable.SmartProgressBar_spb_percent_text_size, DEFAULT_PERCENT_TEXT_SIZE);
            mBorderColor = attributes.getColor(R.styleable.SmartProgressBar_spb_border_color, DEFAULT_BORDER_COLOR);
            mBorderWidth = attributes.getDimension(R.styleable.SmartProgressBar_spb_border_width, 0);
            mRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_radius, 0);
            mTopLeftRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_top_left_radius, 0);
            mTopRightRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_top_right_radius, 0);
            mBottomLeftRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_bottom_left_radius, 0);
            mBottomRightRadius = attributes.getDimension(R.styleable.SmartProgressBar_spb_bottom_right_radius, 0);
            mShapeStyle = attributes.getInt(R.styleable.SmartProgressBar_spb_shape_style, 0);
        } finally {
            attributes.recycle();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        /*进度画笔*/
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL);

        /*进度条背景画笔*/
        mProgressBarBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressBarBgPaint.setColor(mProgressBarBgColor);

        /*边框画笔*/
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        /*进度百分比字体画笔*/
        mPercentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPercentTextPaint.setColor(mPercentTextColor);
        mPercentTextPaint.setStyle(Paint.Style.FILL);
        mPercentTextPaint.setTextSize(mPercentTextSize);

        /*若是设置了radius属性，四个圆角属性值以radius属性值为准*/
        if (mRadius > 0) {
            mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = mRadius;
        }

        /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
        mRadii = new float[]{mTopLeftRadius, mTopLeftRadius, mTopRightRadius, mTopRightRadius,
                mBottomRightRadius, mBottomRightRadius, mBottomLeftRadius, mBottomLeftRadius};

        /*绘制路径*/
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (widthSpecMode == MeasureSpec.AT_MOST
                || widthSpecMode == MeasureSpec.UNSPECIFIED) {
            width = dp2px(getContext(), DEFAULT_WIDTH);
        } else {
            width = widthSpecSize;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            height = dp2px(getContext(), DEFAULT_HEIGHT);
        } else {
            height = heightSpecSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = (float) getWidth() / 2;
        mCenterY = (float) getHeight() / 2;

        if (mShapeStyle == ShapeStyle.HORIZONTAL) {
            /*创建线性颜色渐变器*/
            Shader linearGradient = new LinearGradient(
                    mBorderWidth,
                    (getHeight() - mBorderWidth * 2) / 2,
                    mBorderWidth + (float) mProgress / mMax * (getWidth() - mBorderWidth * 2),
                    (getHeight() - mBorderWidth * 2) / 2,
                    new int[]{mProgressStartColor, mProgressCenterColor, mProgressEndColor},
                    new float[]{0, 0.5f, 1}, Shader.TileMode.MIRROR
            );

            mProgressPaint.setShader(linearGradient);
        } else if (mShapeStyle == ShapeStyle.VERTICAL) {
            /*创建线性颜色渐变器*/
            Shader linearGradient = new LinearGradient(
                    (getWidth() - mBorderWidth * 2) / 2,
                    getHeight() - mBorderWidth,
                    (getWidth() - mBorderWidth * 2) / 2,
                    getHeight() - (float) mProgress / mMax * (getHeight() - mBorderWidth * 2) - mBorderWidth,
                    new int[]{mProgressStartColor, mProgressCenterColor, mProgressEndColor},
                    new float[]{0, 0.5f, 1}, Shader.TileMode.MIRROR
            );
            mProgressPaint.setShader(linearGradient);
        } else if (mShapeStyle == ShapeStyle.RING) {
            /*创建扫描式渐变器*/
            int[] colors = new int[]{
                    mProgressStartColor,
                    mProgressCenterColor,
                    mProgressEndColor,
                    mProgressStartColor
            };
            float[] positions = new float[]{0, 0.45F, 0.9F, 1F};
            Shader sweepGradient = new SweepGradient(
                    0,
                    0,
                    colors,
                    positions
            );

            mProgressPaint.setShader(sweepGradient);

            Matrix gradientMatrix = new Matrix();
            gradientMatrix.setTranslate(mCenterX, mCenterY);
            sweepGradient.setLocalMatrix(gradientMatrix);
        }
        if (mShapeStyle == ShapeStyle.SECTOR) {
            /*创建扫描式渐变器*/
            Shader sweepGradient = new SweepGradient(
                    0,
                    0,
                    new int[]{mProgressStartColor, mProgressCenterColor, mProgressEndColor},
                    null
            );

            mProgressPaint.setShader(sweepGradient);

            Matrix gradientMatrix = new Matrix();
            gradientMatrix.setTranslate(mCenterX, mCenterY);
            sweepGradient.setLocalMatrix(gradientMatrix);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (mShapeStyle == ShapeStyle.HORIZONTAL) {
            drawHorizontalProgressBar(canvas);
        } else if (mShapeStyle == ShapeStyle.VERTICAL) {
            drawVerticalProgressBar(canvas);
        } else if (mShapeStyle == ShapeStyle.RING) {
            drawRingProgressBar(canvas);
        } else if (mShapeStyle == ShapeStyle.SECTOR) {
            drawSectorProgressBar(canvas);
        }
        canvas.restore();
        mPath.reset();
        super.onDraw(canvas);
    }

    /**
     * 绘制水平进度条
     *
     * @param canvas 画布
     */
    private void drawHorizontalProgressBar(Canvas canvas) {
        // 绘制进度条背景
        mPath.addRoundRect(new RectF(
                        mBorderWidth / 2,
                        mBorderWidth / 2,
                        getWidth() - mBorderWidth / 2,
                        getHeight() - mBorderWidth / 2),
                mRadii, Path.Direction.CW);
        canvas.drawPath(mPath, mProgressBarBgPaint);

        // 绘制边框
        if (mBorderWidth > 0) {
            canvas.drawPath(mPath, mBorderPaint);
        }


        // 绘制进度
        mPath.reset();
        mPath.addRoundRect(
                new RectF(
                        mBorderWidth,
                        mBorderWidth,
                        mBorderWidth + (float) mProgress / mMax * (getWidth() - mBorderWidth * 2),
                        getHeight() - mBorderWidth
                ),
                mRadii,
                Path.Direction.CW
        );
        canvas.drawPath(mPath, mProgressPaint);


        if (mIsShowPercentText) {
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制竖直进度条
     *
     * @param canvas 画布
     */
    private void drawVerticalProgressBar(Canvas canvas) {
        // 绘制进度条背景
        mPath.addRoundRect(new RectF(
                        mBorderWidth / 2,
                        mBorderWidth / 2,
                        getWidth() - mBorderWidth / 2,
                        getHeight() - mBorderWidth / 2),
                mRadii, Path.Direction.CW);
        canvas.drawPath(mPath, mProgressBarBgPaint);

        // 绘制边框
        if (mBorderWidth > 0) {
            canvas.drawPath(mPath, mBorderPaint);
        }

        // 绘制进度
        mPath.reset();
        mPath.addRoundRect(new RectF(
                        mBorderWidth,
                        getHeight() - (float) mProgress / mMax * (getHeight() - mBorderWidth * 2) - mBorderWidth,
                        getWidth() - mBorderWidth,
                        getHeight() - mBorderWidth),
                mRadii, Path.Direction.CW);
        canvas.drawPath(mPath, mProgressPaint);

        if (mIsShowPercentText) {
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制圆环进度条
     *
     * @param canvas 画布
     */
    private void drawRingProgressBar(Canvas canvas) {
        float strokeWidth = mCenterX - mRadius - mBorderWidth;
        // 绘制进度条背景
        mProgressBarBgPaint.setStyle(Paint.Style.STROKE);
        mProgressBarBgPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(
                mCenterX,
                mCenterY,
                mCenterX - mBorderWidth - strokeWidth / 2,
                mProgressBarBgPaint
        );

        // 绘制边框
        if (mBorderWidth > 0) {
            mPath.addCircle(mCenterX, mCenterY, mCenterX - mBorderWidth / 2, Path.Direction.CW);
            canvas.drawPath(mPath, mBorderPaint);
        }

        // 绘制进度
        mPath.reset();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeJoin(Paint.Join.ROUND);
        mProgressPaint.setDither(true);
        mProgressPaint.setStrokeWidth(strokeWidth);
        // 逆时针旋转画布90度
        canvas.rotate(-90, mCenterX, mCenterY);
        RectF oval = new RectF(
                mBorderWidth + strokeWidth / 2,
                mBorderWidth + strokeWidth / 2,
                getWidth() - mBorderWidth - strokeWidth / 2,
                getHeight() - mBorderWidth - strokeWidth / 2
        );
        mPath.addArc(oval, 0, 360 * mProgress / (float) mMax);
        canvas.drawPath(mPath, mProgressPaint);

        if (mIsShowPercentText) {
            // 顺时针旋转画布90度
            canvas.rotate(90, mCenterX, mCenterY);
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制扇形扫描式进度
     *
     * @param canvas 画布
     */
    private void drawSectorProgressBar(Canvas canvas) {
        // 绘制进度条背景
        canvas.drawCircle(mCenterX, mCenterY, mCenterX - mBorderWidth, mProgressBarBgPaint);

        // 绘制边框
        if (mBorderWidth > 0) {
            mPath.addCircle(mCenterX, mCenterY, mCenterX - mBorderWidth / 2, Path.Direction.CW);
            canvas.drawPath(mPath, mBorderPaint);
        }

        // 绘制进度
        // 逆时针旋转画布90度
        canvas.rotate(-90, mCenterX, mCenterY);
        RectF oval = new RectF(
                mBorderWidth,
                mBorderWidth,
                getWidth() - mBorderWidth,
                getHeight() - mBorderWidth
        );
        canvas.drawArc(oval, 0, 360 * mProgress / (float) mMax, true, mProgressPaint);

        if (mIsShowPercentText) {
            // 顺时针旋转画布90度
            canvas.rotate(90, mCenterX, mCenterY);
            // 绘制进度文字和进度百分比符号
            drawPercentText(canvas);
        }
    }

    /**
     * 绘制进度文字和进度百分比符号
     *
     * @param canvas 画布
     */
    private void drawPercentText(Canvas canvas) {
        String percent = String.valueOf(mProgress * 100 / mMax);
        if (mIsShowPercentSign) {
            percent = percent + "%";
        }

        Rect rect = new Rect();
        // 获取字符串的宽高值
        mPercentTextPaint.getTextBounds(percent, 0, percent.length(), rect);
        float textWidth = rect.width();
        float textHeight = rect.height();
        if (textWidth >= getWidth()) {
            textWidth = getWidth();
        }
        if (textHeight >= getHeight()) {
            textHeight = getHeight();
        }
        canvas.drawText(percent, mCenterX - textWidth / 2, mCenterY + textHeight / 2, mPercentTextPaint);
    }

    public SmartProgressBar setProgress(int progress) {
        if (progress > mMax) {
            this.mProgress = mMax;
        } else if (progress < 0) {
            this.mProgress = 0;
        } else {
            this.mProgress = progress;
        }
        postInvalidate();
        return this;
    }

    public SmartProgressBar setMax(int max) {
        this.mMax = max;
        return this;
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    private int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
