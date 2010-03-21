import javaclient2.BlobfinderInterface;
import javaclient2.FiducialInterface;
import javaclient2.LaserInterface;
import javaclient2.PlayerClient;
import javaclient2.PlayerException;
import javaclient2.Position2DInterface;
import javaclient2.SonarInterface;
import javaclient2.structures.PlayerConstants;

public class test {
	PlayerClient rbt;
	Position2DInterface posi;
	SonarInterface sonar;
	FiducialInterface fid;
	LaserInterface las;
	BlobfinderInterface blobf;

	public test() {
		try {
			// Connect to the Player server and request access to Position and
			// Sonar
			rbt = new PlayerClient("localhost", 6665);
			posi = rbt.requestInterfacePosition2D(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			sonar = rbt.requestInterfaceSonar(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			fid = rbt.requestInterfaceFiducial(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			las = rbt
					.requestInterfaceLaser(0, PlayerConstants.PLAYER_OPEN_MODE);
			blobf = rbt.requestInterfaceBlobfinder(0,
					PlayerConstants.PLAYER_OPEN_MODE);

		} catch (PlayerException e) {
			System.err
					.println("WallFollowerExample: > Error connecting to Player: ");
			System.err.println("    [ " + e.toString() + " ]");
			System.exit(1);
		}

		rbt.runThreaded(-1, -1);
		
		Thread drawThd = new Thread(new DrawThread(posi,las));
		//drawThd.start();
		//posi.setSpeed(0, -1f);
		//stopThread(1000);
		
		while (true){
			//posi.setSpeed(0, -0.5f);
			while(!las.isDataReady());
			System.out.println("X: "+posi.getData().getPos().getPx());
			System.out.println("Y: "+posi.getData().getPos().getPy());
			System.out.println("Pa: "+posi.getData().getPos().getPa());
			System.out.println("Yaw: "+posi.getYaw() + "\n");
			System.out.println("las160 range: "+ las.getData().getRanges()[160]);
			stopThread(1000);
		}
		
		
		
		/*
		System.out.println(posi.getData().getPos().getPx());
		System.out.println(posi.getData().getPos().getPy());
		System.out.println(posi.getYaw() + "\n");

		
		
		toWayPoint(6, -22);

		System.out.println(posi.getX());
		System.out.println(posi.getY());
		System.out.println(posi.getYaw());
		System.exit(0);
		*/
	}

	public static void main(String[] args) {
		new test();
	}

	double round1dp(double f) {
		return Math.round(f);
		// DecimalFormat form = new DecimalFormat("#.#");
		// return Double.valueOf(form.format(f));
	}

	public void stopThread(long milliSecond) {
		try {
			Thread.sleep(milliSecond);
		} catch (Exception e) {
		}
	}

	public void toWayPoint(float dX, float dY) {
		//x y reverse fuck
		float cX = posi.getX();
		float cY = posi.getY();
		float cYaw = posi.getYaw();

		/*
		 * /convert current yaw to real degree float rYaw = 359 - posi.getYaw();
		 * if(rYaw>359) rYaw -=359;
		 */
		
		// distance
		double tmpDis = Math.sqrt(Math.pow((dY - cY), 2)
				+ Math.pow((dX - cX), 2));
		double ang = Math.toDegrees(Math.atan((dX - cX) / (dY - cY)));

		float dis = (float) tmpDis;
		
		System.out.println("dx/cx: "+(dX - cX));
		System.out.println("dy/cy: "+(dY - cY));
		System.out.println("rad: "+Math.atan((dX - cX) / (dY - cY)));
		
		System.out.println("first ang:" + ang);

		// convert to real degree 0-359
		if (dX >= cX) {
			if (dY < cY) {
				ang += 180;
			}
		} else {
			if (dY >= cY) {
				ang += 359;
			} else {
				ang += 180;
			}
		}
		System.out.println("converted ang:" + ang);

		// convert to machine degree
		// ang = 360-ang;
		if (ang <= 180)
			ang *= -1;
		else
			ang = 360 - ang;

		System.out.println("machine ang:" + ang);
		System.out.println(dis);

		// /turning start
		ang = Math.round(ang);
		double ang1 = ang + 1;
		double ang0 = ang - 1;

		float speed = 0.1f;

		// exceptional case
		if (ang == 179) {
			ang1 = -180;
		}
		if (ang == -180) {
			ang0 = 179;
		}

		// decide to turn in which direction
		if ((ang > cYaw && ang - cYaw > 180) || (cYaw >= ang && cYaw - ang >= -180) ){
			speed *= -1;
		}

		while (Math.round(posi.getYaw()) != ang) {
			if (Math.round(posi.getYaw()) == ang1
					|| Math.round(posi.getYaw()) == ang0)
				break;
			System.out.println(Math.round(posi.getYaw()));
			posi.setSpeed(0, speed);
			stopThread(100);
		}
		posi.setSpeed(0, 0);

		posi.setSpeed(dis, 0);
		stopThread(1000);
		posi.setSpeed(0, 0);
	}

	public void turnTo(double d) {
		d = Math.round(d);
		double d1 = d + 1;
		double d0 = d - 1;

		// exceptional case
		if (d == 179) {
			d1 = -180;
		}
		if (d == -180) {
			d0 = 179;
		}
		while (Math.round(posi.getYaw()) != d) {
			if (Math.round(posi.getYaw()) == d1
					|| Math.round(posi.getYaw()) == d0)
				break;
			System.out.println(Math.round(posi.getYaw()));
			posi.setSpeed(0, 0.1f);
			stopThread(100);
		}
		posi.setSpeed(0, 0);
	}
}