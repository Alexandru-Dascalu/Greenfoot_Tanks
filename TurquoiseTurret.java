/**
 * <p><b>File name: </b> TurquoiseTurret.java
 * @version 1.0
 * @since 14.08.2018
 * <p><b>Last modification date: </b> 14.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>TurquoiseTurret.java is part of Panzer Batallion.
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
 * <p> This class models a turquoise enemy turret for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It strongly follows the position of 
 * the  player tank and fires at it if it can hit it. It cannot fire another 
 * shell if the cool down period has not passed and if there is another shell 
 * fired by this turret still in the world.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class TurquoiseTurret extends EnemyTurret
{
	/**The cooldown period in miliseconds after firing which the turret will not 
	 * fire again. It's value is {@value}.*/
	private static final int FIRE_COOLDOWN=1000;
	
	/**The maximum number of shells that the turret has fired that can be in the 
	 * world at the same time. It's value is {@value}.*/
	private static final int LIVE_SHELLS_ALLOWED=1;
	
	/**The size in degrees of the angle of an imaginary cone whose axis extends
	 * to the position of the player tank. In this angle the turret moves 
	 * randomly. It's value is {@value}.*/
	private static final int AIM_ANGLE=35;
	
	/**
	 * Makes a new Turquoise Turret on the Tank given as an argument.
	 * @param tank The tank on which this Turret will be placed.
	 */
	public TurquoiseTurret(Tank tank)
	{
		//just call the supertype constructor
		super(tank);
	}
	
	/**Gets the cool down period(in milliseconds) after which this turret can 
	 * fire another shell. This period is a static variable and is the same for
	 * all objects of this class.
	 * @return The period in milliseconds after which this turret can fire another
	 * shell.*/
	@Override
	public int getFireCooldown()
	{
		return FIRE_COOLDOWN;
	}
	
	/**Gets the limit of how many shells fired by this turret can be in the world
	 * at the same time. This number is a static variable and is the same for
	 * all objects of this class.
	 * @return the limit of how many shells fired by this turret can be in the world
	 * at the same time. */
	@Override
	public int getLiveShellLimit()
	{
		return LIVE_SHELLS_ALLOWED;
	}
	
	/**
	 * Indicates the type of shell fired by this turret.
	 * @return The type of shell fired by this turret, indicated by a Class 
	 * object.
	 */
	@Override
	public Class<? extends Shell> getShellType()
    {
    	return RocketShell.class;
    }
	
	/**
	 * Gets the size in degrees of the angle of an imaginary cone whose axis extends
	 * to the position of the player tank. In this angle the turret moves 
	 * randomly.
	 * @return The aim angle of this type of turret in relation to the player tank.
	 */
	public int getAimAngle()
	{
		return AIM_ANGLE;
	}
}
