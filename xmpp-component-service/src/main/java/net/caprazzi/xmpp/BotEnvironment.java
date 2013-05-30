package net.caprazzi.xmpp;

import org.xmpp.packet.Packet;

public interface BotEnvironment {
    void send(Packet packet);
}
