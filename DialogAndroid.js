// @flow
import { NativeModules } from 'react-native'
import processColor from 'react-native/Libraries/StyleSheet/processColor'

import type { ColorValue } from 'react-native/Libraries/StyleSheet/StyleSheetTypes'

type IdKey = string | 'id';
type LabelKey = string | 'label';
type ListItem = { label:string, id?:any };

type BaseOptions = {|
    title?: null | string,
    titleColor?: ColorValue,
    content?: null | string,
    isContentHtml?: boolean,
    contentColor?: string,
    positiveText?: null | string, // default "OK"
    negativeText?: null | string, // default "CANCEL"
    neutralText?: string,
    positiveColor?: ColorValue,
    negativeColor?: ColorValue,
    neutralColor?: ColorValue,
    cancelable?: boolean,
    linkColor?: ColorValue, // applies if isContentHtml is true, and there are <a> elements in content string
    forceStacking?: boolean
|}


type ListType =
  | typeof DialogAndroid.listCheckbox
  | typeof DialogAndroid.listPlain
  | typeof DialogAndroid.listRadio;

type ActionType =
  | typeof DialogAndroid.actionDismiss
  | typeof DialogAndroid.actionNegative
  | typeof DialogAndroid.actionNeutral
  | typeof DialogAndroid.actionPositive
  | typeof DialogAndroid.actionSelect;

type Options = BaseOptions | {|
    // plain list
    ...BaseOptions,
    items: ListItem[],
    labelKey?: LabelKey, // required if items is array of objects without key of "label"
    listType?: typeof DialogAndroid.listPlain
|} | {|
    // radio list
    ...BaseOptions,
    items: ListItem[],
    labelKey?: LabelKey,
    idKey?: IdKey,
    widgetColor?: ColorValue, // radio color
    listType: typeof DialogAndroid.listRadio,
    selectedId?: any
|} | {|
    // radio list - automatic accept on item select
    ...BaseOptions,
    items: ListItem[],
    labelKey?: LabelKey,
    idKey?: IdKey,
    listType: typeof DialogAndroid.listRadio,
    widgetColor?: ColorValue,
    selectedId?: any,
    positiveText: null // this causes a press on the item to fire "select" action and close dialog
|} | {|
    // check list
    ...BaseOptions,
    items: ListItem[],
    labelKey?: LabelKey,
    idKey?: IdKey,
    listType: typeof DialogAndroid.listCheckbox,
    widgetColor?: ColorValue, // checkbox color
    selectedIds?: number[]
|} | {|
    // check list with clear button
    ...BaseOptions,
    items: ListItem[],
    labelKey?: LabelKey,
    idKey?: IdKey,
    listType: typeof DialogAndroid.listCheckbox,
    widgetColor?: ColorValue,
    selectedIds?: number[],
    neutralText: string, // must set a text string here. like "Clear". before it used to force set "Clear" but this was not language-localization-internationalization friendly
    shouldNeutralClear?: boolean // causes neutral button to trigger actionSelect with selectedItems being an empty array
|} | {|
    // input text
    ...BaseOptions,
    input: {
        hint?: string,
        prefill?: string,
        allowEmptyInput?: boolean,
        minLength?: number,
        maxLength?: number,
        type?: number
    },
    widgetColor?: ColorValue // underline color, cursor color
|}

type Title = void | null | string;
type Content = void | null | string;

type AlertArgs =
  | [ Title, Content, Options ]
  | [ Title, Options ]
  | [ Content ]
  | [ Options ]

type AlertReturn = {
      action: ActionType
} | {
    action: typeof DialogAndroid.actionSelect, // even if hit positive button, it comes in as select i think
    selectedItem: ListItem
} | {
    action: typeof DialogAndroid.actionPositive,
    selectedItems: ListItem[]
} | {
    action: typeof DialogAndroid.actionPositive,
    text: string
}

type NativeConfig = {|
    ...BaseOptions,
    items: string[],
    widgetColor?: ColorValue,
    selectedIndices?: number[],
    selectedIndex?: number[],
    progress?: {
        indeterminate: true,
        style?: 'horizontal'
    }
|}

class DialogAndroid {
    static listPlain = 'listPlain'
    static listRadio = 'listRadio'
    static listCheckbox = 'listCheckbox'
    static actionDismiss = 'actionDismiss'
    static actionNegative = 'actionNegative'
    static actionNeutral = 'actionNeutral'
    static actionPositive = 'actionPositive'
    static actionSelect = 'actionSelect'

    static defaults = {
        negativeText: 'Cancel',
        positiveText: 'OK'
    }

    static dismiss() {
        NativeModules.DialogAndroid.dismiss();
    }

