import java.io.IOException;
import java.util.*;
public class magicNumbers{
    public static void main(String[] args) throws IOException{
    		
			long[] ROOK_BLOCKER_MASKS = new long[64];
			generateRookBlockerMasks(ROOK_BLOCKER_MASKS);
			
			long[] BISHOP_BLOCKER_MASKS = new long[64];
			generateBishopBlockerMasks(BISHOP_BLOCKER_MASKS);
			
			BoardState.initializeZobrist();
			 
//			long[] QUEEN_BLOCKER_MASKS = new long[64];
//			generateQueenBlockerMasks(QUEEN_BLOCKER_MASKS, ROOK_BLOCKER_MASKS, BISHOP_BLOCKER_MASKS);
			
			MagicData magicRookData = MagicLoader.loadFromText("magic_rook_data.txt");
			long[] magicRookNumbers = magicRookData.magicNumbers;
			long[][] rookAttackTable = magicRookData.attackTable;
			
			MagicData magicBishopData = MagicLoader.loadFromText("magic_bishop_data.txt");
			long[] magicBishopNumbers = magicBishopData.magicNumbers;
			long[][] bishopAttackTable = magicBishopData.attackTable;
			
			BoardState baseBoardState = new BoardState(
				71776119061217280L,
				4755801206503243776L,
				2594073385365405696L,
				-9151314442816847872L,
				576460752303423488L,
				1152921504606846976L,
				65280L,
				66L,
				36L,
				129L,
				8L,
				16L,
				-281474976710656L,
				65535L,
				-281474976645121L,
				15,
				-1,
				true,
				0
			);
			
			long whiteRook = (1L << 44);
			long whiteKing = (1L << 52);
			long blackKing = (1L << 0);
			long blackQueen = (1L << 4);
			
			long whitePieces = whiteKing | whiteRook;
			long blackPieces = blackQueen | blackKing;
			long allPieces = whitePieces | blackPieces;
			
			BoardState test = new BoardState(
				0L,
				0L,
				0L,
				whiteRook,
				0L,
				whiteKing,
				0L,
				0L,
				0L,
				0L,
				blackQueen,
				blackKing,
				whitePieces,
				blackPieces,
				allPieces,
				0,
				-1,
				true,
				22
			);

//			printBitBoard(0x60L);
//			printBitBoard(0x70L);
//			System.out.println("hi");

//			List<BoardState> nextState = new ArrayList<>();
//			nextState = BoardState.generateLegalMoves(baseBoardState);
//			
//			for (int i = 0; i < nextState.size(); i++) {
//				
//				List<BoardState> nextNextState = new ArrayList<>();
//				nextNextState = BoardState.generateLegalMoves(nextState.get(i));
//				
//				
//				for (int j = 0; j < nextNextState.size(); j++) {
//					nextNextState.get(j).printBoard();
//				}
//			}
//			
			long start = System.nanoTime();
			System.out.println(searchChess(baseBoardState, 4));
	        long end = System.nanoTime();
	        System.out.printf("Time taken: %.3f ms%n", (end - start) / 1_000_000.0);
			
			
//			baseBoardState.printBoard();
//			System.out.println(baseBoardState.zobristHash);
			
//	        long blocker = 0L;
//	        blocker |= (1L << 18);
//	        printBitBoard(blocker);
//	
//	        long attack = bishopAttackTable[63][(int)((blocker * magicBishopNumbers[63]) >>> (64 - Integer.numberOfTrailingZeros(bishopAttackTable[63].length)))];
//	        printBitBoard(attack);
    }
    public static int searchChess(BoardState board, int depth) {
    	List<BoardState> nextStates = BoardState.generateLegalMoves(board);
    	if (depth == 1)
    		return nextStates.size();
    	else {
    		int sum = 0;
    		for (int i = 0; i < nextStates.size(); i++) {
    			sum += searchChess(nextStates.get(i), depth-1);
    		}
    		return sum;
    	}
    }
    
    public static void generateRookBlockerMasks(long[] ROOK_BLOCKER_MASKS){
        for (int pos = 0; pos < 64; pos++) {
            int row = pos / 8;
            int col = pos % 8;
            long mask = 0L;

            // NORTH (exclude edge)
            for (int r = row - 1; r > 0; r--) mask |= (1L << (r * 8 + col));
            // SOUTH
            for (int r = row + 1; r < 7; r++) mask |= (1L << (r * 8 + col));
            // WEST
            for (int c = col - 1; c > 0; c--) mask |= (1L << (row * 8 + c));
            // EAST
            for (int c = col + 1; c < 7; c++) mask |= (1L << (row * 8 + c));

            ROOK_BLOCKER_MASKS[pos] = mask;
        }
    }
    
    public static void generateBishopBlockerMasks(long[] BISHOP_BLOCKER_MASKS) {
        for (int pos = 0; pos < 64; pos++) {
            int row = pos / 8;
            int col = pos % 8;
            long mask = 0L;

            // NORTH-EAST
            for (int r = row - 1, c = col + 1; r > 0 && c < 7; r--, c++) {
                mask |= (1L << (r * 8 + c));
            }
            // NORTH-WEST
            for (int r = row - 1, c = col - 1; r > 0 && c > 0; r--, c--) {
                mask |= (1L << (r * 8 + c));
            }
            // SOUTH-EAST
            for (int r = row + 1, c = col + 1; r < 7 && c < 7; r++, c++) {
                mask |= (1L << (r * 8 + c));
            }
            // SOUTH-WEST
            for (int r = row + 1, c = col - 1; r < 7 && c > 0; r++, c--) {
                mask |= (1L << (r * 8 + c));
            }

            BISHOP_BLOCKER_MASKS[pos] = mask;
        }
    }
    
