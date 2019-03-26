package com.zhilai.pcb.GsonTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.zhilai.pcb.business.entity.Box;
import com.zhilai.pcb.business.entity.DoorControlBoard;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DoorControlBoardTypeAdapter extends TypeAdapter<List<DoorControlBoard>> {
    public List<DoorControlBoard> read(final JsonReader in) throws IOException {
        final List<DoorControlBoard> doorControlBoards = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()){
            in.beginObject();
            DoorControlBoard doorControlBoard = new DoorControlBoard();
            while(in.hasNext()){
                switch (in.nextName()){
                    case "ipAddress":
                        doorControlBoard.setIp(in.nextString());
                        break;
                    case "macAddress":
                        doorControlBoard.setMacAddr(in.nextString());
                        break;
                    case "unitType":
                        doorControlBoard.setPutPosition(in.nextString());
                        break;
                    case "lockerConfigs":
                        Type type = new TypeToken<ArrayList<Box>>(){}.getType();
                        Gson gson = new Gson();
                        ArrayList<Box> boxs = gson.fromJson(in,type);
                        doorControlBoard.setBoxs(boxs);
                }
            }
            in.endObject();
            doorControlBoards.add(doorControlBoard);
        }
        in.endArray();
        return doorControlBoards;
    }

    @Override
    public void write(final JsonWriter out, final List<DoorControlBoard> doorControlBoards) throws  IOException{
        out.beginArray();
        for(DoorControlBoard doorControlBoard : doorControlBoards){
            out.beginObject();
            out.name("ipAddress").value(doorControlBoard.getIp());
            out.name("macAddress").value(doorControlBoard.getMacAddr());
            out.name("unitType").value(doorControlBoard.getPutPosition());
            out.name("lockerConfigs").value(doorControlBoard.getBoxs().toString());
            out.endObject();
        }
        out.endArray();
    }
}
