package net.caprazzi.xmpp.component;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.*;
import net.caprazzi.xmpp.bot.BotEnvironment;
import net.caprazzi.xmpp.botservice.ComponentPacket;
import net.caprazzi.xmpp.botservice.ComponentPacketSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class PacketProcessorExecutor {

    private final Logger Log = LoggerFactory.getLogger(PacketProcessorExecutor.class);

    private final ListeningExecutorService executorService;
    private final ComponentPacketSender sender;

    public PacketProcessorExecutor(ExecutorService executorService, ComponentPacketSender sender) {
        this.sender = sender;
        this.executorService = MoreExecutors.listeningDecorator(executorService);
    }

    public void execute(final Component component, final PacketProcessor processor, final Packet packet) {

        BotEnvironment environment = new BotEnvironment() {

            @Override
            public void send(Packet packet) {
                Preconditions.checkNotNull(packet, "PacketProcessor can't pass null to envinronment");
                sender.send(new ComponentPacket(component, packet));
            }
        };

        final ListenableFuture<Object> response = executorService.submit(new PacketProcessorTask(processor, packet, environment));
        Futures.addCallback(response, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.error("Failure when executing PacketProcessorTask. Bot: " + processor + ". packet: " + packet, throwable);
            }
        });
    }

    private static class PacketProcessorTask implements Callable<Object> {

        private final PacketProcessor bot;
        private final Packet packet;
        private final BotEnvironment environment;

        public PacketProcessorTask(PacketProcessor bot, Packet packet, BotEnvironment env) {
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
