import greenfoot.World;

/**
 * <p><b>File name: </b> GreenTank.java
 * @version 1.0
 * @since 10.10.2018
 * <p><b>Last modification date: </b> 10.10.2018
 * @author Alexandru F. Dascalu
 * 
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>GreenTank.java is part of Panzer Batallion.
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
 * <p> This class models a green enemy tank for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is a very basic tank, it does
 * not move unless pushed by another tank and it has a GreenTurret on it.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class GreenTank extends StaticEnemyTank
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
	 * @param world The game world this tank has just been added to.
	 */
	@Override
	protected void addedToWorld(World world)
	{
		setRotation(startRotation);
		tankTurret=new GreenTurret(this);
	}
}
