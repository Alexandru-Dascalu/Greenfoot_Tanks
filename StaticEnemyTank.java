/**
 * <p><b>File name: </b> StaticEnemyTank.java
 * @version 1.0
 * @since 20.10.2018
 * <p><b>Last modification date: </b> 20.10.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.* <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>StaticEnemyTank.java is part of Panzer Batallion.
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
 * <p> This class models a static enemy tank for a Greenfoot recreation of the 
 * Wii Tanks game for the Nintendo Wii. It is a tank that does not move and does 
 * nothing except make sure it is not being pushed into a wall by another tank
 * and telling it's turret to aim and fire.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */
public abstract class StaticEnemyTank extends Tank
{
	 /** Makes a new Static Enemy Tank whose starting location in the level is at
	 * the given coordinates.
	 * @param startX The x coordinate of the initial position of the tank in 
	 * the level.
	 * @param startY The y coordinate of the initial position of the tank in 
	 * the level.
	 * @param startRotation The starting rotation of this tank in the world.
	 */
    public StaticEnemyTank(int startX, int startY, int startRotation)
    {
    	//call superclass constructor
        super(startX, startY, startRotation);
    }
    
    /**
	 * Overrides the default isMoving method because a Brown Tank is a very basic
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
	
	/**
	 * Checks if the tank is moving backward.
	 * @return False, because this type of the tank is stationary.
	 */
	@Override
	public boolean isMovingBackward()
	{
		return false;
	}
	
	/**
	 * The speed of this tank, meaning the distance in cells that the tank moves
	 * each time the move(int) method is called.
	 * @return 0, since static tanks do not move.
	 */
	public int getSpeed()
	{
		return 0;
	}
	
	/**
	 * The number of mines this tank can lay in one level.
	 * @return 0, since tanks that do not move can not lay mines.
	 */
	public int getNumberOfMines()
	{
		return 0;
	}
	
	/**
	 * Getter The maximum number of degrees by which this tank can turn each 
	 * time the act() method is called. Used by mobile enemy tanks and the 
	 * player tank.
	 * @return 	0, since static tanks do not turn or move.
	 */
    public int getMaxTurnSpeed()
    {
    	return 0;
    }
	
}
