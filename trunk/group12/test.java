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
	
	public test(){
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
		
		System.out.println(posi.getYaw());
		posi.setSpeed(0, (float) Math.PI *2);
		stopThread(1000);
		while(Math.round(posi.getYaw())!=0){
			if(Math.round(posi.getYaw())<=1 && Math.round(posi.getYaw())>=-1)
				break;
			System.out.println(Math.round(posi.getYaw()));
			posi.setSpeed(0, 0.2f);
			stopThread(100);
		}
		posi.setSpeed(0, 0);
		stopThread(1000);
		System.out.println(posi.getYaw());
		System.exit(0);
	}
    public static void main(String[] args) {
    	new test();
    }
	double round1dp(double f) {
		return Math.round(f);
		//DecimalFormat form = new DecimalFormat("#.#");
		//return Double.valueOf(form.format(f));
	}
	public void stopThread(long milliSecond) {
		try {
			Thread.sleep(milliSecond);
		} catch (Exception e) {
		}
	}
}