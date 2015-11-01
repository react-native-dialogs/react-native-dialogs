import React from 'react-native';
var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  NativeModules,
  TouchableNativeFeedback,
  ScrollView,
  ToolbarAndroid,
} = React;

import DialogAndroid from 'react-native-dialogs';

import dialogData from './dialogData.js';
import {MKButton } from 'react-native-material-kit';

class ExampleApp extends React.Component {
  showDialog(options) {
    var dialog = new DialogAndroid();
    dialog.set(options);
    dialog.show();
  }

  render() {

    var dialogs = dialogData.map((section, i) => {
      var sectionDialogNodes = section.dialogs.map((op, j) => {
        const ColoredRaisedButton = MKButton.coloredButton()
          .withText(op.buttonText)
          .withOnPress(c => this.showDialog(
              op.data || { title: "NOT IMPLEMENTED!! :-(", positiveText: "OK"}))
          .withStyle({width: 200})
          .build();
        return (
          <View style={{padding: 5}}>
            <ColoredRaisedButton />
          </View>
        );
      });

      return (
        <View key={i}>
          <Text style={styles.welcome}>{section.sectionTitle}</Text>
          {sectionDialogNodes}
        </View>
      );
    });

    return (
      <View style={{flex:1}}>
      <ToolbarAndroid title="React Native Dialogs" style={styles.toolbar} titleColor="#ddd"/>
      <ScrollView>
        <View style={styles.container}>
          {dialogs}
        </View>
      </ScrollView>
      </View>
    );
  }
}




var styles = StyleSheet.create({
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
  toolbar: {
    backgroundColor: '#311B92',
    height: 56,
  },
});

AppRegistry.registerComponent('ExampleApp', () => ExampleApp);


