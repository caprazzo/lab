package net.caprazzi.xmpp.component;

import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

public class PacketRequest {

    private final Component component;
    private final String subdomain;
    private final Packet packet;

    public PacketRequest(Component component, String subdomain, Packet packet) {
        this.component = component;
        this.subdomain = subdomain;
        this.packet = packet;
    }

    public Component getComponent() {
        return component;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public Packet getPacket() {
        return packet;
    }
}
