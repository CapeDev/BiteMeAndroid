#!/usr/bin/env bash

[ -f ./.env ] && . ./.env

git submodule init
git submodule update

emulator -avd emulator -prop persist.sys.language=en -prop persist.sys.country=US -noaudio -no-boot-anim &

echo "Sleeping while emulator initialises..."
sleep 45

mvn install

adb kill-server
adb start-server
adb devices
adb install -r target/trakemoi-1.0.apk

calabash-android run target/trakemoi-1.0.apk