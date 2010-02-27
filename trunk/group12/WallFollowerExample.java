/*
 *  Player Java Client 2 Examples - WallFollowerExample.java
 *  Copyright (C) 2006 Radu Bogdan Rusu
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * $Id: WallFollowerExample.java,v 1.1 2005/11/27 19:10:01 rusu Exp $
 *
 */



import java.text.NumberFormat;

import javaclient2.PlayerClient;
import javaclient2.PlayerException;
import javaclient2.Position2DInterface;
import javaclient2.SonarInterface;
import javaclient2.structures.PlayerConstants;

public class WallFollowerExample {
	
	 NumberFormat fmt = NumberFormat.getInstance ();
	
	// define minimum/maximum allowed values for the SONAR sensors
	 float SONAR_MIN_VALUE = 0.2f;
	 float SONAR_MAX_VALUE = 5.0f;
	
	// define the wall threshold
	 float MIN_WALL_THRESHOLD  = 0.5f;
	 float MAX_WALL_THRESHOLD  = 0.6f;
	
	// define the default translational and rotational speeds
	 

	 float DEF_X_SPEED   = 2.0f;
	 float DEF_YAW_SPEED = 0.5f;
	// float DEF_X_SPEED   = 0.2f;
	// float DEF_YAW_SPEED = 0.15f;
	 
	 float xSpeed = DEF_X_SPEED;
	float yawSpeed = 0;
	
	// array to hold the SONAR sensor values
	 float[] sonarValues;
	 float frontSide, leftSide, rightSide;
	
	public WallFollowerExample(){
		PlayerClient        robot = null;
		Position2DInterface posi  = null;
		SonarInterface      soni  = null;
		
		try {
			// Connect to the Player server and request access to Position and Sonar
			robot  = new PlayerClient ("localhost", 6665);
			posi = robot.requestInterfacePosition2D (0, PlayerConstants.PLAYER_OPEN_MODE);
			soni = robot.requestInterfaceSonar      (0, PlayerConstants.PLAYER_OPEN_MODE);
		} catch (PlayerException e) {
			System.err.println ("WallFollowerExample: > Error connecting to Player: ");
			System.err.println ("    [ " + e.toString() + " ]");
			System.exit (1);
		}
		
		robot.runThreaded (-1, -1);
		
		// Go ahead and find a wall and align to it on the robot's left side
		getWall (posi, soni);
		
		while (true) {
			// get all SONAR values and perform the necessary adjustments
			getSonars (soni);
			
			/*
			// by default, just move in front
			xSpeed   = DEF_X_SPEED;
			yawSpeed = 0;
			
			*/
			//2close to the wall with right side
			if(rightSide<SONAR_MIN_VALUE || leftSide<SONAR_MIN_VALUE || frontSide<SONAR_MIN_VALUE){
				if (rightSide == Math.min(Math.min(rightSide, leftSide),frontSide))
					yawSpeed = DEF_YAW_SPEED * 2;
				else
					yawSpeed = - DEF_YAW_SPEED * 2;
				if (frontSide>=MIN_WALL_THRESHOLD)
					xSpeed   = 0.50f;
				else
					xSpeed   = -0.50f;
					
				
					System.out.println("2close 2 collide");
					System.out.println ("Left side : [" + leftSide + "], Front side : [" + frontSide +"], Right side: [" + rightSide +"], xSpeed : [" + xSpeed + "], yawSpeed : [" + yawSpeed + "]\n");
					posi.setSpeed(xSpeed, yawSpeed);
					try{
						Thread.sleep(500);
					}catch (Exception e){
						
					}
					continue;
			}		
			/*
			if(Math.max(sonarValues[2], sonarValues[4])>=SONAR_MAX_VALUE-1 && Math.max(sonarValues[3], sonarValues[5])>=SONAR_MAX_VALUE-1){
				posi.setSpeed(0, 0);
				System.out.println("Junction found");
				try{
					Thread.sleep(1000);
				}catch (Exception e){
					
				}
				continue;
			}
			*/
			
			// if we're getting too close to the wall with the front side...
			if (frontSide < MAX_WALL_THRESHOLD) {
				// back up a little bit if we're bumping in front
				xSpeed   = -0.50f;
				yawSpeed = - DEF_YAW_SPEED * 4;
				System.out.println("2close with front");
			}else
				// if we're getting too close to the wall with the left side...
				if (leftSide < MIN_WALL_THRESHOLD) {
					if (yawSpeed <= 0.01)
						yawSpeed = -0.5f;
					xSpeed   = DEF_X_SPEED / 2;
					yawSpeed = DEF_YAW_SPEED;
					//yawSpeed = - yawSpeed/3;
					System.out.println("2close with left");
				}
				else
					if(leftSide >= MIN_WALL_THRESHOLD && leftSide <= MAX_WALL_THRESHOLD){
						xSpeed = DEF_X_SPEED*2;
						yawSpeed = 0;
						System.out.println("straight line");
					}else
					// if we're getting too far away from the wall with the left side...
					if (leftSide > MAX_WALL_THRESHOLD) {
						if (xSpeed <= 0.5)
							xSpeed = DEF_X_SPEED*1.5f;
						if (yawSpeed <= 0.5)
							yawSpeed = 3.0f;
						// move slower at corners
						xSpeed   = leftSide+1;
						//xSpeed   = xSpeed-0.5f;
						yawSpeed = leftSide*3f;
						//yawSpeed = yawSpeed*0.7f;
					//	yawSpeed = yawSpeed*1.2f;
						System.out.println("2far with left");
					}

			// Move the robot
			posi.setSpeed (xSpeed, yawSpeed);
			System.out.println ("Left side : [" + leftSide + "], Front side : [" + frontSide +"], Right side: [" + rightSide +"], xSpeed : [" + xSpeed + "], yawSpeed : [" + yawSpeed + "]\n");
			try { Thread.sleep (50); } catch (Exception e) { }
			
		}
	}
	public static void main(String[] args){
		new WallFollowerExample();
	}
	
