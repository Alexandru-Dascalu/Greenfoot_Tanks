import greenfoot.*;

/**
 * <p><b>File name: </b> TargetLine.java
 * 
 * @version 1.1
 * @since 01.06.2018
 * <p><b>Last modification date: </b> 11.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>TargetLine.java is part of Panzer Batallion.
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
 * <p>This class models a target line for a Greenfoot recreation of the Wii
 * Tanks game for the Nintendo Wii. It is a small segment of an
 * interrupted line between the player's turret and the player target.
 * It moves when the player target and the turret move and has the same
 * rotation as the turret. There are a set number of these actors, and
 * the distance between each one increases when the distance between the
 * player turret and the target.
 * 
 * <p><b>Version History</b>
 * <p> -1.0 - Created the class.
 * <p> -1.1 - Fixed a bug that would make the target lines be placed in the
 * wrong place if the game was reset inside Greenfoot.
 */

public class TargetLine extends Actor
{
	/**
	 * The number of target line actors that will form an interrupted line to the
	 * player target. Its values is {@value}.
	 */
	public static final int NR_LINES = 9;

	/**
	 * The player's turret. Static because all target lines need to be aligned with
	 * this turret.
	 */
	private static PlayerTurret PLAYER_TURRET;

	/**
	 * The player's target. Static because all target lines need to be aligned with
	 * this it.
	 */
	private static Target PLAYER_TARGET;

	/**
	 * The number of this object in the interrupted target line. Used to place each
	 * TargetLine actor in the right place.
	 */
	private final int number;

	/**
	 * Makes a new TargetLine actor for the turret and target given.
	 * 
	 * @param playerTurret The player's turret that this TargetLine will be aligned with.
	 * @param playerTarget The player's target that this TargetLine will be aligned with.
	 * @param number The number of this TargetLine object in the line between the
	 * turret and the target.
	 */
	public TargetLine(PlayerTurret playerTurret, Target playerTarget, int number)
	{
		// initialise the instance variables with the arguments given
		PLAYER_TURRET = playerTurret;
		PLAYER_TARGET = playerTarget;
		this.number = number;
	}

	/**
	 * Act - do whatever the TargetLine wants to do. This method is called whenever
	 * the 'Act' or 'Run' button gets pressed in the environment. In this case, it
	 * ensures the TargetLine has the same orientation as the player turret and it
	 * is in the correct place.
	 */
	@Override
	public void act()
	{
		// set the rotation to that of the player turret
		setRotation(PLAYER_TURRET.getRotation());

		/*Set the location to the updated coordinates, so that they form an interrupted
		 * line between the player turret and the target.*/
		setLocation(getNewX(), getNewY());
	}

	/**
	 * Calculates the new x position the TargetLine should have.
	 * 
	 * @return The updated x coordinate this actor should have.
	 */
	public int getNewX()
	{
		/*Another method calculates the distance between TargetLine objects, and here
		 * we calculated the distance between TargetLine actors on the x axis. */
		int xInterval = (int) (getInterval() * Math.cos(Math.toRadians(PLAYER_TURRET.getRotation())));

		/*The new x position should be the turret's x position added with a distance
		 * made from the xInterval multiplied by the number of this TargetLine (1 if it
		 * is the first TargetLine from the turret, 2 if it is the second, etc.).*/
		int xLine = PLAYER_TURRET.getX() + number * xInterval;
		return xLine;
	}

	/**
	 * Calculates the new y position the TargetLine should have.
	 * 
	 * @return The updated y coordinate this actor should have.
	 */
	public int getNewY()
	{
		/*Another method calculates the distance between TargetLine objects, and here
		 * we calculated the distance between TargetLine actors on the y axis.*/
		int yInterval = (int) (getInterval() * Math.sin(Math.toRadians(PLAYER_TURRET.getRotation())));

		/*The new y position should be the turret's x position added with a distance
		 * made from the yInterval multiplied by the number of this TargetLine (1 if it
		 * is the first TargetLine from the turret, 2 if it is the second, etc.).*/
		int yLine = PLAYER_TURRET.getY() + number * yInterval;
		return yLine;
	}

	/**
	 * Calculates the distance that should be between TragetLine actors. This
	 * distance increases proportionally with the distance between the turret and
	 * the player target.
	 * 
	 * @return The updated interval distance between TargetLine actors.
	 */
	private int getInterval()
	{
		/*Calculate the length of the legs of the imaginary right-angled triangle whose
		 * hypotenuse is the distance between the turret and the player target.*/
		int leg1 = PLAYER_TARGET.getX() - PLAYER_TURRET.getX();
		int leg2 = PLAYER_TARGET.getY() - PLAYER_TURRET.getY();

		/* Calculate the distance between the turret and the target using Pythagora's
		 * theorem.*/
		int distance = (int) Math.sqrt((leg1 * leg1) + (leg2 * leg2));

		/*The interval is a tenth (or NR_LINES+1, because there are no TargetLine
		 * objects at either end of the imaginary line, so there is one more space than
		 * the number of TargetLine objects) of the distance between the turret and
		 * target.*/
		int interval = distance / (NR_LINES + 1);
		return interval;
	}
}

