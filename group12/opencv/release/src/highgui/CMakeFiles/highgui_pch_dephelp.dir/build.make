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

# Include any dependencies generated for this target.
include src/highgui/CMakeFiles/highgui_pch_dephelp.dir/depend.make

# Include the progress variables for this target.
include src/highgui/CMakeFiles/highgui_pch_dephelp.dir/progress.make

# Include the compile flags for this target's objects.
include src/highgui/CMakeFiles/highgui_pch_dephelp.dir/flags.make

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o: src/highgui/CMakeFiles/highgui_pch_dephelp.dir/flags.make
src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o: src/highgui/highgui_pch_dephelp.cxx
	$(CMAKE_COMMAND) -E cmake_progress_report /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && /usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o -c /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/highgui_pch_dephelp.cxx

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.i"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/highgui_pch_dephelp.cxx > CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.i

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.s"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && /usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/highgui_pch_dephelp.cxx -o CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.s

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.requires:
.PHONY : src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.requires

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.provides: src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.requires
	$(MAKE) -f src/highgui/CMakeFiles/highgui_pch_dephelp.dir/build.make src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.provides.build
.PHONY : src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.provides

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.provides.build: src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o
.PHONY : src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.provides.build

# Object files for target highgui_pch_dephelp
highgui_pch_dephelp_OBJECTS = \
"CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o"

# External object files for target highgui_pch_dephelp
highgui_pch_dephelp_EXTERNAL_OBJECTS =

lib/libhighgui_pch_dephelp.a: src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o
lib/libhighgui_pch_dephelp.a: src/highgui/CMakeFiles/highgui_pch_dephelp.dir/build.make
lib/libhighgui_pch_dephelp.a: src/highgui/CMakeFiles/highgui_pch_dephelp.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX static library ../../lib/libhighgui_pch_dephelp.a"
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && $(CMAKE_COMMAND) -P CMakeFiles/highgui_pch_dephelp.dir/cmake_clean_target.cmake
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/highgui_pch_dephelp.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
src/highgui/CMakeFiles/highgui_pch_dephelp.dir/build: lib/libhighgui_pch_dephelp.a
.PHONY : src/highgui/CMakeFiles/highgui_pch_dephelp.dir/build

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/requires: src/highgui/CMakeFiles/highgui_pch_dephelp.dir/highgui_pch_dephelp.o.requires
.PHONY : src/highgui/CMakeFiles/highgui_pch_dephelp.dir/requires

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/clean:
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui && $(CMAKE_COMMAND) -P CMakeFiles/highgui_pch_dephelp.dir/cmake_clean.cmake
.PHONY : src/highgui/CMakeFiles/highgui_pch_dephelp.dir/clean

src/highgui/CMakeFiles/highgui_pch_dephelp.dir/depend:
	cd /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/edmond/eclipseProjects/group12/OpenCV-2.0.0 /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/src/highgui /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui /home/edmond/eclipseProjects/group12/OpenCV-2.0.0/release/src/highgui/CMakeFiles/highgui_pch_dephelp.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : src/highgui/CMakeFiles/highgui_pch_dephelp.dir/depend
