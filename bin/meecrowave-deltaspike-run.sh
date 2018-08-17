#!/usr/bin/env bash

set -e

[ "$1" == "install" ] && mvn install -pl run-meecrowave-deltaspike -am
mvn meecrowave:run -pl run-meecrowave-deltaspike
