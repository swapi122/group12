import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.ArrayList;

import javaclient2.structures.*;
import javaclient2.*;

public class test {

	//
	
	// define stop/minimum/maximum allowed values for the SONAR sensors
	float sonarStop = 0.1f; //stop and reverse level
	float sonarMin = 0.5f; //wall follow level
	float sonarMid = 1.0f; //start action level
	float sonarMax = 5.0f;
	
	float wallMin = 0.5f;
	float wallMax = 0.8f;
	
	ArrayList<Float> nodeVisited; 
	
	// define the threshold (any value under this is considered an obstacle)
	//float SONAR_THRESHOLD = 0.5f;
	// define the wheel diameter (~example for a Pioneer 3 robot)
	//float WHEEL_DIAMETER = 24.0f;

	// define the default rotational speed in rad/s
	//float DEF_YAW_SPEED = 0.50f;

	// array to hold the SONAR sensor values
	float[] sonarValues;
	// translational/rotational speed
	
	//starting speed
	float speed = 1.0f;
	float rotation = 0;
	float leftSensors, rightSensors, midSensors;

	NumberFormat fmt = NumberFormat.getInstance();
	
	PlayerClient robot = null;
	Position2DInterface posi = null;
	SonarInterface soni = null;
	
	
	public test() {


		try {
			// Connect to the Player server and request access to Position and
			// Sonar
			robot = new PlayerClient("localhost", 6665);
			posi = robot.requestInterfacePosition2D(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			soni = robot.requestInterfaceSonar(0,
					PlayerConstants.PLAYER_OPEN_MODE);
		} catch (PlayerException e) {
			System.err
					.println("SpaceWandererExample: > Error connecting to Player: ");
			System.err.println("    [ " + e.toString() + " ]");
			
		}

		robot.runThreaded(-1, -1);

		while (true) {
			// get all SONAR values
			while (!soni.isDataReady())
				;
			sonarValues = soni.getData().getRanges();

			// ignore erroneous readings/keep interval [sonarMid;
			// sonarMax]
			/*for (int i = 0; i < soni.getData().getRanges_count(); i++)
				if (sonarValues[i] < sonarMid)
					sonarValues[i] = sonarMid;
				else if (sonarValues[i] > sonarMax)
					sonarValues[i] = sonarMax;
					*/
			
			
			
			/*
			System.out.println("\n"+sonarValues[2]);
			System.out.println(sonarValues[4]);
			System.out.println(sonarValues[0]);
			System.out.println(sonarValues[6]);
			System.out.println(sonarValues[7]);
			System.out.println(sonarValues[1]);
			System.out.println(sonarValues[5]);
			System.out.println(sonarValues[3]);
			
			try {
				Thread.sleep(10000);
				continue;
			} catch (Exception e) {
			}
			*/
			
			
			
			leftSensors = (sonarValues [4] + sonarValues [0]) / 2; // + sonarValues [2]) / 3;
			rightSensors = (sonarValues [1] + sonarValues [5]) / 2; //  + sonarValues [3] / 3;
			midSensors = (sonarValues[6] + sonarValues[7])/2;
			
			//System.out.println("LS: [" + this.leftSensors + "] RS: " + this.rightSensors + "] MS: ["+ this.midSensors+"]");
			//System.out.println(decodeSonars(soni));


			
			posi.setSpeed(speed, rotation);
			
			//this.checkCollision();
			
			//straight follow wall
			if (this.sonarValues[4]==wallMin){
				speed = 1.0f;
				rotation = 0;
				System.out.println("straight");
				continue;
			}
			
			//stop for junction
			if (this.sonarValues[4]==sonarMax){
				System.out.println("junction");
				posi.setSpeed(0, 0);
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
				}	
				continue;
			}
			
			//too close to wall
			if (leftSensors<wallMin){
				speed = 0.5f;
				rotation = -0.1f;
				System.out.println("2close");
				continue;
			}
				
			//too far from wall
			if (leftSensors>wallMax){
				speed = 0.5f;
				rotation = 0.1f;
				System.out.println("2far");
				continue;
			}
			
			/*
			//check junction
			if(this.leftSensors+this.rightSensors<3.0 && this.midSensors > 2.0){
				
				System.out.println("junction");
				speed = 0;
				rotation = 0;
				
				posi.setSpeed(speed, rotation);
				
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
				}
				
			}
			
			
			//turn left
			if (rightSensors <= sonarMid){
				posi.setSpeed(0.1f, 0.2f);
				if (sonarValues[3] <= sonarMin)
					continue;
			}
			
			if (leftSensors <= sonarMid
					&& !(rightSensors <= sonarMid)) {
				posi.setSpeed(0.1f, -0.2f);

				if (sonarValues[2] <= sonarMin)
					continue;
			}
			
			*/

			
			/*
			if (sonarValues[5] <= sonarMid) {
				posi.setSpeed(0.1f, 0.2f);

				if (sonarValues[7] == sonarMid)
					continue;
			}

			if (sonarValues[2] <= sonarMid
					&& !(sonarValues[5] <= sonarMid)) {
				posi.setSpeed(0.1f, -0.2f);

				if (sonarValues[0] == sonarMid)
					continue;
			}
			*/
			
			/*
			 * // read and average the sonar values on the left and right side
			 * leftSensors = (sonarValues [1] + sonarValues [2]) / 2; // +
			 * sonarValues [3]) / 3; rightSensors = (sonarValues [5] + sonarValues
			 * [6]) / 2; // + sonarValues [4]) / 3;
			 * 
			 * // use a divider for the velocities depending on your desired
			 * speed (mm/s, m/s, etc) leftSensors = leftSensors / 10; rightSensors =
			 * rightSensors / 10;
			 * 
			 * // calculate the translational and rotational velocities xspeed =
			 * (leftSensors + rightSensors) / 2; yawspeed = (float)((leftSensors -
			 * rightSensors) * (180 / Math.PI) / WHEEL_DIAMETER);
			 * 
			 * try { Thread.sleep (100); } catch (Exception e) { }
			 * 
			 * // if the path is clear on the left OR on the right, use
			 * {x,yaw}speed if (( (sonarValues [1] > SONAR_THRESHOLD) &&
			 * (sonarValues [2] > SONAR_THRESHOLD) && (sonarValues [3] >
			 * SONAR_THRESHOLD) ) || ( (sonarValues [4] > SONAR_THRESHOLD) &&
			 * (sonarValues [5] > SONAR_THRESHOLD) && (sonarValues [6] >
			 * SONAR_THRESHOLD) )) posi.setSpeed (xspeed, yawspeed); else // if
			 * we have obstacles in front (both left and right), rotate if
			 * (sonarValues [0] < sonarValues [7]) posi.setSpeed (0,
			 * -DEF_YAW_SPEED); else posi.setSpeed (0, DEF_YAW_SPEED);
			 */
		}
	}

