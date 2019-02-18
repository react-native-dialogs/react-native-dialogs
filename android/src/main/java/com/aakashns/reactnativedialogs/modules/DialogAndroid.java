package com.aakashns.reactnativedialogs.modules;

import android.text.Html;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;

import java.lang.reflect.InvocationTargetException;

public class DialogAndroid extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private static final String KIND = "kind";
    private static final String CHECKED = "checked";
    private static final String SELECTED_INDEX = "selectedIndex";
    private static final String SELECTED_INDICES_STRING = "selectedIndicesString";
    private static final String TEXT = "text";
    private static final String DIALOG_ACTION = "dialogAction";
    private MaterialDialog.Builder mBuilder;
    private MaterialDialog mDialog;

    public DialogAndroid(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
    }

    /* Apply the options to the provided builder */
    private static MaterialDialog.Builder applyOptions(MaterialDialog.Builder builder, ReadableMap options) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ReadableMapKeySetIterator iterator = options.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();

            switch (key) {
                case "title":
                    builder.title(options.getString("title"));
                    break;
                case "content":
                    if (options.hasKey("contentIsHtml") && options.getBoolean("contentIsHtml")) {
                        // // i have no idea how to get this to work, it seems its all api level 24 stuff
                        // // requires buildToolsVersion >= "24.0.1"
                        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //     builder.content(Html.fromHtml(options.getString("content"), Html.FROM_HTML_MODE_LEGACY | Html.FROM_HTML_MODE_COMPACT));
                        // } else {
                        //     builder.content(Html.fromHtml(options.getString("content"), Html.FROM_HTML_MODE_COMPACT));
                        // }
                        builder.content(Html.fromHtml(options.getString("content")));
                    } else {
                        builder.content(options.getString("content"));
                    }
                    break;
                case "positiveText":
                    builder.positiveText(options.getString("positiveText"));
                    break;
                case "positiveColor":
                    builder.positiveColor(options.getInt("positiveColor"));
                    break;
                case "negativeText":
                    builder.negativeText(options.getString("negativeText"));
                    break;
                case "negativeColor":
                    builder.negativeColor(options.getInt("negativeColor"));
                    break;
                case "neutralText":
                    builder.neutralText(options.getString("neutralText"));
                    break;
                case "neutralColor":
                    builder.neutralColor(options.getInt("neutralColor"));
                    break;
                case "titleColor":
                    builder.titleColor(options.getInt("titleColor"));
                    break;
                case "widgetColor":
                    builder.widgetColor(options.getInt("widgetColor"));
                    break;
                case "linkColor":
                    builder.linkColor(options.getInt("linkColor"));
                    break;
                case "contentColor":
                    builder.contentColor(options.getInt("contentColor"));
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
                    builder.stackingBehavior(
                            options.getBoolean("forceStacking") ? StackingBehavior.ALWAYS : StackingBehavior.ADAPTIVE);
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
                    builder.progressIndeterminateStyle(options.getBoolean("progressIndeterminateStyle"));
                    break;
                case "buttonsGravity":
                    String bg = options.getString("buttonsGravity");
                    if (bg.equals("start")) {
                        builder.buttonsGravity(GravityEnum.START);
                    } else if (bg.equals("end")) {
                        builder.buttonsGravity(GravityEnum.END);
                    } else {
                        builder.buttonsGravity(GravityEnum.CENTER);
                    }
                    break;
                case "itemsGravity":
                    String ig = options.getString("itemsGravity");
                    if (ig.equals("start")) {
                        builder.itemsGravity(GravityEnum.START);
                    } else if (ig.equals("end")) {
                        builder.itemsGravity(GravityEnum.END);
                    } else {
                        builder.itemsGravity(GravityEnum.CENTER);
                    }
                    break;
                case "titleGravity":
                    String tg = options.getString("titleGravity");
                    if (tg.equals("start")) {
                        builder.titleGravity(GravityEnum.START);
                    } else if (tg.equals("end")) {
                        builder.titleGravity(GravityEnum.END);
                    } else {
                        builder.titleGravity(GravityEnum.CENTER);
                    }
                    break;
                case "rtl":
                    if (options.getBoolean("rtl")) {
                        builder.titleGravity(GravityEnum.END);
                        builder.itemsGravity(GravityEnum.END);
                        builder.contentGravity(GravityEnum.END);
                        builder.buttonsGravity(GravityEnum.START);
                        builder.btnStackedGravity(GravityEnum.START);
                    }
                    break;
                case "checkboxLabel":
                    boolean defaultValue = options.hasKey("checkboxDefaultValue") && options.getBoolean("checkboxDefaultValue");
                    builder.checkBoxPrompt(options.getString("checkboxLabel"), defaultValue, null);
                    break;
                case "progress":
                    ReadableMap progress = options.getMap("progress");
                    boolean indeterminate = progress.hasKey("indeterminate") && progress.getBoolean("indeterminate");

                    if (indeterminate) {
                        builder.progress(true, 0);
                        boolean horizontal = progress.hasKey("style") && progress.getString("style").equals("horizontal");
                        if (horizontal) {
                            builder.progressIndeterminateStyle(horizontal);
                        }
                    } else {
                        // Determinate progress bar not supported currently
                        // TODO : Implement determinate progress bar
                    }
            }
        }

        return builder;
    }

    @Override
    public String getName() {
        return "DialogAndroid";
    }

    @ReactMethod
    public void show(ReadableMap options, final Promise promise) {
        try {
            this.mBuilder = new MaterialDialog.Builder(this.getCurrentActivity());
            DialogAndroid.applyOptions(this.mBuilder, options);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }

        try {
            if (options.hasKey("onAny")) {
                this.mBuilder.onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        WritableMap map = Arguments.createMap();
                        map.putString(DialogAndroid.KIND, "onAny");
                        map.putInt(DialogAndroid.DIALOG_ACTION, dialogAction.ordinal());
                        map.putBoolean(DialogAndroid.CHECKED, materialDialog.isPromptCheckBoxChecked());
                        promise.resolve(map);
                    }
                });
            }

            if (options.hasKey("itemsCallback")) {
                this.mBuilder.itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        WritableMap map = Arguments.createMap();
                        map.putString(DialogAndroid.KIND, "itemsCallback");
                        map.putInt(DialogAndroid.SELECTED_INDEX, i);
                        map.putBoolean(DialogAndroid.CHECKED, materialDialog.isPromptCheckBoxChecked());
                        promise.resolve(map);
                    }
                });
            }

            if (options.hasKey("itemsCallbackSingleChoice")) {
                // Check if there is a preselected index
                int selectedIndex = options.hasKey("selectedIndex") ? options.getInt("selectedIndex") : -1;
                this.mBuilder.itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        WritableMap map = Arguments.createMap();
                        map.putString(DialogAndroid.KIND, "itemsCallbackSingleChoice");
                        map.putInt(DialogAndroid.SELECTED_INDEX, i);
                        map.putBoolean(DialogAndroid.CHECKED, materialDialog.isPromptCheckBoxChecked());
                        promise.resolve(map);
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

                this.mBuilder.itemsCallbackMultiChoice(selectedIndices, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, Integer[] integers, CharSequence[] charSequences) {

                        // Concatenate selected IDs into a string
                        StringBuilder selected = new StringBuilder("");
                        for (int i = 0; i < integers.length - 1; i++) {
                            selected.append(integers[i]).append(",");
                        }
                        if (integers.length > 0) {
                            selected.append(integers[integers.length - 1]);
                        }

                        WritableMap map = Arguments.createMap();
                        map.putString(DialogAndroid.KIND, "itemsCallbackMultiChoice");
                        map.putString(DialogAndroid.SELECTED_INDICES_STRING, selected.toString());
                        map.putBoolean(DialogAndroid.CHECKED, materialDialog.isPromptCheckBoxChecked());
                        promise.resolve(map);
                        return true;
                    }
                });

                // Provide a 'Clear' button to unselect all choices
                if (options.hasKey("multiChoiceClearButton") && options.getBoolean("multiChoiceClearButton")) {
                    this.mBuilder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            materialDialog.clearSelectedIndices();
                        }
                    });
                }
            }

            if (options.hasKey("input")) {
                ReadableMap input = options.getMap("input");

                // Check for hint and prefilled text
                String hint = input.hasKey("hint") ? input.getString("hint") : null;
                String prefill = input.hasKey("prefill") ? input.getString("prefill") : null;

                // Check if empty input is allowed
                boolean allowEmptyInput = !input.hasKey("allowEmptyInput") || input.getBoolean("allowEmptyInput");

                // TODO : Provide pre-selected input types in Javascript
                if (input.hasKey("type")) {
                    this.mBuilder.inputType(input.getInt("type"));
                }

                int minLength = input.hasKey("minLength") ? input.getInt("minLength") : 0;
                int maxLength = input.hasKey("maxLength") ? input.getInt("maxLength") : -1;

                this.mBuilder.inputRange(minLength, maxLength);

                this.mBuilder.input(hint, prefill, allowEmptyInput, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                        WritableMap map = Arguments.createMap();
                        map.putString(DialogAndroid.KIND, "input");
                        map.putString(DialogAndroid.TEXT, charSequence.toString());
                        map.putBoolean(DialogAndroid.CHECKED, materialDialog.isPromptCheckBoxChecked());
                        promise.resolve(map);
                    }
                });
            }
            UiThreadUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DialogAndroid.this.mDialog != null) {
                        DialogAndroid.this.mDialog.dismiss();
                    }
                    DialogAndroid.this.mDialog = DialogAndroid.this.mBuilder.build();
                    DialogAndroid.this.mDialog.show();
                }
            });
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void dismiss() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }
}
