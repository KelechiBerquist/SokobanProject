#!/bin/bash

clear

export WORKDIR="$( cd  "$(dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

export CLASSPATH=$WORKDIR:$WORKDIR/*:$WORKDIR/*/*:$WORKDIR/*/*/*:$CLASSPATH

cd $WORKDIR

rm  ./sokoban/*.class

# echo
# echo
# echo

javac --class-path $CLASSPATH ./sokoban/*.java -Xdiags:verbose

# java sokoban.SokobanTextUI
java sokoban.SokobanGUI
