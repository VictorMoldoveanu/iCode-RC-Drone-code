import java.util.Arrays;
public class Engine {
	private static long startTime = 0L;
	private static long TIME_LIMIT_NS = 0L;
	public static long remainingTime = 600_000_000_000L;
	public static long increment = 0;
	private static int lastBestEval = 0;
	public static int bestMove = -1;
	public static final int[][] moves = new int[64][218];
	public static final BoardMetaData[] metaData = new BoardMetaData[64];
	public static boolean go = false;
	private static TranspositionTable tt = new TranspositionTable();
	
//	public static boolean playingForWhite = true;
	
//	public static long staticEvalCount = 0;
	
	// fen:  rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
	public static final BoardState baseBoardState = new BoardState(
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
		true
	);
	
	// fen:  r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 
	private static final BoardState position2 = new BoardState(
		(1L << 48) | (1L << 49) | (1L << 50) | (1L << 27) | (1L << 36) | (1L << 53) | (1L << 54) | (1L << 55), //WP
		(1L << 42) | (1L << 28), //WN
		(1L << 51) | (1L << 52), //WB
		(1L << 56) | (1L << 63), //WR
		(1L << 45), //WQ
		(1L << 60), //WK
		(1L << 8) | (1L << 33) | (1L << 10) | (1L << 11) | (1L << 20) | (1L << 13) | (1L << 22) | (1L << 47), //BP
		(1L << 17) | (1L << 21), //BN
		(1L << 16) | (1L << 14), //BB
		(1L << 0) | (1L << 7), //BR
		(1L << 12), //BQ
		(1L << 4), //BK
		(1L << 48) | (1L << 49) | (1L << 50) | (1L << 27) | (1L << 36) | (1L << 53) | (1L << 54) | (1L << 55)|(1L << 42) | (1L << 28)|(1L << 51) | (1L << 52)|(1L << 56) | (1L << 63)|(1L << 45)|(1L << 60), //whitePieces
		(1L << 8) | (1L << 33) | (1L << 10) | (1L << 11) | (1L << 20) | (1L << 13) | (1L << 22) | (1L << 47)|(1L << 17) | (1L << 21)|(1L << 16) | (1L << 14)|(1L << 0) | (1L << 7)|(1L << 12)|(1L << 4), //blackPieces
		(1L << 48) | (1L << 49) | (1L << 50) | (1L << 27) | (1L << 36) | (1L << 53) | (1L << 54) | (1L << 55)|(1L << 42) | (1L << 28)|(1L << 51) | (1L << 52)|(1L << 56) | (1L << 63)|(1L << 45)|(1L << 60)|(1L << 8) | (1L << 33) | (1L << 10) | (1L << 11) | (1L << 20) | (1L << 13) | (1L << 22) | (1L << 47)|(1L << 17) | (1L << 21)|(1L << 16) | (1L << 14)|(1L << 0) | (1L << 7)|(1L << 12)|(1L << 4), //allPieces
		15, //castlingRights
		-1, //enPassantFile
		true //whiteToMove
	);
	
	// fen:  8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1
	private static final BoardState position3 = new BoardState(
		(1L << 25) | (1L << 52) | (1L << 54), //WP
		0L, //WN
		0L, //WB
		(1L << 33), //WR
		0L, //WQ
		(1L << 24), //WK
		(1L << 10) | (1L << 19) | (1L << 37), //BP
		0L, //BN
		0L, //BB
		(1L << 31), //BR
		0L, //BQ
		(1L << 39), //BK
		(1L << 24)|(1L << 33)|(1L << 25) | (1L << 52) | (1L << 54), //whitePieces
		(1L << 10) | (1L << 19) | (1L << 37)|(1L << 31)|(1L << 39), //blackPieces
		(1L << 24)|(1L << 33)|(1L << 25) | (1L << 52) | (1L << 54)|(1L << 10) | (1L << 19) | (1L << 37)|(1L << 31)|(1L << 39), //allPieces
		0, //castlingRights
		-1, //enPassantFile
		true //whiteToMove
	);
	
	// fen:  r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1
	private static final BoardState position4 = new BoardState(
		(1L << 8) | (1L << 25) | (1L << 34) | (1L << 51) | (1L << 36) | (1L << 48) | (1L << 54) | (1L << 55), //WP
		(1L << 45) | (1L << 23), //WN
		(1L << 32) | (1L << 33), //WB
		(1L << 56) | (1L << 61), //WR
		(1L << 59), //WQ
		(1L << 62), //WK
		(1L << 9) | (1L << 10) | (1L << 11) | (1L << 13) | (1L << 14) | (1L << 15) | (1L << 49), //BP
		(1L << 24) | (1L << 21), //BN
		(1L << 17) | (1L << 22), //BB
		(1L << 0) | (1L << 7), //BR
		(1L << 40), //BQ
		(1L << 4), //BK
		(1L << 8) | (1L << 25) | (1L << 34) | (1L << 51) | (1L << 36) | (1L << 48) | (1L << 54) | (1L << 55)|(1L << 45) | (1L << 23)|(1L << 32) | (1L << 33)|(1L << 56) | (1L << 61)|(1L << 59)|(1L << 62), //whitePieces
		(1L << 9) | (1L << 10) | (1L << 11) | (1L << 13) | (1L << 14) | (1L << 15) | (1L << 49)|(1L << 24) | (1L << 21)|(1L << 17) | (1L << 22)|(1L << 0) | (1L << 7)|(1L << 40)|(1L << 4), //blackPieces
		(1L << 8) | (1L << 25) | (1L << 34) | (1L << 51) | (1L << 36) | (1L << 48) | (1L << 54) | (1L << 55)|(1L << 45) | (1L << 23)|(1L << 32) | (1L << 33)|(1L << 56) | (1L << 61)|(1L << 59)|(1L << 62)|(1L << 9) | (1L << 10) | (1L << 11) | (1L << 13) | (1L << 14) | (1L << 15) | (1L << 49)|(1L << 24) | (1L << 21)|(1L << 17) | (1L << 22)|(1L << 0) | (1L << 7)|(1L << 40)|(1L << 4), //allPieces
		3, //castlingRights
		-1, //enPassantFile
		true //whiteToMove
	);
	
