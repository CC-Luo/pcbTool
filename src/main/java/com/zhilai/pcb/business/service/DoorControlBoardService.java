package com.zhilai.pcb.business.service;

import com.zhilai.pcb.business.entity.Box;
import com.zhilai.pcb.business.entity.DoorControlBoard;
import com.zhilai.pcb.business.mapper.BoxMapper;
import com.zhilai.pcb.business.mapper.DoorControlBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DoorControlBoardService {

    @Autowired
    public DoorControlBoardMapper doorControlBoardMapper;
    @Autowired
    public BoxMapper boxMapper;


    public boolean insertDoorBoardConfig(List<DoorControlBoard> dataList){
        List<Box> boxList = new ArrayList<Box>();
        for(DoorControlBoard item : dataList){
            for(int i = 0 ; i < item.getBoxNum();i++){
                Box box = new Box();
                box.setMacAddr(item.getMacAddr());
                box.setBoxNo(i+1);
                box.setDoorState("0");
                box.setItemState("0");
                boxList.add(box);
                boxMapper.insert(box);
            }
            int doorFlag = doorControlBoardMapper.insertControlBoard(item);
        }
        return true;
    }

    public boolean insertDoorBoardConfigByConfig(List<DoorControlBoard> dataList){
        for(DoorControlBoard item : dataList){
            int i=0;
            for(Box box : item.getBoxs()){
                i++;
                box.setMacAddr(item.getMacAddr());
                boxMapper.insert(box);
            }
            item.setBoxNum(i);
            doorControlBoardMapper.insertControlBoard(item);
        }
        return true;
    }

    public boolean reSubmit(HashMap<String,List<DoorControlBoard>> dataMap){
        List<DoorControlBoard> data = dataMap.get("delData");
        for(DoorControlBoard item : data){
            doorControlBoardMapper.deleteDoorControlBoard(item.getMacAddr());
            boxMapper.deleteBox(item.getMacAddr());
        }
        data = dataMap.get("addData");
        for(DoorControlBoard item : data){
            doorControlBoardMapper.insertControlBoard(item);
            for(int i = 0 ; i < item.getBoxNum();i++){
                Box box = new Box();
                box.setMacAddr(item.getMacAddr());
                box.setBoxNo(i+1);
                box.setDoorState("0");
                box.setItemState("0");
                boxMapper.insert(box);
            }
        }
        data = dataMap.get("upDateData");
        for(DoorControlBoard item : data){
            doorControlBoardMapper.upDataDoorControlBoard(item);
        }

        return true;
    }

    public boolean reset(){
        int delDoorControl = doorControlBoardMapper.resetDoorControl();
        int delBox = boxMapper.deleteAll();
        return true;
    }

    public List<DoorControlBoard> getDoorControlBoards(){
        List<DoorControlBoard> lists = doorControlBoardMapper.getDoorControlBoards(null);
        return lists;
    }

    public List<DoorControlBoard> getBoardRBoxs(){
        List<DoorControlBoard> lists = doorControlBoardMapper.getDoorControlBoards(null);
        for(DoorControlBoard doorControlBoard : lists){
            Box box = new Box();
            box.setMacAddr(doorControlBoard.getMacAddr());
            List<Box> boxs = boxMapper.findByObj(box);
            doorControlBoard.setBoxs(boxs);
        }
        return lists;
    }

    /**
     * 操作箱子状态*/
    public int changeBoxState(Box box){
        int num = boxMapper.update(box);
        return num;
    }
}
