package lessons;

import robocode.*;

public class EnemyTracker extends AdvancedRobot {

	private EnemyBot enemy = new EnemyBot();

	public void run() {
		setAdjustRadarForGunTurn(true);
		enemy.reset();
		while (true) {
			doScanner();
			doMovement();
			doGun();
			execute(); // you must call this!!!
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		// check if we have no enemy or found the one we were tracking
		if (enemy.none() || e.getName().equals(enemy.getName())) {
			enemy.update(e);
			// turn toward the enemy, a la BearingBot
			setTurnRight(e.getBearing());
		}
	}

	public void onRobotDeath(RobotDeathEvent e) {
		// check if the enemy we were tracking died
		if (e.getName().equals(enemy.getName())) {
			enemy.reset();
		}
	}   

	void doScanner() {
		setTurnRadarRight(360);
	}

	void doMovement() {
		// turning here causes a weird behavior, prolly because we're working
		// with outdated information
		//setTurnRight(enemy.getBearing());

		// move a little closer
		if (enemy.getDistance() > 200)
			setAhead(enemy.getDistance() / 2);
		// but not too close
		if (enemy.getDistance() < 100)
			setBack(enemy.getDistance());
	}

	void doGun() {

		// don't fire if there's no enemy
		if (enemy.none()) return;

		// convenience variable
		double max = Math.max(getBattleFieldHeight(), getBattleFieldWidth());

		// only shoot if we're (close to) pointing at our enemy
		if (Math.abs(getTurnRemaining()) < 10) {
			if (enemy.getDistance() < max / 3) {
				// fire hard when close
				setFire(3);
			} else {
				// otherwise, just plink him
				setFire(1);
			}
		}
	}

}
