import greenfoot.*;
/**
 * <p><b>File name: </b> TurquoiseTurret.java
 * @version 1.0
 * @since 14.08.2018
 * <p><b>Last modification date: </b> 14.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a turquoise enemy turret for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It roughly follows the position of 
 * the  player tank and fires at it if it can hit it. It cannot fire another 
 * shell if the cool down period has not passed and if there is another shell 
 * fired by this turret still in the world.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class TurquoiseTurret extends EnemyTurret
{
	/**The cooldown period in miliseconds after firing which the turret will not 
	 * fire again. It's value is {@value}.*/
	private static final int FIRE_COOLDOWN=1000;
	
	/**The maximum number of shells that the turret has fired that can be in the 
	 * world at the same time. It's value is {@value}.*/
	private static final int LIVE_SHELLS_ALLOWED=1;
	
	/**The size in degrees of the angle of an imaginary cone whose axis extends
	 * to the position of the player tank. In this angle the turret moves 
	 * randomly. It's value is {@value}.*/
	private static final int AIM_ANGLE=60;
	
	/**A reference to the player tank of the game world this turret is in.*/
	private PlayerTank playerTank;
	
	/**
	 * Makes a new Turquoise Turret on the Tank given as an argument.
	 * @param tank The tank on which this Turret will be placed.
	 */
	public TurquoiseTurret(Tank tank)
	{
		//just call the supertype constructor
		super(tank);
	}
	
	/**
	 * Prepares this turret to be added in the game world.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		//get the reference to the player tank of the world
		playerTank=((TankWorld)world).getPlayerTank();
	}
	
	@Override
	public void aim()
	{	
		/*We check if we need to generate a new angle to rotate towards or 
		 * finish the current rotation, based on the value of finishTurn*/
		if(finishTurn)
		{
			/*Get the value of the angle between the horizontal axis and the 
			 * line between this turret and the player tank.*/
			double theta=Math.toDegrees(Math.atan2(playerTank.getY()-getY(), 
	    			playerTank.getX()-getX()));
			
			//transform that angle into a positive integer between 0 and 359 
			nextRotation=(int)Math.round(Tank.normalizeAngle(theta));
			
			/*We would like the turret to be able to turn in either direction,
			 * so we subtract from the random number half of the upper limit. This
			 * means we will get a number from AIM_ANGLE/2 to -AIM_ANGLE/2.*/
			int aimDifference=Greenfoot.getRandomNumber(AIM_ANGLE)-(AIM_ANGLE/2);
			
			/*The next rotation this turret should reach is obtained by adding 
			 * the random number of degrees to the rotation need to make the 
			 * turret point towards the player tank.*/
			nextRotation=(int)Tank.normalizeAngle(nextRotation+aimDifference);
			
			/*Calculate the clockwise and counter clockwise differences between
			 * the desired rotation and the current rotation of the turret to 
			 * decide which way the turret will turn.*/
			int clockwiseDiff=(int)Tank.normalizeAngle(nextRotation-getRotation());
			int counterClockwiseDiff=(int)Tank.normalizeAngle(getRotation()-nextRotation);
			
			/*Check if it is shorter for the turret to turn clockwise.*/
			if(clockwiseDiff<counterClockwiseDiff)
			{
				//if it is, it will turn clockwise
				nextTurn=1;
			}
			else
			{
				//else it will turn counter clockwise.
				nextTurn=-1;
			}
			
			/*The turret has a new angle to turn towards now, so it has not
			 * finished it's current turn.*/
			finishTurn=false;
		}
		/*If the previous turn is not finished, the turret needs to turn in the
		 * correct direction until it reaches nextRotation.*/
		else
		{
			/*Check if the turret has reached nextRotation.*/
			if(nextRotation==getRotation())
			{
				/*If it has, we set finishTurn to true so that a new turn will
				 * be generated.*/
				finishTurn=true;
			}
			/*Else, the needs to slowly turn in the correct direction.*/
			else
			{
				turn(nextTurn);
			}
		}
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
	
	/**
	 * Indicates the type of shell fired by this turret.
	 * @return The type of shell fired by this turret, indicated by a Class 
	 * object.
	 */
	@Override
	public Class<?> getShellType()
    {
    	return RocketShell.class;
    }
}
