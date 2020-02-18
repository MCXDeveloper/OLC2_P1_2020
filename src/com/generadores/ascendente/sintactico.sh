#!/bin/sh
# shellcheck disable=SC2164
cd /home/mcalderon/IdeaProjects/OLC2_P1_2020/src/com/analizador/ascendente/sintactico
java -jar /home/mcalderon/Fuentes/Cup/java-cup-11b.jar -parser Sintactico -symbols CUPSim Sintactico.cup
exit