import greenfoot.*;

public class TurquoiseTurret extends EnemyTurret
{
	/**The cooldown period in miliseconds after firing which the turret will not 
	 * fire again. It's value is {@value}.*/
	private static final int FIRE_COOLDOWN=1000;
	
	/**The maximum number of shells that the turret has fired that can be in the 
	 * world at the same time. It's value is {@value}.*/
	private static final int LIVE_SHELLS_ALLOWED=1;
	
	private static final int AIM_ANGLE=70;
	
	private PlayerTank playerTank;
	
	/**
	 * Makes a new Turquoise Turret on the Tank given as an argument.
	 * @param tank The tank on which this Turret will be placed.
	 */
	public TurquoiseTurret(Tank tank)
	{
		super(tank);
	}
	
	@Override
	public void aim()
	{	
		/*We check if we need to generate a new angle to rotate towards or 
		 * finish the current rotation, based on the value of finishTurn*/
		if(finishTurn)
		{
			TankWorld world=getWorldOfType(TankWorld.class);
			playerTank=world.getPlayerTank();
			
			double theta=Math.toDegrees(Math.atan2(playerTank.getY()-getY(), 
	    			playerTank.getX()-getX()));
			nextRotation=(int)Math.round(Tank.normalizeAngle(theta));
			/*We would like the turret to be able to turn in either direction,
			 * so we subtract from the random number half of the upper limit. This
			 * means we will get a number from AIM_ANGLE/2 to -AIM_ANGLE/2.*/
			int aimDifference=Greenfoot.getRandomNumber(AIM_ANGLE)-(AIM_ANGLE/2);
			
			nextRotation=(int)Tank.normalizeAngle(nextRotation+aimDifference);
			
			int clockwiseDiff=(int)Tank.normalizeAngle(nextRotation-getRotation());
			int counterClockwiseDiff=(int)Tank.normalizeAngle(getRotation()-nextRotation);
			if(clockwiseDiff<counterClockwiseDiff)
			{
				nextTurn=1;
			}
			else
			{
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
	
	@Override
	public int getFireCooldown()
	{
		return FIRE_COOLDOWN;
	}
	
	@Override
	public int getLiveShellLimit()
	{
		return LIVE_SHELLS_ALLOWED;
	}
}
