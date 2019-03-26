ETH_TMP=`ifconfig`
ETH_TMP=${ETH_TMP%% *}
echo "eth name is: $ETH_TMP"

if [ -z $ETH_TMP ]; then
        ETH_TMP=eth0
fi

VIR_ETH_COUNT=$#
if [ $VIR_ETH_COUNT -eq 0 ]; then
        echo "Will not add virtual eth"        
        exit 0;
fi

TMP_COUNT=0
for i in $*;
do

        NEW_VIR_ETH=$ETH_TMP:$TMP_COUNT
        echo "add a new virtual: $NEW_VIR_ETH, ip is $i"
        sudo ifconfig $NEW_VIR_ETH $i
        let TMP_COUNT+=1
done
exit 0;

