import java.util.concurrent.BlockingQueue;

public class TradeProcessor implements Runnable {

    private final BlockingQueue<Trade> queue;
    private final Portfolio portfolio;

    public TradeProcessor(BlockingQueue<Trade> queue, Portfolio portfolio) {
        this.queue = queue;
        this.portfolio = portfolio;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Trade trade = queue.take(); // waits if empty
                boolean success = portfolio.applyTrade(trade);

                if (success) {
                    persist(trade);
                } else {
                    System.out.println("❌ Rejected trade " + trade.tradeId);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void persist(Trade trade) {
        // simulate DB insert
        System.out.println("✅ Persisted trade " + trade.tradeId);
    }
}