import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Portfolio {

    // accountId -> (symbol -> quantity)
    private final ConcurrentHashMap<Long,
            ConcurrentHashMap<String, AtomicInteger>> data = new ConcurrentHashMap<>();

    public boolean applyTrade(Trade trade) {
        data.putIfAbsent(trade.accountId, new ConcurrentHashMap<>());
        ConcurrentHashMap<String, AtomicInteger> positions =
                data.get(trade.accountId);

        positions.putIfAbsent(trade.symbol, new AtomicInteger(0));
        AtomicInteger qty = positions.get(trade.symbol);

        synchronized (qty) {
            if (trade.side == Side.BUY) {
                qty.addAndGet(trade.quantity);
                return true;
            } else {
                if (qty.get() < trade.quantity) {
                    return false; // negative quantity not allowed
                }
                qty.addAndGet(-trade.quantity);
                return true;
            }
        }
    }

    public void printSummary() {
        System.out.println("\n📊 PORTFOLIO SUMMARY");
        data.forEach((account, positions) ->
                positions.forEach((symbol, qty) ->
                        System.out.println("Account " + account +
                                " | " + symbol +
                                " | Qty = " + qty.get())
                )
        );
    }
}