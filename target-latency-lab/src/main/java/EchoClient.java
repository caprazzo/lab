import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FixedLengthFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class EchoClient {

    static final MetricRegistry metrics = new MetricRegistry();
    private static Timer rtt;

    public static void main(String[] args) {
        new EchoClient().connect(3333);
        rtt = metrics.timer(MetricRegistry.name(EchoClient.class, "RTT"));
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(10, TimeUnit.SECONDS);
    }

    public void connect(int port) {
        NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        final EchoClientHandler echoClientHandler = new EchoClientHandler();

        final AtomicLong counter = new AtomicLong();
        final LinkedBlockingQueue <Request> queue = new LinkedBlockingQueue<Request>();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (queue.size() >= 10) {
                    ArrayList<Request> buffer = new ArrayList<Request>(10);
                    queue.drainTo(buffer, 10);
                    echoClientHandler.send(buffer);
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);


        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                    new LengthFieldBasedFrameDecoder(172, 0, 8),
                    new Request.BatchRequestDecoder(),
                    echoClientHandler);
            }
        });

        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(port));
        connect.awaitUninterruptibly();

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                queue.add(new Request(counter.getAndIncrement(), System.nanoTime()));
            }
        }, 1, 5, TimeUnit.MILLISECONDS);
    }

    private static class EchoClientHandler extends SimpleChannelHandler {
        private Channel channel;

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws UnsupportedEncodingException {
            List<Request> responses = (List<Request>) e.getMessage();
            for(Request request : responses) {
                long elapsed = System.nanoTime() - request.getValue();
                rtt.update((elapsed), TimeUnit.NANOSECONDS);
            }
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
            System.out.println("CONNECTED");
            channel = e.getChannel();
        }

        private void send(List<Request> values) {
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
}
