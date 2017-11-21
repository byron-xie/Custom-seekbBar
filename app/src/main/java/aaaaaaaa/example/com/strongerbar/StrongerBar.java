package aaaaaaaa.example.com.strongerbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import aaaaaaaa.example.com.strongerbar.R;

import static aaaaaaaa.example.com.strongerbar.R.styleable.StrongerBarOrientation;


public class StrongerBar extends View {
    private int mBackgroundColor = 0xffffffff;
    private int[] mColorSeeds = new int[]{0xFF000000, 0xFF9900FF, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFF6600, 0xFFFFFF00, 0xFFFFFFFF, 0xFF000000};
    private int c0, c1, mAlpha, mRed, mGreen, mBlue;
    private float x, y;
    private OnStateChangeListener mOnStateChangeListener;
    private Context mContext;
    private boolean mIsShowAlphaBar = false;
    private boolean mIsVertical;
    private boolean mMovingColorBar;
    private boolean mMovingAlphaBar;
    private Bitmap mTransparentBitmap;
    private Rect mColorRect;
    private int mThumbHeight = 20;
    private float mThumbRadius;
    private int mBarHeight = 2;
    private LinearGradient mColorGradient;
    private Paint mColorRectPaint;
    private int realLeft;
    private int realRight;
    private int realTop;
    private int realBottom;
    private int mBarWidth;
    private int mMaxPosition;
    private Rect mAlphaRect;
    private int mCurrentPosition;
    private int mAlphaBarPosition;
    private int mBarMargin = 0;
    private int mPaddingSize;
    private int mViewWidth;
    private int mViewHeight;
    private int mAlphaMinPosition = 0;
    private int mAlphaMaxPosition = 255;
    private List<Integer> mColors = new ArrayList<>();
    private int mColorsToInvoke = -1;
    private boolean mInit = false;
    private boolean mFirstDraw = true;
    private static Matrix mMatrix;

    private OnInitDoneListener mOnInitDoneListener;
    private Rect mColorStroke;
    private boolean mOffSet;
    private boolean mOffSetNoPadding;
    private int mSencondColor;
    private String mTextFacePath;
    private int mFrameColor;
    private int mFrameWidth;
    private int mBubbleMargin;
    private int mBubbleTextSize;
    private Paint mColorPaint;
    private Rect mBubbleBounds;
    private boolean mIsShowBubble;
    private int mOrientation;
    private static final int mOrientationLeft = 0;
    private  static final int mOrientationRight = 1;
    private boolean mColorChangeCallBack;
    private String mAdjustName;
    private int mAdjustNameMargin;
    private int mAdjustNameSize;


