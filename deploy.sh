#!/bin/bash

git pull
./gradlew bootJar
cp build/libs/shashkiserver.jar scripts/
sudo systemctl restart shashki
