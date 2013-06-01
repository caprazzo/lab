package net.caprazzi.xmpp.botservice;

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
