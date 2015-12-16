package com.pi_developers.xscroller.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pi_developers.xscroller.R;

import al.androidfire.loltint.Color;

public class About extends ActionBarActivity{
    private LinearLayout mContent;
    private TextView mCopyRightText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.about);
        getSupportActionBar().hide();
        mContent = (LinearLayout) findViewById(R.id.about_content);
        mCopyRightText = (TextView)findViewById(R.id.copy_right_txt);
        setUpBg();
        setCpRight();
    }

    private void setCpRight() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("            Made with ").append(" ");
        builder.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_heart_white_24dp),
                builder.length() - 1, builder.length(), 0);
        builder.append("\n                 By Pi-Developers");
        mCopyRightText.setText(builder);
        mCopyRightText.setTextColor(Color.WHITE);

    }

    private void setUpBg() {
        Bitmap bitmap = Utils.resizeBitmapAccordingToDevice(getApplicationContext(),R.drawable.about_wallpaper);

        Drawable drawable = new BitmapDrawable(bitmap);

        if (Build.VERSION.SDK_INT >= 16) {
            mContent.setBackground(drawable);
        }else {
            mContent.setBackgroundColor(Color.COLOR_BLUE);
        }
    }
}
