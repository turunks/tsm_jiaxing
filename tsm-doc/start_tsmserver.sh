pids=$(ps -ef | grep java | grep "\/Tsm-Server-1.0-SNAPSHOT.jar" | grep -v color=auto | awk '{print $2}')
for pid in $pids
do
 echo  $pid
 kill  $pid
done
java -Xms256m -Xmx512m -XX:PermSize=256m -XX:MaxPermSize=256m -XX:MaxNewSize=256m  -jar  ../Tsm-Server-1.0-SNAPSHOT.jar 2>&1 &
#tail -f nohup.out
