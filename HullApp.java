import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * An Application that computes the convex hull of a set
 * of points (assuming there are no collinear points) and
 * draws it out graphically.
 * 
 * @author Nathan Sprague 
 * @version 4/10/2009
 * 
 * @author (Fon) Chaniya Miller
 * @version 2/19/2019
 * Modifications: methods added: findConvexHull, mergeHulls, sortCW, sortCCW, sortByX, findCenter, and orientation
 * 
 * With help from: https://iq.opengenus.org/divide-and-conquer-convex-hull/ and Zach Miller
 */
public class HullApp
{
    
	/**
	 * Main method to run the HullApp.
	 * @param args The command line arguments.
	 * @throws java.io.IOException when we can't read a file or something like that.
	 **/
    public static void main(String[] args) throws IOException {
	PointSource ps = new PointSource();
	HullPoint[] points = ps.readPoints("points.dat");
	
	//HullPoint[] hull = ps.readPoints("hull.dat");
	sortByX(points); 
	HullPoint[] hull = findConvexHull(points.length, points);
	HullGUI gui = new HullGUI(points, 1000);

	gui.drawPoints(points); 
	gui.drawHull(hull);
//	gui.erase(points);
//	gui.erase(hull);


	//System.out.println( "debug: " + Arrays.toString( findConvexHull(points.length, points) ) ); 
	
	//System.out.println( Arrays.toString( mergeHulls(hull, hull) ) );

       
    }
    
    /**
     * returns the convex hull of a set of points on a 2-d plane.
     * @param int size of array of points, HullPoint[] points to compute convex hull of
     * @return array of points with vertices of the convex hull
     */
    public static HullPoint[] findConvexHull(int n, HullPoint[] points) {
       
       if (n > 1) {
    	   int h = n/2;
    	   int m = n-h;
    	   HullPoint LH[], RH[]; //left and right hulls        	   
    	   LH = findConvexHull(h, Arrays.copyOfRange(points, 0, h)); 
    	   RH = findConvexHull(m, Arrays.copyOfRange(points, h, n)); 
    	   
//    	   HullPoint LH[] = new HullPoint[4];
//    	   LH[0]=new HullPoint(0.018503643, 0.91690444); //part of the testing process
//    	   LH[1]=new HullPoint(0.23113851,	0.79193704); 
//    	   LH[2]=new HullPoint(0.45646767,	0.9354697); 
//    	   LH[3]=new HullPoint(0.48598247,	0.73820725); 
//    	   
//    	   HullPoint RH[] = new HullPoint[4];
//    	   RH[0]=new HullPoint(0.60684258,	0.92181297); 
//    	   RH[1]=new HullPoint(0.76209683,	0.40570621); 
//    	   RH[2]=new HullPoint(0.89129897,	0.17626614); 
//    	   RH[3]=new HullPoint(0.95012929,	0.61543235); 
    	   
    	   return mergeHulls(LH, RH);
    	   } 

       else 
    	   return points;
    	   
    }
    
