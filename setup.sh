#!/bin/bash

# Заполняем gradle.properties по-умолчанию
echo "org.gradle.jvmargs=-Xmx3G -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError" >> gradle.properties
echo "org.gradle.parallel=true" >> gradle.properties
echo "org.gradle.daemon=false" >> gradle.properties
echo "android.useAndroidX=true" >> gradle.properties
echo "android.enableJetifier=true" >> gradle.properties
echo "kotlin.code.style=official" >> gradle.properties

# Генерируем appVersionCode и appVersionName на основе даты и версии билда
INT_DATE=$(date +%Y%m%d)
STRING_DATE=$(date +%Y.%m.%d)

BUILD_NUMBER=$(printf "%02d" $(((BITBUCKET_BUILD_NUMBER % 100))))

echo "appVersionCode=$INT_DATE$BUILD_NUMBER" >> gradle.properties
echo "appVersionName=$STRING_DATE.$BUILD_NUMBER" >> gradle.properties