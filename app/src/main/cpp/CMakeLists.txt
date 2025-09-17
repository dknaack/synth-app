cmake_minimum_required(VERSION 3.22.1)

project("synth" C)

add_library(synth SHARED synth.c)
target_link_libraries(synth android log aaudio)
target_compile_options(synth PRIVATE -Wno-unused-parameter)
