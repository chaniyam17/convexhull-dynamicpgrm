import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;

/**
 * Graphical representation of a hull.  
 * 
 * @author Nathan Sprague
 * @version 4/10/2009
 */
public class HullComponent extends JComponent
{

    HullPoint[] points;
    HullGUI gui;
    Color color;
    public static final long serialVersionUID = 1L;


   /** Constructs a new HullComponent.
     * 
     *  @param points    the set of points to draw (will be connected with lines)
     *  @param gui    the HullGUI object that will show the points
     *  @param color    the color to draw the points and lines
     */
    public HullComponent(HullPoint[] points, HullGUI gui, Color color)
    {
        this.points = points;
        this.gui = gui;
        this.color = color;
    }
    /**  paintComponent - draw a line to from the first point the second etc. 
     * 
     */
    public void paintComponent(Graphics g)
    {      
    	Graphics2D g2 = (Graphics2D) g;
    	g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
    	for (int i = 0; i< points.length; i++) 
    	{
    		HullPoint pos1 = gui.drawPos(points[i]);
    		HullPoint pos2 = gui.drawPos(points[(i+1) % points.length]);
    		Ellipse2D.Double curPoint =  
    			new Ellipse2D.Double(pos1.x-3.0, pos1.y -3.0, 6.0, 6.0);
    		Line2D.Double curLine =  
    			new Line2D.Double(pos1, pos2);
    		g2.setColor(this.color);
    		// g2.setStroke(new BasicStroke(3));

    		g2.draw(curPoint);
    		g2.draw(curLine);

    	}
     }
}
