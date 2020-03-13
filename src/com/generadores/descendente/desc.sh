#!/bin/sh
# shellcheck disable=SC2164
cd /home/mcalderon/IdeaProjects/OLC2_P1_2020/src/com/analizador/descendente
java -cp /home/mcalderon/Fuentes/JavaCC/javacc.jar javacc JavaCCParser.jj
exit