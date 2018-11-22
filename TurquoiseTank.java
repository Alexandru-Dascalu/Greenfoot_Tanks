import greenfoot.*;

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
	 * time it acts. It's value is {@value}.*/
    private static final int SPEED=1;
    
    /**The maximum number of degrees by which this tank can turn each time the
     * act() method is called. It's value is {@value}.*/
    private static final int MAX_TURN_SPEED=3;
    
    /**The safe distance the tank will keep from a mine when it is avoiding
     * a mine. It's value is {@value}.*/
    private static final int MINE_AVOIDANCE_DISTANCE=(int)(1.7*LENGTH);
    
    /**The distance of a shell to the tank from which the tank will start 
     * making evasive moves. It's value is {@value}.*/
    private static final int SHELL_AVOIDANCE_DISTANCE=(int)(1.8*LENGTH);
    
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
    public int getMineAvoidanceDistance()
    {
    	return MINE_AVOIDANCE_DISTANCE;
    }
    
    /**
     * Indicates the distance from which the tank starts evasive action to avoid
     * an incoming shell.
     * @return 0, unless overriden, since this class should always be extended 
     * and there is no set behaviour for a default mobile enemy tank.
     */
    public int getShellAvoidanceDistance()
    {
    	return SHELL_AVOIDANCE_DISTANCE;
    }
}
