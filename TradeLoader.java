import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;

public class TradeLoader {

    public static void load(Path file, BlockingQueue<Trade> queue) throws Exception {
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] t = line.split(",");

                Trade trade = new Trade(
                        Long.parseLong(t[0]),
                        Long.parseLong(t[1]),
                        t[2],
                        Integer.parseInt(t[3]),
                        Double.parseDouble(t[4]),
                        Side.valueOf(t[5]),
                        LocalDateTime.parse(t[6])
                );

                queue.put(trade); // push into queue
            }
        }
    }
}
