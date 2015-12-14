package com.pi_developers.xscroller;


import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;
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
public class Xposed implements IXposedHookZygoteInit, IXposedHookInitPackageResources,
        IXposedHookLoadPackage{

    private XSharedPreferences xSharedPreferences;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("android"))
            return;

        XposedBridge.log("Loaded app: " + lpparam.packageName);

    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) throws Throwable {

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        xSharedPreferences =  new XSharedPreferences(sahidalmas.xposed.util.Xposed.class.getPackage().getName());
        xSharedPreferences.makeWorldReadable();
        xSharedPreferences.reload();


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
        KeyEvent keyEvent;
        if (param.thisObject instanceof Activity) {
            activity = (Activity) param.thisObject;
        }
        if (activity != null) {
            for (Object object : param.args) {
                if (object instanceof KeyEvent) {
                    keyEvent = (KeyEvent) object;
                    break;
                }
            }
            Toast.makeText(activity.getApplication(),"Eurekka Eurekka",Toast.LENGTH_SHORT).show();
        }




    }
}
