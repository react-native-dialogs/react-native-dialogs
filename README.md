## react-native-dialogs

An Android only module for Material Design dialogs. This is a wrapper over [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs). This module is designed for Android only with no plans to support iOS.

### Table of Contents
- TODO:

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
   async function() {
       const { action } = await DialogAndroid.alert('Title', 'Message', {
           neutralText: 'Ask Later'
       });

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
   ```

### API

#### `setDefaults`

>     static setDefaults({
>         title?: string | null | void,
>         content?: string | null | void,
>         ...options
>     ): void

Set default colors for example, so you don't have to provide it on alert.

#### `dismiss`

>     static dismiss(): void

Hides the currently showing dialog.

#### `alert`

##### Full signature
>     static alert(
>         title: null | string,
>         content?: null | string,
>         options: Options
>     ): Promise<AlertReturn>

##### Two argument signature
>     static alert(
>         title: null | string,
>         options: Options
>     ): Promise<AlertReturn>

##### One string argument signature
>     static alert(
>         content: null | string,
>     ): Promise<AlertReturn>

##### One object argument signature
>     static alert(
>         option: Options
>     ): Promise<AlertReturn>

Shows the dialog and resolves the promise with [`AlertReturn`](#type-alertreturn).

### Types

[Flow](http://flow.org/) is used as the typing system.

#### Internal Types

#### `type ListItem`

>     string | { label:string } | {}

#### `type ListType`

>     "listPlain" | "listRadio" | "listCheckbox"

#### `type ActionType`

>     "actionDismiss" | "actionNegative" | "actionNeutral" | "actionPositive" | "actionSelect"

##### `type AlertReturn`

>     {
>         action: "actionPostive" | "actionNegative" | "actioNeutral" | "actionDismiss"
>     } | {
>         action: "actionSelect",
>         selectedItem: ListItem
>     } | {
>         action: "actionPositive",
>         selectedItems: ListItem[]
>     } | {
>         action: "actionPositive",
>         text: string
>     }

### Examples

#### Progress overlay


#### List of radio items dismissed on press

If we want the first press on an item to close and accept the dialog, we pass `null` to `positiveText`:



### Checklist with clear button

We can make the neutral button be a special button. Pressing it will clear the list and close the dialog.


### Input field

