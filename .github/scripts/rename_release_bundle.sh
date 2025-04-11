#!/bin/bash

APP_VERSION=$1
mv app/build/outputs/bundle/release/app-release.aab \
   app/build/outputs/bundle/release/spacex-release_"$APP_VERSION".aab
