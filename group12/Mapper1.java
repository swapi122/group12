import static name.audet.samuel.javacv.jna.cxcore.CV_RGB;
import static name.audet.samuel.javacv.jna.cxcore.cvLine;
import static name.audet.samuel.javacv.jna.cxcore.v10.*;
import static name.audet.samuel.javacv.jna.cv.v10.*;
import static name.audet.samuel.javacv.jna.highgui.v10.*;
import static name.audet.samuel.javacv.jna.cvaux.v10.*;

import java.awt.Component;
import java.awt.Color.*;
import name.audet.samuel.javacv.CanvasFrame;
import javaclient2.*;




public class Mapper1 implements Runnable {

	PlayerClient robotx;
	Position2DInterface ppd;
	LaserInterface laser;
	
	public Mapper1(PlayerClient robotx, Position2DInterface ppd,LaserInterface laser){
		this.robotx=robotx;
		this.ppd=ppd;
		this.laser=laser;
	}
	
	public float angtorads(int angle){
		
		float rads = (angle*(float)Math.PI)/180;
		return rads;
		
	}
	
	//round up after converting float to 
	public int roundup (float num) {
	    int temp = (int)(num*100);
	    temp = temp%10;;
	    if (temp >= 5) return (int)((num*10)+1);
	    else return (int)(num*10);
	}
	
	
	public void run(){
		    //Variables 
		int degrees,PosRelX,PosRelY; 
		float radians,Dlaser,ODM_ang, ang; 
		int width = 500, height = 500; 
		int centroX = (width / 2);
		int centroY = (height / 2);
		CvPoint pt;
		CvPoint pt1;
		CvPoint pt2;
		CvScalar cinzaE,preto,cinzaC; 
		//char window_name[] = "Map"; 
		
		IplImage image = cvCreateImage(cvSize(width,height), 8, 3);
		
	    pt = new CvPoint();
	    pt1 = new CvPoint();
	    pt2 = new CvPoint();
		
		 //replaced framewindow with canvasframe
	    CanvasFrame xff = new CanvasFrame(true,"test");
	    xff.setVisible(true);

	    
	    
	    preto = CV_RGB(0, 0, 0);        //for indicating obstacles
	    cinzaE = CV_RGB(92, 92, 92);    //To indicate the stranger
	    cinzaC = CV_RGB(150, 150, 150); //To indicate free spaces
	   
		
			// 

		/*     if (playerc_client_datamode (client, PLAYERC_DATAMODE_PULL) != 0) { */
		/*         fprintf(stderr, "error: %s\n", playerc_error_str()); */
		/*         return -1; */
		/*     } */

		/*     if (playerc_client_set_replace_rule (client, -1, -1, PLAYER_MSGTYPE_DATA, -1, 1) != 0) { */
		/*         fprintf(stderr, "error: %s\n", playerc_error_str()); */
		/*         return -1; */
		/*     } */
			
			

		ppd.setSpeed(1,0);
		
		cvSet(image, cinzaE.byValue()); 
		pt.x = centroX;  // Zero coordinate for x */
		pt.y = centroY;  // Zero coordinate for y */
		
		while(true){
			
	
			
			robotx.readAll();
			cvSaveImage("mapa.jpg",image);
			robotx.readAll();
			
			for (degrees = 2; degrees < 180; degrees+=2) {
	              Dlaser = laser.getData().getRanges()[degrees];
	                if (Dlaser < 5) {
	                    radians = angtorads(degrees/2);      //Convert the angle of the laser to radians
	                    ODM_ang = ppd.getData().getPos().getPa();         //Obtain the angle relative to the robot
	                    ang = (float)((1.5*(Math.PI)+radians+ODM_ang));     //Converte the angle relative to the world
	                    PosRelX = roundup(ppd.getData().getPos().getPx());  //Position x relative to robot
	                    PosRelY = roundup(ppd.getData().getPos().getPy());  //Position y relative to robot
	                    
	                    
	                  pt.y = (int)(pt1.y-(Math.sin(ang)*Dlaser*10));
	                  pt.x = (int)(pt1.x+(Math.cos(ang)*Dlaser*10));
	                  
	                   cvLine(image, pt.byValue(),pt.byValue(),cinzaC.byValue(), 1,4,0);
	                    
	                    //marks the object in the map
	                   cvLine(image, pt.byValue(),pt.byValue(),preto.byValue(), 1,4,0);

	                    //Shows the result of the map to the screen
	                    /*ShowImage(window_name, image );*/
	                   xff.showImage(image);
	                   xff.waitKey(1);
	                  
	         }
			
		}
	    
		
	}
		

	}
}
