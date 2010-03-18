/*
 *  Group 12 Main robot
 *  Author: Edmond
 *  
 *  20100226	Prototype built, simple wall following program.
 *  
 *  
 *  20100228	Modified basic behaviours
 *  		
 *  			Problem in entering small entrance
 *  
 *  
 *  20100305	Grouped all basic behaviours in separated methods
 *  
 *  
 *  20100308	Added methods to detect and react in narrow road
 *  	
 *  
 *  20100310	Added PosiThread to record robot position and reaction method
 *  			robot get stuck
 *  
 *  
 *  20100312	Entering small entrance problem solved by
 *  			added enter entrance logic to detect and enter entrance
 *  			
 *  			Problems in exiting small room and dead cycle may form 
 *  			if robot missed 1 more entrance.
 *  
 *  
 *  20100313	Added laserValues to detect the entrance, modified robotStuck
 *  		 
 *  20100316	Added Gary Blobs finder methods
 *  
 *  20100317	Added ArrayList to stored previous visited points, solving dead cycle problem. 
 *  
 *  20100318	Modified Gary object avoidance to avoid hard coding turning rate and time.
 *
 */

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javaclient2.BlobfinderInterface;
import javaclient2.FiducialInterface;
import javaclient2.PlayerClient;
import javaclient2.PlayerException;
import javaclient2.Position2DInterface;
import javaclient2.SonarInterface;
import javaclient2.LaserInterface;
import javaclient2.structures.PlayerConstants;
import javaclient2.structures.PlayerPoint2d;
import javaclient2.structures.blobfinder.PlayerBlobfinderBlob;

public class WallFollowerExample {

	// define minimum/maximum allowed values for the SONAR sensors
	float sonarMin = 0.2f;
	float sonarMax = 2.0f;

	// define the wall threshold
	float wallMin = 0.4f;
	float wallMax = 0.5f;

	// define the default translational and rotational speeds

	float givenSpeed = 2.0f;
	float givenTurn = 0.5f;

	float rbtSpeed = givenSpeed;
	float rbtTurn = 0;

	// array to hold the SONAR sensor values
	float[] sonarValues;
	float[] laserValues;
	PlayerBlobfinderBlob blobsValues[];

	float frontSide, leftSide, rightSide;

	String preBehaviour = "";
	long tmpStartTime;
	long entranceStartTime = 0;
	long stuckStartTime = 0;

	boolean entrance;
	boolean skipEntrance;
	boolean flag = false;

	Point2D.Float currentPosi = new Point2D.Float();

	Thread posiThd, mapThd;
	ArrayList<Point2D.Float> prePosi = new ArrayList<Point2D.Float>();
	ArrayList<Point2D.Float> stuckPosi = new ArrayList<Point2D.Float>();
	ArrayList<Point2D.Float> pathVisited = new ArrayList<Point2D.Float>();

	PlayerClient rbt;
	Position2DInterface posi;
	SonarInterface sonar;
	FiducialInterface fid;
	LaserInterface las;
	BlobfinderInterface blobf;

