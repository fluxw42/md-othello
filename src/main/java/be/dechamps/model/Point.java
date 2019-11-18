package be.dechamps.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public final class Point implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Map<Integer,Point> cache = new HashMap<Integer,Point>(); 
	static{
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				Point prev = cache.put(i*100+j,new Point(i,j));
				System.out.println("Adding i:"+i+" j:"+j+" ixj:"+i*j+" prev:"+prev);
			}
		}
	}
	
	private int x;
	private int y;
	
	public static Point get(int i,int j){
		
			Point p = cache.get(i*100+j);
		if(p==null){
			return new Point(i,j);
		}
		return p;
	}
	
	private Point(int x,int y) {
		this.x= x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "x:"+x+" y:"+y;
	}
	
}
