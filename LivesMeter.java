/**
 * <p><b>File name: </b> LivesMeter.java
 * @version 1.0
 * @since 13.06.2018
 * <p><b>Last modification date: </b> 14.06.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>LivesMeter.java is part of Panzer Batallion.
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
 * <p> This class models UI display for the number of lives the player has for 
 * a Greenfoot recreation of the Wii Tanks game for the Nintendo Wii. It has an
 * image over which a text is displayed , showing how many lives the player has 
 * left. When the player tank is destroyed, the counter changes accordingly.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created a counter for the number lives the player has left.
 */

public class LivesMeter extends UIDisplay
{
	/**The name of the image over which the next will be displayed.*/
	private static final String TRAPEZOID_IMAGE="red_trapezoid.png";
	
	/**The part of the displayed text that remains the same always.*/
	private static final String TEXT="Player lives: ";
	
	/**The x coordinate inside the image where the number of player lives left
	 * will be placed. Its value is {@value}.*/
	private static final int TEXT_XPOS=20;
	
	/**The y coordinate inside the image where the number of player lives left
	 * will be placed. Its value is {@value}.*/
	private static final int TEXT_YPOS=10;
	
	/**Overrides the default method so that the updated data displays the new
	 * number of lives the player has left.*/
	@Override
	public int getNewData()
	{
		TankWorld world=getWorldOfType(TankWorld.class);
		return world.getPlayerLives();
	}
	
	/**
	 * Returns the correct name of the image file for the player lives counter, 
	 * so that the correct image will be displayed.
	 * @return The name of the image file for the player lives counter UI display.
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
	 * @return The x coordinate where the 
	 */
	@Override
    public int getTextYPos()
    {
    	return TEXT_YPOS;
    }
}

