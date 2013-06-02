package net.caprazzi.xmpp.bot.service;

import net.caprazzi.xmpp.bot.service.reflection.AnnotatedBotObject;

public class BotEnvironment {
    private final AnnotatedBotObject annotatedBot;
    private final String node;
    private String password;

    public BotEnvironment(AnnotatedBotObject annotatedBot, String node) {
        this.annotatedBot = annotatedBot;
        this.node = node;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
