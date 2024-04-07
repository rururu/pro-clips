#!/bin/sh

cd $(dirname $0)

java -Djava.library.path=lib -jar lib/CLIPSIDE.jar
