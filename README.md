# GPS Tagger

## Description

GPS Tagger is an android application that allows users to easily "tag" their location, which saves their current GPS coordinates to the device. This information can then be viewed, labeled, and bookmarked.

While a simple concept, GPS Tagger has practical uses.

For example:

* Users can tag bus stops, friend's houses, restaurants, and any location they'll want to find later.
* Alternatively, users can tag where they park their car (or bike) and not worry about forgetting where they left it


There are also several interesting possibilies for extending GPS Tagger's functionality:

* Tag sharing
* NFC-activated tagging (tap an NFC tile on your car/bike to automatically tag its location)
* Augmented-reality tag viewing (much like [Yelp Monocle](http://wearehmc.com/blog/what-is-yelp-monocle/)), in which the user's tags are overlayed on the real-world view from the phone's camera

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
