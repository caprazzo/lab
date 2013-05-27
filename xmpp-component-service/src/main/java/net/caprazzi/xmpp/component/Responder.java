package net.caprazzi.xmpp.component;

import org.jivesoftware.whack.ExternalComponentManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Responder {
    private final ExternalComponentManager manager;

    private BlockingQueue<ComponentResponse> outbox = new LinkedBlockingQueue<ComponentResponse>();

    final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Responder(ExternalComponentManager manager) {
        this.manager = manager;
    }

    public void start() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    ComponentResponse response = outbox.take();
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

    public void respond(ComponentResponse componentResponse) {
        try {
            outbox.put(componentResponse);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
