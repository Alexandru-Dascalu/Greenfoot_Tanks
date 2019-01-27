import greenfoot.World;

/**
 * <p><b>File name: </b> TurquoiseTank.java
 * @version 1.1
 * @since 01.08.2018
 * <p><b>Last modification date: </b> 26.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>TurquoiseTank.java is part of Panzer Batallion.
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
 * <p> This class models a turquoise enemy tank for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is the most basic mobile enemy 
 * tank, and is a subclass of the MobileEnemyTank class. It moves randomly,
 * avoids walls, and has a TurquoiseTurret which fires shells at the player
 * tank when it can hit it.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Moved of the code to a new superclass called MobileEnemyTank,
 * which handles the general behaviour of a moving enemy tank.
 */

public class TurquoiseTank extends MobileEnemyTank
{
	/**The distance measured in cell-size units by which this tank moves each 
	 * time it acts. Its value is {@value}.*/
    private static final int SPEED=1;
    
    /**The maximum number of degrees by which this tank can turn each time the
     * act() method is called. Its value is {@value}.*/
    private static final int MAX_TURN_SPEED=3;
    
    /**
     * A real number that will be multiplied by the length of this 
     * tank and cast as an integer to get the mine avoidance distance of this
     * tank. Its value is {@value}.
     */
    private static final double MINE_LENGTH_MULTIPLIER = 1.7;
    
    /**
     * A real number that will be multiplied by the length of this 
     * tank and cast as an integer to get the shell avoidance distance of this
     * tank. Its value is {@value}.
     */
    private static final double SHELL_LENGTH_MULTIPLIER = 1.8;

    /**
     * Make a new TurquoiseTank whose starting position will be at the given
     * coordinates.
     * @param startX The x coordinate the tank will be at the beginning of the
     * level.
     * @param startY The y coordinate the tank will be at the beginning of the
     * level.
	 * @param startRotation The starting rotation of this tank in the world.
     */
    public TurquoiseTank(int startX, int startY, int startRotation)
    {
    	//simply calls the constructor of the superclass.
        super(startX, startY, startRotation);
    }
    
    /**
   	 * Prepares the turquoise tank for the beginning of the game. Sets the correct
   	 * starting orientation and gives this tank a TurquoiseTurret.
   	 */
    @Override
    protected void addedToWorld(World world)
    {
    	setRotation(startRotation);
    	tankTurret=new TurquoiseTurret(this);
    }
  
    /**
	 * Getter for the speed of this tank, meaning the distance in cells that 
	 * the tank move each time the move(int) method is called.
	 * @return 	The speed of this type of tank tank.
	 */
    @Override
    public int getSpeed()
    {
    	return SPEED;
    }
    
    /**
	 * The number of mines this tank can lay in one level.
	 * @return The number of mines this tank can lay, which is 0, since this 
	 * type of tank does not lay mines.
	 */
	public int getNumberOfMines()
	{
		return 0;
	}
    
    /**
	 * Getter The maximum number of degrees by which this tank can turn each 
	 * time the act() method is called.
	 * @return 	The maximum turn speed of this type of tank.
	 */
    @Override
    public int getMaxTurnSpeed()
    {
    	return MAX_TURN_SPEED;
    }
    
    /**
     * Indicates the safe distance this type of tank will keep from a mine when
     * it is avoiding a mine.
     * @return the safe distance this type of tank will keep from a mine when
     * it is avoiding a mine.
     */
    @Override
    public double getMineLengthMultiplier()
    {
    	return MINE_LENGTH_MULTIPLIER;
    }
    
    /**
     * Indicates the safe distance this type of tank will keep from a shell when
     * it is avoiding a mine.
     * @return the safe distance this type of tank will keep from a shell when
     * it is avoiding a mine.
     */
    @Override
    public double getShellLengthMultiplier()
    {
    	return SHELL_LENGTH_MULTIPLIER;
    }
}
