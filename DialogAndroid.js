// @flow
import { NativeModules } from 'react-native'
import processColor from 'react-native/Libraries/StyleSheet/processColor'

import type { ColorValue } from 'react-native/Libraries/StyleSheet/StyleSheetTypes'

type IdKey = string | 'id';
type LabelKey = string | 'label';
type ListItem = { label:string, id?:any };

type OptionsAlert = {|
    ...OptionsCommon,
|}

type OptionsCommon = {|
    title?: null | string,
    titleColor?: ColorValue,
    content?: null | string,
    contentIsHtml?: boolean,
    contentColor?: string,
    positiveText?: string, // default "OK"
    negativeText?: string,
    neutralText?: string,
    positiveColor?: ColorValue,
    negativeColor?: ColorValue,
    neutralColor?: ColorValue,
    cancelable?: boolean,
    linkColor?: ColorValue, // applies if contentIsHtml is true, and there are <a> elements in content string
    forceStacking?: boolean,
    checkboxLabel?: string,
    checkboxDefaultValue?: boolean
|}

type ListItemJustLabel = { label:string };
type ListItemJustId = { id:string };
type ListItemFull = { label:string, id:any };
type ListItemBare = {};

type OptionsRadioList = {|
    maxNumberOfItems?: number,
    type: typeof ListType.listRadio,
    widgetColor?: ColorValue // radio color
|}
type OptionsCheckboxList = {|
    maxNumberOfItems?: number,
    type: typeof ListType.listCheckbox,
    neutralIsClear?: boolean,
    widgetColor?: ColorValue // checkbox color
|}

