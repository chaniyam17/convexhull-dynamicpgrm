import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Enumeration;
import java.awt.Container;
import java.awt.Color;
import java.util.Hashtable;

/**
 * This class provides a graphical display for a running convex hull program.
 * 
 * @author Nathan Sprague
 * @version 4/10/2009
 */

public class HullGUI
{
    private JFrame frame;
    private double maxX; //of all points
    private double minX; 
    private double maxY;
    private double minY;
    private int pauseTime;
    private Hashtable<Integer, JComponent> components;
    private boolean pointsSet;
   

    private HullComponent dummyComponent;
  
    /**
     * Constructor for objects of class HullGUI
     *
     *  @param pause    time to pause (in milliseconds) after each update
     */
    public HullGUI(int pauseTime)
    {
        frame = new JFrame();
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Container cp = frame.getContentPane();
        cp.setBackground(Color.WHITE);

        this.pauseTime = pauseTime;
        this.pointsSet = false;

        //frame is only updated after components are added.  
        //this is a dummy component that is added after any component
        //is erased, so the eresed component will actually disappear.
        HullPoint[] dp = new HullPoint[1];
        dp[0] = new HullPoint(-1,-1);
        dummyComponent = new HullComponent(dp, this, Color.WHITE);
        frame.add(dummyComponent);

        components = new Hashtable<Integer, JComponent>();
    }
    
    
    /**
     * Constructor for objects of class HullGUI
     *
     *  @param points   the set of points that we will be working with
     *  @param pause    time to pause (in milliseconds) after each update
     */
    public HullGUI(HullPoint[] points, int pauseTime)
    {
        this(pauseTime);
        setPoints(points);
    
    }
    /**
     * Tell the GUI the set of points it will be dealing with.  Used for finding min
     * and max values for scaling. 
     *    @param points 
     */
    public void setPoints(HullPoint[] points)
    {
        this.pointsSet = true;
        maxX = minX = points[0].getX();
        for (int i = 1; i < points.length; i++) {
             if (points[i].getX() > maxX)
                  maxX = points[i].getX();
             if (points[i].getX() < minX)
                  minX = points[i].getX();   
        }
      
        maxY = minY = points[0].getY();
        for (int i = 1; i < points.length; i++) {
             if (points[i].getY() > maxY)
                  maxY = points[i].getY();
             if (points[i].getY() < minY)
                  minY = points[i].getY();   
        }
    }
    
     /**
     * Draw a set of points as small circles
     * 
     * @param  points   the set of points to draw
     */
    public void drawPoints(HullPoint[] points)
    {
        PointsComponent pc = new PointsComponent(points, this, Color.RED);
        frame.add(pc);
        frame.validate();
	components.put(Arrays.deepHashCode(points), pc);	
	this.pause();
    }
    

    /**
     * Draw a set of points as small circles, with lines connecting 
     * subsequent points in the array. 
     * 
     * @param  hull  the array of points to draw
     */
    public void drawHull(HullPoint[] hull)
    {
	if (components.get(Arrays.deepHashCode(hull))== null)  {
	    HullComponent hc = new HullComponent(hull, this, Color.BLUE);
	    frame.add(hc);
	    frame.validate();
	    components.put(Arrays.deepHashCode(hull), hc);
	    this.pause();
	}
    }
    
    /**
     * Erase a previously drawn item. 
     * 
     * @param points   the array of points to be erased (could have been
     * originally drawn using drawPoints or drawHull.)
     */
    public void erase(HullPoint[] points)
    {


        JComponent c = components.remove(Arrays.deepHashCode(points));
        if (c != null) {
            frame.remove(c);
            frame.remove(dummyComponent);
            frame.add(dummyComponent);
            frame.validate();
        } else {
            System.out.println("Attempt to erase non-existent component.");    
        }
    }
    
    /**
     * Clear the gui.
     * 
     */
    public void clear()
    {
        Enumeration<JComponent> enu = components.elements();
        while(enu.hasMoreElements()) {
            JComponent c = enu.nextElement();
            frame.remove(c);
            frame.remove(dummyComponent);
            frame.add(dummyComponent);
            frame.validate();
        } 
    }

    /**
     * Pause execution for a designated period of time. 
     */
    protected void pause()
    {
	if (this.pauseTime > 0) {
	    try {
		Thread.currentThread().sleep(pauseTime);
	    }            
	    catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
    
    /**
     * Translate from raw coordinates to position on the graphics frame. 
     * @param point   the point in raw coordinates
     * @returns the corresponding point on the frame in pixels
     */
    
    protected HullPoint drawPos(HullPoint point)
    {

        HullPoint pos = new HullPoint(0,0);

        double rangeX = maxX - minX;
        pos.x = ((point.getX() - minX) / rangeX) 
	    * frame.getWidth() *.9 + .025 * frame.getWidth();

        double rangeY = maxY - minY;
        pos.y = ((point.getY() - minY) / rangeY) 
	    * frame.getHeight()*.9 + .05 * frame.getHeight();
        
        pos.y = frame.getHeight() - pos.y;
        //pos.y = frame.getHeight() - pos.y - .05 * frame.getHeight();
        
        return pos;
    }
    
   
    
}