	// fen:  rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8
	private static final BoardState position5 = new BoardState(
		(1L << 48) | (1L << 49) | (1L << 50) | (1L << 11) | (1L << 54) | (1L << 55), //WP
		(1L << 57) | (1L << 52), //WN
		(1L << 58) | (1L << 34), //WB
		(1L << 56) | (1L << 63), //WR
		(1L << 59), //WQ
		(1L << 60), //WK
		(1L << 8) | (1L << 9) | (1L << 18) | (1L << 13) | (1L << 14) | (1L << 15), //BP
		(1L << 1) | (1L << 53), //BN
		(1L << 2) | (1L << 12), //BB
		(1L << 0) | (1L << 7), //BR
		(1L << 3), //BQ
		(1L << 5), //BK
		(1L << 48) | (1L << 49) | (1L << 50) | (1L << 11) | (1L << 54) | (1L << 55)|(1L << 57) | (1L << 52)|(1L << 58) | (1L << 34)|(1L << 56) | (1L << 63)|(1L << 59)|(1L << 60), //whitePieces
		(1L << 8) | (1L << 9) | (1L << 18) | (1L << 13) | (1L << 14) | (1L << 15)|(1L << 1) | (1L << 53)|(1L << 2) | (1L << 12)|(1L << 0) | (1L << 7)|(1L << 3)|(1L << 5), //blackPieces
		(1L << 48) | (1L << 49) | (1L << 50) | (1L << 11) | (1L << 54) | (1L << 55)|(1L << 57) | (1L << 52)|(1L << 58) | (1L << 34)|(1L << 56) | (1L << 63)|(1L << 59)|(1L << 60)|(1L << 8) | (1L << 9) | (1L << 18) | (1L << 13) | (1L << 14) | (1L << 15)|(1L << 1) | (1L << 53)|(1L << 2) | (1L << 12)|(1L << 0) | (1L << 7)|(1L << 3)|(1L << 5), //allPieces
		12, //castlingRights
		-1, //enPassantFile
		true //whiteToMove
	);
	
	
	
	// fen:  n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - - 0 1
	private static final BoardState position6 = new BoardState(
		(1L << 8) | (1L << 9) | (1L << 10), //WP
		(1L << 61) | (1L << 63), //WN
		0L, //WB
		0L, //WR
		0L, //WQ
		(1L << 52), //WK
		(1L << 53) | (1L << 54) | (1L << 55), //BP
		(1L << 0) | (1L << 2), //BN
		0L, //BB
		0L, //BR
		0L, //BQ
		(1L << 11), //BK
		(1L << 8) | (1L << 9) | (1L << 10)|(1L << 61) | (1L << 63)|(1L << 52), //whitePieces
		(1L << 53) | (1L << 54) | (1L << 55)|(1L << 0) | (1L << 2)|(1L << 11), //blackPieces
		(1L << 8) | (1L << 9) | (1L << 10)|(1L << 61) | (1L << 63)|(1L << 52)|(1L << 53) | (1L << 54) | (1L << 55)|(1L << 0) | (1L << 2)|(1L << 11), //allPieces
		0, //castlingRights
		-1, //enPassantFile
		false //whiteToMove
	);
	
	// fen:  2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1
	private static final BoardState position7 = new BoardState(
		(1L << 12), //WP
		0L, //WN
		0L, //WB
		0L, //WR
		0L, //WQ
		(1L << 2), //WK
		0L, //BP
		0L, //BN
		0L, //BB
		(1L << 5), //BR
		0L, //BQ
		(1L << 59), //BK
		(1L << 12)|(1L << 2), //whitePieces
		(1L << 5)|(1L << 59), //blackPieces
		(1L << 12)|(1L << 2)|(1L << 5)|(1L << 59), //allPieces
		0, //castlingRights
		-1, //enPassantFile
		true //whiteToMove
	);
	
	// fen:  8/k1P5/8/1K6/8/8/8/8 w - - 0 1
	private static final BoardState position8 = new BoardState(
		(1L << 10), //WP
		0L, //WN
		0L, //WB
		0L, //WR
		0L, //WQ
		(1L << 25), //WK
		0L, //BP
		0L, //BN
		0L, //BB
		0L, //BR
		0L, //BQ
		(1L << 8), //BK
		(1L << 10)|(1L << 25), //whitePieces
		(1L << 8), //blackPieces
		(1L << 10)|(1L << 25)|(1L << 8), //allPieces
		0, //castlingRights
		-1, //enPassantFile
		true //whiteToMove
	);
	
