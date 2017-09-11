# Robocode-Scanning

In this project, you'll explore how the scanning in Robocode works and how to build classes.

# Robot Senses

We'll begin this lesson by discussing your robot's senses. It has only a few.

### Sense of Touch

Your robot knows when it's:

1. hit a wall ([onHitWall](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onHitWall(robocode.HitWallEvent))),
2. been hit by a bullet ([onHitByBullet](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onHitByBullet(robocode.HitByBulletEvent))),
3. or hit another robot ([onHitRobot](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onHitRobot(robocode.HitRobotEvent))).

All of these methods pass you events that give you information about what you touched. (See the links to the on-line docs, above.)

### Sense of Sight

Your robot knows when it's seen another robot, but only if it scans it ([onScannedRobot](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onScannedRobot(robocode.ScannedRobotEvent))).

Scan events are arguably the most important of all events. Scan events give you information about the other robots on the battlefield. (Some robots put 90+% of their code in the onScannedRobot method.) The only way scan events can be generated (practically speaking) is if you move your radar. (If an enemy robot wanders in front of your radar it will generate a scan event, but you should really take a more proactive stance.)

Also, remember in Battlefield Basics, the scanner is the fastest moving part of your robot, so don't be stingy about moving it.

If you want to, you can make the robots' scan arcs visible by selecting Options Menu -> Preferences -> View Options Tab and click on the "Visible Scan Arcs" checkbox. This is handy when debugging. Another handy debugging aid: you can pause / unpause the program by hitting the 'Alt' key.

## Other Senses

Your robot also knows when he's died ([onDeath](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onDeath(robocode.DeathEvent))), when another robot has died ([onRobotDeath](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onRobotDeath(robocode.RobotDeathEvent))), or when he's won the round ([onWin](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onWin(robocode.WinEvent)) -- this is where you write the code for your victory dance. Side note: if you want to test your victory dance, you can start the round with just one bot: yours).

