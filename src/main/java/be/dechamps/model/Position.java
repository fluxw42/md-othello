package be.dechamps.model;

import java.io.Serializable;

/**
 * This class represents a single position
 * @author Mark
 *
 */
public class Position implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private int[][] position;
	private int score = 0;
	
	public Position() {
		// TODO Auto-generated constructor stub
	}
	
	public Position(int width, int height) {
		this.width = width;
		this.height = height;
		position = new int[width][height];
	}

	public int getWidth(){
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getValueAt(int x,int y){
		validateXY(x, y);
		return position[x][y];
	}

	private void validateXY(int x, int y) {
		if(x>=width || y>=height || x<0 || y<0){
			throw new IllegalArgumentException("request x:"+x+" y:"+y+" and width:"+width+" and height:"+height);
		}
	}

	/**
	 * The score for this position
	 * @return
	 */
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setValueAt(int i, int j, int value) {
		validateXY(i, j);
		position[i][j] = value;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int j=0;j<height;j++)
			for(int i=0;i<width;i++)
				builder.append(position[i][j]+",");
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
	public String getString(){
		return this.toString();
	}
}
