package MessageUtil;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @program: MinaClientTest
 * @description: 工厂类
 * @author: Yang Yang
 * @create: 2018-03-13 17:11
 **/
public class MessageFactory implements ProtocolCodecFactory{

    private MessageEncoder messageEncoder;
    private MessageDecoder messageDecoder;

    public MessageFactory() {
        messageDecoder = new MessageDecoder();
        messageEncoder = new MessageEncoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return messageEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return messageDecoder;
    }
}
