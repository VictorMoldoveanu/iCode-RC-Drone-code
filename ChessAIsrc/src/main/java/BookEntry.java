import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookEntry {
	private static final int ENTRY_SIZE = 16;
    private static final String BOOK_FILE_PATH = "komodo.bin";
    private static Random random = new Random();
	
	private int move;
	private int weight;
	
	public BookEntry(int move, int weight) {
        this.move = move;
        this.weight = weight;
    }
	
	private static BookEntry readEntryAt(RandomAccessFile file, long index) throws IOException {
        long pos = index * ENTRY_SIZE;
        file.seek(pos);

        long key = file.readLong(); // 8 bytes key (big endian)
        int move = file.readUnsignedShort(); // 2 bytes move
        int weight = file.readUnsignedShort(); // 2 bytes weight
        file.readInt(); // skip learn field (4 bytes)

        return new BookEntry(move, weight);
    }
	
	private static long readKeyAt(RandomAccessFile file, long index) throws IOException {
        long pos = index * ENTRY_SIZE;
        file.seek(pos);
        return file.readLong();
    }
	
	private static long binarySearch(RandomAccessFile file, long key, long totalEntries) throws IOException {
        long left = 0;
        long right = totalEntries - 1;
        while (left <= right) {
            long mid = (left + right) >>> 1;
            long midKey = readKeyAt(file, mid);
            if (midKey == key) {
                return mid;
            } else if (Long.compareUnsigned(midKey, key) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // not found
    }
	
	public static int openingBookMove(long currZobristHash) {
        try (RandomAccessFile file = new RandomAccessFile(BOOK_FILE_PATH, "r")) {
            long totalEntries = file.length() / ENTRY_SIZE;
            long index = binarySearch(file, currZobristHash, totalEntries);
            if (index == -1) {
                return -1; // no book move found
            }

            // Collect all entries with the same key:
            List<BookEntry> moves = new ArrayList<>();

            // Search backward from index:
            long i = index;
            while (i >= 0) {
                long key = readKeyAt(file, i);
                if (key != currZobristHash) break;
                BookEntry e = readEntryAt(file, i);
                moves.add(0, e); // prepend
                i--;
            }

            // Search forward from index+1:
            i = index + 1;
            while (i < totalEntries) {
                long key = readKeyAt(file, i);
                if (key != currZobristHash) break;
                BookEntry e = readEntryAt(file, i);
                moves.add(e);
                i++;
            }

            // Now pick one randomly weighted by weight:
            int totalWeight = 0;
            for (BookEntry e : moves) totalWeight += e.weight;
            if (totalWeight == 0) return -1;

            int r = random.nextInt(totalWeight);
            int runningSum = 0;
            for (BookEntry e : moves) {
                runningSum += e.weight;
                if (r < runningSum) {
                    return e.move;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
