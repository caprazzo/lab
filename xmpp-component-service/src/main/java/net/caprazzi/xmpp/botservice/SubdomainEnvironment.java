package net.caprazzi.xmpp.botservice;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import net.caprazzi.xmpp.component.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SubdomainEnvironment {

    private final String subdomain;
    private String secret;
    private final Logger log;

    private final ArrayList<AnnotatedBotHolder> bots = new ArrayList<AnnotatedBotHolder>();

    SubdomainEnvironment(String subdomain) {
        Preconditions.checkNotNull(subdomain, "Subdomain must not be null.");
        log = LoggerFactory.getLogger(this.getClass() + "." + subdomain);
        this.subdomain = subdomain;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<AnnotatedBotHolder> getBots() {
        return bots;
    }

    public void addBot(Object bot, String node) {
        Preconditions.checkNotNull(bot, "Bot object must not be null.");
        Preconditions.checkNotNull(node, "node must not be null.");
        addBot(bot, NodeFilters.singleNode(node));
    }

    private void addBot(Object bot, NodeFilter nodeFilter) {
        Preconditions.checkNotNull(bot, "Bot object must not be null.");
        Preconditions.checkNotNull(nodeFilter, "NodeFilter must not be null.");

        Optional<AnnotatedBotObject> annotatedBot = AnnotatedBotObject.from(bot);
        if (!annotatedBot.isPresent()) {
            log.error("Provided annotated bot is not a valid bot implementation: {}", bot);
            return;
        }
        bots.add(new AnnotatedBotHolder(annotatedBot.get(), nodeFilter));
    }

    public String getName() {
        return subdomain;
    }

    public String getSecret() {
        return secret;
    }
}
