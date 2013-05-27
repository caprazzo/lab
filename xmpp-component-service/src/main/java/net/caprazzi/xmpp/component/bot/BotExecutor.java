package net.caprazzi.xmpp.component.bot;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.*;
import net.caprazzi.xmpp.component.ComponentResponse;
import net.caprazzi.xmpp.component.Responder;
import org.xmpp.component.Component;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class BotExecutor {

    private final ListeningExecutorService executorService;
    private final Responder responder;

    public BotExecutor(ExecutorService executorService, Responder responder) {
        this.responder = responder;
        this.executorService = MoreExecutors.listeningDecorator(executorService);
    }

    public void execute(final Component component, Bot bot, Packet packet) {
        ListenableFuture<BotResponse> response = executorService.submit(new BotResponseTask(bot, packet));
        Futures.addCallback(response, new FutureCallback<BotResponse>() {
            @Override
            public void onSuccess(BotResponse response) {
                Preconditions.checkNotNull(response, "Bot handlers can't return null");
                if (response.getPacket().isPresent()) {
                     responder.respond(new ComponentResponse(component, response.getPacket().get()));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private static class BotResponseTask implements Callable<BotResponse> {

        private final Bot bot;
        private final Packet packet;

        public BotResponseTask(Bot bot, Packet packet) {
            this.bot = bot;
            this.packet = packet;
        }

        @Override
        public BotResponse call() throws Exception {
            if (packet instanceof Message) {
                return bot.handleMessage((Message)packet);
            }
            return BotResponses.none();
        }
    }

}
