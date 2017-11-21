package aaaaaaaa.example.com.strongerbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import aaaaaaaa.example.com.strongerbar.SeekBarThumbUtils;
import aaaaaaaa.example.com.strongerbar.StrongerBar;
import aaaaaaaa.example.com.strongerbar.R;

public class MainActivity extends AppCompatActivity implements StrongerBar.OnStateChangeListener,View.OnClickListener{

    private Button viewById1;
    private StrongerBar mStrongerBar;
    private StrongerBar stronger_right;
    private StrongerBar stronger_left;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        mStrongerBar = (StrongerBar) findViewById(R.id.colorSlider);
        stronger_right = (StrongerBar) findViewById(R.id.stronger_right);
        stronger_left = (StrongerBar) findViewById(R.id.stronger_left);
        StrongerBar Stronger_3 = (StrongerBar) findViewById(R.id.Stronger_3);
        StrongerBar Stronger_1 = (StrongerBar) findViewById(R.id.Stronger_1);
        StrongerBar Stronger_2 = (StrongerBar) findViewById(R.id.Stronger_2);
        image = (ImageView) findViewById(R.id.image);
        Stronger_3.setOnStateChangeListener(this);
        Stronger_1.setOnStateChangeListener(this);
        Stronger_2.setOnStateChangeListener(this);
        mStrongerBar.setOnStateChangeListener(this);
        stronger_right.setOnStateChangeListener(this);
        stronger_left.setOnStateChangeListener(this);
        viewById1 = (Button) findViewById(R.id.btView);
        viewById1.setOnClickListener(this);
    }

    boolean flag =false;
    @Override
    public void onClick(View v) {
        mStrongerBar.setEnabled(flag);
        stronger_right.setEnabled(flag);
        stronger_left.setEnabled(flag);
        flag = !flag;
    }

    @Override
        public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
            viewById1.setTextColor(color);
        }

        @Override
        public Bitmap onThumbNeedAnimation(int colorBarPosition, int maxProgress, int radius) {
            return SeekBarThumbUtils.getInstance(MainActivity.this).getThumbBitMap(maxProgress,colorBarPosition,radius);
        }

        @Override
        public String onBubbleTextNeedUpdate(int currentPosition, int maxProgress) {

        return "aaaaa";
    }

    @Override
    public Bitmap onDisableState(int currentPosition, int maxProgress,int width) {
        Drawable bitmapDrawable =  getResources().getDrawable(R.drawable.disable);
        Bitmap bitmap = SeekBarThumbUtils.drawableToBitmap(width, bitmapDrawable);

        return  bitmap;
    }
}
