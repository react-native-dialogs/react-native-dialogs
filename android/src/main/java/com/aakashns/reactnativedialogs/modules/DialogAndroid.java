package com.aakashns.reactnativedialogs.modules;

import android.content.DialogInterface;
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
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

public class DialogAndroid extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "DialogAndroid";
    }

    FragmentActivity mActivity;

    public DialogAndroid(
            ReactApplicationContext reactContext,
            FragmentActivity fragmentActivity) {
        super(reactContext);
        mActivity = fragmentActivity;
    }

//    static Set<String> blacklist = new HashSet<>(Arrays.<String>asList(
//            "items",
//            "itemsCallback",
//            "choice",
//            "selectedIndex"
//    ));


    private MaterialDialog.Builder applyOptions(
            MaterialDialog.Builder builder,
            ReadableMap options
    ) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ReadableMapKeySeyIterator iterator = options.keySetIterator();
        while(iterator.hasNextKey()) {
            String key = iterator.nextKey();

            switch (key) {
                case "title":
                    builder.title(options.getString("title"));
                    break;
                case "content":
                    builder.content(options.getString("content"));
                    break;
                case "positiveText":
                    builder.positiveText(options.getString("positiveText"));
                    break;
                case "negativeText":
                    builder.negativeText(options.getString("negativeText"));
                    break;
                case "neutralText":
                    builder.neutralText(options.getString("neutralText"));
                    break;
                case "items":
                    ReadableArray arr = options.getArray("items");
                    String[] items = new String[arr.size()];
                    for (int i = 0; i < arr.size(); i++) {
                        items[i] = arr.getString(i);
                    }
                    builder.items(items);
                    break;
                case "autoDismiss":
                    builder.autoDismiss(options.getBoolean("autoDismiss"));
                    break;
                case "forceStacking":
                    builder.forceStacking(options.getBoolean("forceStacking"));
                    break;
                case "alwaysCallSingleChoiceCallback":
                    if (options.getBoolean("alwaysCallSingleChoiceCallback")) {
                        builder.alwaysCallSingleChoiceCallback();
                    }
                    break;
                case "alwaysCallMultiChoiceCallback":
                    if (options.getBoolean("alwaysCallMultiChoiceCallback")) {
                        builder.alwaysCallMultiChoiceCallback();
                    }
                    break;
                case "alwaysCallInputCallback":
                    if (options.getBoolean("alwaysCallInputCallback")) {
                        builder.alwaysCallInputCallback();
                    }
                    break;
                case "cancelable":
                    builder.cancelable(options.getBoolean("cancelable"));
                    break;
                case "progressIndeterminateStyle": // true for horizontal, DO NOT USE
                    builder.progressIndeterminateStyle(
                            options.getBoolean("progressIndeterminateStyle"));
                    break;
                case "progress":
                    ReadableMap progress = options.getMap("progress");
                    boolean indeterminate = progress.hasKey("indeterminate") &&
                            progress.getBoolean("indeterminate");

                    if (indeterminate) {
                        builder.progress(true, 0);
                        boolean horizontal = progress.hasKey("style") &&
                                progress.getString("style").equals("horizontal");
                        if (horizontal) builder.progressIndeterminateStyle(horizontal);
                    } else {
                        // Determinate progress bar not supported currently
                    }
            }
        }

        return builder;
    }

    @ReactMethod
    public void show(ReadableMap options, final Callback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity);
        try {
            applyOptions(builder, options);
        } catch (Exception e) {
            callback.invoke("error", e.getMessage(), options.toString());
        }

        if (options.hasKey("onPositive")) {
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onPositive");
                }
            });
        }

        if (options.hasKey("onNegative")) {
            builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onNegative");
                }
            });
        }

        if (options.hasKey("onNeutral")) {
            builder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onNeutral");
                }
            });
        }

        if (options.hasKey("onAny")) {
            builder.onAny(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onAny");
                }
            });
        }

        if (options.hasKey("itemsCallback")) {
            builder.itemsCallback(new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog materialDialog, View view, int i,
                                        CharSequence charSequence) {
                    callback.invoke("itemsCallback", i, charSequence.toString());
                }
            });
        }

        if (options.hasKey("itemsCallbackSingleChoice")) {
            int selectedIndex = options.hasKey("selectedIndex") ?
                    options.getInt("selectedIndex") : -1;
            builder.itemsCallbackSingleChoice(selectedIndex,
                    new MaterialDialog.ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(MaterialDialog materialDialog, View view, int i,
                                           CharSequence charSequence) {
                    callback.invoke("itemsCallbackSingleChoice", i, charSequence.toString());
                    return true;
                }
            });
        }

        if (options.hasKey("itemsCallbackMultiChoice")) {
            Integer[] selectedIndices = null;
            if (options.hasKey("selectedIndices")) {
                ReadableArray arr = options.getArray("selectedIndices");
                selectedIndices = new Integer[arr.size()];
                for (int i = 0; i < arr.size(); i++) {
                    selectedIndices[i] = arr.getInt(i);
                }
            }

            builder.itemsCallbackMultiChoice(selectedIndices,
                    new MaterialDialog.ListCallbackMultiChoice() {
                @Override
                public boolean onSelection(MaterialDialog materialDialog,
                                           Integer[] integers, CharSequence[] charSequences) {
                    StringBuilder selected = new StringBuilder("");
                    for (int i = 0; i < integers.length - 1; i++) {
                        selected.append(integers[i]).append(",");
                    }
                    if (integers.length > 0) {
                        selected.append(integers[integers.length - 1]);
                    }

                    callback.invoke("itemsCallbackMultiChoice", selected.toString());
                    return true;
                }
            });

            if (options.hasKey("multiChoiceClearButton") &&
                    options.getBoolean("multiChoiceClearButton")) {
                builder.neutralText("Clear").onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.clearSelectedIndices();
                    }
                });
            }
        }

        if (options.hasKey("showListener")) {
            builder.showListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    callback.invoke("showListener");
                }
            });
        }

        if (options.hasKey("cancelListener")) {
            builder.cancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    callback.invoke("cancelListener");
                }
            });
        }

        if (options.hasKey("dismissListener")) {
            builder.dismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    callback.invoke("dismissListener");
                }
            });
        }

        if (options.hasKey("input")) {
            ReadableMap input = options.getMap("input");
            String hint = input.hasKey("hint") ? input.getString("hint") : null;
            String prefill = input.hasKey("prefill") ? input.getString("prefill") : null;
            boolean allowEmptyInput = !input.hasKey("allowEmptyInput") ||
                    input.getBoolean("allowEmptyInput");

            if (input.hasKey("type")) {
                builder.inputType(input.getInt("type"));
            }

            int minLength = input.hasKey("minLength") ? input.getInt("minLength") : 0;
            int maxLength = input.hasKey("maxLength") ? input.getInt("maxLength") : -1;

            builder.inputRange(minLength, maxLength);

            builder.input(hint, prefill, allowEmptyInput, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                    callback.invoke("input", charSequence.toString());
                }
            });
        }

        builder.show();
    }