	private static final BoardState tylerPos = new BoardState(
		(1L << 48) | (1L << 49) | (1L << 50) | (1L << 53) | (1L << 54) | (1L << 55), //WP
		(1L << 11) | (1L << 45), //WN
		0L, //WB
		(1L << 56) | (1L << 60), //WR
		0L, //WQ
		(1L << 62), //WK
		(1L << 8) | (1L << 9) | (1L << 13) | (1L << 15), //BP
		0L, //BN
		(1L << 19), //BB
		(1L << 0) | (1L << 5), //BR
		0L, //BQ
		(1L << 7), //BK
		(1L << 62)|(1L << 56) | (1L << 60)|(1L << 11) | (1L << 45)|(1L << 48) | (1L << 49) | (1L << 50) | (1L << 53) | (1L << 54) | (1L << 55), //whitePieces
		(1L << 7)|(1L << 0) | (1L << 5)|(1L << 19)|(1L << 8) | (1L << 9) | (1L << 13) | (1L << 15), //blackPieces
		(1L << 62)|(1L << 56) | (1L << 60)|(1L << 11) | (1L << 45)|(1L << 48) | (1L << 49) | (1L << 50) | (1L << 53) | (1L << 54) | (1L << 55)|(1L << 7)|(1L << 0) | (1L << 5)|(1L << 19)|(1L << 8) | (1L << 9) | (1L << 13) | (1L << 15), //allPieces
		0, //castlingRights
		-1, //enPassantFile
		false //whiteToMove
	);
	// fen:  r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1
	private static final BoardState position9 = new BoardState(
		0L, //WP
		0L, //WN
		0L, //WB
		(1L << 56) | (1L << 63), //WR
		(1L << 19), //WQ
		(1L << 60), //WK
		0L, //BP
		0L, //BN
		0L, //BB
		(1L << 0) | (1L << 7), //BR
		(1L << 45), //BQ
		(1L << 4), //BK
		(1L << 56) | (1L << 63)|(1L << 19)|(1L << 60), //whitePieces
		(1L << 0) | (1L << 7)|(1L << 45)|(1L << 4), //blackPieces
		(1L << 56) | (1L << 63)|(1L << 19)|(1L << 60)|(1L << 0) | (1L << 7)|(1L << 45)|(1L << 4), //allPieces
		15, //castlingRights
		-1, //enPassantFile
		false //whiteToMove
	);
	
//	BoardState.loadFEN("3r2k1/2p2p2/p6p/2bq2P1/6P1/8/P3R3/4K3 b - - 0 1");
//	BoardState.loadFEN("8/k7/3p4/p2P1p2/P2P1P2/8/8/K7 w - - 0 0");
	public static BoardState Board = baseBoardState;
	
	private static final int[] mg_pawn_table_white = {
	      0,   0,   0,   0,   0,   0,  0,   0,
	     98, 134,  61,  95,  68, 126, 34, -11,
	     -6,   7,  26,  31,  65,  56, 25, -20,
	    -14,  13,   6,  21,  23,  12, 17, -23,
	    -27,  -2,  -5,  12,  17,   6, 10, -25,
	    -26,  -4,  -4, -10,   3,   3, 33, -12,
	    -35,  -1, -20, -23, -15,  24, 38, -22,
	      0,   0,   0,   0,   0,   0,  0,   0,
	};

	private static final int[] eg_pawn_table_white = {
	      0,   0,   0,   0,   0,   0,   0,   0,
	    178, 173, 158, 134, 147, 132, 165, 187,
	     94, 100,  85,  67,  56,  53,  82,  84,
	     32,  24,  13,   5,  -2,   4,  17,  17,
	     13,   9,  -3,  -7,  -7,  -8,   3,  -1,
	      4,   7,  -6,   1,   0,  -5,  -1,  -8,
	     13,   8,   8,  10,  13,   0,   2,  -7,
	      0,   0,   0,   0,   0,   0,   0,   0,
	};

	private static final int[] mg_knight_table_white = {
	    -167, -89, -34, -49,  61, -97, -15, -107,
	     -73, -41,  72,  36,  23,  62,   7,  -17,
	     -47,  60,  37,  65,  84, 129,  73,   44,
	      -9,  17,  19,  53,  37,  69,  18,   22,
	     -13,   4,  16,  13,  28,  19,  21,   -8,
	     -23,  -9,  12,  10,  19,  17,  25,  -16,
	     -29, -53, -12,  -3,  -1,  18, -14,  -19,
	    -105, -21, -58, -33, -17, -28, -19,  -23,
	};

	private static final int[] eg_knight_table_white = {
	    -58, -38, -13, -28, -31, -27, -63, -99,
	    -25,  -8, -25,  -2,  -9, -25, -24, -52,
	    -24, -20,  10,   9,  -1,  -9, -19, -41,
	    -17,   3,  22,  22,  22,  11,   8, -18,
	    -18,  -6,  16,  25,  16,  17,   4, -18,
	    -23,  -3,  -1,  15,  10,  -3, -20, -22,
	    -42, -20, -10,  -5,  -2, -20, -23, -44,
	    -29, -51, -23, -15, -22, -18, -50, -64,
	};

	private static final int[] mg_bishop_table_white = {
	    -29,   4, -82, -37, -25, -42,   7,  -8,
	    -26,  16, -18, -13,  30,  59,  18, -47,
	    -16,  37,  43,  40,  35,  50,  37,  -2,
	     -4,   5,  19,  50,  37,  37,   7,  -2,
	     -6,  13,  13,  26,  34,  12,  10,   4,
	      0,  15,  15,  15,  14,  27,  18,  10,
	      4,  15,  16,   0,   7,  21,  33,   1,
	    -33,  -3, -14, -21, -13, -12, -39, -21,
	};

	private static final int[] eg_bishop_table_white = {
	    -14, -21, -11,  -8, -7,  -9, -17, -24,
	     -8,  -4,   7, -12, -3, -13,  -4, -14,
	      2,  -8,   0,  -1, -2,   6,   0,   4,
	     -3,   9,  12,   9, 14,  10,   3,   2,
	     -6,   3,  13,  19,  7,  10,  -3,  -9,
	    -12,  -3,   8,  10, 13,   3,  -7, -15,
	    -14, -18,  -7,  -1,  4,  -9, -15, -27,
	    -23,  -9, -23,  -5, -9, -16,  -5, -17,
	};

	private static final int[] mg_rook_table_white = {
	     32,  42,  32,  51, 63,  9,  31,  43,
	     27,  32,  58,  62, 80, 67,  26,  44,
	     -5,  19,  26,  36, 17, 45,  61,  16,
	    -24, -11,   7,  26, 24, 35,  -8, -20,
	    -36, -26, -12,  -1,  9, -7,   6, -23,
	    -45, -25, -16, -17,  3,  0,  -5, -33,
	    -44, -16, -20,  -9, -1, 11,  -6, -71,
	    -19, -13,   1,  17, 16,  7, -37, -26,
	};

