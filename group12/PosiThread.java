import javaclient2.Position2DInterface;
import java.util.ArrayList;

public class PosiThread implements Runnable {
	Position2DInterface posi;
	static ArrayList<Float> test = new ArrayList<Float>();
	ArrayList<Float> posX;
	ArrayList<Float> posY;

	public PosiThread(Position2DInterface posi, ArrayList<Float> posX,
			ArrayList<Float> posY) {
		this.posi = posi;
		this.posX = posX;
		this.posY = posY;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			if (posX.size() > 10 || posY.size() > 10) {
				posX.remove(0);
				posY.remove(0);
			}
			posX.add(posi.getX());
			posY.add(posi.getY());
		}
	}

	public void getPosX() {

	}

	public static void main(String[] args) {
		for (float i = 0; i < 10; i++) {
			test.add(i);
		}
		for (int i = 0; i < test.size(); i++) {
			System.out.println("Index " + i + " = " + test.get(i));
		}

		test.remove(0);
		for (int i = 0; i < test.size(); i++) {
			System.out.println("Index " + i + " = " + test.get(i));
		}

	}

}
