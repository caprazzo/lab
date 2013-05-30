package net.caprazzi.xmpp.component.bot;

import org.xmpp.packet.Packet;

public interface PacketProcessor {
    public ResponsePacket processPacket(Packet message);
}