	private static final int[] eg_rook_table_white = {
	    13, 10, 18, 15, 12,  12,   8,   5,
	    11, 13, 13, 11, -3,   3,   8,   3,
	     7,  7,  7,  5,  4,  -3,  -5,  -3,
	     4,  3, 13,  1,  2,   1,  -1,   2,
	     3,  5,  8,  4, -5,  -6,  -8, -11,
	    -4,  0, -5, -1, -7, -12,  -8, -16,
	    -6, -6,  0,  2, -9,  -9, -11,  -3,
	    -9,  2,  3, -1, -5, -13,   4, -20,
	};

	private static final int[] mg_queen_table_white = {
	    -28,   0,  29,  12,  59,  44,  43,  45,
	    -24, -39,  -5,   1, -16,  57,  28,  54,
	    -13, -17,   7,   8,  29,  56,  47,  57,
	    -27, -27, -16, -16,  -1,  17,  -2,   1,
	     -9, -26,  -9, -10,  -2,  -4,   3,  -3,
	    -14,   2, -11,  -2,  -5,   2,  14,   5,
	    -35,  -8,  11,   2,   8,  15,  -3,   1,
	     -1, -18,  -9,  10, -15, -25, -31, -50,
	};

	private static final int[] eg_queen_table_white = {
	     -9,  22,  22,  27,  27,  19,  10,  20,
	    -17,  20,  32,  41,  58,  25,  30,   0,
	    -20,   6,   9,  49,  47,  35,  19,   9,
	      3,  22,  24,  45,  57,  40,  57,  36,
	    -18,  28,  19,  47,  31,  34,  39,  23,
	    -16, -27,  15,   6,   9,  17,  10,   5,
	    -22, -23, -30, -16, -16, -23, -36, -32,
	    -33, -28, -22, -43,  -5, -32, -20, -41,
	};

	private static final int[] mg_king_table_white = {
	    -65,  23,  16, -15, -56, -34,   2,  13,
	     29,  -1, -20,  -7,  -8,  -4, -38, -29,
	     -9,  24,   2, -16, -20,   6,  22, -22,
	    -17, -20, -12, -27, -30, -25, -14, -36,
	    -49,  -1, -27, -39, -46, -44, -33, -51,
	    -14, -14, -22, -46, -44, -30, -15, -27,
	      1,   7,  -8, -64, -43, -16,   9,   8,
	    -15,  36,  12, -54,   8, -28,  24,  14,
	};

	private static final int[] eg_king_table_white = {
	    -74, -35, -18, -18, -11,  15,   4, -17,
	    -12,  17,  14,  17,  17,  38,  23,  11,
	     10,  17,  23,  15,  20,  45,  44,  13,
	     -8,  22,  24,  27,  26,  33,  26,   3,
	    -18,  -4,  21,  24,  27,  23,   9, -11,
	    -19,  -3,  11,  21,  23,  16,   7,  -9,
	    -27, -11,   4,  13,  14,   4,  -5, -17,
	    -53, -34, -21, -11, -28, -14, -24, -43
	};
	
	private static final int[] mg_pawn_table_black = {
	     0,   0,   0,   0,   0,   0,  0,   0,
	   -35,  -1, -20, -23, -15,  24, 38, -22,
	   -26,  -4,  -4, -10,   3,   3, 33, -12,
	   -27,  -2,  -5,  12,  17,   6, 10, -25,
	   -14,  13,   6,  21,  23,  12, 17, -23,
	    -6,   7,  26,  31,  65,  56, 25, -20,
	    98, 134,  61,  95,  68, 126, 34, -11,
	     0,   0,   0,   0,   0,   0,  0,   0
	};

	private static final int[] eg_pawn_table_black = {
	     0,   0,   0,   0,   0,   0,   0,   0,
	    13,   8,   8,  10,  13,   0,   2,  -7,
	     4,   7,  -6,   1,   0,  -5,  -1,  -8,
	    13,   9,  -3,  -7,  -7,  -8,   3,  -1,
	    32,  24,  13,   5,  -2,   4,  17,  17,
	    94, 100,  85,  67,  56,  53,  82,  84,
	   178, 173, 158, 134, 147, 132, 165, 187,
	     0,   0,   0,   0,   0,   0,   0,   0
	};
	
	private static final int[] mg_knight_table_black = {
	  -105, -21, -58, -33, -17, -28, -19,  -23,
	   -29, -53, -12,  -3,  -1,  18, -14,  -19,
	   -23,  -9,  12,  10,  19,  17,  25,  -16,
	   -13,   4,  16,  13,  28,  19,  21,   -8,
	    -9,  17,  19,  53,  37,  69,  18,   22,
	   -47,  60,  37,  65,  84, 129,  73,   44,
	   -73, -41,  72,  36,  23,  62,   7,  -17,
	  -167, -89, -34, -49,  61, -97, -15, -107
	};

	private static final int[] eg_knight_table_black = {
	  -29, -51, -23, -15, -22, -18, -50, -64,
	  -42, -20, -10,  -5,  -2, -20, -23, -44,
	  -23,  -3,  -1,  15,  10,  -3, -20, -22,
	  -18,  -6,  16,  25,  16,  17,   4, -18,
	  -17,   3,  22,  22,  22,  11,   8, -18,
	  -24, -20,  10,   9,  -1,  -9, -19, -41,
	  -25,  -8, -25,  -2,  -9, -25, -24, -52,
	  -58, -38, -13, -28, -31, -27, -63, -99
	};
	
	private static final int[] mg_bishop_table_black = {
	   -33,  -3, -14, -21, -13, -12, -39, -21,
	     4,  15,  16,   0,   7,  21,  33,   1,
	     0,  15,  15,  15,  14,  27,  18,  10,
	    -6,  13,  13,  26,  34,  12,  10,   4,
	    -4,   5,  19,  50,  37,  37,   7,  -2,
	   -16,  37,  43,  40,  35,  50,  37,  -2,
	   -26,  16, -18, -13,  30,  59,  18, -47,
	   -29,   4, -82, -37, -25, -42,   7,  -8
	};

