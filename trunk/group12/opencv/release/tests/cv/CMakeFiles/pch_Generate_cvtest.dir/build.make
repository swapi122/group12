# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.6

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canoncical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/edmond/eclipseProjects/group12/OpenCV-2.0.0

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release

# Utility rule file for pch_Generate_cvtest.

tests/cv/CMakeFiles/pch_Generate_cvtest: tests/cv/cvtest.h.gch/cvtest_RELEASE.gch

tests/cv/cvtest.h.gch/cvtest_RELEASE.gch: ../tests/cv/src/cvtest.h
tests/cv/cvtest.h.gch/cvtest_RELEASE.gch: tests/cv/cvtest.h
tests/cv/cvtest.h.gch/cvtest_RELEASE.gch: lib/libcvtest_pch_dephelp.a
	$(CMAKE_COMMAND) -E cmake_progress_report /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold "Generating cvtest.h.gch/cvtest_RELEASE.gch"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv && /usr/bin/c++ -O3 -DNDEBUG -fomit-frame-pointer -O3 -ffast-math -mmmx -DNDEBUG -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/. -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/include -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/include/opencv -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/tests/cv/src -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/tests/cv/../cxts -DHAVE_CONFIG_H -DHAVE_CONFIG_H -Wall -pthread -ffunction-sections -x c++-header -o /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv/cvtest.h.gch/cvtest_RELEASE.gch /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv/cvtest.h

tests/cv/cvtest.h: ../tests/cv/src/cvtest.h
	$(CMAKE_COMMAND) -E cmake_progress_report /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/CMakeFiles $(CMAKE_PROGRESS_2)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold "Generating cvtest.h"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv && /usr/bin/cmake -E copy /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/tests/cv/src/cvtest.h /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv/cvtest.h

pch_Generate_cvtest: tests/cv/CMakeFiles/pch_Generate_cvtest
pch_Generate_cvtest: tests/cv/cvtest.h.gch/cvtest_RELEASE.gch
pch_Generate_cvtest: tests/cv/cvtest.h
pch_Generate_cvtest: tests/cv/CMakeFiles/pch_Generate_cvtest.dir/build.make
.PHONY : pch_Generate_cvtest

# Rule to build all files generated by this target.
tests/cv/CMakeFiles/pch_Generate_cvtest.dir/build: pch_Generate_cvtest
.PHONY : tests/cv/CMakeFiles/pch_Generate_cvtest.dir/build

tests/cv/CMakeFiles/pch_Generate_cvtest.dir/clean:
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv && $(CMAKE_COMMAND) -P CMakeFiles/pch_Generate_cvtest.dir/cmake_clean.cmake
.PHONY : tests/cv/CMakeFiles/pch_Generate_cvtest.dir/clean

tests/cv/CMakeFiles/pch_Generate_cvtest.dir/depend:
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/edmond/eclipseProjects/group12/OpenCV-2.0.0 /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/tests/cv /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/tests/cv/CMakeFiles/pch_Generate_cvtest.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : tests/cv/CMakeFiles/pch_Generate_cvtest.dir/depend

