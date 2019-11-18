package be.dechamps.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class OthelloPosition extends Position implements Cloneable ,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private boolean blacksMove = true;// zwart begint
	public static final int WHITE_PAWN = 1;
	public static final int BLACK_PAWN = 2;
	public static final int EMPTY = 0;
	private final int width = 8;
	private final int height = 8;
	private Point numberOfPawns;
	private Point emptySquareCount;
	private Point borderPawns;
	private String positionString;
	private LinkedList<Collection<Point>> lastFlippedSquares = new LinkedList<Collection<Point>>();
	private LinkedList<Point> lastMove = new LinkedList<Point>();
	 
	
	
	public OthelloPosition() {
		super(8,8);
	}

	public OthelloPosition(String positionToParse) {
		super(8,8);
		String[] pawns = positionToParse.split(",");
		
		for(int i=0;i<pawns.length;i++){
			int value = Integer.valueOf(pawns[i]);
			setValueAt(i%8, i/8, value);
		}
		 doCounters();
	}

	/**
	 * Positions will be duplicated all the tiem to do calculations
	 */
	protected OthelloPosition clone() throws CloneNotSupportedException {
		OthelloPosition position = new OthelloPosition();
		for (int i = 0; i < getWidth(); i++)
			for (int j = 0; j < getHeight(); j++)
				position.setValueAt(i, j, this.getValueAt(i, j));
		position.setScore(this.getScore());
		position.setBlacksMove(isBlacksMove());
		return position;
	}

	private boolean isOpponentPawnAt(int i, int j) {
		// als zwart aan zet is dan is de tegenstander wit
		int opponent = blacksMove ? WHITE_PAWN : BLACK_PAWN;
		return getValueAt(i, j) == opponent;
	}

	public boolean isMyPawnAt(int i, int j) {
		// als zwart aan zet is dan is de tegenstander wit
		int me = blacksMove ? BLACK_PAWN : WHITE_PAWN;
		return getValueAt(i, j) == me;
	}

	public boolean isEmptyFieldAt(int i, int j) {
		return getValueAt(i, j) == EMPTY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isBlacksMove() {
		return blacksMove;
	}

	public void setBlacksMove(boolean blacksMove) {
		this.blacksMove = blacksMove;
	}

	public void flip(int x, int y) {
		int value = getValueAt(x, y);
		if (value != WHITE_PAWN && value != BLACK_PAWN) {
			throw new IllegalStateException(
					"Field must contain white or black pawn");
		}
		value = value == WHITE_PAWN ? BLACK_PAWN : WHITE_PAWN;
		setValueAt(x, y, value);

	}

	private void putPawn(int x, int y) {
		setValueAt(x, y, blacksMove ? BLACK_PAWN : WHITE_PAWN);
	}

	public void switchMove() {
		blacksMove = !blacksMove;
	}

	public Point countPawns() {
		return numberOfPawns;
	}

	/**
	 * Counts number of white and black pawns on the position
	 * and creates positionString
	 * @return a point with in the X the white and Y the black pawns
	 */
	private void doCounters() {
		int borderWhite=0,borderBlack=0;
		int white = 0, black = 0;
		int whiteSpace = 0, blackSpace = 0;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getWidth(); i++)
			for (int j = 0; j < getHeight(); j++) {
				int valueAt = getValueAt(i, j);
				builder.append(valueAt+",");
				switch (valueAt) {
				case EMPTY:
					continue;
				case WHITE_PAWN:
					white++;
					whiteSpace += countEmptySquares(i, j);
					borderWhite = (j==0||i==0)?borderWhite++:borderWhite;
					break;
				case BLACK_PAWN:
					black++;
					blackSpace += countEmptySquares(i, j);
					borderBlack = (j==0||i==0)?borderBlack++:borderBlack;
					break;
				}
			}
		borderPawns = Point.get(borderWhite,borderBlack);
		numberOfPawns = Point.get(white, black);
		emptySquareCount = Point.get(whiteSpace,blackSpace);
		positionString = builder.deleteCharAt(builder.length()-1).toString();
	}

	public String getPositionString(){
		return positionString;
	}
	
	public Point getEmptySquareCount() {
		return emptySquareCount;
	}

	public void setEmptySquareCount(Point emptySquareCount) {
		this.emptySquareCount = emptySquareCount;
	}

	private int countEmptySquares(int i, int j) {
		int result = 0;
		boolean i_gt_0 = i > 0;
		boolean j_gt_0 = j > 0;
		boolean i_st_width = i < getWidth() - 1;
		boolean j_st_height = j < getHeight() - 1;

		if (i_gt_0) {
			result += getValueAt(i - 1, j) == EMPTY ? 1 : 0;// left
			if (j_gt_0) {
				result += getValueAt(i - 1, j - 1) == EMPTY ? 1 : 0;// left up
			}
			if (j_st_height) {
				result += getValueAt(i - 1, j + 1) == EMPTY ? 1 : 0;// left down
			}

		}

		if (i_st_width){
			result += getValueAt(i + 1, j) == EMPTY ? 1 : 0;
			if (j_gt_0) {
				result += getValueAt(i + 1, j - 1) == EMPTY ? 1 : 0;// right up
			}
			if (j_st_height) {
				result += getValueAt(i + 1, j + 1) == EMPTY ? 1 : 0;// right down
			}
		}

		if (j_st_height)
			result += getValueAt(i, j + 1) == EMPTY ? 1 : 0;//down

		if (j_gt_0)
			result += getValueAt(i, j - 1) == EMPTY ? 1 : 0;//up

		return result;
	}

	public boolean isEmpty(int x, int y) {
		return getValueAt(x, y) == EMPTY;
	}

	private Collection<Point> getRightDownPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		x++;
		y++;
		boolean foundOpponentPawn = false;
		for (; x < getWidth() && y < getHeight(); x++, y++) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	private Collection<Point> getRightUpPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		x++;
		y--;
		boolean foundOpponentPawn = false;
		for (; x < getWidth() && y >= 0; x++, y--) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	private Collection<Point> getLeftDownPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		x--;
		y++;
		boolean foundOpponentPawn = false;
		for (; x >= 0 && y < getHeight(); x--, y++) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	private Collection<Point> getUpPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		y--;
		boolean foundOpponentPawn = false;
		for (; y >= 0; y--) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	private Collection<Point> getDownPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		y++;
		boolean foundOpponentPawn = false;
		for (; y < getHeight(); y++) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	private Collection<Point> getLeftUpPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		x--;
		y--;
		boolean foundOpponentPawn = false;
		for (; x >= 0 && y >= 0; x--, y--) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	private Collection<Point> getRightPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		x++;
		boolean foundOpponentPawn = false;
		for (; x < getWidth(); x++) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	private Collection<Point> getLeftPawnsToTurn(int x, int y) {
		Collection<Point> result = new ArrayList<Point>();
		// ga op eerste doelveld staan
		x--;
		boolean foundOpponentPawn = false;
		for (; x >= 0; x--) {
			boolean opponentPawn = isOpponentPawnAt(x, y);
			boolean emptyField = isEmptyFieldAt(x, y);
			boolean myPawn = isMyPawnAt(x, y);
			// leeg veld is hoedanook bailen
			// als je een pion van jezelf tegen komt en
			// je vond nog geen van je tegenstander dan geen goede zet
			if ((myPawn && foundOpponentPawn == false) || emptyField) {
				result.clear();
				return result;
			}
			if (opponentPawn == true) {
				foundOpponentPawn = true;
				result.add(Point.get(x, y));
			}
			//
			if (myPawn && foundOpponentPawn == true) {
				return result;
			}
		}
		result.clear();
		return result;
	}

	public List<Point> getPossibleMoves() {
		List<Point> result = new ArrayList<Point>();
		// generate moves
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				if (getValueAt(i, j) != OthelloPosition.EMPTY) {
					continue;
				}
				if (canMove(i, j)) {
					result.add(Point.get(i, j));
				}
			}
		}
		return result;
	}

	private boolean insideBoard(int x, int y) {
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
			return false;
		} else
			return true;

	}

	/**
	 * Looks in all directions to see if it can capture something in that
	 * direction. If it can, it won't do the other checks anymore
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canMove(int x, int y) {
		/**
		 * een zet kan, als ze binnen het bord ligt als er nog geen pion ligt
		 * als er rond die plaats een kleur van de tegenstander ligt, EN er in
		 * die richting dan ergens nog een kleur van jouw ligt. Anders niet.
		 */
		/* buiten het bord ? */
		if (!insideBoard(x, y)) {
			return false;
		}

		/* Ligt er al een pion ? */
		if (!isEmpty(x, y)) {
			return false;
		}

		return canCaptureDown(x, y) || canCaptureLeft(x, y)
				|| canCaptureLeftDown(x, y) || canCaptureLeftUp(x, y)
				|| canCaptureRight(x, y) || canCaptureRightDown(x, y)
				|| canCaptureRightUp(x, y) || canCaptureUp(x, y);
	}

	private boolean canCaptureRightDown(int x, int y) {
		return !getRightDownPawnsToTurn(x, y).isEmpty();
	}

	private boolean canCaptureRightUp(int x, int y) {
		return !getRightUpPawnsToTurn(x, y).isEmpty();
	}

	private boolean canCaptureDown(int x, int y) {
		return !getDownPawnsToTurn(x, y).isEmpty();
	}

	private boolean canCaptureLeftUp(int x, int y) {
		return !getLeftUpPawnsToTurn(x, y).isEmpty();
	}

	private boolean canCaptureRight(int x, int y) {
		return !getRightPawnsToTurn(x, y).isEmpty();
	}

	private boolean canCaptureLeft(int x, int y) {
		return !getLeftPawnsToTurn(x, y).isEmpty();
	}

	private boolean canCaptureLeftDown(int x, int y) {
		return !getLeftDownPawnsToTurn(x, y).isEmpty();
	}

	private boolean canCaptureUp(int x, int y) {
		return !getUpPawnsToTurn(x, y).isEmpty();
	}

	/**
	 * Gives back the pawns that would turn around if you would put a pawn at x
	 * and y
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Collection<Point> getFlipPoints(int x, int y) {
		Collection<Point> toFlip = new HashSet<Point>();
		toFlip.addAll(getDownPawnsToTurn(x, y));
		toFlip.addAll(getUpPawnsToTurn(x, y));
		toFlip.addAll(getRightPawnsToTurn(x, y));
		toFlip.addAll(getLeftPawnsToTurn(x, y));

		toFlip.addAll(getLeftUpPawnsToTurn(x, y));
		toFlip.addAll(getLeftDownPawnsToTurn(x, y));
		toFlip.addAll(getRightDownPawnsToTurn(x, y));
		toFlip.addAll(getRightUpPawnsToTurn(x, y));
		return toFlip;
	}

	public boolean makeMove(int x, int y) {
		Collection<Point> toFlip = getFlipPoints(x, y);

		if (!toFlip.isEmpty()) {
			for (Point p : toFlip) {
				flip(p.getX(), p.getY());
			}
			
			putPawn(x, y);
			Point thisMove = Point.get(x,y);
			lastMove.push(thisMove);
			lastFlippedSquares.push(toFlip);
				
			switchMove();
			doCounters();
			return true;
		}
		return false;
	}

	public boolean undo(){
		
		
		if (!lastFlippedSquares.isEmpty()) {
			Collection<Point> toFlip = lastFlippedSquares.pop();
			Point move = lastMove.pop();
			
			for (Point p : toFlip) {
				flip(p.getX(), p.getY());
			}
			setValueAt(move.getX(), move.getY(), EMPTY);
			switchMove();
			doCounters();
			
			return true;
		}
		return false;
	}
	
	public String toGraphString() {
		StringBuilder builder = new StringBuilder();
		for (int j = 0; j < height; j++) {
			builder.append("\n");
			for (int i = 0; i < width; i++) {
				int valueAt = getValueAt(i, j);
				if (valueAt == EMPTY) {
					builder.append(".");
				} else
					builder.append(valueAt == WHITE_PAWN ? "W" : "B");

			}
		}

		return builder.toString();
	}

	public Point getLastMove(){
		return lastMove.peekFirst();
	}

	public Point getBorderPawns() {
		return borderPawns;
	}

}
