import React from 'react-native';
var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  NativeModules,
  TouchableNativeFeedback,
} = React;

import DialogAndroid from 'react-native-dialogs';

import dialogData from './dialogData.js';


class ExampleApp extends React.Component {
  constructor(props) {
    super(props);

    this.state = {selected: 0};
  }

  handleClick() {
    var dialog = new DialogAndroid();

    dialog.set({
      title: "Country",
      items: [
        "India",
        "United States",
        "China",
        "Russia",
      ],
      positiveText: "Hola",
      selectedIndex: this.state.selected,
      itemsCallbackSingleChoice: (i) => this.setState({selected: i}),
    });

    dialog.show();
  }

  showDialog(options) {
    var dialog = new DialogAndroid();
    dialog.set(options);
    dialog.show();
  }

  render() {

    var dialogs = dialogData.map((section, i) => {
      var sectionDialogNodes = section.dialogs.map((op, j) =>
        <TouchableNativeFeedback
          key={j}
          background={TouchableNativeFeedback.SelectableBackground()}
          onPress={c => this.showDialog(
            op.data || { title: "NOT IMPLEMENTED!! :-(", positiveText: "OK"})}>
          <View>
            <Text>
              {op.buttonText}
            </Text>
          </View>
        </TouchableNativeFeedback>
      );

      return (
        <View key={i}>
          <Text style={styles.welcome}>{section.sectionTitle}</Text>
          {sectionDialogNodes}
        </View>
      );
    });

    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <TouchableNativeFeedback onPress={(c) =>this.handleClick(c)}>
          <View>
            <Text style={styles.instructions}>
              To get started, edit index.android.js
            </Text>
          </View>
        </TouchableNativeFeedback>
        <Text style={styles.instructions}>
          {"Selected : " + this.state.selected}
        </Text>
        {dialogs}
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
});

AppRegistry.registerComponent('ExampleApp', () => ExampleApp);


