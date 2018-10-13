
/**
 * <p><b>File name: </b> RocketShell.java
 * @version 1.0
 * @since 06.09.2018
 * <p><b>Last modification date: </b> 07.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>RocketShell.java is part of Panzer Batallion.
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
 * <p> This class models a tank rockets shell for a Greenfoot recreation of the 
 * Wii Tanks game for the Nintendo Wii. It moves faster than the normal 
 * shells, does not bounce when hitting a wall or the boundary of the world and
 * destroys tanks and other shells that it hits. It also makes landmines that 
 * it intersects explode, but that is handled in the LandMine class.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class RocketShell extends Shell
{
	/**The speed with which all shells move. The value is {@value}.*/
    private static final int SHELL_SPEED=6;
    
    /**The number of times the shell is allowed to bounce off a wall.The value
     *  is {@value}.*/
    public static final int TIMES_ALLOWED_TO_BOUNCE=0;
    
    /**The average distance at which the shell looks ahead to check if it will
     * hit a wall. The value is {@value}, because the shell image is 37 pixels
     * long, and it is set slightly more than half so shells will not disappear
     * too far away from the wall.*/
    private static final int LOOK_AHEAD=20;
    
    /**
     * Makes a new rocket shell at the given coordinates and with the rotation
     * given.
     * @param rotation The rotation of the new shell, the same as the rotation 
     * of the turret that fired it when it was fired.
     * @param parent The tank whose turret fired this shell.
     * @param x The x coordinate where the new shell will be put.
     * @param y The y coordinate where the new shell will be put.
     */
	public RocketShell(int rotation, Tank parent, int x, int y)
	{
		//just call the superclass constructor
		super(rotation, parent, x ,y);
	}
	
	/**
	 * The speed of this shell, meaning the distance in cells that the shell moves
	 * each time the move(int) method is called.
	 * @return the speed of this type of shells, defined by a static constant.
	 */
	public int getSpeed()
    {
    	return SHELL_SPEED;
    }
    
	/**
     * Gets the number of times the shell is allowed to bounce off a wall. We 
     * have this method despite the fact it returns a public constant so that 
     * we can override for different types of shells.
     * @return the number of times the shell is allowed to bounce off a wall.
     */
    public int getBounceLimit()
    {
    	return TIMES_ALLOWED_TO_BOUNCE;
    }
    
    /**
     * Gets the average distance at which the shell looks ahead to check if 
     * it will hit a wall.
     * @return the average distance at which the shell looks ahead to check if 
     * it will hit a wall.
     */
    public int getLookAhead()
    {
    	return LOOK_AHEAD;
    }
}
