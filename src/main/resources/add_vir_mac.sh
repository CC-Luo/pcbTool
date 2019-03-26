ETH_TMP=`ifconfig`
ETH_TMP=${ETH_TMP%% *}
echo "eth name is: $ETH_TMP"

if [ -z $ETH_TMP ]; then
        ETH_TMP=lo0
fi

VIR_ETH_COUNT=$#
if [ $VIR_ETH_COUNT -eq 0 ]; then
        echo "Will not add virtual eth"        
        exit 0;
fi

TMP_COUNT=0
for i in $*;
do

        echo "add a new virtual: $NEW_VIR_ETH, ip is $i"
        sudo ifconfig lo0 alias $i 255.255.255.0
        let TMP_COUNT+=1
done
exit 0;

