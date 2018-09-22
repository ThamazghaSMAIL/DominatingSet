package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algorithms.Tools.DistPath;


public class Tools {
	

	public static ArrayList<Point> MIS_Steiner (ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>(); 
		ArrayList<Point> listvoisins = new ArrayList<Point>();
		for(Point p :points ){
			if(yapasarete(result, p , edgeThreshold) && !tout_le_monde_est_domine(points, result, edgeThreshold)){
				result.add(p);
			}
		}

		ArrayList<Point> voisins = new ArrayList<Point>();
		for( Point p : points ) {
			voisins = voisins(result, p, edgeThreshold);
			if( voisins.size() <= 5 && voisins.size()>=2)//477
				result.add(p);
		}

		System.out.println(" la taille d epoints :"+points.size());
		ArrayList<Point> res = new ArrayList<Point>();
		res.addAll(result);
		for( Point p : result ) {
			res.remove(p);
			if( ! connected(res, edgeThreshold) || ! tout_le_monde_est_domine(points, res, edgeThreshold))
				res.add(p);
		}
		return result;
	}
	public static ArrayList<Point> MIS_Black_Blue (ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>(); 
		ArrayList<Point> listvoisins = new ArrayList<Point>();
		
		for(Point p :points ){
			if(voisins(points, p, edgeThreshold).isEmpty()){
				result.add(p);
			}
			else {
				listvoisins = voisins(points, p, edgeThreshold);
				for( Point l : listvoisins ) {
					if( ! domine(result , l , edgeThreshold) ) {
						result.add(p);
					}
				}
			}
		}
		ArrayList<Point> voisins = new ArrayList<Point>();
		for( Point p : points ) {
			voisins = voisins(result, p, edgeThreshold);
			if( voisins.size() <= 5 && voisins.size()>=2)//477
				result.add(p);
		}

		System.out.println(" la taille d epoints :"+points.size());
		ArrayList<Point> res = new ArrayList<Point>();
		res.addAll(result);
		for( Point p : result ) {
			res.remove(p);
			if( ! connected(res, edgeThreshold) || ! tout_le_monde_est_domine(points, res, edgeThreshold))
				res.add(p);
		}
		
		return res ;
	}
	public static ArrayList<Point> Meilleur_Score(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>(); 
		ArrayList<Point> listvoisins = new ArrayList<Point>();

		for(Point p : points ){
			if(voisins(points, p, edgeThreshold).isEmpty()){
				result.add(p);
			}

			else {
				listvoisins = voisins(points, p , edgeThreshold);
				for( Point l : listvoisins ) {
					if( ! domine(result , l , edgeThreshold) ) {
						result.add(p);
					}
				}
			}
		}
		DistPath distPath = new Tools.DistPath(points, edgeThreshold);
		double [][] dists =  distPath.getDists();
		int[][] paths=distPath.getPaths();

		ArrayList<Edge> edgesK = new ArrayList<Edge>();
		for (int i = 0; i<result.size();i++){
			for (int j = 0; j<result.size();j++){
				if(i!=j)
					edgesK.add(new Edge(result.get(i),
							result.get(j),
							dists[points.indexOf(result.get(i))][points.indexOf(result.get(j))]));
			}
		}

		ArrayList<Edge> edgesKS = Tools.kruskal(result, edgesK);
		ArrayList<Edge> edgesG=new ArrayList<Edge>();
		result = new ArrayList<Point>();
		for(int i=0;i<edgesKS.size();i++){
			Edge e = edgesKS.get(i);
			ArrayList<Point> p = Tools.chemin (e,points,paths);
			for(Point p1 : p)
				if(!result.contains(p1))
					result.add(p1);
			for(int j = 0; j< p.size()-1; j++){
				edgesG.add(new Edge(p.get(j),p.get(j+1)));

			}
		}
		edgesG = Tools.kruskal(result, edgesG);
		result = new ArrayList<Point>();
		for(int i=0;i<edgesG.size();i++){
			Edge e = edgesG.get(i);
			if(!result.contains(e.getP1()))
				result.add(e.getP1());
			if(!result.contains(e.getP2()))
				result.add(e.getP2());	
		}

		ArrayList<Point> res = (ArrayList<Point>) result.clone();
		for ( Point p : result ) {
			res.remove(p);
			if( ! connected(res, edgeThreshold) || ! tout_le_monde_est_domine(points, res, edgeThreshold))
				res.add(p);
		}



		if(tout_le_monde_est_domine(points, result, edgeThreshold))
			System.out.println(" tout_le_monde_est_domine");

		System.out.println("connected : "+connected(points, edgeThreshold));



		//return cycle(points , result, edgeThreshold);
		return res ;

	}
	
	
	public static boolean yapasarete(ArrayList<Point> S , Point d , int edgeThreshold){
		for(Point p : S){
			if(p.distance(d) < edgeThreshold){
				return false;
			}
		}
		return true;//il a aucune arete avec les elts de S
	}
	public static boolean tout_le_monde_est_domine ( ArrayList<Point> points , ArrayList<Point> S , int edgeThreshold){
		for(Point p : points) {
			if( ! domine( S , p , edgeThreshold))
				return false ;
		}
		return true ;
	}	
	public static ArrayList<Point> Wu_Li(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>(); 

		for(Point p :points ){

			if( a_deux_voisins_non_connectés(points, p, edgeThreshold) )
				result.add(p);
		}

		return result;
	}
	public static boolean domine(ArrayList<Point> S , Point d , int edgeThreshold){

		for(Point s : S){
			if(s.equals(d))
				return true;
			else
				if( s.distance(d) <= edgeThreshold ) 
					return true;
		}
		return false;
	}
	public static boolean connected ( ArrayList<Point> points , int edgeThreshold ) {
		ArrayList<Point> reste = new ArrayList<>();
		ArrayList<Point> prochains = new ArrayList<>();
		reste.addAll(points);

		prochains.add(reste.remove(0));

		while( prochains.size() > 0 ) {
			List<Point> tmp = new ArrayList<>();
			Point p = prochains.remove(0);

			for( int i = 0 ; i < reste.size() ; i++ ) {
				if( reste.get(i).distance(p) <= edgeThreshold )
					tmp.add(reste.get(i));

			}
			for( Point p0 : tmp ) {
				prochains.add(p0);
				reste.remove(p0);
			}
		}
		if (reste.size() == 0)
			return true ;
		else 
			return false ;
	}
	public static ArrayList<Point> Wu_Li_ameliore(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>(); 

		for(Point p :points ){

			if( a_deux_voisins_non_connectés(points, p, edgeThreshold) )
				result.add(p);
		}

		return result;
	}
	public static ArrayList<Point> voisins(ArrayList<Point> points , Point p , int edgeThreshold){
		ArrayList<Point> v = new ArrayList<Point>();
		for(Point q : points ) {
			if( (p.distance(q) <= edgeThreshold) && (! q.equals(p)) )
				v.add(q);
		}
		return v;
	}


