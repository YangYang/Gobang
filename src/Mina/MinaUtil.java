package Mina;

import MessageUtil.MessageFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

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
    private String serverAddress = null;


    /**
     * 客户端使用
     * IoSession对象
     * */
    private IoSession session = null;

    /**
     * 服务器用：
     * NioSocketAcceptor
     * */
    private static NioSocketAcceptor acceptor = null;

    /**
     * 会话列表
     * */
    private List<IoSession> sessions = new ArrayList<>();

    /**
     * 标记
     * */
    private Boolean isServer = null;

    /**
     * 服务端对象
     * */
    private static MinaUtil minaUtilServer = null;

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
    private MinaUtil(SimpleMinaListener simpleMinaListener, Boolean isServer, String serverAddress) {
        this.isServer = isServer;
        this.serverAddress = serverAddress;
        this.simpleMinaListener = simpleMinaListener;
        if(isServer){
            //服务端
            if(acceptor == null){
                //初次建立
                //初始化NioProcessor，个数为CPU个数 + 1
                acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
            }
            acceptor.setReuseAddress(true);//主服务监听的端口可以重用
            acceptor.getSessionConfig().setReadBufferSize(8192);
            acceptor.setHandler(new MinaServerHandler());
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageFactory()));
        } else {
            //客户端
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    NioSocketConnector connector = new NioSocketConnector();
//                    connector.setHandler(new MinaClientHandler());
//                    connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageFactory()));
//                    ConnectFuture future;
//                }
//            });
        }
    }


    public static MinaUtil getInstance(SimpleMinaListener simpleMinaListener, Boolean isServer, String serverAddress){
        if(isServer){
            //是服务器
            if(minaUtilServer == null){
                minaUtilServer = new MinaUtil(simpleMinaListener,isServer,null);
            }
            return minaUtilServer;
        } else {
            //是客户端
            if (minaUtilClient == null){
                minaUtilClient = new MinaUtil(simpleMinaListener,isServer,serverAddress);
            }
            return minaUtilClient;
        }
    }












    /**
     * @program: MinaClientTest
     * @description: 服务器Handler，实现各种服务器消息的回调
     * @author: Yang Yang
     * @create: 2018-03-13 16:37
     **/
    public class MinaServerHandler extends IoHandlerAdapter {
        /**
         * @Description: 异常捕捉
         * @Param: [session, cause]
         * @return: void
         * @Author: Yang Yang
         * @Time: 16:38 2018/3/13
         **/
        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            System.out.println(session.getId());
            System.out.println("messageCaught");
            System.out.println(cause.getMessage());
        }

        /**
         * @Description: 接受到消息的回调
         * @Param: [session, message]
         * @return: void
         * @Author: Yang Yang
         * @Time: 16:38 2018/3/13
         **/
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            System.out.println(session.getId());
            System.out.println("messageReceived");
        }

        /**
         * @Description: 发送消息
         * @Param: [session, message]
         * @return: void
         * @Author: Yang Yang
         * @Time: 16:39 2018/3/13
         **/
        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            System.out.println(session.getId());
            System.out.println("messageSent");
        }

        /**
         * @Description:打开Session
         * @Param: [session]
         * @return: void
         * @Author: Yang Yang
         * @Time: 16:39 2018/3/13
         **/
        @Override
        public void sessionOpened(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionOpened");
            simpleMinaListener.onLine("对方玩家("+ session.getRemoteAddress().toString().replaceAll("/","") +")已上线");
        }

        /**
         * @Description: 关闭Session
         * @Param: [session]
         * @return: void
         * @Author: Yang Yang
         * @Time: 16:39 2018/3/13
         **/
        @Override
        public void sessionClosed(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionClosed");
        }

        /**
         * @Description:客户端空闲时回调
         * @Param: [session, status]
         * @return: void
         * @Author: Yang Yang
         * @Time: 16:40 2018/3/13
         **/
        @Override
        public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionIdle");
        }

        /**
         * @Description: session创建回调
         * @Param: [session]
         * @return: void
         * @Author: Yang Yang
         * @Time: 16:41 2018/3/13
         **/
        @Override
        public void sessionCreated(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionCreated");
//            sessions.add(session);
        }
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
//            simpleListener.onReceive(message,session);
            simpleMinaListener.onReceive(message,session);
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
