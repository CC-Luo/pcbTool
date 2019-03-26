package com.zhilai.pcb.business.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.zhilai.pcb.GsonTypeAdapter.DoorControlBoardTypeAdapter;

import java.util.List;

public class DoorControlBoard {
    /** 开门板编号 */
    @Expose(serialize = false)
    private String id;
    /** MAC地址 */
    @Expose
    @SerializedName("macAddress")
    private String macAddr;
    /** 开门板IP */
    @Expose
    @SerializedName("ipAddress")
    private String ip;
    /** 代别 */
    @Expose(serialize = false)
    private String gen;
    /** 门数 */
    @Expose(serialize = false)
    private Integer boxNum;
    /** 摆放位置 Z00：主柜 L01：左一 L02：左二 R01：右一 R02：右二 */
    @Expose
    @SerializedName("unitType")
    private String putPosition;

    @Expose
    @SerializedName("lockerConfigs")
    private List<Box> boxs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGen() { return gen;};

    public void setGen(String gen) { this.gen = gen;};

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public String getPutPosition() {
        return putPosition;
    }

    public void setPutPosition(String putPosition) {
        this.putPosition = putPosition;
    }

    public List<Box> getBoxs() { return boxs;}

    public void setBoxs(List<Box> boxs) { this.boxs = boxs;}
}
