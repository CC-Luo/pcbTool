package com.zhilai.pcb.protocol;

import com.zhilai.pcb.utils.NumberUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Component
@Scope("prototype")
public class ServerIoHandler extends IoHandlerAdapter {
	@Resource
	private ProtocolProcess protocolProcess;

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		IoBuffer io = (IoBuffer) message;
		byte[] receiveMsg = io.array();
		InetSocketAddress inetSocketAddress = (InetSocketAddress) session.getLocalAddress();
		String ip = inetSocketAddress.getHostString();
		System.out.print("接收到客户端"+ip+"消息：");
		printByteMsg(receiveMsg);
		byte[] returnByte = protocolProcess.apply(receiveMsg, session);
		if(returnByte != null)
			session.write(returnByte);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("收到一个连接");
	}


	public void printByteMsg(byte[] msg){
		for (int i = 0; i < msg.length; i++) {
			System.out.print(NumberUtil.numToHex(msg[i])+" ");
		}
		System.out.println("");
	}

}
