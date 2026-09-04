public class BoardMetaData {
	public int castlingRights;
	public int enPassantFile;
	
	public BoardMetaData(int castlingRights, int enPassantFile) {
		this.castlingRights = castlingRights;
		this.enPassantFile = enPassantFile;
	}
	
	public BoardMetaData() {
		castlingRights = 0;
		enPassantFile = 0;
	}
	
	public void reset() {
		castlingRights = 0;
		enPassantFile = 0;
	}
}
