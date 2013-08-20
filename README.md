# GPS Tagger

## Description

GPS Tagger is an android application that allows users to easily "tag" their location, which saves their current GPS coordinates to the device. This information can then be viewed, labeled, and bookmarked. Tag locations can also be opened in Google Maps.

While a simple concept, GPS Tagger has practical uses.

For example:

* Users can tag bus stops, friend's houses, restaurants, and any location they'll want to find later.
* Alternatively, users can tag where they park their car (or bike) and not worry about forgetting where they left it

GPS-Tagger can also interface with [NFC Task Launcher](https://play.google.com/store/apps/details?id=com.jwsoft.nfcactionlauncher&hl=en) to allow GPS tag storing/clearing upon NFC tag recognition. See Deployment Instructions for how to configure NFC Task Launcher.

(I have an NFC tag in my car that I tap when I exit and enter my car so store and clear my location data, respectively, so I don't forget where I parked my car)
## Deployment Instructions

GPS Tagger is not yet in the Android store. To deploy GPS Tagger to your android device, run `git clone git://github.com/jonmellman/GPS-Tagger.git`. Now you can import the project into Google's ADT and push it to your device.

GPS Tagger cannot be run on an android emulator as the Google Maps API requires a physical android device to run.

To configure NFC Task Launcher to create a GPS Tag: 

1. Create a new NFC Task in NFC Task Launcher
2. Add an 'Applications & Shortcuts' action to the new task
3. Select 'Open Activity' at the next menu, then select `GPS Tagger` as the application and `com.jonmellman.gpstagger.ToggleCreate` as the activity.

![Screen Shot](/docs/NFC Task Launcher.png)

To configure NFC Task Launcher to clear a GPS Tag, follow the same steps but replace `com.jonmellman.gpstagger.ToggleCreate` with `com.jonmellman.gpstagger.ToggleClear`



## Features

* Users can easily and conveniently tag their current GPS location
* Tags are saved with an editable label and a date
* Users can browse, label, and delete each of their saved locations
* Users can bookmark tags as "favorites"
* Users can open their locations in Google Maps or similar apps

## Implementation

* Tags will be stored client-side in a SQLite Database
* Standard Android libraries will be used
* GPS Tagger will be developed using Google's early access preview of the Android Studio IDE (v0.1)
* GPS Tagger will be tested on a physical android device - a Galaxy S3 running Jellybean 4.1

## Screenshots

1: Home Screen
![Home](/docs/Home.png)

2: My Tags
![My Tags](/docs/My Tags.png)

2: View Tag
![View Tag](/docs/View Tag.png)
