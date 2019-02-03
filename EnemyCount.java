/**
 * <p><b>File name: </b> EnemyTank.java
 * @version 1.0
 * @since 13.06.2018
 * <p><b>Last modification date: </b> 14.06.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.<p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>EnemyCount.java is part of Panzer Batallion.
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
 * <p> This class models UI display for the number of enemies in the level for 
 * a Greenfoot recreation of the Wii Tanks game for the Nintendo Wii. It has an
 * image over which a text is displayed , showing the number of enemy tanks still
 * currently in the level. When an enemy is destroyed or the level is reloaded
 * or changed, the counter updates accordingly.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created a counter for the number of enemy tanks.
 */

public class EnemyCount extends UIDisplay
{
	/**The name of the image over which the next will be displayed.*/
	private static final String TRAPEZOID_IMAGE="blue_trapezoid.png";
	
	/**The part of the displayed text that remains the same always.*/
	private static final String TEXT="Enemy tanks: ";
	
	/**The x coordinate inside the image where the number of enemy tanks left
	 * will be placed. Its value is {@value}.*/
	private static final int TEXT_XPOS=50;
	
	/**The y coordinate inside the image where the number of enemy tanks left
	 * will be placed. Its value is {@value}.*/
	private static final int TEXT_YPOS=12;
	
	/**Overrides the default method so that the updated data displays the new
	 * number of enemy tanks in the level.*/
	@Override
	public int getNewData()
	{
		TankWorld world=getWorldOfType(TankWorld.class);
		return world.getNrEnemyTanks();
	}
	
	/**
	 * Returns the correct name of the image file for the enemy count, so that
	 * the correct image will be displayed.
	 * @return The name of the image file for the enemy counter UI display.
	 */
	@Override
	public String getShapeImage()
	{
		return TRAPEZOID_IMAGE;
	}
	
	/**
	 * Returns the correct text to be displayed on the enemy count UI element.
	 * @return The text to be displayed on the image of the enemy counter.
	 */
	@Override
	public String getText()
	{
		return TEXT;
	}
	
	/**
	 * Returns the correct x coordinate where the updated text should be displayed.
	 * @return The x coordinate where the 
	 */
	@Override
	public int getTextXPos()
    {
    	return TEXT_XPOS;
    }
    
	/**
	 * Returns the correct y coordinate where the updated text should be displayed.
	 * @return The y coordinate where the 
	 */
	@Override
    public int getTextYPos()
    {
    	return TEXT_YPOS;
    }
}

