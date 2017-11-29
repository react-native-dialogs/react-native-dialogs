package com.aakashns.reactnativedialogs.modules;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.UiThreadUtil;

import java.lang.reflect.InvocationTargetException;

public class DialogAndroid extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "DialogAndroid";
    }


    public DialogAndroid(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /* Apply the options to the provided builder */
    private MaterialDialog.Builder applyOptions(
            MaterialDialog.Builder builder,
            ReadableMap options
    ) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ReadableMapKeySetIterator iterator = options.keySetIterator();
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
                case "positiveColor":
                    builder.positiveColor(Color.parseColor(options.getString("positiveColor")));
                    break;
                case "negativeText":
                    builder.negativeText(options.getString("negativeText"));
                    break;
                case "negativeColor":
                    builder.negativeColor(Color.parseColor(options.getString("negativeColor")));
                    break;
                case "neutralText":
                    builder.neutralText(options.getString("neutralText"));
                    break;
                case "neutralColor":
                    builder.neutralColor(Color.parseColor(options.getString("neutralColor")));
                    break;
                case "titleColor":
                    builder.titleColor(Color.parseColor(options.getString("titleColor")));
                    break;  
                case "widgetColor":
                    builder.widgetColor(Color.parseColor(options.getString("widgetColor")));
                    break;
                case "linkColor":
                    builder.linkColor(Color.parseColor(options.getString("linkColor")));
                    break;    
                case "contentColor":
                    builder.contentColor(Color.parseColor(options.getString("contentColor")));
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
                case "buttonsGravity":
                    String bg = options.getString("buttonsGravity");
                    if( bg.equals("start") )
                        builder.buttonsGravity(GravityEnum.START);
                    else if( bg.equals("end") )
                        builder.buttonsGravity(GravityEnum.END);
                    else
                        builder.buttonsGravity(GravityEnum.CENTER);
                    break;
                case "itemsGravity":
                    String ig = options.getString("itemsGravity");
                    if( ig.equals("start") )
                        builder.itemsGravity(GravityEnum.START);
                    else if( ig.equals("end") )
                        builder.itemsGravity(GravityEnum.END);
                    else
                        builder.itemsGravity(GravityEnum.CENTER);
                    break;
                case "titleGravity":
                    String tg = options.getString("titleGravity");
                    if( tg.equals("start") )
                        builder.titleGravity(GravityEnum.START);
                    else if( tg.equals("end") )
                        builder.titleGravity(GravityEnum.END);
                    else
                        builder.titleGravity(GravityEnum.CENTER);
                    break;
                case "rtl":
                    if( options.getBoolean("rtl") ) {
                        builder.titleGravity(GravityEnum.END);
                        builder.itemsGravity(GravityEnum.END);
                        builder.contentGravity(GravityEnum.END);
                        builder.buttonsGravity(GravityEnum.START);
                        builder.btnStackedGravity(GravityEnum.START);
                    }
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
                        // TODO : Implement determinate progress bar
                    }
            }
        }

        return builder;
    }

    MaterialDialog.Builder mBuilder;
    MaterialDialog mDialog;

    @ReactMethod
    public void show(ReadableMap options, final Callback callback) {
        mBuilder = new MaterialDialog.Builder(getCurrentActivity());
        try {
            applyOptions(mBuilder, options);
        } catch (Exception e) {
            callback.invoke("error", e.getMessage(), options.toString());
        }

        if (options.hasKey("onPositive")) {
            mBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onPositive");
                }
            });
        }

        if (options.hasKey("onNegative")) {
            mBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onNegative");
                }
            });
        }

        if (options.hasKey("onNeutral")) {
            mBuilder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onNeutral");
                }
            });
        }

        if (options.hasKey("onAny")) {
            mBuilder.onAny(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    callback.invoke("onAny");
                }
            });
        }

        if (options.hasKey("itemsCallback")) {
            mBuilder.itemsCallback(new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog materialDialog, View view, int i,
                                        CharSequence charSequence) {
                    callback.invoke("itemsCallback", i, charSequence == null ? null : charSequence.toString());
                }
            });
        }

        if (options.hasKey("itemsCallbackSingleChoice")) {
            // Check if there is a preselected index
            int selectedIndex = options.hasKey("selectedIndex") ?
                    options.getInt("selectedIndex") : -1;
            mBuilder.itemsCallbackSingleChoice(selectedIndex,
                    new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog materialDialog, View view, int i,
                                                   CharSequence charSequence) {
                            charSequence = charSequence == null ? "" : charSequence;
                            callback.invoke("itemsCallbackSingleChoice", i, charSequence.toString());
                            return true;
                        }
                    });
        }

        if (options.hasKey("itemsCallbackMultiChoice")) {
            // Check if there are preselected indices
            Integer[] selectedIndices = null;
            if (options.hasKey("selectedIndices")) {
                ReadableArray arr = options.getArray("selectedIndices");
                selectedIndices = new Integer[arr.size()];
                for (int i = 0; i < arr.size(); i++) {
                    selectedIndices[i] = arr.getInt(i);
                }
            }

            mBuilder.itemsCallbackMultiChoice(selectedIndices,
                    new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog materialDialog,
                                                   Integer[] integers, CharSequence[] charSequences) {

                            // Concatenate selected IDs into a string
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

            // Provide a 'Clear' button to unselect all choices
            if (options.hasKey("multiChoiceClearButton") &&
                    options.getBoolean("multiChoiceClearButton")) {
                mBuilder.neutralText("Clear").onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.clearSelectedIndices();
                    }
                });
            }
        }

        if (options.hasKey("showListener")) {
            mBuilder.showListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    callback.invoke("showListener");
                }
            });
        }

        if (options.hasKey("cancelListener")) {
            mBuilder.cancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    callback.invoke("cancelListener");
                }
            });
        }

        if (options.hasKey("dismissListener")) {
            mBuilder.dismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    callback.invoke("dismissListener");
                }
            });
        }

        if (options.hasKey("input")) {
            ReadableMap input = options.getMap("input");

            // Check for hint and prefilled text
            String hint = input.hasKey("hint") ? input.getString("hint") : null;
            String prefill = input.hasKey("prefill") ? input.getString("prefill") : null;

            // Check if empty input is allowed
            boolean allowEmptyInput = !input.hasKey("allowEmptyInput") ||
                    input.getBoolean("allowEmptyInput");

            // TODO : Provide pre-selected input types in Javascript
            if (input.hasKey("type")) {
                mBuilder.inputType(input.getInt("type"));
            }

            int minLength = input.hasKey("minLength") ? input.getInt("minLength") : 0;
            int maxLength = input.hasKey("maxLength") ? input.getInt("maxLength") : -1;

            mBuilder.inputRange(minLength, maxLength);

            mBuilder.input(hint, prefill, allowEmptyInput, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                    callback.invoke("input", charSequence.toString());
                }
            });
        }
        UiThreadUtil.runOnUiThread(new Runnable() {
            public void run() {
                if (mDialog != null)
                    mDialog.dismiss();
                mDialog = mBuilder.build();
                mDialog.show();
            }
        });
    }

    MaterialDialog simple;
    @ReactMethod
    public void list(ReadableMap options, final Callback callback) {
        final MaterialSimpleListAdapter simpleListAdapter = new MaterialSimpleListAdapter(getCurrentActivity());

        ReadableArray arr = options.getArray("items");
        for(int i = 0; i < arr.size(); i++){
            simpleListAdapter.add(new MaterialSimpleListItem.Builder(getCurrentActivity())
                    .content(arr.getString(i))
                    .build());
        }

        final MaterialDialog.Builder adapter = new MaterialDialog.Builder(getCurrentActivity())
                .title(options.hasKey("title") ? options.getString("title") : "")
                .adapter(simpleListAdapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        callback.invoke(which, text);
                        if (simple != null) {
                            simple.dismiss();
                        }
                    }
                })
                .autoDismiss(true);

        UiThreadUtil.runOnUiThread(new Runnable() {
            public void run() {
                if (simple != null) {
                    simple.dismiss();
                }
                simple = adapter.build();
                simple.show();
            }
        });
    }

    @ReactMethod
    public void dismiss() {
        if(mDialog != null)
            mDialog.dismiss();
    }

}
