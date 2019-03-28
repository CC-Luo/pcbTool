#ETH_TMP=`ifconfig`
#ETH_TMP=${ETH_TMP%% *}
ETH_TMP=en0
echo "eth name is: $ETH_TMP"

if [ -z $ETH_TMP ]; then
        ETH_TMP=lo0
fi

VIR_ETH_COUNT=$#
if [ $VIR_ETH_COUNT -eq 0 ]; then
        echo "Will not del virtual eth"        
        exit 0;
fi

TMP_COUNT=0
for i in $*;
do

        echo "del gived virtual network: ip is $i"
        sudo ifconfig $ETH_TMP -alias $i
        let TMP_COUNT+=1
done
exit 0;

