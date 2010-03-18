import java.util.*;
import java.lang.Math.*;
import java.text.FieldPosition;
import java.text.NumberFormat;
import javaclient2.*;
import javaclient2.structures.position2d.*;
import javaclient2.structures.laser.*;
import javaclient2.structures.blobfinder.*;
import javaclient2.structures.fiducial.*;
import javaclient2.structures.gripper.*;
import javaclient2.structures.sonar.*;
import javaclient2.structures.PlayerPose3d.*;


public class objectdetection{
	
	PlayerClient        robt = null;
	Position2DInterface pstn  = null;
	SonarInterface      sonr  = null;
	FiducialInterface 	fdcl  = null; 
	LaserInterface 		lasr = null;
	BlobfinderInterface blbf = null;
	
	
	//x,y coordinates of robot
	int posx;
	int posy;
	
	//used to check if coordinates x,y has object
	int checkx; 
	int checky; 
	
	//array of 180 laser values
	float lasVals[];
	
	/*
	//remembers the locations of objects
	int objectposgrid[][]; 
	*/
	
	//Array of detected blobs
	static PlayerBlobfinderBlob blobs[];
	
	//constructor
	public objectdetection(PlayerClient tempbot, LaserInterface laser,
			BlobfinderInterface blobfinder, FiducialInterface fiducial, Position2DInterface position){
		
		robt = tempbot;
		lasr = laser;
		blbf = blobfinder;
		fdcl = fiducial;
		pstn = position;
		/*
		objectposgrid = new int[60][30];
		*/
	}
	
	//checks if object blocking the robots wall is an object
	public boolean isOccupied(){
		
		//do nothing until blobfinder data ready
		while(!blbf.isDataReady()); 
		blobs = blbf.getData().getBlobs();
		if(blobs.length>0){
			
			for(int i = 0; i<blobs.length; i++){
				//if a blob is in close range, return true
				System.out.println("range is " + blobs[i].getRange()/10 + "color is" + blobs[i].getColor());
				if((int)(blobs[i].getRange()/10)<120) return true; 
			
		}
		
			
		/*
		while(!pstn.isDataReady());
		
		posx = (int)pstn.getData().getPos().getPx(); 
		posy = (int)pstn.getData().getPos().getPy(); 
		
		checkx = (int)Math.cos(pstn.getYaw()-10) + posx;
		checky = (int)Math.sin(pstn.getYaw()-10) + posy;
		
		checkx = (int)Math.cos(robpos.getYaw()-10)*lsrVals[90] + posx;
		checky = (int)Math.sin(robpos.getYaw()-10)&*lsrVals[90] + posy; 
		
		//if the location has an object, then return true
		if(objectposgrid[checkx][checky]>1){return true;}
		else return false;
		*/
		}
		return false;
	}
	
	//checks if the wall if blocked
	public boolean iswallblocked(){
		
		while(!lasr.isDataReady());

		lasVals = lasr.getData().getRanges();
		
		//if rightside of laser against wall, and object blocking wall
		for(int i = 89; i>=80; i--){
		if(lasVals[i]<1.5 && isOccupied()){
			return true;
		}
		}
		return false;
	}
	
	//if object is against or near wall, turn away
	public void wallblockedturn(){
		
		
		
		//stop and turn right
			System.out.println("WALL BLOCKED, TURNING");
		pstn.setSpeed(0,-1.57f);
		try { Thread.sleep (1000); } catch (Exception e) { }
		pstn.setSpeed(1,0);
		try { Thread.sleep (1000); } catch (Exception e) { }
		
		
	}
	
}