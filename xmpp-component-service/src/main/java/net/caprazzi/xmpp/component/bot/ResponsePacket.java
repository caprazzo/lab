package net.caprazzi.xmpp.component.bot;


import com.google.common.base.Optional;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

public interface ResponsePacket {
    Optional<Packet> getPacket();
}
