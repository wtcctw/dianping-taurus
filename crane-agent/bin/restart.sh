#/bin/bash
#
tauruspid=`ps -ef |grep com.cip.crane.agent.StartServer |grep -v grep |awk '{print $2}'`

User=ps -ef |grep com.cip.crane.agent.StartServer |grep -v grep |awk '{print $1}

case $User in
	root )
        kill -9 $tauruspid
		bash /data/app/taurus-agent/bin/start.sh &/dev/null
		;;
     nobody )
         kill -9 $tauruspid
         cd /data/app/taurus-agent/
         sudo -u nobody;bin/start.sh &/dev/null
         ;;

         *)
			exit 0
		;;
  esac