    static assignDefaults(defaults: { title?:Title, content?:Content, ...Options }) {
        Object.assign(this.defaults, defaults);
    }
    static alert(...args: AlertArgs): Promise<AlertReturn> {
        return new Promise((resolve, reject) => {

            let title, content, options;
            if (args.length === 3) {
                ([title, content, options={}] = args);
            } else if (args.length === 2) {
                ([title, options={}] = args);
            } else if (args.length === 1) {
                if (typeof args[0] === 'string') {
                    content = args[0];
                    options = {};
                } else {
                    options = args[0];
                }
            }

            let contentIsHtml, idKey, items, labelKey, listType, neutralIsClear, selectedId, selectedIds;
            ({ contentIsHtml, idKey='id', items, labelKey='label', listType=DialogAndroid.listPlain, neutralIsClear, selectedId, selectedIds, ...options} = options);

            const nativeConfig: NativeConfig = {
                ...this.defaults,
                ...options,
                onAny: true,
                dismissListener: true,
                cancelListener: true
            };
            if (title) nativeConfig.title = title;
            if (content) nativeConfig.content = content;
            // omit null?

            // process colors
            for (const prop of Object.keys(nativeConfig)) {
                if (prop.endsWith('Color')) {
                    nativeConfig[prop] = processColor(nativeConfig[prop]);
                }
            }

            // turn non-native properties to native properties
            if (items) {
                nativeConfig.items = items.map(item => item[labelKey]);
                switch (listType) {
                    case DialogAndroid.listCheckbox: {
                            nativeConfig.itemsCallbackMultiChoice = true;
                            if (selectedIds) {
                                nativeConfig.selectedIndices = selectedIds.map(id => items.findIndex(item => item[idKey] === id));
                            }
                        break;
                    }
                    case DialogAndroid.listRadio: {
                            nativeConfig.itemsCallbackSingleChoice = true;
                            if (selectedId !== undefined) {
                                nativeConfig.selectedIndex = items.findIndex(item => item[idKey] === selectedId);
                            }
                        break;
                    }
                    default:
                        nativeConfig.itemsCallback = true;
                }
            }
            if (nativeConfig.shouldNeutralClear) {
                delete nativeConfig.shouldNeutralClear;
                nativeConfig.multiChoiceClearButton = true;
            }

            NativeModules.DialogAndroid.show(nativeConfig, (kind: string, ...rest) => {
                switch (kind) {
                    case 'error': {
                        const [ error, nativeConfig ] = rest;
                        return reject(`DialogAndroid ${error}. nativeConfig: ${nativeConfig}`);
                    }
                    case 'itemsCallbackMultiChoice': {
                        const selectedIndices = rest[0].split(',');

                        let selectedItems;
                        if (selectedIndices.length === 1 && isNaN(selectedIndices[0])) {
                            // the case of empty selection
                            selectedItems = [];
                        } else {
                            selectedItems = selectedIndices.map(index => items[index]);
                        }
                        return resolve({ action:DialogAndroid.actionPositive, selectedItems });
                    }
                    case 'itemsCallback':
                    case 'itemsCallbackSingleChoice': {
                        const [ selectedIndex ] = rest;
                        const selectedItem = items[selectedIndex];
                        return resolve({ action:DialogAndroid.actionSelect, selectedItem });
                    }
                    case 'onAny': {
                        const [ dialogAction ] = rest;
                        switch (dialogAction) {
                            case 0: return resolve({ action:DialogAndroid.actionPositive });
                            case 1: return resolve({ action:DialogAndroid.actionNeutral });
                            case 2: return resolve({ action:DialogAndroid.actionNegative });
                        }
                    }
                    case 'dismissListener': {
                        return resolve({ action:DialogAndroid.actionDismiss });
                    }
                    case 'input': {
                        const [ text ] = rest;
                        return resolve({ action:DialogAndroid.actionPositive, text });
                    }
                    case 'cancelListener': {
                        // fires when input text field is there and hit back or in back to dismiss
                        return resolve({ action:DialogAndroid.actionDismiss });
                    }
                    default: {
                        return reject(`Unknown callback kind: "${kind}"`);
                    }
                }
            });
        });
    }

    static showProgress(message: string) {
        NativeModules.DialogAndroid.show({
            content: message.padStart(5),
            contentColor: DialogAndroid.defaults.contentColor ? processColor(DialogAndroid.defaults.contentColor) : undefined,
            progress: {
                indeterminate: true,
                // style: 'horizontal'
            },
            widgetColor: DialogAndroid.defaults.widgetColor ? processColor(DialogAndroid.defaults.widgetColor) : undefined,
            cancelable: false
        }, () => null);
    }
}

export default DialogAndroid
