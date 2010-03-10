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



import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.util.ArrayList;

import javaclient2.FiducialInterface;
import javaclient2.PlayerClient;
import javaclient2.PlayerException;
import javaclient2.Position2DInterface;
import javaclient2.SonarInterface;
import javaclient2.structures.PlayerConstants;

public class WallFollowerExample {
	
	NumberFormat fmt = NumberFormat.getInstance ();
	
	// define minimum/maximum allowed values for the SONAR sensors
	float sonarMin = 0.2f;
	float sonarMax = 2.0f;
	
	// define the wall threshold
	float wallMin  = 0.5f;
	float wallMax  = 0.6f;
	
	// define the default translational and rotational speeds
	 

	float givenSpeed   = 2.0f;
	float givenTurn = 0.5f;
	// float givenSpeed   = 0.2f;
	// float givenTurn = 0.15f;
	 
	float rbtSpeed = givenSpeed;
	float rbtTurn = 0;
	
	// array to hold the SONAR sensor values
	float[] sonarValues;
	float frontSide, leftSide, rightSide;
	
	
	Thread posiThd;
	ArrayList<Point2D.Float> prePosi = new ArrayList<Point2D.Float>();
	
	PlayerClient        rbt = null;
	Position2DInterface posi  = null;
	SonarInterface      sonar  = null;
	FiducialInterface 	fid	  = null; 
	 
