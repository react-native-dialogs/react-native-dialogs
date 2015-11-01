import { ToastAndroid } from 'react-native';


var toastCallback = (id, text) => ToastAndroid.show(id + ": " + text, ToastAndroid.SHORT);

var toastMultiChoiceCallback = (ids, items) => {
  ToastAndroid.show(
    ids.map((id, i) => String(id) + " : " + String(items[i])).toString(),
    ToastAndroid.SHORT);
}

var socialNetworks = [
  "Twitter",
  "Google+",
  "Instagram",
  "Facebook"
];

var socialNetworksLong = [
  "Twitter is an online social networking service that enables users to send and read short 140-character messages called \"tweets\"",
  "Google+ is an interest-based social network that is owned and operated by Google Inc. The service is Google\'s fourth foray into social networking.",
  "Instagram is an online mobile photo-sharing, video-sharing and social networking service that enables its users to take pictures and videos, and share them on a variety of social networking platforms.",
  "Google+ is an interest-based social network that is owned and operated by Google Inc. The service is Google\'s fourth foray into social networking.",
  "Instagram is an online mobile photo-sharing, video-sharing and social networking service that enables its users to take pictures and videos, and share them on a variety of social networking platforms.",
  "Facebook is an online social networking service headquartered in Menlo Park, California. Its website was launched on February 4, 2004, by Mark Zuckerberg with friends."
];

