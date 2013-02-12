#!/bin/sh
# run from inside project adjacent to gluegen and jogl
cp ../gluegen/build/gluegen-java-src.zip                    ../com.jogamp.jogl
cp ../gluegen/build/gluegen-rt.jar                          ../com.jogamp.jogl
cp ../gluegen/build/gluegen-rt-natives-macosx-universal.jar ../com.jogamp.jogl

cp ../jogl/build/jar/jogl-all.jar                           ../com.jogamp.jogl
cp ../jogl/build/jar/jogl-all-natives-macosx-universal.jar  ../com.jogamp.jogl
cp ../jogl/build/jogl-java-src.zip                          ../com.jogamp.jogl
