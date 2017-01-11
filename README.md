# Robocode-Scanning

In this project, you'll explore how the scanning in Robocode works and how to build classes.

## Robot Senses

We'll begin this lesson by discussing your robot's senses. It has only a few.

### Sense of Touch

Your robot knows when it's:

1. hit a wall ([onHitWall](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onHitWall(robocode.HitWallEvent))),
2. been hit by a bullet ([onHitByBullet](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onHitByBullet(robocode.HitByBulletEvent))),
3. or hit another robot ([onHitRobot](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onHitRobot(robocode.HitRobotEvent))).

All of these methods pass you events that give you information about what you touched. (See the links to the on-line docs, above.)

### Sense of Sight

Your robot knows when it's seen another robot, but only if it scans it (onScannedRobot).

Scan events are arguably the most important of all events. Scan events give you information about the other robots on the battlefield. (Some robots put 90+% of their code in the onScannedRobot method.) The only way scan events can be generated (practically speaking) is if you move your radar. (If an enemy robot wanders in front of your radar it will generate a scan event, but you should really take a more proactive stance.)

Also, remember in Battlefield Basics, the scanner is the fastest moving part of your robot, so don't be stingy about moving it.

If you want to, you can make the robots' scan arcs visible by selecting Options Menu -> Preferences -> View Options Tab and click on the "Visible Scan Arcs" checkbox. This is handy when debugging. Another handy debugging aid: you can pause / unpause the program by hitting the 'Alt' key.

Other Senses

Your robot also knows when he's died (onDeath), when another robot has died (onRobotDeath), or when he's won the round (onWin -- this is where you write the code for your victory dance. Side note: if you want to test your victory dance, you can start the round with just one bot: yours).

Your robot also is aware of his bullets and knows when a bullet has hit an opponent (onBulletHit), when a bullet hits a wall (onBulletMissed), when a bullet hits another bullet (onBulletHitBullet).
