#!/usr/bin/env bash
set -e
./build.sh
java -cp bin main.Main --demo
