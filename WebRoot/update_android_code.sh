#!/bin/sh
# 0,check the tmp exist , if not exist mkdir 
echo create /usr/local/Android/svn/tmp dir
mkdir /usr/local/Android/svn/tmp
cd /usr/local/Android/svn/tmp
mkdir /usr/local/Android/svn/Libs
echo

# 1,update svn code to tmp dir
svn checkout https://svn.sucunyun.com/repos/crab/Android/SucunCloudStor .
echo update code from svn
#svn update https://svn.sucunyun.com/repos/crab/Android/SucunCloudStor . 
echo

# 2,copy src code to /Android/pro-samples/SucunCloudStor replace the src dir old code
echo copy src 
cp -rf /usr/local/Android/svn/tmp/src/* /usr/local/Android/pro-samples/SucunCloudStor/src/
echo

# 3,copy res code to /Android/pro-samples/SucunCloudStor replace the res dir old code
echo copy res
cp -rf /usr/local/Android/svn/tmp/res/* /usr/local/Android/pro-samples/SucunCloudStor/res/
echo

# 4,copy assets code to 
echo copy assets
cp -rf /usr/local/Android/svn/tmp/assets/* /usr/local/Android/pro-samples/SucunCloudStor/assets/ 
echo

# 5,checkout library to libs
echo checkout libCode
svn checkout https://svn.sucunyun.com/repos/crab/Android/Library /usr/local/Android/svn/Libs
echo 

# 6,copy lib to libs
cp -rf /usr/local/Android/svn/Libs/* /usr/local/Android/pro-samples/SucunCloudStor/libs/
cp -rf /usr/local/Android/svn/tmp/libs/* /usr/local/Android/pro-samples/SucunCloudStor/libs/


cp -rf /usr/local/Android/svn/tmp/AndroidManifest.xml /usr/local/Android/pro-samples/SucunCloudStor/AndroidManifest.xml
echo

