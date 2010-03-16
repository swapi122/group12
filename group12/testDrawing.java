import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class testDrawing {
	int width = 800;
	int height = 600;

	double xStart = 0;
	double xEnd = 100;

	double yStart = 0;
	double yEnd = 100;

	Line2D.Float line = new Line2D.Float();
	
	AffineTransform at;
	Shape newLine;

	testDrawing() {
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bi.createGraphics();
		line.setLine(xStart, yStart, xEnd, yEnd);
		g2d.draw(line);
		initTransform(line);
		g2d.draw(newLine);

		try {
			ImageIO.write(bi, "BMP", new File("./a.bmp"));
			System.out.println("done");
		} catch (Exception e) {
			System.err.println(" [ " + e.toString() + " ]");
		}
	}

	public static void main(String[] args) {
		new testDrawing();
	}
	private void initTransform(Line2D.Float l) {
        //double x = 310;
        //double y = 170;
        double x = 0;
        double y = 0;
        double theta = Math.toRadians(-90);
        at = AffineTransform.getTranslateInstance(x,y);
        double cx = (l.getX2()-l.getX1())/2;
        double cy = (l.getY2()-l.getY1())/2;
        at.rotate(theta, cx, cy);
        newLine = at.createTransformedShape(l);
    }
}