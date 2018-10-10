/**
 * <p><b>File name: </b> EnemyTurret.java
 * @version 1.1
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 14.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models an enemy turret for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It contains code used by all enemy 
 * turrets to detect the player tank and fire a shell. It's line of
 * sight bounces off walls as many times as the shells each type of enemy turret fires.
 * If it detects the player, it fires a shell, unless it already has
 * an x amount (depends on the subtype) shells fired by it still in the world. It 
 * also has a cooldown period, after which it will not fire another shell (each 
 * subtype has a different value for this period).
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Changed the code to allow enemy turrets to fire normal shells or 
 * rocket shells also.
 */

public class EnemyTurret extends Turret
{
	/**The approximate length in cells(pixels) between points on the line of
	 * sight where we check if the player is there. It's value is {@value}.*/
	private static final int DETECT_INTERVAL=10;
	
	/**The number of intervals away from this turret's tank's centre where the 
	 * search for the target will start, to make sure the search does not stop 
	 * immediately because it checks a point inside this turret's tank.*/
	private static final int NR_INTERVALS_FROM_TANK=3;
	
	/**The speed at which this turret turns left or right.*/
	protected static final int TURN_SPEED=1;
	
	/**A boolean that says whether the turret has finished turning to the last 
	 * position that has been generated, and whether we need to start another
	 * turn.*/
	protected boolean finishTurn;
	
	/**The next angle at which the turret must turn towards. It is a value in
	 * degrees from 0 to 359.*/
	protected int nextRotation;
	
	/**The amount of degrees the turret turns until the next angle, as stored
	 * in nextRotation. If this value is positive, it turns clockwise, anticlockwise
	 * if otherwise.*/
	protected int nextTurn;
	
	/**The last time, in milliseconds, that the turret has fired.*/
	private long lastFiring;
	
	/**
	 * Makes a new enemy Turret on the Tank given as an argument.
	 * @param tank The Tank on which this Turret will pe placed.
	 */
	public EnemyTurret(Tank tank)
	{
		super(tank);
		
		/*No new rotation position has been generated so we need to generate a
		 * new one, which is why this is initialised as true.*/
		finishTurn=true;
		
		//no shell has been fired
		lastFiring=0;
	}
	
	/**
	 * Fires a shell if it has detected the player, if the cooldown period has
	 * passed and if the limit of live shells in the world fire by this turret
	 * has not been reached.
	 */
	@Override
	public void fire()
	{
		/*We only fire if the player tank has been detected.*/
		if(detectTarget())
		{
			/*Even then, we only fire if the cooldown period has passed and 
			 * the live shells in the world limit has not been reached.*/
			if((lastFiring+getFireCooldown()<System.currentTimeMillis()) &&
					liveShells<getLiveShellLimit())
			{
				/*We fire the shell, update lastFiring and increment the counter
				 * of live shells in the world fired by this turret.*/
				super.fire();
				lastFiring=System.currentTimeMillis();
			}
		}
	}
	
