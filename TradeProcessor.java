import java.util.concurrent.BlockingQueue;

public class TradeProcessor implements Runnable {

    private final BlockingQueue<Trade> queue;
    private final Portfolio portfolio;
    private final TradeDAO tradeDAO = new TradeDAO();

    public TradeProcessor(BlockingQueue<Trade> queue, Portfolio portfolio) {
        this.queue = queue;
        this.portfolio = portfolio;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Trade trade = queue.take();

                boolean success = portfolio.applyTrade(trade);
                if (success) {
                    tradeDAO.saveTrade(trade);
                    System.out.println("✅ Trade saved: " + trade.tradeId);
                } else {
                    System.out.println("❌ Rejected trade: " + trade.tradeId);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void persist(Trade trade) {
        System.out.println("✅ Persisted trade " + trade.tradeId);
    }
}