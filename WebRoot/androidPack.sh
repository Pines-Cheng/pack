#!/bin/sh
androidTempProjectPath=$1


source ${androidTempProjectPath}/android.properties

if [ $# -eq 0 ] ; then
        echo "Usage:"$0 "dir!"
        exit 0
else
        cd ${androidTempProjectPath}
        touch shellOperateLog
        ant auto-release &> shellOperateLog
        
        #package the apk ,waiting for load
        if [ -e ${androidTempProjectPath}/bin/${publishApkName}_V${versionName}.apk ];then
        	cd ${androidTempProjectPath}/bin
        	tar -cvf ../androidapp.tar  ./${publishApkName}_V${versionName}.apk
        	exit 0
        else
        	exit 1	
        fi
fi

