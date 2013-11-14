import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchMain {
    public static void main(String[] args) {
        // start echo batch server
        // start echo batch client
        // start generator

        RequestGenerator generator = new RequestGenerator();
        generator.generate(1, TimeUnit.MILLISECONDS, new RequestListener() {
            @Override
            public void onRequest(Request request) {
                // add messages to queue
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
