import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TradeDAO {

    private static final String INSERT_SQL =
            "INSERT INTO trades " +
                    "(trade_id, account_id, symbol, quantity, price, side, trade_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (trade_id) DO NOTHING";  // Handles duplicates gracefully

    public void saveTrade(Trade trade) {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {
            ps.setLong(1, trade.tradeId);
            ps.setLong(2, trade.accountId);
            ps.setString(3, trade.symbol);
            ps.setInt(4, trade.quantity);
            ps.setDouble(5, trade.price);
            ps.setString(6, trade.side.name());
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(trade.timestamp));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Trade saved: " + trade.tradeId);
            } else {
                System.out.println("⚠️ Duplicate trade skipped: " + trade.tradeId);
            }

        } catch (SQLException e) {
            // Fallback for any other SQL errors
            System.err.println("❌ DB Error for trade " + trade.tradeId + ": " + e.getMessage());
        }
    }
}
