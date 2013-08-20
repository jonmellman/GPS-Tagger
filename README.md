# GPS Tagger

## Description

GPS Tagger is an android application that allows users to easily "tag" their location, which saves their current GPS coordinates to the device. This information can then be viewed, labeled, and bookmarked. Tag locations can also be opened in Google Maps.

While a simple concept, GPS Tagger has practical uses.

For example:

* Users can tag bus stops, friend's houses, restaurants, and any location they'll want to find later.
* Alternatively, users can tag where they park their car (or bike) and not worry about forgetting where they left it

GPS-Tagger can also interface with (https://play.google.com/store/apps/details?id=com.jwsoft.nfcactionlauncher&hl=en, "NFC Task Launcher") to allow GPS tag storing/clearing upon NFC tag recognition. See Deployment Instructions for how to configure NFC Task Launcher.
I have a tag in my car which I set to toggle between creating and clearing a GPS tag. When I exit my car, I tap the NFC tag to store my car's parking location. When I enter my car, I tag the NFC tag again to clear that stored location data.

## Deployment Instructions

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

## Screen Mockups

1: Home Screen
![Home](/docs/Home.png)

2: My Tags
![My Tags](/docs/My Tags.png)

2: View Tag
![View Tag](/docs/View Tag.png)
