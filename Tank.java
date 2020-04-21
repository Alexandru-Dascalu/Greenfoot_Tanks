import greenfoot.Actor;
import greenfoot.GreenfootSound;
import greenfoot.World;

/**
 * <p><b>File name: </b> Tank.java
 * @version 1.7
 * @since 02.05.2018
 * <p><b>Last modification date: </b> 03.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>Tank.java is part of Panzer Batallion.
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
 * <p> This class models a general tank for a Greenfoot recreation of the Wii Tanks 
 * game for the Nintendo Wii. By default, it moves, plays a sound when moving
 * and tells it's turret to aim and fire. It is meant to be inherited always and
 * you should not have an actor that is just a Tank object since the class was
 * not meant to be used in this way.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created a simple tank that does not move.
 * <p>	-1.1 - Made the tank move using keyboard input.
 * <p>	-1.2 - Made the tank play a sound when moving.
 * <p>	-1.3 - Made the tank generate a simple tank turret on top if it when it
 * is added to the world.
 * <p>  -1.4 - Made the tank detect collisions with walls and not move into wall
 * blocks.
 * <p>  -1.5 - Modified this class to be a general tank class and moved part of 
 * code into the PlayerTank class, which inherits from this.
 * <p>	-1.6 - Modified this class so tanks can push each other while moving.
 * <p>	-1.7 - Added a getMaxTurnSpeed() method to be overridden in the player 
 * tank and mobile enemy tanks subclasses.
 */

public abstract class Tank extends Actor
{
	/**The file name of the sound that will play when the tank moves.*/
	protected static final String DRIVING_SOUND_NAME="tank_moving_1.wav";
	
	/**The length of half of the diagonal of a tanks image, in pixels. Used for
	 * detecting collisions with walls and other tanks. For the current image
	 * the value is {@value}.*/
	protected static final double HALF_DIAGONAL=42.07433897282;
	
	/**The length of the long side of the tank. Currently, the tank's 
	 * image is 51 by 49 pixels, so the value is {@value}.*/
	public static final int LENGTH=60;
	
	/**The length of the short side of the tank. Currently, the tank's 
	 * image is 51 by 49 pixels, so the value is {@value}.*/
	public static final int WIDTH=59;
	
	/**The angle in degrees between the length of the tank and it's diagonal.
	 * For the current image the value is {@value}.*/
	protected static final double ANGLE=44.5185341941;
	
	/**The GreenfootSound object associated with this tank. Used to make sure
	 * the sound only plays when this tank moves.*/
	protected GreenfootSound drivingSound;
	
	/**The tank turret of this tank. By default it is a simple turret.*/
	protected Turret tankTurret;
	
	/**The starting x position of the tank in the world. Needed so we know where
	 * to put the tank when the level is reloaded.*/
	protected final int startX;
	
	/**The starting y position of the tank in the world. Needed so we know where
	 * to put the tank when the level is reloaded.*/
	protected final int startY;
	
	/**The starting rotation of the tank when the level starts. It is a number 
	 * between 0 and 359.*/
	protected final int startRotation;
	
	/**The correct x position of the tank, represented by a real number.*/
	protected double realX;
	
	/**The correct y position of the tank, represented by a real number.*/
	protected double realY;
	
	/**The number of mines laid by this tank in the current level so far.*/
	protected int minesLaid;

	/**
	 * Makes a new tank object.
	 * @param startX The starting x position of the tank in the world.
	 * @param startY The starting y position of the tank in the world.
	 * @param startRotation The starting rotation of this tank in the world.
	 */
	public Tank(int startX, int startY, int startRotation)
	{
		super();
		
		/*Initialize variables that store the starting position of the tank and
		 * the sound object of this tank.*/
		this.startX=startX;
		this.startY=startY;
		this.startRotation=startRotation;
		realX=startX;
		realY=startY;
		minesLaid=0;
		
		drivingSound = new GreenfootSound(DRIVING_SOUND_NAME);
	}
	
	/**
	 * Act - do whatever the Tank wants to do. This method is called whenever the
	 * 'Act' or 'Run' button gets pressed in the environment. In this case, this
	 * method makes the tank play a sound if it moves and tells it's turret to 
	 * aim and fire.
	 */
	@Override
	public void act()
	{
		//playSound();
		pushOtherTanks();
		
		tankTurret.aim();
		try {
			tankTurret.fire();
		} catch (IllegalStateException e) {
			System.out.print(e.getMessage());
			System.out.println(tankTurret.getWorld());
			throw e;
		}
		
	}
	
	/**
     * Moves this tank by approximately the distance given as a parameter
     * in the direction it is currently facing. Overrides the default one so that
     * errors do not accumulate over time dues to the fact in Greenfoot actor
     * position is represented by integers and not real numbers. Since before
     * with each call of the method the rounding to the nearest integers would
     * add over time, we store the correct coordinates as doubles and set the 
     * location to a rounded integer of these values, precision is not lost 
     * with each call of this method. Makes the tanks actually move how they
     * should move, not with deviations like before.
     * @param distance The distance the tank will be moved in it's current 
     * direction.
     */
    @Override
    public void move(int distance)
	{
    	//calculate the rotation of the tank in radians
    	double radians = Math.toRadians(getRotation());
    	
    	/*Calculate the distance the tank should move by in each axis.*/
    	double dx = Math.cos(radians) * distance;
    	double dy = Math.sin(radians) * distance;
    	
    	//update the real x and y coordinates of the tank
    	realX+=dx;
    	realY+=dy;
    	
    	/*A world in Greenfoot is made up of finite cells, so positions can only
    	 * be integers. So we round to the nearest integers the value of the real
    	 * coordinates and set the location of the tank to these numbers.*/
    	int tempX=(int) Math.round(realX);
    	int tempY=(int) Math.round(realY);
    	setLocation(tempX, tempY);
    	
    	/*Check if this tank has a turret placed in the world.*/
    	if(tankTurret!=null && tankTurret.getWorld()!=null)
    	{
    		//If so, also move the turret by the same distance
    		tankTurret.setLocation(tempX, tempY);
    	}
	}
    
