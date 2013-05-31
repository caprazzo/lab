package net.caprazzi.xmpp.botservice;

import net.caprazzi.xmpp.component.NodeFilter;
import net.caprazzi.xmpp.component.PacketRouter;
import net.caprazzi.xmpp.component.bot.PacketProcessor;
import net.caprazzi.xmpp.component.utils.AbstractInterceptComponent;
import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.packet.Packet;

public class SubdomainEnvironment {

    private final String subdomain;
    private String secret;
    private final Logger log;

    SubdomainEnvironment(String subdomain) {
        log = LoggerFactory.getLogger(this.getClass() + "." + subdomain);
        this.subdomain = subdomain;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    void connect(ExternalComponentManager componentManager, PacketRouter router) {
        Component component = new PacketRouterComponent(router, subdomain);
        try {
            componentManager.setSecretKey(subdomain, secret);
            componentManager.removeComponent(subdomain);
            componentManager.addComponent(subdomain, component);
            log.info("Connected");
        } catch (ComponentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Maybe to simplify things, instead of nodeFilter, we can start with just one node?
     */
    public void addBot(PacketProcessor bot, NodeFilter nodeFilter) {

    }

    private class PacketRouterComponent extends AbstractInterceptComponent {
        private final PacketRouter router;

        public PacketRouterComponent(PacketRouter router, String subdomain) {
            super(subdomain);
            this.router = router;
        }

        @Override
        public void processPacket(Packet packet) {
            log.debug("Processing incoming packet {}", packet.toXML());
            router.route(this, subdomain, packet);
        }
    }

}