	public static void main(String[] args) {
		new test();

	}

	// Misc routines for nice alignment of text on screen
	public String align(NumberFormat fmt, float n, int sp) {
		StringBuffer buf = new StringBuffer();
		FieldPosition fpos = new FieldPosition(NumberFormat.INTEGER_FIELD);
		fmt.format(n, buf, fpos);
		for (int i = 0; i < sp - fpos.getEndIndex(); ++i)
			buf.insert(0, ' ');
		return buf.toString();
	}

	public String decodeSonars(SonarInterface soni) {
		String out = "\nSonar vars: \n";
		for (int i = 0; i < soni.getData().getRanges_count(); i++) {
			out += " [" + align(fmt, i + 1, 2) + "] = "
					+ align(fmt, soni.getData().getRanges()[i], 5);
			if (((i + 1) % 8) == 0)
				out += "\n";
		}
		return out;
	}
	
	public void checkCollision(){
		System.out.println("checking collision");
		System.out.println(leftSensors);
		System.out.println(midSensors);
		System.out.println(rightSensors);
		if (leftSensors <= sonarStop) {
			posi.setSpeed(-0.5f, -0.5f);
			System.out.println("leftSensor collide");	
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
		if (rightSensors <= sonarStop) {
			posi.setSpeed(-0.5f, 0.5f);
			System.out.println("leftSensor collide");	
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
		
		/*
		if (sonarValues[5] <= sonarMid + 1.0f) {
			posi.setSpeed(0.1f, 0.2f);

			if (sonarValues[7] == sonarMid)
				continue;
		}

		if (sonarValues[2] <= sonarMid + 1.0f
				&& !(sonarValues[5] <= sonarMid + 1.0f)) {
			posi.setSpeed(0.1f, -0.2f);

			if (sonarValues[0] == sonarMid)
				continue;
		}
		*/

		
	}

}
