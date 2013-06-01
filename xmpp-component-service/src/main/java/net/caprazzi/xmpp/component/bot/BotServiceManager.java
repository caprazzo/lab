package net.caprazzi.xmpp.component.bot;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import net.caprazzi.xmpp.component.*;
import net.caprazzi.xmpp.component.utils.AbstractInterceptComponent;
import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.ComponentException;
import org.xmpp.packet.Packet;

import java.net.ConnectException;
import java.util.concurrent.Executors;

public class BotServiceManager implements BotService {

    private final Logger Log = LoggerFactory.getLogger(BotServiceManager.class);

    private final ExternalComponentManager componentManager;
    private final SubdomainPacketRouter router;

    public BotServiceManager(ExternalComponentManager componentManager) {
        this.componentManager = componentManager;
        ComponentPacketSender responder = new ComponentPacketSender(componentManager);
        PacketProcessorExecutor botExecutor = new PacketProcessorExecutor(Executors.newFixedThreadPool(10), responder);
        router = new SubdomainPacketRouter(botExecutor);
        responder.start();
    }

    public synchronized void addAnnotatedBot(Object bot, String subdomain, NodeFilter nodeFilter) {
        Log.info("Adding annotated bot {} to subdomain {} using node filter " + nodeFilter, bot, subdomain);
        Optional<AnnotatedBotObject> annotatedBot = AnnotatedBotObject.from(bot);
        if (!annotatedBot.isPresent()) {
            Log.error("Provided annotated bot is not usable a bot implementation: {}", bot);
            return;
        }

       // AnnotatedBotPacketProcessor processor = new AnnotatedBotPacketProcessor(annotatedBot.get(), sender);
       // addBot(processor, subdomain, nodeFilter);
    }

    @Override
    public synchronized void addBot(PacketProcessor bot, String subdomain, NodeFilter nodeFilter) {
        Log.info("Adding bot {} to subdomain {} using node filter " + nodeFilter, bot, subdomain);
        router.addBot(bot, subdomain, nodeFilter);
    }

    @Override
    public synchronized void removeBot(PacketProcessor bot, String subdomain) {
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


        while (true) {
            try {
                componentManager.removeComponent(subdomain);
                componentManager.addComponent(subdomain, new AbstractInterceptComponent(subdomain) {
                    @Override
                    public void processPacket(Packet packet) {
                        Log.debug("Processing incoming packet {}", packet.toXML());
                        router.route(this, subdomain, packet);
                    }
                });
                Log.info("Connected as subdomain " + subdomain);
                break;
            } catch (ComponentException e) {
                e.printStackTrace();
                if (e.getCause() instanceof ConnectException) {

                    Log.warn("Connection failed when configuring subdomain " + subdomain + ". Trying again in 2s");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                }
                else {
                    throw new RuntimeException(e);
                }
            }
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