	/**
	 * méthode qui retourne true si le point a au moins deux voisins non connectés
	 * @param points
	 * @param d
	 * @param edgeThreshold
	 * @return
	 */
	public static boolean a_deux_voisins_non_connectés(ArrayList<Point> points , Point dominateur_candidat , int edgeThreshold){

		Point domine1 = null;
		Point domine2 = null;
		for( Point p : points ) {
			if(domine1 == null) {
				if(p.distance(dominateur_candidat) <= edgeThreshold){
					domine1 = p ;
				}
			}
			else {
				if(domine2==null) {
					if(p.distance(dominateur_candidat) <= edgeThreshold){
						domine2 = p ;
					}
				}else {
					if(domine1.distance(domine2) > edgeThreshold ){
						return true; //donc c'est un dominateur
					}
				}

			}
		}
		return false ;
	}


	public static List<Edge> pointsToEdges(List<Point> points){

		List<Edge> edges = new ArrayList<>();
		for(int j=0;j<points.size();j++) {	
			Point p1 = points.get(j);
			for(int i=0;i<points.size();i++){
				Point p2 = points.get(i);
				if(p1.equals(p2))
					continue;
				Edge edge = new Edge(p1,p2);
				edges.add(edge);
			}

		}
		return edges;
	}

	public static Tree2D naif(ArrayList<Point> points) {

		Point pere,fils,candidat;
		fils=null;
		candidat=null;
		pere=points.remove(0);
		Tree2D root = new Tree2D(pere,new ArrayList<Tree2D>());
		Tree2D myResult = root;
		int size = points.size();
		double distance,distanceTmp;
		distance=0;
		while(points.size()>0){
			System.out.println("1");
			for(int j=0;j<points.size();j++){
				candidat = points.get(j);
				System.out.println("2");
				if(fils == null){
					System.out.println("2.1");
					fils = candidat;
					distance=Math.sqrt(Math.pow(pere.getX()-candidat.getX(),2)
							+Math.pow(pere.getY()-candidat.getY(),2));
				}

				else{
					distanceTmp=Math.sqrt(Math.pow(pere.getX()-candidat.getX(),2)
							+Math.pow(pere.getY()-candidat.getY(),2));
					System.out.println("2.2");
					if(distance>distanceTmp){
						System.out.println("2.2.1");
						fils=candidat;
						distance=distanceTmp;
					}
				}
			}
			pere=points.remove(points.indexOf(fils));
			fils=null;
			Tree2D tmp = new Tree2D(pere,new ArrayList<Tree2D>());
			root.getSubTrees().add(tmp);
			root=tmp;
		}
		return myResult;
	}

