import greenfoot.*;

/**
 * <p><b>File name: </b> EnemyTank.java
 * @version 1.0
 * @since 13.06.2018
 * <p><b>Last modification date: </b> 14.06.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
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
	 * will be placed.*/
	private static final int TEXT_XPOS=38;
	
	/**The y coordinate inside the image where the number of enemy tanks left
	 * will be placed.*/
	private static final int TEXT_YPOS=8;
	
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
