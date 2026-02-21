import java.nio.file.Path;
import java.util.concurrent.*;

public class TradingEngine {

    public static void main(String[] args) throws Exception {

        BlockingQueue<Trade> queue = new LinkedBlockingQueue<>();
        Portfolio portfolio = new Portfolio();

        ExecutorService workers = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            workers.submit(new TradeProcessor(queue, portfolio));
        }
        TradeLoader.load(Path.of("trades.csv"), queue);

        Thread.sleep(2000); // allow processing
        portfolio.printSummary();

        workers.shutdownNow();
    }
}