    public StrongerBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public StrongerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public StrongerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StrongerBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        applyStyle(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyStyle(int resId) {
        applyStyle(getContext(), null, 0, resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = widthMeasureSpec;
        mViewHeight = heightMeasureSpec;
        int widthSpeMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpeMode = MeasureSpec.getMode(heightMeasureSpec);
        int barHeight = mIsShowAlphaBar ? mBarHeight * 2 : mBarHeight;
        int thumbHeight = mIsShowAlphaBar ? mThumbHeight * 2 : mThumbHeight;
        if(isVertical()){
            if (widthSpeMode == MeasureSpec.AT_MOST || widthSpeMode == MeasureSpec.UNSPECIFIED) {
                mViewWidth = thumbHeight + barHeight + mBarMargin;
                setMeasuredDimension(mViewWidth, mViewHeight);
            }

        }else{
            if (widthSpeMode == MeasureSpec.AT_MOST || widthSpeMode == MeasureSpec.UNSPECIFIED) {
                mViewHeight = thumbHeight + barHeight + mBarMargin;
                setMeasuredDimension(mViewWidth, mViewHeight);
            }
        }
    }


    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        //get attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrongerBar, defStyleAttr, defStyleRes);
        int colorsId = a.getResourceId(R.styleable.StrongerBar_colorGradient, 0);
        mMaxPosition = a.getInteger(R.styleable.StrongerBar_maxPosition, 100);
        mCurrentPosition = a.getInteger(R.styleable.StrongerBar_currentPosition, 0);
        mAlphaBarPosition = a.getInteger(R.styleable.StrongerBar_alphaBarPosition, mAlphaMinPosition);
        mIsVertical = a.getBoolean(R.styleable.StrongerBar_isVertical, false);
        mIsShowAlphaBar = a.getBoolean(R.styleable.StrongerBar_showAlphaBar, false);
        mBackgroundColor = a.getColor(R.styleable.StrongerBar_bgColor, Color.TRANSPARENT);
        mSencondColor = a.getColor(R.styleable.StrongerBar_secondColor, Color.TRANSPARENT);

        mBarHeight = (int) a.getDimension(R.styleable.StrongerBar_barHeight, (float) dp2px(2));
        mThumbHeight = (int) a.getDimension(R.styleable.StrongerBar_thumbHeight, (float) dp2px(30));
        mBarMargin = (int) a.getDimension(R.styleable.StrongerBar_barMargin, (float) dp2px(0));
        mOffSet = a.getBoolean(R.styleable.StrongerBar_offset, false);
        mOffSetNoPadding = a.getBoolean(R.styleable.StrongerBar_offsetNoPadding, true);
        mIsShowBubble = a.getBoolean(R.styleable.StrongerBar_isShowBubble, false);
        mColorChangeCallBack = a.getBoolean(R.styleable.StrongerBar_ColorChangeCallBack, false);
        mTextFacePath = a.getString(R.styleable.StrongerBar_textFacePath);
        mTextFacePath = a.getString(R.styleable.StrongerBar_textFacePath);
        mFrameColor = a.getColor(R.styleable.StrongerBar_frameColor, Color.TRANSPARENT);
        mFrameWidth = a.getInt(R.styleable.StrongerBar_frameWidth, 0);
        mAdjustName = a.getString(R.styleable.StrongerBar_adjustName);
        mBubbleMargin = a.getInt(R.styleable.StrongerBar_bubbleMargin, 0);
        mAdjustNameSize = a.getInt(R.styleable.StrongerBar_adjustNameSize, 18);
        mBubbleTextSize = a.getInt(R.styleable.StrongerBar_bubbleTextSize, 0);
        mAdjustNameMargin = a.getInt(R.styleable.StrongerBar_adjustNameMargin, 0);
        mColorPaint = new Paint();
        mBubbleBounds = new Rect();

        a.recycle();
        TypedArray b = context.obtainStyledAttributes(attrs,
                StrongerBarOrientation,defStyleAttr,defStyleRes);
        mOrientation = b.getInt(R.styleable.StrongerBarOrientation_rotation,-1);

        b.recycle();
        if (colorsId != 0) mColorSeeds = getColorsById(colorsId);

        if(mColorSeeds.length ==1){
            mColorSeeds = new int[]{mColorSeeds[0],mColorSeeds[0]};
            mColorChangeCallBack = false;
        }
        setBackgroundColor(mBackgroundColor);
        initData();
    }

    private void initData() {
        mAdjustNameMargin = dp2px(mAdjustNameMargin);
        mBubbleMargin = dp2px(mBubbleMargin);
    }

    private int[] getColorsById(int id) {
        if (isInEditMode()) {
            String[] s = mContext.getResources().getStringArray(id);
            int[] colors = new int[s.length];
            for (int j = 0; j < s.length; j++) {
                colors[j] = Color.parseColor(s[j]);
            }
            return colors;
        } else {
            TypedArray typedArray = mContext.getResources().obtainTypedArray(id);
            int[] colors = new int[typedArray.length()];
            for (int j = 0; j < typedArray.length(); j++) {
                colors[j] = typedArray.getColor(j, Color.BLACK);
            }
            typedArray.recycle();
            return colors;
        }
    }

    private void init() {
        mThumbRadius = mThumbHeight / 2;
        mPaddingSize = (int) mThumbRadius;
        int viewBottom = mOffSetNoPadding?getHeight() - getPaddingBottom():getHeight() - getPaddingBottom() - mPaddingSize ;
        int viewRight = mOffSetNoPadding?getWidth() - getPaddingRight():getWidth() - getPaddingRight() - mPaddingSize;

        realLeft = mOffSetNoPadding?getPaddingLeft():getPaddingLeft() + mPaddingSize;
        realRight = mIsVertical ? viewBottom : viewRight;

        realTop = getPaddingTop() + mPaddingSize;
        int i = mBarHeight / 2;
        realTop = isVertical()?getWidth()/2:getHeight()/2;
        realTop-=i;

        mBarWidth = realRight - realLeft;

        if(mFrameColor != Color.TRANSPARENT) {
            if(mIsVertical){
                realLeft +=1;
                mColorRect = new Rect(realLeft+mFrameWidth, realTop, realRight-mFrameWidth+1,realTop + mBarHeight );
                mColorStroke = new Rect(realLeft+mFrameWidth/2, realTop-mFrameWidth/2, realRight-mFrameWidth/2, realTop + mBarHeight+mFrameWidth/2);
            }else {
                mColorRect = new Rect(realLeft+mFrameWidth, realTop, realRight-mFrameWidth,realTop + mBarHeight );
                mColorStroke = new Rect(realLeft+mFrameWidth/2, realTop-mFrameWidth/2-1 , realRight-mFrameWidth/2-1, realTop + mBarHeight+mFrameWidth/2);
            }


        }else {
            mColorRect = new Rect(realLeft , realTop , realRight ,realTop + mBarHeight );
            mColorStroke = new Rect(realLeft+mFrameWidth/2, realTop-mFrameWidth/2, realRight-mFrameWidth/2, realTop + mBarHeight+mFrameWidth/2);
        }
        mColorGradient = new LinearGradient(0, 0, mColorRect.width(), 0, mColorSeeds, null, Shader.TileMode.MIRROR);
        mColorRectPaint = new Paint();
        mColorRectPaint.setShader(mColorGradient);
        mColorRectPaint.setAntiAlias(true);
        mMatrix = new Matrix();
        cacheColors();
        setAlphaValue();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mIsVertical) {
            mTransparentBitmap = Bitmap.createBitmap(h, w, Bitmap.Config.ARGB_4444);
        } else {
            mTransparentBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        }
        mTransparentBitmap.eraseColor(Color.TRANSPARENT);
        init();
        mInit = true;
        if (mColorsToInvoke != -1) setColor(mColorsToInvoke);
    }


