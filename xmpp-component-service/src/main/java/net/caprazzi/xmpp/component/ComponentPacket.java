package net.caprazzi.xmpp.component;

import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

public class ComponentPacket {
    private final Component component;
    private final Packet packet;

    public ComponentPacket(Component component, Packet packet) {
        this.component = component;
        this.packet = packet;
    }

    public Component getComponent() {
        return component;
    }

    public Packet getPacket() {
        return packet;
    }
}
