package com.aakashns.reactnativedialogs.modules;

import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Html;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.os.Build;
import android.view.WindowManager;

import com.aakashns.reactnativedialogs.R;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
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
import com.facebook.react.module.annotations.ReactModule;

import java.lang.reflect.InvocationTargetException;

@ReactModule(name = DialogAndroid.NAME)
public class DialogAndroid extends ReactContextBaseJavaModule {

    public static final String NAME = "DialogAndroid";

    @Override
    public String getName() {
        return NAME;
    }

    public DialogAndroid(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /* Apply the options to the provided builder */
    private MaterialDialog.Builder applyOptions(MaterialDialog.Builder builder, ReadableMap options) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ReadableMapKeySetIterator iterator = options.keySetIterator();
        while(iterator.hasNextKey()) {
            String key = iterator.nextKey();

            switch (key) {
                case "title":
                    builder.title(options.getString("title"));
                    break;
                case "content":
                    if(options.hasKey("contentIsHtml") && options.getBoolean("contentIsHtml")) {
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
                case "backgroundColor":
                    builder.backgroundColor(options.getInt("backgroundColor"));
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
    private boolean mCallbackConsumed = false;

    @ReactMethod
    public void show(ReadableMap options, final Callback callback) {
        if (getCurrentActivity() == null) {
            callback.invoke("error", "React Native Activity is null", options.toString());
            return;
        }
        mBuilder = new MaterialDialog.Builder(getCurrentActivity());
        try {
            applyOptions(mBuilder, options);
        } catch (Exception e) {
            callback.invoke("error", e.getMessage(), options.toString());
            return;
        }

        if (options.hasKey("onPositive")) {
            mBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("onPositive");
                    }
                }
            });
        }

        if (options.hasKey("onNegative")) {
            mBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("onNegative");
                    }
                }
            });
        }

        if (options.hasKey("onNeutral")) {
            mBuilder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("onNeutral");
                    }
                }
            });
        }

        if (options.hasKey("onAny")) {
            mBuilder.onAny(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        if (dialogAction == DialogAction.POSITIVE) {
                            callback.invoke("onAny", 0, materialDialog.isPromptCheckBoxChecked());
                        } else if (dialogAction == DialogAction.NEUTRAL) {
                            callback.invoke("onAny", 1, materialDialog.isPromptCheckBoxChecked());
                        } else {
                            callback.invoke("onAny", 2, materialDialog.isPromptCheckBoxChecked());
                        }
                    }
                }
            });
        }

        if (options.hasKey("itemsCallback")) {
            mBuilder.itemsCallback(new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("itemsCallback", i, materialDialog.isPromptCheckBoxChecked());
                    }
                }
            });
        }

        if (options.hasKey("itemsCallbackSingleChoice")) {
            // Check if there is a preselected index
            int selectedIndex = options.hasKey("selectedIndex") ? options.getInt("selectedIndex") : -1;
            mBuilder.itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        charSequence = charSequence == null ? "" : charSequence;
                        callback.invoke("itemsCallbackSingleChoice", i, materialDialog.isPromptCheckBoxChecked());
                    }
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

            mBuilder.itemsCallbackMultiChoice(selectedIndices, new MaterialDialog.ListCallbackMultiChoice() {
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

                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("itemsCallbackMultiChoice", selected.toString(), materialDialog.isPromptCheckBoxChecked());
                    }
                    return true;
                }
            });

            // Provide a 'Clear' button to unselect all choices
            if (options.hasKey("multiChoiceClearButton") && options.getBoolean("multiChoiceClearButton")) {
                mBuilder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.clearSelectedIndices();
                    }
                });
            }
        }

        mBuilder.showListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // if (!mCallbackConsumed) {
                //     mCallbackConsumed = true;
                //     callback.invoke("showListener");
                // }
                mCallbackConsumed = false;
            }
        });

        if (options.hasKey("cancelListener")) {
            mBuilder.cancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("cancelListener");
                    }
                }
            });
        }

        if (options.hasKey("dismissListener")) {
            mBuilder.dismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("dismissListener");
                    }
                }
            });
        }

        if (options.hasKey("input")) {
            ReadableMap input = options.getMap("input");

            // Check for hint and prefilled text
            String hint = input.hasKey("hint") ? input.getString("hint") : null;
            String prefill = input.hasKey("prefill") ? input.getString("prefill") : null;

            // Check if empty input is allowed
            boolean allowEmptyInput = !input.hasKey("allowEmptyInput") || input.getBoolean("allowEmptyInput");

            if (input.hasKey("keyboardType")) {
                switch (input.getString("keyboardType")) {
                    case "phone-pad":
                        mBuilder.inputType(InputType.TYPE_CLASS_PHONE);
                        break;

                    case "number-pad":
                        mBuilder.inputType(InputType.TYPE_CLASS_NUMBER);
                        break;

                    case "decimal-pad":
                        mBuilder.inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        break;

                    case "numeric":
                        mBuilder.inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        break;

                    case "numeric-password":
                        mBuilder.inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        break;

                    case "email-address":
                        mBuilder.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        break;

                    case "password":
                        mBuilder.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;

                    case "url":
                        mBuilder.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_URI);
                        break;

                    default:
                        mBuilder.inputType(InputType.TYPE_CLASS_TEXT);
                }
            }

            int minLength = input.hasKey("minLength") ? input.getInt("minLength") : 0;
            int maxLength = input.hasKey("maxLength") ? input.getInt("maxLength") : -1;

            mBuilder.inputRange(minLength, maxLength);

            mBuilder.input(hint, prefill, allowEmptyInput, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                    if (!mCallbackConsumed) {
                        mCallbackConsumed = true;
                        callback.invoke("input", charSequence.toString(), materialDialog.isPromptCheckBoxChecked());
                    }
                }
            });
        }

        final int numberOfItems;
        if (options.hasKey("maxNumberOfItems")) {
            numberOfItems = options.getInt("maxNumberOfItems");
        }else{
            numberOfItems = -1;
        }

        UiThreadUtil.runOnUiThread(new Runnable() {
            public void run() {
                if (mDialog != null)
                    mDialog.dismiss();
                mDialog = mBuilder.build();

                if(numberOfItems > 0) {
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(mDialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;

                    int dp = (int) (getReactApplicationContext().getResources().getDimension(R.dimen.md_listitem_height)
                            / getReactApplicationContext().getResources().getDisplayMetrics().density);


                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp * (numberOfItems +3), getReactApplicationContext().getResources().getDisplayMetrics());


                    lp.height = (int) pixels;
                    mDialog.getWindow().setAttributes(lp);
                }


                mDialog.show();
            }
        });
    }

    MaterialDialog simple;
    @ReactMethod
    public void list(ReadableMap options, final Callback callback) {
        final MaterialSimpleListAdapter simpleListAdapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                if (!mCallbackConsumed) {
                    mCallbackConsumed = true;
                    callback.invoke(index, item.getContent());
                }
                if (simple != null) {
                    simple.dismiss();
                }
            }
        });

        ReadableArray arr = options.getArray("items");
        for(int i = 0; i < arr.size(); i++){
            simpleListAdapter.add(new MaterialSimpleListItem.Builder(getCurrentActivity())
                    .content(arr.getString(i))
                    .build());
        }

        final MaterialDialog.Builder adapter = new MaterialDialog.Builder(getCurrentActivity())
                .title(options.hasKey("title") ? options.getString("title") : "")
                .adapter(simpleListAdapter, null)
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
