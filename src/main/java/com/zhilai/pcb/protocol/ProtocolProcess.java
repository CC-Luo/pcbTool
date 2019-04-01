package com.zhilai.pcb.protocol;

import com.zhilai.pcb.business.entity.Box;
import com.zhilai.pcb.business.entity.DoorControlBoard;
import com.zhilai.pcb.business.mapper.BoxMapper;
import com.zhilai.pcb.business.mapper.DoorControlBoardMapper;
import com.zhilai.pcb.utils.NumberUtil;
import com.zhilai.pcb.websocket.MyWebSocketHandler;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ProtocolProcess
 * @Description 协议响应
 * @Author zhouhang
 * @Date 2019/1/16
 * @Company 深圳市智莱科技股份有限公司
 */
@Component
public class ProtocolProcess {
    @Autowired
    private DoorControlBoardMapper doorControlBoardMapper;
    @Autowired
    private BoxMapper boxMapper;

    public byte[] apply(byte[] applyByte, IoSession session) throws  Exception {
        //获取开门板IP
        String ip = ((InetSocketAddress)session.getServiceAddress()).getAddress().getHostName();

        MyWebSocketHandler.sendMessageToUser(ip,applyByte,"0");

        int start = applyByte[0];
        if (0x4F == start){
            //检查指令长度
            if(applyByte.length == 7){
                //检查格式
                byte[] compareByte = new byte[]{0x4f,0x50,0x45,0x4E,0x23};
                if(Arrays.equals(compareByte,Arrays.copyOf(applyByte,5))){
                    //收到指令答复
                    byte[] receiveConfirm = new byte[]{0x52, 0x45, 0x43, 0x45, 0x49, 0x56, 0x49, 0x4E, 0x47, 0x20, 0x4F, 0x50, 0x45, 0x4E, 0x24, applyByte[5], applyByte[6], 0x20, 0x43, 0x4F, 0x4D, 0x4D, 0x41, 0x4E, 0x44, 0x20, 0x4F, 0x4B};
                    WriteFuture future = session.write(receiveConfirm);
                    future.awaitUninterruptibly();
                    MyWebSocketHandler.sendMessageToUser(ip,receiveConfirm,"1");
                    //间隔700ms
                    Thread.sleep(700);
                    //获取箱号
                    int boxNo = 0;
                    try {
                        boxNo = Integer.parseInt((char)applyByte[5]+""+(char)applyByte[6]);
                        if((boxNo>0 && boxNo<24) || (boxNo>50 && boxNo<56) || (boxNo>90 && boxNo<96)){

                            //获取箱子
                            Box box = findOne(ip,boxNo);
                            if(box == null){
                                byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }

                            if("0".equals(box.getDoorState()) && "0".equals(box.getItemState())){
                                box.setDoorState("1");
                                box.setId(box.getBoxNo()+"");
                                boxMapper.update(box);
                                byte[] returnByte = new byte[]{0x4F, 0x50, 0x45, 0x4E, 0x24, applyByte[5], applyByte[6], 0x20, 0x4F, 0x4B};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }else{
                                byte[] returnByte = new byte[]{0x4F, 0x50, 0x45, 0x4E, 0x24, applyByte[5], applyByte[6], 0x20, 0x46, 0x41, 0x49, 0x4C, 0x45, 0x44};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }
                        }else{
                            //箱号超出范围
                            byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                            MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                            return returnByte;
                        }
                    } catch (NumberFormatException e) {
                        //箱号超出范围
                        byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                        MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                        return returnByte;
                    }
                }
            }

        }else if(0x50 == start){
            //检查指令长度
            if(applyByte.length == 16){
                //查询门磁状态
                byte[] compareByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x44, 0x4F, 0x4F, 0x52, 0x23, 0x41, 0x4C, 0x4C};
                //查询物品状态
                byte[] compareByte2 = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x49, 0x54, 0x45, 0x4D, 0x23, 0x41, 0x4C, 0x4C};
                if(Arrays.equals(applyByte,compareByte)){
                    //收到指令答复
                    byte[] receiveConfirm = new byte[]{0x52, 0x45, 0x43, 0x45, 0x49, 0x56, 0x49, 0x4E, 0x47, 0x20, 0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x44, 0x4F, 0x4F, 0x52, 0x24, 0x41, 0x4C, 0x4C, 0x20, 0x43, 0x4F, 0x4D, 0x4D, 0x41, 0x4E, 0x44, 0x20, 0x4F, 0x4B};
                    WriteFuture future = session.write(receiveConfirm);
                    future.awaitUninterruptibly();
                    MyWebSocketHandler.sendMessageToUser(ip,receiveConfirm,"1");
                    Box box = new Box();
                    box.setIp(ip);
                    List<Box> boxList = boxMapper.findByObj(box);
                    byte[] startByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x41, 0x4C, 0x4C, 0x20, 0x44, 0x4F, 0x4F, 0x52, 0x24, 0x20};
                    byte[] returnByte = Arrays.copyOf(startByte,18+boxList.size());
                    for (int i = 0; i < boxList.size(); i++) {
                        if(boxList.get(i).getDoorState().equals("0"))
                            returnByte[18+i] = 0x53;
                        else
                            returnByte[18+i] = 0x4F;
                    }
                    MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                    return returnByte;
                }else if(Arrays.equals(applyByte,compareByte2)){
                    //收到指令答复
                    byte[] receiveConfirm = new byte[]{0x52, 0x45, 0x43, 0x45, 0x49, 0x56, 0x49, 0x4E, 0x47, 0x20, 0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x49, 0x54, 0x45, 0x4D, 0x24, 0x41, 0x4C, 0x4C, 0x20, 0x43, 0x4F, 0x4D, 0x4D, 0x41, 0x4E, 0x44, 0x20, 0x4F, 0x4B};
                    WriteFuture future = session.write(receiveConfirm);
                    future.awaitUninterruptibly();
                    MyWebSocketHandler.sendMessageToUser(ip,receiveConfirm,"1");
                    Box box = new Box();
                    box.setIp(ip);
                    List<Box> boxList = boxMapper.findByObj(box);
                    byte[] startByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x41, 0x4C, 0x4C, 0x20, 0x49, 0x54, 0x45, 0x4D, 0x24, 0x20};
                    byte[] returnByte = Arrays.copyOf(startByte,18+boxList.size());
                    for (int i = 0; i < boxList.size(); i++) {
                        if(boxList.get(i).getItemState().equals("0"))
                            returnByte[18+i] = 0x45;
                        else
                            returnByte[18+i] = 0x4F;
                    }
                    MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                    return returnByte;
                }
            }else if(applyByte.length == 15){
                //查询门磁状态
                byte[] compareByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x44, 0x4F, 0x4F, 0x52, 0x23};
                //查询物品状态
                byte[] compareByte2 = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x49, 0x54, 0x45, 0x4D, 0x23};
                //取前13个字节
                byte[] startBytes = Arrays.copyOf(applyByte,13);
                if(Arrays.equals(startBytes,compareByte)){
                    //收到指令答复
                    byte[] receiveConfirm = new byte[]{0x52, 0x45, 0x43, 0x45, 0x49, 0x56, 0x49, 0x4E, 0x47, 0x20, 0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x44, 0x4F, 0x4F, 0x52, 0x24, applyByte[13], applyByte[14], 0x20, 0x43, 0x4F, 0x4D, 0x4D, 0x41, 0x4E, 0x44, 0x20, 0x4F, 0x4B};
                    WriteFuture future = session.write(receiveConfirm);
                    future.awaitUninterruptibly();
                    MyWebSocketHandler.sendMessageToUser(ip,receiveConfirm,"1");
                    //获取箱号
                    int boxNo = 0;
                    try {
                        boxNo = Integer.parseInt((char)applyByte[13]+""+(char)applyByte[14]);
                        if((boxNo>0 && boxNo<24) || (boxNo>50 && boxNo<56) || (boxNo>90 && boxNo<96)){
                            //获取箱子
                            Box box = findOne(ip,boxNo);
                            if(box == null){
                                byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }

                            if("0".equals(box.getDoorState())){
                                byte[] returnByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x44, 0x4F, 0x4F, 0x52, 0x24, applyByte[13], applyByte[14], 0x53};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }else{
                                byte[] returnByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x44, 0x4F, 0x4F, 0x52, 0x24, applyByte[13], applyByte[14], 0x4F};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }

                        }else{
                            //箱号超出范围
                            byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                            MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                            return returnByte;
                        }
                    } catch (NumberFormatException e) {
                        //箱号超出范围
                        byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                        MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                        return returnByte;
                    }
                }else if(Arrays.equals(startBytes,compareByte2)){
                    //收到指令答复
                    byte[] receiveConfirm = new byte[]{0x52, 0x45, 0x43, 0x45, 0x49, 0x56, 0x49, 0x4E, 0x47, 0x20, 0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x49, 0x54, 0x45, 0x4D, 0x24, applyByte[13], applyByte[14], 0x20, 0x43, 0x4F, 0x4D, 0x4D, 0x41, 0x4E, 0x44, 0x20, 0x4F, 0x4B};
                    WriteFuture future = session.write(receiveConfirm);
                    future.awaitUninterruptibly();
                    MyWebSocketHandler.sendMessageToUser(ip,receiveConfirm,"1");
                    //获取箱号
                    int boxNo = 0;
                    try {
                        boxNo = Integer.parseInt((char)applyByte[13]+""+(char)applyByte[14]);
                        if((boxNo>0 && boxNo<24) || (boxNo>50 && boxNo<56) || (boxNo>90 && boxNo<96)){
                            //获取箱子
                            Box box = findOne(ip,boxNo);
                            if(box == null){
                                byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }
                            if("0".equals(box.getItemState())){
                                byte[] returnByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x49, 0x54, 0x45, 0x4D, 0x24, applyByte[13], applyByte[14], 0x45};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }
                            else{
                                byte[] returnByte = new byte[]{0x50, 0x4F, 0x4C, 0x4C, 0x49, 0x4E, 0x47, 0x20, 0x49, 0x54, 0x45, 0x4D, 0x24, applyByte[13], applyByte[14], 0x4F};
                                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                                return returnByte;
                            }
                        }else{
                            //箱号超出范围
                            byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                            MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                            return returnByte;
                        }
                    } catch (NumberFormatException e) {
                        //箱号超出范围
                        byte[] returnByte = new byte[]{0x42, 0x4F, 0x58, 0x4E, 0x55, 0x4D, 0x42, 0x45, 0x52, 0x20, 0x49, 0x53, 0x20, 0x4F, 0x55, 0x54, 0x20, 0x4F, 0x46, 0x20, 0x52, 0x41, 0x4E, 0x47, 0x45};
                        MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                        return returnByte;
                    }
                }
            }
        }else if(0x47 == start){
            byte[] compareByte = new byte[]{0x47, 0x45, 0x54, 0x20, 0x46, 0x49, 0x52, 0x4D, 0x57, 0x41, 0x52, 0x45, 0x20, 0x56, 0x45, 0x52, 0x53, 0x49, 0x4F, 0x4E, 0x23};
            if(Arrays.equals(compareByte,applyByte)){
                byte[] receiveConfirm = new byte[]{0x52, 0x45, 0x56, 0x45, 0x49, 0x56, 0x49, 0x4E, 0x47, 0x20, 0x47, 0x45, 0x54, 0x20, 0x46, 0x49, 0x52, 0x4D, 0x57, 0x41, 0x52, 0x45, 0x20, 0x56, 0x45, 0x52, 0x53, 0x49, 0x4F, 0x4E, 0x20, 0x43, 0x4F, 0x4D, 0x4D, 0x41, 0x4E, 0x44, 0x20, 0x4F, 0x4B};
                WriteFuture future = session.write(receiveConfirm);
                future.awaitUninterruptibly();
                MyWebSocketHandler.sendMessageToUser(ip,receiveConfirm,"1");
                byte[] returnByte = new byte[]{0x46, 0x49, 0x52, 0x4D, 0x57, 0x41, 0x52, 0x45, 0x20, 0x56, 0x45, 0x52, 0x53, 0x49, 0x4F, 0x4E, 0x24, 0x20, 0x56, 0x32, 0x2E, 0x31, 0x31};
                MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
                return returnByte;
            }
        }

        byte[] returnByte = invalidFormat(applyByte);
        MyWebSocketHandler.sendMessageToUser(ip,returnByte,"1");
        return returnByte;
    }

    public byte[] invalidFormat(byte[] applyByte){
        byte[] returnByte = new byte[4];
        returnByte[0] = 0x03;
        if(applyByte.length>1)
            returnByte[1] = applyByte[1];
        else
            returnByte[1] = 0x01;
        returnByte[2] = (byte) 0x81;
        returnByte[3] = NumberUtil.getXor(returnByte);
        return returnByte;
    }

    public Box findOne(String ip,int boxNo){
        Box box = new Box();
        box.setIp(ip);
        box.setBoxNo(boxNo);
        List<Box> boxList = boxMapper.findByObj(box);
        return boxList.size()>0?boxList.get(0):null;
    }
}
