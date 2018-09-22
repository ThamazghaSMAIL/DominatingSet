package algorithms;

import java.awt.Point;

public class Edge implements Comparable<Edge>{

	protected Point p1;
	protected Point p2;
	protected double dist;

	public Edge(Point p1, Point p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		dist = distance();
	}
	public Edge(Point p1, Point p2, double d) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		dist = d;
	}
	public double distance(){
	    
		return  Math.sqrt(Math.pow(this.p1.getX()-this.p2.getX(),2)+Math.pow(this.p1.getY()-this.p2.getY(),2));
	    
	}
	
	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	
	public int compareTo(Edge e) {
		if(dist==e.dist)
			return 0;
		else
			if(dist<e.dist)
				return -1;
			else
				return 1;
	}
}
