package lessons;

import robocode.*;

public class Oscillator extends AdvancedRobot {

	private byte scanDirection = 1;
	
	public void run() {
		setAdjustRadarForGunTurn(true);
		// Turn the scanner until we find an enemy robot
		setTurnRadarRight(36000);
		execute(); // you must call this!!!
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		// When we scan a robot, turn toward him
		setTurnRight(e.getBearing());

		// move a little closer
		if (e.getDistance() > 200)
			setAhead(e.getDistance() / 2);
		// but not too close
		if (e.getDistance() < 100)
			setBack(e.getDistance());

		// shoot at him
		setFire(3);

		// wobble the radar to generate another scan event
		scanDirection *= -1;
		setTurnRadarRight(36000 * scanDirection);
	}
}
