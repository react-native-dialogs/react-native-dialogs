## react-native-dialogs
[![All Contributors](https://img.shields.io/badge/all_contributors-25-orange.svg?style=flat-square)](#contributors)

An Android only module for Material Design dialogs. This is a wrapper over [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs). This module is designed for Android only with no plans to support iOS.

### Table of Contents
  - [Installation](#installation)
    - [Manual Linking](#manual-linking)
  - [Usage](#usage)
  - [API](#api)
    - [Properties](#properties)
      - [`defaults`](#defaults)
      - [`actionDismiss`](#actiondismiss)
      - [`actionNegative`](#actionnegative)
      - [`actionNeutral`](#actionneutral)
      - [`actionPositive`](#actionpositive)
      - [`listPlain`](#listplain)
      - [`listRadio`](#listradio)
      - [`listCheckbox`](#listcheckbox)
      - [`progressHorizontal`](#progresshorizontal)
    - [Methods](#methods)
      - [`alert`](#alert)
      - [`assignDefaults`](#assigndefaults)
      - [`dismiss`](#dismiss)
      - [`prompt`](#prompt)
      - [`showPicker`](#showpicker)
      - [`showProgress`](#showprogress)
  - [Types](#types)
    - [Internal Types](#internal-types)
      - [`type ActionType`](#type-actiontype)
      - [`type ListItem`](#type-listitem)
      - [`type ListType`](#type-listtype)
      - [`type OptionsAlert`](#type-optionsalert)
      - [`type OptionsCommon`](#type-optionscommon)
      - [`type OptionsProgress`](#type-optionsprogress)
      - [`type OptionsPicker`](#type-optionspicker)
      - [`type OptionsPrompt`](#type-optionsprompt)
      - [`type ProgressStyle`](#type-progressstyle)
  - [Examples](#examples)
    - [Progress Dialog](#progress-dialog)
    - [Basic List](#basic-list)
    - [Radio List](#radio-list)
      - [Without auto-dismiss](#without-auto-dismiss)
    - [Checklist](#checklist)
      - [With clear list button](#with-clear-list-button)
    - [Prompt](#prompt-1)
    - [HTML](#html)
    - [assignDefaults](#assigndefaults-1)
- [Contributors](#contributors)

### Installation

1. Install:
    - Using [npm](https://www.npmjs.com/#getting-started): `npm install react-native-dialogs --save`
    - Using [Yarn](https://yarnpkg.com/): `yarn add react-native-dialogs`

2. [Link](https://facebook.github.io/react-native/docs/linking-libraries-ios.html):
    - `react-native link react-native-dialogs`
    - Or if this fails, link manually using [these steps](#manual-linking)

3. Compile application using `react-native run-android`

#### Manual Linking
Follow these steps if automatic linking (`react-native link`) failed.

1. In `android/app/build.gradle`, add the dependency to your app build:

   ```
   dependencies {
       ...
       compile project(':react-native-dialogs') // Add this
   }
   ```

2. In `android/build.gradle`, it should already be there, but in case it is not, add Jitpack maven repository to download the library [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs):

   ```
   allprojects {
       repositories {
           ...
           jcenter() // Add this if it is not already here
           ...
       }
   }
   ```

3. In `android/settings.gradle`:

   ```
   rootProject.name = ...
   ...
   include ':react-native-dialogs' // Add this
   project(':react-native-dialogs').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-dialogs/android') // Add this

   ...
   include ':app'
   ```

4. Import and add package, in `android/app/src/main/.../MainApplication.java`:

   ```java
   ...
   import android.app.Application;
   ...

   import com.aakashns.reactnativedialogs.ReactNativeDialogsPackage; // Add new import

   ...

   public class MainApplication extends Application implements ReactApplication {
     ...
     @Override
     protected List<ReactPackage> getPackages() {
       return Arrays.<ReactPackage>asList(
         new MainReactPackage(),
         ...
         new ReactNativeDialogsPackage() // Add the package here
       );
     }
   }
   ```

### Usage

1. Import it in your JS:

   ```js
   import DialogAndroid from 'react-native-dialogs';
   ```

2. Call API:

   ```js
   class Blah extends Component {
       render() {
           return <Button title="Show DialogAndroid" onPress={this.showDialogAndroid} />
       }

       showDialogAndroid = async () => {
           const { action } = await DialogAndroid.alert('Title', 'Message');
           switch (action) {
               case DialogAndroid.actionPositive:
                   console.log('positive!')
                   break;
               case DialogAndroid.actionNegative:
                   console.log('negative!')
                   break;
               case DialogAndroid.actionNeutral:
                   console.log('neutral!')
                   break;
               case DialogAndroid.actionDismiss:
                   console.log('dismissed!')
                   break;
           }
       }
   }
   ```

### API

#### Properties

##### `defaults`

>    {
>        positiveText: 'OK'
>    }

The default options to be used by all methods. To modify this, either directly manipulate with `DialogAndroid.defaults = { ... }` or use [`assignDefaults`](#assigndefaults)

##### `actionDismiss`

> static actionDismiss = "actionDismiss"

##### `actionNegative`

> static actionNegative = "actionNegative"

##### `actionNeutral`

> static actionNeutral = "actionNeutral"

##### `actionPositive`

> static actionPositive = "actionPositive"

##### `listPlain`

> static listPlain = "listPlain"

##### `listRadio`

> static listRadio = "listRadio"

##### `listCheckbox`

> static listCheckbox = "listCheckbox"

##### `progressHorizontal`

> static progressHorizontal = "progressHorizontal"

#### Methods
##### `alert`

>     static alert(
>         title: Title,
>         content: Content,
>         options: OptionsAlert
>     ): Promise<
>         {| action: "actionDismiss" | "actionNegative" | "actionNeutral" | "actionPositive" |} |
>         {| action: "actionDismiss" | "actionNegative" | "actionNeutral" | "actionPositive", checked: boolean |}
>     >

Shows a dialog.

| Parameter | Type                                 | Default | Required | Description                              |
|-----------|--------------------------------------|---------|----------|------------------------------------------|
| title     | `string, null, void`                 |         |          | Title of dialog                          |
| content   | `string, null, void`                 |         |          | Message of dialog                        |
| options   | [`OptionsAlert`](#type-optionsalert) |         |          | See [`OptionsAlert`](#type-optionsalert) |

##### `assignDefaults`

>     static assignDefaults({
>         [string]: value
>     ): void

Set default colors for example, so you don't have to provide it on every method call.

>    {
>        positiveText: 'OK'
>    }


##### `dismiss`

>     static dismiss(): void

Hides the currently showing dialog.

##### `prompt`

>     static prompt(
>         title?: null | string,
>         content?: null | string,
>         options: OptionsPrompt
>     ): Promise<
>         {| action: "actionNegative" | "actionNeutral" | "actionDismiss" |} |
>         {| action: "actionNegative" | "actionNeutral", checked: boolean |} |
>         {| action: "actionPositive", text: string |} |
>         {| action: "actionPositive", text: string, checked: boolean |}
>     >

Shows a dialog with a text input field.

| Parameter | Type                                   | Default | Required | Description                                |
|-----------|----------------------------------------|---------|----------|--------------------------------------------|
| title     | `string, null, void`                   |         |          | Title of dialog                            |
| content   | `string, null, void`                   |         |          | Message of dialog                          |
| options   | [`OptionsPrompt`](#type-optionsprompt) |         |          | See [`OptionsPrompt`](#type-optionsprompt) |

##### `showPicker`

>     static showPicker(
>         title?: null | string,
>         content?: null | string,
>         options: OptionsPicker
>     ): Promise<
>         {| action: "actionNegative" | "actionNeutral" | "actionDismiss" |} |
>         {| action: "actionNegative" | "actionNeutral" | "actionDismiss", checked: boolean |} |
>         {| action: "actionSelect", selectedItem: ListItem |} |
>         {| action: "actionSelect", selectedItem: ListItem, checked: boolean |} |
>         {| action: "actionSelect", selectedItems: ListItem[] |} |
>         {| action: "actionSelect", selectedItems: ListItem[], checked: boolean |}
>     >

Shows a regular alert, but also with items that can be selected.

| Parameter | Type                                     | Default | Required | Description                                |
|-----------|------------------------------------------|---------|----------|--------------------------------------------|
| title     | `string, null, void`                     |         |          | Title of dialog                            |
| content   | `string, null, void`                     |         |          | Message of dialog                          |
| options   | [`OptionsPicker`](#type-optionsprogress) |         |          | See [`OptionsPrompt`](#type-optionspicker) |

##### `showProgress`

>     static showProgress(
>         content?: null | string,
>         options: OptionsProgress
>     ): Promise<{| action:"actionDismiss" |}>

Shows a progress dialog. By default no buttons are shown, and hardware back button does not close it. You must close it with `DialogAndroid.dismiss()`.

| Parameter | Type                                       | Default | Required | Description                                  |
|-----------|--------------------------------------------|---------|----------|----------------------------------------------|
| content   | `string, null, void`                       |         |          | Message of dialog                            |
| options   | [`OptionsProgress`](#type-optionsprogress) |         |          | See [`OptionsPrompt`](#type-optionsprogress) |

### Types

[Flow](http://flow.org/) is used as the typing system.

#### Internal Types

##### `type ActionType`

>     "actionDismiss" | "actionNegative" | "actionNeutral" | "actionPositive" | "actionSelect"

##### `type ListItem`

>     { label:string } | { label:string, id:any } | {}

**Notes**

* If `label` key does not exist, specify which key should be used as the label with `labelKey` property of [`OptionsPicker`](#type-optionspicker).
* `id` is only required if `selectedId`/`selectedIds` needs to be used.
  * If `id` key does not exist, specify which key should be used as the id with `idKey` property of [`OptionsPicker`](#type-optionspicker).

##### `type ListType`

>     "listCheckbox" | "listPlain" | "listRadio"

##### `type OptionsAlert`

>     {
>         ...OptionsCommon
>     }

| Key              | Type                                   | Default | Required | Description                                |
|------------------|----------------------------------------|---------|----------|--------------------------------------------|
| ...OptionsCommon | [`OptionsCommon`](#type-optionscommon) |         |          | See [`OptionsCommon`](#type-optionscommon) |

##### `type OptionsCommon`

>     {
>         cancelable?: boolean,
>         checkboxDefaultValue?: boolean
>         checkboxLabel?: string,
>         content?: string,
>         contentColor?: string,
>         contentIsHtml?: boolean,
>         forceStacking?: boolean,
>         linkColor?: ColorValue,
>         negativeColor?: ColorValue,
>         negativeText?: string,
>         neutralColor?: ColorValue,
>         neutralText?: string,
>         positiveColor?: ColorValue,
>         positiveText?: string, // default "OK"
>         backgroundColor?: ColorValue,
>         title?: string,
>         titleColor?: ColorValue,
>     }

| Key                  | Type                                                                     | Default | Required | Description                                                                                                                                     |
|----------------------|--------------------------------------------------------------------------|---------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| cancelable           | `boolean`                                                                |         |          | If tapping outside of dialog area, or hardware back button, should dismiss dialog.                                                              |
| checkboxDefaultValue | `boolean`                                                                | `false` |          | The initial state of the checkbox. Does nothing if `checkboxLabel` is not set.                                                                  |
| checkboxLabel        | `string`                                                                 |         |          | If set, then there is a checkbox shown at the bottom of the dialog with this label                                                              |
| content              | `string`                                                                 |         |          | Dialog message                                                                                                                                  |
| contentColor         | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of dialog message                                                                                                                         |
| contentIsHtml        | `boolean`                                                                |         |          | If dialog message should be parsed as html. (supported tags include: `<a>`, `<img>`, etc)                                                       |
| forceStacking        | `boolean`                                                                |         |          | If you have multiple action buttons that together are too wide to fit on one line, the dialog will stack the buttons to be vertically oriented. |
| linkColor            | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | If `contentIsHtml` is true, and `content` contains `<a>` tags, these are colored with this color                                                |
| negativeColor        | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          |                                                                                                                                                 |
| negativeText         | `string`                                                                 |         |          | If falsy, button is not shown.                                                                                                                  |
| neutralColor         | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          |                                                                                                                                                 |
| neutralText          | `string`                                                                 |         |          | Shows button in far left with this string as label. If falsy, button is not shown.                                                              |
| positiveColor        | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          |                                                                                                                                                 |
| positiveText         | `string`                                                                 |         |          | If falsy, button is not shown.                                                                                                                  |
| backgroundColor      | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          |                                                                                                                                                 |
| title                | `string`                                                                 |         |          | Title of dialog                                                                                                                                 |
| titleColor           | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of title                                                                                                                                  |

##### `type OptionsProgress`

>     {
>         contentColor: $PropertyType<OptionsCommon, 'contentColor'>,
>         contentIsHtml: $PropertyType<OptionsCommon, 'contentIsHtml'>,
>         linkColor: $PropertyType<OptionsCommon, 'linkColor'>,
>         style?: ProgressStyle,
>         title: $PropertyType<OptionsCommon, 'title'>,
>         titleColor: $PropertyType<OptionsCommon, 'titleColor'>',
>         widgetColor: $PropertyType<OptionsCommon, 'widgetColor'>
>     }

| Key           | Type                                                                     | Default | Required | Description                                              |
|---------------|--------------------------------------------------------------------------|---------|----------|----------------------------------------------------------|
| contentColor  | [`OptionsCommon#contentColor`](#type-optionscommon)                      |         |          | See [`OptionsCommon#contentColor`](#type-optionscommon)  |
| contentIsHtml | [`OptionsCommon#contentIsHtml`](#type-optionscommon)                     |         |          | See [`OptionsCommon#contentIsHtml`](#type-optionscommon) |
| linkColor     | [`OptionsCommon#linkColor`](#type-optionscommon)                         |         |          | See [`OptionsCommon#linkColor`](#type-optionscommon)     |
| style         | [`ProgressStyle`](#type-ProgressStyle)                                   |         |          | See [`ProgressStyle`](#type-progressstyle)               |
| title         | [`OptionsCommon#title`](#type-optionscommon)                             |         |          | See [`OptionsCommon#title`](#type-optionscommon)         |
| titleColor    | [`OptionsCommon#titleColor`](#type-optionscommon)                        |         |          | See [`OptionsCommon#titleColor`](#type-optionscommon)    |
| widgetColor   | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of progress indicator                              |

##### `type OptionsPicker`

>    {|
>        ...OptionsCommon,
>        type?: typeof ListType.listPlain,
>        maxNumberOfItems?: number,
>        items: ListItemJustLabel[],
>    |} | {|
>        ...OptionsCommon,
>        type?: typeof ListType.listPlain,
>        maxNumberOfItems?: number,
>        items: ListItemBare[],
>        labelKey: string
>    |} | {|
>        // radio - no preselected
>        ...OptionsCommon,
>        type: typeof ListType.listRadio,
>        widgetColor?: ColorValue // radio color
>        items: ListItemJustLabel[],
>    |} | {|
>        // radio - no preselected
>        ...OptionsCommon,
>        type: typeof ListType.listRadio,
>        widgetColor?: ColorValue // radio color
>        items: ListItemBare[],
>        labelKey: string
>    |} | {|
>        // radio - preselected - ListItemFull
>        ...OptionsCommon,
>        type: typeof ListType.listRadio,
>        widgetColor?: ColorValue // radio color
>        items: ListItemFull[],
>        selectedId: any
>    |} | {|
>        // radio - preselected - ListItemJustlabel
>        ...OptionsCommon,
>        type: typeof ListType.listRadio,
>        widgetColor?: ColorValue // radio color
>        items: ListItemJustLabel[],
>        idKey: string,
>        selectedId: any
>    |} | {|
>        // radio - preselected - ListItemJustId
>        ...OptionsCommon,
>        type: typeof ListType.listRadio,
>        widgetColor?: ColorValue // radio color
>        items: ListItemJustId[],
>        labelKey: string,
>        selectedId: any
>    |} | {|
>        // radio - preselected - ListItemBare
>        ...OptionsCommon,
>        type: typeof ListType.listRadio,
>        widgetColor?: ColorValue // radio color
>        items: ListItemBare[],
>        idKey: string,
>        labelKey: string,
>        selectedId: any
>    |} | {|
>        // checklist - no preselected - ListItemJustLabel
>        ...OptionsCommon,
>        type: typeof ListType.listCheckbox,
>        neutralIsClear?: boolean,
>        widgetColor?: ColorValue, // checkbox color
>        items: ListItemJustLabel[]
>    |} | {|
>        // checklist - no preselected - ListItemBare
>        ...OptionsCommon,
>        type: typeof ListType.listCheckbox,
>        neutralIsClear?: boolean,
>        widgetColor?: ColorValue, // checkbox color
>        items: ListItemBare[],
>        labelKey: string
>    |} | {|
>        // checklist - preselected - ListItemFull
>        ...OptionsCommon,
>        type: typeof ListType.listCheckbox,
>        neutralIsClear?: boolean,
>        widgetColor?: ColorValue, // checkbox color
>        items: ListItemFull[],
>        selectedIds: any[]
>    |} | {|
>        // checklist - preselected - ListItemJustlabel
>        ...OptionsCommon,
>        type: typeof ListType.listCheckbox,
>        neutralIsClear?: boolean,
>        widgetColor?: ColorValue, // checkbox color
>        items: ListItemJustLabel[],
>        idKey: string,
>        selectedIds: any
>    |} | {|
>        // checklist - preselected - ListItemJustId
>        ...OptionsCommon,
>        type: typeof ListType.listCheckbox,
>        neutralIsClear?: boolean,
>        widgetColor?: ColorValue, // checkbox color
>        items: ListItemJustId[],
>        labelKey: string,
>        selectedIds: any
>    |} | {|
>        // checklist - preselected - ListItemBare
>        ...OptionsCommon,
>        type: typeof ListType.listCheckbox,
>        neutralIsClear?: boolean,
>        widgetColor?: ColorValue, // checkbox color
>        items: ListItemBare[],
>        idKey: string,
>        labelKey: string,
>        selectedIds: any
>    |}

| Key              | Type                                                                     | Default                   | Required | Description                                                                                                                                                                                                                            |
|------------------|--------------------------------------------------------------------------|---------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| OptionsCommon    | [`OptionsCommon`](#type-optionscommon)                                   |                           |          | See [`OptionsCommon`](#type-optionscommon)                                                                                                                                                                                             |
| idKey            | `string`                                                                 | "id"                      |          |                                                                                                                                                                                                                                        |
| items            | [`ListItem`](#type-listitem)[]                                           |                           | Yes      | See [`ListItem`](#type-listitem)                                                                                                                                                                                                       |
| labelKey         | `string`                                                                 | "label"                   |          |                                                                                                                                                                                                                                        |
| maxNumberOfItems | `number`                                                                 |                           |          | If you want to set a max amount of visible items in a list                                                                                                                                                                             |
| neutralIsClear   | `boolean`                                                                |                           |          | Pressing the neutral button causes the dialog to be closed and `selectedItems` to be an empty array. Only works if `neutralText` is also supplied.                                                                                     |
| selectedId       | `any`                                                                    |                           |          | The respective radio will be selected on dialog show. If no such id is found, then nothing is selected. Only applicable if `type` is `DialogAndroid.listRadio`. Requires that `items[]` contain key described by `idKey`.              |
| selectedIds      | `any[]`                                                                  |                           |          | The respective checkbox will be selected on dialog show. If no such id is found, nothing is selected for that id. Only applicable if `type` is `DialogAndroid.listCheckbox`. Requires that `items[]` contain key described by `idKey`. |
| type             | [`ListType`](#type-listtype)                                             | `DialogAndroid.listPlain` |          | See [`ListType`](#type-listtype)                                                                                                                                                                                                       |
| widgetColor      | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |                           |          | Color of radio or checkbox                                                                                                                                                                                                             |

##### `type OptionsPrompt`

>     {
>         ...OptionsCommon,
>         keyboardType?:
>           | 'numeric'
>           | 'number-pad'
>           | 'numeric-password'
>           | 'decimal-pad'
>           | 'email-address'
>           | 'password'
>           | 'phone-pad'
>           | 'url',
>         defaultValue?: string,
>         placeholder?: string,
>         allowEmptyInput?: boolean,
>         minLength?: number,
>         maxLength?: number,
>         widgetColor?: ColorValue
>     }

| Key           | Type                                                                     | Default | Required | Description                                |
|---------------|--------------------------------------------------------------------------|---------|----------|--------------------------------------------|
| OptionsCommon | [`OptionsCommon`](#type-optionscommon)                                   |         |          | See [`OptionsCommon`](#type-optionscommon) |
| widgetColor   | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of field underline and cursor        |

##### `type ProgressStyle`

>     "progressHorizontal"

### Examples

To see the examples redone with `checkboxLabel` see this PR - [Github :: aakashns/react-native-dialogs - #86](https://github.com/aakashns/react-native-dialogs/pull/86#issuecomment-393408317)

#### Progress Dialog

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/progress-bar.png)
![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/progress-circle.png)

```js
DialogAndroid.showProgress('Downloading...', {
    style: DialogAndroid.progressHorizontal // omit this to get circular
});
setTimeout(DialogAndroid.dismiss, 5000);
```

#### Basic List

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/list.png)

```js
const { selectedItem } = await DialogAndroid.showPicker('Pick a fruit', null, {
    items: [
        { label:'Apple', id:'apple' },
        { label:'Orange', id:'orange' },
        { label:'Pear', id:'pear' }
    ]
});
if (selectedItem) {
    // when negative button is clicked, selectedItem is not present, so it doesn't get here
    console.log('You selected item:', selectedItem);
}
```

#### Radio List

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/radiolist-autodismiss.gif)

Set `positiveText` to `null` for auto-dismiss behavior on press of a radio button item.

```js
const { selectedItem } = await DialogAndroid.showPicker('Pick a fruit', null, {
    // positiveText: null, // if positiveText is null, then on select of item, it dismisses dialog
    negativeText: 'Cancel',
    type: DialogAndroid.listRadio,
    selectedId: 'apple',
    items: [
        { label:'Apple', id:'apple' },
        { label:'Orange', id:'orange' },
        { label:'Pear', id:'pear' }
    ]
});
if (selectedItem) {
    // when negative button is clicked, selectedItem is not present, so it doesn't get here
    console.log('You picked:', selectedItem);
}
```

##### Without auto-dismiss

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/radiolist-nodismiss.gif)

Here we pass in a string to `positiveText`, this prevents the auto-dismiss on select of a radio item.

```js
const { selectedItem } = await DialogAndroid.showPicker('Pick a fruit', null, {
    positiveText: 'OK', // this is what makes disables auto dismiss
    negativeText: 'Cancel',
    type: DialogAndroid.listRadio,
    selectedId: 'apple',
    items: [
        { label:'Apple', id:'apple' },
        { label:'Orange', id:'orange' },
        { label:'Pear', id:'pear' }
    ]
});
if (selectedItem) {
    // when negative button is clicked, selectedItem is not present, so it doesn't get here
    console.log('You picked:', selectedItem);
}
```

#### Checklist

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/checklist.png)

```js
const { selectedItems } = await DialogAndroid.showPicker('Select multiple fruits', null, {
    type: DialogAndroid.listCheckbox,
    selectedIds: ['apple', 'orange'],
    items: [
        { label:'Apple', id:'apple' },
        { label:'Orange', id:'orange' },
        { label:'Pear', id:'pear' }
    ]
});
if (selectedItems) {
    if (!selectedItems.length) {
        console.log('You selected no items.');
    } else {
        console.log('You selected items:', selectedItems);
    }
}
```

##### With clear list button

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/checklist-clear.png)

We can make the neutral button be a special button. Pressing it will clear the list and close the dialog. If you want this behavior, set `neutralIsClear` to `true` and also set `neutralText` to a string. Both of these properties must be set.

```js
const { selectedItems } = await DialogAndroid.showPicker('Select multiple fruits', null, {
    type: DialogAndroid.listCheckbox,
    selectedIds: ['apple', 'orange'],
    neutralIsClear: true, /////// added this
    neutralText: 'Clear', /////// added this
    items: [
        { label:'Apple', id:'apple' },
        { label:'Orange', id:'orange' },
        { label:'Pear', id:'pear' }
    ]
});
if (selectedItems) {
    if (!selectedItems.length) {
        console.log('You selected no items.');
    } else {
        console.log('You selected items:', selectedItems);
    }
}
```

#### Prompt

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/prompt.png)

```js
const { action, text } = await DialogAndroid.prompt('Title - optional', 'Message - optional');
if (action === DialogAndroid.actionPositive) {
    console.log(`You submitted: "${text}"`);
}
```

#### HTML

![](https://github.com/aakashns/react-native-dialogs/blob/master/screenshots/html.png)

```js
DialogAndroid.alert('Title', `This is a link <a href="https://www.duckduckgo.com/">DuckDuckGo</a>`, {
    contentIsHtml: true
});
```

#### assignDefaults

You can set some defaults so you don't have to change it everytime.

```js
DialogAndroid.assignDefaults({
    title: 'Default Title',
    positiveText: null,
    contentColor: 'rgba(0, 0, 0, 0.2)',
    widgetColor: 'blue'
})
```


Now any time you omit or pass `undefined` to `contentColor`, `widgetColor`, or `title`, it will use the defaults we assigned here.

```js
DialogAndroid.alert(undefined, 'message here')
```

This will show title "Default Title", with no positive button, and the color of the message will be 20% black. If you did `Dialog.showProgress`, the progress indicator would be blue. etc.

## Contributors

Thanks goes to these wonderful people ([emoji key](https://github.com/kentcdodds/all-contributors#emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
| [<img src="https://avatars1.githubusercontent.com/u/1566403?v=4" width="100px;"/><br /><sub><b>Vojtech Novak</b></sub>](https://github.com/vonovak)<br />[💬](#question-vonovak "Answering Questions") [💻](https://github.com/aakashns/react-native-dialogs/commits?author=vonovak "Code") [🤔](#ideas-vonovak "Ideas, Planning, & Feedback") [👀](#review-vonovak "Reviewed Pull Requests") | [<img src="https://avatars0.githubusercontent.com/u/6372489?v=4" width="100px;"/><br /><sub><b>Noitidart</b></sub>](http://noitidart.github.io/)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=Noitidart "Code") [📖](https://github.com/aakashns/react-native-dialogs/commits?author=Noitidart "Documentation") [💡](#example-Noitidart "Examples") [🤔](#ideas-Noitidart "Ideas, Planning, & Feedback") |                                               [<img src="https://avatars3.githubusercontent.com/u/6080124?v=4" width="100px;"/><br /><sub><b>Alisson Carvalho</b></sub>](http://alissoncs.com)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=alissoncs "Code")                                               |                               [<img src="https://avatars1.githubusercontent.com/u/1567160?v=4" width="100px;"/><br /><sub><b>Anthony Ou</b></sub>](https://github.com/Anthonyzou)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=Anthonyzou "Code")                              |         [<img src="https://avatars0.githubusercontent.com/u/844437?v=4" width="100px;"/><br /><sub><b>Ashley White</b></sub>](http://ashleyd.ws)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=ashleydw "Code")        |   [<img src="https://avatars0.githubusercontent.com/u/239360?v=4" width="100px;"/><br /><sub><b>Bee</b></sub>](https://github.com/1ne8ight7even)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=1ne8ight7even "Code")  |          [<img src="https://avatars3.githubusercontent.com/u/6874216?v=4" width="100px;"/><br /><sub><b>BrianSo</b></sub>](https://github.com/BrianSo)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=BrianSo "Code")          |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                                                           [<img src="https://avatars3.githubusercontent.com/u/1411784?v=4" width="100px;"/><br /><sub><b>Byron Wang</b></sub>](https://github.com/byronpc)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=byronpc "Code")                                                                          |                                                                                         [<img src="https://avatars3.githubusercontent.com/u/5062458?v=4" width="100px;"/><br /><sub><b>Farzad Abdolhosseini</b></sub>](https://github.com/farzadab)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=farzadab "Code")                                                                                        | [<img src="https://avatars3.githubusercontent.com/u/8598682?v=4" width="100px;"/><br /><sub><b>Geoffrey Goh</b></sub>](https://github.com/geof90)<br />[🐛](https://github.com/aakashns/react-native-dialogs/issues?q=author%3Ageof90 "Bug reports") [💻](https://github.com/aakashns/react-native-dialogs/commits?author=geof90 "Code") | [<img src="https://avatars3.githubusercontent.com/u/7588480?v=4" width="100px;"/><br /><sub><b>Gustavo Fão Valvassori</b></sub>](http://gustavofao.com/)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=faogustavo "Code") [🤔](#ideas-faogustavo "Ideas, Planning, & Feedback") | [<img src="https://avatars2.githubusercontent.com/u/16625347?v=4" width="100px;"/><br /><sub><b>Henrik</b></sub>](https://github.com/Henreich)<br />[📖](https://github.com/aakashns/react-native-dialogs/commits?author=Henreich "Documentation") |     [<img src="https://avatars2.githubusercontent.com/u/1103539?v=4" width="100px;"/><br /><sub><b>heydabop</b></sub>](https://github.com/heydabop)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=heydabop "Code")    |          [<img src="https://avatars0.githubusercontent.com/u/13056774?v=4" width="100px;"/><br /><sub><b>Huang Yu</b></sub>](https://github.com/hyugit)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=hyugit "Code")          |
|                                                                              [<img src="https://avatars0.githubusercontent.com/u/1516807?v=4" width="100px;"/><br /><sub><b>Iragne</b></sub>](http://pcdn.jairagne.ovh)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=Iragne "Code")                                                                              |                                                                                     [<img src="https://avatars2.githubusercontent.com/u/2677334?v=4" width="100px;"/><br /><sub><b>Janic Duplessis</b></sub>](https://medium.com/@janicduplessis)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=janicduplessis "Code")                                                                                    |                                    [<img src="https://avatars2.githubusercontent.com/u/7968613?v=4" width="100px;"/><br /><sub><b>jeffchienzabinet</b></sub>](https://github.com/jeffchienzabinet)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=jeffchienzabinet "Code")                                    |                            [<img src="https://avatars3.githubusercontent.com/u/1088099?v=4" width="100px;"/><br /><sub><b>Jeremy Dagorn</b></sub>](http://www.jeremydagorn.com)<br />[📖](https://github.com/aakashns/react-native-dialogs/commits?author=jrm2k6 "Documentation")                           |         [<img src="https://avatars0.githubusercontent.com/u/13287601?v=4" width="100px;"/><br /><sub><b>jykun</b></sub>](https://github.com/jykun)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=jykun "Code")         | [<img src="https://avatars2.githubusercontent.com/u/195925?v=4" width="100px;"/><br /><sub><b>Mattias Pfeiffer</b></sub>](http://pfeiffer.dk)<br />[📖](https://github.com/aakashns/react-native-dialogs/commits?author=pfeiffer "Documentation") | [<img src="https://avatars3.githubusercontent.com/u/14799874?v=4" width="100px;"/><br /><sub><b>pureday</b></sub>](https://github.com/lakeoffaith)<br />[📖](https://github.com/aakashns/react-native-dialogs/commits?author=lakeoffaith "Documentation") |
|                                                                        [<img src="https://avatars0.githubusercontent.com/u/7029942?v=4" width="100px;"/><br /><sub><b>Radek Czemerys</b></sub>](https://twitter.com/radko93)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=radko93 "Code")                                                                        |                                                                                      [<img src="https://avatars3.githubusercontent.com/u/1160365?v=4" width="100px;"/><br /><sub><b>Ricardo Fuhrmann</b></sub>](https://github.com/Fuhrmann)<br />[📖](https://github.com/aakashns/react-native-dialogs/commits?author=Fuhrmann "Documentation")                                                                                      |                                                   [<img src="https://avatars0.githubusercontent.com/u/22330398?v=4" width="100px;"/><br /><sub><b>Ross</b></sub>](https://thebhwgroup.com/)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=rdixonbhw "Code")                                                  |                                [<img src="https://avatars2.githubusercontent.com/u/5407363?v=4" width="100px;"/><br /><sub><b>Vinicius Zaramella</b></sub>](http://programei.com)<br />[💻](https://github.com/aakashns/react-native-dialogs/commits?author=vzaramel "Code")                                |                                                                                                                                                                                                                                                    |                                                                                                                                                                                                                                                   |                                                                                                                                                                                                                                                           |
<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/kentcdodds/all-contributors) specification. Contributions of any kind welcome!
