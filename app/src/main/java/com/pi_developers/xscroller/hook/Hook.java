package com.pi_developers.xscroller.hook;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
/**
 * @author Sahid Almas
 */
public abstract class Hook  {

    public Hook() {

    }

    public abstract void onCreate(XC_LoadPackage.LoadPackageParam lpparam);
}
