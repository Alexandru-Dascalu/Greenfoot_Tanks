import greenfoot.World;

/**
 * <p><b>File name: </b> GreenTank.java
 * @version 1.0
 * @since 10.10.2018
 * <p><b>Last modification date: </b> 10.10.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a green enemy tank for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is a very basic tank, it does
 * not move unless pushed by another tank and it has a GreenTurret on it.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class GreenTank extends Tank
{
	/**
	 * Makes a new Green Tank, with it's start x and y coordinates the ones given
	 * as arguments.
	 * @param startX The starting x coordinate of this tank.
	 * @param startY The starting y coordinate of this tank.
	 * @param startRotation The starting rotation of this tank in the world.
	 */
	public GreenTank(int startX, int startY, int startRotation)
	{
		//simply calls the constructor of the superclass.
		super(startX,startY, startRotation);
	}
	
	/**
	 * Overrides the superclass addedToWorld method so that a Green Turret will be 
	 * placed on this tank not a simple Turret object.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		setRotation(startRotation);
		tankTurret=new GreenTurret(this);
	}
	
	/**
	 * Overrides the default isMoving method because a Green Tank is a basic
	 * enemy and it does not move at all, unless pushed by another tank. This 
	 * method returns false always.
	 * @return false.
	 */
	@Override
	public boolean isMoving()
	{
		return false;
	}
	
	/**
	 * Checks if the tank is moving forward.
	 * @return False, because this type of the tank is stationary.
	 */
	@Override
	public boolean isMovingForward()
	{
		return false;
	}
}
