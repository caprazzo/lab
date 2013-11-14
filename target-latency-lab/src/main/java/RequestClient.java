import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RequestClient  extends SimpleChannelHandler {

    private final BatchMain.RequestListener listener;

    private Channel channel;

    public RequestClient(BatchMain.RequestListener listener) {
        this.listener = listener;
    }

    public void connect(int port) {
        NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new LengthFieldBasedFrameDecoder(172, 0, 8),
                        new Request.BatchRequestDecoder(),
                        RequestClient.this);
            }
        });

        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(port));
        connect.awaitUninterruptibly();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws UnsupportedEncodingException {
        List<Request> responses = (List<Request>) e.getMessage();
        for(Request request : responses) {
            listener.onRequest(request);
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        System.out.println("CONNECTED");
        channel = e.getChannel();
    }

    public void send(Request request) {
        if (channel != null && channel.isWritable()) {
            channel.write(Request.RequestDecoder.encode(request));
        }
    }

    public void send(List<Request> values) {
        if (channel != null && channel.isWritable()) {
            channel.write(Request.BatchRequestDecoder.encode(values));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
