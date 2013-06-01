package net.caprazzi.xmpp.botservice;

import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ComponentPacketSender {

    private final Logger Log = LoggerFactory.getLogger(ComponentPacketSender.class);

    private final ExternalComponentManager manager;

    private BlockingQueue<ComponentPacket> outbox = new LinkedBlockingQueue<ComponentPacket>();

    final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ComponentPacketSender(ExternalComponentManager manager) {
        this.manager = manager;
    }

    public void start() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    ComponentPacket response = outbox.take();
                    Log.debug("Sending response {}", response.getPacket());
                    manager.sendPacket(response.getComponent(), response.getPacket());
                } catch (InterruptedException e) {
                    // it's ok, we have bee cancelled :(
                }
            }
            }
        });
    }

    public void stop() {
        executor.shutdownNow();
    }

    public void send(ComponentPacket componentPacket) {
        try {
            outbox.put(componentPacket);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