	private static final int[] eg_bishop_table_black = {
	  -23,  -9, -23,  -5, -9, -16,  -5, -17,
	  -14, -18,  -7,  -1,  4,  -9, -15, -27,
	  -12,  -3,   8,  10, 13,   3,  -7, -15,
	   -6,   3,  13,  19,  7,  10,  -3,  -9,
	   -3,   9,  12,   9, 14,  10,   3,   2,
	     2,  -8,   0,  -1, -2,   6,   0,   4,
	    -8,  -4,   7, -12, -3, -13,  -4, -14,
	   -14, -21, -11,  -8, -7,  -9, -17, -24
	};
	
	private static final int[] mg_rook_table_black = {
	   -19, -13,   1,  17, 16,  7, -37, -26,
	   -44, -16, -20,  -9, -1, 11,  -6, -71,
	   -45, -25, -16, -17,  3,  0,  -5, -33,
	   -36, -26, -12,  -1,  9, -7,   6, -23,
	   -24, -11,   7,  26, 24, 35,  -8, -20,
	    -5,  19,  26,  36, 17, 45,  61,  16,
	    27,  32,  58,  62, 80, 67,  26,  44,
	    32,  42,  32,  51, 63,  9,  31,  43
	};

	private static final int[] eg_rook_table_black = {
	   -9,  2,  3, -1, -5, -13,   4, -20,
	   -6, -6,  0,  2, -9,  -9, -11,  -3,
	   -4,  0, -5, -1, -7, -12,  -8, -16,
	    3,  5,  8,  4, -5,  -6,  -8, -11,
	    4,  3, 13,  1,  2,   1,  -1,   2,
	    7,  7,  7,  5,  4,  -3,  -5,  -3,
	   11, 13, 13, 11, -3,   3,   8,   3,
	   13, 10, 18, 15, 12,  12,   8,   5
	};
	
	private static final int[] mg_queen_table_black = {
	    -1, -18,  -9,  10, -15, -25, -31, -50,
	   -35,  -8,  11,   2,   8,  15,  -3,   1,
	   -14,   2, -11,  -2,  -5,   2,  14,   5,
	    -9, -26,  -9, -10,  -2,  -4,   3,  -3,
	   -27, -27, -16, -16,  -1,  17,  -2,   1,
	   -13, -17,   7,   8,  29,  56,  47,  57,
	   -24, -39,  -5,   1, -16,  57,  28,  54,
	   -28,   0,  29,  12,  59,  44,  43,  45
	};

	private static final int[] eg_queen_table_black = {
	  -33, -28, -22, -43,  -5, -32, -20, -41,
	  -22, -23, -30, -16, -16, -23, -36, -32,
	  -16, -27,  15,   6,   9,  17,  10,   5,
	  -18,  28,  19,  47,  31,  34,  39,  23,
	    3,  22,  24,  45,  57,  40,  57,  36,
	  -20,   6,   9,  49,  47,  35,  19,   9,
	  -17,  20,  32,  41,  58,  25,  30,   0,
	   -9,  22,  22,  27,  27,  19,  10,  20
	};
	
	private static final int[] mg_king_table_black = {
	   -15,  36,  12, -54,   8, -28,  24,  14,
	     1,   7,  -8, -64, -43, -16,   9,   8,
	   -14, -14, -22, -46, -44, -30, -15, -27,
	   -49,  -1, -27, -39, -46, -44, -33, -51,
	   -17, -20, -12, -27, -30, -25, -14, -36,
	    -9,  24,   2, -16, -20,   6,  22, -22,
	    29,  -1, -20,  -7,  -8,  -4, -38, -29,
	   -65,  23,  16, -15, -56, -34,   2,  13
	};

	private static final int[] eg_king_table_black = {
	  -53, -34, -21, -11, -28, -14, -24, -43,
	  -27, -11,   4,  13,  14,   4,  -5, -17,
	  -19,  -3,  11,  21,  23,  16,   7,  -9,
	  -18,  -4,  21,  24,  27,  23,   9, -11,
	   -8,  22,  24,  27,  26,  33,  26,   3,
	   10,  17,  23,  15,  20,  45,  44,  13,
	  -12,  17,  14,  17,  17,  38,  23,  11,
	  -74, -35, -18, -18, -11,  15,   4, -17
	};
	
	public static int findBestMove() {
		TIME_LIMIT_NS = (remainingTime / estimatePliesRemaining()) + (increment/2);
		
		startTime = System.nanoTime();
		
		int depth = 1;
		bestMove = -1;
		
		bestMove = Board.getBookMove();
		if (bestMove != -1)
			return bestMove;
		
		while (!timeExceeded()) {
			miniMaxRoot(depth);
			if ((lastBestEval == Integer.MIN_VALUE) || (lastBestEval == Integer.MAX_VALUE))
				return bestMove;
//			System.out.println("Annalyzed to a depth of "+depth+". Best Move: "+moveToString(bestMove));
			depth++;
		}		
		return bestMove;
	}
	
