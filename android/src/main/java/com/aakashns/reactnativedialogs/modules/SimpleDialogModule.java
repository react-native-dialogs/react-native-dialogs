package com.aakashns.reactnativedialogs.modules;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySeyIterator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

public class SimpleDialogModule extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "SimpleDialog";
    }

    FragmentActivity mActivity;

    public SimpleDialogModule(
            ReactApplicationContext reactContext,
            FragmentActivity fragmentActivity) {
        super(reactContext);
        mActivity = fragmentActivity;
    }

    static Set<String> blacklist = new HashSet<>(Arrays.<String>asList(
            "items",
            "itemsCallback",
            "choice",
            "selectedIndex"
    ));

    private MaterialDialog.Builder applyOptions(
            MaterialDialog.Builder builder,
            ReadableMap options
    ) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {


        Method[] allMethods = builder.getClass().getDeclaredMethods();
        ReadableMapKeySeyIterator iterator = options.keySetIterator();

        while(iterator.hasNextKey()) {
            String key = iterator.nextKey();
            if (blacklist.contains(key)) continue;
            Method method = MaterialDialog.Builder.class
                    .getMethod(key, new Class[]{CharSequence.class});
            method.invoke(builder, options.getString(key));
        }

        if (options.hasKey("items")) {
            ReadableArray arr = options.getArray("items");
            String[] items = new String[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                items[i] = arr.getString(i);
            }
            builder.items(items);
        }

        return builder;
    }

    @ReactMethod
    public void showItemsDialog(
            ReadableMap options,
            final Callback itemsCallback,
            final Callback errorCallback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity);
        try {
            builder = applyOptions(builder, options);
            boolean choice =  options.hasKey("choice") && options.getBoolean("choice");

            int selectedIndex = options.hasKey("selectedIndex") ?
                    options.getInt("selectedIndex") : -1;

            if (itemsCallback != null) {
                if (choice) {
                    builder.itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                            itemsCallback.invoke(i);
                            return true;
                        }
                    });
                }
            }
        } catch (Exception e) {
            errorCallback.invoke(e.getMessage(), options.toString());
        }
        builder.show();
    }

    @ReactMethod
    public void showBasicDialog(
            ReadableMap options,
            final Callback positiveCallback,
            final Callback negativeCallback,
            final Callback errorCallback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity);

        try {
            builder = applyOptions(builder, options);
            if (positiveCallback != null) {
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        positiveCallback.invoke();
                    }
                });
            }

            if (negativeCallback != null) {
                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        negativeCallback.invoke();
                    }
                });
            }
        } catch (Exception e) {
            if (errorCallback != null) {
                errorCallback.invoke(e.getMessage(), options.toString());
            }
            return;
        }
        builder.show();
    }
}
