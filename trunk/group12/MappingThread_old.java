//it's not working! edmond 20100311


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javaclient2.Position2DInterface;

public class MappingThread_old implements Runnable {
	Position2DInterface posi;

	long startTime;
	int width = 800;
	int height = 600;

	float xPoint;
	float yPoint;

    double tX = 310;
    double tY = 170;

	BufferedImage bi = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d;
	Line2D.Float line = new Line2D.Float();
	AffineTransform at;
	Shape newLine;

	public MappingThread_old(Position2DInterface posi) {
		this.posi = posi;
		startTime = System.currentTimeMillis();
	}
	private void pointTransform(Line2D.Float l) {
        //double x = 0;
        //double y = 0;
        double theta = Math.toRadians(-90);
        at = AffineTransform.getTranslateInstance(tX,tY);
        double cx = (l.getX2()-l.getX1())/2;
        double cy = (l.getY2()-l.getY1())/2;
        at.rotate(theta, cx, cy);
        newLine = at.createTransformedShape(l);
    }

	public void run() {
		g2d = bi.createGraphics();
		g2d.fill(new Rectangle(0,0,width, height));
		g2d.setColor(Color.black);
		
		while (true) {
			xPoint = posi.getX()*10;
			yPoint = posi.getY()*10;
			line.setLine(xPoint, yPoint, xPoint, yPoint);
			pointTransform(line);
			g2d.draw(newLine);
			try {
				ImageIO.write(bi, "BMP", new File("./a.bmp"));
				System.out.println("xPoint: "+xPoint/10+" yPoint: "+yPoint/10);
				Thread.sleep(100);
			} catch (Exception e) {
				System.err.println("    [ " + e.toString() + " ]");
			}
		}
	}

}