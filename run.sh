#!/bin/sh

cd $(dirname $0)

JARS=lib/'*'
MAIN_CLASS=edu.stanford.smi.protege.Application
OPTIONS=-Xmx1000M
ENCODING=-Dfile.encoding=UTF-8
LIB_PATH=-Djava.library.path=lib

java $OPTIONS $ENCODING $LIB_PATH -cp .:classes:$JARS:src:src/clojure $MAIN_CLASS


