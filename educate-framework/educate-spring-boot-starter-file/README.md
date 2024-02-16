### 给maven仓库安装阿里云上传的SDK
必须在jar的目录下面执行下面的语句
mvn install:install-file -DgroupId=com.aliyun -DartifactId=aliyun-sdk-vod-upload -Dversion=1.4.15 -Dpackaging=jar -Dfile=aliyun-java-vod-upload-1.4.15.jar

### 分析
mvn install:install-file

-DgroupId=com.aliyun

-DartifactId=aliyun-sdk-vod-upload

-Dversion=1.4.15

-Dpackaging=jar

-Dfile=aliyun-java-vod-upload-1.4.15.jar