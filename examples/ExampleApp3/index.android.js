/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {
  AppRegistry,
  Component,
  TouchableNativeFeedback,
  StyleSheet,
  Text,
  View
} from 'react-native';

import DialogAndroid from 'react-native-dialogs';

import dialogData from './dialogData.js';

class ExampleApp3 extends Component {
  showDialog(options) {
    var dialog = new DialogAndroid();
    dialog.set(options);
    dialog.show();
  }

  render() {
    var dialogs = dialogData[0].dialogs.map((op, i) => {

      return (
        <TouchableNativeFeedback key={i}
          onPress={c => this.showDialog(
               op.data || { title: "NOT IMPLEMENTED!! :-(", positiveText: "OK"})}>
          <View>
            <Text>{op.buttonText}</Text>
          </View>
        </TouchableNativeFeedback>
      );
    });
    // var dialogs = dialogData.map((section, i) => {
    //   var sectionDialogNodes = section.dialogs.map((op, j) => {
    //     const ColoredRaisedButton = MKButton.coloredButton()
    //       .withText(op.buttonText)
    //       .withOnPress(c => this.showDialog(
    //           op.data || { title: "NOT IMPLEMENTED!! :-(", positiveText: "OK"}))
    //       .withStyle({width: 200})
    //       .build();
    //     return (
    //       <View style={{padding: 5}} key={j}>
    //         <Text>Hi</Text>
    //       </View>
    //     );
    //   });

    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Shake or press menu button for dev menu
        </Text>
        {dialogs}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('ExampleApp3', () => ExampleApp3);