    /**
     * merges two convex hulls
     * @param HullA[] and HullB[]
     * @return mergedHull array with vertices of the resulting merged hulls
     */
    //merges two convex hulls & returns mergedHull[]
    public static HullPoint[] mergeHulls(HullPoint[] HullA, HullPoint[] HullB) {

    	sortCW(HullB); //sort HullB vertices in clockwise orientation
    	sortCCW(HullA); //sort HullA vertices in counterclockwise orientation

    	// indexA->index of rightmost point of HullA && indexB->index of leftmost point of HullB
        int sizeHullA = HullA.length, sizeHullB = HullB.length;
        int indexA = 0, indexB = 0;
        
        for (int i = 0; i < sizeHullA; i++) { 
            if (HullA[i].getX() > HullA[indexA].getX())
            	indexA = i;
        }
        
        for (int i = 0; i < sizeHullB; i++) { 
            if (HullB[i].getX() < HullB[indexB].getX())
            	indexB = i;
        }
        
        //finding the upper tangent
        int currentA = indexA, currentB = indexB;
        boolean done = false;
        while (!done) {
        	done = true;
        	while ( orientation(HullA[currentA], HullB[currentB], HullB[ (currentB+1 + sizeHullB)% sizeHullB] ) > 0 ) { 
        		currentB = (currentB+1 + sizeHullB) %sizeHullB;
        	}
        	while ( orientation(HullA[currentA], HullB[currentB], HullA[(currentA+1 + sizeHullA)% sizeHullA]) > 0 ) {
        		currentA = (currentA+1 + sizeHullA) %sizeHullA;
        		done = false;
        	}
        }
//        System.out.println("upperA: " + HullA[currentA].toString() + "upperB: " + HullB[currentB].toString());
        int upperA = currentA, upperB = currentB; //store upper tangent indexes

       
        //finding the lower tangent
        currentA = indexA; //reset currentA 
        currentB = indexB; //reset currentB
        done = false; // reset done
        while (!done) {
        	done = true;
        	while ( orientation(HullA[currentA], HullB[currentB], HullB[(currentB-1 + sizeHullB)% sizeHullB]) < 0 ) {
        		currentB = (currentB-1 + sizeHullB) %sizeHullB;
        	}
        	while ( orientation(HullA[currentA], HullB[currentB], HullA[(currentA-1 + sizeHullA)% sizeHullA]) < 0 ) {
        		currentA = (currentA-1 + sizeHullA) %sizeHullA;
        		//System.out.println("debug: " + currentA);
        		done = false;
        	}
        }
//      System.out.println("lowerA: " + HullA[currentA].toString() + "lowerB: " + HullB[currentB].toString());
        int lowerA = currentA, lowerB = currentB; //store lower tangent indexes
        
        //store new convex vertices 
        List<HullPoint> mergedHull = new ArrayList<>();
        mergedHull.add(HullA[upperA]);
        int index = upperA; //start at upperA tangent index(in HullA)
        //int mergedHullSize = mergedHull.size(); //update size of mergedHull[]
        
        //fills in mergedHull with points in HullA from upperA index -(CCW)-> lowerA index
        while (index != lowerA) {
        	index = (index+1 + sizeHullA)% sizeHullA;
        	mergedHull.add(HullA[index]);
        }
        
        mergedHull.add(HullB[lowerB]);
        index = lowerB; //continue at lowerB tangent index in HullB
        while (index != upperB) { //fills in mergedHull with points in HullB from lowerB index -(CCW)-> upperB index
        	index = (index-1 + sizeHullB)% sizeHullB;
        	mergedHull.add(HullB[index]);
        }
        
        HullPoint[] mergedHull2 = new HullPoint[mergedHull.size()];
        mergedHull2 = mergedHull.toArray(mergedHull2);
    	return mergedHull2;
    }
    
    /**
     * Sorts and returns array in clockwise order
     * @param HullPoints[] array to be sorted
     * @return points array sorted in clockwise order
     */
    public static HullPoint[] sortCW(HullPoint[] points) {
    	HullPoint center = findCenter(points);
    	Arrays.sort(points, (p1, p2) -> {
    		double angle1 = (Math.toDegrees(Math.atan2(p1.getY() - center.y, p1.x - center.x)) + 360) %360;
    		double angle2 = (Math.toDegrees(Math.atan2(p2.y - center.y, p2.x - center.x)) + 360) %360;
    		return (int) (angle2-angle1);
    			});
    	return points;
    }
    
    /**
     * Sorts and returns array in counter-clockwise order
     * @param HullPoints[] array to be sorted
     * @return points array sorted in counter-clockwise order
     */
    public static HullPoint[] sortCCW(HullPoint[] points) {
    	HullPoint center = findCenter(points);
    	Arrays.sort(points, (p1, p2) -> {
    		double angle1 = (Math.toDegrees(Math.atan2(p1.getY() - center.y, p1.x - center.x)) + 360) %360;
    		double angle2 = (Math.toDegrees(Math.atan2(p2.y - center.y, p2.x - center.x)) + 360) %360;
    		return (int) (angle1-angle2);
    			});
    	return points;
    }
    
    /**
     * Computes the center point in a given array of points
     * @param HullPoints[] array of points
     * @return HullPoint in the center of all points
     */
    public static HullPoint findCenter(HullPoint[] points) {
		double x = 0;
		double y = 0;
    	for (HullPoint point : points) {
			x += point.getX();
			y += point.getY();
    	}
    	HullPoint center = new HullPoint(x/(points.length), y/(points.length));
    	return center;
    }
    
    /**
     * Sorts a given array of HullPoints by x-value
     * @param HullPoints[] array of points
     */
    //sorts a given array of HullPoints by x-value
    public static void sortByX(HullPoint[] points) {
		Arrays.sort(points, new SortbyX()); //sorts array by X
    }
    
    /** (adapted from GeeksforGeeks)
     * returns the cross product of vectors (a,b) and (a,c)
     * if crossprod > 0, (a,b) is to the left of (a,c)
     * if crossprod < 0, (a,b) is to the right of (a,c)
     * if crossprod = 0, a,b,c are collinear 
     * @param HullPoints a, b, c
     * @return double cross product
     */
    private static double orientation (HullPoint a, HullPoint b, HullPoint c) {
    	double x1, x2, y1, y2;
    	y1 = a.getY() - b.getY();
    	x1 = a.getX() - b.getX();
    	y2 = a.getY() - c.getY();
    	x2 = a.getX() - c.getX();
    	return y2*x1 - y1*x2;
    }

    
    
}
