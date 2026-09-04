import java.io.*;
import java.util.*;

public class MagicLoader {
    public static MagicData loadFromText(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            long[] magicNumbers = new long[64];
            long[][] attackTable = new long[64][];
            String line;
            int index = 0;

            // Read magic numbers
            while ((line = reader.readLine()) != null) {
                if (line.equals("MAGIC_END")) break;
                magicNumbers[index++] = Long.parseLong(line);
            }

            // Read attack table
            int row = 0;
            while ((line = reader.readLine()) != null && row < 64) {
                String[] parts = line.split(",");
                attackTable[row] = new long[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    if (!parts[i].isEmpty()) {
                        attackTable[row][i] = Long.parseLong(parts[i]);
                    }
                }
                row++;
            }

            return new MagicData(magicNumbers, attackTable);
        }
        catch(Exception e) {
        	System.out.println("chatgpt code aint even running "+ e);
        	return null;
        }
    }
}