import greenfoot.*;

/**
 * <p><b>File name: </b> BrownTurret.java
 * @version 1.3
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 21.06.2018
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
 */

public class BrownTurret extends Turret
{
	/**The approximate length in cells(pixels) between points on the line of
	 * sight where we check if the player is there. It's value is {@value}.*/
	private static final int DETECT_INTERVAL=10;
	
	/**The cooldown period in miliseconds after firing which the turret will not 
	 * fire again. It's value is {@value}.*/
	private static final int FIRE_COOLDOWN=1500;
	
	/**The maximum number of shells that the turret has fired that can be in the 
	 * world at the same time. It's value is {@value}.*/
	private static final int SHELLS_ALLOWED=2;
	
	/**A boolean that says whether the turret has finished turning to the last 
	 * position that has been generated, and whether we need to start another
	 * turn.*/
	private boolean finishTurn;
	
	/**The next angle at which the turret must turn towards. It is a value in
	 * degrees from 0 to 359.*/
	private int nextRotation;
	
	/**The amount of degrees the turret turns untill the next angle, as stored
	 * in nextRotation. If this value is positive, it turns clockwise, anticlockwise
	 * if otherwise.*/
	private int nextTurn;
	
	/**The last time, in milliseconds, that the turret has fired.*/
	private long lastFiring;
	
	/**
	 * Makes a new Brown Turret on the Brown Tank given as an argument.
	 * @param brownTank The Brown Tank on which this Turret will pe placed.
	 */
	public BrownTurret(Tank brownTank)
	{
		super(brownTank);
		
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
			if((lastFiring+FIRE_COOLDOWN<System.currentTimeMillis()) &&
					liveShells<SHELLS_ALLOWED)
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
		 * DETECT_INTERVAL away from the turret, so it is initialised as xInterval.*/
		double xRealOffset=xInterval;
		
		/*The y coordinate relative to this turret's position of the point where
		 * we look for the player.We first start looking at a distance of 
		 * DETECT_INTERVAL away from the turret, so it is initialised as yInterval.*/
		double yRealOffset=yInterval;
		
		//says whether to continue looking for the player or stop
		boolean cont=true;
		
		/*Tells whether to ignore the presence of this turret's tank in the 
		 * line of sight. It does not fire if this tank comes before the
		 * player, but because the detect interval is only 10 the first few
		 * points where we check are within the this turret's tank, so we ignore
		 * this tank until we check the first point outside of this tank,
		 * after which detection works as normal.*/
		boolean ignoreTank=true;
		
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
			
			/*Get the Player Tank, Brown Tank and WallBlock located at the point we 
			 * are checking*/
			PlayerTank target=(PlayerTank) getOneObjectAtOffset(xOffset,yOffset,
					PlayerTank.class);
			BrownTank brownTank=(BrownTank) getOneObjectAtOffset(xOffset,yOffset,
					BrownTank.class);
			WallBlock wall=(WallBlock) getOneObjectAtOffset(xOffset,yOffset,
					WallBlock.class);
			
			/*We check if there is a Brown Tank at the point.*/
			if(brownTank!=null)
			{
				/*If there is, we check if we ignore it.*/
				if(!ignoreTank)
				{
					/*if we do not need to ignore it, it doesn't fire so it will 
					 * not destroy itself.*/
					return false;
				}
			}
			/*Else, the point we are checking is outside the tank, so we do not
			 * need to ignore this turret's tank anymore.*/
			else
			{
				ignoreTank=false;
			}
			
			/*If there is a Player Tank at the point we checked, then it has
			 * found it's target and it returns true.*/
			if(target!=null)
			{
				return true;
			}
			/*Else, it sees if there is a wall at the point we checked it bounces 
			 * or it stops it's search.*/
			else if(wall!=null)
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
	
	/**
	 * Randomly moves the turret around.
	 */
	@Override
	public void aim()
	{	
		/*We check if we need to generate a new angle to rotate towards or 
		 * finish the current rotation, based on the value of finishTurn*/
		if(finishTurn)
		{
			/*If the previous turn was finished, we generate a new angle.
			 * We would like this turret to have varried movements, but then if
			 * the generated angle is big it would turn around too much. Therefore,
			 * we generate a random number (turnChance), based on which we will
			 * decide the upper limit of the degrees the turret will turn.*/
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
					turn(1);
				}
				/*If it is negative, the turret turns anti clockwise*/
				else
				{
					turn(-1);
				}
			}
		}
	}
}
