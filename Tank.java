import greenfoot.*;

/**
 * <p><b>File name: </b> Tank.java
 * @version 1.6
 * @since 14.05.2018
 * <p><p><b>Last modification date: </b> 09.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a general tank for a Greenfoot recreation of the Wii Tanks 
 * game for the Nintendo Wii. By default, it moves, plays a sound when moving
 * and tells it's turret to aim and fire. It is meant to be inherited always and
 * you should not have an actor that is just a Tank object since it the class was
 * not meant to be used in this way.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created a simple tank that does not move.
 * <p>	-1.1 - Made the tank move using keyboard input.
 * <p>	-1.2 - Made the tank play a sound when moving.
 * <p>	-1.3 - Made the tank generate a simple tank turret on top if it when it
 * is added to the world.
 * <p> -1.4 - Made the tank detect collisions with walls and not move into wall
 * blocks.
 * <p> -1.5 - Modified this class to be a general tank class and moved part of 
 * code into the PlayerTank class, which inherits from this.
 */

public class Tank extends Actor
{
	/**The tank turret of this tank. By default it is a simple turret.*/
	protected Turret tankTurret;
	
	/**The file name of the sound that will play when the tank moves.*/
	protected static final String DRIVING_SOUND_NAME="tank_moving_1.wav";
	
	/**The GreenfootSound object associated with this tank. Used to make sure
	 * the sound only plays when this tank moves.*/
	protected GreenfootSound drivingSound;
	
	/**The length of half of the diagonal of a tanks image, in pixels. Used for
	 * detecting collisions with walls and other tanks. For the current image
	 * the value is {@value}.*/
	protected final static double HALF_DIAGONAL=35.362409;
	
	/**The length of half of the long side of the tank. Currently, the tank's 
	 * image is 51 by 49 pixels, so the value is {@value}.*/
	protected static final double HALF_LENGTH=25.5;
	
	/**The angle in degrees between the length of the tank and it's diagonal.
	 * For the current image the value is {@value}.*/
	protected final static double ANGLE=43.85423781591219;
	
	/**The starting x position of the tank in the world. Needed so we know where
	 * to put the tank when the level is reloaded.*/
	protected final int startX;
	
	/**The starting y position of the tank in the world. Needed so we know where
	 * to put the tank when the level is reloaded.*/
	protected final int startY;
	
	/**
	 * Makes a new tank object.
	 * @param startX The starting x position of the tank in the world.
	 * @param startY The starting y position of the tank in the world.
	 */
	public Tank(int startX, int startY)
	{
		//make new tank
		super();
		
		/*Initialise variables that store the starting position of the tank and
		 * the sound object of this tank.*/
		this.startX=startX;
		this.startY=startY;
		drivingSound = new GreenfootSound(DRIVING_SOUND_NAME);
	}
	
	/**Method sets the correct rotation of the tank after it is placed in the 
	 * world and then makes a new tank turret that will be placed on this tank.*/
	@Override
	protected void addedToWorld(World world)
	{
		setRotation(180);
		this.tankTurret = new Turret(this);
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
		playSound();
		
		tankTurret.aim();
		tankTurret.fire();
	}

	/**
	 * Method checks if the tank can move forwards.
	 * @return True if the tank can move forwards, false if not.
	 */
	public boolean canMoveForwards()
	{
		/*The tank can move forwards if it does not intersect a wall on it's front
		 * side. We check that by seeing if there is a wall intersecting the tank
		 * at it's front right corner, front left corner and in the middle point of
		 * it's front side.*/
		Actor frontLeft;
		Actor frontRight;
		Actor front;

		/*We get walls located at these points by calculating the distance between
		 * these points and the center of the tank using other private methods.*/
		frontRight = getOneObjectAtOffset(getXOffset("front right"), getYOffset("front right"), WallBlock.class);
		frontLeft = getOneObjectAtOffset(getXOffset("front left"), getYOffset("front left"), WallBlock.class);
		front= getOneObjectAtOffset(getXOffset("front"),getYOffset("front"),WallBlock.class);
		

		/*Check if the tank can move forward. It can only if no wall has been
		 * detected at those three points, meaning the getOneObjectAtOffset
		 * method calls returned null.*/
		if ((frontRight == null) && (frontLeft == null) && (front==null))
		{
			/*If no walls are at those points, the tank can move forward.*/
			return true;
		} 
		/*If at least one actor is not null, there is a wall in the way and the
		 * method returns false.*/
		else
		{
			return false;
		}
	}