    /**
     * Accurately moves the only on the horizontal axis by the horizontal 
     * component of the distance given.
     * @param distance The distance the tank would travel if it were moved on
     * both axes.
     */
    protected void moveHorizontal(int distance)
    {
    	//calculate the rotation of the tank in radians
    	double radians = Math.toRadians(getRotation());
    	
    	/*Calculate the distance the tank should move by in the x axis.*/
    	double dx = Math.cos(radians) * distance;
    	
    	//update the real x coordinate of the tank
    	realX+=dx;
    	
    	/*A world in Greenfoot is made up of finite cells, so positions can only
    	 * be integers. So we round to the nearest integers the value of the real
    	 *  X coordinate and set the location of the tank to this number.*/
    	int tempX=(int) Math.round(realX);
    	setLocation(tempX, getY());
    	
    	/*Check if this tank has a turret placed in the world.*/
    	if(tankTurret!=null && tankTurret.getWorld()!=null)
    	{
    		//If so, also move the turret by the same distance
    		tankTurret.setLocation(tempX, getY());
    	}
    }
    
    /**
     * Accurately moves the only on the vertical axis by the horizontal 
     * component of the distance given.
     * @param distance The distance the tank would travel if it were moved on
     * both axes.
     */
    protected void moveVertical(int distance)
    {
    	//calculate the rotation of the tank in radians
    	double radians = Math.toRadians(getRotation());
    	
    	/*Calculate the distance the tank should move by in the x axis.*/
    	double dy = Math.sin(radians) * distance;
    	
    	//update the real y coordinate of the tank
    	realY += dy;
    	
    	/*A world in Greenfoot is made up of finite cells, so positions can only
    	 * be integers. So we round to the nearest integers the value of the real
    	 * Y coordinate and set the location of the tank to this number.*/
    	int tempY = (int) Math.round(realY);
    	setLocation(getX(), tempY);
    	
    	/*Check if this tank has a turret placed in the world.*/
    	if(tankTurret!=null && tankTurret.getWorld()!=null)
    	{
    		//If so, also move the turret by the same distance
    		tankTurret.setLocation(getX(), tempY);
    	}
    }
    
    /**
     * Moves this tank on each axis by the distances given. Makes sure rounding 
     * errors do not accumulate. It is used when this tank is pushed by another
     * tank. This method also ensures this tank is not pushed by another tank 
     * into a wall and ensures that if this tank cannot be pushed on one axis,
     * neither will the other tank be able to move on that axis (and thus 
     * overlap this tank).
     * @param dx The distance the tank will be moved horizontally.
     * @param dy The distance the tank will be moved vertically.
     * @param pushingTank The tank that is pushing this tank. This reference 
     * is needed so if this tank cannot be pushed on one axis, the pushing Tank
     * will also not move on that axis. If this reference is null, then this tank
     * is not being pushed so this method just moves this tank on each axis by
     * the distances given.
     */
    private void getPushed(double dx, double dy, Tank pushingTank)
    {
    	/*Check if this tank is being pushed by another.*/
    	if(pushingTank!=null)
    	{
    		/*If it is, we must make sure it is not being pushed inside a wall.*/
    		WallBlock wall=(WallBlock)getOneIntersectingObject(WallBlock.class);
        	
    		/*Check if it is intersecting a wall.*/
        	if(wall!=null)
        	{
        		/*If it, find out which side of the wall block this tank is 
        		 * touching.*/
        		String quadrant=wall.getQuadrant(getX(), getY());
        		
        		/*Check if this tank is touching the left or right side of the
        		 * wall block.*/
        		if(quadrant.equals("right") || quadrant.equals("left"))
        		{
        			/*If it is, this tank cannot be pushed on the horizontal 
        			 * axis. We push the pushing tank on the horizontal axis 
        			 * in the opposite direction to cancel out the move it did
        			 * on it's own and make sure it will not overlap this tank.*/
        			pushingTank.getPushed(-dx, 0, null);
        			
        			//set dx to 0 so this tank will not move horizontally
        			dx=0;
        		}
        		/*Else if this tank is touching the top or bottom side of the
        		 * wall block.*/
        		else if(quadrant.equals("top") || quadrant.equals("bottom"))
        		{
        			/*If it is, this tank cannot be pushed on the vertical 
        			 * axis. We push the pushing tank on the vertical axis 
        			 * in the opposite direction to cancel out the move it did
        			 * on it's own and make sure it will not overlap this tank.*/
        			pushingTank.getPushed(0, -dy, null);
        			
        			//set dy to 0 so this tank will not move horizontally
        			dy=0;
        		}
        	}
    	}
    	
    	//update the real x and y coordinates of the tank
    	realX+=dx;
    	realY+=dy;
    	
    	/*A world in Greenfoot is made up of finite cells, so positions can only
    	 * be integers. So we round to the nearest integers the value of the real
    	 * coordinates and set the location of the tank to these numbers.*/
    	int tempX=(int) Math.round(realX);
    	int tempY=(int) Math.round(realY);
    	setLocation(tempX, tempY);
    	
    	/*Check if this tank has a turret placed in the world.*/
    	if(tankTurret!=null && tankTurret.getWorld()!=null)
    	{
    		//If so, also move the turret by the same distances
    		tankTurret.setLocation(tempX, tempY);
    	}
    }
    
