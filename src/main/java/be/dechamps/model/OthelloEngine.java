package be.dechamps.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class puts it all together, it is what makes a position an othello
 * position. It will generate the moves etc. It contains the othello business
 * logic.
 * 
 * @author Mark
 * 
 */
public class OthelloEngine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private static final int CORNER_VALUE = 200;
	private static final int NTCORNER_VALUE = 100;
	private static final int CANTMOVE = 10;

	private int depth = 4;
	private int size=0;
	private int black;
	private int white;
	private boolean gameOver = false;
	//private Logger LOGGER = Logger.getAnonymousLogger();

	private Set<OthelloPosition> positions = null;
	private OthelloPosition currentPosition;

	public OthelloEngine() {
		init();
	}

	public OthelloEngine(OthelloPosition othelloPosition) {
		this.currentPosition = othelloPosition;
		size = othelloPosition.getHeight()*othelloPosition.getWidth();
	}

	private void init() {
		OthelloPosition board = new OthelloPosition();
		this.currentPosition = board;
		size = board.getHeight()*board.getWidth();
		initOthelloStartPosition(currentPosition);
	}

	private void initOthelloStartPosition(Position position) {
		position.setValueAt(3, 3, OthelloPosition.WHITE_PAWN);
		position.setValueAt(4, 3, OthelloPosition.BLACK_PAWN);
		position.setValueAt(3, 4, OthelloPosition.BLACK_PAWN);
		position.setValueAt(4, 4, OthelloPosition.WHITE_PAWN);
		black = 2;
		white = 2;
		gameOver = false;
	}

	public int getWidth() {
		return currentPosition.getWidth();
	}

	public int getHeight() {
		return currentPosition.getHeight();
	}

	public int getValue(int x, int y) {
		return currentPosition.getValueAt(x, y);
	}

	public boolean isBlacksMove() {
		return currentPosition.isBlacksMove();
	}

	public void setBlacksMove(boolean blacksMove) {
		currentPosition.setBlacksMove(blacksMove);
	}

	public int getBlack() {
		return black;
	}

	public int getWhite() {
		return white;
	}

	public void setWhite(int white) {
		this.white = white;
	}

	@Override
	public String toString() {
		return currentPosition.toString();
	}

	public String getString() {
		return this.toString();
	}

	private boolean makeMove(Point move) {
		return makeMove(move.getX(), move.getY());
	}

	public boolean makeMove(int parameter) {
		int x = parameter % getWidth();
		int y = parameter / getHeight();
		return makeMove(x, y);
	}

	public boolean makeMove(int x, int y) {
		if (gameOver) {
			return false;
		}

		if (!currentPosition.isEmptyFieldAt(x, y)) {
			return false;
		}

		boolean succes = currentPosition.makeMove(x, y);
		if (succes) {
			/**
			 * is the game over? check for no more empty squares
			 * 
			 */
			Point colors = currentPosition.countPawns();
			black = colors.getY();
			white = colors.getX();

			if ((black + white == getWidth() * getHeight()) || white == 0
					|| black == 0) {
				System.out.println("GAME OVER!");
				gameOver = true;
			}
		} else {
			return false;
		}
		return true;
	}

	public boolean canMove(int parameter) {
		int x = parameter % getWidth();
		int y = parameter / getHeight();
		return currentPosition.canMove(x, y);
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void thinkAndMove() {
		//LOGGER.setLevel(Level.OFF);
		Set<String> lookup = new HashSet<String>();
		System.out.println("---------- Thinking starts ---------for Black?"
				+ currentPosition.isBlacksMove());
		List<Point> possibleMoves = currentPosition.getPossibleMoves();
		Point bestMove = null;
		int highestScore = 0;
		boolean initialized = false;
		if (!possibleMoves.isEmpty()) {
			for (Point p : possibleMoves) {

				OthelloPosition position = currentPosition;// .clone();
				position.makeMove(p.getX(), p.getY());
				if (lookup.contains(position.getPositionString())) {
					position.undo();
					continue;
				}
				int score = minimax(position, depth, lookup);

				/**
				 * vb mens speelt met wit, doet een zet. Nu is het aan de
				 * computer en die genereert al zijn mogelijke zetten. Hij wil
				 * die evalueren en de beste zet vinden, dus hij wil de laagste
				 * waarde In elk bord die hij genereerde heeft hij een zwarte
				 * zet gespeeld en is wit dus aan de beurt. Dus als wit aan de
				 * beurt is zijn we geinterreseerd in de laagste waarden
				 */
				if (position.isBlacksMove()) {
					if (!initialized) {
						highestScore = Integer.MIN_VALUE;
						initialized = true;
					}
					int max = Math.max(highestScore, score);
					if (max > highestScore) {
						bestMove = p;
						highestScore = max;
					}
				} else {
					if (!initialized) {
						highestScore = Integer.MAX_VALUE;
						initialized = true;
					}
					int min = Math.min(highestScore, score);
					if (min < highestScore) {
						bestMove = p;
						highestScore = min;
					}
				}
				position.undo();

			}

			if (bestMove != null) {
				currentPosition.setScore(highestScore);
				makeMove(bestMove);
			}
			// for each move, minimax, pick best score and move!
		} else {
			// computer kan niet meer zetten, passen dan maar
			currentPosition.switchMove();
		}
		System.out.println("---------- Thinking stops ---------");
	}

	public int getScore() {
		return currentPosition.getScore();
	}

	int minimax(OthelloPosition startPosition, int depth, Set<String> positions) {

		List<Point> moves = startPosition.getPossibleMoves();
		if (depth <= 0) {
			int score = evaluate(startPosition, moves.size());
			return score;
		}

		if (moves.isEmpty()) {
			// goed voor de speler die niet aan zet is, voor zwart is dit een
			// positief getal, voor wit een negatief
			return startPosition.isBlacksMove() ? CANTMOVE : -CANTMOVE;
		}
		int alpha = 0;
		boolean initialized = false;
		for (Point p : moves) {
			// OthelloPosition newPosition = (OthelloPosition) startPosition
			// .clone();
			OthelloPosition newPosition = startPosition;
			newPosition.makeMove(p.getX(), p.getY());
			if(!positions.add(newPosition.toString())){
				newPosition.undo();
				//System.out.println("skipping:"+newPosition.toString());
				continue;
			}
			
			int minimaxScore = minimax(newPosition, depth - 1, positions);
			/**
			 * vb mens speelt met wit, doet een zet. Nu is het aan de computer
			 * en die genereert al zijn mogelijke zetten. Hij wil die evalueren
			 * en de beste zet vinden, dus hij wil de laagste waarde. In elk
			 * bord die hij genereerde heeft hij een zwarte zet gespeeld en is
			 * wit dus aan de beurt. Dus als wit aan de beurt is zijn we
			 * genterreeerd in de laagste waarden
			 */
			if (newPosition.isBlacksMove()) {
				if (!initialized) {
					// wit speelde een zet, nu is het aan zwart. Wit speelde
					// een zet en zoekt dus de hoogste waarde.
					alpha = Integer.MIN_VALUE;
					initialized = true;
				}
				alpha = Math.max(alpha, minimaxScore);
			} else {
				if (!initialized) {
					alpha = Integer.MAX_VALUE;
					initialized = true;
				}
				alpha = Math.min(alpha, minimaxScore);
			}
			newPosition.undo();
		}

		return alpha;
	}

	private int evaluate(OthelloPosition position, int nrMoves) {

		// we evalueren altijd DE STELLING, dus los van wie aan zet is
		Point numberOfPawns = position.countPawns();

		int score = 0;
		// check if there are pawns left
		boolean blacksmove = position.isBlacksMove();
		if (numberOfPawns.getX() == 0)
			// no white pawns left,good for black!
			if (blacksmove)
				return Integer.MIN_VALUE;
			else
				return Integer.MAX_VALUE;
		if (numberOfPawns.getY() == 0)
			// no blackpawns, good for white!
			if (blacksmove)
				return Integer.MAX_VALUE;
			else
				return Integer.MIN_VALUE;
		
		//if the board is full, the one with most pawns wins
		if(numberOfPawns.getX()+numberOfPawns.getY()==size){
			if (numberOfPawns.getY()>numberOfPawns.getX())
				return Integer.MIN_VALUE;
			else if (numberOfPawns.getX()>numberOfPawns.getY())
				return Integer.MAX_VALUE;
		}
		
		// check corners
		if (numberOfPawns.getX() + numberOfPawns.getY() < 40) {
			// hoe minder pionnen hoe beter...
			//score += (numberOfPawns.getY() - numberOfPawns.getX())/2;
		}else{
			//hoe meer pionnen hoe beter
			score += (numberOfPawns.getX() - numberOfPawns.getY())*2;
		}

		int luc = position.getValueAt(0, 0);// left upper corner
		int height = getHeight();
		int ldc = position.getValueAt(0, height - 1);// left bottom corner
		int width = getWidth();
		int ruc = position.getValueAt(width - 1, 0);// right top corner
		int rdc = position.getValueAt(width - 1, height - 1);

		score += scoreCorner(luc);
		score += scoreCorner(ldc);
		score += scoreCorner(ruc);
		score += scoreCorner(rdc);

		// check next to corners, squares diagonally inside the corners
		// left upper next to corner
		int lunc = position.getValueAt(1, 1);
		// left bottom next to corner
		int ldnc = position.getValueAt(1, height - 2);
		// right top next to corner
		int runc = position.getValueAt(width - 2, 1);
		// right down next to corner
		int rdnc = position.getValueAt(width - 2, height - 2);

		score += scoreNTCorner(lunc, luc);
		score += scoreNTCorner(ldnc, ldc);
		score += scoreNTCorner(runc, ruc);
		score += scoreNTCorner(rdnc, rdc);

		// calculate number of moves opponent has from this position
		if (blacksmove) {
			// hier willen we wel weten wie aan zet is om deze waarde betekenis
			// te knn geven
			// so white played a move and is evaluating and wants + score, the
			// more moves for the opponent, the worse
			score -= nrMoves;
		} else {
			// hoe meer zetten wit heeft, hoe gelukkiger hij is
			score += nrMoves;
		}

		// empty squares count
		Point emptySquares = position.getEmptySquareCount();
		// black wants a lot of white empty squares (so -X) and little black (so
		// +Y, the more, the worse)
		score += emptySquares.getY() - emptySquares.getX();

		//border pawns are good
		Point border = position.getBorderPawns();
		score += border.getX()-border.getY();
		
		position.setScore(score);
		if(score<=Integer.MIN_VALUE){
			       
			System.out.println(position.getString());
		}
		return score;
	}

	private int scoreCorner(int value) {

		if (value == OthelloPosition.WHITE_PAWN)
			return CORNER_VALUE;
		if (value == OthelloPosition.BLACK_PAWN)
			return -CORNER_VALUE;
		return 0;
	}

	private int scoreNTCorner(int value, int corner) {
		final int EMPTY = OthelloPosition.EMPTY;

		// good for black
		if (value == OthelloPosition.WHITE_PAWN && corner == EMPTY) {
			return -NTCORNER_VALUE;
		}
		// good for white
		if (value == OthelloPosition.BLACK_PAWN && corner == EMPTY) {
			return NTCORNER_VALUE;
		}

		return 0;
	}

	public boolean hasMoveLeft() {
		return !currentPosition.getPossibleMoves().isEmpty();
	}

	public void restart() {
		init();

	}

	public OthelloPosition getCurrentPosition() {
		return currentPosition;
	}

	public Set<OthelloPosition> getPositions() {
		return positions;
	}

	public void setDepth(int level) {
		this.depth = level;

	}

	public Object getDepth() {
		return depth;
	}

	public Point getLastMove() {
		return currentPosition.getLastMove();
	}

	public int evaluatePosition(OthelloPosition othelloPosition, int i) {
		return evaluate(othelloPosition, i);

	}

}