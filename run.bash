#!/bin/bash

clear

export WORKDIR="$( cd  "$(dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
export CLASSPATH=$WORKDIR:$WORKDIR/*:$WORKDIR/*/*:$WORKDIR/*/*/*:$WORKDIR/ext/junit4.10/junit-4.10.jar:$WORKDIR/ext/mockito-core-4.4.0.jar:$WORKDIR/ext/powermock-module-junit4-2.0.9.jar:$WORKDIR/ext/byte-buddy-1.12.8.jar:$WORKDIR/ext/byte-buddy-agent-1.12.8.jar:$WORKDIR/ext/objenesis-3.2.jar:$CLASSPATH

cd $WORKDIR

rm  ./sokoban/*.class
rm  ./test/*.class


# echo
# echo
# echo

# echo $CLASSPATH

javac --class-path $CLASSPATH ./sokoban/*.java -Xdiags:verbose
javac --class-path $CLASSPATH ./test/*.java -Xdiags:verbose

# java sokoban.TextUI
# # java sokoban.GUI

java org.junit.runner.JUnitCore test.TestTextUI #junit4
