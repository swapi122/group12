/*
 *  Group 12 Mapping
 *  Author: Gary
 *  
 *  20100315 	Code currently follows the robots track as it navigates the map
 *  
 *  20100316	Edited by Edmond for use in a thread
 *  
 *  20100317	Now maps walls, but still exhibiting some inaccuracies
 *
 */

import static name.audet.samuel.javacv.jna.cxcore.CV_RGB;
import static name.audet.samuel.javacv.jna.cxcore.cvLine;
import static name.audet.samuel.javacv.jna.cxcore.v10.*;
import static name.audet.samuel.javacv.jna.cv.v10.*;
import static name.audet.samuel.javacv.jna.highgui.v10.*;
import static name.audet.samuel.javacv.jna.cvaux.v10.*;
import name.audet.samuel.javacv.CanvasFrame;
import javaclient2.*;
import javaclient2.structures.PlayerConstants;

public class WorkingMapperwthread1 implements Runnable {

	// Variables

	/*
	 * int degs,Rbtxposx,Rbtxposy; float radians, ang,
	 */

	// robots angle
	float robotxangle, sinangle, cosangle;

	// laservalue[179] angle
	float wallmapper;

	// Window dimensions
	int width = 600, height = 300;

	// Center of map x,y variables
	int xcenter = (width / 2);
	int ycenter = (height / 2);

	// Point 1 for mapping walls, point 2 for mapping objects
	CvPoint point1;
	CvPoint point2;

	CvScalar grey, walls;
	float[] laservals;

	// Connect to the Player server and request access to Position and Sonar
	PlayerClient robotx;
	Position2DInterface ppd;
	LaserInterface laser;

	public WorkingMapperwthread1(PlayerClient robotx, Position2DInterface ppd,
			LaserInterface laser) {
		this.robotx = robotx;
		this.ppd = ppd;
		this.laser = laser;

	}

	public void run() {

		// Image creation
		IplImage image = cvCreateImage(cvSize(width, height), 8, 3);

		point1 = new CvPoint();
		point2 = new CvPoint();

		// Create the canvas display window, full screen true, window name set
		CanvasFrame xff = new CanvasFrame(false, "map");
		// Make it visible
		xff.setVisible(true);

		// Set the scalar print colours
		walls = CV_RGB(0, 0, 0); // Indicates the walls
		grey = CV_RGB(92, 92, 92); // To indicate Obstacles

		// run threaded
		// robotx.runThreaded(-1,-1);

		// set robot speed
		// ppd.setSpeed(1,0);

		// Set the image background
		cvSet(image, grey.byValue());

		// initial point for wall mapper
		point1.x = xcenter;
		point1.y = ycenter;

		while (true) {

			while (!laser.isDataReady())
				;

			//edmond edit here
			//if (laser.getData().getRanges()[179] < 4.0f) {
				// Update data from the robot
				robotx.readAll();
				// Update image
				cvSaveImage("group12map.bmp", image);
				// Update again
				robotx.readAll();

				/*
				 * laservals = laser.getData().getRanges();
				 * 
				 * if(laservals[179] < 4.0f){
				 * 
				 * robotxangle = ppd.getData().getPos().getPa(); wallmapper =
				 * robotxangle + 90.0f;//Obtain the angle relative to the robot
				 * 
				 * Rbtxposx = roundup(ppd.getData().getPos().getPx());
				 * //Position x relative to robot Rbtxposy =
				 * roundup(ppd.getData().getPos().getPy()); //Position y
				 * relative to robot point2.y = (ycenter-Rbtxposy);
				 * //Co-ordinated global y of the robot point2.x =
				 * (xcenter+Rbtxposx); //Co-ordinated global x of the robot
				 */

				while (!ppd.isDataReady())
					;

				robotxangle = ppd.getData().getPos().getPa();
				
				/*
				 * cosangle = (float)Math.cos((float)robotxangle *
				 * (180/Math.PI)); sinangle = (float)Math.sin((float)robotxangle
				 * * (180/Math.PI));
				 * 
				 * point1.y = roundup((cosangle *
				 * laser.getData().getRanges()[179]) +
				 * ppd.getData().getPos().getPy()); point1.x = roundup((sinangle
				 * * laser.getData().getRanges()[179]) +
				 * ppd.getData().getPos().getPx());
				 */

				for(int i=0; i<180; i++){
				
					
				point1.x=roundup((float)(laser.getData().getRanges()[i] * Math.cos(Math.toRadians(i))+ppd.getData().getPos().getPx()));
				point1.y=roundup((float)(laser.getData().getRanges()[i] * Math.sin(Math.toRadians(i))+ppd.getData().getPos().getPy()));
				System.out.println("X COORDINATE IS: " + point1.x
						+ " Y COODINATE IS " + point1.y
						+ " Robot position is: " + " x is: "
						+ roundup(ppd.getData().getPos().getPx()) + " y is "
						+ roundup(ppd.getData().getPos().getPy())
						+ " angle is " + ppd.getData().getPos().getPa());

				// marks the object in the map
				cvLine(image, point1.byValue(), point1.byValue(), walls
						.byValue(), 5, 4, 0);

				// Shows the result of the map to the screen

				xff.showImage(image);

				xff.waitKey(1);
				}
			//}

		}

	}

	/*
	 * //Conversion of angles to radians static float angtorads(int angle){
	 * 
	 * float rads = (angle*(float)Math.PI)/180; return rads;
	 * 
	 * }
	 */

	// round up float numbers for the grid, and precision mapping
	static int roundup(float num) {
		int temp = (int) (num * 100);
		temp = temp % 10;
		;
		if (temp >= 5)
			return (int) ((num * 10) + 1);
		else
			return (int) (num * 10);
	}

}
