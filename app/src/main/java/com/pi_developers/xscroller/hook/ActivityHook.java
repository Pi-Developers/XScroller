package com.pi_developers.xscroller.hook;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author Sahid Almas
 */
public class ActivityHook extends Hook {

    public static Class<?> ACTIVITY_CLASS;
    public static String TAG_NAME = "[XScroller]";
    @Override
    public void onCreate(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        ACTIVITY_CLASS = XposedHelpers.findClass("android.app.Activity",loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(ACTIVITY_CLASS, "dispatchKeyEvent", KeyEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                KeyEvent keyEvent = null;
                for (Object obj : param.args) {
                    if (obj != null) {
                        keyEvent = (KeyEvent) obj;
                        break;
                    }
                }
                int action = keyEvent.getAction();

                int keyCode = keyEvent.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.KEYCODE_VOLUME_UP:
                        if (action == KeyEvent.ACTION_DOWN) {
                            Toast.makeText(activity.getApplicationContext(),"Volume Up",Toast.LENGTH_SHORT).show();
                        }
                    case KeyEvent.KEYCODE_VOLUME_DOWN:
                        if (action == KeyEvent.ACTION_DOWN) {
                            Toast.makeText(activity.getApplicationContext(),"Volume Down",Toast.LENGTH_SHORT).show();
                        }
                    default:
                }
            }
        });
    }
}
