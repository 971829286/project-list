#!/usr/bin/env sh

cd `dirname $0`
cd ..

Usage() {
    echo -e "   Usage: $(basename $0) command [options]
    command:
       generate: 从数据字典获取数据生成JAVA Bean
       create: 新增/修改一类数据字典结构, 并且初始化数据, 会升级大版本
       update: 修改数据字典数据, 不修改结构, 会升级小版本
    options:
       -Dfile=data[.dic|.conf] 需要加载的配置文件
       -Dbean=com.souche.xxx.xxxEnum[.java] 通过修改java类来新增/更新,修改数据
    例： 
    $(basename $0) generate
    $(basename $0) create -Dfile=template 或者 -Dbean = com.souche.xxx.xxxEnum
    $(basename $0) update -Dfile=template 或者 -Dbean = com.souche.xxx.xxxEnum
        "
}

p1=$1;
p2=$2;
p3=$3;
p4=$4;
p5=$5;

EXEC_MVN() {
    mvn $1 $p2 $p3 $p4
}

case "$p1" in 
    "")                      Usage            ;;
    "-h")                    Usage            ;;
    generate)       EXEC_MVN dictionary:generate;;
    create)         EXEC_MVN dictionary:create;;
    update)         EXEC_MVN dictionary:update;;
esac
