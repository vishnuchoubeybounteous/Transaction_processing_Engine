import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;

public class TradingEngine {

    public static void main(String[] args) throws Exception {
        // Create table if not exists
        try (Connection conn = DBUtil.getConnection()) {
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS trades (
                    trade_id BIGINT PRIMARY KEY,
                    account_id BIGINT NOT NULL,
                    symbol VARCHAR(10) NOT NULL,
                    quantity INT NOT NULL,
                    price DOUBLE PRECISION NOT NULL,
                    side VARCHAR(10) NOT NULL,
                    trade_time TIMESTAMP NOT NULL
                )""");
            System.out.println("✅ Database ready");
        } catch (SQLException e) {
            throw new RuntimeException("DB setup failed", e);
        }

        BlockingQueue<Trade> queue = new LinkedBlockingQueue<>();
        Portfolio portfolio = new Portfolio();

        ExecutorService workers = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            workers.submit(new TradeProcessor(queue, portfolio));
        }

        TradeLoader.load(Path.of("trades.csv"), queue);

        // Proper shutdown & wait
        workers.shutdown();
        if (!workers.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("⚠️ Forcing shutdown");
            workers.shutdownNow();
        }

        portfolio.printSummary();
        System.out.println("🏁 Engine stopped");
    }
}