//    @ReactMethod
//    public void showItemsDialog(
//            ReadableMap options,
//            final Callback itemsCallback,
//            final Callback errorCallback) {
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity);
//        try {
//            builder = applyOptions(builder, options);
//            boolean choice =  options.hasKey("choice") && options.getBoolean("choice");
//
//            int selectedIndex = options.hasKey("selectedIndex") ?
//                    options.getInt("selectedIndex") : -1;
//
//            if (itemsCallback != null) {
//                if (choice) {
//                    builder.itemsCallbackSingleChoice(selectedIndex,
//                            new MaterialDialog.ListCallbackSingleChoice() {
//                        @Override
//                        public boolean onSelection(MaterialDialog materialDialog, View view, int i,
//                                                   CharSequence charSequence) {
//                            itemsCallback.invoke(i);
//                            return true;
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//            errorCallback.invoke(e.getMessage(), options.toString());
//        }
//        builder.show();
//    }
//
//    @ReactMethod
//    public void showBasicDialog(
//            ReadableMap options,
//            final Callback positiveCallback,
//            final Callback negativeCallback,
//            final Callback errorCallback) {
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity);
//
//        try {
//            builder = applyOptions(builder, options);
//            if (positiveCallback != null) {
//                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//                        positiveCallback.invoke();
//                    }
//                });
//            }
//
//            if (negativeCallback != null) {
//                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
//                        negativeCallback.invoke();
//                    }
//                });
//            }
//        } catch (Exception e) {
//            if (errorCallback != null) {
//                errorCallback.invoke(e.getMessage(), options.toString());
//            }
//            return;
//        }
//        builder.show();
//    }
}
