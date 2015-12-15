package com.pi_developers.xscroller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;



public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();

        if (Utils.isModuleActive()) {
                intent.setClass(getApplicationContext(), Main.class);
        } else {
                intent.setClass(getApplicationContext(), ModuleNotActivated.class);
        }
        startActivity(intent);

        finish();



    }



}
