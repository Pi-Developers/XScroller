package com.pi_developers.xscroller;

import com.pi_developers.xscroller.hook.ActivityHook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author Sahid Almas
 */
public class Xposed implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("android"))
            return;

        new ActivityHook().onCreate(lpparam);

    }
}
