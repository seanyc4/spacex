#!/bin/bash

VERSION=$1
main_apk="spacex-debug-main.apk"
branch_apk="spacex-debug_$VERSION.apk"
file_size_one_kb=`du -k "$main_apk" | cut -f1`
file_size_two_kb=`du -k "$branch_apk" | cut -f1`

file_size_one_mb=$(echo $file_size_one_kb | tail -1 | awk {'print $1'} | awk '{ total = $1 / 1024 ; printf("%.2fMB\n", total) }')
file_size_two_mb=$(echo $file_size_two_kb | tail -1 | awk {'print $1'} | awk '{ total = $1 / 1024 ; printf("%.2fMB\n", total) }')

if [[ $file_size_one_kb == $file_size_two_kb ]]; then
    echo "OUTPUT=✅  APK size has not changed." >> $GITHUB_ENV
elif [[ "$file_size_one_kb" -gt "$file_size_two_kb" ]]; then
    change=$((file_size_one_kb-file_size_two_kb))
    file_size=$(echo $change | tail -1 | awk {'print $1'} | awk '{ total = $1 / 1024 ; printf("%.2fMB\n", total) }')
    echo "OUTPUT=📉  New APK is ${file_size} smaller." >> $GITHUB_ENV
elif [[ "$file_size_two_kb" -gt "$file_size_one_kb" ]]; then
    change=$((file_size_two_kb-file_size_one_kb))
    file_size=$(echo $change | tail -1 | awk {'print $1'} | awk '{ total = $1 / 1024 ; printf("%.2fMB\n", total) }')
    echo "OUTPUT=📈  New APK is ${file_size} larger." >> $GITHUB_ENV
fi
