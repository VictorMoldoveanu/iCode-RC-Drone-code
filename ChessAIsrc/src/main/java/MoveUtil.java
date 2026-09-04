public class MoveUtil {
	// move flag
    public static final int DOUBLE_PAWN_PUSH = 1;
    public static final int KING_CASTLE = 2;
    public static final int QUEEN_CASTLE = 3;
    public static final int EN_PASSANT = 5;
    public static final int PROMOTE_Q = 8;
    public static final int PROMOTE_R = 9;
    public static final int PROMOTE_B = 10;
    public static final int PROMOTE_N = 11;
    
    // capture flag
    public static final int NO_CAPTURE = 0;
    public static final int PAWN_CAPTURE = 1;
    public static final int KNIGHT_CAPTURE = 2;
    public static final int BISHOP_CAPTURE = 3;
    public static final int ROOK_CAPTURE = 4;
    public static final int QUEEN_CAPTURE = 5;
    
    public static int encodeMove(int from, int to, int flag, int captureFlag, int score) {
        return (score << 19) | (captureFlag << 16) | (flag << 12) | (to << 6) | from;
    }

    public static int encodeMove(int from, int to, int flag, int captureFlag) {
        return encodeMove(from, to, flag, captureFlag, 0);
    }

    public static int getFromSquare(int move) {
        return move & 0x3F;
    }

    public static int getToSquare(int move) {
        return (move >>> 6) & 0x3F;
    }

    public static int getFlags(int move) {
        return (move >>> 12) & 0xF;
    }
    
    public static int getCaptureFlags(int move) {
    	return (move >>> 16) & 0x7;
    }

    public static int getScore(int move) {
    	int raw = (move >>> 19) & 0x1FFF;
        // If bit 12 is set, it's negative
        if ((raw & 0x1000) != 0) {
            return raw | ~0x1FFF; // Sign-extend to 32 bits
        }
        return raw;
    }
}