	public static Tree2D kruskal(ArrayList<Point> points) {

		Map<Point,Integer> map=new HashMap<Point,Integer>();
		for(int i=0;i<points.size();i++){
			map.put(points.get(i), new Integer(i));
		}


		ArrayList<Edge> edges = new ArrayList<>();


		System.out.println(edges.size());
		edges.sort(new EdgeComparator());
		System.out.println(edges.size());

		ArrayList<Edge> treeEdges = new ArrayList<>();


		for(int i=0;treeEdges.size() < points.size()-1;i++){
			Edge edge = edges.get(i);
			Point p1= edge.getP1();
			Point p2= edge.getP2();
			Integer v1 = map.get(p1);
			Integer v2 = map.get(p2);
			if(! (v1.equals(v2)) ) {
				for(Point p : map.keySet())
					if(map.get(p).equals(v1))
						map.put(p, v2);
				treeEdges.add(edge);
			}
		}

		return edgesToTree(treeEdges,treeEdges.get(0).getP1());

	}

	public static ArrayList<Edge> kruskal(ArrayList<Point> points,ArrayList<Edge> edges) {

		Map<Point,Integer> map=new HashMap<Point,Integer>();
		for(int i=0;i<points.size();i++){
			map.put(points.get(i), new Integer(i));
		}
		System.out.println(edges.size());
		edges.sort(new EdgeComparator());
		System.out.println(edges.size());

		ArrayList<Edge> treeEdges = new ArrayList<>();


		for(int i=0;treeEdges.size() < points.size()-1;i++){
			Edge edge = edges.get(i);
			Point p1= edge.getP1();
			Point p2= edge.getP2();
			Integer v1 = map.get(p1);
			Integer v2 = map.get(p2);
			if(! (v1.equals(v2)) ) {
				for(Point p : map.keySet())
					if(map.get(p).equals(v1))
						map.put(p, v2);
				treeEdges.add(edge);
			}
		}

		return treeEdges;

	}

	public static Tree2D edgesToTree(ArrayList<Edge> edges,Point pere){
		Tree2D tree = new Tree2D(pere,new ArrayList<Tree2D>());
		ArrayList<Edge> sons = new ArrayList<Edge>();

		for(int i =0; i<edges.size(); i++){
			Edge edge = edges.get(i);
			Point a = edge.getP1();;
			Point b = edge.getP2();
			if(pere.equals(a))
				sons.add(edge);
			else
				if(pere.equals(b)){
					sons.add(edge);
				}	
		}


		for(int i = 0; i<sons.size();i++)
			edges.remove(sons.get(i));


		for(int i =0; i<sons.size(); i++){
			Edge edge = sons.get(i);
			Point a = edge.getP1();;
			Point b = edge.getP2();
			if(pere.equals(a))
				tree.getSubTrees().add(edgesToTree(edges,b));
			else
				if(pere.equals(b))
					tree.getSubTrees().add(edgesToTree(edges,a));
		}
		return tree;
	}

