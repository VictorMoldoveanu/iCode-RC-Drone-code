public class TTEntry {
    public static final int EXACT = 0;
    public static final int LOWER_BOUND = 1;
    public static final int UPPER_BOUND = 2;
    public static final int MATE = 3;

    public long zobristKey; // for collision resolution
    public int score;
    public int depth;
    public int bound; // use constants above
    public int bestMove;

    public TTEntry(long zobristKey, int score, int depth, int bound, int bestMove) {
        this.zobristKey = zobristKey;
        this.score = score;
        this.depth = depth;
        this.bound = bound;
        this.bestMove = bestMove;
    }
    
    public TTEntry() {
        this.zobristKey = 0;
        this.score = 0;
        this.depth = -1; // negative depth = unused slot
        this.bound = EXACT;
        this.bestMove = 0;
    }
}