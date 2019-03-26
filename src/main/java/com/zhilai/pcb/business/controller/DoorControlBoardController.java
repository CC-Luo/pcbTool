package com.zhilai.pcb.business.controller;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.zhilai.pcb.GsonTypeAdapter.DoorControlBoardTypeAdapter;
import com.zhilai.pcb.business.entity.Box;
import com.zhilai.pcb.business.entity.DoorControlBoard;
import com.zhilai.pcb.business.service.DoorControlBoardService;
import com.zhilai.pcb.common.R;
import com.zhilai.pcb.protocol.UdpServerAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


@Controller
public class DoorControlBoardController{

    @Autowired
    private DoorControlBoardService doorControlBoardService;
    @Autowired
    private UdpServerAcceptor udpServerAcceptor;

    @RequestMapping("/home")
    public String toHome(ModelMap model){
        List<DoorControlBoard> doorControlBoards = doorControlBoardService.getDoorControlBoards();
        model.addAttribute("doorControlBoards",doorControlBoards);
        return "cabinetConfiguration";
    }

    @RequestMapping("/submit")
    @ResponseBody
    public R submitDoorBoard(HttpServletRequest req) throws Exception{
        String deskData = req.getParameter("data");
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, DoorControlBoard.class);
        List<DoorControlBoard> deskDataList =  (List<DoorControlBoard>)mapper.readValue(deskData, javaType);
        doorControlBoardService.insertDoorBoardConfig(deskDataList);
        return R.ok();
    }

    @RequestMapping("/reSubmit")
    @ResponseBody
    public R reSubmitDoorBoard(HttpServletRequest req) throws  Exception{
        HashMap<String,List<DoorControlBoard>> dataMap = new HashMap<String,List<DoorControlBoard>>(4);
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, DoorControlBoard.class);
        String data = req.getParameter("delData");
        List<DoorControlBoard> delDataList =  (List<DoorControlBoard>)mapper.readValue(data, javaType);
        dataMap.put("delData",delDataList);
        data = req.getParameter("addData");
        List<DoorControlBoard> addDataList =  (List<DoorControlBoard>)mapper.readValue(data, javaType);
        dataMap.put("addData",addDataList);
        data = req.getParameter("upDateData");
        List<DoorControlBoard> upDateData =  (List<DoorControlBoard>)mapper.readValue(data, javaType);
        dataMap.put("upDateData",upDateData);
        doorControlBoardService.reSubmit(dataMap);
        return R.ok();
    }

    @RequestMapping("/reset")
    @ResponseBody
    public R reset(){
        doorControlBoardService.reset();
        return R.ok();
    }

    @RequestMapping("/doorState")
    public String BoardRBoxs(ModelMap model,HttpSession httpSession,HttpServletRequest req){
        httpSession.setAttribute("uid", "2019");
        String ip = req.getServerName();
        int port = req.getServerPort();
        String webApp = req.getContextPath();
        List<DoorControlBoard> lists = doorControlBoardService.getBoardRBoxs();
        model.addAttribute("projectAddr",ip+":"+port+webApp);
        model.addAttribute("lists",lists);
        model.addAttribute("uid","pcbTool");
        return "cabinetState";
    }

    @RequestMapping("/portal")
    public String BoardRBoxss(ModelMap model,HttpSession httpSession,HttpServletRequest req){
        httpSession.setAttribute("uid", "2019");
        String ip = req.getServerName();
        int port = req.getServerPort();
        String webApp = req.getContextPath();
        List<DoorControlBoard> lists = doorControlBoardService.getBoardRBoxs();
        model.addAttribute("projectAddr",ip+":"+port+webApp);
        model.addAttribute("lists",lists);
        model.addAttribute("uid","pcbTool");
        return "configCabinetState";
    }

    @RequestMapping("/changeBoxState")
    @ResponseBody
    public R changeBoxState(Box box){
        int num = doorControlBoardService.changeBoxState(box);
        if(num!=1){
            return R.error("Failed to update box status");
        }
        return R.ok();
    }

    @RequestMapping("/startServer")
    @ResponseBody
    public R startServer(){
        List<DoorControlBoard> doorControlBoardList = doorControlBoardService.getBoardRBoxs();
        try{
            for (DoorControlBoard var : doorControlBoardList){
                udpServerAcceptor.start(var.getIp());
            }
        } catch (Exception e){
            return R.error("Cannot assign requested address: bind");
        }
        return R.ok();
    }

    @RequestMapping("/shutDownServer")
    @ResponseBody
    public R shutDownServer(){
        List<DoorControlBoard> doorControlBoardList = doorControlBoardService.getBoardRBoxs();
        for (DoorControlBoard var : doorControlBoardList){
            udpServerAcceptor.shutDowm(var.getIp());
        }
        return R.ok();
    }

    @RequestMapping("/upload")
    @ResponseBody
    public R upload(@RequestPart("configFile") MultipartFile configFile) {
        List<DoorControlBoard> lists = new ArrayList<DoorControlBoard>();
        try{
            Type type = new TypeToken<List<DoorControlBoard>>() {}.getType();
            final Gson gson = new GsonBuilder().registerTypeAdapter(type,new DoorControlBoardTypeAdapter()).create();
            lists = gson.fromJson(new InputStreamReader(configFile.getInputStream()),type);
            doorControlBoardService.reset();
            doorControlBoardService.insertDoorBoardConfigByConfig(lists);
        }catch (IOException e){
            return R.error(500,"upload fail");
        }
        return R.ok().put("dataList",lists);
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest req, HttpServletResponse resp){
        BufferedOutputStream buff = null;
        try{
            Type type = new TypeToken<List<DoorControlBoard>>() {}.getType();
            Gson gson = new GsonBuilder().registerTypeAdapter(type,new DoorControlBoardTypeAdapter()).excludeFieldsWithoutExposeAnnotation().create();
            ServletOutputStream outputStream = resp.getOutputStream();
            buff = new BufferedOutputStream(outputStream);
            SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HHmmss");
            Calendar calendar= Calendar.getInstance();
            String fileName=sFormat.format(calendar.getTime());
            resp.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt");
            resp.setContentType("text/plain");
            List<DoorControlBoard> doorControlBoards = doorControlBoardService.getBoardRBoxs();
            String jsonData = gson.toJson(doorControlBoards);
            buff.write(jsonData.getBytes("UTF-8"));
            buff.flush();
            buff.close();
        }catch (Exception e){

        }

    }
}
