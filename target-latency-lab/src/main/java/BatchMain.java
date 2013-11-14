import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchMain {
    public static void main(String[] args) {
        // start echo batch server
        // start echo batch client
        // start generator
        final MetricRegistry metrics = new MetricRegistry();

        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(10, TimeUnit.SECONDS);

        final Timer timer = metrics.timer(MetricRegistry.name(EchoClient.class, "RTT"));
        final RequestClient client = new RequestClient(new RequestListener() {
            @Override
            public void onRequest(Request request) {
                timer.update(System.nanoTime() - request.getValue(), TimeUnit.NANOSECONDS);
            }
        });


        RequestGenerator generator = new RequestGenerator();
        client.connect(3333);
        generator.generate(1, TimeUnit.MILLISECONDS, new RequestListener() {
            @Override
            public void onRequest(Request request) {
                client.send(request);
            }
        });
    }

    public static class RequestGenerator {
        private final AtomicInteger counter = new AtomicInteger();
        private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        public void generate(int delay, TimeUnit unit, final RequestListener listener) {
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    listener.onRequest(new Request(counter.getAndIncrement(), System.nanoTime()));
                }
            }, 0, delay, unit);
        }
    }

    public interface RequestListener {
        public void onRequest(Request request);
    }
}
