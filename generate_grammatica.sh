#!/usr/bin/env bash
java -jar lib/grammatica-1.6.jar pdn/PdnReading.grammar --javaoutput src/main/java --javapackage com.workingbit.shashkiapp.grammar
java -jar lib/grammatica-1.6.jar pdn/Fen.grammar --javaoutput src/main/java --javapackage com.workingbit.shashkiapp.grammar
