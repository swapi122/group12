

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
 
public class XPoints extends JPanel {
    Rectangle origRect = new Rectangle(50, 100, 200, 150);
    Shape xShape;
    AffineTransform at;
    Point[] pts;
 
    public XPoints() {
        int[][] coords = {
            {  75, 120, 185, 210 },
            { 190, 155, 125, 225 }
        };
        pts = new Point[coords[0].length];
        for(int i = 0; i < coords[0].length; i++) {
            pts[i] = new Point(coords[0][i], coords[1][i]);
        }
    }
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        if(at == null) initTransform();
        g2.setPaint(Color.blue);
        g2.draw(origRect);
        for(int i = 0; i < pts.length; i++) {
            g2.fill(new Ellipse2D.Double(pts[i].x-1.5, pts[i].y-1.5, 4, 4));
        }
        // Draw transformed primitives.
        g2.setPaint(Color.red);
        g2.draw(xShape);
        Point2D.Double p = new Point2D.Double();
        for(int i = 0; i < pts.length; i++) {
            at.transform(pts[i], p);
            g2.fill(new Ellipse2D.Double(p.x-1.5, p.y-1.5, 4, 4));
        }
    }
 
    private void initTransform() {
        double x = 0;
        double y = 0;
        //double x = 125;
        //double y = 100;
        double theta = Math.toRadians(-90);
        double cx = origRect.getCenterX();
        double cy = origRect.getCenterY();
        at = AffineTransform.getTranslateInstance(x,y);
        at.rotate(theta, cx, cy);
        xShape = at.createTransformedShape(origRect);
    }
 
    public Dimension getPreferredSize() {
        return new Dimension(400,400);
    }
 
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new XPoints());
        f.pack();
        f.setLocation(100,100);
        f.setVisible(true);
    }
}

