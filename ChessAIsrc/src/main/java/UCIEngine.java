import java.io.IOException;
import java.util.*;
public class UCIEngine{
    public static void main(String[] args) throws IOException{
    		
//			Engine.runPerftSuite();
//			Engine.Board.printBoard();
//			Engine.findBestMove();	
    	
//    	System.err.println("info string Zorya Engine: Starting up...");
    	Engine.runPerftSuite();
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
//        	Engine.Board.printBoard();
//        	System.out.println(Engine.Board.zobristHash);
//        	System.out.println(Engine.Board.previousHashes);
            String input = scanner.nextLine().trim();
            
            if (input.equals("uci")) {
//            	System.err.println("info string Starting uci");
                handleUci();
            } else if (input.equals("isready")) {
//            	System.err.println("info string Starting isready");
                handleIsReady();
            } else if (input.equals("ucinewgame")) {
//            	System.err.println("info string Starting ucinewgame");
                handleNewGame();
            } else if (input.startsWith("position")) {
//            	System.err.println("info string Starting position");
                handlePosition(input);
            } else if (input.startsWith("go")) {
//            	System.err.println("info string Starting go");
                handleGo(input);
            } else if (input.equals("stop")) {
//            	System.err.println("info string Starting stop");
                handleStop();
            } else if (input.equals("quit")) {
//            	System.err.println("info string Starting quit");
                handleQuit();
            } else if (input.startsWith("setoption")) {
//            	System.err.println("info string Starting setoption");
                handleSetOption(input);
            }
            
        }
//        System.err.println("info string Zorya Engine: ending down...");
        
        scanner.close();
			
    }
    private static void handleUci() {
    	System.out.println("id name Zorya");
        System.out.println("id author Victor");
        
        // Standard UCI options often expected by GUIs like lichess-bot:
        System.out.println("option name Hash type spin default 16 min 1 max 2048"); 
        System.out.println("option name Threads type spin default 1 min 1 max 8"); 
        System.out.println("option name Move Overhead type spin default 30 min 0 max 1000"); 
        System.out.println("option name SyzygyPath type string default <empty>"); 
        System.out.println("option name NalimovPath type string default <empty>"); // As you've added
        System.out.println("option name NalimovCache type spin default 1 min 1 max 1000"); // As you've added
        System.out.println("option name Ponder type check default false"); 
        System.out.println("option name UCI_LimitStrength type check default false");
        System.out.println("option name UCI_Elo type spin default 1350 min 1350 max 3000"); 
        System.out.println("option name MultiPV type spin default 1 min 1 max 5"); 
        System.out.println("option name Clear Hash type button");

        // Add this line to declare the "UCI_ShowWDL" option
        System.out.println("option name UCI_ShowWDL type check default false"); // Add this line!
        
        System.out.println("uciok"); 
    }
    private static void handleIsReady() {
    	long temp = BoardState.KNIGHT_MOVES[21];
        System.out.println("readyok");
    }

    private static void handleNewGame() {
    	for (int i = 0; i < Engine.moves.length; i++) {
    	    Arrays.fill(Engine.moves[i], 0);
    	}

    	for (int i = 0; i < Engine.metaData.length; i++) {
    	    if (Engine.metaData[i] == null) {
    	    	Engine.metaData[i] = new BoardMetaData();  // create if not already created
    	    } else {
    	        Engine.metaData[i].reset();  // implement this method to clear internal fields
    	    }
    	}
    }

    private static void handlePosition(String input) {
    	String command = input.substring("position ".length());
    	
    	if (command.startsWith("startpos")) {
    		Engine.Board = new BoardState(Engine.baseBoardState);
    	}
    	else if (command.startsWith("fen")) {
    		int movesIndex = command.indexOf(" moves ");
    	    String fenString;
    	    
    	    if (movesIndex != -1) {
    	        // There are moves after the fen string
    	        fenString = command.substring("fen ".length(), movesIndex).trim();
    	    } else {
    	        // No moves, fen is the rest of the string
    	        fenString = command.substring("fen ".length()).trim();
    	    }
    	    Engine.Board = BoardState.loadFEN(fenString);
    	}
    	
    	int movesIndex = command.indexOf(" moves ");
    	String movesString = null;
    	if (movesIndex != -1) {
    	    movesString = command.substring(movesIndex + " moves ".length()).trim();
    	}
    	
    	if (movesString != null)
    		BoardState.makeMoves(Engine.Board,movesString);
    	
    }

    private static void handleGo(String input) {
    	Engine.go = true;
    	
    	int wtime = 0;
        int btime = 0;
        int winc = 0;
        int binc = 0;
        int movetime = 0;

        String[] tokens = input.split("\\s+");

        for (int i = 0; i < tokens.length - 1; i++) {
            switch (tokens[i]) {
                case "wtime" -> wtime = Integer.parseInt(tokens[++i]);
                case "btime" -> btime = Integer.parseInt(tokens[++i]);
                case "winc"  -> winc  = Integer.parseInt(tokens[++i]);
                case "binc"  -> binc  = Integer.parseInt(tokens[++i]);
                case "movetime"->movetime = Integer.parseInt(tokens[++i]);
            }
        }
        
        
        if (Engine.Board.whiteToMove) {
        	Engine.remainingTime = wtime * 1_000_000L;
        	Engine.increment = winc * 1_000_000L;
        }
        else {
        	Engine.remainingTime = btime * 1_000_000L;
        	Engine.increment = binc * 1_000_000L;
        }
        if (movetime != 0) {
        	Engine.remainingTime = movetime*500_000L;
        	Engine.increment = 0;
        }
        
//        new Thread(() -> {
        Engine.findBestMove(); // replace with your implementation
        String moveString = Engine.moveToString(Engine.bestMove);
        System.out.println("bestmove " + moveString);
//        }).start();
    }

    private static void handleStop() {
        Engine.go = false; // Tell your engine to halt the current search ASAP
    }

    private static void handleQuit() {
        System.exit(0);
    }
    
    private static void handleSetOption(String input) {
    	System.err.println(input);
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