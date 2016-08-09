/**
 * @providesModule DialogAndroid
 */

'use strict';

var { NativeModules } = require('react-native');

var callbackNames = [
  'onPositive',
  'onNegative',
  'onNeutral',
  'onAny',
  'itemsCallback',
  'itemsCallbackSingleChoice',
  'itemsCallbackMultiChoice',
  'showListener',
  'cancelListener',
  'dismissListener',
];

class DialogAndroid {
  constructor() {
    this.options = {};
  }

  set(obj) {
    Object.assign(this.options, obj);
  }

  show() {
    var finalOptions = Object.assign({}, this.options);

    var callbacks = {
      error: (err, op) => console.error(err, op),
    }

    // Remove callbacks from the options, and store them separately
    callbackNames.forEach(cb => {
      if (cb in finalOptions) {
        callbacks[cb] = finalOptions[cb];
        finalOptions[cb] = true;
      }
    });

    // Handle special case of input separately
    if ('input' in finalOptions) {
      finalOptions.input = Object.assign({}, finalOptions.input);
      var inputCallback = finalOptions.input.callback || (x => console.log(x));
      finalOptions.input.callback = true;
      callbacks['input'] = inputCallback;
    }

    // Parse the result form multiple choice dialog
    if ('itemsCallbackMultiChoice' in callbacks) {
      var originalCallback = callbacks.itemsCallbackMultiChoice;
      callbacks.itemsCallbackMultiChoice = selected => {
        var indices = selected.split(',').map(x => parseInt(x));
        var elements = indices.map(ind => (finalOptions.items || [])[ind]);
        if(indices.length === 1 && isNaN(indices[0])){
          indices=[] // the case of empty selection
          elements=[]
        }
        originalCallback(indices, elements);
      }
    }

    var callbackFunc = (cb, ...rest) => callbacks[cb](...rest);

    NativeModules.DialogAndroid.show(finalOptions, callbackFunc);
  }

  dismiss() {
    NativeModules.DialogAndroid.dismiss();
  }

  list(options, cb){
    NativeModules.DialogAndroid.list(options, cb)
  }
}

module.exports = DialogAndroid;
