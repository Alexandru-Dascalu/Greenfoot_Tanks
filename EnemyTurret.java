import greenfoot.Greenfoot;
import greenfoot.World;

/**
 * <p><b>File name: </b> EnemyTurret.java
 * @version 1.1
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 14.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>EnemyTurret.java is part of Panzer Batallion.
 * Panzer Batallion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * <p>You should have received a copy of the GNU General Public License v3
 * along with this program.  If not, see <a href="https://www.gnu.org/licenses/">https://www.gnu.org/licenses/</a> .
 * 
 * <p>A summary of the license can be found here: 
 * <a href="https://choosealicense.com/licenses/gpl-3.0/">https://choosealicense.com/licenses/gpl-3.0/</a> .
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

public abstract class EnemyTurret extends Turret
{
	/**The approximate length in cells(pixels) between points on the line of
	 * sight where we check if the player is there. Its value is {@value}.*/
	private static final int DETECT_INTERVAL=10;
	
	/**The number of intervals away from this turret's tank's centre where the 
	 * search for the target will start, to make sure the search does not stop 
	 * immediately because it checks a point inside this turret's tank.
	 * Its value is {@value}.*/
	private static final int NR_INTERVALS_FROM_TANK=5;

	/**The next angle at which the turret must turn towards. It is a value in
	 * degrees from 0 to 359.*/
	protected int nextRotation;
	
	/**The last time, in milliseconds, that the turret has fired.*/
	private long lastFiring;
	
	/**A reference to the player tank of the game world this turret is in.*/
	private PlayerTank playerTank;
	
	/**
	 * Makes a new enemy Turret on the Tank given as an argument.
	 * @param tank The Tank on which this Turret will pe placed.
	 */
	public EnemyTurret(Tank tank)
	{
		super(tank);
		
		//no shell has been fired
		lastFiring = 0;
		nextRotation = 0;
	}
	
	/**
	 * Prepares this turret to be added in the game world.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		//get the reference to the player tank of the world
		playerTank=((TankWorld)world).getPlayerTank();
		super.addedToWorld(world);
	}
	
	/**
	 * Fires a shell if it has detected the player, if the cool down period has
	 * passed and if the limit of live shells in the world fire by this turret
	 * has not been reached.
	 */
	@Override
	public void fire()
	{
		/*We only fire if the player tank has been detected.*/
		if(detectPlayer())
		{
			/*Even then, we only fire if the cool down period has passed and 
			 * the live shells in the world limit has not been reached.*/
			if((lastFiring + getFireCooldown() < System.currentTimeMillis()) &&
					liveShells < getLiveShellLimit())
			{
				/*We fire the shell, update lastFiring and increment the counter
				 * of live shells in the world fired by this turret.*/
				//super.fire();
				lastFiring=System.currentTimeMillis();
			}
		}
	}
	
	/**
	 * Moves the turret around, according to what the calculateTurn() method 
	 * sets the next rotation of this turret as.
	 */
	public void aim()
	{	
		int nextTurn = getTurnDirection();
		
		/* Check if the turret has reached nextRotation. */
		if (nextTurn == 0) 
		{
			calculateNextRotation();
		}
		/* Else, the needs to slowly turn in the correct direction. */
		else 
		{
			turn(nextTurn);
		}
	}
	
	/**
	 * Method detects if the player tank is in the line of fire of this turret.
	 * @return True if the player tank is in the line of fire of this turret,
	 * false if not or if the shell would hit another enemy tank before it would
	 * hit the player.
	 */
	private boolean detectPlayer()
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
		boolean lookingForPlayer=true;
		
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
		while(lookingForPlayer)
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
					lookingForPlayer=false;
				}
			}
			
			/*If the point we checked is beyond the edge of the world, it stops
			 * the search*/
			if((getX()+xOffset>=TankWorld.LENGTH) || (getX()+xOffset<0) || (getY()+yOffset>=TankWorld.WIDTH)
					|| (getY()+yOffset<0))
			{
				lookingForPlayer=false;
			}
			
			/*Increase the offsets for the next point we search.*/
			xRealOffset+=xInterval;
			yRealOffset+=yInterval;
		}
		
		/*If the search has stopped and it has not found the target, it returns
		 * false.*/
		return false;
	}
	
	/**Calculates where the turret should turn next. It modifies the nextRotation
	 * attribute. Makes the turret follow the player around, it does not point at 
	 * the player exactly, but with a random offset.*/
	protected void calculateNextRotation()
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
		int aimDifference=Greenfoot.getRandomNumber(getAimAngle())-(getAimAngle()/2);
		
		/*The next rotation this turret should reach is obtained by adding 
		 * the random number of degrees to the rotation need to make the 
		 * turret point towards the player tank.*/
		nextRotation=(int)Tank.normalizeAngle(nextRotation+aimDifference);
	}
	
	/**
	 * Calculates which way the turret should turn so that it turns towards 
	 * its target rotation.
	 * @return -1 if the target rotation is to the left, 1 if it is to the right,
	 * 0 if the turret has arrived at its desired rotation.
	 */
	protected int getTurnDirection()
	{
		/*Calculate the clockwise and counter clockwise differences between
		 * the desired rotation and the current rotation of the turret to 
		 * decide which way the turret will turn.*/
		int clockwiseDiff=(int)Tank.normalizeAngle(nextRotation-getRotation());
		int counterClockwiseDiff=(int)Tank.normalizeAngle(getRotation()-nextRotation);
		int nextTurn;
		
		//check if the rotation of this tank is the same as the target rotation
		if(clockwiseDiff == 0 || counterClockwiseDiff == 0)
		{
			nextTurn = 0;
		}
		/*Else, Check if it is shorter for the turret to turn clockwise.*/
		else if(clockwiseDiff<counterClockwiseDiff)
		{
			//if it is, it will turn clockwise
			nextTurn = 1;
		}
		else
		{
			/*else it will turn counter clockwise. The differences are positive 
			 * values, and nextTurn is set to a negative one because the turn(int)
			 * method turns the actor counter clockwise only if the argument is 
			 * negative.*/
			nextTurn = -1;
		}
		
		return nextTurn;
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
        /*Else, check if this turret is firing rocket shells MK2.*/
        /*else if(getShellType()==RocketShellMk2.class)
        {
        	//if so, return the amount of times rocket shells can bounce
        	return RocketShellMk2.TIMES_ALLOWED_TO_BOUNCE;
        }*/
        else
        {
        	/*If the shell type is something different, returning it's type
        	 * bounce limit must be hard coded. Since this method does not 
        	 * recognize it, throw an exception.*/
        	throw new IllegalStateException("The type of shell fired by this "
        			+ "turret is not recognized by this method.");
        }
	}
	
	/**Gets the cool down period(in milliseconds) after which this turret can 
	 * fire another shell. This period is a static variable and is the same for
	 * all objects of this class. It returns the maximum value of an integer so 
	 * that it will not fire a shell until the game is played for 600 hours 
	 * non-stop, unless the is overridden.
	 * @return The period in milliseconds after which this turret can fire another
	 * shell.*/
	public int getFireCooldown()
	{
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Gets the size in degrees of the angle of an imaginary cone whose axis extends
	 * to the position of the player tank. In this angle the turret moves 
	 * randomly. Returns 360 unless overridden, so the turret would aim around randomly.
	 * @return The aim angle of this type of turret in relation to the player tank.
	 */
	public int getAimAngle()
	{
		return 360;
	}
}

