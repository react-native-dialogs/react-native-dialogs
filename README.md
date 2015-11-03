# react-native-dialogs
Material Design dialogs for React Native Android apps (wrapper over [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs))

Installation
------------

Install the npm package [`react-native-dialogs`](https://www.npmjs.com/package/react-native-dialogs). Inside your React Native project, run ([example](https://github.com/aakashns/react-native-dialogs-example/commit/e6b83bf3d2238cf7e4ec3688519f38b2544ccad5)):
```bash
npm install --save react-native-dialogs
```

In `android/settings.gradle`, remove the line `include ':app'` and add the following lines ([example](https://github.com/aakashns/react-native-dialogs-example/commit/32b4159725601e0ea17e140f0a9b62161d567804)):
```
include ':app', ':react-native-dialogs'
project(':react-native-dialogs').projectDir = file('../node_modules/react-native-dialogs/android')
```
**NOTE** : If you have included other libraries in your project, the `include` line will contain the other dependencies too.

In `android/app/build.gradle`, add a dependency to `':react-native-dialogs'` and URL of the Jitpack maven repository (to download the library https://github.com/afollestad/material-dialogs) :
```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    // after other dependencies
    compile project(':react-native-dialogs')
}
```
The changes should look like [this](https://github.com/aakashns/react-native-dialogs-example/commit/b58086d8fb9ece99f0e678dd8bf0e689a856bd43).

Next, you need to change the `MainActivity` of your app to extends `FragmentActivity` instead of `Activity` (otherwise dialogs will not be rendered), and register `ReactNativeDialogsPackage` : 
```java
import android.support.v4.app.FragmentActivity;
import com.aakashns.reactnativedialogs.ReactNativeDialogsPackage;

public class MainActivity extends FragmentActivity implements DefaultHardwareBackBtnHandler {
    //...
  
          mReactInstanceManager = ReactInstanceManager.builder()
                //...
                .addPackage(new MainReactPackage())
                .addPackage(new ReactNativeDialogsPackage(this)) // <- ADD THIS LINE!
                //...
                .build();

```
See [this changelog](https://github.com/aakashns/react-native-dialogs-example/commit/52cac27756963bcd2f4fdcd039e1a78028bb0abd) for reference.

Now you're finally ready to start using module in your React Native application. See [this changelog](https://github.com/aakashns/react-native-dialogs-example/commit/2d8e02c22275479d2fbbb89f99dcb846834bec9d) for an example that uses `DialogAndroid`.

Usage
-----
```javascript
var DialogAndroid = require('react-native-dialogs');

var options = {
  title: 'Hello, World!',
  content: 'I\'m just simple Dialog',
  positiveText: 'OK',
  negativeText: 'Cancel'
};

var showDialog = function () {
  var dialog = new DialogAndroid();
  dialog.set(options);
  dialog.show();
}
```
Creation of a dialog works in 3 steps :
1. Create a new dialog using `new DialogAndroid()`.
2. Set some options using `dialog.set(options)`. `set` can be called multiple times, and options from multiple calls will be merged.
3. Show the dialog using `dialog.show()`.

This library is a thin wrapper over [afollestad/material-dialogs](https://github.com/afollestad/material-dialogs), which provides builders for showing Material Design dialogs in Android apps. The options provided to `set` map more or less directly to the methods provided in the original library. See [its documentation](https://github.com/afollestad/material-dialogs#basic-dialog) for reference.

The following options are currently supported (value type is `String` unless mentioned otherwise) :
* [`title`](https://github.com/afollestad/material-dialogs#basic-dialog)
* [`content`](https://github.com/afollestad/material-dialogs#basic-dialog)
* [`positiveText`](https://github.com/afollestad/material-dialogs#basic-dialog)
* [`onPositive`](https://github.com/afollestad/material-dialogs#callbacks) (function with no arguments)
* [`negativeText`](https://github.com/afollestad/material-dialogs#basic-dialog)
* [`onNegative`](https://github.com/afollestad/material-dialogs#callbacks) (function with no arguments)
* [`neutralText`](https://github.com/afollestad/material-dialogs#neutral-action-button)
* [`onNeutral`](https://github.com/afollestad/material-dialogs#callbacks) (function with no arguments)
* [`onAny`](https://github.com/afollestad/material-dialogs#callbacks) (function with no arguments)
* [`items`](https://github.com/afollestad/material-dialogs#list-dialogs) (array of strings)
* [`itemsCallback`](https://github.com/afollestad/material-dialogs#list-dialogs) (function with 2 arguments : selectedIndex (int) and selectedItem (string))
* [`itemsCallbackSingleChoice`](https://github.com/afollestad/material-dialogs#single-choice-list-dialogs) (function with 2 arguments : selectedIndex (int) and selectedItem (string))
* [`selectedIndex`](https://github.com/afollestad/material-dialogs#single-choice-list-dialogs) (int) - set the preselected index for Single Choice List
* [`itemsCallbackMultiChoice`](https://github.com/afollestad/material-dialogs#multi-choice-list-dialogs) (function with 2 arguments : selected indices (array of ints) and selected items (array of strings)
* [`selectedIndices`](https://github.com/afollestad/material-dialogs#multi-choice-list-dialogs) (array of ints) - set the preselected indices for Multiple Choice List
* `multiChoiceClearButton` (boolean) - provide a 'Clear' button in Multiple Choice List
* `autoDismiss` (boolean)
* [`forceStacking`](https://github.com/afollestad/material-dialogs#stacked-action-buttons) (boolean)
* [`alwaysCallSingleChoiceCallback`](https://github.com/afollestad/material-dialogs#single-choice-list-dialogs) (boolean)
* [`alwaysCallMultipleChoiceCallback`](https://github.com/afollestad/material-dialogs#multi-choice-list-dialogs) (boolean)
* [`cancelable`](https://github.com/afollestad/material-dialogs#show-cancel-and-dismiss-callbacks) (boolean)
* [`showListener`](https://github.com/afollestad/material-dialogs#show-cancel-and-dismiss-callbacks) (function)
* [`cancelListener`](https://github.com/afollestad/material-dialogs#show-cancel-and-dismiss-callbacks) (function)
* [`dismissListener`](https://github.com/afollestad/material-dialogs#show-cancel-and-dismiss-callbacks) (function)
* [`input`](https://github.com/afollestad/material-dialogs#input-dialogs) - Object containing the following keys (all optional except callback) :
  * [`hint`](https://github.com/afollestad/material-dialogs#input-dialogs)
  * [`prefill`](https://github.com/afollestad/material-dialogs#input-dialogs)
  * [`allowEmptyInput`](https://github.com/afollestad/material-dialogs#input-dialogs) (boolean)
  * [`minLength`](https://github.com/afollestad/material-dialogs#limiting-input-length) (int)
  * [`maxLength`](https://github.com/afollestad/material-dialogs#limiting-input-length) (int)
  * [`type`](https://github.com/afollestad/material-dialogs#input-dialogs) (int)
  * [`callback`](https://github.com/afollestad/material-dialogs#input-dialogs) (function with 1 argument : user provided input)
* [`alwaysCallInputCallback`](https://github.com/afollestad/material-dialogs#input-dialogs) (boolean)

Examples
--------

Simple example project : https://github.com/aakashns/react-native-dialogs-example

Complex example project : [examples/ExampleApp](./examples/ExampleApp)

Try out the following values for option (taken from [examples/ExampleApp/dialogData.js](./examples/ExampleApp/dialogData.js)):

```javascript
{
  "title": "Use Google's Location Services?",
  "content": "This app wants to access your location.",
  "positiveText": "Agree",
  "negativeText": "Disagree"
}
```

```javascript
{
  "title": "Use Google's Location Services?",
  "content": "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.",
  "positiveText": "Agree",
  "negativeText": "Disagree",
  "neutralText": "More Info",
  "onPositive": () => ToastAndroid.show("POSITIVE!", ToastAndroid.SHORT),
  "onNegative": () => ToastAndroid.show("NEGATIVE!", ToastAndroid.SHORT),
  "onNeutral": () => ToastAndroid.show("NEUTRAL!", ToastAndroid.SHORT),
}
```

```javascript
"data": {
  "items": [
    "Twitter",
    "Google+",
    "Instagram",
    "Facebook"
  ],
  "title": "Social Networks",
  itemsCallback: (id, text) => ToastAndroid.show(id + ": " + text, ToastAndroid.SHORT);
}
```

```javascript
"data": {
  "items": [
    "Twitter",
    "Google+",
    "Instagram",
    "Facebook"
  ],
  "title": "Social Networks",
  itemsCallbackSingleChoice: (id, text) => ToastAndroid.show(id + ": " + text, ToastAndroid.SHORT);
}
```

```javascript
"data": {
  "items": [
    "Twitter",
    "Google+",
    "Instagram",
    "Facebook"
  ],
  "title": "Social Networks",
  "positiveText": "Choose",
  itemsCallbackMultiChoice: (id, text) => ToastAndroid.show(id + ": " + text, ToastAndroid.SHORT);
}
```



Known Issues
------------
TODO

Upcoming Features
-------
TODO

