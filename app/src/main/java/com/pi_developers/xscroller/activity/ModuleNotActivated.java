package com.pi_developers.xscroller.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import com.pi_developers.xscroller.R;
import al.androidfire.loltint.Color;
import al.androidfire.loltint.LolTint;

/**
 * @author Sahid Almas
 */
public class ModuleNotActivated extends ActionBarActivity {

    private TextView mMessageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_not_activated);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.COLOR_TEAL));

        mMessageText = (TextView)findViewById(R.id.module_act_message);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Show the text when the the window attached to Window
        mMessageText.setText(getString(R.string.module_not_activated));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Enabling the LolTint
        LolTint.on(true,true,true,this);
    }
}
