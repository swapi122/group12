

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;
 
public class DrawingPanel extends JPanel implements Runnable{
	Line2D.Float line = new Line2D.Float();
	AffineTransform at;
	Shape newLine;

	double xStart = -50;
	double xEnd = 100;

	double yStart = -50;
	double yEnd = 100;
	
	int degree = -90;
	
    public DrawingPanel() {
        line.setLine(xStart, yStart, xEnd, yEnd);
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        if(at == null) initTransform(line);
        g2.setPaint(Color.blue);
        g2.draw(line);
        
        // Draw transformed primitives.
        g2.setPaint(Color.red);
        g2.draw(newLine);
    }
 
    private void initTransform(Line2D.Float l) {
        double x = 310;
        double y = 170;
        //double x = 125;
        //double y = 100;
        double theta = Math.toRadians(degree);
        //double cx = origRect.getCenterX();
        //double cy = origRect.getCenterY();
        at = AffineTransform.getTranslateInstance(x,y);
        double cx = l.getX2()-l.getX1();
        double cy = l.getY2()-l.getY2();
        System.out.println(cx);
        System.out.println(cy);
        at.rotate(theta, cx, cy);
        newLine = at.createTransformedShape(l);
    }
 
    public Dimension getPreferredSize() {
        return new Dimension(640,480);
    }
 
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new DrawingPanel());
        f.pack();
        f.setLocation(100,100);
        f.setVisible(true);
    }
    public void run(){
    	
    }
}

