/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */
'use strict';

var React = require('react-native');
var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  NativeModules,
  TouchableNativeFeedback,
} = React;

class ExampleApp extends React.Component {
  constructor(props) {
    super(props);

    this.state = {selected: 0};
  }
  handleClick() {

    NativeModules.SimpleDialog.showItemsDialog({
      title: "Country",
      items: [
        "India",
        "United States",
        "China",
        "Russia",
      ],
      choice: true,
      selectedIndex: this.state.selected,
    },
    (i) => this.setState({selected: i}),
    (x, op) => console.log(x, op)
    );
  }

  render() {
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