	/**
	 * Moves the turret around, according to what the calculateTurn() method 
	 * sets the next rotation of this turret as.
	 */
	@Override
	public void aim()
	{	
		/*We check if we need to generate a new angle to rotate towards or 
		 * finish the current rotation, based on the value of finishTurn.*/
		if(finishTurn)
		{
			calculateTurn();
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
				/*Decide the correct direction.
				 * If nextTurn is positive, the turret turns clockwise.*/
				if(nextTurn>=0)
				{
					turn(TURN_SPEED);
				}
				/*If it is negative, the turret turns anti clockwise*/
				else
				{
					turn(-TURN_SPEED);
				}
			}
		}
	}
	
	/**
	 * Method detects if the player tank is in the line of fire of this turret.
	 * @return True if the player tank is in the line of fire of this turret,
	 * false if not or if the shell would hit another enemy tank before it would
	 * hit the player.
	 */
	private boolean detectTarget()
	{
		//counts the number of times the line of sight has bounced off walls
		int bounces=0;
		
		/*The length of the x axis component of the interval between points at which
		 * we look for the player.*/
		double xInterval=DETECT_INTERVAL * Math.cos(Math.toRadians(getRotation()));
		
		/*The length of the y axis component of the interval between points at which
		 * we look for the player.*/
		double yInterval=DETECT_INTERVAL * Math.sin(Math.toRadians(getRotation()));
		
		/*The x coordinate relative to this turret's position of the point where
		 * we look for the player. We first start looking at a distance of 
		 * DETECT_INTERVAL*NR_INTERVALS_FROM_TANK, so that the first point we check 
		 * is guaranteed to be outside of this turrets tank, so this method does not 
		 * return false just after starting the search.*/
		double xRealOffset=NR_INTERVALS_FROM_TANK*xInterval;
		
		/*The y coordinate relative to this turret's position of the point where
		 * we look for the player. We first start looking at a distance of 
		 * DETECT_INTERVAL*NR_INTERVALS_FROM_TANK, so that the first point we check 
		 * is guaranteed to be outside of this turrets tank, so this method does not 
		 * return false just after starting the search.*/		
		double yRealOffset=NR_INTERVALS_FROM_TANK*yInterval;
		
		//says whether to continue looking for the player or stop
		boolean cont=true;
		
		/*The detection works by checking if the player tank is at some points
		 * in the world relative to this turret. These points have a distance
		 * between of roughly DETECT_INTERVAL and roughly form a line starting 
		 * from the turret and towards where the turret points and extending
		 * untill it hits a wall, at which point the line bounces just like a 
		 * shell would. cont is set to false if the line can not bounce anymore
		 * (because it has already bounced as many times as the shells this turret
		 * fires do) or if this turret's tank is detected in it's line of sight
		 * (so that it won't fire a shell that destroys this tank before it can
		 * reach the player).*/
		while(cont)
		{
			/*Greenfoot Worlds work in integers not real numbers, so we approximate
			 * to the closest integer bigger than the real offsets*/
			int xOffset=(int) Math.round(xRealOffset);
			int yOffset=(int) Math.round(yRealOffset);
			
			/*Get the Tank and WallBlock located at the point we 
			 * are checking*/
			Tank tank= (Tank) getOneObjectAtOffset(xOffset,yOffset,Tank.class);
			WallBlock wall=(WallBlock) getOneObjectAtOffset(xOffset,yOffset,
					WallBlock.class);
			
			/*Check if there is a tank at the point we are checking.*/
			if(tank!=null)
			{
				//See if the tank is a player tank
				if(tank.getClass()==PlayerTank.class)
				{
					//if so, return true to fire in the player
					return true;
				}
				/*Else, the tank is an enemy tank and we return false so it 
				 * does not fire in other enemies. */
				else
				{
					return false;
				}
			}
			
			/*Else, it sees if there is a wall at the point we checked it bounces 
			 * or it stops it's search.*/
			if(wall!=null)
			{
				/*Check if the line of sight can bounce again.*/
				if(bounces<getShellBounceLimit())
				{
					/*If it can, we need to find out the exact point where the
					 * wall starts, since the last point we checked for a wall 
					 * may already be inside the wall block, creating imprecision
					 * for the turret targeting.*/
					
					/*Reset the offsets to the last point before the wall.*/
					xRealOffset-=xInterval;
					xOffset=(int) Math.round(xRealOffset);
					
					yRealOffset-=yInterval;
					yOffset=(int) Math.round(xRealOffset);
					
					/*Starting from the the last point before the wall, we check
					 * points at an interval of 1 cell/pixel to find the exact
					 * point the wall starts. Therefore, the temporary intervals
					 * we use for this are the intervals we used so far, divided
					 * by the detect interval.*/
					double xTempInterval=xInterval/DETECT_INTERVAL;
					double yTempInterval=yInterval/DETECT_INTERVAL;
					
					/*While there is no wall at the point we are checking at, 
					 * increase the offset with the value of the temporary offsets.
					 * The loop will thus terminate when we reach a point on the
					 * surface of the wall block.*/
					while(getOneObjectAtOffset(xOffset,yOffset,WallBlock.class)
							==null)
					{
						xRealOffset+=xTempInterval;
						yRealOffset+=yTempInterval;
						
						/*Recalculate the integer approximations so that the loop
						 * condition will check a new point for the wall.*/
						xOffset=(int) Math.round(xRealOffset);
						yOffset=(int) Math.round(yRealOffset);
					}
					
					/*We get the diagonal quarter of the wall block where 
					 * where the line of sight intersects this wall block.*/
					String quadrant=wall.getQuadrant(getX()+xOffset, getY()+yOffset);
					
					/*We change the direction of the line of sight accordingly.*/
					if(quadrant.equals("left") || quadrant.equals("right"))
					{
						/*if it touches the left or right side of the block,
						 * the horizontal direction is switched*/
						xInterval= -xInterval;
					}
					/*else if it touches the top or bottom side of the block,
					 * the vertical direction is switched*/
					else if(quadrant.equals("top") || quadrant.equals("bottom"))
					{
						yInterval= -yInterval;
					}
					
					//increment the counter of bounces
					bounces++;
				}
				/*If it can not bounce again, we stop out search*/
				else
				{
					cont=false;
				}
			}
			
			/*If the point we checked is beyond the edge of the world, it stops
			 * the search*/
			if((getX()+xOffset>=1000) || (getX()+xOffset<0) || (getY()+yOffset>=800)
					|| (getY()+yOffset<0))
			{
				cont=false;
			}
			
			/*Increase the offsets for the next point we search.*/
			xRealOffset+=xInterval;
			yRealOffset+=yInterval;
		}
		
		/*If the search has stopped and it has not found the target, it returns
		 * false.*/
		return false;
	}
	
	/**Calculates how the turret should turn next. It modifies the nextRotation,
	 * nextTurn and finishTurn attributes. Does nothing by default since there
	 * is no set way an enemy turret moves. Should be overridden always.*/
	protected void calculateTurn()
	{
		
	}
	
	/**Gets the cool down period(in milliseconds) after which this turret can 
	 * fire another shell. This period is a static variable and is the same for
	 * all objects of this class. It returns 0 because this method is meant to 
	 * be always overridden.
	 * @return The period in milliseconds after which this turret can fire another
	 * shell, which is 0, unless overridden.*/
	public int getFireCooldown()
	{
		return 0;
	}
	
	/**
	 * Gets the maximum number of times a shell fired by this turret can bounce.
	 * Returns the bounce limit of the type of shells fired by this turret.
	 * @return The bounce limit of the type of shells fired by this turret.
	 */
	public int getShellBounceLimit()
	{
		/*Check if this turret is firing normal shells.*/
    	if(getShellType()==Shell.class)
    	{
    		//if so, return the amount of times normal shells can bounce
    		return Shell.TIMES_ALLOWED_TO_BOUNCE;
    	}
    	/*Else, check if this turret is firing rocket shells.*/
    	else if(getShellType()==RocketShell.class)
    	{
    		//if so, return the amount of times rocket shells can bounce
    		return RocketShell.TIMES_ALLOWED_TO_BOUNCE;
    	}
    	else
    	{
    		/*If the shell type is something different, returning it's type
    		 * bounce limit must be hardcoded. So if it is not, an exception
    		 * is thrown.*/
    		throw new IllegalStateException("The type of shell fired by this "
    				+ "turred is not recognized by this method.");
    	}
	}
}
