BiteMeAndroid
=============

The BiteMe android app

Getting started
=======
You'll need maven. Lucky you. Once you have maven, do the following:

```
git submodule update --init --recursive
mvn install
```


You need Google Play Service installed in order to provide google map service
```
adb install com.android.vending-1.apk
adb install com.google.android.gms-3025110-v3.0.25\ \(583950-10\).apk
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
calabash-android run target/bitemoi-1.0.apk
```



