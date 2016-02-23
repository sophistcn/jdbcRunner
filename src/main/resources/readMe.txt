1、如果需要连接Oracle，需要本地Maven仓库安装Oracle Jdbc驱动。
	如果已经安装，替换pom.xml里面的oracle驱动坐标。
	如果没有，需要安装驱动到本地maven仓库。
		1、打开cmd窗口，定位到本项目根目录。
		2、执行下面maven脚本：
		mvn install:install-file -Dfile=./lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.1.0 -Dpackaging=jar

2、
	