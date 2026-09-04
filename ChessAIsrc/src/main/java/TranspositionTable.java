public class TranspositionTable {
    private static final int TABLE_SIZE = 1 << 22; // 2^22 = 4,194,304 entries
    private final TTEntry[] table = new TTEntry[TABLE_SIZE];

    public TranspositionTable() {
        for (int i = 0; i < TABLE_SIZE; i++) {
            table[i] = new TTEntry(); // pre-allocate to avoid null checks
        }
    }

    public void store(long zobristKey, int score, int depth, int bound, int bestMove) {
        int index = (int)(zobristKey & (TABLE_SIZE - 1)); // fast mod via mask
        TTEntry entry = table[index];

        // Replace only if deeper or unused
        if (entry.depth < depth || entry.zobristKey == 0 && entry.bound != TTEntry.MATE) {
            entry.zobristKey = zobristKey;
            entry.score = score;
            entry.depth = depth;
            entry.bound = bound;
            entry.bestMove = bestMove;
        }
    }

    public TTEntry retrieve(long zobristKey) {
        int index = (int)(zobristKey & (TABLE_SIZE - 1));
        TTEntry entry = table[index];
        return (entry.zobristKey == zobristKey) ? entry : null;
    }
}