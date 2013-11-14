import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FixedLengthFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.util.List;
import java.util.concurrent.Executors;

public class EchoServer {

    private final int port;

    public static void main(String[] args) throws Exception {
        new EchoServer(3333).run();
    }

    public EchoServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                    new LengthFieldBasedFrameDecoder(172, 0, 8),
                    new Request.BatchRequestDecoder(),
                    new EchoServerHandler());
            }
        });

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        bootstrap.bind(new InetSocketAddress(port));

    }

    public static class EchoServerHandler  extends SimpleChannelHandler {

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws UnsupportedEncodingException {

            List<Request> requests = (List<Request>) e.getMessage();

            System.out.println("Received a batch of " + requests.size() + " requests");

            for(Request request : requests) {
                System.out.println(request);
            }

            // send the batch back right away
            ctx.getChannel().write(Request.BatchRequestDecoder.encode(requests));
        }

        @Override
             public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
            e.getCause().printStackTrace();

            Channel ch = e.getChannel();
                     ch.close();
        }
    }
}
