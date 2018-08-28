
public class EnemyTurret extends Turret
{
	/**The approximate length in cells(pixels) between points on the line of
	 * sight where we check if the player is there. It's value is {@value}.*/
	private static final int DETECT_INTERVAL=10;
	
	/**A boolean that says whether the turret has finished turning to the last 
	 * position that has been generated, and whether we need to start another
	 * turn.*/
	protected boolean finishTurn;
	
	/**The next angle at which the turret must turn towards. It is a value in
	 * degrees from 0 to 359.*/
	protected int nextRotation;
	
	/**The amount of degrees the turret turns untill the next angle, as stored
	 * in nextRotation. If this value is positive, it turns clockwise, anticlockwise
	 * if otherwise.*/
	protected int nextTurn;
	
	/**The last time, in milliseconds, that the turret has fired.*/
	private long lastFiring;
	
	/**
	 * Makes a new enemy Turret on the Tank given as an argument.
	 * @param brownTank The Brown Tank on which this Turret will pe placed.
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
	 * Method detects if the player tank is in the line of fire of this turret.
	 * @return True if the player tank is in the line of fire of this turret,
	 * false if not or if the shell would hit this turret's tank before it would
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
		 * DETECT_INTERVAL away from the turret, so it is initialised as 3*xInterval,
		 * so that the first point we check is guaranteed to be outside of this turrets
		 * tank, so this method does not return false just after starting the search.*/
		double xRealOffset=3*xInterval;
		
		/*The y coordinate relative to this turret's position of the point where
		 * we look for the player.We first start looking at a distance of 
		 * DETECT_INTERVAL away from the turret, so it is initialised as 3*yInterval,
		 * so that the first point we check is guaranteed to be outside of this turrets
		 * tank, so this method does not return false just after starting the search.*/
		double yRealOffset=3*yInterval;
		
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
				if(bounces<Shell.TIMES_ALLOWED_TO_BOUNCE)
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
	
	public int getFireCooldown()
	{
		return 0;
	}
	
	public int getLiveShellLimit()
	{
		return 0;
	}
}
