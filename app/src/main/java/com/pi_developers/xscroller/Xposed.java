package com.pi_developers.xscroller;

import com.pi_developers.xscroller.hook.ActivityHook;
import com.pi_developers.xscroller.hook.Hook;

import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author Sahid Almas
 */
public class Xposed implements IXposedHookLoadPackage{

    private Set<Hook> hooks = new HashSet<>();
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("android"))
            return;

        hooks.add(new ActivityHook());

        for (Hook hook : hooks) {
            hook.onCreate(lpparam);
        }

    }
}
