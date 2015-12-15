package com.pi_developers.xscroller;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ScrollView;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author Sahid Almas
 */
public class Xposed implements IXposedHookZygoteInit,IXposedHookInitPackageResources,
        IXposedHookLoadPackage{

    private XSharedPreferences xSharedPreferences;
    public static String PACKAGE_NAME = "com.pi_developers.xscroller";
    public static int VOLUME_UP_PRESSED = 0x112;
    public static int VOLUME_DOWN_PRESSED = 0x211;
    public static int VERTICAL_SCROLL = 0x245;
    public static int HORIZONTAL_SCROLL = 0x542;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("android"))
            return;

        XposedBridge.log("Loaded app: " + lpparam.packageName);

        handleActivity(lpparam.classLoader);
        initZScrollView(lpparam.classLoader);

    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) throws Throwable {

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        xSharedPreferences =  new XSharedPreferences(sahidalmas.xposed.util.Xposed.class.getPackage().getName());
        xSharedPreferences.makeWorldReadable();
        xSharedPreferences.reload();


        handleActivity(null);
        initZScrollView(null);

    }

    private void handleActivity(ClassLoader classLoader) {

        final Class<?> classActivity = XposedHelpers.findClass("android.app.Activity", classLoader);

        XposedHelpers.findMethodExact(classActivity, "dispatchKeyEvent", KeyEvent.class);
        XposedHelpers.findAndHookMethod(classActivity, "dispatchKeyEvent", KeyEvent.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                dispatchKeyEvent(param);
            }
        });
    }


    private void dispatchKeyEvent(XC_MethodHook.MethodHookParam param) {

        Activity activity = null;
        KeyEvent event = null;
        if (param.thisObject instanceof Activity) {
            activity = (Activity) param.thisObject;
        }
        if (activity != null) {
            for (Object object : param.args) {
                if (object instanceof KeyEvent) {
                    event = (KeyEvent) object;
                    break;
                }
            }

        }
        assert event != null;

        int action = event.getAction();

        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    sendBroadcast(activity.getApplicationContext(),VOLUME_UP_PRESSED);
                }

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    sendBroadcast(activity.getApplicationContext(),VOLUME_DOWN_PRESSED);
                }

            default:

        }
    }

    private void sendBroadcast(Context context,int _id) {

        xSharedPreferences.reload();
        Intent intent = new Intent();
        intent.setAction(PACKAGE_NAME);
        intent.putExtra("scr",_id);
        context.getApplicationContext().sendBroadcast(intent);
    }

    private void initZScrollView(ClassLoader classLoader) {

        final Class<?> aClass = XposedHelpers.findClass("android.widget.ScrollView", classLoader);
        XposedHelpers.findAndHookConstructor(aClass, Context.class, AttributeSet.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ScrollView scrollView = null;
                Context context = null;
                AttributeSet attributeSet = null;
                if (param.thisObject instanceof ScrollView) {
                    scrollView = (ScrollView) param.thisObject;
                }
                for (Object object : param.args) {
                    if (object instanceof Context) {
                        context = (Context) object;
                    } else if (object instanceof AttributeSet) {
                        attributeSet = (AttributeSet) object;
                    }
                }

                if (context != null && scrollView != null || attributeSet != null) {
                    handleScrollViewBroadCast(scrollView);
                }
            }
        });
    }

    private void handleScrollViewBroadCast(final ScrollView scrollView) {


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                xSharedPreferences.reload();
                int POLICY_TO_SCROLL = xSharedPreferences.getInt("scrolling_policy",VERTICAL_SCROLL);
                Object object = intent.getExtras().get("scr");
                int code = 0;
                if (object instanceof Integer) {
                    code = (int) object;
                }


                if (code == VOLUME_DOWN_PRESSED) {

                    if (POLICY_TO_SCROLL == VERTICAL_SCROLL) {
                        XposedBridge.log("DOWN_VERTICAL SCROLLING");
                    }else if(POLICY_TO_SCROLL == HORIZONTAL_SCROLL){
                        XposedBridge.log("DOWN_HORIZONTAL SCROLLING");
                    }

                }else if (code == VOLUME_UP_PRESSED) {
                    if (POLICY_TO_SCROLL == VERTICAL_SCROLL) {
                        XposedBridge.log("UP_VERTICAL SCROLLING");
                    }else if(POLICY_TO_SCROLL == HORIZONTAL_SCROLL){
                        XposedBridge.log("UP_HORIZONTAL SCROLLING");
                    }
                }else {
                    XposedBridge.log("Something Went Wrong");
                }

            }
        };
        scrollView.getContext().registerReceiver(receiver,new IntentFilter(PACKAGE_NAME));


    }
}
