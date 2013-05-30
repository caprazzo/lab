package net.caprazzi.xmpp.component.bot;

import com.google.common.base.Optional;
import org.xmpp.packet.Packet;

public class BotResponses {

    public static ResponsePacket none() {
        return noResponse;
    }

    public static ResponsePacket from(final Optional<Packet> packet) {
        return (packet.isPresent())
           ? from(packet.get())
           : none();
    }

    public static ResponsePacket from(final Packet packet) {
        return new ResponsePacket() {
            @Override
            public Optional<Packet> getPacket() {
                return Optional.of(packet);
            }
        };
    }

    private static final ResponsePacket noResponse = new ResponsePacket() {
        @Override
        public Optional<Packet> getPacket() {
            return Optional.absent();
        }
    };

}
