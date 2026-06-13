#!/usr/bin/env bash
set -e
./build.sh
java -ea -cp bin test.TesteSistema
