package net.caprazzi.xmpp.component.bot;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.*;
import net.caprazzi.xmpp.BotEnvironment;
import net.caprazzi.xmpp.component.ComponentResponse;
import net.caprazzi.xmpp.component.Responder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class BotExecutor {

    private final Logger Log = LoggerFactory.getLogger(BotExecutor.class);

    private final ListeningExecutorService executorService;
    private final Responder responder;

    public BotExecutor(ExecutorService executorService, Responder responder) {
        this.responder = responder;
        this.executorService = MoreExecutors.listeningDecorator(executorService);
    }

    public void execute(final Component component, final PacketProcessor bot, final Packet packet) {

        BotEnvironment environment = new BotEnvironment() {

            @Override
            public void send(Packet packet) {
                Preconditions.checkNotNull(packet, "PacketProcessor can't pass null to envinronment");
                responder.respond(new ComponentResponse(component, packet));
            }
        };

        final ListenableFuture<Object> response = executorService.submit(new BotResponseTask(bot, packet, environment));
        Futures.addCallback(response, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Failure when executing BotResponseTask. Bot: " + bot + ". packet: " + packet, throwable);
            }
        });
    }

    private static class BotResponseTask implements Callable<Object> {

        private final PacketProcessor bot;
        private final Packet packet;
        private final BotEnvironment environment;

        public BotResponseTask(PacketProcessor bot, Packet packet, BotEnvironment env) {
            this.bot = bot;
            this.packet = packet;
            this.environment = env;
        }

        @Override
        public Object call() throws Exception {
            bot.processPacket(packet, environment);
            return null;
        }
    }

}
