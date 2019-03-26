package com.zhilai.pcb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhilai.pcb.GsonTypeAdapter.DoorControlBoardTypeAdapter;
import com.zhilai.pcb.business.entity.Box;
import com.zhilai.pcb.business.entity.DoorControlBoard;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class mainTest {
    public static void main(String[] args) {
        Type type = new TypeToken<List<DoorControlBoard>>() {}.getType();
        final Gson gson = new GsonBuilder().registerTypeAdapter(type,new DoorControlBoardTypeAdapter()).create();


        String json ="[{\"ipAddress\" : \"127.0.0.1\", \"macAddress\" : \"2A:3B:4C:5D:6E:7F\", \"unitType\" : \"unitType (Adder or Starter)\", \"lockerConfigs\" : [{\"lockerId\" : \"1\",\"column\" : \"3\", \"row\" : \"2.\", \"doorStatus\" : \"Open\", \"occupancyStatus\" : \"Empty\"}]}]";

        DoorControlBoard d1 = new DoorControlBoard();
        d1.setIp("192.168.1.12");
        d1.setMacAddr("4D:6F:4E:8B:9E");
        d1.setPutPosition("ADD");
        Box b1 = new Box();
        b1.setBoxNo(1);
        b1.setColumn("2");
        b1.setRow("2");
        b1.setDoorState("1");
        b1.setItemState("1");

        DoorControlBoard d2 = new DoorControlBoard();
        d2.setIp("192.168.1.13");
        d2.setMacAddr("4D:6F:4E:8B:9F");
        d2.setPutPosition("START");
        Box b2 = new Box();
        b2.setBoxNo(2);
        b2.setColumn("3");
        b2.setRow("3");
        b2.setDoorState("0");
        b2.setItemState("0");

        List<Box> boxs = new ArrayList<>();
        boxs.add(b1);
        boxs.add(b2);
        d1.setBoxs(boxs);

        List<DoorControlBoard> doorControlBoards = new ArrayList<DoorControlBoard>();
        doorControlBoards.add(d1);
        doorControlBoards.add(d2);
        System.out.println("json doorcontrolBoards:"+doorControlBoards);
        System.out.println("json:"+gson.toJson(doorControlBoards));

        List<DoorControlBoard> lists = new ArrayList<DoorControlBoard>();
        lists = gson.fromJson(json,type);
        for(DoorControlBoard item : lists){
            System.out.println(item);
        }
    }
}