    public static void generateQueenBlockerMasks(
            long[] QUEEN_BLOCKER_MASKS,
            long[] ROOK_BLOCKER_MASKS,
            long[] BISHOP_BLOCKER_MASKS) {

        for (int pos = 0; pos < 64; pos++) {
            QUEEN_BLOCKER_MASKS[pos] =
                ROOK_BLOCKER_MASKS[pos] | BISHOP_BLOCKER_MASKS[pos];
        }
    }
    
    public static void generateMagicNumbersAndAttackTable(long[][] attackTable, long[] magicNumbers, ArrayList<Pair>[] knownPairTable){
        for (int pos = 0; pos < 64; pos++){
            long magicNumber = 0L;
            int permutations = knownPairTable[pos].size();
            int shift = 64-Integer.numberOfTrailingZeros(permutations);
            
            outer:
            while (true){
                Random random = new Random();
                magicNumber = random.nextLong() & random.nextLong() & random.nextLong();
                
                HashSet<Long> seenIndexes = new HashSet<>();
                for (int i = 0; i < permutations; i++){
                    // System.out.println(i);
                    if (!seenIndexes.add((knownPairTable[pos].get(i).blocker * magicNumber) >>> shift)){
                        continue outer;
                    }
                }
                // System.out.println("MAGIC NUMBER FOUND for pos "+pos+ " it is "+magicNumber);
                magicNumbers[pos] = magicNumber;
                break;
            }
            attackTable[pos] = new long[permutations];
            for (int i = 0; i < permutations; i++){
                long index = ((knownPairTable[pos].get(i).blocker * magicNumber) >>> shift);
                attackTable[pos][(int)index] = knownPairTable[pos].get(i).attack;
            }
        }
    }
    
    public static void generateBlockerPermutations(ArrayList<Pair>[] knownPairTable){
        for (int pos = 0; pos < 64; pos++){
            int row = pos/8;
            int col = pos%8;
            
            long relevantBlockerSquares = generateNorthLineAttack(row,col,0L) | generateEastLineAttack(row,col,0L) | generateSouthLineAttack(row,col,0L) | generateWestLineAttack(row,col,0L);
            int relevantBlockerSquaresCount = Long.bitCount(relevantBlockerSquares);
            int relevantBlockerSquaresPermutations = 1 << relevantBlockerSquaresCount;
            
            ArrayList<Integer> relevantIndices = new ArrayList<>();
            for (int i = 0; i < 64; i++) {
                if (((relevantBlockerSquares >>> i) & 1L) != 0) {
                    relevantIndices.add(i);
                }
            }
            

            for (int i = 0; i < relevantBlockerSquaresPermutations; i++){
                long blocker = 0L;
                for (int j = 0; j < relevantIndices.size(); j++){
                    if ((i & (1 << j)) != 0){
                        blocker |= (1L << relevantIndices.get(j));
                    }
                }
                
                long attack = 
                OGgenerateNorthLineAttack(row,col,blocker) |
                OGgenerateEastLineAttack(row,col,blocker) |
                OGgenerateSouthLineAttack(row,col,blocker) |
                OGgenerateWestLineAttack(row,col,blocker);
                
                knownPairTable[pos].add(new Pair(blocker,attack));
            }
        }
    }
    
    private static long generateNorthLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (row-1 > 0){
            row--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long generateNorthEastLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row-1 > 0) && (col+1 < 7)){
            row--;
            col++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long generateEastLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (col+1 < 7){
            col++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long generateSouthEastLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row+1 < 7) && (col+1 < 7)){
            row++;
            col++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long generateSouthLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (row+1 < 7){
            row++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long generateSouthWestLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row+1 < 7) && (col-1 > 0)){
            row++;
            col--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long generateWestLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (col-1 > 0){
            col--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long generateNorthWestLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row-1 > 0) && (col-1 > 0)){
            row--;
            col--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    
    private static long OGgenerateNorthLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (row-1 > 0){
            row--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long OGgenerateNorthEastLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row-1 >= 0) && (col+1 <= 7)){
            row--;
            col++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long OGgenerateEastLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (col+1 <= 7){
            col++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long OGgenerateSouthEastLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row+1 <= 7) && (col+1 <= 7)){
            row++;
            col++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long OGgenerateSouthLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (row+1 <= 7){
            row++;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long OGgenerateSouthWestLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row+1 <= 7) && (col-1 >= 0)){
            row++;
            col--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long OGgenerateWestLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while (col-1 >= 0){
            col--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    private static long OGgenerateNorthWestLineAttack(int row, int col, long allPieces){
        long attackSquares = 0L;
        while ((row-1 >= 0) && (col-1 >= 0)){
            row--;
            col--;
            attackSquares |= (1L << (row*8+col));
            if ((attackSquares & allPieces) != 0)
                break;
        }
        return attackSquares;
    }
    
    public static void printBitBoard(long bitBoard){
        System.out.println("  _________________________________");
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (j == 0)
                    System.out.print(8-i + " ");
                if ((bitBoard & (1L << (i*8+j))) != 0){
                    System.out.print("| 1 ");
                }
                else{
                    System.out.print("| 0 ");
                }
                if (j == 7){
                    System.out.println("|");
                    System.out.println("  _________________________________");
                }
            }
        }
        System.out.println("    a   b   c   d   e   f   g   h");
    }

}