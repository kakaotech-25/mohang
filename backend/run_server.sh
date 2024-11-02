#! /bin/bash

# ./gradlew bootRun
./gradlew clean bootJar

java -jar -Dspring.profiles.active=local build/libs/demo-0.0.1-SNAPSHOT.jar