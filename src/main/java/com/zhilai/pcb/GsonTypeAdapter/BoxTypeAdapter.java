package com.zhilai.pcb.GsonTypeAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.zhilai.pcb.business.entity.Box;

import java.io.IOException;

public class BoxTypeAdapter extends TypeAdapter<Box> {

    @Override
    public Box read(final JsonReader in) throws IOException{
        final Box box = new Box();

        in.beginObject();
        while (in.hasNext()){
            switch(in.nextName()){
                case "lockerId":
                    box.setBoxNo(in.nextInt());
                    break;
                case "column":
                    box.setColumn(in.nextString());
                    break;
                case "row":
                    box.setRow(in.nextString());
                    break;
                case "doorStatus":
                    box.setDoorState("Open".equals(in.nextString())?"1":"0");
                    break;
                case "occupancyStatus":
                    box.setItemState("Full".equals(in.nextString())?"1":"0");
                    break;

            }
        }
        in.endObject();
        return box;
    }

    @Override
    public void write(final JsonWriter out,final Box box) throws  IOException{
        out.beginObject();
        out.name("lockerId").value(box.getBoxNo());
        out.name("column").value(box.getColumn());
        out.name("row").value(box.getRow());
        out.name("occupancyStatus").value("1".equals(box.getItemState())?"Full":"Empty");
        out.name("doorStatus").value("1".equals(box.getDoorState())?"Open":"Close");
        out.toString();
        out.endObject();
    }
}
