import java.time.LocalDateTime;

public class Trade {
    public final long tradeId;
    public final long accountId;
    public final String symbol;
    public final int quantity;
    public final double price;
    public final Side side;
    public final LocalDateTime timestamp;

    public Trade(long tradeId, long accountId, String symbol,
                 int quantity, double price, Side side,
                 LocalDateTime timestamp) {
        this.tradeId = tradeId;
        this.accountId = accountId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.timestamp = timestamp;
    }
}