export default [
  {
    "sectionTitle": "Basic",
    "dialogs": [
      {
        "buttonText": "Basic (No Title)",
        "data": {
          "content": "This app wants to access your location.",
          "positiveText": "Agree",
          "negativeText": "Disagree"
        }
      },
      {
        "buttonText": "Basic",
        "data": {
          "title": "Use Google's Location Services?",
          "content": "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.",
          "positiveText": "Agree",
          "negativeText": "Disagree"
        }
      },
      {
        "buttonText": "Basic (Long Content)",
        "data": {
          "title": "Use Google's Location Services?",
          "content": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur vitae metus nec sapien elementum interdum ac mollis tortor. Nunc porttitor metus placerat orci auctor, sed dictum magna dapibus. Morbi a lacus ante. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin posuere nisl imperdiet lectus elementum accumsan. Etiam lacinia nisi ut nulla fermentum tempus. Integer ornare lacinia diam non maximus. Vestibulum iaculis urna eu elementum scelerisque. Etiam nisl erat, bibendum vitae magna nec, convallis malesuada erat. Cras non porttitor nibh. Pellentesque rhoncus sem luctus felis tincidunt bibendum. Pellentesque vulputate eros at nulla sollicitudin volutpat. Pellentesque laoreet est sit amet erat laoreet, id molestie nulla efficitur. Interdum et malesuada fames ac ante ipsum primis in faucibus. Sed varius nisl at imperdiet facilisis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur vitae metus nec sapien elementum interdum ac mollis tortor. Nunc porttitor metus placerat orci auctor, sed dictum magna dapibus. Morbi a lacus ante. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin posuere nisl imperdiet lectus elementum accumsan. Etiam lacinia nisi ut nulla fermentum tempus. Integer ornare lacinia diam non maximus. Vestibulum iaculis urna eu elementum scelerisque. Etiam nisl erat, bibendum vitae magna nec, convallis malesuada erat. Cras non porttitor nibh. Pellentesque rhoncus sem luctus felis tincidunt bibendum. Pellentesque vulputate eros at nulla sollicitudin volutpat. Pellentesque laoreet est sit amet erat laoreet, id molestie nulla efficitur. Interdum et malesuada fames ac ante ipsum primis in faucibus. Sed varius nisl at imperdiet facilisis.",
          "positiveText": "Agree",
          "negativeText": "Disagree"
        }
      },
      {
        "buttonText": "Basic (Icon)"
      }
    ]
  },
  {
    "sectionTitle": "Action Buttons",
    "dialogs": [
      {
        "buttonText": "Stacked Buttons",
        "data": {
          "title": "Use Google's Location Services?",
          "content": "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.",
          "positiveText": "Turn On Speed Boost Now",
          "negativeText": "No Thanks",
          "forceStacking": true,
        }
      },
      {
        "buttonText": "Neutral Button",
        "data": {
          "title": "Use Google's Location Services?",
          "content": "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.",
          "positiveText": "Agree",
          "negativeText": "Disagree",
          "neutralText": "More Info"
        }
      },
      {
        "buttonText": "Callbacks",
        "data": {
          "title": "Use Google's Location Services?",
          "content": "Let Google help apps determine location. This means sending anonymous location data to Google, even when no apps are running.",
          "positiveText": "Agree",
          "negativeText": "Disagree",
          "neutralText": "More Info",
          "onPositive": () => {
            ToastAndroid.show("POSITIVE!", ToastAndroid.SHORT);
          },
          "onNegative": () => {
            ToastAndroid.show("NEGATIVE!", ToastAndroid.SHORT);
          },
          "onNeutral": () => {
            ToastAndroid.show("NEUTRAL!", ToastAndroid.SHORT);
          },
        }
      }
    ]
  },
  {
    "sectionTitle": "Basic Lists",
    "dialogs": [
      {
        "buttonText": "Basic List (No Title)",
        "data": {
          items: socialNetworks,
          itemsCallback: toastCallback
        }
      },
      {
        "buttonText": "Basic List",
        "data": {
          "items": socialNetworks,
          "title": "Social Networks",
          itemsCallback: toastCallback
        }
      },
      {
        "buttonText": "Basic List(Long)",
        "data": {
          "items": [
            'Alabama',
            'Alaska',
            'American Samoa',
            'Arizona',
            'Arkansas',
            'California',
            'Colorado',
            'Connecticut',
            'Delaware',
            'District Of Columbia',
            'Federated States Of Micronesia',
            'Florida',
            'Georgia',
            'Guam',
            'Hawaii',
            'Idaho',
            'Illinois',
            'Indiana',
            'Iowa',
            'Kansas',
            'Kentucky',
            'Louisiana',
            'Maine',
            'Marshall Islands',
            'Maryland',
            'Massachusetts',
            'Michigan',
            'Minnesota',
            'Mississippi',
            'Missouri',
            'Montana',
            'Nebraska',
            'Nevada',
            'New Hampshire',
            'New Jersey',
            'New Mexico',
            'New York',
            'North Carolina',
            'North Dakota',
            'Northern Mariana Islands',
            'Ohio',
            'Oklahoma',
            'Oregon',
            'Palau',
            'Pennsylvania',
            'Puerto Rico',
            'Rhode Island',
            'South Carolina',
            'South Dakota',
            'Tennessee',
            'Texas',
            'Utah',
            'Vermont',
            'Virgin Islands',
            'Virginia',
            'Washington',
            'West Virginia',
            'Wisconsin',
            'Wyoming'
          ],
          itemsCallback: toastCallback,
          "negativeText": "Cancel",
          "title": "State"
        }
      },
      {
        "buttonText": "Basic List (Long Items)",
        "data": {
          items: socialNetworksLong,
          title: "Social Networks",
          itemsCallback: (id, text) => toastCallback,
        }
      }
    ]
  },
  {
    sectionTitle: "Choice Lists",
    dialogs: [
      {
        "buttonText": "Single Choice",
        "data": {
          items: socialNetworks,
          positiveText: "Choose",
          title: "Social Networks",
          itemsCallbackSingleChoice: toastCallback
        }
      },
      {
        "buttonText": "Single Choice (Long Items)",
        "data": {
          items: socialNetworksLong,
          positiveText: "Choose",
          title: "Social Networks",
          itemsCallbackSingleChoice: toastCallback
        }
      },
      {
        "buttonText": "Multi Choice",
        "data": {
          items: socialNetworks,
          positiveText: "Choose",
          title: "Social Networks",
          itemsCallbackMultiChoice: toastMultiChoiceCallback,
          positiveCallback: () => 23,
          "multiChoiceClearButton": true
        }
      },
      {
        "buttonText": "Multi Choice (Long Items)",
        "data": {
          items: socialNetworksLong,
          positiveText: "Choose",
          title: "Social Networks",
          itemsCallbackMultiChoice: toastMultiChoiceCallback
        }
      },
    ]
  }
];
