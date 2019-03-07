import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.awt.geom.Ellipse2D;
import java.awt.Color;
import java.awt.RenderingHints;
/**
 * Graphical representation of a set of two dimensional points.
 * 
 * @author Nathan Sprague
 * @version 1
 * @date 4/10/2009
 */
public class PointsComponent extends JComponent
{
   
    HullPoint[] points;
    HullGUI gui;
    Color color;
    public static final long serialVersionUID = 1L;


   /** Constructs a new PointsComponent.
     * 
     *  @param points    the set of points to draw
     *  @param gui    the HullGUI object that will show the points
     *  @param color    the color to draw the points
     */
    public PointsComponent(HullPoint[] points, HullGUI gui, Color color)
    {
        this.points = points;
        this.gui = gui;
	this.color = color;
    }

    /**  paintComponent
     * 
     */
    public void paintComponent(Graphics g)
    {      
	Graphics2D g2 = (Graphics2D) g;
	g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
	for (int i = 0; i< points.length; i++) 
            {
                HullPoint pos = gui.drawPos(points[i]);
		Ellipse2D.Double curPoint =  
                    new Ellipse2D.Double(pos.x-2.0, pos.y -2.0, 4.0, 4.0);
		g2.setColor(Color.RED);
		g2.fill(curPoint);
            }
    }
}
