import java.util.Comparator;

/**
 * Sorts two given points by ascending x-value.
 * @author Fon
 * @version 2/19/2019
 */
public class SortbyX implements Comparator<HullPoint> 
{ 
	@Override
	public int compare(HullPoint p1, HullPoint p2) {
		if (p1.getX() - p2.getX() > 0)
			return 1;
		
		else if (p1.getX() - p2.getX() == 0)
			return 0;
		
		else 
			return -1;
	}
}
