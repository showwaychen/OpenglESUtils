# OpenglESUtils
 **此库是对opengles 的一些常用操作的封装。
   包括egl、opengl es线程、纹理加载操作、关联到texture的framebuffer等常用功能。
   
#NDK层编译步骤
   系统环境：Ubuntu
   ndk版本：12b
   cmake3.9以上
   
   export NDKROOT=/ndk dir/
   mkdir thirdparts
   cd thirdparts
   git clone https://github.com/showwaychen/baselib.git
   ./build.sh
   cd ../openglesutils/openglcapture
   ./build.sh
   
