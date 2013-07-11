#!/usr/bin/env bash
set -e
PROJECT_ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
cd $PROJECT_ROOT

sudo apt-get update -qq
if [ `uname -m` = x86_64 ]; then sudo apt-get install -qq --force-yes libgd2-xpm ia32-libs ia32-libs-multiarch; fi
wget http://dl.google.com/android/android-sdk_r21.0.1-linux.tgz
tar -xzf android-sdk_r21.0.1-linux.tgz
export ANDROID_HOME=$PROJECT_ROOT/android-sdk-linux
export PATH=${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools
android update sdk --no-ui --force --filter platform-tools,extra-android-support,android-17,sysimg-17,extra-google-google_play_services
git submodule foreach git pull origin master
cp vendor/gms-mvn/gms-mvn-install.sh android-sdk-linux/extras/google/google_play_services/libproject/google-play-services_lib/ 
(cd android-sdk-linux/extras/google/google_play_services/libproject/google-play-services_lib/ && ./gms-mvn-install.sh 7)
echo no | android create avd -n emulator -t android-17 --skin WVGA800 --force --abi armeabi-v7a
gem install calabash-android
