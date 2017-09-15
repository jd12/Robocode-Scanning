package sampleBots;

import robocode.*;

public class AdvancedBearingBot extends AdvancedRobot {
	
	public void run() {
		setAdjustRadarForGunTurn(true);
		while (true) {
			// Turn the scanner until we find an enemy robot
			setTurnRadarRight(10000);
			execute(); // you must call this!!!
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		out.println("scanned a robot at bearing " + e.getBearing());
		out.println("my heading is currently " + getHeading());

		// When we scan a robot, turn toward him
		setTurnRight(e.getBearing());
		// shoot at him
		setFire(3);
		// and ram into him
		setAhead(e.getDistance());

		// don't have to call execute here, the Robocode engine does it for
		// you after you handle an event.
	}

	public void onHitRobot(HitRobotEvent e) {
		out.println("hit a robot at bearing " + e.getBearing());
		out.println("my heading is currently " + getHeading());

		if (e.isMyFault()) {
			out.println("I hit " + e.getName());
		} else {
			out.println(e.getName() + " hit me");
		}
	}
}