    private void cacheColors() {
        if (mBarWidth < 1) return;
        mColors.clear();
        for (int i = 0; i <= mMaxPosition; i++) {
            mColors.add(pickColor(i));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mIsVertical) {
            canvas.rotate(90);
            canvas.translate(0, -super.getWidth());
        }
        mColorPaint.setAntiAlias(true);
        float colorPosition = (float) mCurrentPosition / mMaxPosition * (mOffSet?mBarWidth-mThumbRadius*2:mBarWidth);
        colorPosition = mOffSet?colorPosition+=mThumbRadius:colorPosition;
        int color = getColor(false);
        int colorStartTransparent = Color.argb(mAlphaMaxPosition, Color.red(color), Color.green(color), Color.blue(color));
        int colorEndTransparent = Color.argb(mAlphaMinPosition, Color.red(color), Color.green(color), Color.blue(color));
        Paint.Style style = mColorPaint.getStyle();

        mColorPaint.setStyle(Paint.Style.STROKE);
        mColorPaint.setStrokeWidth(mFrameWidth);
        mColorPaint.setColor(mFrameColor);
        canvas.drawRect(mColorStroke, mColorPaint);
        mColorPaint.setStyle(style);
        int[] toAlpha = new int[]{colorStartTransparent, colorEndTransparent};
        //clear
        canvas.drawBitmap(mTransparentBitmap, 0, 0, null);
        float thumbY = mColorRect.top + mColorRect.height() / 2;
        if(!TextUtils.isEmpty(mAdjustName)){
            TextPaint textPaint = new TextPaint();
            if(mTextFacePath!=null) {
                Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), mTextFacePath);
                if (typeFace != null) {
                    textPaint.setTypeface(typeFace);
                }
            }
            textPaint.setAntiAlias(true);
            if(!isEnabled()){textPaint.setColor(Color.GRAY);}
            textPaint.setTextSize(mAdjustNameSize);
            StaticLayout sl= new StaticLayout(mAdjustName, textPaint, mTransparentBitmap.getWidth()-8, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            canvas.save();
            if(mOrientation == mOrientationLeft){
                canvas.translate(mOffSetNoPadding?0:mThumbRadius, thumbY-mThumbRadius/2+mBarHeight-mAdjustNameMargin-mFrameWidth);
            }else if(mOrientation == mOrientationRight){
                canvas.translate(mOffSetNoPadding?0:mThumbRadius, thumbY-mThumbRadius/2+mBarHeight/2-mFrameWidth-mAdjustNameMargin);
            }else {
                canvas.translate(mOffSetNoPadding?0:mThumbRadius, thumbY-mThumbRadius/2+mBarHeight/2-mFrameWidth-mAdjustNameMargin);
            }
            sl.draw(canvas);
            canvas.restore();
        }