	/**Makes the tank lay down a land mine.*/
	protected void layMine()
	{
		//get a reference to the world the tank is in
		TankWorld world= (TankWorld) getWorld();
		
		//make a new land mine and put it in the game world
		LandMine mine=new LandMine(this);
		
		//lay the mine in the world where this tank is
		world.addObject(mine, getX(),getY());
		minesLaid++;
	}
	
	/**
	 * Method checks if the tank can move forwards.
	 * @return True if the tank can move forwards, false if not.
	 */
	public WallBlock getForwardsWallContact()
	{
		/*The tank can move forwards if it does not intersect a wall on it's front
		 * side. We check that by seeing if there is a wall intersecting the tank
		 * at it's front right corner, front left corner and in the middle point of
		 * it's front side.*/
		WallBlock frontLeft;
		WallBlock frontRight;
		WallBlock front;

		/*We get walls located at these points by calculating the distance between
		 * these points and the center of the tank using other private methods.*/
		frontRight = (WallBlock )getOneObjectAtOffset(getXOffset("front right"),
				getYOffset("front right"), WallBlock.class);
		frontLeft = (WallBlock) getOneObjectAtOffset(getXOffset("front left"), 
				getYOffset("front left"), WallBlock.class);
		front = (WallBlock) getOneObjectAtOffset(getXOffset("front"), 
				getYOffset("front"),WallBlock.class);
		

		/*Check if the tank can move forward. It can only if no wall has been
		 * detected at those three points, meaning the getOneObjectAtOffset
		 * method calls returned null.*/
		if (frontRight != null)
		{
			/*If no walls are at those points, the tank can move forward.*/
			return frontRight;
		} 
		/*If at least one actor is not null, there is a wall in the way and the
		 * method returns false.*/
		else if(frontLeft != null)
		{
			return frontLeft;
		}
		else
		{
			return front;
		}
	}

	/**
	 * Method checks if the tank can move backwards.
	 * @return True if the tank can move backwards, false if not.
	 */
	public WallBlock getBackwardsWallContact()
	{
		/*The tank can move backwards if it does not intersect a wall on it's back
		 * side. We check that by seeing if there is a wall intersecting the tank
		 * at it's back right corner, back left corner and in the middle point of
		 * it's back side.*/
		WallBlock backLeft;
		WallBlock backRight;
		WallBlock back;

		/*We get walls located at these points by calculating the distance between
		 * these points and the center of the tank using other private methods.*/
		backRight = (WallBlock) getOneObjectAtOffset(getXOffset("back right"), 
				getYOffset("back right"), WallBlock.class);
		backLeft = (WallBlock) getOneObjectAtOffset(getXOffset("back left"), 
				getYOffset("back left"), WallBlock.class);
		back = (WallBlock) getOneObjectAtOffset(getXOffset("back"),
				getYOffset("back"), WallBlock.class);
		
		/*Check if the tank can move backwards. It can only if no wall has been
		 * detected at those three points, meaning the getOneObjectAtOffset
		 * method calls returned null.*/
		if (backRight != null)
		{
			/*If no walls are at those points, the tank can move forward.*/
			return backRight;
		} 
		/*If at least one actor is not null, there is a wall in the way and the
		 * method returns false.*/
		else if(backLeft != null)
		{
			return backLeft;
		}
		else
		{
			return back;
		}
	}
	
	/**
	 * Method checks if the tank can turn right.
	 * @return True if the tank can turn right, false if not.
	 */
	public boolean canTurnRight()
	{
		/*The tank can turn right if it does not intersect a wall on it's front right
		 * or back left corners.*/
		Actor frontRight;
		Actor backLeft;
		
		/*We get walls located at these points by calculating the distance between
		 * these points and the center of the tank using other private methods.*/
		frontRight = getOneObjectAtOffset(getXOffset("front right"), getYOffset("front right"), WallBlock.class);
		backLeft = getOneObjectAtOffset(getXOffset("back left"), getYOffset("back left"), WallBlock.class);
		
		/*The tank can turn right if both of the getOneObjectAtOffset methods
		 * returned null, meaning there are no walls in the way. If at least one
		 * returned a wall block, it can not turn right.*/
		return ((frontRight==null) && (backLeft==null));
	}
	
