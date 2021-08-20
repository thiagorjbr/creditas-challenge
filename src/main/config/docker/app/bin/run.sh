#!/bin/sh

cd /app/bin

umask 027
JAR=`ls *jar`
DIRNAME=`dirname $0`
HOME=`cd $DIRNAME/../; pwd`

. $DIRNAME/run.conf

exec java -server $JAVA_OPTS -jar ${JAR}