import static name.audet.samuel.javacv.jna.cxcore.*;
import static name.audet.samuel.javacv.jna.cv.*;
import static name.audet.samuel.javacv.jna.highgui.*;
import static name.audet.samuel.javacv.jna.cvaux.*;

public class test {
    public static void main(String[] args) {
        IplImage image = cvLoadImage("samplemap.png", 1);
        cvSmooth(image, image, CV_GAUSSIAN, 3, 0, 0, 0);
        // ...
    }
}