        canvas.drawRect(mColorRect, mColorRectPaint);
        float thumbX = colorPosition + realLeft;
        if(mSencondColor != -1){
            mColorPaint.setColor(mSencondColor);
            float v = thumbX < 0 ? 0 : thumbX;
            float right = v > realRight ? realRight : v;
            Rect secondRect = new Rect(realLeft+mFrameWidth, realTop, (int) right-mFrameWidth, realTop + mBarHeight);
            canvas.drawRect(secondRect, mColorPaint);

        }

         thumbY = mColorRect.top + mColorRect.height() / 2;
        mColorPaint.setColor(Color.BLACK);

        if(mIsShowBubble){
            canvas.save();
            if(mBubbleTextSize != 0){
                mColorPaint.setTextSize(sp2px(mContext,mBubbleTextSize));
            }else {
                mColorPaint.setTextSize(sp2px(mContext,mAdjustNameSize));
            }
            if(!TextUtils.isEmpty(mTextFacePath)){
                Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), mTextFacePath);
                mColorPaint.setTypeface(typeFace);
                if(!isEnabled()){mColorPaint.setColor(Color.GRAY);}
            }
            String testString = "";
            if(mOnStateChangeListener!=null){
                testString = mOnStateChangeListener.onBubbleTextNeedUpdate(mCurrentPosition, mMaxPosition);
            }
            mColorPaint.getTextBounds(testString, 0, testString.length(), mBubbleBounds);

            if(isVertical()){
                if(mOrientation==mOrientationLeft){
                    canvas.rotate(-90);
                    canvas.drawText(testString,thumbY-getWidth()+mBubbleMargin+mThumbRadius-2,thumbX+mBubbleBounds.height()/2, mColorPaint);
                }else {
                    canvas.rotate(-90);
                    float y = thumbY -getWidth()-mBubbleBounds.width()-mThumbRadius-mBubbleMargin;
                    mColorPaint.getTextBounds(testString, 0, testString.length(), mBubbleBounds);
                    canvas.drawText(testString,y,thumbX+mBubbleBounds.height()/2, mColorPaint);
                }

            }else {
                float y = thumbY -mThumbRadius -mBubbleMargin;
                canvas.drawText(testString,thumbX-mBubbleBounds.width()/2,y, mColorPaint);
            }

            canvas.restore();
        }
        Bitmap bitmap = null;
        if(mOnStateChangeListener != null ){
            if(isEnabled()){
                bitmap = mOnStateChangeListener.onThumbNeedAnimation(mCurrentPosition, mMaxPosition, (int) mThumbRadius * 2);
            }else {
                bitmap = mOnStateChangeListener.onDisableState(mCurrentPosition, mMaxPosition,(int) mThumbRadius*2);
            }
        }
        Log.e("mThumbRadius","mThumbRadius bitmap is ?"+bitmap);
        if(bitmap !=null){
        if(mIsVertical){
            bitmap = rotateToDegrees(bitmap,-90);
            canvas.drawBitmap(bitmap,thumbX-mThumbRadius,thumbY-mThumbRadius, mColorPaint);
        }else {

            canvas.drawBitmap(bitmap,thumbX-mThumbRadius,thumbY-mThumbRadius, mColorPaint);
        }
        }
        if (mIsShowAlphaBar) {
            int top = (int) (mThumbHeight + mThumbRadius + mBarHeight + mBarMargin);
            mAlphaRect = new Rect(realLeft, top, realRight, top + mBarHeight);
            Paint alphaBarPaint = new Paint();
            alphaBarPaint.setAntiAlias(true);
            LinearGradient alphaBarShader = new LinearGradient(0, 0, mAlphaRect.width(), 0, toAlpha, null, Shader.TileMode.MIRROR);
            alphaBarPaint.setShader(alphaBarShader);
            canvas.drawRect(mAlphaRect, alphaBarPaint);

            float alphaPosition = (float) (mAlphaBarPosition - mAlphaMinPosition) / (mAlphaMaxPosition - mAlphaMinPosition)  * mBarWidth;
            float alphaThumbX = alphaPosition + realLeft;
            float alphaThumbY = mAlphaRect.top + mAlphaRect.height() / 2;
            canvas.drawCircle(alphaThumbX, alphaThumbY, mBarHeight / 2 + 5, mColorPaint);

            RadialGradient alphaThumbShader = new RadialGradient(alphaThumbX, alphaThumbY, mThumbRadius, toAlpha, null, Shader.TileMode.MIRROR);
            Paint alphaThumbGradientPaint = new Paint();
            alphaThumbGradientPaint.setAntiAlias(true);
            alphaThumbGradientPaint.setShader(alphaThumbShader);
            canvas.drawCircle(alphaThumbX, alphaThumbY, mThumbHeight / 2, alphaThumbGradientPaint);
        }

        if (mFirstDraw) {

            if (mOnStateChangeListener != null && mColorChangeCallBack) {
                mOnStateChangeListener.onColorChangeListener(mCurrentPosition, mMaxPosition, getColor());
            }

            mFirstDraw = false;

            if(mOnInitDoneListener != null){
                mOnInitDoneListener.done();
            }
        }


        super.onDraw(canvas);
    }

    public static int sp2px(Context context,float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled())return false;
        x = mIsVertical ? event.getY() : event.getX();
        y = mIsVertical ? event.getX() : event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isOnBar(mColorRect, x, y)) {
                    mMovingColorBar = true;
                } else if (mIsShowAlphaBar) {
                    if (isOnBar(mAlphaRect, x, y)) {
                        mMovingAlphaBar = true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mMovingColorBar) {
                    float value = (x - realLeft-(mOffSet?mThumbRadius:0)) / (mBarWidth-(mOffSet?mThumbRadius*2:0)) * mMaxPosition;
                    mCurrentPosition = (int) value;
                    if (mCurrentPosition < 0) mCurrentPosition = 0;
                    if (mCurrentPosition > mMaxPosition) mCurrentPosition = mMaxPosition;
                } else if (mIsShowAlphaBar) {
                    if (mMovingAlphaBar) {
                        float value = (x - realLeft ) / (float)mBarWidth * (mAlphaMaxPosition - mAlphaMinPosition) + mAlphaMinPosition;
                        mAlphaBarPosition = (int) value;
                        if (mAlphaBarPosition < mAlphaMinPosition) mAlphaBarPosition = mAlphaMinPosition;
                        else if (mAlphaBarPosition > mAlphaMaxPosition) mAlphaBarPosition = mAlphaMaxPosition;
                        setAlphaValue();
                    }
                }
                if (mColorChangeCallBack && mOnStateChangeListener != null && (mMovingAlphaBar || mMovingColorBar)){
                    mOnStateChangeListener.onColorChangeListener(mCurrentPosition, mMaxPosition, getColor());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mMovingColorBar = false;
                mMovingAlphaBar = false;
                break;
        }
        return true;
    }
    public void setProgress(int index){
        mCurrentPosition = index;
        invalidate();
    }
    /***
     *
     * @param alphaMaxPosition <= 255 && > alphaMinPosition
     */
    public void setAlphaMaxPosition(int alphaMaxPosition) {
        mAlphaMaxPosition = alphaMaxPosition;
        if(mAlphaMaxPosition > 255){
            mAlphaMaxPosition = 255;
        }else if(mAlphaMaxPosition <= mAlphaMinPosition){
            mAlphaMaxPosition = mAlphaMinPosition+1;
        }

        if(mAlphaBarPosition > mAlphaMinPosition){
            mAlphaBarPosition = mAlphaMaxPosition;
        }
        invalidate();
    }

    public int getAlphaMaxPosition() {
        return mAlphaMaxPosition;
    }

    /***
     *
     * @param alphaMinPosition >=0 && < alphaMaxPosition
     */
    public void setAlphaMinPosition(int alphaMinPosition) {
        this.mAlphaMinPosition = alphaMinPosition;
        if(mAlphaMinPosition >= mAlphaMaxPosition){
            mAlphaMinPosition = mAlphaMaxPosition - 1;
        }else if(mAlphaMinPosition < 0){
            mAlphaMinPosition = 0;
        }

        if(mAlphaBarPosition < mAlphaMinPosition){
            mAlphaBarPosition = mAlphaMinPosition;
        }
        invalidate();
    }

    public int getAlphaMinPosition() {
        return mAlphaMinPosition;
    }

    /**
     * @param r
     * @param x
     * @param y
     * @return whether MotionEvent is performing on bar or not
     */
    private boolean isOnBar(Rect r, float x, float y) {
        if (r.left - mThumbRadius < x && x < r.right + mThumbRadius && r.top - mThumbRadius < y && y < r.bottom + mThumbRadius) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return
     * @deprecated use {@link #setOnInitDoneListener(OnInitDoneListener)} instead.
     */
    public boolean isFirstDraw() {
        return mFirstDraw;
    }


    /**
     * @param value
     * @return color
     */
    private int pickColor(int value) {
        return pickColor((float) value / mMaxPosition * mBarWidth);
    }

    /**
     * @param position
     * @return color
     */
    private int pickColor(float position) {
        float unit = position / mBarWidth;
        if (unit <= 0.0)
            return mColorSeeds[0];

        if (unit >= 1)
            return mColorSeeds[mColorSeeds.length - 1];

        float colorPosition = unit * (mColorSeeds.length - 1);
        int i = (int) colorPosition;
        colorPosition -= i;
        c0 = mColorSeeds[i];
        c1 = mColorSeeds[i + 1];
//         mAlpha = mix(Color.alpha(c0), Color.alpha(c1), colorPosition);
        mRed = mix(Color.red(c0), Color.red(c1), colorPosition);
        mGreen = mix(Color.green(c0), Color.green(c1), colorPosition);
        mBlue = mix(Color.blue(c0), Color.blue(c1), colorPosition);
        return Color.rgb(mRed, mGreen, mBlue);
    }

    /**
     * @param start
     * @param end
     * @param position
     * @return
     */
    private int mix(int start, int end, float position) {
        return start + Math.round(position * (end - start));
    }

    public int getColor() {
        return getColor(mIsShowAlphaBar);
    }

    /**
     * @param withAlpha
     * @return
     */
    public int getColor(boolean withAlpha) {
        //pick mode
        if (mCurrentPosition >= mColors.size()) {
            int color = pickColor(mCurrentPosition);
            if (withAlpha) {
                return color;
            } else {
                return Color.argb(getAlphaValue(), Color.red(color), Color.green(color), Color.blue(color));
            }
        }

        //cache mode
        int color = mColors.get(mCurrentPosition);

        if (withAlpha) {
            return Color.argb(getAlphaValue(), Color.red(color), Color.green(color), Color.blue(color));
        }
        return color;
    }

    public int getAlphaBarPosition() {
        return mAlphaBarPosition;
    }

    public int getAlphaValue() {
        return mAlpha;
    }

    public interface OnStateChangeListener {
        /**
         * @param colorBarPosition between 0-maxValue
         * @param maxPosition maxValue
         * @param color            return the color contains alpha value whether showAlphaBar is true or without alpha value
         */
        void onColorChangeListener(int colorBarPosition, int maxPosition, int color);
        Bitmap onThumbNeedAnimation(int currentPosition, int maxProgress , int radius);
        String onBubbleTextNeedUpdate(int currentPosition, int maxProgress);
        Bitmap onDisableState(int currentPosition, int maxProgress,int width);
    }

    /**
     * @param onStateChangeListener
     */
    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }


    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Set colors by resource id. The resource's type must be ArrayRes
     *
     * @param resId
     */
    public void setColorSeeds(@ArrayRes int resId) {
        setColorSeeds(getColorsById(resId));
    }

    public void setColorSeeds(int[] colors) {
        mColorSeeds = colors;
        init();
        invalidate();
        if (mOnStateChangeListener != null)
            mOnStateChangeListener.onColorChangeListener(mCurrentPosition, mMaxPosition, getColor());
    }

    /**
     * @param color
     * @return the color's position in the bar, if not in the bar ,return -1;
     */
    public int getColorIndexPosition(int color) {
        return mColors.indexOf(Color.argb(255, Color.red(color), Color.green(color), Color.blue(color)));
    }

    public List<Integer> getColors() {
        return mColors;
    }

    public boolean isShowAlphaBar() {
        return mIsShowAlphaBar;
    }

    private void refreshLayoutParams() {
        setLayoutParams(getLayoutParams());
    }

//    public void setVertical(boolean vertical) {
//        mIsVertical = vertical;
//        refreshLayoutParams();
//        invalidate();
//    }

    public boolean isVertical() {
        return mIsVertical;
    }

    public void setShowAlphaBar(boolean show) {
        mIsShowAlphaBar = show;
        refreshLayoutParams();
        invalidate();
        if (mOnStateChangeListener != null&&mColorChangeCallBack)
            mOnStateChangeListener.onColorChangeListener(mCurrentPosition, mMaxPosition, getColor());
    }

    /**
     * @param dp
     */
    public void setBarHeight(float dp) {
        mBarHeight = dp2px(dp);
        refreshLayoutParams();
        invalidate();
    }

    /**
     * @param px
     */
    public void setBarHeightPx(int px) {
        mBarHeight = px;
        refreshLayoutParams();
        invalidate();
    }

    private void setAlphaValue() {
        mAlpha = 255 - mAlphaBarPosition;
    }

    public void setAlphaBarPosition(int value) {
        this.mAlphaBarPosition = value;
        setAlphaValue();
        invalidate();
    }

    public int getMaxValue() {
        return mMaxPosition;
    }

    public void setMaxPosition(int value) {
        this.mMaxPosition = value;
        invalidate();
        cacheColors();
    }

    /**
     * set margin between bars
     *
     * @param mBarMargin
     */
    public void setBarMargin(float mBarMargin) {
        this.mBarMargin = dp2px(mBarMargin);
        refreshLayoutParams();
        invalidate();
    }

    /**
     * set margin between bars
     *
     * @param mBarMargin
     */
    public void setBarMarginPx(int mBarMargin) {
        this.mBarMargin = mBarMargin;
        refreshLayoutParams();
        invalidate();
    }


    /**
     * Set the value of color bar, if out of bounds , it will be 0 or maxValue;
     * @param value
     */
    public void setColorBarPosition(int value) {
        this.mCurrentPosition = value;
        mCurrentPosition = mCurrentPosition > mMaxPosition ? mMaxPosition : mCurrentPosition;
        mCurrentPosition = mCurrentPosition < 0 ? 0 : mCurrentPosition;
        invalidate();
        if (mOnStateChangeListener != null&&mColorChangeCallBack)
            mOnStateChangeListener.onColorChangeListener(mCurrentPosition, mMaxPosition, getColor());
    }

    public void setOnInitDoneListener(OnInitDoneListener  listener){
        this.mOnInitDoneListener = listener;
    }

    /**
     * Set color, it must correspond to the value, if not , setColorBarPosition(0);
     *
     * @paam color
     */
    public void setColor(int color) {
        int withoutAlphaColor = Color.rgb(Color.red(color), Color.green(color), Color.blue(color));

        if (mInit) {
            int value = mColors.indexOf(withoutAlphaColor);
//            mColorsToInvoke = color;
            setColorBarPosition(value);
        } else {
            mColorsToInvoke = color;
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

    }

    /**
     * set thumb's height by dpi
     *
     * @param dp
     */
    public void setThumbHeight(float dp) {
        this.mThumbHeight = dp2px(dp);
        mThumbRadius = mThumbHeight / 2;
        refreshLayoutParams();
        invalidate();
    }

    /**
     * set thumb's height by pixels
     *
     * @param px
     */
    public void setThumbHeightPx(int px) {
        this.mThumbHeight = px;
        mThumbRadius = mThumbHeight / 2;
        refreshLayoutParams();
        invalidate();
    }

    public int getBarHeight() {
        return mBarHeight;
    }

    public int getThumbHeight() {
        return mThumbHeight;
    }

    public int getBarMargin() {
        return mBarMargin;
    }

    public float getColorBarValue() {
        return mCurrentPosition;
    }

    public interface OnInitDoneListener{
        void done();
    }
    public  Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
        mMatrix.reset();
        mMatrix.setRotate(degrees);
        return Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), mMatrix, true);
    }
}