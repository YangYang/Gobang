package Mina;

import MessageUtil.MessageFactory;
import MyData.MyData;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: MinaClientTest
 * @description: mina工具类
 * @author: Yang Yang
 * @create: 2018-03-13 16:42
 **/
public class MinaUtil {

    /**
     * 客户端用
     * 服务器IP地址
     * */
    private static String serverAddress = "127.0.0.1";

    /**
     * 客户端使用
     * IoSession对象
     * */
    private IoSession session = null;

    /**
     * 客户端对象
     * */
    private static MinaUtil minaUtilClient = null;

    /**
     * 监听器
     * */
    private SimpleMinaListener simpleMinaListener = null;

    /**
     * 单例实现
     * */
    private MinaUtil(SimpleMinaListener simpleMinaListener) {
        this.simpleMinaListener = simpleMinaListener;
        NioSocketConnector connector = new NioSocketConnector();
        connector.setHandler(new MinaClientHandler());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageFactory()));
        ConnectFuture future;
        future = connector.connect(new InetSocketAddress(serverAddress, 9123));
        future.awaitUninterruptibly();
        MinaUtil.this.session = future.getSession();
    }


    public static MinaUtil getInstance(SimpleMinaListener simpleMinaListener){
        if (minaUtilClient == null){
            minaUtilClient = new MinaUtil(simpleMinaListener);
        }
        return minaUtilClient;
    }

    public boolean sent(Object object){
        if(session.isConnected()){
            session.write(object);
            System.out.println("success");
            return true;
        }
        System.out.println("failed");
        return false;
    }

    /**
     * @program: MinaClientTest
     * @description: 客户端Handler
     * @author: Yang Yang
     * @create: 2018-03-13 16:36
     **/
    public class MinaClientHandler extends IoHandlerAdapter {
        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            System.out.println(session.getId());
            System.out.println("messageCaught");
            System.out.println(cause.getMessage());
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            MyData myData = (MyData) message;
            simpleMinaListener.onReceive(message,session,myData.getType());
            System.out.println(session.getId());
            System.out.println("messageReceived");

        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            System.out.println(session.getId());
            System.out.println("messageSent");
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionClosed");
        }

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionCreated");
        }
    }
}