	private static void miniMaxRoot(int depth) {
		int localBestMove = 0;
		metaData[depth] = new BoardMetaData(Board.castlingRights, Board.enPassantFile);
		
		if (Board.whiteToMove) {
			int bestEval = Integer.MIN_VALUE;
			BoardState.generateLegalMoves(Board, moves[depth]);
			int localMoveCount = BoardState.moveCount;
			
			TTEntry entry = tt.retrieve(Board.zobristHash);
			if (entry != null && entry.depth < depth && entry.bestMove != 0) {
				for (int i = 0; i < localMoveCount; i++) {
					if (moves[depth][i] == entry.bestMove) {
						moves[depth][i] &= 0x7FFFF; // removes old score
						moves[depth][i] |= (3000 << 19); // set new score
						break;
					}
				}
			}
			
			// move ordering by MVV LVA
			sortMoves(moves[depth], localMoveCount);
			
			for (int i = 0; i < localMoveCount && !timeExceeded(); i++) {
				BoardState.applyMove(Board, moves[depth][i]);
				int eval = miniMax(depth-1, bestEval, Integer.MAX_VALUE, false);
				BoardState.undoMove(Board, moves[depth][i],metaData[depth]);
				if (eval > bestEval) {
					localBestMove = moves[depth][i];
					bestEval = eval;
				}
			}
			
			// only partial search completed
			if (timeExceeded()) {
				
			}
			//full search completed
			else {
				bestMove = localBestMove;
				lastBestEval = bestEval;
				tt.store(Board.zobristHash, bestEval, depth, TTEntry.EXACT, bestMove);
			}
		}
		else {
			int bestEval = Integer.MAX_VALUE;
			BoardState.generateLegalMoves(Board, moves[depth]);
			int localMoveCount = BoardState.moveCount;
		
			TTEntry entry = tt.retrieve(Board.zobristHash);
			if (entry != null && entry.depth < depth && entry.bestMove != 0) {
				for (int i = 0; i < localMoveCount; i++) {
					if (moves[depth][i] == entry.bestMove) {
						moves[depth][i] &= 0x7FFFF; // removes old score
						moves[depth][i] |= (3000 << 19); // set new score
						break;
					}
				}
			}
			
			// move ordering by MVV LVA
			sortMoves(moves[depth], localMoveCount);
			
			for (int i = 0; i < localMoveCount && !timeExceeded(); i++) {
				BoardState.applyMove(Board, moves[depth][i]);
				int eval = miniMax(depth-1, Integer.MIN_VALUE, bestEval, true);
				BoardState.undoMove(Board, moves[depth][i],metaData[depth]);
				if (eval < bestEval) {
					localBestMove = moves[depth][i];
					bestEval = eval;
				}
			}
			
			// only partial search completed
			if (timeExceeded()) {
				
			}
			//full search completed
			else {
				bestMove = localBestMove;
				lastBestEval = bestEval;
				tt.store(Board.zobristHash, bestEval, depth, TTEntry.EXACT, bestMove);
			}
		}
	}
	
	public static int miniMax(int depth, int alpha, int beta, boolean maximizingPlayer) {
		
		TTEntry entry = tt.retrieve(Board.zobristHash);
		if (entry != null && entry.depth >= depth) {
		    if (entry.bound == TTEntry.EXACT) {
		        return entry.score;
		    } else if (entry.bound == TTEntry.LOWER_BOUND && entry.score >= beta) {
		        return entry.score;
		    } else if (entry.bound == TTEntry.UPPER_BOUND && entry.score <= alpha) {
		        return entry.score;
		    }
		}
		int alphaOriginal = alpha;
		
		int checks = BoardState.generateLegalMoves(Board, moves[depth]);
		int localMoveCount = BoardState.moveCount;
		if (localMoveCount == 0) {
			if ( (checks != 0) && (Board.whiteToMove) ) {
				tt.store(Board.zobristHash, Integer.MIN_VALUE, depth, TTEntry.MATE, 0);
				return Integer.MIN_VALUE; // what could be worse than losing a chess game
			}
			else if ( (checks != 0) && (!Board.whiteToMove) ) {
				tt.store(Board.zobristHash, Integer.MAX_VALUE, depth, TTEntry.MATE, 0);
				return Integer.MAX_VALUE;
			}
			tt.store(Board.zobristHash, 0, depth, TTEntry.MATE, 0);
			return 0; // Stale-mate
		}
		if (Board.previousHashes.contains(Board.zobristHash)) {
			tt.store(Board.zobristHash, 0, depth, TTEntry.EXACT, 0);
			return 0; // on the way to three fold repetition
		}
		
		if (depth == 0) {
//			staticEvalCount++;
			int staticEval = staticEvaluation();
			tt.store(Board.zobristHash, staticEval, 0, TTEntry.EXACT, 0);
			return staticEval;
		}
		if (entry != null && entry.depth < depth && entry.bestMove != 0) {
			for (int i = 0; i < localMoveCount; i++) {
				if (moves[depth][i] == entry.bestMove) {
					moves[depth][i] &= 0x7FFFF; // removes old score
					moves[depth][i] |= (3000 << 19); // set new score
					break;
				}
			}
		}
		metaData[depth] = new BoardMetaData(Board.castlingRights, Board.enPassantFile);
		sortMoves(moves[depth], localMoveCount);
		int localBestMove = 0;
		
		int bestEval = Integer.MIN_VALUE;
		if (maximizingPlayer) {
			
			for (int i = 0; i < localMoveCount; i++) {
				BoardState.applyMove(Board, moves[depth][i]);
				int eval = miniMax(depth-1, alpha, beta, false);
				BoardState.undoMove(Board, moves[depth][i], metaData[depth]);
				
				if (eval > bestEval) {
					bestEval = eval;
					localBestMove = moves[depth][i];
				}
				alpha = Math.max(alpha, bestEval);
				if (alpha >= beta) {
					break;
				}
			}
			int bound;
			if (bestEval <= alphaOriginal) {
			    bound = TTEntry.UPPER_BOUND;
			} else if (bestEval >= beta) {
			    bound = TTEntry.LOWER_BOUND;
			} else {
			    bound = TTEntry.EXACT;
			}
			tt.store(Board.zobristHash, bestEval, depth, bound, localBestMove);

			return bestEval;
		}
		else {
			bestEval = Integer.MAX_VALUE;
			for (int i = 0; i < localMoveCount; i++) {
				BoardState.applyMove(Board, moves[depth][i]);
				int eval = miniMax(depth-1, alpha, beta, true);
				BoardState.undoMove(Board, moves[depth][i], metaData[depth]);
				
				if (eval < bestEval) {
					bestEval = eval;
					localBestMove = moves[depth][i];
				}
				beta = Math.min(beta, bestEval);
				if (alpha >= beta) {
					break;
				}
			}
			int bound;
			if (bestEval <= alphaOriginal) {
			    bound = TTEntry.UPPER_BOUND;
			} else if (bestEval >= beta) {
			    bound = TTEntry.LOWER_BOUND;
			} else {
			    bound = TTEntry.EXACT;
			}
			tt.store(Board.zobristHash, bestEval, depth, bound, localBestMove);
			
			return bestEval;
		}
	}
	
