package net.caprazzi.xmpp.component.bot;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.*;
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
        final ListenableFuture<ResponsePacket> response = executorService.submit(new BotResponseTask(bot, packet));
        Futures.addCallback(response, new FutureCallback<ResponsePacket>() {
            @Override
            public void onSuccess(ResponsePacket response) {
                Preconditions.checkNotNull(response, "PacketProcessor handlers can't return null");
                if (response.getPacket().isPresent()) {
                     responder.respond(new ComponentResponse(component, response.getPacket().get()));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Failure when executing BotResponseTask. Bot: " + bot + ". packet: " + packet, throwable);
            }
        });
    }

    private static class BotResponseTask implements Callable<ResponsePacket> {

        private final PacketProcessor bot;
        private final Packet packet;

        public BotResponseTask(PacketProcessor bot, Packet packet) {
            this.bot = bot;
            this.packet = packet;
        }

        @Override
        public ResponsePacket call() throws Exception {
            return bot.processPacket(packet);
        }
    }

}
