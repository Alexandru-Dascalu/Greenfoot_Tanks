import greenfoot.*;

/**
 * <p><b>File name: </b> BrownTurret.java
 * @version 1.6
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 29.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a brown turret for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is the most basic enemy
 * turret. It randomly turns around, looking for the player.It's line of
 * sight bounces off walls as many times as the shells it fires do.
 * If it detects the player, it fires a shell, unless it already has
 * 2 shells fired by it still in the world. It also has a cooldown period,
 * after which it will not fire another shell.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Made the random turning be more varied.
 * <p>	-1.2 - Made the turret detect the enemy player if there is a straight
 * line to it.
 * <p>	-1.3 - Made the turret's line of sight bounce off walls as many times
 * as the shells it fires do.
 * <p>	-1.4 - Made the aiming of the turret more accurate.
 * <p>	-1.5 - Moved code for firing and detecting targets to a new class called
 * EnemyTurret that is the supertype of all enemy tank turrets and added methods
 * to allow different enemy turrets to have different stats.
 * <p>	-1.6 - Added some static variables and getters for the stats of a brown 
 * turret to work with the standard code from the enemy turret class.
 */

public class BrownTurret extends EnemyTurret
{
	/**The cooldown period in miliseconds after firing which the turret will not 
	 * fire again. It's value is {@value}.*/
	private static final int FIRE_COOLDOWN=1400;
	
	/**The maximum number of shells that the turret has fired that can be in the 
	 * world at the same time. It's value is {@value}.*/
	private static final int LIVE_SHELLS_ALLOWED=2;
	
	/**
	 * Makes a new Brown Turret on the tank given as an argument.
	 * @param tank The tank on which this Turret will be placed.
	 */
	public BrownTurret(Tank tank)
	{
		//just call the supertype constructor
		super(tank);
	}
	
	/**Calculates how the turret should turn next. It modifies the nextRotation,
	 * nextTurn and finishTurn attributes. Makes the turret move randomly.*/
	@Override
	protected void calculateTurn()
	{
		/*We generate a new angle. We would like this turret to have varried
		 * movements, but then if the generated angle is big it would turn 
		 * around too much. Therefore, we generate a random number (turnChance),
		 * based on which we will decide the upper limit of the degrees the 
		 * turret will turn.*/
		int turnChance=Greenfoot.getRandomNumber(100);
		int upperLimit;
		
		/*Based on the value of turnChance, we decide the upperLimit in degree 
		 * of the next turn. This is because we would like the turret to have varied
		 * moves, so it will mostly make small turns, but sometimes it might
		 * also make huge turns.*/
		if(turnChance<60)
		{
			/*There is a 60% chance that the upperLimit is 160.*/
			upperLimit=160;
		}
		/*Otherwise, the upper limit is 720.*/
		else
		{
			upperLimit=720;
		}
	
		/*We would like the turret to be able to turn in either direction,
		 * so we subtract from the random number half of the upper limit. This
		 * means we will get a number either from -80 to 80, exclusive, or from
		 * -360 to 360, exclusive.*/
		nextTurn=Greenfoot.getRandomNumber(upperLimit)-(upperLimit/2);
		
		/*We need to know when to stop, so we calculate the rotation of the 
		 * turret after it will have turned nextTurn degrees. In Greenfoot 
		 * the rotation of an object is from 0 to 359, so we use MOD 360
		 * to ensure that, after we add nextTurn to the current rotation
		 * of the turret.*/
		nextRotation=(getRotation()+nextTurn)%360;
		
		/*nextTurn may be negative and thus nextRotation could also be.
		 * By adding 360 we get the equivalent positive angle.*/
		if(nextRotation<0)
		{
			nextRotation=360+nextRotation;
		}
	
		/*The turret has a new angle to turn towards now, so it has not
		 * finished it's current turn.*/
		finishTurn=false;
	}
	
	/**Gets the cool down period(in milliseconds) after which this turret can 
	 * fire another shell. This period is a static variable and is the same for
	 * all objects of this class.
	 * @return The period in milliseconds after which this turret can fire another
	 * shell.*/
	@Override
	public int getFireCooldown()
	{
		return FIRE_COOLDOWN;
	}
	
	/**Gets the limit of how many shells fired by this turret can be in the world
	 * at the same time. This number is a static variable and is the same for
	 * all objects of this class.
	 * @return the limit of how many shells fired by this turret can be in the world
	 * at the same time. */
	@Override
	public int getLiveShellLimit()
	{
		return LIVE_SHELLS_ALLOWED;
	}
}
