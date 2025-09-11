cmake_minimum_required(VERSION 3.22.1)

project("synth")

add_library(${CMAKE_PROJECT_NAME} SHARED synth.cpp)
find_package(oboe REQUIRED CONFIG)
target_link_libraries(${CMAKE_PROJECT_NAME} android log oboe::oboe)
