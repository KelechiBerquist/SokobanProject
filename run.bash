#!/bin/bash

clear


export WORKDIR="$( cd  "$(dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
export CLASSPATH=$WORKDIR/sokoban:$CLASSPATH:$WORKDIR/sokoban/*:$CLASSPATH
export CLASSPATH=$WORKDIR/ext/junit4.10/junit-4.10.jar:$WORKDIR/ext/mockito-core-4.4.0.jar:$CLASSPATH
export CLASSPATH=$WORKDIR/ext/powermock-module-junit4-2.0.9.jar:$WORKDIR/ext/byte-buddy-1.12.8.jar:$CLASSPATH
export CLASSPATH=$WORKDIR/ext/byte-buddy-agent-1.12.8.jar:$WORKDIR/ext/objenesis-3.2.jar:$CLASSPATH
export PATH="$(which java)":$PATH
export CWD=$WORKDIR

cd $WORKDIR

javac --class-path $CLASSPATH $WORKDIR/sokoban/*.java -Xdiags:verbose
# # # java org.junit.runner.JUnitCore sokoban.TUITest #junit4

java --class-path $CLASSPATH sokoban.PlaySokoban