	public boolean noCycle2(ArrayList<Tree2D> edges,Tree2D edge,Map<Point,Integer> map){
		Point p1= edge.getRoot();
		Point p2= edge.getSubTrees().get(0).getRoot();
		System.out.println(p1+" "+p2);
		System.out.println(map.get(p1)+" "+map.get(p2));
		if(map.get(p1).equals(map.get(p2)))
			return false;
		else {
			for(Point p : map.keySet())
				if(map.get(p).equals(map.get(p1)))
					map.put(p, map.get(p2));
			edges.add(edge);
			return true;
		}
	}

	public  static int[][] calculShortestPaths(ArrayList<Point> points, int edgeThreshold) {

		double [][] dists =  new double[points.size()][points.size()];
		int[][] paths=new int[points.size()][points.size()];

		for(int i=0;i<points.size();i++){
			for(int j=0;j<points.size();j++){
				if(i==j){
					dists[i][j]=0;
					paths[i][j]=j;
				}
				else{
					double dist = points.get(i).distance(points.get(j));
					if(dist>=edgeThreshold)
						dists[i][j]=Double.POSITIVE_INFINITY;
					else{
						dists[i][j]= dist;
						paths[i][j] = j;
					}
				}
			}
		}
		for(int k = 0; k<points.size();k++){
			for(int i=0;i<points.size();i++){
				for(int j=0;j<points.size();j++){
					double d =dists[i][k]+dists[k][j];
					if(dists[i][j]>d){
						paths[i][j]= paths[i][k];
						dists[i][j]=d;
					}
				}
			}
		}
		return paths;
	}

	public static Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {

		DistPath distPath = new Tools.DistPath(points, edgeThreshold);
		double [][] dists =  distPath.getDists();
		int[][] paths=distPath.getPaths();

		ArrayList<Edge> edgesK = new ArrayList<Edge>();
		for (int i = 0; i<hitPoints.size();i++){
			for (int j = 0; j<hitPoints.size();j++){
				if(i!=j)
					edgesK.add(new Edge(hitPoints.get(i),
							hitPoints.get(j),
							dists[points.indexOf(hitPoints.get(i))][points.indexOf(hitPoints.get(j))]));
			}
		}

		ArrayList<Edge> edgesKS = Tools.kruskal(hitPoints, edgesK);
		ArrayList<Edge> edgesG=new ArrayList<Edge>();
		ArrayList<Point> res = new ArrayList<Point>();
		for(int i=0;i<edgesKS.size();i++){
			Edge e = edgesKS.get(i);
			ArrayList<Point> p = chemin (e,points,paths);
			for(Point p1 : p)
				if(!res.contains(p1))
					res.add(p1);
			for(int j = 0; j< p.size()-1; j++){
				edgesG.add(new Edge(p.get(j),p.get(j+1)));

			}
		}
		edgesK = Tools.kruskal(res, edgesG);
		return Tools.edgesToTree(edgesG,edgesG.get(0).getP1());
	}

	public static ArrayList<Point> chemin(Edge e,ArrayList<Point> ps,int [][] pa){
		ArrayList<Point> res = new ArrayList<Point>();
		Point tmp = e.getP1();

		while(!tmp.equals(e.getP2())){
			res.add(tmp);
			int i = ps.indexOf(tmp);
			int j = ps.indexOf(e.getP2());
			tmp = ps.get(pa[i][j]);
		}
		res.add(e.getP2());
		return res;
	}
	public static class DistPath{

		private double [][] dists;
		private int[][] paths;
		public double[][] getDists() {
			return dists;
		}
		public int[][] getPaths() {
			return paths;
		}
		public DistPath (ArrayList<Point> points, int edgeThreshold) {

			dists =  new double[points.size()][points.size()];
			paths=new int[points.size()][points.size()];

			for(int i=0;i<points.size();i++){
				for(int j=0;j<points.size();j++){
					if(i==j){
						dists[i][j]=0;
						paths[i][j]=j;
					}
					else{
						double dist = points.get(i).distance(points.get(j));
						if(dist>=edgeThreshold)
							dists[i][j]=Double.POSITIVE_INFINITY;
						else{
							dists[i][j]= dist;
							paths[i][j] = j;
						}
					}
				}
			}
			for(int k = 0; k<points.size();k++){
				for(int i=0;i<points.size();i++){
					for(int j=0;j<points.size();j++){
						double d =dists[i][k]+dists[k][j];
						if(dists[i][j]>d){
							paths[i][j]= paths[i][k];
							dists[i][j]=d;
						}
					}
				}
			}
		}
	}
}
