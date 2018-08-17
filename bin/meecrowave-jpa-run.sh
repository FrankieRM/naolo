#!/usr/bin/env bash

set -e

[ "$1" == "install" ] && mvn install -pl run-meecrowave-jpa -am
mvn meecrowave:run -pl run-meecrowave-jpa
