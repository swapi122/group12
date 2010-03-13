/*
 *  Group 12 Main robot
 *  20100226	Prototype built, simple wall following program.
 *  Edmond
 *  
 *  20100228	Modified basic behaviours
 *  Edmond		
 *  			Problem in entering small entrance
 *  
 *  
 *  20100305	Grouped all basic behaviours in separated methods
 *  Edmond
 *  
 *  
 *  20100308	Added methods to detect and react in narrow road
 *  Edmond	
 *  
 *  
 *  20100310	Added PosiThread to record robot position and reaction method
 *  Edmond		robot get stuck
 *  
 *  
 *  20100312	Entering small entrance problem solved by
 *  Edmond		added enter entrance logic to detect and enter entrance
 *  			
 *  			Problems in exiting small room and dead cycle may form 
 *  			if robot missed 1 more entrance.
 *  
 *  
 *  20100313	Added laserValues to detect the entrance, modified robotStuck
 *  Edmond		 
 *  
 *  
 *
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javaclient2.FiducialInterface;
import javaclient2.PlayerClient;
import javaclient2.PlayerException;
import javaclient2.Position2DInterface;
import javaclient2.SonarInterface;
import javaclient2.LaserInterface;
import javaclient2.structures.PlayerConstants;

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

	float frontSide, leftSide, rightSide;

	String preBehaviour = "";
	long tmpStartTime;
	long entranceStartTime = 0;
	long stuckStartTime = 0;
	
	boolean entrance;
	boolean flag = false;

	Thread posiThd;
	ArrayList<Point2D.Float> prePosi = new ArrayList<Point2D.Float>();

	PlayerClient rbt;
	Position2DInterface posi;
	SonarInterface sonar;
	FiducialInterface fid;
	LaserInterface las;

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

		} catch (PlayerException e) {
			System.err
					.println("WallFollowerExample: > Error connecting to Player: ");
			System.err.println("    [ " + e.toString() + " ]");
			System.exit(1);
		}

		rbt.runThreaded(-1, -1);

		// Go ahead and find a wall and align to it on the rbt's left side
		// getWall (posi, sonar);

		posiThd = new Thread(new PosiThread(posi, prePosi));
		getSonars();
		posiThd.start();

		while (true) {
			// get all SONAR values and perform the necessary adjustments
			getSonars();

			if (isStuck()) {
				robotStuck();
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
			if (leftSide > wallMax && laserValues[170] < sonarMax) {// sonarValues[0]
				// <
				// this.sonarMax)
				// {
				robotFarLeft();
			} else

			if (laserValues[170] > sonarMax - 0.5f) {// if (sonarValues[0] >
				// sonarMax - 0.5f) {
				System.out.println("entrance dif time: "
						+ (System.currentTimeMillis() - entranceStartTime));
				if (System.currentTimeMillis() - entranceStartTime < 8000) {
					if (leftSide >= sonarMax && rightSide >= sonarMax
							&& rightSide >= sonarMax)
						robotStraight();
					else
						robotFarLeft();
					continue;
				}

				entrance = false;
				System.out.println("test entrance");
				posi.setSpeed(1, 0);
				entranceStartTime = System.currentTimeMillis();
				while (System.currentTimeMillis() - entranceStartTime < 2300) {
					getSonars();
					if (laserValues[170] < sonarMax || frontSide < wallMin) {
						System.out.println("entrance");
						posi.setSpeed(-1, 0);
						if (!((System.currentTimeMillis() - entranceStartTime) <= 700)) {
							try {
								Thread
										.sleep((System.currentTimeMillis() - entranceStartTime) / 2);
							} catch (Exception e) {
							}
						}
						posi.setSpeed(0, 2.6f);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
						}
						posi.setSpeed(1, 0);
						try {
							Thread
									.sleep((System.currentTimeMillis() - entranceStartTime) / 2);
						} catch (Exception e) {
						}
						getSonars();
						if (isNarrow())
							System.out.println("Real entrance");
						entrance = true;
						break;
					}
				}
				// not an entrance
				if (!entrance) {
					posi.setSpeed(-1, 0);
					System.out.println(System.currentTimeMillis()
							- entranceStartTime);
					try {
						Thread
								.sleep((System.currentTimeMillis() - entranceStartTime) / 2);
					} catch (Exception e) {
					}
					posi.setSpeed(0, 0);
					robotFarLeft();
				}
			}

			else {
				System.out.println("else");
				robotStraight();
			}

			// Move the rbt
			posi.setSpeed(rbtSpeed, rbtTurn);
			System.out.println("Left side : [" + leftSide + "], Front side : ["
					+ frontSide + "], Right side: [" + rightSide
					+ "], rbtSpeed : [" + rbtSpeed + "], rbtTurn : [" + rbtTurn
					+ "]\n");
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
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
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
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
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}

			getSonars();
		}
		posi.setSpeed(0, 0);

	}

	public void getSonars() {
		while (!sonar.isDataReady())
			;
		sonarValues = sonar.getData().getRanges();

		while (!las.isDataReady())
			;
		laserValues = las.getData().getRanges();

		leftSide = Math.min(Math.min(sonarValues[0], sonarValues[1]),
				sonarValues[0]);
		rightSide = Math.min(Math.min(sonarValues[7], sonarValues[6]),
				sonarValues[7]);
		frontSide = Math.min(sonarValues[3], sonarValues[4]);
	}

	/********************************************
	 * Robot moving behaviour start
	 ******************************************/
	public boolean isStuck() {
		if (prePosi.size() > 1) {
			if (Math.abs(prePosi.get(prePosi.size() - 1).getX()
					- prePosi.get(prePosi.size() - 2).getX()) <= 0.4) {
				if (Math.abs(prePosi.get(prePosi.size() - 1).getY()
						- prePosi.get(prePosi.size() - 2).getY()) <= 0.4)
					if(System.currentTimeMillis()-stuckStartTime >5000){
						stuckStartTime=System.currentTimeMillis();
						System.out.println(System.currentTimeMillis());
						System.out.println(stuckStartTime);
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

		posi.setSpeed(-1f, 0);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		if (leftSide >= sonarMax && frontSide >= sonarMax
				&& rightSide >= sonarMax)
			getWall();
		else

		if (isNarrow()) {
			posi.setSpeed(1, 3.5f);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			posi.setSpeed(1, 0);
			for (int i = 0; i < 3; i++) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				if (sonarValues[1] > sonarMin || sonarValues[6] > sonarMin) {
					if (sonarValues[1] > sonarValues[6]) {
						posi.setSpeed(1, givenTurn * 3);
					} else {
						posi.setSpeed(1, -givenTurn * 3);
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
					break;
				}
			}

		} else {
			if (leftSide == Math.max(leftSide, Math.max(rightSide, frontSide))) {
				if (leftSide > 2)
					posi.setSpeed(1, givenTurn * 3);
				else
					posi.setSpeed(1, leftSide * 3);
			} else if (rightSide == Math.max(rightSide, frontSide)) {
				if (rightSide > 2)
					posi.setSpeed(1, -givenTurn * 3);
				else
					posi.setSpeed(1, -rightSide * 3);
			} else {
				if ((frontSide - rightSide) > (frontSide - leftSide))
					posi.setSpeed(1, -givenTurn);
				posi.setSpeed(1, givenTurn);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}

	public boolean isNarrow() {
		if (sonarValues[0] + sonarValues[7] < 0.6 && frontSide > wallMin) {
			return true;
		}
		return false;
	}

	public void robotNarrow() {
		long tmpStartTime = System.currentTimeMillis();
		System.out.println("narrow road");
		while (leftSide != rightSide && isNarrow()) {
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
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

	public void robotCollide() {
		if (rightSide == Math.min(Math.min(rightSide, leftSide), frontSide)) {
			rbtTurn = givenTurn / 2f;
			preBehaviour = "turnLeft";
		} else {
			rbtTurn = -givenTurn / 2f;
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

		System.out.println("straight line");
	}

	public void robotFarLeft() {
		// move slower at corners
		rbtSpeed = givenSpeed / 2f;
		// rbtSpeed = rbtSpeed-0.5f;
		if (leftSide < 1f)
			leftSide = 1;
		if (leftSide > 2.5f)
			leftSide = 2.5f;
		rbtTurn = leftSide;
		// rbtTurn = rbtTurn*0.7f;
		// rbtTurn = rbtTurn*1.2f;
		System.out.println("2far with left");
		preBehaviour = "turnLeft";
		posi.setSpeed(rbtSpeed, rbtTurn);
	}
	/***************************************
	 * Robot moving behaviour end
	 ***************************************/
}