//it's not working! edmond 20100311


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javaclient2.Position2DInterface;

public class DrawingThread implements Runnable {
	Position2DInterface posi;

	long startTime;
	int width = 800;
	int height = 600;

	float xPoint;
	float yPoint;

	BufferedImage bi = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d;
	Line2D.Float line = new Line2D.Float();

	public DrawingThread(Position2DInterface posi) {
		this.posi = posi;
		startTime = System.currentTimeMillis();
	}

	public void run() {
		g2d = bi.createGraphics();
		g2d.fill(new Rectangle(0,0,width, height));
		g2d.setColor(Color.black);
		g2d.translate((int) posi.getX(), (int) posi.getY());
		while (true) {
			xPoint = posi.getX();
			yPoint = posi.getY();
			line.setLine(xPoint, yPoint, xPoint, yPoint);
			g2d.draw(line);
			

			try {
				//if(System.currentTimeMillis()-startTime>60000)
				ImageIO.write(bi, "BMP", new File("./a.bmp"));
				System.out.println("xPoint: "+xPoint+" yPoint: "+yPoint);
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println("    [ " + e.toString() + " ]");
			}
		}
	}

}
