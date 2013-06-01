package net.caprazzi.xmpp.botservice;

import net.caprazzi.xmpp.component.AbstractInterceptComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Packet;

class PacketRoutingComponent extends AbstractInterceptComponent {
    private final SubdomainPacketRouter router;
    private final Logger log;

    public PacketRoutingComponent(SubdomainPacketRouter router, String subdomain) {
        super(subdomain);
        log = LoggerFactory.getLogger(PacketRoutingComponent.class.getName() + "." + subdomain);
        this.router = router;
    }

    @Override
    public void processPacket(Packet packet) {
        log.debug("Processing incoming packet {}", packet.toXML());
        router.route(this, subdomain, packet);
    }
}
