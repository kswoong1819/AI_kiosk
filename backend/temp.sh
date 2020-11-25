echo "> Check the currently running spring server pid "
 CURRENT_PID=$(ps -ef | grep java | grep tomcat | awk '{print $2}')
echo "$CURRENT_PID"
 if [ -z $CURRENT_PID ]; then
echo "> No applications are currently running and will not be shut down."
else
echo "> kill -9 $CURRENT_PID"
sudo kill -9 $CURRENT_PID
fi
