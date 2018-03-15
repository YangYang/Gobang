package MessageUtil;

import MyData.MyData;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * @program: MinaClientTest
 * @description: 对传输信息进行加密的类
 * @author: Yang Yang
 * @create: 2018-03-13 17:10
 **/
public class MessageEncoder implements ProtocolEncoder {
    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        if (o != null){

            MyData myData = (MyData)o;
            IoBuffer ioBuffer;
            ioBuffer = IoBuffer.allocate(1024).setAutoExpand(true);
            ioBuffer.setAutoShrink(true);
            ioBuffer.setAutoExpand(true);
            ioBuffer.putInt(myData.getX());
            ioBuffer.putInt(myData.getY());
            ioBuffer.flip();
            protocolEncoderOutput.write(ioBuffer);
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
