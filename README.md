## react-native-dialogs

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
    - [`type OptionsCommon`](#type-optionscommon)
    - [`type OptionsProgress`](#type-optionsprogress)
    - [`type OptionsPicker`](#type-optionspicker)
    - [`type OptionsPrompt`](#type-optionsprompt)
    - [`type ProgressStyle`](#type-progressstyle)
- [Examples](#examples)
  - [Progress overlay](#progress-overlay)
  - [List of radio items dismissed on press](#list-of-radio-items-dismissed-on-press)
  - [Checklist with clear button](#checklist-with-clear-button)
  - [Prompt](#prompt)
  - [HTML](#html)
  - [assignDefaults](#assigndefaults)

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

1. Include this module in `android/settings.gradle`:

   ```
   ...
   include ':autocompletetextview' // Add this
   project(':autocompletetextview').projectDir = file("../node_modules/autocompletetextview/android") // Add this

   ...
   include ':app'
   ```

2. In `android/app/build.gradle`, add the dependency to your app build:

   ```
   dependencies {
       ...
       compile project(':react-native-dialogs') // Add this
   }
   ```

3. In `android/build.gradle`, it should already be there, but in case it is not, add Jitpack maven repository to download the library [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs):

   ```
   allprojects {
       repositories {
           ...
           jcenter() // Add this if it is not already here
           ...
       }
   }
   ```

4. In `android/settings.gradle`:

   ```
   rootProject.name = ...
   ...
   include ':react-native-dialogs' // Add this
   project(':react-native-dialogs').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-dialogs/android') // Add this

   ...
   include ':app'
   ```

5. Import and add package, in `android/app/src/main/.../MainApplication.java`:

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
                   console.log('netural!')
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
>         options: Options
>     ): Promise<{| action: "actionDismiss" | "actionNegative" | "actionNeutral" | "actionPositive" |}>

Shows a dialog.

| Parameter | Type                                   | Default | Required | Description                                |
|-----------|----------------------------------------|---------|----------|--------------------------------------------|
| title     | `string, null, void`                   |         |          | Title of dialog                            |
| content   | `string, null, void`                   |         |          | Message of dialog                          |
| options   | [`OptionsCommon`](#type-optionscommon) |         |          | See [`OptionsCommon`](#type-optionscommon) |

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
>         {| action: "actionPositive", text: string |}
>     >

Shows a dialog with a text input field.

| Parameter | Type                                   | Default | Required | Description                                |
|-----------|----------------------------------------|---------|----------|--------------------------------------------|
| title     | `string, null, void`                   |         |          | Title of dialog                            |
| content   | `string, null, void`                   |         |          | Message of dialog                          |
| options   | [`OptionsPrompt`](#type-optionsprompt) |         |          | See [`OptionsPrompt`](#type-optionsprompt) |

##### `showPicker`

>     static showProgress(
>         title?: null | string,
>         content?: null | string,
>         options: OptionsPicker
>     ): Promise<
>         {| action: "actionNegative" | "actionNeutral" | "actionDismiss" |} |
>         {| action: "actionSelect", selectedItem: ListItem |} |
>         {| action: "actionSelect", selectedItems: ListItem[] |}
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

##### `type OptionsCommon`

>     {
>         cancelable?: boolean,
>         content?: string,
>         contentColor?: string,
>         contentIsHtml?: boolean,
>         forceStacking?: boolean
>         linkColor?: ColorValue,
>         negativeColor?: ColorValue,
>         negativeText?: string,
>         neutralColor?: ColorValue,
>         neutralText?: string,
>         positiveColor?: ColorValue,
>         positiveText?: string, // default "OK"
>         title?: string,
>         titleColor?: ColorValue,
>     }

| Key           | Type                                                                       | Default | Required | Description                                                                                                                                     |
|---------------|----------------------------------------------------------------------------|---------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| cancelable    | `boolean`                                                                  |         |          | If tapping outside of dialog area, or hardware back button, should dismiss dialog.                                                              |
| content       | `string`                                                                   |         |          | Dialog message                                                                                                                                  |
| contentColor  | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of dialog message                                                                                                                         |
| contentIsHtml | `boolean`                                                                  |         |          | If dialog message should be parsed as html. (supported tags include: `<a>`, `<img>`, etc)                                                       |
| forceStacking | `boolean`                                                                  |         |          | If you have multiple action buttons that together are too wide to fit on one line, the dialog will stack the buttons to be vertically oriented. |
| linkColor     | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | If `contentIsHtml` is true, and `content` contains `<a>` tags, these are colored with this color                                                |
| negativeColor | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          |                                                                                                                                                 |
| negativeText  | `string`                                                                   |         |          | If falsy, button is not shown.                                                                                                                  |
| neutralColor  | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          |                                                                                                                                                 |
| neutralText   | `string`                                                                   |         |          | Shows button in far left with this string as label. If falsy, button is not shown.                                                              |
| positiveColor | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          |                                                                                                                                                 |
| positiveText  | `string`                                                                   |         |          | If falsy, button is not shown.                                                                                                                  |
| title         | `string`                                                                   |         |          | Title of dialog                                                                                                                                 |
| titleColor    | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of title                                                                                                                                  |

##### `type OptionsProgress`

>     {
>         contentColor: $PropertyType<OptionsCommon, 'contentColor'>,
>         contentIsHtml: $PropertyType<OptionsCommon, 'contentIsHtml'>,
>         linkColor: $PropertyType<OptionsCommon, 'linkColor'>,
>         style?: ProgressStyle,
>         title: $PropertyType<OptionsCommon, 'title'>,
>         titleColor: $PropertyType<OptionsCommon, 'titleColor'>',
>         widgetColor: $PropertyType<OptionsCommon, 'widgetColor'>
>         widgetColor?: ColorValue
>     }

| Key           | Type                                                                       | Default | Required | Description                                              |
|---------------|----------------------------------------------------------------------------|---------|----------|----------------------------------------------------------|
| contentColor  | [`OptionsCommon#contentColor`](#type-optionscommon)                        |         |          | See [`OptionsCommon#contentColor`](#type-optionscommon)  |
| contentIsHtml | [`OptionsCommon#contentIsHtml`](#type-optionscommon)                       |         |          | See [`OptionsCommon#contentIsHtml`](#type-optionscommon) |
| linkColor     | [`OptionsCommon#linkColor`](#type-optionscommon)                           |         |          | See [`OptionsCommon#linkColor`](#type-optionscommon)     |
| style         | [`ProgressStyle`](#type-ProgressStyle)                                     |         |          | See [`ProgressStyle`](#type-progressstyle)               |
| title         | [`OptionsCommon#title`](#type-optionscommon)                               |         |          | See [`OptionsCommon#title`](#type-optionscommon)         |
| titleColor    | [`OptionsCommon#titleColor`](#type-optionscommon)                          |         |          | See [`OptionsCommon#titleColor`](#type-optionscommon)    |
| widgetColor   | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of progress indicator                              |

##### `type OptionsPicker`

>     {
>         ...OptionsCommon,
>         idKey?: string,
>         items: ListItem[],
>         labelKey?: string,
>         neutralIsClear?: boolean,
>         selectedId?: any,
>         selectedIds?: any[],
>         type?: string,
>         widgetColor?: ColorValue
>     }

| Key            | Type                                                                       | Default                   | Required | Description                                                                                                                                                                                                                            |
|----------------|----------------------------------------------------------------------------|---------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| OptionsCommon  | [`OptionsCommon`](#type-optionscommon)                                     |                           |          | See [`OptionsCommon`](#type-optionscommon)                                                                                                                                                                                             |
| idKey          | `string`                                                                   | "id"                      |          |                                                                                                                                                                                                                                        |
| items          | [`ListItem`](#type-listitem)[]                                             |                           | Yes      | See [`ListItem`](#type-listitem)                                                                                                                                                                                                       |
| labelKey       | `string`                                                                   | "label"                   |          |                                                                                                                                                                                                                                        |
| neutralIsClear | `boolean`                                                                  |                           |          | Pressing the neutral button causes the dialog to be closed and `selectedItems` to be an empty array. Only works if `neutralText` is also supplied.                                                                                     |
| selectedId     | `any`                                                                      |                           |          | The respective radio will be selected on dialog show. If no such id is found, then nothing is selected. Only applicable if `type` is `DialogAndroid.listRadio`. Requires that `items[]` contain key described by `idKey`.              |
| selectedIds    | `any[]`                                                                    |                           |          | The respective checkbox will be selected on dialog show. If no such id is found, nothing is selected for that id. Only applicable if `type` is `DialogAndroid.listCheckbox`. Requires that `items[]` contain key described by `idKey`. |
| type           | [`ListType`](#type-listtype)                                               | `DialogAndroid.listPlain` |          | See [`ListType`](#type-listtype)                                                                                                                                                                                                       |
| widgetColor    | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |                           |          | Color of radio or checkbox                                                                                                                                                                                                             |

##### `type OptionsPrompt`

>     {
>         ...OptionsCommon,
>
>         widgetColor?: ColorValue
>     }

| Key           | Type                                                                       | Default | Required | Description                                |
|---------------|----------------------------------------------------------------------------|---------|----------|--------------------------------------------|
| OptionsCommon | [`OptionsCommon`](#type-optionscommon)                                     |         |          | See [`OptionsCommon`](#type-optionscommon) |
| widgetColor   | [`ColorValue`](https://facebook.github.io/react-native/docs/colors.html) |         |          | Color of field underline and cursor        |

##### `type ProgressStyle`

>     "progressHorizontal"

### Examples

#### Progress dialog

```js
DialogAndroid.showProgress(null, 'Downloading...', {
    style: DialogAndroid.progressHorizontal
});
setTimeout(DialogAndroid.dismiss, 5000);
```

#### List of radio items dismissed on press

If we want the first press on an item to close and accept the dialog, we pass `null` to `positiveText`:

```js
const { selectedItem } = await DialogAndroid.alert('Title', null, {
    positiveText: null,
    items: [
        { label:'Apple', id:'apple' },
        { label:'Orange', id:'orange' },
        { label:'Pear', id:'pear' }
    ],
    selectedId: 'apple'
});
if (selectedItem) {
    console.log('You selected item:', item);
}
```


#### Checklist with clear button

We can make the neutral button be a special button. Pressing it will clear the list and close the dialog.

```js
const { selectedItems } = await DialogAndroid.alert('Title', null, {
    items: [
        { label:'Apple', id:'apple' },
        { label:'Orange', id:'orange' },
        { label:'Pear', id:'pear' }
    ],
    selectedIds: ['apple', 'orange'], // or if is not array of objects with "id" can use selectedIndices
    neutralIsClear: true,
    neutralText: 'Empty List'
});
if (selectedItems) {
    if (!selectedItems.length) {
        console.log('You emptied the list');
    } else {
        console.log('You selected items:', selectedItems);
    }
}
```


#### Prompt

```js
const { action, text } = await DialogAndroid.prompt('Title', 'Message', {
    isHorizontal:true
});
if (action === DialogAndroid.actionPositive) {
    alert(`You submitted: ${text}`)
}
```

#### HTML

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
    contentColor: 'rgba(0, 0, 0, 0.2)',
    widgetColor: 'blue'
})
```


Now any time you supply `undefined` to title, it will use the default assigned above.

```js
DialogAndroid.alert(undefined, 'message here')
```

This will show title "Default Title", with no negative button, and the color of the message will be 20% black. If you did `Dialog.showProgress`, the progress indicator would be blue. etc.