	public WallFollowerExample(){

		
		try {
			// Connect to the Player server and request access to Position and Sonar
			rbt  = new PlayerClient ("localhost", 6665);
			posi = rbt.requestInterfacePosition2D (0, PlayerConstants.PLAYER_OPEN_MODE);
			sonar = rbt.requestInterfaceSonar      (0, PlayerConstants.PLAYER_OPEN_MODE);
			fid = rbt.requestInterfaceFiducial(0, PlayerConstants.PLAYER_OPEN_MODE);
		} catch (PlayerException e) {
			System.err.println ("WallFollowerExample: > Error connecting to Player: ");
			System.err.println ("    [ " + e.toString() + " ]");
			System.exit (1);
		}
		
		rbt.runThreaded (-1, -1);
		
		// Go ahead and find a wall and align to it on the rbt's left side
		getWall (posi, sonar);
		
		posiThd = new Thread(new PosiThread(posi, prePosi));
		posiThd.start();
		
		
		while (true) {
			// get all SONAR values and perform the necessary adjustments
			getSonars (sonar);
			
			/*
			// by default, just move in front
			rbtSpeed   = givenSpeed;
			rbtTurn = 0;
			
			*/

			if(isStuck()){
				System.out.println("\nrbt stuck");
				System.out.println("posX: "+ prePosi.get(prePosi.size()-1).getX()+ "\npre posX "+ prePosi.get(prePosi.size()-2).getX());
				System.out.println("posY: "+ prePosi.get(prePosi.size()-1).getY()+ "\npre posY "+ prePosi.get(prePosi.size()-2).getY());
				System.out.println("\nDifference");
				System.out.println(Math.abs(prePosi.get(prePosi.size()-1).getX()- prePosi.get(prePosi.size()-2).getX()));
				System.out.println(Math.abs(prePosi.get(prePosi.size()-1).getY()- prePosi.get(prePosi.size()-2).getY()));
				prePosi.remove(prePosi.size()-1);
				System.out.println("\nLeftside: "+leftSide+" FrontSide: "+frontSide+" RightSide: "+rightSide);
				/*
				System.out.println("posX: "+ posX.get(posX.size()-1)+ "\npre posX "+ posX.get(posX.size()-2));
				System.out.println("posY: "+ posY.get(posY.size()-1)+ "\npre posY "+ posY.get(posY.size()-2));
				System.out.println("\nDifference");
				System.out.println(Math.abs(posX.get(posX.size()-1)- posX.get(posX.size()-2)));
				System.out.println(Math.abs(posY.get(posY.size()-1)- posY.get(posY.size()-2)));
				posX.remove(posX.size()-1);
				posY.remove(posY.size()-1);
				*/
				posi.setSpeed(-0.5f, 0);
				try {Thread.sleep(100);} catch (InterruptedException e) {}
				if (leftSide==Math.max(leftSide, Math.max(rightSide, frontSide))){
					posi.setSpeed(1, 0.5f);
				}else if(rightSide==Math.max(rightSide, frontSide)){
					posi.setSpeed(1, -0.5f);
				}else
					posi.setSpeed(1, 0);
				try {Thread.sleep(500);} catch (InterruptedException e) {}
				continue;
			}
			
			if(isNarrow()){
				System.out.println("narrow road");
				while(leftSide!=rightSide && isNarrow()){
					this.getSonars(sonar);
					if(leftSide==rightSide)
						posi.setSpeed(0.5f	,0);
					else
					if(leftSide>rightSide)
						posi.setSpeed(0.5f, 0.2f);
					else
						posi.setSpeed(0.5f, -0.2f);
				}
				try {Thread.sleep(500);} catch (InterruptedException e) {}
				continue;
			}
				
				
			//2close to the wall with right side
			if(rightSide<sonarMin || leftSide<sonarMin || frontSide<sonarMin){
				if (rightSide == Math.min(Math.min(rightSide, leftSide),frontSide))
					rbtTurn = givenTurn;
				else
					rbtTurn = - givenTurn;
				if (frontSide>=wallMin)
					rbtSpeed   = 1f;
				else
					rbtSpeed   = -1f;
					
				
					System.out.println("2close 2 collide");
					System.out.println ("Left side : [" + leftSide + "], Front side : [" + frontSide +"], Right side: [" + rightSide +"], rbtSpeed : [" + rbtSpeed + "], rbtTurn : [" + rbtTurn + "]\n");
					
			}		else
			/*
			if(Math.max(sonarValues[2], sonarValues[4])>=sonarMax-1 && Math.max(sonarValues[3], sonarValues[5])>=sonarMax-1){
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
			if (frontSide < wallMax) {
				// back up a little bit if we're bumping in front
				rbtSpeed   = -0.50f;
				rbtTurn = - givenTurn * 4;
				System.out.println("2close with front");
			}else
				// if we're getting too close to the wall with the left side...
				if (leftSide < wallMin) {
					rbtSpeed   = givenSpeed;
					rbtTurn = -givenTurn;
					System.out.println("2close with left");
				}
				else
					if(leftSide >= wallMin && leftSide <= wallMax){
						rbtSpeed = givenSpeed;
						rbtTurn = 0;
						System.out.println("straight line");
					}else
					// if we're getting too far away from the wall with the left side...
					if (leftSide > wallMax){// && sonarValues[0]<this.sonarMax) {
						// move slower at corners
						rbtSpeed   = givenSpeed/10f;
						//rbtSpeed   = rbtSpeed-0.5f;
						rbtTurn = leftSide;
						//rbtTurn = rbtTurn*0.7f;
					//	rbtTurn = rbtTurn*1.2f;
						System.out.println("2far with left");
					}else
						if(sonarValues[0]>this.sonarMax){
							//posi.setSpeed(0.5f, 0);
							//try { Thread.sleep (1000); } catch (Exception e) { }
							System.out.println("test entrance");
							posi.setSpeed(0.5f, 0);
							try { Thread.sleep (500); } catch (Exception e) { }
							posi.setSpeed(0, 2f);
							try { Thread.sleep (1000); } catch (Exception e) { }
							
							long startTime = System.currentTimeMillis();
							
							while((leftSide+rightSide)/2>1.0){
								this.getSonars(sonar);
								System.out.println("left: "+leftSide+ " right: "+rightSide);
								posi.setSpeed(0.5f, 0);

								try { Thread.sleep (100); } catch (Exception e) { }
								//posi.setSpeed(0, 0);
								//try { Thread.sleep (5000); } catch (Exception e) { }
									
								if((leftSide+rightSide)/2<=1.0){
									System.out.println("real entrance");
									break;
								}
								if(System.currentTimeMillis()-startTime>=3000){
									System.out.println("break by time");
									break;
								}
							}
							continue;
						}
			

			// Move the rbt
			posi.setSpeed (rbtSpeed, rbtTurn);
			System.out.println ("Left side : [" + leftSide + "], Front side : [" + frontSide +"], Right side: [" + rightSide +"], rbtSpeed : [" + rbtSpeed + "], rbtTurn : [" + rbtTurn + "]\n");
			try { Thread.sleep (100); } catch (Exception e) { }
			//posi.setSpeed(0,0);
			//try { Thread.sleep (2000); } catch (Exception e) { }
			
		}
	}
	public static void main(String[] args){
		new WallFollowerExample();
	}
	
	public boolean isStuck(){
		if(prePosi.size()>1){
			if(Math.abs(prePosi.get(prePosi.size()-1).getX()- prePosi.get(prePosi.size()-2).getX())<=0.4){
				if(Math.abs(prePosi.get(prePosi.size()-1).getY()- prePosi.get(prePosi.size()-2).getY())<=0.4)
					return true;	
			}
		}
		return false;
		
		/*if(posX.size()>1 || posY.size()>1){
			if(Math.abs(posX.get(posX.size()-1)- posX.get(posX.size()-2))<=0.6){
				if(Math.abs(posY.get(posY.size()-1)- posY.get(posY.size()-2))<=0.6)
					return true;
				
			}
		}
		return false;
		/*
		if((preX==posi.getX() || preX + 0.5 == posi.getX() || preX -0.5 == posi.getX())){
			if(preY==posi.getY() || preY + 0.5 == posi.getY() || preY -0.5 == posi.getX() ){
				/*
				preX = posi.getX();
				preY = posi.getY();
				if (preTime.size()>1000)
					preTime.remove(0);
				preTime.add(System.currentTimeMillis());
				System.out.println(System.currentTimeMillis()-preX);
				
				return true;
			}
		}
		
		preX = posi.getX();
		preY = posi.getY();
		preTime.add(System.currentTimeMillis());
		*/
	}
	
	public void getWall (Position2DInterface posi, SonarInterface sonar) {
		// get all SONAR values and perform the necessary adjustments
		getSonars (sonar);
		/*
		// if the rbt is in open space, go ahead until it "sees" the wall
		while ((leftSide > wallMax) && 
				(frontSide > wallMax)) {
			posi.setSpeed (givenSpeed, 0);
			try { Thread.sleep (100); } catch (Exception e) { }
			getSonars (sonar);
		}
		
		float previousLeftSide = sonarValues[2];
		
		// rotate until we get a smaller value in sonar 0 
		while (sonarValues[2] <= previousLeftSide) {
			
			// rotate more if we're almost bumping in front
			if (Math.min (leftSide, frontSide) == frontSide)
				rbtTurn = -givenTurn * 3;
			else
				rbtTurn = -givenTurn;
			
			// Move the rbt
			posi.setSpeed (0, rbtTurn);
			try { Thread.sleep (100); } catch (Exception e) { }
			
			getSonars (sonar);
		}
		posi.setSpeed (0, 0);
		*/
	}
	
	public void getSonars (SonarInterface sonar) {
		while (!sonar.isDataReady ());
		sonarValues = sonar.getData ().getRanges ();
		/*
		// ignore erroneous readings/keep interval [sonarMin; sonarMax]
		for (int i = 0; i < sonar.getData ().getRanges_count (); i++)
			if (sonarValues[i] < sonarMin)
				sonarValues[i] = sonarMin;
			else
				if (sonarValues[i] > sonarMax)
					sonarValues[i] = sonarMax;
		*/
		leftSide = Math.min (Math.min (sonarValues[0], sonarValues [1]), sonarValues [0]);
		rightSide = Math.min (Math.min (sonarValues[7], sonarValues [6]), sonarValues [7]);
		frontSide = Math.min (sonarValues [3], sonarValues [4]);
	}
	public boolean isNarrow(){
		if(sonarValues[0]+sonarValues[7]<0.6){
			return true;
		}
		return false;
	}
}