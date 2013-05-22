package net.caprazzi.xmpp.component;


import com.google.common.base.Optional;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

public interface BotResponse {
    Optional<Packet> getPacket();
}
