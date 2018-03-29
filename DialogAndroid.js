// @flow
import { NativeModules } from 'react-native'
import processColor from 'react-native/Libraries/StyleSheet/processColor'

import CONFIG from '../../config'

import type { ColorValue } from 'react-native/Libraries/StyleSheet/StyleSheetTypes'

type ListItem = string | { label:string } | {};

type BaseOptions = {|
    title?: string,
    titleColor?: ColorValue,
    content?: string,
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
    labelKey?: string, // required if items is array of objects without key of "label"
    listType?: typeof DialogAndroid.listPlain
|} | {|
    // radio list
    ...BaseOptions,
    items: ListItem[],
    labelKey?: string,
    widgetColor?: ColorValue, // radio color
    listType: typeof DialogAndroid.listRadio,
    selectedIndex?: number
|} | {|
    // radio list - automatic accept on item select
    ...BaseOptions,
    items: ListItem[],
    labelKey?: string,
    listType: typeof DialogAndroid.listRadio,
    widgetColor?: ColorValue,
    selectedIndex?: number,
    positiveText: null // this causes a press on the item to fire "select" action and close dialog
|} | {|
    // check list
    ...BaseOptions,
    items: ListItem[],
    labelKey?: string,
    listType: typeof DialogAndroid.listCheckbox,
    widgetColor?: ColorValue, // checkbox color
    selectedIndices?: number[]
|} | {|
    // check list with clear button
    ...BaseOptions,
    items: ListItem[],
    labelKey?: string,
    listType: typeof DialogAndroid.listCheckbox,
    widgetColor?: ColorValue,
    selectedIndices?: number[],
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

type Title = string;
type Content = string;

type AlertArgs =
  | [ Title, Content, Options ]
  | [ Title, Options ]
  | [ Content ]
  | [ Options ]

type AlertReturn = Promise< {
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
}>

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

    static dismiss() {
        NativeModules.DialogAndroid.dismiss();
    }

    static defaults = {

    }

    static setDefaults(defaults: { title?:Title, content?:Content, ...Options }) {
        this.defaults = defaults;
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

            const nativeConfig: NativeConfig = {
                title,
                content,
                negativeText: 'Cancel',
                positiveText: 'OK',
                ...this.defaults,
                ...options,
                onAny: true,
                dismissListener: true,
                cancelListener: true
            };
            // omit null?

            // process colors
            for (const prop of Object.keys(nativeConfig)) {
                if (prop.endsWith('Color')) {
                    nativeConfig[prop] = processColor(nativeConfig[prop]);
                }
            }

            // turn non-native properties to native properties
            if (nativeConfig.items) {
                const labelKey = nativeConfig.labelKey;
                delete nativeConfig.labelKey;
                nativeConfig.items = nativeConfig.items.map((item: ListItem) => {
                    if (typeof item === 'string') {
                        return item;
                    } else {
                        if (item.hasOwnProperty('label')) {
                            return item.label;
                        } else {
                            if (!labelKey) {
                                reject('DialogAndroid TypeError: labelKey is required because an object without key of "label" was found in items.');
                                throw new Error('DialogAndroid TypeError: labelKey is required because an object without key of "label" was found in items.');
                            }
                            return item[labelKey];
                        }
                    }
                })
                switch (nativeConfig.listType) {
                    case DialogAndroid.listCheckbox: nativeConfig.itemsCallbackMultiChoice = true; break;
                    case DialogAndroid.listRadio: nativeConfig.itemsCallbackSingleChoice = true; break;
                    default: nativeConfig.itemsCallback = true;
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
                            selectedItems =  [];
                        } else {
                            selectedItems =  selectedIndices.map(index => options.items[index]);
                        }
                        return resolve({ action:DialogAndroid.actionPositive, selectedItems });
                    }
                    case 'itemsCallback':
                    case 'itemsCallbackSingleChoice': {
                        const [ selectedIndex ] = rest;
                        const selectedItem = options.items[selectedIndex];
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
            contentColor: processColor(CONFIG.blackish),
            progress: {
                indeterminate: true,
                // style: 'horizontal'
            },
            widgetColor: processColor(CONFIG.backcolor),
            cancelable: false
        }, () => null);
    }
}

// (async function() {
//     console.log(await DialogAndroid.alert('Create Folder', 'Folder Name', {
//         input: {
//             allowEmptyInput: false
//         }
//     }));
// })()
// DialogAndroid.showProgress('loading...');
// setTimeout(DialogAndroid.dismiss, 5000);

export default DialogAndroid