	public WallFollowerExample() {

		try {
			// Connect to the Player server and request access to Position and
			// Sonar
			rbt = new PlayerClient("localhost", 6665);
			posi = rbt.requestInterfacePosition2D(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			sonar = rbt.requestInterfaceSonar(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			fid = rbt.requestInterfaceFiducial(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			las = rbt
					.requestInterfaceLaser(0, PlayerConstants.PLAYER_OPEN_MODE);
			blobf = rbt.requestInterfaceBlobfinder(0,
					PlayerConstants.PLAYER_OPEN_MODE);

		} catch (PlayerException e) {
			System.err
					.println("WallFollowerExample: > Error connecting to Player: ");
			System.err.println("    [ " + e.toString() + " ]");
			System.exit(1);
		}

		rbt.runThreaded(-1, -1);

		getSonars();
		posiThd = new Thread(new PosiThread(posi, prePosi));
		// mapThd = new Thread(new WorkingMapper(rbt,posi,las));
		posiThd.start();
		// mapThd.start();

		while (true) {
			// get all SONAR values and perform the necessary adjustments
			getSonars();

			// repeated Path
			if (repeatedPath()) {
				System.out.println("PATH REPEATED");
				skipEntrance = true;
			}

			if (isStuck()) {
				robotStuck();
			} else

			if (iswallblocked()) {
				wallblockedturn();
			} else

			if (isNarrow()) {
				robotNarrow();
			} else

			// 2close to the wall with right side
			if (rightSide < sonarMin || leftSide < sonarMin
					|| frontSide < sonarMin) {
				robotCollide();
			} else

			// if we're getting too close to the wall with the front side...
			if (frontSide < wallMax) {
				robotCloseFront();
			} else

			// if we're getting too close to the wall with the left side...
			if (leftSide < wallMin) {
				robotCloseLeft();
			} else

			if (leftSide >= wallMin && leftSide <= wallMax) {
				robotStraight();
			} else

			// if we're getting too far away from the wall with the left side...
			if (leftSide > wallMax && laserValues[175] <= sonarMax - 0.5) {
				robotFarLeft();
			} else

			if (laserValues[175] > sonarMax - 0.5f) {// if (sonarValues[0] >
				// sonarMax - 0.5f) {
				System.out.println("entrance dif time: "
						+ (System.currentTimeMillis() - entranceStartTime));
				if (System.currentTimeMillis() - entranceStartTime < 8000) {
					if (leftSide >= 4 && rightSide >= 4 && rightSide >= 4)
						robotStraight();
					else
						robotFarLeft();
					stopThread(100);
					continue;
				}

				// repeated path skip 1 entrance
				if (skipEntrance) {
					System.out.println("entrance skipped!");
					robotStraight();
					stopThread(1000);
					skipEntrance = false;
					continue;
				}

				entrance = false;
				System.out.println("test entrance");
				posi.setSpeed(1, 0);
				entranceStartTime = System.currentTimeMillis();
				while (System.currentTimeMillis() - entranceStartTime < 2000) {
					System.out.println("entrance dif time: "
							+ (System.currentTimeMillis() - entranceStartTime));
					getSonars();
					if (laserValues[175] < sonarMax || frontSide < wallMin) {
						System.out.println("entrance");
						posi.setSpeed(-1, 0);
						if (!((System.currentTimeMillis() - entranceStartTime) <= 200)) {
							stopThread((System.currentTimeMillis() - entranceStartTime) / 2);
						}
						posi.setSpeed(0, 2.7f);
						stopThread(1000);
						posi.setSpeed(1, 0);
						stopThread((System.currentTimeMillis() - entranceStartTime) / 2);
						getSonars();
						if (leftSide + rightSide <= 2 * wallMax)

							System.out.println("Real entrance");
						entrance = true;
						robotFarLeft();
						stopThread(100);
						break;
					}
				}
				// not an entrance
				if (!entrance) {
					posi.setSpeed(-1, 0);
					System.out.println(System.currentTimeMillis()
							- entranceStartTime);
					stopThread((System.currentTimeMillis() - entranceStartTime));
					robotFarLeft();
				}
				if (prePosi.size() >= 2)
					prePosi.remove(prePosi.size() - 1);
			} else {
				System.out.println("else");
				robotStraight();
			}

			// Move the rbt
			posi.setSpeed(rbtSpeed, rbtTurn);
			System.out.println("Left side : [" + leftSide + "], Front side : ["
					+ frontSide + "], Right side: [" + rightSide
					+ "], rbtSpeed : [" + rbtSpeed + "], rbtTurn : [" + rbtTurn
					+ "]\n");
			currentPosi.setLocation(posi.getX(), posi.getY());
			pathVisited.add(new Point2D.Float((float) posi.getX(), (float) posi
					.getY()));
			System.out.println("pathVisited size: " + pathVisited.size());
			System.out.println("X: " + round1dp(posi.getX()) + " Y: "
					+ round1dp(posi.getY()));
			stopThread(200);
		}
	}

	public static void main(String[] args) {
		new WallFollowerExample();
	}

	public void getWall() {
		// get all SONAR values and perform the necessary adjustments
		getSonars();

		// if the robot is in open space, go ahead until it "sees" the wall
		while ((leftSide > wallMax) && (frontSide > wallMax)) {
			posi.setSpeed(givenSpeed, 0);
			stopThread(100);
			getSonars();
		}

		float previousLeftSide = sonarValues[0];

		// rotate until we get a smaller value in sonar 0
		while (sonarValues[0] <= previousLeftSide) {
			previousLeftSide = sonarValues[0];

			// rotate more if we're almost bumping in front
			if (Math.min(leftSide, frontSide) == frontSide)
				rbtTurn = -givenTurn * 3;
			else
				rbtTurn = -givenTurn;

			// Move the robot
			posi.setSpeed(0, rbtTurn);
			stopThread(100);
			getSonars();
		}
		robotStop();

	}

	public void getSonars() {
		// get sensors+blobfinder data
		while (!sonar.isDataReady())
			;
		sonarValues = sonar.getData().getRanges();

		while (!las.isDataReady())
			;
		laserValues = las.getData().getRanges();

		while (!blobf.isDataReady())
			;
		blobsValues = blobf.getData().getBlobs();

		leftSide = Math.min(sonarValues[0], sonarValues[1]);
		rightSide = Math.min(sonarValues[7], sonarValues[6]);
		frontSide = Math.min(sonarValues[3], sonarValues[4]);
	}

	/********************************************
	 * Robot moving behaviour start
	 ******************************************/
	public boolean isStuck() {
		if (prePosi.size() > 1) {
			if (Math.abs(prePosi.get(prePosi.size() - 1).getX()
					- prePosi.get(prePosi.size() - 2).getX()) <= 0.5) {
				if (Math.abs(prePosi.get(prePosi.size() - 1).getY()
						- prePosi.get(prePosi.size() - 2).getY()) <= 0.5)
					if (System.currentTimeMillis() - stuckStartTime > 5000) {
						stuckStartTime = System.currentTimeMillis();
						return true;
					}
			}
		}
		return false;
	}

	public void robotStuck() {
		System.out.println("\nrbt stuck");
		System.out.println("posX: " + prePosi.get(prePosi.size() - 1).getX()
				+ "\npre posX " + prePosi.get(prePosi.size() - 2).getX());
		System.out.println("posY: " + prePosi.get(prePosi.size() - 1).getY()
				+ "\npre posY " + prePosi.get(prePosi.size() - 2).getY());
		System.out.println("\nDifference");
		System.out.println(Math.abs(prePosi.get(prePosi.size() - 1).getX()
				- prePosi.get(prePosi.size() - 2).getX()));
		System.out.println(Math.abs(prePosi.get(prePosi.size() - 1).getY()
				- prePosi.get(prePosi.size() - 2).getY()));
		prePosi.remove(0);
		System.out.println("\nLeftside: " + leftSide + " FrontSide: "
				+ frontSide + " RightSide: " + rightSide);

		stuckPosi.add(prePosi.get(prePosi.size() - 1));

		posi.setSpeed(-1f, 0);
		stopThread(2000);

		if (leftSide >= wallMax && frontSide >= wallMax && rightSide >= wallMax)
			getWall();
		else

		if (isNarrow()) {
			// posi.setSpeed(1, 3.5f);
			// /stopThread(1000);
			posi.setSpeed(1, 0);
			for (int i = 0; i < 3; i++) {
				stopThread(2000);
				getSonars();
				if (sonarValues[1] > sonarMin || sonarValues[6] > sonarMin) {
					if (sonarValues[1] > sonarValues[6]) {
						posi.setSpeed(1, 2);
					} else {
						posi.setSpeed(1, -2);
					}
					stopThread(2000);
					break;
				}
			}

		} else {
			if (leftSide == Math.max(leftSide, Math.max(rightSide, frontSide))) {
				// if (leftSide > 2)
				posi.setSpeed(1, givenTurn * 2);
				// else
				// posi.setSpeed(1, leftSide * 3);
			} else if (rightSide == Math.max(rightSide, frontSide)) {
				// if (rightSide > 2)
				posi.setSpeed(1, -givenTurn * 2);
				// else
				// posi.setSpeed(1, -rightSide * 3);
			} else {
				if ((frontSide - rightSide) > (frontSide - leftSide))
					posi.setSpeed(1, -givenTurn / 2);
				posi.setSpeed(1, givenTurn / 2);
			}
			stopThread(1500);
			if (prePosi.size() >= 2)
				prePosi.remove(prePosi.size() - 1);
		}
	}

	public boolean isNarrow() {
		if (sonarValues[0] + sonarValues[7] < 0.6 && frontSide > wallMax) {
			return true;
		}
		return false;
	}

	public void robotNarrow() {
		long tmpStartTime = System.currentTimeMillis();
		System.out.println("narrow road");

		while (frontSide > sonarMin && leftSide != rightSide && !isStuck()
				&& isNarrow()) {
			this.getSonars();
			if (leftSide == rightSide)
				posi.setSpeed(1f, 0);
			else if (leftSide > rightSide)
				posi.setSpeed(1f, 0.2f);
			else
				posi.setSpeed(1f, -0.2f);
			if (System.currentTimeMillis() - tmpStartTime > 20000 || isStuck())
				break;
		}
		if (frontSide <= sonarMin) {
			posi.setSpeed(0, (float) (4 * Math.PI));
		}
		stopThread(500);
		getSonars();
		System.out.println(isStuck());
		if (isStuck())
			robotStuck();
	}

	public void robotCollide() {
		if (rightSide == Math.min(Math.min(rightSide, leftSide), frontSide)) {
			rbtTurn = givenTurn;
			preBehaviour = "turnLeft";
		} else {
			rbtTurn = -givenTurn;
			preBehaviour = "turnRight";
		}
		if (frontSide >= wallMin) {
			rbtSpeed = 1f;
		} else {
			rbtSpeed = -1f;
		}
		System.out.println("2close 2 collide");
		System.out.println("Left side : [" + leftSide + "], Front side : ["
				+ frontSide + "], Right side: [" + rightSide
				+ "], rbtSpeed : [" + rbtSpeed + "], rbtTurn : [" + rbtTurn
				+ "]\n");
	}

	public void robotCloseFront() {
		// back up a little bit if we're bumping in front
		rbtSpeed = -0.50f;
		if (leftSide > sonarMax)
			rbtTurn = leftSide;
		else
			rbtTurn = -givenTurn * 4;
		System.out.println("2close with front");
	}

	public void robotCloseLeft() {
		rbtSpeed = givenSpeed;
		rbtTurn = -givenTurn / 10f;
		System.out.println("2close with left");
		preBehaviour = "turnRight";
	}

	public void robotStraight() {
		rbtSpeed = givenSpeed;
		if (preBehaviour.equals("turnRight"))
			rbtTurn = givenTurn / 4f;
		else
			rbtTurn = -givenTurn / 4f;

		posi.setSpeed(rbtSpeed, rbtTurn);
		System.out.println("straight line");
	}

	public void robotFarLeft() {
		// move slower at corners
		rbtSpeed = 1.2f;
		if (leftSide < 1.5f)
			leftSide = 1.5f;
		if (leftSide > 3f)
			leftSide = 3f;
		rbtTurn = leftSide;
		System.out.println("2far with left");
		preBehaviour = "turnLeft";
		posi.setSpeed(rbtSpeed, rbtTurn);
	}

	public void robotStop() {
		posi.setSpeed(0, 0);
	}

	public void stopThread(long milliSecond) {
		try {
			Thread.sleep(milliSecond);
		} catch (Exception e) {
		}
	}

	public void stopThread(long milliSecond, boolean b) {
		for (int i = 0; i < 10; i++) {
			if(!b)
				break;
			try {
				Thread.sleep(milliSecond / 10);
			} catch (Exception e) {
			}
		}
	}

	public boolean repeatedPath() {
		if (pathVisited.size() > 200)
			for (int i = 0; i < pathVisited.size() - 100; i++) {
				if (round1dp(currentPosi.getX()) == round1dp(pathVisited.get(i)
						.getX())
						&& round1dp(currentPosi.getY()) == round1dp(pathVisited
								.get(i).getY())) {
					return true;
				}
			}
		return false;
	}

	/***************************************
	 * Robot moving behaviour end
	 ***************************************/

	/**************************************
	 * Blod finder behaviour start
	 **************************************/
	// checks if object blocking the robots wall is an object
	public boolean isOccupied() {

		if (blobsValues.length > 0) {

			for (int i = 0; i < blobsValues.length; i++) {
				// if (blobsValues[i].getColor() == 0)
				// return false;

				// if a blob is in close range, return true
				System.out.println("range is " + blobsValues[i].getRange() / 10
						+ " color is " + blobsValues[i].getColor());
				if ((int) (blobsValues[i].getRange() / 10) < 120)
					return true;

			}

			/*
			 * while(!pstn.isDataReady());
			 * 
			 * posx = (int)pstn.getData().getPos().getPx(); posy =
			 * (int)pstn.getData().getPos().getPy();
			 * 
			 * checkx = (int)Math.cos(pstn.getYaw()-10) + posx; checky =
			 * (int)Math.sin(pstn.getYaw()-10) + posy;
			 * 
			 * checkx = (int)Math.cos(robpos.getYaw()-10)lsrVals[90] + posx;
			 * checky = (int)Math.sin(robpos.getYaw()-10)&lsrVals[90] + posy;
			 * 
			 * //if the location has an object, then return true
			 * if(objectposgrid[checkx][checky]>1){return true;} else return
			 * false;
			 */
		}
		return false;
	}

	// checks if the wall if blocked
	public boolean iswallblocked() {

		// if rightside of laser against wall, and object blocking wall
		for (int i = 129; i >= 60; i--) {
			if (laserValues[i] < sonarMax && isOccupied()) {
				return true;
			}
		}
		return false;
	}

	// if object is against or near wall, turn away
	public void wallblockedturn() {

		// stop and turn right
		System.out.println("WALL BLOCKED, TURNING");
		// posi.setSpeed(0.5f, -givenTurn*3);
		rbtSpeed = 0.5f;
		rbtTurn = -givenTurn * 3;

		/*
		 * posi.setSpeed(0, -1.57f); try { Thread.sleep(1000); } catch
		 * (Exception e) { } posi.setSpeed(1, 0); try { Thread.sleep(1000); }
		 * catch (Exception e) { }
		 */

	}

	/**************************************
	 * Blod finder behaviour end
	 **************************************/

	// general functions
	double round1dp(double f) {
		DecimalFormat form = new DecimalFormat("#.#");
		return Double.valueOf(form.format(f));
	}
}
