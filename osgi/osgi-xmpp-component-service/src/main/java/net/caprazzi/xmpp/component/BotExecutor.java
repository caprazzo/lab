package net.caprazzi.xmpp.component;

import com.google.common.util.concurrent.*;
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
                if (response.getPacket().isPresent()) {
                     responder.respond(new ComponentResponse(component, response.getPacket().get()));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
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