type OptionsPicker = {|
    ...OptionsCommon,
    type?: typeof ListType.listPlain,
    maxNumberOfItems?: number,
    items: ListItemJustLabel[],
|} | {|
    ...OptionsCommon,
    type?: typeof ListType.listPlain,
    maxNumberOfItems?: number,
    items: ListItemBare[],
    labelKey: string
|} | {|
    // radio - no preselected
    ...OptionsCommon,
    ...OptionsRadioList,
    items: ListItemJustLabel[],
|} | {|
    // radio - no preselected
    ...OptionsCommon,
    ...OptionsRadioList,
    items: ListItemBare[],
    labelKey: string
|} | {|
    // radio - preselected - ListItemFull
    ...OptionsCommon,
    ...OptionsRadioList,
    items: ListItemFull[],
    selectedId: any
|} | {|
    // radio - preselected - ListItemJustlabel
    ...OptionsCommon,
    ...OptionsRadioList,
    items: ListItemJustLabel[],
    idKey: string,
    selectedId: any
|} | {|
    // radio - preselected - ListItemJustId
    ...OptionsCommon,
    ...OptionsRadioList,
    items: ListItemJustId[],
    labelKey: string,
    selectedId: any
|} | {|
    // radio - preselected - ListItemBare
    ...OptionsCommon,
    ...OptionsRadioList,
    items: ListItemBare[],
    idKey: string,
    labelKey: string,
    selectedId: any
|} | {|
    // checklist - no preselected - ListItemJustLabel
    ...OptionsCommon,
    ...OptionsCheckboxList,
    items: ListItemJustLabel[]
|} | {|
    // checklist - no preselected - ListItemBare
    ...OptionsCommon,
    ...OptionsCheckboxList,
    items: ListItemBare[],
    labelKey: string
|} | {|
    // checklist - preselected - ListItemFull
    ...OptionsCommon,
    ...OptionsCheckboxList,
    items: ListItemFull[],
    selectedIds: any[]
|} | {|
    // checklist - preselected - ListItemJustlabel
    ...OptionsCommon,
    ...OptionsCheckboxList,
    items: ListItemJustLabel[],
    idKey: string,
    selectedIds: any
|} | {|
    // checklist - preselected - ListItemJustId
    ...OptionsCommon,
    ...OptionsCheckboxList,
    items: ListItemJustId[],
    labelKey: string,
    selectedIds: any
|} | {|
    // checklist - preselected - ListItemBare
    ...OptionsCommon,
    ...OptionsCheckboxList,
    items: ListItemBare[],
    idKey: string,
    labelKey: string,
    selectedIds: any
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

type Options = OptionsAlert | OptionsPicker | OptionsProgress | OptionsPrompt;

type OptionsProgress = {|
    contentColor?: $PropertyType<OptionsCommon, 'contentColor'>,
    contentIsHtml?: $PropertyType<OptionsCommon, 'contentIsHtml'>,
    linkColor?: $PropertyType<OptionsCommon, 'linkColor'>,
    style?: ProgressStyle,
    title?: $PropertyType<OptionsCommon, 'title'>,
    titleColor?: $PropertyType<OptionsCommon, 'titleColor'>,
    widgetColor?: $PropertyType<OptionsCommon, 'widgetColor'>
|}

type ProgressStyle = typeof DialogAndroid.progressHorizontal;

type OptionsPrompt = {|
    ...OptionsCommon,
    keyboardType?: 'numeric' | 'number-pad' | 'decimal-pad' | 'numeric-password' | 'email-address' | 'password' | 'phone-pad' | 'url',
    defaultValue?: string,
    placeholder?: string,
    allowEmptyInput?: boolean,
    minLength?: number,
    maxLength?: number
|}

type Title = void | null | string;
type Content = void | null | string;


type NativeConfig = {|
    ...OptionsCommon,
    items: string[],
    widgetColor?: ColorValue,
    selectedIndices?: number[],
    selectedIndex?: number[],
    progress?: {
        indeterminate: true,
        style?: 'horizontal'
    }
|}

function processColors(nativeConfig: {}) {
    for (const prop of Object.keys(nativeConfig)) {
        if (prop.endsWith('Color')) {
            nativeConfig[prop] = processColor(nativeConfig[prop]);
        }
    }
}

function pick(source, ...keys) {
    const target = {};
    for (const key of keys) {
        if (source.hasOwnProperty(key)) {
            target[key] = source[key];
        }
    }
    return target;
}

class DialogAndroid {
    static listPlain = 'listPlain'
    static listRadio = 'listRadio'
    static listCheckbox = 'listCheckbox'
    static actionDismiss = 'actionDismiss'
    static actionNegative = 'actionNegative'
    static actionNeutral = 'actionNeutral'
    static actionPositive = 'actionPositive'
    static actionSelect = 'actionSelect'
    static progressHorizontal = 'progressHorizontal'

    static defaults = {
        positiveText: 'OK'
    }

    static dismiss(): void {
        NativeModules.DialogAndroid.dismiss();
    }

    static assignDefaults(defaults: { title?:Title, content?:Content, ...Options }): void {
        Object.assign(DialogAndroid.defaults, defaults);
    }

    static alert(title: Title, content: Content, options?: OptionsAlert = {}): Promise<
        {| action: typeof DialogAndroid.actionPositive | typeof DialogAndroid.actionNegative | typeof DialogAndroid.actionNeutral | typeof DialogAndroid.actionDismiss |} |
        {| action: typeof DialogAndroid.actionPositive | typeof DialogAndroid.actionNegative | typeof DialogAndroid.actionNeutral, checked: boolean |}
    > {
        return new Promise((resolve, reject) => {
            const nativeConfig: NativeConfig = {
                ...DialogAndroid.defaults,
                ...options,
                onAny: true,
                dismissListener: true
            };
            if (title) nativeConfig.title = title;
            if (content) nativeConfig.content = content;

            processColors(nativeConfig);

            NativeModules.DialogAndroid.show(nativeConfig, (kind: string, ...rest) => {
                switch (kind) {
                    case 'error': {
                        const [ error, nativeConfig ] = rest;
                        return reject(`DialogAndroid ${error}. nativeConfig: ${nativeConfig}`);
                    }
                    case 'dismissListener': {
                        return resolve({ action:DialogAndroid.actionDismiss });
                    }
                    case 'onAny': {
                        const [ dialogAction, checked ] = rest;
                        switch (dialogAction) {
                            case 0: return resolve({ action:DialogAndroid.actionPositive, ...getChecked(nativeConfig, checked) });
                            case 1: return resolve({ action:DialogAndroid.actionNeutral, ...getChecked(nativeConfig, checked) });
                            case 2: return resolve({ action:DialogAndroid.actionNegative, ...getChecked(nativeConfig, checked) });
                        }
                    }
                    default: {
                        return reject(`Unknown callback kind: "${kind}"`);
                    }
                }
            });
        });
    }

    static showPicker(title: Title, content: Content, options: OptionsPicker): Promise<
        {| action: typeof DialogAndroid.actionNegative | typeof DialogAndroid.actionNeutral | typeof DialogAndroid.actionDismiss |} |
        {| action: typeof DialogAndroid.actionNegative | typeof DialogAndroid.actionNeutral, checked: boolean |} |
        {| action: typeof DialogAndroid.actionSelect, selectedItem: ListItem |} |
        {| action: typeof DialogAndroid.actionSelect, selectedItem: ListItem, checked: boolean |} |
        {| action: typeof DialogAndroid.actionSelect, selectedItems: ListItem[] |} |
        {| action: typeof DialogAndroid.actionSelect, selectedItems: ListItem[], checked: boolean |}
    > {
        // options is required, must defined items

        return new Promise((resolve, reject) => {

            const {
                idKey='id',
                items,
                labelKey='label',
                type,
                neutralIsClear,
                selectedId,
                selectedIds,
                ...filteredOptions
            } = options;

            const nativeConfig: NativeConfig = {
                ...DialogAndroid.defaults,
                ...filteredOptions,
                onAny: true,
                dismissListener: true
            };
            if (title) nativeConfig.title = title;
            if (content) nativeConfig.content = content;

            if (items) {
                nativeConfig.items = items.map(item => item[labelKey]);
                switch (type) {
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

            if (neutralIsClear) nativeConfig.multiChoiceClearButton = true;

            processColors(nativeConfig);

            NativeModules.DialogAndroid.show(nativeConfig, (kind: string, ...rest) => {
                switch (kind) {
                    case 'error': {
                        const [ error, nativeConfig ] = rest;
                        return reject(`DialogAndroid ${error}. nativeConfig: ${nativeConfig}`);
                    }
                    case 'itemsCallbackMultiChoice': {
                        const [selectedIndicesString, checked] = rest; // blank string when nothing selected
                        const selectedItems = selectedIndicesString === '' ? [] : selectedIndicesString.split(',').map(index => items[index]);

                        return resolve({ action:DialogAndroid.actionPositive, selectedItems, ...getChecked(nativeConfig, checked) });
                    }
                    case 'itemsCallback':
                    case 'itemsCallbackSingleChoice': {
                        const [ selectedIndex, checked ] = rest;
                        const selectedItem = items[selectedIndex];
                        return resolve({ action:DialogAndroid.actionSelect, selectedItem, ...getChecked(nativeConfig, checked) });
                    }
                    case 'onAny': {
                        const [ dialogAction, checked ] = rest;
                        switch (dialogAction) {
                            case 0: return resolve({ action:DialogAndroid.actionPositive, ...getChecked(nativeConfig, checked) });
                            case 1: return resolve({ action:DialogAndroid.actionNeutral, ...getChecked(nativeConfig, checked) });
                            case 2: return resolve({ action:DialogAndroid.actionNegative, ...getChecked(nativeConfig, checked) });
                        }
                    }
                    case 'dismissListener': {
                        return resolve({ action:DialogAndroid.actionDismiss });
                    }
                    default: {
                        return reject(`Unknown callback kind: "${kind}"`);
                    }
                }
            });
        })
    }

    static showProgress(content: string, options?: OptionsProgress = {}): Promise<
        {|
            action: typeof DialogAndroid.actionDismiss
        |}
    > {

        return new Promise((resolve, reject) => {
            const defaults = pick(DialogAndroid.defaults,
                'contentColor',
                'contentIsHtml',
                'linkColor',
                'title',
                'widgetColor',
                'titleColor'
            )

            const {
                style,
                ...finalOptions
            } = options;

            const nativeConfig = {
                ...defaults,
                progress: {
                    indeterminate: true,
                    style: style === DialogAndroid.progressHorizontal ? 'horizontal' : undefined
                },
                cancelable: false,
                ...finalOptions,
                dismissListener: true
            }
            if (content) nativeConfig.content = content;
            if (content && style !== DialogAndroid.progressHorizontal) nativeConfig.content = '     ' + content;
            processColors(nativeConfig);

            NativeModules.DialogAndroid.show(nativeConfig, (kind: string, ...rest) => {
                switch (kind) {
                    case 'error': {
                        const [ error, nativeConfig ] = rest;
                        return reject(`DialogAndroid ${error}. nativeConfig: ${nativeConfig}`);
                    }
                    case 'dismissListener': {
                        return resolve({ action:DialogAndroid.actionDismiss });
                    }
                }
            });
        })
    }

    static prompt(title: Title, content: Content, options?: OptionsPrompt = {}): Promise<
        {| action: typeof DialogAndroid.actionNegative | typeof DialogAndroid.actionNeutral | typeof DialogAndroid.actionDismiss |} |
        {| action: typeof DialogAndroid.actionNegative | typeof DialogAndroid.actionNeutral, checked: boolean |} |
        {| action: typeof DialogAndroid.actionPositive, text: string |} |
        {| action: typeof DialogAndroid.actionPositive, text: string, checked: boolean |}
    > {
        return new Promise((resolve, reject) => {

            const {
                keyboardType,
                defaultValue,
                placeholder,
                allowEmptyInput,
                minLength,
                maxLength,
                ...finalOptions
            } = options;

            const inputConfig = {};
            if (defaultValue) inputConfig.prefill = defaultValue;
            if (placeholder) inputConfig.hint = placeholder;
            if (allowEmptyInput !== undefined) inputConfig.allowEmptyInput = allowEmptyInput;
            if (minLength) inputConfig.minLength = minLength;
            if (maxLength) inputConfig.maxLength = maxLength;
            if (keyboardType) inputConfig.keyboardType = keyboardType;

            const nativeConfig = {
                ...DialogAndroid.defaults,
                input: inputConfig,
                ...finalOptions,
                onAny: true,
                dismissListener: true
            }
            if (title) nativeConfig.title = title;
            if (content) nativeConfig.content = content;

            processColors(nativeConfig);

            NativeModules.DialogAndroid.show(nativeConfig, (kind: string, ...rest) => {
                switch (kind) {
                    case 'error': {
                        const [ error, nativeConfig ] = rest;
                        return reject(`DialogAndroid ${error}. nativeConfig: ${nativeConfig}`);
                    }
                    case 'onAny': {
                        const [ dialogAction, checked ] = rest;
                        switch (dialogAction) {
                            case 1: return resolve({ action:DialogAndroid.actionNeutral, ...getChecked(nativeConfig, checked) });
                            case 2: return resolve({ action:DialogAndroid.actionNegative, ...getChecked(nativeConfig, checked) });
                        }
                    }
                    case 'input': {
                        const [ text, checked ] = rest;
                        return resolve({ action:DialogAndroid.actionPositive, text, ...getChecked(nativeConfig, checked) });
                    }
                    case 'dismissListener': {
                        return resolve({ action:DialogAndroid.actionDismiss });
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

        })
    }
}

function getChecked(nativeConfig, checked) {
    return nativeConfig.checkboxLabel ? { checked } : {};
}

export default DialogAndroid
