package guiClasses;

import java.awt.Graphics;

public class Vector {

	/**
	 * Draws a vector.
	 * 
	 * @param g Graphics object passed on from the paintComponent
	 * @param x1 x coordinate of the base of the vector
	 * @param y1 y coordinate of the base of the vector
	 * @param x2 x coordinate of the tip of the vector
	 * @param y2 y coordinate of the tip of the vector
	 */
	public static void drawVector(Graphics g, int x1, int y1, int x2, int y2) {
		
		// draws the basic vector's line
		g.drawLine(x1, y1, x2, y2);	
		
		
		// Now we have to figure out the coordinates of the tip of the sides of the arrows of the vector.
		double xTip1, yTip1;
		double xTip2, yTip2;
		
		// intersection (point that is 90% up from the base to the tip):
		double xI = 0.9*x2 + 0.1*x1;
		double yI = 0.9*y2 + 0.1*y1;
				  
		// distance of intersection to head:
		// "double L = Math.sqrt(Math.pow(x2-xI, 2) + Math.pow(y2-yI, 2))" also works
		double L = 0.1 * Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
				  
		if (y2-y1 != 0) {
			
			// the following method doesn't work for k=0 as we get infinity in one point
			// that's why we have if-else
			double k = (double)(y2-y1)/(x2-x1);
			
			// perpendicular's coefficient: 
			double kI = -1/k;			
			  
			/* perpendicular's formula:
			* p... y-yI = kI * (x-xI)
			* 
			* all points (x,y) distance L from intersection (xI, yI):
			* (x-xI)^2 + (y-yI)^2 = L^2
			* 
			* plugging in p:
			* (x-xI)^2 + ( kI*(x-xI) )^2 = L^2
			* (1+kI^2) (x-xI)^2 = L^2 
			* (x-xI)^2 = L^2 / (1+kI^2)
			* x-xI = +- (L / sqrt(1+kI^2))
			* x = +-(L/sqrt(1+kI^2)) + xI
			*
			*/
			
			xTip1 = L/Math.sqrt(1+Math.pow(kI, 2)) + xI; 
			xTip2 = -L/Math.sqrt(1+Math.pow(kI, 2)) + xI; 
			
			// we get y coordinates by plugging x into perpendicular's formula:
			yTip1 = kI * (xTip1-xI) + yI;
			yTip2 = kI * (xTip2-xI) + yI;
		}
			
		else  {
			// luckily, k=0 makes the geometry much simpler
			xTip1 = xI;
			xTip2 = xI;
			
			yTip1 = yI + L;
			yTip2 = yI -L;
		}

		// finally, we can draw! 
		g.drawLine((int)xTip1, (int)yTip1, x2, y2);	
		g.drawLine((int)xTip2, (int)yTip2, x2, y2);	
	}
	
}
