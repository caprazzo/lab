package net.caprazzi.xmpp.component.bot;

import com.google.common.base.Strings;
import net.caprazzi.xmpp.component.NodeFilter;
import net.caprazzi.xmpp.component.PacketRouter;
import net.caprazzi.xmpp.component.Responder;
import net.caprazzi.xmpp.component.utils.AbstractInterceptComponent;
import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentException;
import org.xmpp.packet.Packet;

import java.util.concurrent.Executors;

public class BotServiceManager implements BotService {

    private final Logger Log = LoggerFactory.getLogger(BotServiceManager.class);

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
        Log.info("Adding bot {} to subdomain {} using node filter " + nodeFilter, bot, subdomain);
        router.addBot(bot, subdomain, nodeFilter);
    }

    @Override
    public synchronized void removeBot(Bot bot, String subdomain) {
        Log.info("Removing bot {} from subdomain {}", bot, subdomain);
        router.removeBot(bot, subdomain);
    }

    @Override
    public synchronized void addDomain(final String subdomain, String password) {
        Log.info("Configuring domain {} with secret [{}]", subdomain, !Strings.isNullOrEmpty(password));
        // TODO: make sure the domain does not already exist
        // TODO: make sure the password is not null or empty
        componentManager.setSecretKey(subdomain, password);

        // this is to avoid conflicts after an untidy termination
        // TODO: make this a configuration option
        componentManager.setMultipleAllowed(subdomain, true);
        try {
            componentManager.addComponent(subdomain, new AbstractInterceptComponent(subdomain) {
                @Override
                public void processPacket(Packet packet) {
                    router.route(this, subdomain, packet);
                }
            });
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void removeDomain(String subdomain) throws ComponentException {
        Log.info("Removing domain {}", subdomain);
        componentManager.removeComponent(subdomain);
    }

    @Override
    public void shutdown() {
        for(String domain : router.getDomains()) {
            try {
                removeDomain(domain);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
