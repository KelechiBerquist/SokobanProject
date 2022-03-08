#!/bin/bash

clear

export WORKDIR="$( cd  "$(dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

export CLASSPATH=$WORKDIR:$WORKDIR/*:$WORKDIR/*/*:$WORKDIR/*/*/*:$CLASSPATH

cd $WORKDIR

# echo
# echo
# echo

javac --class-path $CLASSPATH ./sokoban/actions/*.java -Xdiags:verbose
javac --class-path $CLASSPATH ./sokoban/ui/*.java -Xdiags:verbose

java sokoban.ui.SokobanTextUI


# error: non-static variable  cannot be referenced from a static context