Your robot also is aware of his bullets and knows when a bullet has hit an opponent ([onBulletHit](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onBulletHit(robocode.BulletHitEvent))), when a bullet hits a wall ([onBulletMissed](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onBulletMissed(robocode.BulletMissedEvent))), when a bullet hits another bullet ([onBulletHitBullet](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#onBulletHitBullet(robocode.BulletHitBulletEvent))).

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
(Note that we call [setAdjustRadarForRobotTurn(true)](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#setAdjustRadarForRobotTurn(boolean)) so as to have radar movement independent from gun movement.)

Indeed, our beloved BearingBot from earlier did exactly that: he rotates his radar and moves closer to whoever he scans.

You may have noticed that BearingBot has a large defect that you can see if you match him up against an opponent that moves a lot (like, say, Crazy). He scans an opponent, then moves to where he saw him, and by the time he gets there, the opponent has moved away.

## Compound Movements with the AdvancedRobot Class

It would be great if we could do more than one thing at once (scan AND turn AND fire). Thankfully, the powers that be have provided us with a means to accomplish this: The [AdvancedRobot](http://jd12.github.io/RobocodeInitial/robocode/AdvancedRobot.html) base class, which allows us to make non-blocking calls to move the robot and then executes them all as a compound action. Crazy and SpinBot (and oddly enough SittingDuck) are all examples of advanced robots.

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


# Locking Onto an Enemy

## Narrow Beam

We can easily lock onto our opponent by constantly turning the radar toward him whenever we scan him. Intuatively, you might think of doing something with the scanned robot's bearing like so:
```java
    public void onScannedRobot(ScannedRobotEvent e) {
		// Lock on to our target (I hope...)
		 setTurnRadarRight(e.getBearing());
		 ...
```
There's a problem with this, though: the `ScannedRobotEvent` gives us a bearing to the scanned robot but it is relative to our tank's position, not our radar's position. How do we resolve this little quandry?

Easy: we find the difference between our tank heading ([getHeading()](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#getHeading())) and our radar heading ([getRadarHeading()](http://jd12.github.io/RobocodeInitial/robocode/Robot.html#getRadarHeading())) and add the bearing to the scanned robot ([e.getBearing()](http://jd12.github.io/RobocodeInitial/robocode/ScannedRobotEvent.html#getBearing())), like so:
```java
    public void onScannedRobot(ScannedRobotEvent e) {
		// Lock on to our target (this time for sure)
		setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
		...
```
Sample robot: [NarrowBeam](http://mark.random-article.com/robocode/lessons/NarrowBeam.java) - Uses the above source to lock onto an opponent and nail him. Match him up against as many opponents as you want.

## Oscillating (or "Wobbling") the Radar

An alternative technique is to oscillate your radar. Every time you see an opponent, you whipsaw the radar back so as to focus on one robot and continuously generate scan events. This is an improvement over the narrow beam because you are a little more aware of nearby robots.

To make this work, you need a variable that keeps track of which direction to turn the radar, it will only ever have values of 1 and -1, so it can be small. You can learn more about different data types [here] (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html). You can declare it in your robot like so:

```java
class MyRobot extends AdvancedRobot {
	private byte scanDirection = 1;
	...
```

The run method can look just like the one above, but in the onScannedRobot method you do the following:

```java
public void onScannedRobot(ScannedRobotEvent e) {
	...
	scanDirection *= -1; // changes value from 1 to -1
	setTurnRadarRight(360 * scanDirection);
	...
```

Flipping the value of `scanDirection` creates the oscillating effect.

Sample robot: [Oscillator](http://mark.random-article.com/robocode/lessons/Oscillator.java) - wobbles his radar to keep track of his opponent. Note that while he tends to track an enemy, he'll chase others that are nearby, too.

# Assignment Part I: Add scanning to your bot

1. Convert your robot from a `Robot` to an `AdvancedRobot`
2. Add some scanning code, either the Narrow Beam or Oscillating technique. 

# Assignment Part II: Create EnemyBot class

Now we are going to create an EnemyBot that you are going to use to test your scanning abilities. You may find the following information useful. These are links to Sun's online Java tutorials.

[What Is a Class?](http://java.sun.com/docs/books/tutorial/java/concepts/class.html) - very basic primer on class basics

[Creating Classes](http://java.sun.com/docs/books/tutorial/java/javaOO/classes.html) - the main components of a class

[Data Types] (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) - information on primitive data types

## Specifications

Write a class that can hold the information about an enemy robot.

### Detail

1. In Robocode, go to Source Editor and create a new Java file called "EnemyBot.java". You will most likely have to change the name of the class to match the filename. Make sure this file is in the same package as your other robots
2. Make sure your package declaration is your initials/the same package name as the rest of your robots. Add `import robocode.*` right under your package declaration.
3. Add the following private variables to the class: bearing, distance, energy, heading, name, velocity. All of these will be of type double except for name which will be of type String. These are the attributes for your EnemyBot
4. Implement a public constructor which just calls the reset function(You haven't written the reset function yet, but you will in the next step). Note: the constructor must be the same name as the class. Also, constructors never specify a return value.
5. Implement another state-change method called reset which sets the name variable to the empty string ("") and all the variables of type double to 0.0. The reset method will also return void.
6. Implement a state-change method called update which takes a [ScannedRobotEvent](http://jd12.github.io/RobocodeInitial/robocode/ScannedRobotEvent.html) as a parameter. Call the ScannedRobotEvent's methods (same names as the ones in step #7) to set your private variables (step #3). The update method will return void.
7. Add the following public accessor methods to the class: getBearing(), getDistance(), getEnergy(), getHeading(), getName(), getVelocity(). These will all return the values in the private variables.
8. Implement a (state-reporting) accessor method called none which will return true if name is "" or false otherwise. (Remember to use the equals() method of the String class.) Basically, this method will return true if the reset method was just called.
 
Layout of the code

Kindly lay out your code like so:

```java
public class EnemyBot {

	// private data (attributes)

	// constructor

	// mutator methods (reset & update) 

	// accessor methods (get*)

	// state methods (none) 

}
```

# Part III: Improve your Scanning 

Now you will use your Enemybot class to improve your scanning techniques

## The Problem with NarrowBeam

A recurring theme in the computer industry is that the solution to one problem leads to the creation of another. Fittingly, having solved the problem of how to lock onto a target, we are now faced with another problem, which the following screenshot illustrates:

![Image of NarrowBeam]
(http://mark.random-article.com/robocode/narrow_beam.jpg)

Note that NarrowBeam has locked on so tightly to Crazy that he is blithely unaware that Tracker is killing him from behind.

This is the problem with NarrowBeam: other robots can still sneak up behind and he'll never know they're there. Oscillator does a little better than this by having a wider scan range, but he tends to get a little unfocused, at times.

We could scan around periodically, but that would mean losing our target! What's a bot to do?

## Enemy Tracking

### Using the EnemyBot Class

A further improvement that could be made would be to single out an individual robot, focus on him, and destroy him completely. The sample robot Tracker does this to a limited extent, but we'll do one better with the EnemyBot class you guys all wrote.

To keep track of the information about an enemy robot, you first need to make a member variable in your robot like so:

```java
class MyRobot extends AdvancedRobot {
	private EnemyBot enemy = new EnemyBot();
	...
```

You will want to reset (clear) your enemy at the top of your run method like so:

```java
public void run() {
	setAdjustRadarForRobotTurn(true);
	enemy.reset();
	while (true) {
		setTurnRadarRight(360);
		...
		execute();
	}
}
```

And you need to update the enemy's information in the onScannedRobot method like so:

```java
public void onScannedRobot(ScannedRobotEvent e) {
	// if we have no enemy or we found the one we're tracking..
	if (enemy.none() || e.getName().equals(enemy.getName())) {
		// track him!
		enemy.update(e);
	}
	...
```

From this point, you can use all of the information about the enemy in any other method of your class.

There is one last detail, though, if the enemy you're tracking dies, you'll want to reset it so you can track another. To do that, implement an onRobotDeath method like so:

```java
public void onRobotDeath(RobotDeathEvent e) {
	// if the bot we were tracking died..
	if (e.getName().equals(enemy.getName())) {
		// clear his info, so we can track another
		enemy.reset();
	}
}
```

**Sample robot:** [EnemyTracker](http://mark.random-article.com/robocode/lessons/EnemyTracker.java) - a robot that uses the EnemyBot class. Note that even though he rotates the radar, he just tracks one enemy. This allows him to keep an eye on what's going on in the rest of the battlefield while concentrating on his target.

### Slightly Smarter Tracking

Another small optimization could be made here: If a closer robot moves into view, we probably want to start shooting him instead. You can accomplish this by modifying your onScannedRobot method like so:

```java
public void onScannedRobot(ScannedRobotEvent e) {

	if (
		// we have no enemy, or..
		enemy.none() ||
		// the one we just spotted is closer, or..
		e.getDistance() < enemy.getDistance() ||
		// we found the one we've been tracking..
		e.getName().equals(enemy.getName())
		) {
		// track him!
		enemy.update(e);
	}
	...
```

And that's pretty good, but there's only one problem: if there are two equally close targets, your bot from spin back and forth between them rather indecicively.
To solve this problem, rather than just saying
```java
	// the one we just spotted is closer, or..
	e.getDistance() < enemy.getDistance() ||
```

you can instead bias the current target by saying

```java
	// the one we just spotted is _significantly_ closer, or..
	e.getDistance() < enemy.getDistance() - 70 ||
```

which will make sure that you only pick a new target if it is decisively closer.

**Sample robot:** [EnemyOrCloser](http://mark.random-article.com/robocode/lessons/EnemyOrCloser.java) uses the above scanning technique to hit the closest enemy. As he rotates his radar, he will begin tracking any enemy that is closer (even if someone sneaks up behind him).

### Improved Oscillation

You may be wondering how to wobble the scanner outside of the onScannedRobot method (rather than rotating). Here's how you can do it:

```java
double turn = getHeading() - getRadarHeading() + enemy.getBearing();
turn += 30 * scanDirection;
setTurnRadarRight(turn);
scanDirection *= -1;
```

Feel free to experiment with offset values other than 30.

**Sample robot:** [ImprovedOscillator](http://mark.random-article.com/robocode/lessons/ImprovedOscillator.java) - Note how this bot creates a perfect cone around the robot you are tracking. The disadvantage is that ImprovedOscillator can still be blindsided a la NarrowBeam.

# Assignment Part III: Improve your Tracking 

1. Have your robot use the EnemyBot class to track enemies better.
