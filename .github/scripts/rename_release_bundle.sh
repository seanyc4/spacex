#!/bin/bash

VERSION=$1
mv app/build/outputs/bundle/release/app-release.aab \
   app/build/outputs/bundle/release/spacex-release_"$VERSION".aab