	/**
	 * Method checks if the tank can turn left.
	 * @return True if the tank can turn left, false if not.
	 */
	public boolean canTurnLeft()
	{
		/*The tank can turn left if it does not intersect a wall on it's front left
		 * or back right corners.*/
		Actor forwardLeft;
		Actor backwardRight;
		
		/*We get walls located at these points by calculating the distance between
		 * these points and the center of the tank using other private methods.*/
		forwardLeft = getOneObjectAtOffset(getXOffset("front left"), getYOffset("front left"), WallBlock.class);
		backwardRight = getOneObjectAtOffset(getXOffset("back right"), getYOffset("back right"), WallBlock.class);
		
		/*The tank can turn left if both of the getOneObjectAtOffset methods
		 * returned null, meaning there are no walls in the way. If at least one
		 * returned a wall block, it can not turn left.*/
		return ((forwardLeft==null) && (backwardRight==null));
	}

	/***
	 * Checks if the tank is moving.
	 * @return True if the tank is moving, false if not. Because most enemy tanks
	 * move continuously, this method always returns true unless overridden.
	 */
	public abstract boolean isMoving();
	
	/**
	 * Checks if the tank is moving forward.
	 * @return True if the tank is moving forward, false if not. Because most 
	 * enemy tanks move continuously, this method always returns true unless 
	 * overridden.
	 */
	public abstract boolean isMovingForward();
	
	/**
	 * Checks if the tank is moving backward.
	 * @return True if the tank is moving backward, false if not. Because most 
	 * enemy tanks move forward continuously, this method always returns false
	 * unless overridden.
	 */
	public abstract boolean isMovingBackward();
	
