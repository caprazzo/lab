package net.caprazzi.xmpp.component.bot;

import com.google.common.base.Optional;
import net.caprazzi.xmpp.component.bot.BotResponse;
import org.xmpp.packet.Packet;

public class BotResponses {

    public static BotResponse none() {
        return noResponse;
    }

    public static BotResponse from(final Packet packet) {
        return new BotResponse() {
            @Override
            public Optional<Packet> getPacket() {
                return Optional.of(packet);
            }
        };
    }

    private static final BotResponse noResponse = new BotResponse() {
        @Override
        public Optional<Packet> getPacket() {
            return Optional.absent();
        }
    };

}
