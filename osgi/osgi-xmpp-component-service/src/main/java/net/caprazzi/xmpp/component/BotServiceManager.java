package net.caprazzi.xmpp.component;

import net.caprazzi.xmpp.component.utils.AbstractInterceptComponent;
import org.jivesoftware.whack.ExternalComponentManager;
import org.xmpp.component.ComponentException;
import org.xmpp.packet.Packet;

import java.util.concurrent.Executors;

public class BotServiceManager implements BotService {

    private final ExternalComponentManager componentManager;
    private final PacketRouter router;

    public BotServiceManager(ExternalComponentManager componentManager) {
        this.componentManager = componentManager;
        Responder responder = new Responder(componentManager);
        BotExecutor botExecutor = new BotExecutor(Executors.newFixedThreadPool(10), responder);
        router = new PacketRouter(botExecutor);
        responder.start();
    }

    @Override
    public synchronized void addBot(Bot bot, String subdomain, NodeFilter nodeFilter) {
        router.addBot(bot, subdomain, nodeFilter);
    }

    @Override
    public synchronized void removeBot(Bot bot, String subdomain) {
        router.removeBot(bot, subdomain);
    }

    @Override
    public synchronized void addDomain(final String subdomain, String password) {
        // TODO: make sure the domain does not already exist
        // TODO: make sure the password is not null or empty
        componentManager.setSecretKey(subdomain, password);
        try {
            componentManager.addComponent(subdomain, new AbstractInterceptComponent(subdomain) {
                @Override
                public void processPacket(Packet packet) {
                    router.route(this, subdomain, packet);
                }
            });
        } catch (ComponentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void removeDomain(String subdomain) throws ComponentException {
        componentManager.removeComponent(subdomain);
    }
}
