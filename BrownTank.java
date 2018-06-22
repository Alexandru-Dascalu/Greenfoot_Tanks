import greenfoot.*;

/**
 * <p><b>File name: </b> BrownTank.java
 * @version 1.1
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 21.06.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a brown enemy tank for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is the most basic enemy tank,
 * and is a subclass of the Tank class. It differs from a normal Tank because
 * it does not move and it has a turret of the class BrownTurret.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Added an explicit constructor to set the x and y values of it's 
 * starting position.
 */

public class BrownTank extends Tank
{
	/**
	 * Makes a new Brown Tank, with it's start x and y coordinates the ones given
	 * as arguments.
	 * @param startX The starting x coordinate of this tank.
	 * @param startY The starting y coordinate of this tank.
	 */
	public BrownTank(int startX, int startY)
	{
		//simply calls the constructor of the superclass.
		super(startX,startY);
	}
	
	/**
	 * Overrides the default isMoving method because a Brown Tank is a very basic
	 * enemy and it does not move at all, unless pushed by another tank. This 
	 * method returns false always.
	 */
	@Override
	public boolean isMoving()
	{
		return false;
	}
	
	/**
	 * Overrides the superclass addedToWorld method so that a Brown Turret will be 
	 * placed on this tank not a simple Turret object.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		this.setRotation(180);
		tankTurret=new BrownTurret(this);
	}
}
