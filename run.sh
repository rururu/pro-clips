#!/bin/sh

cd $(dirname $0)

java -Djava.library.path=lib -jar target/proclips-0.0.1-standalone.jar
