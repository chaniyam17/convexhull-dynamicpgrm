import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;


/*
 * Much of the io code below taken from Sun's io tutorial: 
 *  http://java.sun.com/docs/books/tutorial/essential/io/scanning.html
 * Here is their copyright notice...
 *
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 




/**
 * The PointSource class either reads an array of 2d points from a file, 
 * or generates a set of points randomly.  
 * 
 * @author Nathan Sprague
 * @version 4/10/2009
 */
public class PointSource
{
     /** readPoints reads a file containing a set of two dimensional points.
     * It should discard any whitespace characters and non-numeric values.
     * 
     *  @param fileName    location of the file containing the points
     *  @returns points    An array of points
     */
    public static HullPoint[] readPoints(String fileName) {
	
	ArrayList<HullPoint> pointList = 
	    new ArrayList<HullPoint>();  
	Scanner s = null;
        double sum = 0;
        try {
            s = new Scanner(new BufferedReader(new FileReader(fileName)));
            s.useLocale(Locale.US);
	    
	    double x, y;
	    boolean foundy;
            while (s.hasNext()) {
		
		if (s.hasNextDouble()) {
		    x = s.nextDouble();
		    foundy = false;
		    while (s.hasNext()) {
			if (s.hasNextDouble()) {
			    y = s.nextDouble();
			    HullPoint point = new HullPoint(x,y);
			    pointList.add(point);
			    foundy = true;
			    break;
			} else {
			    s.next();
			}   
		    }
		    if (!foundy) {
			System.out.println("Error: x without y");
			return null;
		    }
		    
		} else {
		    s.next();
		}   
	    }
	    
        } catch (IOException e) {
            System.out.println("Unable to open " + fileName+": "+e.getMessage());
        } finally {
            s.close();
        }   

	return pointList.toArray(new HullPoint[0]);
    }
    
}
