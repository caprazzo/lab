import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.util.ArrayList;
import java.util.List;

public class Request {

    private final long id;
    private final long value;

    public Request(long id, long value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public long getValue() {
        return value;
    }

    public static class RequestDecoder extends FrameDecoder {

        @Override
        protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
            return new Request(buffer.readLong(), buffer.readLong());
        }

        public static ChannelBuffer encode(Request request) {
            ChannelBuffer buf = ChannelBuffers.buffer(16);
            buf.writeLong(request.getId());
            buf.writeLong(request.getValue());
            return buf;
        }
    }

    @Override
    public String toString() {
        return "Request(id=" + id + ", value=" + value +")";
    }

    public static class BatchRequestDecoder extends FrameDecoder {
        @Override
        protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
            long payload = buffer.readLong();
            int batchSize = buffer.readInt();
            System.out.println("Received a message with payload of " + payload + " bytes, batch=" + batchSize);

            ArrayList<Request> batch = new ArrayList<Request>(batchSize);
            for(int i = 0; i < batchSize; i++) {
                batch.add(new Request(buffer.readLong(), buffer.readLong()));
            }
            return batch;
        }

        /**
         * Encodes an array of requests to a batch request
         * [ payload length header: 8 bytes ][ number of requests: 8 bytes ][ requests: 16 bytes each ]
         * @param batch
         * @return
         */
        public static ChannelBuffer encode(List<Request> batch) {
            // total length of message: [payload:8]-[num requests:8]-[requests: N * 16]
            int lenLengthField = 8;
            int lenRequestsCount = 4;
            int lenRequests = batch.size() * 16;
            int lenPayload = lenRequestsCount + lenRequests;
            int lenMessage = lenLengthField + lenPayload;

            ChannelBuffer buf = ChannelBuffers.buffer(lenMessage);

            System.out.println("Sending batch msg=" + lenMessage + " payload=" + lenPayload);

            // total length of payload: 8 bytes request count + 16 bytes for each request
            buf.writeLong(lenPayload);

            // number of requests
            buf.writeInt(batch.size());

            for(Request request : batch) {
                buf.writeLong(request.getId());
                buf.writeLong(request.getValue());
            }

            return buf;
        }

    }
}
