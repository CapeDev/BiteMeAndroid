TrakéMeAndroid
=============

The TrakéMe android app

<p><a href="https://travis-ci.org/CapeDev/TrakeMoi" target="_blank">
<img border="0" src="https://api.travis-ci.org/CapeDev/TrakeMoi.png" ></a></p>

Getting started
=======
You'll need maven. Lucky you. Once you have maven, do the following:

```
git submodule update --init --recursive
mvn clean install

```

You can install the app by :

```
adb install -r target/trakemoi-1.0.apk
```

You need Google Play Service installed in order to provide google map service
```
adb install com.android.vending-1.apk
adb install com.google.android.gms-3025110-v3.0.25\ \(583950-10\).apk

**updated** (for google play service rev7)

adb install com.google.vending.apk
adb install com.google.android.gms.apk
```

Calabash Tests:
==============
To install Calabash: 
```
gem install calabash-android
```

Running calabash tets:

Make sure emulator is running.
And then run:
```
calabash-android run target/trakemoi-1.0.apk
```

More information:
==============
With Google Play Service rev.7  
[More detalis](http://ifyouseeit.org/blog/2013/07/13/google-map-on-android-with-play-service-7/)

