//it's not working! edmond 20100311


import static name.audet.samuel.javacv.jna.cxcore.cvLine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javaclient2.LaserInterface;
import javaclient2.PlayerClient;
import javaclient2.Position2DInterface;

public class DrawThread implements Runnable {
	Position2DInterface posi;

	long startTime;
	int width = 800;
	int height = 600;

	double xPoint;
	double yPoint;
	float range;
	float[] laserValues;
	int degree = -90;

	BufferedImage bi = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d;
	Line2D.Float line = new Line2D.Float();
	AffineTransform at;
	Shape newLine;
	
	LaserInterface laser;

	public DrawThread(Position2DInterface posi,LaserInterface laser) {
		this.posi = posi;
		this.laser=laser;
		startTime = System.currentTimeMillis();
	}
	private void coordTransform(Line2D.Float l) {
        double x = 20;
        double y = 300;
        double theta = Math.toRadians(degree);
        at = AffineTransform.getTranslateInstance(x,y);
        double cx = l.getX2()-l.getX1();
        double cy = l.getY2()-l.getY2();
        at.rotate(theta, cx, cy);
        newLine = at.createTransformedShape(l);
    }

	public void run() {
		g2d = bi.createGraphics();
		g2d.fill(new Rectangle(0,0,width, height));
		g2d.setColor(Color.black);
		while (true) {
			while (!laser.isDataReady());
			laserValues = laser.getData().getRanges();
			for(int i=0; i<laserValues.length-1; i++){
				if(laser.getData().getRanges()[i] >= 5f)
					continue;
				
				range =laserValues[i];

				yPoint=range * Math.cos(Math.toRadians(i))+posi.getX()*10;
				xPoint=range * Math.sin(Math.toRadians(i))+posi.getY()*10;
				
				line.setLine(xPoint, yPoint, xPoint, yPoint);
				coordTransform(line);
				g2d.draw(newLine);
				System.out.println("i:"+i+" range: "+laser.getData().getRanges()[i]);
				System.out.println("xPoint: "+xPoint+" yPoint: "+yPoint);
				}
			
			
			try {
				if(System.currentTimeMillis()-startTime>120000){
				ImageIO.write(bi, "BMP", new File("./a.bmp"));
				System.exit(0);
				}
				//Thread.sleep(1000);
				//System.exit(0);
			} catch (Exception e) {
				System.err.println("    [ " + e.toString() + " ]");
			}
		}
	}

}
