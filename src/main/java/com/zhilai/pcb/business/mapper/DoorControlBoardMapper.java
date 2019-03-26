package com.zhilai.pcb.business.mapper;

import com.zhilai.pcb.business.entity.DoorControlBoard;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoorControlBoardMapper {
    int insertControlBoard(DoorControlBoard doorControlBoard);
    List<DoorControlBoard> getDoorControlBoards(DoorControlBoard doorControlBoard);
    int deleteDoorControlBoard(String macAddr);
    int upDataDoorControlBoard(DoorControlBoard doorControlBoard);
    int resetDoorControl();
}
