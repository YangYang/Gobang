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
     * tag 1 正常开始，0 房间已满/不存在房间，-1 不能加入自己的房间
     * */
    public void onReceive(Object obj, IoSession ioSession, int tag);

    /**
     * 上线
     * */
    public void onLine(String msg);

}