	/**
	 * Method returns the approximate distance (or offset) between the x position
	 * of the centre of the tank and the x position of one of the six points on 
	 * the edges of the tank (front left corner, front right corner, middle of 
	 * front side, back left corner, back right corner and middle of back side.)
	 * @param point A string that tells the method which point we want the offset
	 * for.
	 * @return The distance between the x position of the tank and the x position 
	 * of the point given in the string argument.
	 */
	public int getXOffset(String point)
	{
		//the offset that will be calculated and returned.
		int xOffset;
		
		/*The degree between a line from the centre of the tank to the requested 
		 * point to a horizontal line passing through the centre of the tank.*/
		double degree;
		
		/*Depending on the string argument given, we calculate the offset.*/
		if(point.equals("front right"))
		{
			/*the degree is the rotation of the tank added with the angle between
			 * the length of the tank with it's diagonal.*/
			degree = ANGLE+getRotation();
			
			/*The offset is the length the projection of half of the diagonal
			 * of the tank on the horizontal axis, so we multiply it with the
			 * cosine of the degree and round to the nearest higher integer.*/
			xOffset = (int) Math.round(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
		}
		else if(point.equals("front"))
		{
			/*the degree is the rotation of the tank*/
			degree=getRotation();
			
			/*The offset is the length of the projection of half of the length
			 * of the tank on the horizontal axis, so we multiply it with the
			 * cosine of the degree and round to the nearest higher integer.*/
			xOffset=(int) Math.round((LENGTH/2.0)*Math.cos(Math.toRadians((int)degree)));
		}
		else if(point.equals("front left"))
		{
			/*the degree is the rotation of the tank added minus the angle between
			 * the length of the tank and it's diagonal, since the front 
			 * left corner of the tank comes before the middle of the front
			 * side in a clockwise rotation.*/
			degree=getRotation()-ANGLE;
			
			/*The offset is the length the projection of half of the diagonal
			 * of the tank on the horizontal axis, so we multiply it with the
			 * cosine of the degree and round to the nearest higher integer.*/
			xOffset = (int) Math.round(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
		}
		else if(point.equals("back left"))
		{
			/*The back left corner of the tank is diametrically opposite from
			 * the front right corner compared to the tank's centre, so we 
			 * make the same calculations as for the front right corner
			 * and change the offset to it's opposite.*/
			degree = ANGLE+getRotation();
			xOffset = (int) Math.round(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
			xOffset= -xOffset;
		}
		else if(point.equals("back"))
		{
			/*The middle of the back side of the tank is diametrically 
			 * opposite from the middle of the front side compared to the 
			 * tank's centre, so we make the same calculations as for the
			 * front point and change the offset to it's opposite.*/
			degree=getRotation();
			xOffset= (int) Math.round((LENGTH/2.0)*Math.cos(Math.toRadians((int)degree)));
			xOffset= -xOffset;
		}
		else if(point.equals("back right"))
		{
			/*The back right corner of the tank is diametrically opposite from
			 * the front left corner compared to the tank's centre, so we 
			 * make the same calculations as for the front left corner
			 * and change the offset to it's opposite.*/
			degree=getRotation()-ANGLE;
			xOffset = (int) Math.round(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
			xOffset= -xOffset;
		}
		else
		{
			/*Should never be reached since it is a private method, but it is
			 * put to highlight bugs if a mistake is made*/
			xOffset=0;
		}
		
		return xOffset;
	}
	
	/**
	 * Method returns the approximate distance (or offset) between the y position
	 * of the centre of the tank and the y position of one of the six points on the
	 * edges of the tank (front left corner, front right corner, middle of front
	 * side, back left corner, back right corner and middle of back side.)
	 * @param point A string that tells the method which point we want the offset
	 * for.
	 * @return The distance between the y position of the tank and the y position 
	 * of the point given in the string argument.
	 */
	public int getYOffset(String point)
	{
		//the offset that will be calculated and returned.
		int yOffset;
		
		/*The degree between a line from the centre of the tank to the requested 
		 * point to a horizontal line passing through the centre of the tank.*/
		double degree;
		
		/*Depending on the string argument given, we calculate the offset.*/
		if(point.equals("front right"))
		{
			/*the degree is the rotation of the tank added with the angle between
			 * the length of the tank with it's diagonal.*/
			degree = ANGLE+getRotation();
			
			/*The offset is the length the projection of half of the diagonal
			 * of the tank on the vertical axis, so we multiply it with the
			 * sine of the degree and round to the nearest higher integer.*/
			yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
		}
		else if(point.equals("front"))
		{
			/*the degree id the rotation of the tank.*/
			degree=getRotation();
			
			/*The offset is the length of the projection of half of the length
			 * of the tank on the vertical axis, so we multiply it with the
			 * sine of the degree and round to the nearest higher integer.*/
			yOffset=(int) Math.ceil((LENGTH/2.0)*Math.sin(Math.toRadians((int)degree)));
		}
		else if(point.equals("front left"))
		{
			/*the degree is the rotation of the tank added minus the angle between
			 * the length of the tank and it's diagonal, since the front 
			 * left corner of the tank comes before the middle of the front
			 * side in a clockwise rotation.*/
			degree=getRotation()-ANGLE;
			
			/*The offset is the length the projection of half of the diagonal
			 * of the tank on the vertical axis, so we multiply it with the
			 * sine of the degree and round to the nearest higher integer.*/
			yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
		}
		else if(point.equals("back left"))
		{
			/*The back left corner of the tank is diametrically opposite from
			 * the front right corner compared to the tank's centre, so we 
			 * make the same calculations as for the front right corner
			 * and change the offset to it's opposite.*/
			degree = ANGLE+getRotation();
			yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
			yOffset= -yOffset;
		}
		else if(point.equals("back"))
		{
			/*The middle of the back side of the tank is diametrically 
			 * opposite from the middle of the front side compared to the 
			 * tank's centre, so we make the same calculations as for the
			 * front point and change the offset to it's opposite.*/
			degree=getRotation(); 
			yOffset= (int) Math.ceil((LENGTH/2.0)*Math.sin(Math.toRadians((int)degree)));
			yOffset= -yOffset;
		}
		else if(point.equals("back right"))
		{
			/*The back right corner of the tank is diametrically opposite from
			 * the front left corner compared to the tank's centre, so we 
			 * make the same calculations as for the front left corner
			 * and change the offset to it's opposite.*/
			degree=getRotation()-ANGLE;
			yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
			yOffset= -yOffset;
		}
		else
		{
			/*Should never be reached since it is a private method, but it is
			* put to highlight bugs if a mistake is made.*/
			yOffset=0;
		}
		
		return yOffset;
	}
	
	/**
	 * Locates what corner of this tank is touched by the other tank given as a 
	 * parameter.
	 * @param otherTank Any other tank actor.
	 * @return A string representing the corner that is touched by the given tank,
	 * or null if that tank does not touch a corner of this tank.
	 */
	public String getContactCorner(Tank otherTank)
	{
		//Check if the parameter tank is the same as this tank.
		if(otherTank==this)
		{
			/*If it is, return null since we are looking if a different tank 
			 * touches one of the corners of this tank, so it should be a 
			 * different tank.*/
			return null;
		}
		
		//the string representing the result
		String corner=null;
		
		/*We use the getXOffset and getYOffset methods to calculate the distance 
		 * between the tank's corners and it's centre, check if the other tank
		 * intersects those coordinates and set the corner variable accordingly.*/
		
		/*Check if the other tank intersects the front left corner of this tank.*/
		if((Tank)getOneObjectAtOffset(getXOffset("front left"),getYOffset("front"
				+ " left"),Tank.class)==otherTank) 
		{
			corner="front left";
		}
		/*Check if the other tank intersects the front right corner of this tank.*/
		else if((Tank)getOneObjectAtOffset(getXOffset("front right"),getYOffset
				("front right"),Tank.class)==otherTank)
		{
			corner="front right";
		}
		/*Check if the other tank intersects the back left corner of this tank.*/
		else if((Tank)getOneObjectAtOffset(getXOffset("back left"),getYOffset
				("back left"),Tank.class)==otherTank)
		{
			corner="back left";
		}
		/*Check if the other tank intersects the back right corner of this tank.*/
		else if((Tank)getOneObjectAtOffset(getXOffset("back right"),getYOffset
				("back right"),Tank.class)==otherTank)
		{
			corner="back right";
		}
		
		return corner;
	}
	
	/**Make the tank play a sound if it is moving.*/
	protected void playSound()
	{
		/*Check if the tank is moving.*/
		if (isMoving())
		{
			/*If it is, check if the sound is already playing.*/
			if (!drivingSound.isPlaying())
			{
				/*If it was not playing already, make it play.*/
				drivingSound.play();
			}
		} 
		/*If the tank is not moving, the driving sound should not play, so the
		 * sound is stopped.*/
		else
		{
			drivingSound.stop();
		}
	}
	
	/**
	 * Checks if this tank should push other tanks while it moves and calculates the 
	 * x and y distances by which the other tanks should be moved.
	 */
	protected void pushOtherTanks()
	{
		/*Check if this tank is moving, if the tank does not move it cannot 
		 * push other tanks.*/
		if(isMoving())
		{
			/*Get an instance of another tank which intersects this tank.*/
			Tank otherTank= (Tank) getOneIntersectingObject(Tank.class);

			/*This tank needs to push other tanks if it intersects another tank.*/
			if(otherTank!=null)
			{
				/*If it needs to push another tank, we determine how the other tank
				 * needs to be moved, based on how Wii Tanks works. There are 2 basic
				 * cases : when this tank touches one of the other tank's sides 
				 * with one of it's corners, in which case the other tank must be 
				 * moved both vertically and horizontally, and when the other tank
				 * touches one of this tank's sides with one of it's corners, or 
				 * when this tank touches with one of it's sides one of the sides 
				 * of the other tank, in which case the other tank must be moved 
				 * only along one axis. We will decide the case using the getContactCorner
				 * method and the string variable corner.*/
				
				//Represents the corner that one of the tanks will touch the other with.
				String corner=null;
				
				/*Represents the diagonal quadrant (or side in this case) of one the two 
				 * tanks where the other's contact corner is located.*/
				String side=null;
				
				//The distance on the x axis the other tank should be moved
				double dx=0;
				
				//The distance on the y axis the other tank should be moved
				double dy=0;
				
				//See if this tank touches the other tank with one of it's corners
				corner=getContactCorner(otherTank);
				if(corner!=null)
				{	
					/*If it does, find out what diagonal quadrant (or side) of the other
					 * tank is touched by this tank's corner touches.*/
					side=otherTank.getQuadrant(getX()+getXOffset(corner),
							getY()+getYOffset(corner));
					
					/*Calculate the distances the other tank should be moved by based
					 * on what side of it is touched by this tank.*/
					if(side.equals("front") || side.equals("back"))
					{
						dx = computeHorizontalPush(corner);
					}
					/*Else, if the other tank is touched on it's right or left sides,
					 * it should be moved vertically.*/ 
					else
					{
						dy = computeVerticalPush(corner);
					}
				}
				/*Else, we need to decide if the other tank touches this tank with
				 * one of it's corners or if this tank with one of it's sides touches
				 * one of the other tank's sides.*/
				else
				{
					//Get the corner of the other tank that touches this tank.
					corner=otherTank.getContactCorner(this);
					
					/*Check if the other tank touches a side of this tank with 
					 * one of it's corners.*/
					if(corner!=null)
					{
						/*If it does, get the side of this tank that is touched by
						 * the other tanks' corner.*/
						side=getQuadrant(otherTank.getX()+otherTank.getXOffset(corner),
								otherTank.getY()+otherTank.getYOffset(corner));	
					}
					//If not, see what side of this tank pushes a side of the other tank
					else
					{
						/*Check if this tank pushes a side of the other tank with 
						 * it's front side.*/
						if((Tank)getOneObjectAtOffset(getXOffset("front"),
								getYOffset("front"),Tank.class)==otherTank)
						{
							side="front";
						}
						/*Else, this tank must push a side of the other tank with 
						 * it's back, since that is the only possible case left.*/
						else
						{
							side="back";
						}
					}
					
					/*Check if this tank pushes the other tank with it's front side. 
					 * Which is only if the front side of this tank touches the other 
					 * tank and if this tank is moving forward.*/
					if(isMovingForward() && side.equals("front"))
					{
						/*If so, the distances the other tank should be moved by 
						 * are the x and y axis components of this tank's movement 
						 * vector.*/
						dx=getSpeed()*Math.cos(Math.toRadians(getRotation()));
						
						dy=getSpeed()*Math.sin(Math.toRadians(getRotation()));
					}
					/*Else, check if this tank pushes the other tank with it's back
					 * side. Which is only if the back side of this tank touches
					 * the other tank and if this tank is moving backward.*/
					else if(isMovingBackward() && side.equals("back"))
					{
						/*If so, the distances the other tank should be moved by 
						 * are the x and y axis components of this tank's movement 
						 * vector. The opposite values to the above conditional 
						 * block since the tank moves backward.*/
						dx=-(getSpeed()*Math.cos(Math.toRadians(getRotation())));
						
						dy=-(getSpeed()*Math.sin(Math.toRadians(getRotation())));
					}	
				}
				
				//move the other tank by the distances established
				otherTank.getPushed(dx, dy, this);
			}
		}	
	}
	
	/**
	 * Calculate the distance on the horizontal axis the tank being pushed by 
	 * this tank should move, in the case when this tank touches the front or 
	 * back side of the other tank with one of it's corners.
	 * @param corner The corner of this touching the other tank.
	 * @return The distance the other should be moved horizontally.
	 */
	private double computeHorizontalPush(String corner)
	{
		//The distance on the x axis the other tank should be moved
		double dx = 0;
		
		/*If the other tank is touched on it's front or back sides, it
		 * should be moved horizontally. Check if this tank pushes the
		 * other tank with it's front, by seeing if it touches the other
		 * tank with one of it's front corners and if it also moves forward.*/
		if(isMovingForward() && (corner.equals("front left") || 
			corner.equals("front right")))
		{
			/*If so, the distance the other tank should be moved by 
			 * is the x axis component of this tank's movement vector.*/
			dx=getSpeed()*Math.cos(Math.toRadians(getRotation()));
		}
		/*Check if this tank pushes the other tank with it's back, by 
		 * seeing if it touches the other tank with one of it's back
		 * corners and if it also moves backward.*/
		else if(isMovingBackward() && (corner.equals("back left") 
			|| corner.equals("back right")))
		{
			/*If so, the distance the other tank should be moved by 
			 * is the x axis component of this tank's movement vector.
			 * The opposite value to the above conditional block since
			 * the tank moves backwards.*/
			dx=-(getSpeed()*Math.cos(Math.toRadians(getRotation())));
		}
		
		return dx;
	}
	
	/**
	 * Calculate the distance on the vertical axis the tank being pushed by 
	 * this tank should move, in the case when this tank touches the left or 
	 * right side of the other tank with one of it's corners.
	 * @param corner The corner of this touching the other tank.
	 * @return The distance the other should be moved vertically.
	 */
	private double computeVerticalPush(String corner)
	{
		//The distance on the y axis the other tank should be moved
		double dy = 0;
		
		//Check if this tank pushes the other tank with it's front.
		if(isMovingForward() && (corner.equals("front left") || 
				corner.equals("front right")))
		{
			/*If so, the distance the other tank should be moved by 
			 * is the y axis component of this tank's movement vector.*/
			dy=getSpeed()*Math.sin(Math.toRadians(getRotation()));
		}
		//Check if this tank pushes the other tank with it's back.
		else if(isMovingBackward() && (corner.equals("back left") 
			|| corner.equals("back right")))
		{
			/*If so, the distance the other tank should be moved by 
			 * is the y axis component of this tank's movement vector.
			 * The opposite value to the above conditional block since
			 * the tank moves backwards.*/
			dy=-(getSpeed()*Math.sin(Math.toRadians(getRotation())));
		}
		
		return dy;
	}
	
	/**
	 * Calculates what diagonal quadrant of this tank a point with the
	 * given coordinates is in. It also applies to points outside the tank
	 * (the quadrants start from the center of this tank and their 
	 * imaginary edges extend up to the world's boundary.) Takes into account 
	 * the rotation of this tank.
	 * @param x The x coordinate of the point.
	 * @param y The y coordinate of the point.
	 * @return A string representing the diagonal quadrant the point is in :
	 * "top", "bottom", "left or"right".
	 */
    public String getQuadrant(int x, int y)
    {
    	/*From here on, diag1 or diagonal 1 refers to the diagonal between the 
    	 * back left corner of the tank and the front right corner. Diag2 or 
    	 * diagonal 2 refers to the diagonal connecting the back right corner
    	 * of the tank and the front left corner.*/
    	
    	//the angle between diagonal 1 and the horizontal axis
    	double diag1Angle=normalizeAngle(ANGLE+getRotation());
    	
    	//the slope of diagonal 1, which is the tangent of diag1Angle
    	double diag1Slope=Math.tan(Math.toRadians(diag1Angle));
    	
    	//the angle between diagonal 2 and the horizontal axis
    	double diag2Angle=normalizeAngle(getRotation()-ANGLE);
    	
    	//the slope of diagonal 2, which is the tangent of diag2Angle
    	double diag2Slope=Math.tan(Math.toRadians(diag2Angle));
    	
    	/*We have to take into account the position of this tank and it's rotation,
    	 * so imagine we translate the tank so that it's back left corner is in
    	 * the top left corner of the world. This is the reference so in the 
    	 * analytical geometry equations we will subtract to coordinates of the
    	 * back left corner of this tank.*/
    	
    	/*Calculate the x and y coordinates of the back left corner of this block.*/
    	double backLeftCornerX=getX()+getXOffset("back left");
    	double backLeftCornerY=getY()+getYOffset("back left");
    	
    	/*A matrix that holds the names of the 4 quadrants.*/
    	String[][] quadrants= { {"left","front"}, {"back","right"} };
    	
    	/*An array that will be set to one of the rows of the matrix, based  on
    	 * if the given point is above or below the diagonal that points to the
    	 * lower left.*/
    	String[] temp;
    	
    	/*The result of this computation. It will be set to one of the values in
    	 * the temp array, based on if the given point is above or below the 
    	 * diagonal that points to the upper right.*/
    	String quadrant;
    	
    	/*We narrow down the possible results by seeing if the given point is 
    	 * above or below the diagonal that points to the lower left. We check
    	 * this using analytical geometry and the formula of y-y' =m*(x-x') .
    	 * Where m is the slope of the diagonal.*/
    	
    	//the left member of the equation
    	double leftMember=y-backLeftCornerY;
    	
    	//the right member of this equation
    	double rightMember=diag1Slope*(x-backLeftCornerX);
    	
    	/*Depending on the rotation of the tank, the left and front quadrants
    	 * might be above diagonal 1, or under it, so we take it into account.*/
    	if((leftMember<=rightMember && (diag1Angle<=90 || diag1Angle>=270))
    			|| (leftMember>rightMember && (diag1Angle>90 && diag1Angle<270)))
    	{
    		 /*We check if the point is above diagonal 1 and diag1Angle is not
    		  * between 90 and 270, or if the point is under diagonal 1 and between
    		  * 90 and 270. If so, it is in either the left or front quadrants.*/
    		temp=quadrants[0];
    	}
    	/*Else, it is in the back or right quadrants of the tank.*/
    	else
    	{
    		temp=quadrants[1];
    	}
    	
    	/*After we translate the tank so that the back left corner is in the top 
    	 * left corner of the world, the second diagonal starts from a different
    	 * point on the y axis than diagonal 1. We have to take this into account,
    	 * and we will have to subtract this distance between the top left corner
    	 * of the world and the place where the second diagonal intersects the y axis.
    	 * This distance is diag2YStart. We find this length using sine theory.
    	 * We can see a triangle between the y axis and the 2 diagonals is formed.
    	 * We know the angle between diagonals and the angle between diag2 and the
    	 * y axis. diag2YStart is the side opposite the angle between diagonals, 
    	 * and half of diag1 is the side opposite the angle between diag2 and the
    	 * y axis.*/
    	double diag2YStart=(HALF_DIAGONAL*Math.sin(Math.toRadians(2*ANGLE)))/
    			(Math.sin(Math.PI/2+Math.toRadians(getRotation()-ANGLE)));
    	
    	//the left member of the equation
    	leftMember=y-backLeftCornerY-diag2YStart;
    	
    	//the right member of this equation
    	rightMember=diag2Slope*(x-backLeftCornerX);
    	
    	/*We find the quadrant by seeing if the given point is above or below 
    	 * the diagonal 2. We check this using analytical geometry and the 
    	 * formula of y-y' =m*(x-x'). Where m is the slope of diagonal 2.
    	 * Depending on the rotation of the tank, the left and back quadrants
    	 * might be above diagonal 2, or under it, so we take it into account.*/
    	if((leftMember<=rightMember && (diag2Angle<=90 || diag2Angle>=270))
    			|| (leftMember>rightMember && (diag2Angle>90 && diag2Angle<270)))
    	{
    		/*We check if the point is above diagonal 2 and diag2Angle is not
    		  * between 90 and 270, or if the point is under diagonal 2 and between
    		  * 90 and 270. If so, it is in either the left or back quadrants.*/
    		quadrant=temp[0];
    	}
    	/*Else, it is in the front or right quadrants of the tank.*/
    	else
    	{
    		quadrant=temp[1];
    	}
    	
    	return quadrant;
    }
    
	/**
	 * Getter for the turret of this tank.
	 * @return A reference to this tank's turret.
	 */
	public Turret getTurret()
	{
		return tankTurret;
	}
	
	/**
	 * The speed of this tank, meaning the distance in cells that the tank moves
	 * each time the move(int) method is called.
	 * @return The speed of this tank.
	 */
	public abstract int getSpeed();
	
	/**
	 * The number of mines this tank can lay in one level.
	 * @return The number of mines this tank can lay.
	 */
	public abstract int getNumberOfMines();
	
	/**
	 * Getter The maximum number of degrees by which this tank can turn each 
	 * time the act() method is called. Used by mobile enemy tanks and the 
	 * player tank.
	 * @return 	The maximum turn speed of this type of tank.
	 */
    public abstract int getMaxTurnSpeed();
	
	/**Deletes this tank and it's turret along with any other associated actors
	 * from this game world.*/
	public void deleteTank()
	{
		World world= getWorld();
		
		//call the appropiate deleteTurret method for this turret.
		tankTurret.deleteTurret();
		
		/*Check if the world is not null, since this method may be called after
    	 * the tank is not in the world after the level has ended because all
    	 * remaining enemies were destroyed by the land mine explosion.*/
		if(world!=null)
		{
			world.removeObject(this);
		}
	}
	
	/**Method reloads this tank into the game world to prepare it for another start
	 * of the current level, meaning it resets the position and orientation of this
	 * tank and it's turret, and all other instance variables. */
	public void reloadTank()
	{
		TankWorld world = (TankWorld) getWorld();
		
		/*Check if the tank is in a game world to avoid exceptions.*/
		if(world!=null)
		{
			/*Reset the real number values of the tank's position and the counter
			 * of mines laid by this tank.*/
			realX=startX;
			realY=startY;
			minesLaid=0;
			
			/*Place the tank and it's turret at it's original position and reset
			 * their orientation.*/
			setLocation(startX,startY);
			tankTurret.setLocation(startX, startY);
			setRotation(startRotation);
			tankTurret.setRotation(startRotation);
		}
	}
	
	/**
	 * Normalises the value of an angle in degrees. Meaning it brings the value 
	 * of angle into an equivalent value between 0 and 359.
	 * @param angle The angle that will be normalised.
	 * @return The normalised value of the angle.
	 */
	protected static double normalizeAngle(double angle)
	{	
		//Use mod division to bring it to a value between -359 and 359.
		double normalizedAngle=angle%360;;
		
		//Check if the value is negative.
		if(normalizedAngle<0)
		{
			//If it is, make it an equivalent positive value.
			normalizedAngle+=360;
		}
		
		return normalizedAngle;
	}
	
	/**
	 * Gets the distance between this tank and another given tank.
	 * @param tank The tank whose distance from this tank we calculate.
	 * @return The distance between this tank and the given tank as a double.
	 */
	public double getDistanceFrom(Tank tank)
    {
		//get the distances between the 2 tanks on the the 2 axes of the world
    	int xDistance=tank.getX()-getX();
    	int yDistance=tank.getY()-getY();
    	
    	/*Calculate the distance using Pythagora's theorem.*/
    	double distance=Math.sqrt((xDistance*xDistance)+(yDistance*yDistance));
    	return distance;
    }
}

