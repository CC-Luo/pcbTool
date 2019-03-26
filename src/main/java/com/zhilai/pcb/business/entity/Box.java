package com.zhilai.pcb.business.entity;

import com.google.gson.annotations.JsonAdapter;
import com.zhilai.pcb.GsonTypeAdapter.BoxTypeAdapter;

/**
 * @ClassName Box
 * @Description
 * @Author zhouhang
 * @Date 2019/1/14
 * @Company 深圳市智莱科技股份有限公司
 */
@JsonAdapter(BoxTypeAdapter.class)
public class Box {
    /** 开门板MAC */
    private String macAddr;
    /** 开门板号 */
    private String id;
    /** 箱号 */
    private Integer boxNo;

    private String column;

    private String row;
    /** 门状态 0:关闭 1：打开 */
    private String doorState;
    /** 物品状态 0：空箱 1：有物 */
    private String itemState;

    private String ip;

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(Integer boxNo) {
        this.boxNo = boxNo;
    }

    public String getColumn(){ return column;}

    public void setColumn(String column){this.column = column;}

    public String getRow(){return row;}

    public void setRow(String row){ this.row = row;}

    public String getDoorState() {
        return doorState;
    }

    public void setDoorState(String doorState) {
        this.doorState = doorState;
    }

    public String getItemState() {
        return itemState;
    }

    public void setItemState(String itemState) {
        this.itemState = itemState;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
