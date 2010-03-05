import javaclient2.Position2DInterface;
import java.util.ArrayList;

public class PosiThread implements Runnable {
	Position2DInterface posi;
	ArrayList<Float> posX;
	ArrayList<Float> posY;
	int arrayLength = 2;
	int i =0;

	public PosiThread(Position2DInterface posi, ArrayList<Float> posX,
			ArrayList<Float> posY) {
		this.posi = posi;
		this.posX = posX;
		this.posY = posY;
	}

	public void run() {
		while (true) {
			if (posX.size() > arrayLength || posY.size() > arrayLength) {
				posX.remove(0);
				posY.remove(0);
			}
			posX.add(posi.getX());
			posY.add(posi.getY());
			/*
			System.out.println("thread added value "+ ++i);
			System.out.println(posX.get(posX.size()-1));
			System.out.println(posY.get(posY.size()-1));
			*/
			try {Thread.sleep(4000);} catch (Exception e) {}
		}
	}


}
