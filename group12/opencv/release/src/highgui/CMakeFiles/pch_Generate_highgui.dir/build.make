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

# Utility rule file for pch_Generate_highgui.

src/highgui/CMakeFiles/pch_Generate_highgui: src/highgui/_highgui.h.gch/highgui_RELEASE.gch

src/highgui/_highgui.h.gch/highgui_RELEASE.gch: ../src/highgui/_highgui.h
src/highgui/_highgui.h.gch/highgui_RELEASE.gch: src/highgui/_highgui.h
src/highgui/_highgui.h.gch/highgui_RELEASE.gch: lib/libhighgui_pch_dephelp.a
	$(CMAKE_COMMAND) -E cmake_progress_report /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold "Generating _highgui.h.gch/highgui_RELEASE.gch"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && /usr/bin/c++ -O3 -DNDEBUG -fomit-frame-pointer -O3 -ffast-math -mmmx -DNDEBUG -O3 -DNDEBUG -fomit-frame-pointer -O3 -ffast-math -mmmx -DNDEBUG -fPIC -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/. -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/include -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/include/opencv -I/usr/include -I/usr/include/glib-2.0 -I/usr/lib/glib-2.0/include -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/src/highgui/../../include/opencv -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/src/highgui -I/home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui -DHAVE_CONFIG_H -DHAVE_JPEG -DHAVE_PNG -DHAVE_TIFF -DHAVE_JASPER -DHIGHGUI_EXPORTS -DCVAPI_EXPORTS -DHAVE_CONFIG_H -Wall -pthread -ffunction-sections -x c++-header -o /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/_highgui.h.gch/highgui_RELEASE.gch /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/_highgui.h

src/highgui/_highgui.h: ../src/highgui/_highgui.h
	$(CMAKE_COMMAND) -E cmake_progress_report /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/CMakeFiles $(CMAKE_PROGRESS_2)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold "Generating _highgui.h"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && /usr/bin/cmake -E copy /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/src/highgui/_highgui.h /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/_highgui.h

pch_Generate_highgui: src/highgui/CMakeFiles/pch_Generate_highgui
pch_Generate_highgui: src/highgui/_highgui.h.gch/highgui_RELEASE.gch
pch_Generate_highgui: src/highgui/_highgui.h
pch_Generate_highgui: src/highgui/CMakeFiles/pch_Generate_highgui.dir/build.make
.PHONY : pch_Generate_highgui

# Rule to build all files generated by this target.
src/highgui/CMakeFiles/pch_Generate_highgui.dir/build: pch_Generate_highgui
.PHONY : src/highgui/CMakeFiles/pch_Generate_highgui.dir/build

src/highgui/CMakeFiles/pch_Generate_highgui.dir/clean:
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && $(CMAKE_COMMAND) -P CMakeFiles/pch_Generate_highgui.dir/cmake_clean.cmake
.PHONY : src/highgui/CMakeFiles/pch_Generate_highgui.dir/clean

src/highgui/CMakeFiles/pch_Generate_highgui.dir/depend:
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/edmond/eclipseProjects/group12/OpenCV-2.0.0 /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/src/highgui /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/CMakeFiles/pch_Generate_highgui.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : src/highgui/CMakeFiles/pch_Generate_highgui.dir/depend

