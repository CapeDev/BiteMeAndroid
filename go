#!/usr/bin/env bash

[ -f ./setup-env ] && . ./setup-env

emulator -avd emulator -prop persist.sys.language=en -prop persist.sys.country=US -noaudio -no-boot-anim &

echo "Sleeping while emulator initialises..."
sleep 45

mvn install

adb kill-server
adb start-server
adb devices
adb install -r target/bitemoi-1.0.apk

calabash-android run target/bitemoi-1.0.apk