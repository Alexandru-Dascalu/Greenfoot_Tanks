import greenfoot.*;

/**
 * <p><b>File name: </b> BrownTank.java
 * @version 1.2
 * @since 07.06.2018
 * <p><b>Last modification date: </b> 24.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>BrownTank.java is part of Panzer Batallion.
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
 * <p> This class models a brown enemy tank for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is the most basic enemy tank,
 * and is a subclass of the Tank class. It differs from a normal Tank because
 * it does not move and it has a turret of the class BrownTurret.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Added an explicit constructor to set the x and y values of it's 
 * starting position.
 * <p>	-1.2 - Changed the class so brown tanks can be pushed by other tanks.
 */

public class BrownTank extends StaticEnemyTank
{
	/**
	 * Makes a new Brown Tank, with it's start x and y coordinates the ones given
	 * as arguments.
	 * @param startX The starting x coordinate of this tank.
	 * @param startY The starting y coordinate of this tank.
	 * @param startRotation The starting rotation of this tank in the world.
	 */
	public BrownTank(int startX, int startY, int startRotation)
	{
		//simply calls the constructor of the superclass.
		super(startX,startY, startRotation);
	}
	
	/**
	 * Overrides the superclass addedToWorld method so that a Brown Turret will be 
	 * placed on this tank not a simple Turret object.
	 * @param world The game world this tank has just been added to.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		setRotation(startRotation);
		tankTurret=new BrownTurret(this);
	}
}
