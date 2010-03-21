//it's not working! edmond 20100311


import static name.audet.samuel.javacv.jna.cxcore.cvLine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javaclient2.LaserInterface;
import javaclient2.PlayerClient;
import javaclient2.Position2DInterface;

public class DrawingThread implements Runnable {
	Position2DInterface posi;

	long startTime;
	int width = 800;
	int height = 600;

	float xPoint;
	float yPoint;
	
	int degree = -90;

	BufferedImage bi = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d;
	Line2D.Float line = new Line2D.Float();
	AffineTransform at;
	Shape newLine;
	
	float[] laservals;
	LaserInterface laser;

	public DrawingThread(Position2DInterface posi,LaserInterface laser) {
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
			laservals = laser.getData().getRanges();
			
			for(int i=0; i<180; i++){
				xPoint = posi.getX();
				yPoint = posi.getY();
				xPoint=Math.round(laser.getData().getRanges()[i] * Math.cos(Math.toRadians(i))+xPoint);
				yPoint.y=Math.round((laser.getData().getRanges()[i] * Math.sin(Math.toRadians(i))+yPoint);
				line.setLine(xPoint, yPoint, xPoint, yPoint);
				coordTransform(line);
				g2d.draw(newLine);

				}
			
			
			
			
			

			
			
			try {
				//if(System.currentTimeMillis()-startTime>60000)
				ImageIO.write(bi, "BMP", new File("./a.bmp"));
				//System.out.println("xPoint: "+xPoint+" yPoint: "+yPoint);
				Thread.sleep(50);
			} catch (Exception e) {
				System.err.println("    [ " + e.toString() + " ]");
			}
		}
	}

}