	public static boolean timeExceeded() {
	    return ((System.nanoTime() - startTime) > TIME_LIMIT_NS) || !go;
	}
	
	public static void sortMoves(int[] moves, int moveCount) {
		Arrays.sort(moves, 0, moveCount);
		for (int i = 0; i < moveCount / 2; i++) {
		    int temp = moves[i];
		    moves[i] = moves[moveCount - 1 - i];
		    moves[moveCount - 1 - i] = temp;
		}
	}
	
	public static void runPerftSuite() {
		BoardState temp = Engine.Board;
		
	    BoardState[] positions = {
	        Engine.Board, Engine.position2, Engine.position3,
	        Engine.position4, Engine.position5, Engine.position6,
	        Engine.position7, Engine.position8, Engine.position9
	    };

	    int[] depths = { 5, 4, 6, 5, 4, 5, 6, 7, 4 };
	    long[] expectedNodes = {
	        4865609L, 4085603L, 11030083L,
	        15833292L, 2103487L, 3605103L,
	        3821001L, 567584L, 1720476L
	    };

	    System.out.printf(
	        "%-10s %-8s %-15s %-15s %-10s %-6s%n",
	        "Position", "Depth", "Nodes", "Expected", "Time", "Result"
	    );
	    System.out.println("---------------------------------------------------------------------");

	    for (int i = 0; i < positions.length; i++) {
	        Engine.Board = positions[i];
	        long startTime = System.nanoTime();
	        long nodes = Engine.perft(depths[i]);
	        long endTime = System.nanoTime();
	        double timeSeconds = (endTime - startTime) / 1e9;

	        boolean correct = (nodes == expectedNodes[i]);
	        String status = correct ? "PASS ✅" : "FAIL ❌";

	        System.out.printf(
	            "%-10s %-8d %-15d %-15d %-10.2fs %-6s%n",
	            "Pos " + (i + 1), depths[i], nodes, expectedNodes[i], timeSeconds, status
	        );
	    }
	    
	    Engine.Board = temp;
	}

	
	public static long perftDebug(int depth) {
		Arrays.fill(moves[depth], 0);
		BoardState.generateLegalMoves(Board, moves[depth]);
        int localMoveCount = BoardState.moveCount;
        
//        Arrays.sort(moves[depth], 0, localMoveCount);
//		for (int i = 0; i < localMoveCount / 2; i++) {
//		    int temp = moves[depth][i];
//		    moves[depth][i] = moves[depth][localMoveCount - 1 - i];
//		    moves[depth][localMoveCount - 1 - i] = temp;
//		}
        
//        if (depth == 1)
//        	return localMoveCount;
        
        metaData[depth] = new BoardMetaData(Board.castlingRights, Board.enPassantFile);
        long subNodes = 0L;
        
        for (int i = 0; i < localMoveCount; i++) {
        	BoardState.applyMove(Board, moves[depth][i]);
        	long subNode = 1L;
        	if (depth != 1)
        		subNode = perft(depth-1);
        	System.out.println(moveToString(moves[depth][i]) +": "+ subNode + " index: " +i);
        	subNodes += subNode;
        	BoardState.undoMove(Board, moves[depth][i], metaData[depth]);
        }

        return subNodes;
	}
    
    // sorts by MVV LVA enable it if you want it to run 3x as slow
//    Arrays.sort(moves[depth], 0, localMoveCount);
//	for (int i = 0; i < localMoveCount / 2; i++) {
//	    int temp = moves[depth][i];
//	    moves[depth][i] = moves[depth][localMoveCount - 1 - i];
//	    moves[depth][localMoveCount - 1 - i] = temp;
//	}
	
	public static long perft(int depth) {
        BoardState.generateLegalMoves(Board, moves[depth]);
        int localMoveCount = BoardState.moveCount;

        if (depth == 1)
        	return localMoveCount;
        
        
        metaData[depth] = new BoardMetaData(Board.castlingRights, Board.enPassantFile);
        long subNodes = 0L;
        
        for (int i = 0; i < localMoveCount; i++) {
        	BoardState.applyMove(Board, moves[depth][i]);
        	subNodes += perft(depth-1);
        	BoardState.undoMove(Board, moves[depth][i], metaData[depth]);
        }

        return subNodes;
    }
	
	public static String moveToString(int move) {
		String stringMove = "";
	    int from = MoveUtil.getFromSquare(move);
	    int to = MoveUtil.getToSquare(move);
	    char fromFile = (char) ('a' + (from % 8));
	    int fromRank = 8 - (from / 8);
	    char toFile = (char) ('a' + (to % 8));
	    int toRank = 8 - (to / 8);
	    stringMove += stringMove + fromFile + fromRank + toFile + toRank;
		switch (MoveUtil.getFlags(move)) {
		    case MoveUtil.PROMOTE_Q -> stringMove += "q";
		    case MoveUtil.PROMOTE_R -> stringMove += "r";
		    case MoveUtil.PROMOTE_B -> stringMove += "b";
		    case MoveUtil.PROMOTE_N -> stringMove += "n";
		}
	    return stringMove;
	}
	
