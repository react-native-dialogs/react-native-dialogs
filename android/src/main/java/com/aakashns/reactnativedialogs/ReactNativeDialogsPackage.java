package com.aakashns.reactnativedialogs;

import android.support.v4.app.FragmentActivity;

import com.aakashns.reactnativedialogs.modules.DialogAndroid;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.List;

public class ReactNativeDialogsPackage implements ReactPackage {
    FragmentActivity mActivity;

    public ReactNativeDialogsPackage(FragmentActivity fragmentActivity) {
        mActivity = fragmentActivity;
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new DialogAndroid(reactContext, mActivity));
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return new ArrayList<>();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return new ArrayList<>();
    }
}
