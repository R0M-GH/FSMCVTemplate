import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;

public class LimeLight {

	private PhotonCamera camera;
	private static final double CAMERA_ANGLE = Math.toRadians(10); //RADIANS //30.25
	private static final double HUB_HEIGHT = 2.6; //METERS
	private static final double APRIL_TAG_HEIGHT = 0.4826; //METERS
	private static final double CAMERA_HEIGHT = 0.508; //METERS
	public static final double INVALID_RETURN = -2;
	private double last_seen_location = -1;
	public CVPipelines currentPipeline;

	public enum CVPipelines {
		SHOOTER_PIPELINE,
		BALL_DETECTION_PIPELINE,
		ALIGNMENT_PIPELINE
	}

	public LimeLight() {
		camera = new PhotonCamera("gloworm");
	}

	public void update() {
		SmartDashboard.putNumber("Distance", getAprilTagDistance());
		SmartDashboard.updateValues();

	}

	public double getHubDistance() {
		camera.setPipelineIndex(0);
		var result = camera.getLatestResult();
		if (result.hasTargets()) {
			return PhotonUtils.calculateDistanceToTargetMeters(
								CAMERA_HEIGHT,
								HUB_HEIGHT,
								CAMERA_ANGLE,
								Math.toRadians(result.getBestTarget().getPitch()));
		}
		return -1;
	}
	
	/* 
	public double getBallDistance() {
		camera.setPipelineIndex(1);
		var result = camera.getLatestResult();
		if (result.hasTargets()) {
			return PhotonUtils.calculateDistanceToTargetMeters(
								CAMERA_HEIGHT,
								BALL_HEIGHT,
								BALL_CAMERA_ANGLE,
								Math.toRadians(result.getBestTarget().getPitch()));
		}
		return -1;
	}
	*/
	
	public double getHubTurningPower() {
		camera.setPipelineIndex(0);
		var result = camera.getLatestResult();
		if (!result.hasTargets()) {
			return INVALID_RETURN;
		}
		double angle = result.getBestTarget().getYaw();
		if (Math.abs(angle) < 6) {
			return 0;
		}
		return Math.abs(angle) / -angle;
	}

	public double getAprilTagTurningPower() {
		camera.setPipelineIndex(1);
		if (!result.hasTargets()) {
			return last_seen_location;
		}
		double angle = result.getBestTarget().getYaw();
		//if (Math.abs(angle) < 6) {
			//return 0;
		//}
		//last_seen_location =  Math.abs(angle) / -angle;
		//return last_seen_location;
		if (angle < 0){
			last_seen_location = 1 - (1/((x * x / 10) + 1));
		}else{
			last_seen_location = (1/((x * x / 10) + 1)) - 1;
		}
		return last_seen_location;
	}

	public double getAprilTagDistance() {
		camera.setPipelineIndex(1);
		var result = camera.getLatestResult();
		if (result.hasTargets()) {
			SmartDashboard.putNumber("angle", result.getBestTarget().getPitch());
			SmartDashboard.updateValues();
			/*
			return PhotonUtils.calculateDistanceToTargetMeters(
								CAMERA_HEIGHT,
								APRIL_TAG_HEIGHT,
								HUB_CAMERA_ANGLE,
								Math.toRadians(result.getBestTarget().getPitch()));
			*/
			return (APRIL_TAG_HEIGHT - CAMERA_HEIGHT)/Math.tan(CAMERA_ANGLE+Math.toRadians(result.getBestTarget().getPitch()));

		}
		return -1;
	}
	
	// Stuff
	public double[] runPipeline() {
		switch (currentPipeline) {
			case (CVPipelines.SHOOTER_PIPELINE):
				return runShooter();
			case (CVPipelines.BALL_DETECTION_PIPELINE):
				return runBallDetection();
			case (CVPipelines.ALIGNMENT_PIPELINE):
				return runAlignment();
			default:
				throw Exception("you dumb asf");
		}
	}

	public double runShooter() {

	}

	public double runBallDetection() {

	}

	public double runAlignment() {

	}

	public void setCamAngle(double angle) {
		CAMERA_ANGLE = Math.toRadians(angle);
	}

	public double getCamAngle() {
		return Math.toDegrees(CAMERA_ANGLE);
	}
}