	public static int estimatePliesRemaining() {
	    // Piece counts from bitboards
	    int whiteKnightCount = Long.bitCount(Board.WN);
	    int blackKnightCount = Long.bitCount(Board.BN);
	    int whiteBishopCount = Long.bitCount(Board.WB);
	    int blackBishopCount = Long.bitCount(Board.BB);
	    int whiteRookCount   = Long.bitCount(Board.WR);
	    int blackRookCount   = Long.bitCount(Board.BR);
	    int whiteQueenCount  = Long.bitCount(Board.WQ);
	    int blackQueenCount  = Long.bitCount(Board.BQ);

	    // Calculate game phase (0 to 24)
	    int mgPhase =
	        whiteKnightCount + blackKnightCount +
	        whiteBishopCount + blackBishopCount +
	        2 * (whiteRookCount + blackRookCount) +
	        4 * (whiteQueenCount + blackQueenCount);

	    // Map phase to plies remaining (10 to 80)
	    int minPlies = 10;
	    int maxPlies = 80;

	    int pliesRemaining = minPlies + (mgPhase * (maxPlies - minPlies) / 24);

	    return pliesRemaining;
	}

	
	public static int staticEvaluation() {
		int whitePawnCount   = Long.bitCount(Board.WP);
		int whiteKnightCount = Long.bitCount(Board.WN);
		int whiteBishopCount = Long.bitCount(Board.WB);
		int whiteRookCount   = Long.bitCount(Board.WR);
		int whiteQueenCount  = Long.bitCount(Board.WQ);

		int blackPawnCount   = Long.bitCount(Board.BP);
		int blackKnightCount = Long.bitCount(Board.BN);
		int blackBishopCount = Long.bitCount(Board.BB);
		int blackRookCount   = Long.bitCount(Board.BR);
		int blackQueenCount  = Long.bitCount(Board.BQ);

		// game phase calculator
	    int mgPhase =
	    		whiteKnightCount
	            + blackKnightCount
	            + whiteBishopCount
	            + blackBishopCount
	            + 2 * (whiteRookCount + blackRookCount)
	            + 4 * (whiteQueenCount + blackQueenCount);
	    
	    if (mgPhase > 24)
	    	mgPhase = 24;
	    
	    int egPhase = 24-mgPhase;
	    
	    // eval for material
		int whiteEval =
		    whitePawnCount   * (82 * mgPhase + 94 * egPhase) +
		    whiteKnightCount * (337 * mgPhase + 281 * egPhase) +
		    whiteBishopCount * (365 * mgPhase + 297 * egPhase) +
		    whiteRookCount   * (477 * mgPhase + 512 * egPhase) +
		    whiteQueenCount  * (1025 * mgPhase + 936 * egPhase);
		
		int blackEval =
		    blackPawnCount   * (82 * mgPhase + 94 * egPhase) +
		    blackKnightCount * (337 * mgPhase + 281 * egPhase) +
		    blackBishopCount * (365 * mgPhase + 297 * egPhase) +
		    blackRookCount   * (477 * mgPhase + 512 * egPhase) +
		    blackQueenCount  * (1025 * mgPhase + 936 * egPhase);
		
		int eval = whiteEval - blackEval;
	    
	    // eval for piece square tables
	    long pawns = Board.WP;
	    while (pawns != 0) {
	        int pos = Long.numberOfTrailingZeros(pawns);
	        pawns &= pawns - 1;
	        eval += (mg_pawn_table_white[pos] * mgPhase) + (eg_pawn_table_white[pos] * egPhase);
	    }
	    
	    long knights = Board.WN;
	    while (knights != 0) {
	        int pos = Long.numberOfTrailingZeros(knights);
	        knights &= knights - 1;
	        eval += (mg_knight_table_white[pos] * mgPhase) + (eg_knight_table_white[pos] * egPhase);
	    }

	    long bishops = Board.WB;
	    while (bishops != 0) {
	        int pos = Long.numberOfTrailingZeros(bishops);
	        bishops &= bishops - 1;
	        eval += (mg_bishop_table_white[pos] * mgPhase) + (eg_bishop_table_white[pos] * egPhase);
	    }

	    long rooks = Board.WR;
	    while (rooks != 0) {
	        int pos = Long.numberOfTrailingZeros(rooks);
	        rooks &= rooks - 1;
	        eval += (mg_rook_table_white[pos] * mgPhase) + (eg_rook_table_white[pos] * egPhase);
	    }

	    long queens = Board.WQ;
	    while (queens != 0) {
	        int pos = Long.numberOfTrailingZeros(queens);
	        queens &= queens - 1;
	        eval += (mg_queen_table_white[pos] * mgPhase) + (eg_queen_table_white[pos] * egPhase);
	    }

        int kingPos = Long.numberOfTrailingZeros(Board.WK);
        eval += (mg_king_table_white[kingPos] * mgPhase) + (eg_king_table_white[kingPos] * egPhase);
        
        pawns = Board.BP;
        while (pawns != 0) {
            int pos = Long.numberOfTrailingZeros(pawns);
            pawns &= pawns - 1;
            eval -= (mg_pawn_table_black[pos] * mgPhase) + (eg_pawn_table_black[pos] * egPhase);
        }

        knights = Board.BN;
        while (knights != 0) {
            int pos = Long.numberOfTrailingZeros(knights);
            knights &= knights - 1;
            eval -= (mg_knight_table_black[pos] * mgPhase) + (eg_knight_table_black[pos] * egPhase);
        }

        bishops = Board.BB;
        while (bishops != 0) {
            int pos = Long.numberOfTrailingZeros(bishops);
            bishops &= bishops - 1;
            eval -= (mg_bishop_table_black[pos] * mgPhase) + (eg_bishop_table_black[pos] * egPhase);
        }

        rooks = Board.BR;
        while (rooks != 0) {
            int pos = Long.numberOfTrailingZeros(rooks);
            rooks &= rooks - 1;
            eval -= (mg_rook_table_black[pos] * mgPhase) + (eg_rook_table_black[pos] * egPhase);
        }

        queens = Board.BQ;
        while (queens != 0) {
            int pos = Long.numberOfTrailingZeros(queens);
            queens &= queens - 1;
            eval -= (mg_queen_table_black[pos] * mgPhase) + (eg_queen_table_black[pos] * egPhase);
        }

        kingPos = Long.numberOfTrailingZeros(Board.BK);
        eval -= (mg_king_table_black[kingPos] * mgPhase) + (eg_king_table_black[kingPos] * egPhase);
	            
		return eval;
	}
}