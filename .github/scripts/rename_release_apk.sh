#!/bin/bash

VERSION=$1
mv app/build/outputs/apk/release/app-release-unsigned-signed.apk \
   app/build/outputs/apk/release/spacexDev-release_"$VERSION".apk