	/**
	 * Method checks if the tank can move backwards.
	 * @return True if the tank can move backwards, false if not.
	 */
	public boolean canMoveBackwards()
	{
		/*The tank can move backwards if it does not intersect a wall on it's back
		 * side. We check that by seeing if there is a wall intersecting the tank
		 * at it's back right corner, back left corner and in the middle point of
		 * it's back side.*/
		Actor backLeft;
		Actor backRight;
		Actor back;

		/*We get walls located at these points by calculating the distance between
		 * these points and the center of the tank using other private methods.*/
		backRight = getOneObjectAtOffset(getXOffset("back right"), getYOffset("back right"), WallBlock.class);
		backLeft = getOneObjectAtOffset(getXOffset("back left"), getYOffset("back left"), WallBlock.class);
		back= getOneObjectAtOffset(getXOffset("back"), getYOffset("back"), WallBlock.class);
		
		/*Check if the tank can move backwards. It can only if no wall has been
		 * detected at those three points, meaning the getOneObjectAtOffset
		 * method calls returned null.*/
		if ((backRight == null) && (backLeft == null) && (back==null))
		{
			/*If no walls are at those points, the tank can move forward.*/
			return true;
		} 
		/*If at least one actor is not null, there is a wall in the way and the
		 * method returns false.*/
		else
		{
			return false;
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
	 * move continuously, this method always returns true unless overriden.
	 */
	public boolean isMoving()
	{
		return true;
	}

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
	private int getXOffset(String point)
	{
		//the offset that will be calculated and returned.
		int xOffset;
		
		/*The degree between a line from the centre of the tank to the requested 
		 * point to a horizontal line passing through the centre of the tank.*/
		double degree;
		
		/*Depending on the string argument given, we calculate the offset.*/
		switch (point)
		{
			case "front right":
				/*the degree is the rotation of the tank added with the angle between
				 * the length of the tank with it's diagonal.*/
				degree = ANGLE+getRotation();
				
				/*The offset is the length the projection of half of the diagonal
				 * of the tank on the horizontal axis, so we multiply it with the
				 * cosine of the degree and round to the nearest higher integer.*/
				xOffset = (int) Math.ceil(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
				break;
				
			case "front":
				/*the degree is the rotation of the tank*/
				degree=getRotation();
				
				/*The offset is the length of the projection of half of the length
				 * of the tank on the horizontal axis, so we multiply it with the
				 * cosine of the degree and round to the nearest higher integer.*/
				xOffset=(int) Math.ceil(HALF_LENGTH*Math.cos(Math.toRadians((int)degree)));
				break;
				
			case "front left":
				/*the degree is the rotation of the tank added minus the angle between
				 * the length of the tank and it's diagonal, since the front 
				 * left corner of the tank comes before the middle of the front
				 * side in a clockwise rotation.*/
				degree=getRotation()-ANGLE;
				
				/*The offset is the length the projection of half of the diagonal
				 * of the tank on the horizontal axis, so we multiply it with the
				 * cosine of the degree and round to the nearest higher integer.*/
				xOffset = (int) Math.ceil(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
				break;

			case "back left":
				/*The back left corner of the tank is diametrically opposite from
				 * the front right corner compared to the tank's centre, so we 
				 * make the same calculations as for the front right corner
				 * and change the offset to it's opposite.*/
				degree = ANGLE+getRotation();
				xOffset = (int) Math.ceil(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
				xOffset= -xOffset;
				break;
				
			case "back":
				/*The middle of the back side of the tank is diametrically 
				 * opposite from the middle of the front side compared to the 
				 * tank's centre, so we make the same calculations as for the
				 * front point and change the offset to it's opposite.*/
				degree=getRotation();
				xOffset= (int) Math.ceil(HALF_LENGTH*Math.cos(Math.toRadians((int)degree)));
				xOffset= -xOffset;
				break;
				
			case "back right":
				/*The back right corner of the tank is diametrically opposite from
				 * the front left corner compared to the tank's centre, so we 
				 * make the same calculations as for the front left corner
				 * and change the offset to it's opposite.*/
				degree=getRotation()-ANGLE;
				xOffset = (int) Math.ceil(HALF_DIAGONAL * Math.cos(Math.toRadians(degree)));
				xOffset= -xOffset;
				break;
				
			/*Should never be reached since it is a private method, but it is
			 * put to highlight bugs if a mistake is made*/
			default:
				xOffset=0;
				break;
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
	private int getYOffset(String point)
	{
		//the offset that will be calculated and returned.
		int yOffset;
		
		/*The degree between a line from the centre of the tank to the requested 
		 * point to a horizontal line passing through the centre of the tank.*/
		double degree;
		
		/*Depending on the string argument given, we calculate the offset.*/
		switch (point)
		{
			case "front right":
				/*the degree is the rotation of the tank added with the angle between
				 * the length of the tank with it's diagonal.*/
				degree = ANGLE+getRotation();
				
				/*The offset is the length the projection of half of the diagonal
				 * of the tank on the vertical axis, so we multiply it with the
				 * sine of the degree and round to the nearest higher integer.*/
				yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
				break;
				
			case "front":
				/*the degree id the rotation of the tank.*/
				degree=getRotation();
				
				/*The offset is the length of the projection of half of the length
				 * of the tank on the vertical axis, so we multiply it with the
				 * sine of the degree and round to the nearest higher integer.*/
				yOffset=(int) Math.ceil(HALF_LENGTH*Math.sin(Math.toRadians((int)degree)));
				break;
				
			case "front left":
				/*the degree is the rotation of the tank added minus the angle between
				 * the length of the tank and it's diagonal, since the front 
				 * left corner of the tank comes before the middle of the front
				 * side in a clockwise rotation.*/
				degree=getRotation()-ANGLE;
				
				/*The offset is the length the projection of half of the diagonal
				 * of the tank on the vertical axis, so we multiply it with the
				 * sine of the degree and round to the nearest higher integer.*/
				yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
				break;

			case "back left":
				/*The back left corner of the tank is diametrically opposite from
				 * the front right corner compared to the tank's centre, so we 
				 * make the same calculations as for the front right corner
				 * and change the offset to it's opposite.*/
				degree = ANGLE+getRotation();
				yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
				yOffset= -yOffset;
				break;
				
			case "back":
				/*The middle of the back side of the tank is diametrically 
				 * opposite from the middle of the front side compared to the 
				 * tank's centre, so we make the same calculations as for the
				 * front point and change the offset to it's opposite.*/
				degree=getRotation(); 
				yOffset= (int) Math.ceil(HALF_LENGTH*Math.sin(Math.toRadians((int)degree)));
				yOffset= -yOffset;
				break;
				
			case "back right":
				/*The back right corner of the tank is diametrically opposite from
				 * the front left corner compared to the tank's centre, so we 
				 * make the same calculations as for the front left corner
				 * and change the offset to it's opposite.*/
				degree=getRotation()-ANGLE;
				yOffset = (int) Math.ceil(HALF_DIAGONAL * Math.sin(Math.toRadians(degree)));
				yOffset= -yOffset;
				break;
				
			/*Should never be reached since it is a private method, but it is
			* put to highlight bugs if a mistake is made.*/
			default:
				yOffset=0;
				break;
		}
		
		return yOffset;
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
	 * Getter for the turret of this tank.
	 * @return A reference to this tank's turret.
	 */
	public Turret getTurret()
	{
		return tankTurret;
	}
	
	/**Deletes this tank and it's turret along with any other associated actors
	 * from this game world.*/
	public void deleteTank()
	{
		World world= getWorld();
		
		//call the appropiate deleteTurret method for this turret.
		tankTurret.deleteTurret();
		world.removeObject(this);
	}
	
	/**Method reloads this tank into the game world to prepare it for another start
	 * of the current level. */
	public void reloadTank()
	{
		TankWorld world=getWorldOfType(TankWorld.class);
		
		/*Check if the tank is in a game world to avoid exceptions.*/
		if(world!=null)
		{
			/*Place the tank and it's turret at it's original position and reset
			 * their orientation.*/
			setLocation(startX,startY);
			tankTurret.setLocation(startX, startY);
			setRotation(180);
			tankTurret.setRotation(180);
		}
	}
}