	 void getWall (Position2DInterface posi, SonarInterface soni) {
		// get all SONAR values and perform the necessary adjustments
		getSonars (soni);
		
		// if the robot is in open space, go ahead until it "sees" the wall
		while ((leftSide > MAX_WALL_THRESHOLD) && 
				(frontSide > MAX_WALL_THRESHOLD)) {
			posi.setSpeed (DEF_X_SPEED, 0);
			try { Thread.sleep (100); } catch (Exception e) { }
			getSonars (soni);
		}
		
		float previousLeftSide = sonarValues[2];
		
		// rotate until we get a smaller value in sonar 0 
		while (sonarValues[2] <= previousLeftSide) {
			
			// rotate more if we're almost bumping in front
			if (Math.min (leftSide, frontSide) == frontSide)
				yawSpeed = -DEF_YAW_SPEED * 3;
			else
				yawSpeed = -DEF_YAW_SPEED;
			
			// Move the robot
			posi.setSpeed (0, yawSpeed);
			try { Thread.sleep (100); } catch (Exception e) { }
			
			getSonars (soni);
		}
		posi.setSpeed (0, 0);
	}
	
	 void getSonars (SonarInterface soni) {
		while (!soni.isDataReady ());
		sonarValues = soni.getData ().getRanges ();
		/*
		// ignore erroneous readings/keep interval [SONAR_MIN_VALUE; SONAR_MAX_VALUE]
		for (int i = 0; i < soni.getData ().getRanges_count (); i++)
			if (sonarValues[i] < SONAR_MIN_VALUE)
				sonarValues[i] = SONAR_MIN_VALUE;
			else
				if (sonarValues[i] > SONAR_MAX_VALUE)
					sonarValues[i] = SONAR_MAX_VALUE;
		*/
		leftSide = Math.min (Math.min (sonarValues[2], sonarValues [4]), sonarValues [4]);
		rightSide = Math.min (Math.min (sonarValues[3], sonarValues [5]), sonarValues [1]);
		frontSide = Math.min (sonarValues [6], sonarValues [7]);
	}
}