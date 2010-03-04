import java.text.FieldPosition;
import java.text.NumberFormat;

import javaclient2.*;
import javaclient2.structures.blobfinder.*;
import javaclient2.structures.fiducial.*;
import javaclient2.structures.gripper.*;
import javaclient2.structures.position2d.*;
import javaclient2.structures.laser.*;
import javaclient2.structures.sonar.*;


import javaclient2.structures.PlayerConstants;


public class mostrecent {
	
	static float[] sonarVals;
	static float rightside;
	static float forwardright;
	static float leftside;
	static float forwardleft;
	static float center;
	static float threshold;

	public static void escape(Position2DInterface ppx){
	
		ppx.setSpeed(-1,0);
		try { Thread.sleep (400); } catch (Exception e){}
		ppx.setSpeed(0,3);
		try { Thread.sleep (400); } catch (Exception e){}
		
	
	}
			
	public static void main(String[] args){
	

		//Creation of the robot, assigned to port 6665
		PlayerClient robotx = robotx = new PlayerClient("localhost", 6665);

		//Creation of the various interfaces
		Position2DInterface ppd = robotx.requestInterfacePosition2D(0, PlayerConstants.PLAYER_OPEN_MODE);
		LaserInterface laser = robotx.requestInterfaceLaser(0, PlayerConstants.PLAYER_OPEN_MODE);
		SonarInterface sonar = robotx.requestInterfaceSonar(0, PlayerConstants.PLAYER_OPEN_MODE);
		FiducialInterface fid = robotx.requestInterfaceFiducial(0, PlayerConstants.PLAYER_OPEN_MODE);
		GripperInterface grip = robotx.requestInterfaceGripper(0, PlayerConstants.PLAYER_OPEN_MODE);
		BlobfinderInterface blob = robotx.requestInterfaceBlobfinder(0, PlayerConstants.PLAYER_OPEN_MODE);

		//Initalize & set initial speed
		robotx.runThreaded(-1, -1);	
		ppd.setSpeed(2,0);	

		while(true) {
		
			while (!sonar.isDataReady()); //do nothing until sonar data ready
 			sonarVals = sonar.getData().getRanges(); //get ranges

			rightside = (sonarVals[0] + sonarVals[1])/2; 
			forwardright = (sonarVals[1] + sonarVals[2] + sonarVals[3])/3;
			center = (sonarVals[3] + sonarVals[4])/2;
			forwardleft = (sonarVals[5] + sonarVals[6]  + sonarVals[4])/3;
			leftside = (sonarVals[6] + sonarVals[7])/2;
			threshold = 0.6f;

			//if the robot is trapped
			if((forwardright < threshold && forwardleft <threshold) && center < threshold)
			{	
				escape(ppd);
			}
			//if the right threshold is broken, stop, turn left
			else if(forwardright < threshold){ ppd.setSpeed(0,-1); }
		
			//if the left threshold is broken, stop, turn right
			else if(forwardleft <threshold){ ppd.setSpeed(0, 1); }

			//if the central threshold alone is broken
			else if(center < threshold){ 
				//if there is more room on the left, move left
				if(forwardright < forwardleft){ 
					ppd.setSpeed(0,-1);
				}
				//if there is more room on the right, move right
				else if(forwardleft < forwardright){
					ppd.setSpeed(0,1);
				
				}
			}
			
			//If the robots path is entirely clear, travel forwards
			else ppd.setSpeed(1,0);

			//range data for terminal
			System.out.println("range 0: " + (int)sonarVals[0] + 
					   " range 1: " + (int)sonarVals[1] +
					   " range 2: " + (int)sonarVals[2] +
					   " range 3: " + (int)sonarVals[3] +  
					   " range 4: " + (int)sonarVals[4] + 
					   " range 5: " + (int)sonarVals[5] +
					   " range 6: " + (int)sonarVals[6] +
					   " range 7: " + (int)sonarVals[7]);
	}
  }
}
