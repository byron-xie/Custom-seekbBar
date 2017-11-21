package aaaaaaaa.example.com.strongerbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import aaaaaaaa.example.com.strongerbar.R;


/**
 * Created by root on 17-11-14.
 */

public class SeekBarThumbUtils {
    private View v;
    private static Canvas mCanvas;
    private static Drawable mShutter;
    private static SeekBarThumbUtils mSeekBarThumbUtils;
    private static Drawable mShutterPoint;

    private SeekBarThumbUtils(){};

    public static SeekBarThumbUtils getInstance(Context context){
        if(mSeekBarThumbUtils ==null){
            mCanvas = new Canvas();
            mShutter = context.getResources().getDrawable(R.drawable.red_shutter);
            mSeekBarThumbUtils = new SeekBarThumbUtils();
            mShutterPoint = context.getResources().getDrawable(R.drawable.shutter_point);

        }
        return mSeekBarThumbUtils;
    }




    public void setButton(View v){
        this.v=v;
    }
    public Bitmap getThumbBitMap(int max , int progress, int width) {
        float range = max / 180f;
        float v = progress / range;
        return drawableToBitmap(v, width);

    }
//        if(mFrameColor != Color.TRANSPARENT) {
//        if(mIsVertical&&mOffSetNoPadding){
//            mColorRect = new Rect(realLeft+mFrameWidth  , realTop, realRight-mFrameWidth,realTop + mBarHeight );
//            mColorStroke = new Rect(realLeft+mFrameWidth/2, realTop , realRight-mFrameWidth/2, realTop + mBarHeight+mFrameWidth/2);;
//        }else if(!mIsVertical&&mOffSetNoPadding){
//            mColorRect = new Rect(realLeft, realTop, realRight,realTop + mBarHeight );
//            mColorStroke = new Rect(realLeft+mFrameWidth/2, realTop-mFrameWidth/2-1 , realRight-mFrameWidth/2-1, realTop + mBarHeight+mFrameWidth/2);
//        }else if(mIsVertical){
//
//        }else {
//        }
//    }else {
//        mColorRect = new Rect(realLeft , realTop , realRight ,realTop + mBarHeight );
//        mColorStroke = null;
//    }



    public  Bitmap drawableToBitmap(float range ,int width) {
        Bitmap bitmap = Bitmap.createBitmap(
                width,
                width,
                Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(null);
        mCanvas.setBitmap(bitmap);
        mShutter.setBounds(0, 0, width, width);
        mShutter.draw(mCanvas);
        mShutterPoint.setBounds(0,0,width/2-30,9);
        mCanvas.translate(width/2,width/2+9/2 - range/180*9);
        mCanvas.rotate(180+range);
        mShutterPoint.draw(mCanvas);
        if(v !=null){
            v.setBackground(new BitmapDrawable(bitmap));
        }
        return bitmap;
    }


    public  static Bitmap drawableToBitmap(int width,Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                width,
                width,
                Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(null);
        mCanvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, width, width);
        drawable.draw(mCanvas);

        return bitmap;
    }

}
