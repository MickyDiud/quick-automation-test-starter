#!/bin/bash
cd $0
sleep 1
mvn clean test -Dsurefire.suiteXmlFiles=config/testrun_suite.xml &>logs/logFile.log
echo $?
if [[ $rc -ne '0' ]] ; then
  echo 'could not perform tests';
  exit $rc
fi