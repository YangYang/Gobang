package Mina;

import org.apache.mina.core.session.IoSession;

/**
 * @program: MinaClientTest
 * @description: mina监听者接口
 * @author: Yang Yang
 * @create: 2018-03-13 16:44
 **/
public interface SimpleMinaListener {

    /**
     * 接收到消息
     * */
    public void onReceive(Object obj, IoSession ioSession);

    /**
     * 上线
     * */
    public void onLine(Object obj, IoSession ioSession);
}
