package com.frowhy.wake.xposed;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * WakeXposed
 * Created by frowhy on 2017/3/17.
 */

public class WakeAnyApp implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XC_MethodHook mCallback = new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int IntentType = (int) param.args[2];
                Intent Intent = (Intent) param.args[3];

                if (IntentType == 0) {

                    String Package = Intent.getPackage();
                    Set<String> Categories = Intent.getCategories();
                    String Action = Intent.getAction();
                    String Scheme = Intent.getScheme();
                    Uri Data = Intent.getData();
                    String Extras = Intent.getExtras().toString();
                    ClipData ClipData = Intent.getClipData();
                    ComponentName Component = Intent.getComponent();

                    log("----------start----------");
                    log("Extras : " + Extras);
                    log("IntentType : " + IntentType);
                    log("Intent : " + Intent);
                    log("Package : " + Package);
                    log("Categories : " + Categories);
                    log("Action : " + Action);
                    log("Scheme : " + Scheme);
                    log("Data : " + Data);
                    log("ClipData : " + ClipData);
                    log("Component : " + Component);
                    log("----------end----------");
                    log(" ");
                }
            }
        };

        Class<?> intentFirewall = XposedHelpers.findClass("com.android.server.firewall.IntentFirewall", lpparam.classLoader);
        XposedBridge.hookAllMethods(intentFirewall, "checkIntent", mCallback);

    }

    @SuppressWarnings("InfiniteRecursion")
    private void log(String string) {
        Log.d("WTF", "wake | " + string);
        XposedBridge.log("wake | " + string);
    }
}
