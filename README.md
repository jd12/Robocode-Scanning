# Robocode-Scanning

In this project, you'll explore how the scanning in Robocode works and how to build classes.

# Robot Senses

We'll begin this lesson by discussing your robot's senses. It has only a few.

### Sense of Touch

Your robot knows when it's:

1. hit a wall ([onHitWall](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onHitWall(robocode.HitWallEvent))),
2. been hit by a bullet ([onHitByBullet](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onHitByBullet(robocode.HitByBulletEvent))),
3. or hit another robot ([onHitRobot](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onHitRobot(robocode.HitRobotEvent))).

All of these methods pass you events that give you information about what you touched. (See the links to the on-line docs, above.)

### Sense of Sight

Your robot knows when it's seen another robot, but only if it scans it ([onScannedRobot](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onScannedRobot(robocode.ScannedRobotEvent))).

Scan events are arguably the most important of all events. Scan events give you information about the other robots on the battlefield. (Some robots put 90+% of their code in the onScannedRobot method.) The only way scan events can be generated (practically speaking) is if you move your radar. (If an enemy robot wanders in front of your radar it will generate a scan event, but you should really take a more proactive stance.)

Also, remember in Battlefield Basics, the scanner is the fastest moving part of your robot, so don't be stingy about moving it.

If you want to, you can make the robots' scan arcs visible by selecting Options Menu -> Preferences -> View Options Tab and click on the "Visible Scan Arcs" checkbox. This is handy when debugging. Another handy debugging aid: you can pause / unpause the program by hitting the 'Alt' key.

## Other Senses

Your robot also knows when he's died ([onDeath](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onDeath(robocode.DeathEvent))), when another robot has died ([onRobotDeath](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onRobotDeath(robocode.RobotDeathEvent))), or when he's won the round ([onWin](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onWin(robocode.WinEvent)) -- this is where you write the code for your victory dance. Side note: if you want to test your victory dance, you can start the round with just one bot: yours).

Your robot also is aware of his bullets and knows when a bullet has hit an opponent ([onBulletHit](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onBulletHit(robocode.BulletHitEvent))), when a bullet hits a wall ([onBulletMissed](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onBulletMissed(robocode.BulletMissedEvent))), when a bullet hits another bullet ([onBulletHitBullet](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#onBulletHitBullet(robocode.BulletHitBulletEvent))).

# Building A Better Robot

## Serial Movements with The Robot Class

You want to find other robots to kill. To do that, you need to scan for other robots. The simplest approach to scanning is to just turn the radar around and around. Your run method could look something like this:

```java
public void run() {
	setAdjustRadarForRobotTurn(true);
	while (true) {
		turnRadarRight(360);
	}
}
```
(Note that we call [setAdjustRadarForRobotTurn(true)](http://mark.random-article.com/robocode/javadoc/robocode/Robot.html#setAdjustRadarForRobotTurn(boolean)) so as to have radar movement independent from gun movement.)

Indeed, our beloved BearingBot from earlier did exactly that: he rotates his radar and moves closer to whoever he scans.

You may have noticed that BearingBot has a large defect that you can see if you match him up against an opponent that moves a lot (like, say, Crazy). He scans an opponent, then moves to where he saw him, and by the time he gets there, the opponent has moved away.

## Compound Movements with the AdvancedRobot Class

It would be great if we could do more than one thing at once (scan AND turn AND fire). Thankfully, the powers that be have provided us with a means to accomplish this: The [AdvancedRobot](http://mark.random-article.com/robocode/javadoc/robocode/AdvancedRobot.html) base class, which allows us to make non-blocking calls to move the robot and then executes them all as a compound action. Crazy and SpinBot (and oddly enough SittingDuck) are all examples of advanced robots.

To change your robot to an advanced robot, just change your class declaration from:
```java
class MyRobot extends Robot {
	...
 ```
to
```java
class MyRobot extends AdvancedRobot {
	...
```
Now you're inheriting from `AdvancedRobot` rather than `Robot`; now you can use the set* methods provided by the AdvancedRobot class. (We will discuss more about inheritance later.)

Sample robot: [AdvancedBearingBot](http://mark.random-article.com/robocode/lessons/AdvancedBearingBot.java) - A great improvement over 'BearingBot', because he can do compound movements.

Sample robot: [AdvancedTracker](http://mark.random-article.com/robocode/lessons/AdvancedTracker.java) - This is a modification of the 'Tracker' sample robot. Per the source code for Tracker, notice how much he improves when you turn him into an AdvancedRobot.

Important Note: If you make a robot derived from AdvancedRobot, you must call the `execute()` method to perform all the queued-up actions. If you forget to do that, your bot will become disabled. You can click on your bot's button on the right to see the error message from the system.

But AdvancedBearingBot has another large defect which you can see if you match him up against more than one opponent: he goes driving all over the battlefield chasing one robot after another and doesn't get a lot accomplished. This is because his radar keeps scanning robots, and he chases every one he scans. In short, he lacks focus.

