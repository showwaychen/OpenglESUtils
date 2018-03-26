cmake_minimum_required(VERSION 3.9)
project (OPENGLCAPTURE)

#defined										 
set(CMAKE_CXX_FLAGS "-Wall -std=c++11 -D__STDC_CONSTANT_MACROS -D__STDC_LIMIT_MACROS -funwind-tables  -Wno-unused-label -Wno-maybe-uninitialized -Wno-unused-but-set-variable -Wno-deprecated-declarations -Wno-comment -Wno-unused-variable -Wno-sign-compare")
if(${CMAKE_BUILD_TYPE} MATCHES "Release")
    set(CMAKE_CXX_FLAGS " ${CMAKE_CXX_FLAGS} -O3")
else()
    set(CMAKE_CXX_FLAGS " ${CMAKE_CXX_FLAGS} -g3")
endif()
set(CMAKE_C_FLAGS "-D__STDC_CONSTANT_MACROS")
ADD_DEFINITIONS( -DWEBRTC_ANDROID -DWEBRTC_POSIX)

set(BASELIBROOT_INCLUDEDIR ${CMAKE_CURRENT_SOURCE_DIR}/../../thirdparts/baselib)
set(BASELIB_INCLUDEDIR ${BASELIBROOT_INCLUDEDIR}/webrtc)
set(BASELIB_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../thirdparts/baselib/android/lib/)

include_directories("${BASELIB_INCLUDEDIR}" "${BASELIBROOT_INCLUDEDIR}")


#baselib
set(BASELIB_LIB  ${BASELIB_PATH}/libbaselib.a)	
set (STATICLIBS ${BASELIB_LIB} )
#project build
file(GLOB  PLAYER_SOURCES_FILES 		${CMAKE_CURRENT_SOURCE_DIR}/*.h
										 ${CMAKE_CURRENT_SOURCE_DIR}/*.cpp
										 )							 
add_library( 
             openglcapture
             SHARED
             ${PLAYER_SOURCES_FILES} )
find_library(log-lib
              log )
find_library( 
              GLESv1_CM-lib
              GLESv1_CM )
find_library( 
              GLESv2-lib
              GLESv2 )
find_library( 
              EGL-LIB
              EGL )
target_link_libraries( 
                       openglcapture
                       ${log-lib} ${GLESv1_CM-lib} ${GLESv2-lib} ${EGL-LIB} ${STATICLIBS})
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI})	
set (CMAKE_INSTALL_PREFIX ${CMAKE_CURRENT_SOURCE_DIR}/libs/${ANDROID_ABI})	
INSTALL(TARGETS openglcapture
				LIBRARY DESTINATION ${CMAKE_INSTALL_PREFIX} 
				ARCHIVE DESTINATION ${CMAKE_INSTALL_PREFIX}
				)			   
if(${CMAKE_BUILD_TYPE} MATCHES "Release")
    ADD_CUSTOM_COMMAND(TARGET openglcapture
    POST_BUILD
    COMMAND   ${CMAKE_STRIP} ./*.so )	
endif()	
