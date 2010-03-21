/*Position thread to record robot coordinate
 * 
 * 20100309		Prototype built. 
 * Edmond
 * 
 */

import javaclient2.Position2DInterface;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PosiThread implements Runnable {
	Position2DInterface posi;
	ArrayList<Point2D.Float> prePosi;
	int arrayLength = 2;

	public PosiThread(Position2DInterface posi, ArrayList<Point2D.Float> prePosi) {
		this.posi = posi;
		this.prePosi = prePosi;
	}

	public void run() {
		while (true) {
			if(prePosi.size()>arrayLength){
				prePosi.remove(0);
			}
			prePosi.add(new Point2D.Float(posi.getX(),posi.getY()));
			try {Thread.sleep(6000);} catch (Exception e) {}
		}
	}


}
