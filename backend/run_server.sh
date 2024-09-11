#! /bin/bash

# ./gradlew bootRun
./gradlew clean bootJar

java -jar build/libs/*SNAPSHOT.jar
