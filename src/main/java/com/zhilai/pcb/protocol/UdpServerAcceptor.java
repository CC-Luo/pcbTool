package com.zhilai.pcb.protocol;

import com.zhilai.pcb.protocol.codec.ByteArrayCodecFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.DatagramAcceptor;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * @ClassName UdpServerAcceptor
 * @Description
 * @Author zhouhang
 * @Date 2019/1/14
 * @Company 深圳市智莱科技股份有限公司
 */
@Component
public class UdpServerAcceptor {
    @Autowired
    private ServerIoHandler serverIoHandler;
    public static Map<String,DatagramAcceptor> acceptorMap = new HashMap<>();

    public void start(String ip) throws Exception {
        DatagramAcceptor acceptor = acceptorMap.get(ip);
        if(acceptor != null){
            acceptor.dispose(true);
            acceptorMap.remove(ip);
        }
        acceptor = new NioDatagramAcceptor();
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        // 注册解码器
        chain.addLast("mycoder", new ProtocolCodecFilter(new ByteArrayCodecFactory()));
        //
        DatagramSessionConfig sessionConfig = acceptor.getSessionConfig();
        sessionConfig.setReceiveBufferSize(1024);
        sessionConfig.setReadBufferSize(2048);
        sessionConfig.setSendBufferSize(1024);
        sessionConfig.setReuseAddress(true);

        acceptor.setHandler(serverIoHandler);
        acceptor.bind(new InetSocketAddress(ip,30000));
        acceptorMap.put(ip,acceptor);
    }

    public void shutDowm(String ip){
        DatagramAcceptor acceptor = acceptorMap.get(ip);
        if(acceptor != null){
            acceptor.dispose(true);
            acceptorMap.remove(ip);
        }
    }
}
