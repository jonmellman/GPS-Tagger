# **GPS Tagger** Design Document

## Models

1. GPSTag  

**Database Columns:**  
id | label | latitude | longitude | created\_at | updated\_at

Users can save GPSTags, which only hold the latitude and longitude of their current Location. A GoogleMap will then be generated at runtime from each of the user's GPSTags. This minimizes memory storeage requirements.

## .xml views

1. **activity\_main:**  
![main](/Home.jpg)

Simply layout consisting of a large "Tag" button.

2. **activity\_my\_tags:**  
![review](/My Tags.jpg)

my\_tags layout is comprised of a listview which shows the label and created_at fields for each saved GPSTag. A map preview is also displayed.


3. **activity\_view\_tag:**  
![trends](//View Tag.jpg) 

view\_tag contains an EditText to allow the user to modify the current tag's label. Also displayed are the created_at datetime, a larger map preview, and a delete button which deletes the tag and navigates the user back to my\_tags.

## Java Activities

1. **MainActivity:**

onCreate() will create an array of GoogleMaps objects from the items in the GPSTags database. When a user taps the "Tag" button, the GPSTag database is updated with the new entry's latitude and longitude, and a corresponding GoogleMap will be created and added to the existing array of GoogleMaps objects

The new GoogleMap will be passed to the ViewTagActivity so the user can view and edit their new tag.

2. **MyTagsActivity:**

onCreate() will obtain the MainActivity's array of GoogleMaps objects through a getter and populate the .xml file with the data. If a user taps an item in the list, it will pass the GoogleMap object to ViewTagActivity to be displayed.

3. **ViewTagActivity:**

Displays the GoogleMap that was passed in, along with options to edit the label and delete the tag entirely.

After a label has been edited or a tag has been deleted, the associated GoogleMap will be deleted from the array (which was created during MainActivity's initial onCreate()). The GPSTags database will be updated to reflect the changes once the application is closed.
