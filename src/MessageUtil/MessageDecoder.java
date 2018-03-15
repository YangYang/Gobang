package MessageUtil;

import MyData.MyData;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * @program: MinaClientTest
 * @description: 对传输信息进行解密的类
 * @author: Yang Yang
 * @create: 2018-03-13 17:10
 **/
public class MessageDecoder implements ProtocolDecoder {

    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        MyData myData = new MyData();
        myData.setX(ioBuffer.getInt());
        myData.setY(ioBuffer.getInt());
        protocolDecoderOutput.write(myData);
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}