package net.caprazzi.xmpp.botservice;

import net.caprazzi.xmpp.component.AnnotatedBotObject;
import net.caprazzi.xmpp.component.NodeFilter;

class AnnotatedBotHolder {
    private final AnnotatedBotObject bot;
    private final NodeFilter nodeFilter;

    public AnnotatedBotHolder(AnnotatedBotObject bot, NodeFilter nodeFilter) {
        this.bot = bot;
        this.nodeFilter = nodeFilter;
    }

    public AnnotatedBotObject getBot() {
        return bot;
    }

    public NodeFilter getNodeFilter() {
        return nodeFilter;
    }
}
