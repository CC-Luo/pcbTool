ETH_TMP=`ifconfig en0`
ETH_TMP=${ETH_TMP%%:*}
if [ $ETH_TMP != "en0" ]; then
	echo "Don't have en0, please change to the right ethernet!"
	exit -1;
fi

#please change ETH_TMP to your own ethernet card
ETH_TMP=en0

if [ -z $ETH_TMP ]; then
        ETH_TMP=en0
fi

VIR_ETH_COUNT=$#
if [ $VIR_ETH_COUNT -eq 0 ]; then
        echo "Useage:"
	echo "       ./add_vir_mac.sh 192.168.1.101 192.168.1.102 ..."        
        exit -1;
fi

TMP_COUNT=0
for i in $*;
do

        echo "add a new virtual: $NEW_VIR_ETH, ip is $i"
        sudo ifconfig $ETH_TMP alias $i 192.168.1.255
        let TMP_COUNT+=1
done
exit 0;

